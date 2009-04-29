package net.sf.mxlosgi.mxlosgisock5bundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.Properties;

import net.sf.mxlosgi.mxlosgidataformsbundle.DataForm;
import net.sf.mxlosgi.mxlosgidataformsbundle.FormField;
import net.sf.mxlosgi.mxlosgifiletransferbundle.ReceiveFileTransfer;
import net.sf.mxlosgi.mxlosgimainbundle.StanzaCollector;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgimainbundle.filter.StanzaFilter;
import net.sf.mxlosgi.mxlosgisock5bundle.BytestreamPacketExtension.StreamHost;
import net.sf.mxlosgi.mxlosgistreaminitiationbundle.StreamInitiation;
import net.sf.mxlosgi.mxlosgiutilsbundle.StringUtils;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;
import net.sf.mxlosgi.mxlosgixmppbundle.XMPPError;

/**
 * @author noah
 *
 */
public class SOCKS5ReceiveFileTransfer implements ReceiveFileTransfer, Runnable
{
	private String streamMethod;
	
	private File saveFile;
	
	private StreamInitiation.XMPPFile xmppFile;
	
	private JID peerJID;
	
	private String stanzaID;
	
	private StreamInitiation si;
	
	private Status status = Status.inactive;;
	
	private long sizeReceived;
	
	private XMPPConnection connection;
	
	private Error error = Error.none;
	
	private XMPPException exception;
	
	private Long offset;
	
	private Thread receiveThread;
	
	private InputStream in;
	
	public SOCKS5ReceiveFileTransfer(JID peerJID, 
							String stanzaID, 
							StreamInitiation si, 
							XMPPConnection connection)
	{
		this.peerJID = peerJID;
		this.stanzaID = stanzaID;
		this.si = si;
		this.xmppFile = this.si.getFile();
		this.connection = connection;
		
		streamMethod = "http://jabber.org/protocol/bytestreams";
	}

	@Override
	public void receiveFile(File saveFile) throws XMPPException
	{
		if (status == Status.cancelled)
		{
			throw new XMPPException("file transfer has been cancel");
		}
		if (status != Status.inactive)
		{
			throw new XMPPException("file transfer has started");
		}
		if (saveFile.exists())
		{
			status = Status.error;
			throw new XMPPException("file is exist");
		}
		
		this.saveFile = saveFile;
		
		
		receiveThread = new Thread(this);
		receiveThread.start();
		
	}

