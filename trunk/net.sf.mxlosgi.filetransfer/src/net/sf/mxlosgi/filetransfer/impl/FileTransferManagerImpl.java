/**
 * 
 */
package net.sf.mxlosgi.filetransfer.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import net.sf.mxlosgi.core.ServerTimeoutException;
import net.sf.mxlosgi.core.StanzaCollector;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.core.filter.PacketIDFilter;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.dataform.DataForm;
import net.sf.mxlosgi.dataform.FormField;
import net.sf.mxlosgi.disco.DiscoInfoManager;
import net.sf.mxlosgi.disco.DiscoInfoPacketExtension;
import net.sf.mxlosgi.filetransfer.FileTransferManager;
import net.sf.mxlosgi.filetransfer.SendFileTransfer;
import net.sf.mxlosgi.filetransfer.SendFileTransferFactory;
import net.sf.mxlosgi.streaminitiation.StreamInitiation;
import net.sf.mxlosgi.utils.DateTimeUtils;
import net.sf.mxlosgi.utils.StringUtils;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.XmlStanza;


/**
 * @author noah
 *
 */
public class FileTransferManagerImpl implements FileTransferManager, StanzaReceListener
{
	private DiscoInfoManagerServiceTracker discoInfoManagerServiceTracker;
	
	private FileTransferListenerServiceTracker fileTransferListenerServiceTracker;
	
	private SendFileTransferFactoryServiceTracker sendFileTransferFactoryServiceTracker;

	private ReceiveFileTransferFactoryServiceTracker receiveFileTransferFactoryServiceTracker;

	
	
	/**
	 * @param discoInfoManager
	 * @param discoItemsManager
	 */
	public FileTransferManagerImpl(DiscoInfoManagerServiceTracker discoInfoManagerServiceTracker,
								FileTransferListenerServiceTracker fileTransferListenerServiceTracker,
								SendFileTransferFactoryServiceTracker sendFileTransferFactoryServiceTracker,
								ReceiveFileTransferFactoryServiceTracker receiveFileTransferFactoryServiceTracker)
	{
		this.discoInfoManagerServiceTracker = discoInfoManagerServiceTracker;
		this.fileTransferListenerServiceTracker = fileTransferListenerServiceTracker;
		this.sendFileTransferFactoryServiceTracker = sendFileTransferFactoryServiceTracker;
		this.receiveFileTransferFactoryServiceTracker = receiveFileTransferFactoryServiceTracker;
	}
	
	@Override
	public SendFileTransfer createSendFileTransfer(XmppConnection connection, JID to, File file) throws FileNotFoundException, XmppException
	{
		return createSendFileTransfer(connection, to, file, null, null);
	}

	@Override
	public SendFileTransfer createSendFileTransfer(XmppConnection connection, JID to, String filepath) throws FileNotFoundException, XmppException
	{
		return createSendFileTransfer(connection, to, new File(filepath));
	}

	@Override
	public SendFileTransfer createSendFileTransfer(XmppConnection connection, JID to, File file, String description, String mimeType) throws FileNotFoundException, XmppException
	{
		if (sendFileTransferFactoryServiceTracker.size() <= 0)
		{
			throw new XmppException("stream-method is empty");
		}
		if (!file.exists())
		{
			throw new FileNotFoundException("file is not exist");
		}
		if (!file.isFile())
		{
			throw new IllegalArgumentException("not a file");
		}
		
		DiscoInfoManager discoInfoManager = discoInfoManagerServiceTracker.getDiscoInfoManager();
		DiscoInfoPacketExtension discoInfo = discoInfoManager.getDiscoInfo(connection, to);
		
		boolean siSupported = false;
		boolean siFileTransferSupported = false;
		for (DiscoInfoPacketExtension.Feature feature : discoInfo.getFeatures())
		{
			String strFeature = feature.getFeature();
			if ("http://jabber.org/protocol/si".equals(strFeature))
			{
				siSupported = true;
			}
			else if ("http://jabber.org/protocol/si/profile/file-transfer".equals(strFeature))
			{
				siFileTransferSupported = true;
			}
		}
		boolean support =  siSupported && siFileTransferSupported;
		
		if (!support)
		{
			throw new XmppException("the other side don't support filetransfer");
		}
		
		Iq iq = new Iq(Iq.Type.set);
		iq.setTo(to);
		
		StreamInitiation si = new StreamInitiation();
		si.setMimeType(mimeType);
		
		StreamInitiation.XmppFile xmppFile = new StreamInitiation.XmppFile(file.getName(), file.length());		
		xmppFile.setDate(DateTimeUtils.utcFormat(file.lastModified()));
		xmppFile.setRanged(true);
		
		try
		{
			String md5 = StringUtils.getMD5(file);
			xmppFile.setHash(md5);
		}
		catch (IOException e)
		{
		}
		
		if (description != null && !description.isEmpty())
		{
			xmppFile.setDesc(description);
		}
		si.setFile(xmppFile);
		
		DataForm dataForm = new DataForm();
		dataForm.setType(DataForm.Type.submit);
		
		FormField field = new FormField("stream-method");
		field.setType(FormField.TYPE_LIST_SINGLE);
		String[] methods = sendFileTransferFactoryServiceTracker.getStreamMethods();
		for (String method : methods)
		{
			field.addOption(new FormField.Option(method));
		}
		dataForm.addField(field);
		
		
		si.setFeatureForm(dataForm);
		
		iq.addExtension(si);
		
	
		
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XmlStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (stanza == null)
		{
			throw new ServerTimeoutException("no response from the other");
		}
		if (stanza instanceof Iq)
		{
			Iq iqResponse = (Iq) stanza;
			Iq.Type type = iqResponse.getType();
			if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
			else if (type == Iq.Type.result)
			{
				StreamInitiation siResponse = (StreamInitiation) iqResponse.getExtension(si.getElementName(), si.getNamespace());
				DataForm form = siResponse.getFeatureForm();
				for (Iterator<FormField> it = form.getFields().iterator(); it.hasNext();)
				{
					FormField formField = it.next();
					if (formField.getVariable().equals("stream-method"))
					{
						Iterator<String> itValues = formField.getValues();
						if (itValues.hasNext())
						{
							String value = itValues.next();
							SendFileTransferFactory sendFileTransferFactory = sendFileTransferFactoryServiceTracker.getSendFileTransferFactory(value);
							if (sendFileTransferFactory != null)
							{
								return sendFileTransferFactory.createSendFileTransfer(file, to, si.getId(), siResponse, connection, null);
							}
							else
							{
								throw new XmppException("the stream method has not been supported yet");
							}
						}
					}
					
				}
			}
		}
		
		return null;
	}

	@Override
	public void processReceStanza(XmppConnection connection, XmlStanza data)
	{
		Iq iq = (Iq) data;
		StreamInitiation streamInitiation = (StreamInitiation) iq.getExtension("si", "http://jabber.org/protocol/si");
		FileTransferRequestImpl request
					= new FileTransferRequestImpl(receiveFileTransferFactoryServiceTracker,
											streamInitiation,
											iq.getFrom(),
											iq.getStanzaID(),
											connection);
		fileTransferListenerServiceTracker.fireFileTransferRequest(request);
	}
	
}
