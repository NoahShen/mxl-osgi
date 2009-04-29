/**
 * 
 */
package net.sf.mxlosgi.mxlosgifiletransferbundle.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.mxlosgi.mxlosgidataformsbundle.DataForm;
import net.sf.mxlosgi.mxlosgidataformsbundle.FormField;
import net.sf.mxlosgi.mxlosgidataformsbundle.FormField.Option;
import net.sf.mxlosgi.mxlosgifiletransferbundle.FileTransferRequest;
import net.sf.mxlosgi.mxlosgifiletransferbundle.ReceiveFileTransfer;
import net.sf.mxlosgi.mxlosgifiletransferbundle.ReceiveFileTransferFactory;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgistreaminitiationbundle.StreamInitiation;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.XMPPError;

/**
 * @author noah
 * 
 */
public class FileTransferRequestImpl implements FileTransferRequest
{

	private JID senderJID;

	private String stanzaID;

	private Map<String, ReceiveFileTransferFactory> receiveFileTransferFactories;
	
	private XMPPConnection connection;
	
	private boolean handle = false;

	private StreamInitiation streamInitiation;
	
	/**
	 * @param fileName
	 * @param fileSize
	 * @param senderJID
	 * @param streamID
	 */
	public FileTransferRequestImpl(Map<String, ReceiveFileTransferFactory> receiveFileTransferFactories, 
							StreamInitiation streamInitiation,
							JID senderJID, 
							String stanzaID,
							XMPPConnection connection)
	{
		this.receiveFileTransferFactories = receiveFileTransferFactories;
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
		StreamInitiation.XMPPFile f = streamInitiation.getFile();
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
		StreamInitiation.XMPPFile f = streamInitiation.getFile();
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
		StreamInitiation.XMPPFile f = streamInitiation.getFile();
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
		StreamInitiation.XMPPFile f = streamInitiation.getFile();
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
		StreamInitiation.XMPPFile f = streamInitiation.getFile();
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
			for (Iterator<Option> i = field.getOptions(); i.hasNext();)
			{
				Option option = i.next();
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
		StreamInitiation.XMPPFile f = streamInitiation.getFile();
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
			throw new XMPPException("The request has been handled");
		}
		
		handle = true;
		ReceiveFileTransferFactory factory = receiveFileTransferFactories.get(streamMethod);
		if (factory == null)
		{
			IQ iq = new IQ(IQ.Type.error);
			iq.setStanzaID(stanzaID);
			iq.setTo(senderJID);
			
			XMPPError error = new XMPPError(XMPPError.Condition.bad_request);
			error.addOtherCondition("no-valid-streams", "http://jabber.org/protocol/si");
			error.setCode(400);
			error.setType(XMPPError.Type.CANCEL);
			
			iq.setError(error);
			
			connection.sendStanza(iq);
			
			throw new XMPPException("stream method is not supported yet");
		}
		
		return factory.createReceiveFileTransfer(senderJID, stanzaID, streamInitiation, connection, null);
	}

	@Override
	public synchronized void reject() throws Exception
	{
		if (handle)
		{
			throw new XMPPException("The request has been handled");
		}
		
		IQ iq = new IQ(IQ.Type.error);
		iq.setStanzaID(stanzaID);
		iq.setTo(senderJID);
		
		XMPPError error = new XMPPError(XMPPError.Condition.forbidden);		
		iq.setError(error);
		
		connection.sendStanza(iq);
		
		handle = true;
	}

	@Override
	public XMPPConnection getConnection()
	{
		return connection;
	}

}