	@Override
	public void run()
	{
		status = Status.connecting;
		
		if (xmppFile != null && xmppFile.isRanged())
		{
			String strOffset = getTempProperty(xmppFile.getHash(), "offset");
			offset = (strOffset == null ? null : Long.parseLong(strOffset));
			
		}
		IQ iq = new IQ(IQ.Type.result);
		iq.setTo(peerJID);
		iq.setStanzaID(stanzaID);
		
		StreamInitiation streamInitiation = new StreamInitiation();
		streamInitiation.setId(StreamInitiation.ID_NOT_AVAILABLE);
		
		StreamInitiation.XMPPFile xmppFile = new StreamInitiation.XMPPFile();
		
		if (offset != null)
		{
			xmppFile.setRanged(true);
			xmppFile.setOffset(offset != null ? offset : null);
		}
		
		streamInitiation.setFile(xmppFile);
		
		DataForm dataForm = new DataForm();
		dataForm.setType(DataForm.Type.submit);
		
		FormField field = new FormField("stream-method");
		field.addValue("http://jabber.org/protocol/bytestreams");
		
		dataForm.addField(field);
		
		streamInitiation.setFeatureForm(dataForm);
		
		iq.addExtension(streamInitiation);
		
		
		
		StanzaCollector collector = connection.createStanzaCollector(new StanzaFilter(){

			@Override
			public boolean accept(XMPPConnection connection, XMLStanza data)
			{
				if (data instanceof IQ)
				{
					IQ iqResponse = (IQ) data;
					if (iqResponse.getType() == IQ.Type.set 
							&& iqResponse.getExtension("query", "http://jabber.org/protocol/bytestreams") != null)
					{
						return true;
					}
				}
				return false;
			}
		});
		
		connection.sendStanza(iq);
		XMLStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		
		collector.cancel();
		if (stanza == null)
		{
			status = Status.error;
			error = Error.no_response;
			return;
		}
		else
		{
			IQ iqStreamHost = (IQ) stanza;
			BytestreamPacketExtension bytestreamPacketExtension = 
				(BytestreamPacketExtension) iqStreamHost.getExtension("query", "http://jabber.org/protocol/bytestreams");
			
			String sid = si.getId();
			
			boolean success = false;
			for (Iterator<BytestreamPacketExtension.StreamHost> it = 
					bytestreamPacketExtension.getStreamHosts().iterator(); it.hasNext();)
			{
				BytestreamPacketExtension.StreamHost streamHost = it.next();	
				InputStream in = null;
				try
				{
					in = connectToProxy(streamHost, sid);
				}
				catch (IOException e)
				{
					continue;
				}
				if (in != null)
				{
					this.in = in;
					IQ iqUsedHost = new IQ(IQ.Type.result);
					iqUsedHost.setTo(peerJID);
					iqUsedHost.setStanzaID(iqStreamHost.getStanzaID());
					
					BytestreamPacketExtension bytestreamUsedHost = new BytestreamPacketExtension();
					bytestreamUsedHost.setUsedHost(streamHost.getJid());
					
					iqUsedHost.addExtension(bytestreamUsedHost);
					
					connection.sendStanza(iqUsedHost);
					
					success = true;
					break;
				}
			}
			
			if (!success)
			{
				IQ iqError = new IQ(IQ.Type.error);
				iqError.setStanzaID(iqStreamHost.getStanzaID());
				iqError.setTo(peerJID);
				
				XMPPError error = new XMPPError(XMPPError.Condition.item_not_found);
				iqError.setError(error);
				
				connection.sendStanza(iqError);
				
				status = Status.error;
				this.error = Error.connection;
				
				return;
			}
			
			try
			{
				startReceive();
			}
			catch (IOException e)
			{
				if (status != Status.cancelled)
				{
					status = Status.error;
					error = Error.stream;
					exception = new XMPPException(e);
				}
			}
		}
	}

	private void startReceive() throws IOException
	{
		File tempFile = new File(saveFile.getAbsolutePath() + ".temp");
		if (!tempFile.exists())
		{
			tempFile.createNewFile();
		}
		
		RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
		
		status = Status.in_progress;
		
		byte[] b = new byte[BUFFER_SIZE];
		
		int count = 0;

		sizeReceived = 0;
		if (offset != null && offset.longValue() > 0)
		{
			sizeReceived = offset.longValue();
			raf.seek(offset);
		}
		
		do
		{
			// write to the output stream
			raf.write(b, 0, count);
			sizeReceived += count;
			
			updateTempXML();
			
			// read more bytes from the input stream
			if (sizeReceived + b.length > getFileSize())
			{
				int i = (int) (getFileSize() - sizeReceived);
				b = new byte[i];
			}
			
			count = in.read(b);
			
		}
		while (count != -1 
				&& !getStatus().equals(Status.cancelled)
				&& sizeReceived < getFileSize());
		
		updateStatus();
		
		receiveEnd(tempFile);
		
		raf.close();
	}

	private void receiveEnd(File tempFile)
	{
		if (status == Status.complete && !tempFile.equals(saveFile))
		{
			tempFile.renameTo(saveFile);
			File tempXML = new File(saveFile.getAbsolutePath() + ".tempXML");
			if (tempXML.exists())
			{
				tempXML.delete();
			}
		}
	}

