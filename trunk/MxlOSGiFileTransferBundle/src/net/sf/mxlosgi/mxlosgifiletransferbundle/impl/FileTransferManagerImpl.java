/**
 * 
 */
package net.sf.mxlosgi.mxlosgifiletransferbundle.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.mxlosgi.mxlosgidataformsbundle.DataForm;
import net.sf.mxlosgi.mxlosgidataformsbundle.FormField;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoManager;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoPacketExtension;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoItemsManager;
import net.sf.mxlosgi.mxlosgifiletransferbundle.FileTransferManager;
import net.sf.mxlosgi.mxlosgifiletransferbundle.ReceiveFileTransferFactory;
import net.sf.mxlosgi.mxlosgifiletransferbundle.SendFileTransfer;
import net.sf.mxlosgi.mxlosgifiletransferbundle.SendFileTransferFactory;
import net.sf.mxlosgi.mxlosgifiletransferbundle.listener.FileTransferListener;
import net.sf.mxlosgi.mxlosgimainbundle.ServerTimeoutException;
import net.sf.mxlosgi.mxlosgimainbundle.StanzaCollector;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgimainbundle.filter.PacketIDFilter;
import net.sf.mxlosgi.mxlosgimainbundle.listener.StanzaReceListener;
import net.sf.mxlosgi.mxlosgistreaminitiationbundle.StreamInitiation;
import net.sf.mxlosgi.mxlosgiutilsbundle.DateTimeUtils;
import net.sf.mxlosgi.mxlosgiutilsbundle.StringUtils;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class FileTransferManagerImpl implements FileTransferManager, StanzaReceListener
{
	
	private DiscoInfoManager discoInfoManager;
	
	private DiscoItemsManager discoItemsManager;
	
	private final List<FileTransferListener> fileTransferListeners = new CopyOnWriteArrayList<FileTransferListener>();
	
	private final List<JID> xmppFileTransferProxies = new CopyOnWriteArrayList<JID>();
	
	private final Map<String, SendFileTransferFactory> sendFileTransferFactories = 
		new ConcurrentHashMap<String, SendFileTransferFactory>();

	private final Map<String, ReceiveFileTransferFactory> receiveFileTransferFactories = 
		new ConcurrentHashMap<String, ReceiveFileTransferFactory>();
	
	
	/**
	 * @param discoInfoManager
	 * @param discoItemsManager
	 */
	public FileTransferManagerImpl(DiscoInfoManager discoInfoManager, DiscoItemsManager discoItemsManager)
	{
		this.discoInfoManager = discoInfoManager;
		this.discoItemsManager = discoItemsManager;
	}
	
	@Override
	public void addFileTransferListener(FileTransferListener listener)
	{
		fileTransferListeners.add(listener);
	}

	@Override
	public void addProxy(JID proxy)
	{
		xmppFileTransferProxies.add(proxy);
	}

	@Override
	public void addReceiveFileTransferFactory(ReceiveFileTransferFactory receiveFileTransferFactory)
	{
		receiveFileTransferFactories.put(receiveFileTransferFactory.getStreamMethod(), receiveFileTransferFactory);
	}

	@Override
	public void addSendFileTransferFactory(SendFileTransferFactory sendFileTransferFactory)
	{
		sendFileTransferFactories.put(sendFileTransferFactory.getStreamMethod(), sendFileTransferFactory);
	}

	@Override
	public SendFileTransfer createSendFileTransfer(XMPPConnection connection, JID to, File file) throws FileNotFoundException, XMPPException
	{
		return createSendFileTransfer(connection, to, file, null, null);
	}

	@Override
	public SendFileTransfer createSendFileTransfer(XMPPConnection connection, JID to, String filepath) throws FileNotFoundException, XMPPException
	{
		return createSendFileTransfer(connection, to, new File(filepath));
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgifiletransferbundle.FileTransferManager#createSendFileTransfer(java.lang.String, java.io.File, java.lang.String)
	 */
	@Override
	public SendFileTransfer createSendFileTransfer(XMPPConnection connection, JID to, File file, String description, String mimeType) throws FileNotFoundException, XMPPException
	{
		if (sendFileTransferFactories.isEmpty())
		{
			throw new XMPPException("stream-method is empty");
		}
		if (!file.exists())
		{
			throw new FileNotFoundException("file is not exist");
		}
		if (!file.isFile())
		{
			throw new IllegalArgumentException("not a file");
		}
		
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
			throw new XMPPException("the other side don't support filetransfer");
		}
		
		IQ iq = new IQ(IQ.Type.set);
		iq.setTo(to);
		
		StreamInitiation si = new StreamInitiation();
		si.setMimeType(mimeType);
		
		StreamInitiation.XMPPFile xmppFile = new StreamInitiation.XMPPFile(file.getName(), file.length());		
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
		for (Iterator<String> it = sendFileTransferFactories.keySet().iterator(); it.hasNext();)
		{
			field.addOption(new FormField.Option(it.next()));
		}
		dataForm.addField(field);
		
		
		si.setFeatureForm(dataForm);
		
		iq.addExtension(si);
		
	
		
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XMLStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (stanza == null)
		{
			throw new ServerTimeoutException("no response from the other");
		}
		if (stanza instanceof IQ)
		{
			IQ iqResponse = (IQ) stanza;
			IQ.Type type = iqResponse.getType();
			if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
			else if (type == IQ.Type.result)
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
							SendFileTransferFactory sendFileTransferFactory = sendFileTransferFactories.get(value);
							if (sendFileTransferFactory != null)
							{
								Map<String, Object> properties = new HashMap<String, Object>();
								properties.put("DiscoInfoManager", discoInfoManager);
								properties.put("DiscoItemsManager", discoItemsManager);
								return sendFileTransferFactory.createSendFileTransfer(file, to, si.getId(), siResponse, this, connection, properties);
							}
							else
							{
								throw new XMPPException("the stream method has not been supported yet");
							}
						}
					}
					
				}
			}
		}
		
		return null;
	}

	@Override
	public JID[] getProxies()
	{
		return this.xmppFileTransferProxies.toArray(new JID[]{});
	}

	@Override
	public void removeFileTransferListener(FileTransferListener listener)
	{
		fileTransferListeners.remove(listener);
	}

	@Override
	public void removeProxy(JID proxy)
	{
		xmppFileTransferProxies.remove(proxy);
	}

	@Override
	public void removeReceiveFileTransferFactory(ReceiveFileTransferFactory receiveFileTransferFactory)
	{
		receiveFileTransferFactories.remove(receiveFileTransferFactory.getStreamMethod());
	}

	@Override
	public void removeSendFileTransferFactory(SendFileTransferFactory sendFileTransferFactory)
	{
		sendFileTransferFactories.remove(sendFileTransferFactory.getStreamMethod());
	}

	@Override
	public void processReceStanza(XMPPConnection connection, XMLStanza data)
	{
		IQ iq = (IQ) data;
		StreamInitiation streamInitiation = (StreamInitiation) iq.getExtension("si", "http://jabber.org/protocol/si");
		FileTransferRequestImpl request
					= new FileTransferRequestImpl(receiveFileTransferFactories,
											streamInitiation,
											iq.getFrom(),
											iq.getStanzaID(),
											connection);
		fireFileTransferRequest(request);
	}
	private void fireFileTransferRequest(FileTransferRequestImpl request)
	{
		for (FileTransferListener listener : fileTransferListeners)
		{
			listener.fileTransferRequest(request);
		}
	}
}
