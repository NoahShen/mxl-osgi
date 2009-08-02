/**
 * 
 */
package net.sf.mxlosgi.filetransfer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.dataform.DataForm;
import net.sf.mxlosgi.dataform.FormField;
import net.sf.mxlosgi.filetransfer.FileTransferRequest;
import net.sf.mxlosgi.filetransfer.ReceiveFileTransfer;
import net.sf.mxlosgi.filetransfer.ReceiveFileTransferFactory;
import net.sf.mxlosgi.streaminitiation.StreamInitiation;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.XmppError;

/**
 * @author noah
 * 
 */
public class FileTransferRequestImpl implements FileTransferRequest
{

	private JID senderJID;

	private String stanzaID;

	private ReceiveFileTransferFactoryServiceTracker receiveFileTransferFactoryServiceTracker;
	
	private XmppConnection connection;
	
	private boolean handle = false;

	private StreamInitiation streamInitiation;
	
	/**
	 * @param fileName
	 * @param fileSize
	 * @param senderJID
	 * @param streamID
	 */
	public FileTransferRequestImpl(ReceiveFileTransferFactoryServiceTracker receiveFileTransferFactoryServiceTracker, 
							StreamInitiation streamInitiation,
							JID senderJID, 
							String stanzaID,
							XmppConnection connection)
	{
		this.receiveFileTransferFactoryServiceTracker = receiveFileTransferFactoryServiceTracker;
		this.streamInitiation = streamInitiation;
		this.senderJID = senderJID;
		this.stanzaID = stanzaID;
		this.connection = connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgifiletransferbundle.FileTransferRequest#getDescription()
	 */
	@Override
	public String getDescription()
	{
		StreamInitiation.XmppFile f = streamInitiation.getFile();
		if (f != null)
		{
			return f.getDesc();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgifiletransferbundle.FileTransferRequest#getFileName()
	 */
	@Override
	public String getFileName()
	{
		StreamInitiation.XmppFile f = streamInitiation.getFile();
		if (f != null)
		{
			return f.getName();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgifiletransferbundle.FileTransferRequest#getFileSize()
	 */
	@Override
	public long getFileSize()
	{
		StreamInitiation.XmppFile f = streamInitiation.getFile();
		if (f != null)
		{
			return f.getSize();
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgifiletransferbundle.FileTransferRequest#getHash()
	 */
	@Override
	public String getHash()
	{
		StreamInitiation.XmppFile f = streamInitiation.getFile();
		if (f != null)
		{
			return f.getHash();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgifiletransferbundle.FileTransferRequest#getLastModifyDate()
	 */
	@Override
	public String getLastModifyDate()
	{
		StreamInitiation.XmppFile f = streamInitiation.getFile();
		if (f != null)
		{
			return f.getDate();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgifiletransferbundle.FileTransferRequest#getMimeType()
	 */
	@Override
	public String getMimeType()
	{
		return streamInitiation.getMimeType();
	}

	@Override
	public JID getSenderJID()
	{
		return senderJID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgifiletransferbundle.FileTransferRequest#getStreamID()
	 */
	@Override
	public String getStreamID()
	{
		return streamInitiation.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgifiletransferbundle.FileTransferRequest#getStreamMethods()
	 */
	@Override
	public Collection<String> getStreamMethods()
	{
		List<String> streamMethods = new ArrayList<String>();
		DataForm form = streamInitiation.getFeatureForm();
		Iterator<FormField> it = form.getFields().iterator();
		if (it.hasNext())
		{
			FormField field = it.next();
			for (Iterator<FormField.Option> i = field.getOptions(); i.hasNext();)
			{
				FormField.Option option = i.next();
				streamMethods.add(option.getValue());
			}
		}
		return Collections.unmodifiableCollection(streamMethods);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgifiletransferbundle.FileTransferRequest#isRanged()
	 */
	@Override
	public boolean isRanged()
	{
		StreamInitiation.XmppFile f = streamInitiation.getFile();
		if (f != null)
		{
			return f.isRanged();
		}
		return false;
	}

	@Override
	public synchronized ReceiveFileTransfer accept(String streamMethod) throws Exception
	{
		if (handle)
		{
			throw new XmppException("The request has been handled");
		}
		
		handle = true;
		ReceiveFileTransferFactory factory = 
			receiveFileTransferFactoryServiceTracker.getReceiveFileTransferFactory(streamMethod);
		if (factory == null)
		{
			Iq iq = new Iq(Iq.Type.error);
			iq.setStanzaID(stanzaID);
			iq.setTo(senderJID);
			
			XmppError error = new XmppError(XmppError.Condition.bad_request);
			error.addOtherCondition("no-valid-streams", "http://jabber.org/protocol/si");
			error.setCode(400);
			error.setType(XmppError.Type.CANCEL);
			
			iq.setError(error);
			
			connection.sendStanza(iq);
			
			throw new XmppException("stream method is not supported yet");
		}
		
		return factory.createReceiveFileTransfer(senderJID, stanzaID, streamInitiation, connection, null);
	}

	@Override
	public synchronized void reject() throws Exception
	{
		if (handle)
		{
			throw new XmppException("The request has been handled");
		}
		
		Iq iq = new Iq(Iq.Type.error);
		iq.setStanzaID(stanzaID);
		iq.setTo(senderJID);
		
		XmppError error = new XmppError(XmppError.Condition.forbidden);		
		iq.setError(error);
		
		connection.sendStanza(iq);
		
		handle = true;
	}

	@Override
	public XmppConnection getConnection()
	{
		return connection;
	}

}