	private void updateTempXML()
	{
		try
		{
			File tempXML = new File(saveFile.getAbsolutePath() + ".tempXML");

			Properties properties = new Properties();


			properties.put("offset", String.valueOf(sizeReceived));
			String hash = xmppFile.getHash();
			if (hash != null)
			{
				properties.put("hash", hash);
			}
			
			properties.storeToXML(new FileOutputStream(tempXML), "XMPP Temp File");
		}
		catch (Exception e)
		{
		}
	}

	private void updateStatus()
	{
		if (!getStatus().equals(Status.cancelled) 
				&& (getError() == null || getError() == Error.none) 
				&& sizeReceived != getFileSize())
		{
			status = Status.error;
			error = Error.connection;
		}
		else if (!getStatus().equals(Status.error) 
				&& (getError() == null || getError() == Error.none) 
				&& sizeReceived == getFileSize())
		{
			status = Status.complete;
		}
	}

	private InputStream connectToProxy(StreamHost streamHost, String sid) throws IOException
	{
		Socket socket = new Socket();
		InetSocketAddress address = new InetSocketAddress(streamHost.getHost(), streamHost.getPort());
		socket.connect(address, connection.getConnectionConfig().getConnectionTimeout());
		String addr = createAddr(sid);
		SOCKS5Negotiator.establishSOCKS5ConnectionToProxy(socket, addr);
		
		return socket.getInputStream();
	}

	private String createAddr(String sid)
	{
		String ownerFullJID = connection.getJid().toPrepedFullJID();
		String addr = sid + peerJID.toPrepedFullJID() + ownerFullJID;
		String sha1 = StringUtils.hash(addr);
		return sha1;
	}
	
	private String getTempProperty(String hash, String property)
	{
		File tempXML = new File(saveFile.getAbsolutePath() + ".tempXML");
		if (tempXML.exists())
		{
			Properties properties = new Properties();
			try
			{
				properties.loadFromXML(new FileInputStream(tempXML));
				if (hash != null)
				{
					String tempHash = properties.getProperty("hash");
					if (hash.equals(tempHash))
					{
						return properties.getProperty(property);
					}
					else
					{
						return null;
					}
				}
				return properties.getProperty(property);
			}
			catch (Exception e)
			{
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlfiletransferextension.FileTransfer#cancel()
	 */
	@Override
	public void cancel()
	{
		status = Status.cancelled;
		if (receiveThread != null && receiveThread.isAlive())
		{
			receiveThread.interrupt();
		}
		close();
	}

	private void close()
	{
		if (in != null)
		{
			try
			{
				in.close();
			}
			catch (IOException e)
			{
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlfiletransferextension.FileTransfer#getError()
	 */
	@Override
	public Error getError()
	{
		return error;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlfiletransferextension.FileTransfer#getException()
	 */
	@Override
	public XMPPException getException()
	{
		return exception;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlfiletransferextension.FileTransfer#getFileName()
	 */
	@Override
	public String getFileName()
	{
		return xmppFile.getName();
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlfiletransferextension.FileTransfer#getFilePath()
	 */
	@Override
	public String getFilePath()
	{
		return saveFile.getAbsolutePath();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlfiletransferextension.FileTransfer#getFileSize()
	 */
	@Override
	public long getFileSize()
	{
		return xmppFile.getSize();
	}

	@Override
	public JID getPeerJID()
	{
		return peerJID;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlfiletransferextension.FileTransfer#getProgress()
	 */
	@Override
	public double getProgress()
	{
		if (sizeReceived <= 0 || getFileSize() <= 0)
		{
			return 0;
		}
		
		return (double) sizeReceived/ getFileSize();
	}

	@Override
	public long getTransferedSize()
	{
		return sizeReceived;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlfiletransferextension.FileTransfer#getStatus()
	 */
	@Override
	public Status getStatus()
	{
		return status;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlfiletransferextension.FileTransfer#getStreamMethod()
	 */
	@Override
	public String getStreamMethod()
	{
		return streamMethod;
	}


}