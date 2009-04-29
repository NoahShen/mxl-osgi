package net.sf.mxlosgi.mxlosgisock5bundle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoManager;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoPacketExtension;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoItemsManager;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoItemsPacketExtension;
import net.sf.mxlosgi.mxlosgifiletransferbundle.FileTransferManager;
import net.sf.mxlosgi.mxlosgifiletransferbundle.SendFileTransfer;
import net.sf.mxlosgi.mxlosgimainbundle.StanzaCollector;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgimainbundle.filter.PacketIDFilter;
import net.sf.mxlosgi.mxlosgisock5bundle.BytestreamPacketExtension.StreamHost;
import net.sf.mxlosgi.mxlosgistreaminitiationbundle.StreamInitiation;
import net.sf.mxlosgi.mxlosgiutilsbundle.NetworkUtils;
import net.sf.mxlosgi.mxlosgiutilsbundle.StringUtils;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class SOCKS5SendFileTransfer implements SendFileTransfer, Runnable
{

	private File file;
	
	private JID peerJID;
	
	private StreamInitiation siResponse;
	
	private String sid;
	
	private Status status = Status.inactive;;
	
	private String streamMethod;
	
	private long sizeSent;
	
	private FileTransferManager fileTransferManager;
	
	private XMPPConnection connection;
	
	private Error error = Error.none;
	
	private Exception exception;
	
	private Long offset;
	
	private Thread sendThread;
	
	private Socket sendSocket;
	
	private Map<String, Object> properties;
	
	public SOCKS5SendFileTransfer(File file, 
							JID peerJID, 
							String sid, 
							StreamInitiation siResponse, 
							FileTransferManager fileTransferManager, 
							XMPPConnection connection,
							Map<String, Object> properties)
	{
		this.file = file;
		this.peerJID = peerJID;
		this.siResponse = siResponse;
		this.sid = sid;
		this.fileTransferManager = fileTransferManager;
		this.connection = connection;
		this.properties = properties;
		streamMethod = "http://jabber.org/protocol/bytestreams";
		
		StreamInitiation.XMPPFile xmppFile = siResponse.getFile();
		
		if (xmppFile != null)
		{
			if (xmppFile.isRanged())
			{
				this.offset = xmppFile.getOffset();
			}
		}
	}

	/**
	 * @return the si
	 */
	public StreamInitiation getSi()
	{
		return siResponse;
	}

	/**
	 * @param si the si to set
	 */
	public void setSi(StreamInitiation si)
	{
		this.siResponse = si;
	}

	@Override
	public void sendFile() throws XMPPException
	{
		if (status == Status.cancelled)
		{
			throw new XMPPException("file transfer has been cancel");
		}
		if (status != Status.inactive)
		{
			throw new XMPPException("file transfer has started");
		}
		
		sendThread = new Thread(this);
		sendThread.start();
	}


	@Override
	public void run()
	{
		
		Collection<BytestreamPacketExtension.StreamHost> streamHosts = getStreamHosts();
		
		if (streamHosts.isEmpty())
		{
			streamHosts = searchStreamHosts();
		}
		
		BytestreamPacketExtension byteStream = new BytestreamPacketExtension();
		byteStream.setStreamID(sid);
		byteStream.setMode(BytestreamPacketExtension.Mode.tcp);
		XMPPSOCKS5Proxy proxy = getLocalProxy();
		String ip = NetworkUtils.getLocalIP();
		if (proxy != null && ip != null)
		{
			JID ownerFullJID = connection.getJid();
			byteStream.addStreamHost(ownerFullJID, 
								ip, //proxy.getHost(), 
								proxy.getPort());
		}
		
		for (Iterator<BytestreamPacketExtension.StreamHost> it = streamHosts.iterator(); it.hasNext();)
		{
			byteStream.addStreamHost(it.next());
		}
		
		if (proxy != null)
		{
			proxy.startListening();
		}
		BytestreamPacketExtension byteStreamX = initInteraction(byteStream);
		if (byteStreamX == null)
		{
			return;
		}
		
		BytestreamPacketExtension.StreamHostUsed usedHost= byteStreamX.getUsedHost();
		if (usedHost != null)
		{
			JID ownerFullJID = connection.getJid();
			if (usedHost.getJid().equals(ownerFullJID))
			{
				if (sendSocket != null)
				{
					startSending();
				}
			}
			else
			{
				proxy.stopListening();
				
				BytestreamPacketExtension.StreamHost usedStreamHost = byteStream.getStreamHost(usedHost.getJid());
				
				try
				{
					sendSocket = connectToProxy(usedStreamHost);
					String addr = createAddr();
					
					// don't use SOCKS5Negotiator's method
					establishSOCKS5ConnectionToProxy(sendSocket, addr);
					if (activateStream(usedHost.getJid()))
					{
						startSending();
					}
					else
					{
						status = Status.error;
						error = Error.connection;
					}
				}
				catch (IOException e)
				{
					status = Status.error;
					error = Error.connection;
					exception = e;
					return;
				}

			}
		}
		else
		{
			status = Status.error;
			error = Error.not_acceptable;
		}
	}
	
	private void establishSOCKS5ConnectionToProxy(Socket sendSocket2, String addr) throws IOException
	{
		byte[] cmd = new byte[3];
		
		OutputStream out = new DataOutputStream(sendSocket2.getOutputStream());
		InputStream in = new DataInputStream(sendSocket2.getInputStream());
		
		cmd[0] = (byte) 0x05;
		cmd[1] = (byte) 0x01;
		cmd[2] = (byte) 0x00;


		out.write(cmd);


		byte[] response = new byte[2];
		
		in.read(response);
		
		cmd = createOutgoingSocks5Message(1, addr);
		out.write(cmd);
		createIncomingSocks5Message(in);
	}

	private byte[] createOutgoingSocks5Message(int cmd, String strAddr)
	{
		byte addr[] = strAddr.getBytes();

		byte[] data = new byte[7 + addr.length];
		data[0] = (byte) 5;
		data[1] = (byte) cmd;
		data[2] = (byte) 0;
		data[3] = (byte) 0x3;
		data[4] = (byte) addr.length;

		System.arraycopy(addr, 0, data, 5, addr.length);
		data[data.length - 2] = (byte) 0;
		data[data.length - 1] = (byte) 0;

		return data;
	}
	
	private String createIncomingSocks5Message(InputStream in) throws IOException
	{
		byte[] cmd = new byte[5];
		in.read(cmd, 0, 5);

		byte[] addr = new byte[cmd[4]];
		in.read(addr, 0, addr.length);
		String strAddr = new String(addr);
		return strAddr;
	}
	
	private boolean activateStream(JID jid)
	{
		IQ iq = new IQ(IQ.Type.set);
		JID ownerFullJID = connection.getJid();
		iq.setFrom(ownerFullJID);
		iq.setTo(jid);
		BytestreamPacketExtension byteStream = new BytestreamPacketExtension();
		byteStream.setStreamID(sid);
		byteStream.setActivate(peerJID.toFullJID());
		byteStream.setMode(null);
		
		iq.addExtension(byteStream);

		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));

		connection.sendStanza(iq);
		XMLStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());

		collector.cancel();
		if (stanza != null)
		{
			IQ iqResponse = (IQ) stanza;
			if (iqResponse.getType() == IQ.Type.result)
			{
				return true;
			}
		}
		return false;
	}

	private Socket connectToProxy(StreamHost usedStreamHost) throws IOException
	{
		sendSocket = new Socket();
		InetSocketAddress address = new InetSocketAddress(usedStreamHost.getHost(), usedStreamHost.getPort());
		sendSocket.connect(address, connection.getConnectionConfig().getConnectionTimeout());
		return sendSocket;
	}

	private String createAddr()
	{
		String ownerFullJID = connection.getJid().toPrepedFullJID();

		String addr = sid + ownerFullJID + peerJID.toPrepedFullJID();
		String sha1 = StringUtils.hash(addr);
		return sha1;
	}

	private BytestreamPacketExtension initInteraction(BytestreamPacketExtension byteStream)
	{
		IQ iq = new IQ(IQ.Type.set);
		iq.setTo(peerJID);
		iq.addExtension(byteStream);
		
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		
		connection.sendStanza(iq);
		XMLStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		
		collector.cancel();
		if (stanza == null)
		{
			status = Status.error;
			error = Error.no_response;
			return null;
		}
		else
		{
			IQ iqResponse = (IQ) stanza;
			if (iqResponse.getType() == IQ.Type.error)
			{
				status = Status.error;
				error = Error.not_acceptable;
				return null;
			}
			else if (iqResponse.getType() == IQ.Type.result)
			{
				BytestreamPacketExtension byteStreamX = 
					(BytestreamPacketExtension) iqResponse.getExtension(byteStream.getElementName(), 
															byteStream.getNamespace());
				return byteStreamX;
			}
		}
		return null;
	}



	private XMPPSOCKS5Proxy getLocalProxy()
	{
		XMPPSOCKS5Proxy proxy = null;
		
		int i = 5;
		int base = 8000;
		Random random=new Random();
		while (i-- > 0)
		{
			int port = base + random.nextInt(2000);
			try
			{
				proxy = new XMPPSOCKS5Proxy(port);
				break;
			}
			catch (IOException e)
			{
			}
		}
		
		return proxy;
	}


	private Collection<StreamHost> searchStreamHosts()
	{
		List<StreamHost> list = new ArrayList<StreamHost>();
		try
		{
			DiscoItemsManager discoItemsManager = (DiscoItemsManager) properties.get("DiscoItemsManager");
			DiscoInfoManager discoInfoManager = (DiscoInfoManager) properties.get("DiscoInfoManager");
			JID serverJID = new JID(null, connection.getConnectionConfig().getServiceName(), null);
			DiscoItemsPacketExtension discoItems = discoItemsManager.getDiscoItems(connection, serverJID);
			for (Iterator<DiscoItemsPacketExtension.Item> it = discoItems.getItems().iterator(); it.hasNext();)
			{
				DiscoItemsPacketExtension.Item item = it.next();
				JID jid = item.getJid();
				DiscoInfoPacketExtension discoInfo = discoInfoManager.getDiscoInfo(connection, jid);
				if (discoInfo.getFeatures().contains("http://jabber.org/protocol/bytestreams"))
				{
					for (Iterator<DiscoInfoPacketExtension.Identity> itDiscoInfo = discoInfo.getIdentities().iterator(); itDiscoInfo.hasNext();)
					{
						DiscoInfoPacketExtension.Identity identity = itDiscoInfo.next();
						String category = identity.getCategory();
						String type = identity.getType();
						if (category != null && category.equals("proxy")
								&& type != null && type.equals("bytestreams"))
						{
							StreamHost streamHost = getSteamHostInfo(jid);
							if (streamHost != null)
							{
								list.add(streamHost);
							}
						}
					}
				}
			}
		}
		catch (XMPPException e)
		{
		}
		return list;
	}

	private StreamHost getSteamHostInfo(JID jid)
	{
		IQ iq = new IQ(IQ.Type.get);
		iq.setTo(jid);
		BytestreamPacketExtension bytestream = new BytestreamPacketExtension();
		bytestream.setStreamID(BytestreamPacketExtension.ID_NOT_AVAILABLE);
		iq.addExtension(bytestream);
		
		
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XMLStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		
		collector.cancel();
		if (stanza == null)
		{
//			status = Status.error;
//			error = Error.no_response;
			return  null;
		}
		else
		{
			IQ iqResponse = (IQ) stanza;
			if (iqResponse.getType() == IQ.Type.error)
			{
//				XMPPError error = iqResponse.getError();
//				status = Status.error;
//				exception =  new XMPPException(error);
				return null;
			}
			else if (iqResponse.getType() == IQ.Type.result)
			{
				BytestreamPacketExtension byteStream = 
					(BytestreamPacketExtension) iqResponse.getExtension(bytestream.getElementName(), 
																	bytestream.getNamespace());
				Iterator<StreamHost> itHost = byteStream.getStreamHosts().iterator();
				if (itHost.hasNext())
				{
					StreamHost streamHost = itHost.next();
					return streamHost;
				}
			}
		}
		return null;
	}



	private Collection<StreamHost> getStreamHosts()
	{
		List<StreamHost> list = new ArrayList<StreamHost>();
		
		for (JID proxy : fileTransferManager.getProxies())
		{			
			StreamHost streamHost = getSteamHostInfo(proxy);
			if (streamHost != null)
			{
				list.add(streamHost);
			}
			
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlfiletransferextension.FileTransfer#cancel()
	 */
	@Override
	public void cancel()
	{
		status = Status.cancelled;
		if (sendThread != null && sendThread.isAlive())
		{
			sendThread.interrupt();
		}
		closeSocket();
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
	public Exception getException()
	{
		return exception;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlfiletransferextension.FileTransfer#getFileName()
	 */
	@Override
	public String getFileName()
	{
		return file.getName();
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlfiletransferextension.FileTransfer#getFilePath()
	 */
	@Override
	public String getFilePath()
	{
		return file.getAbsolutePath();
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlfiletransferextension.FileTransfer#getFileSize()
	 */
	@Override
	public long getFileSize()
	{
		return file.length();
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlfiletransferextension.FileTransfer#getPeerJID()
	 */
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
		if (sizeSent <= 0 || getFileSize() <= 0)
		{
			return 0;
		}
		
		return (double) sizeSent/ getFileSize();
	}

	@Override
	public long getTransferedSize()
	{
		return sizeSent;
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
	
	private void startSending()
	{
		try
		{
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			sendFile(raf);

			closeSocket();
		}
		catch (IOException e)
		{	
			status = Status.error;
			error = Error.bad_file;
			exception = e;
		}
		catch (XMPPException e)
		{
			status = Status.error;
			error = Error.stream;
			exception = e;
		}
	}

	private void closeSocket()
	{
		if (sendSocket != null && !sendSocket.isClosed())
		{
			try
			{
				sendSocket.close();
			}
			catch (IOException e)
			{
			}
		}
	}

	
	private void sendFile( RandomAccessFile raf) throws IOException, XMPPException
	{
		OutputStream out = new DataOutputStream(sendSocket.getOutputStream());
		status = Status.in_progress;
		byte[] b = new byte[BUFFER_SIZE];
		int count = 0;
		sizeSent = 0;

		if (offset != null && offset > 0)
		{
			sizeSent = offset.longValue();
			raf.seek(offset);
		}
		do
		{
			// write to the output stream
			try
			{
				out.write(b, 0, count);
				sizeSent += count;
			}
			catch (IOException e)
			{
				closeSocket();
				throw new XMPPException("error writing to output stream", e);
			}
			
			// read more bytes from the input stream
			if (sizeSent + b.length > getFileSize())
			{
				int i = (int) (getFileSize() - sizeSent);
				b = new byte[i];
			}
			try
			{
				count = raf.read(b);
			}
			catch (IOException e)
			{
				closeSocket();
				throw new XMPPException("error reading from input stream", e);
			}
		}
		while (count != -1 
				&& !SOCKS5SendFileTransfer.this.getStatus().equals(Status.cancelled)
				&& sizeSent < getFileSize());
		updateStatus();

		
	}

	private void updateStatus()
	{
		if (!getStatus().equals(Status.cancelled) 
				&& (getError() == null || getError() == Error.none) 
				&& sizeSent != getFileSize())
		{
			status = Status.error;
			SOCKS5SendFileTransfer.this.error = Error.connection;
		}
		else if (!getStatus().equals(Status.error) 
				&& (getError() == null || getError() == Error.none) 
				&& sizeSent == getFileSize())
		{
			status = Status.complete;
		}
		
	}
	
	private class XMPPSOCKS5Proxy implements Runnable
	{
		private ServerSocket serverSocket;

		private Thread thread;
		
		private boolean done = false;
		
		public XMPPSOCKS5Proxy(int port) throws IOException
		{
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(connection.getConnectionConfig().getConnectionTimeout());
		}

		public String getHost()
		{
			if (serverSocket != null)
			{
				return serverSocket.getInetAddress().getHostAddress();
			}
			return null;
		}
		
		public int getPort()
		{
			if (serverSocket != null)
			{
				return serverSocket.getLocalPort();
			}
			return -1;
		}

		public void startListening()
		{
			thread = new Thread(this);
			thread.start();
		}
		
		public void stopListening()
		{
			done = true;
			if (serverSocket != null && !serverSocket.isClosed())
			{
				try
				{
					serverSocket.close();
				}
				catch (IOException e)
				{
				}
			}
			
		}
		
		@Override
		public void run()
		{
			try
			{
				while (!done)
				{
					Socket socket = serverSocket.accept();

					if (socket == null)
					{
						continue;
					}
					if (done)
					{
						break;
					}
					
					OutputStream out = new DataOutputStream(socket.getOutputStream());
					InputStream in = new DataInputStream(socket.getInputStream());

					SOCKS5Negotiator.establishSocks5UploadConnection(out, in);


					if (!socket.isConnected())
					{
						throw new XMPPException("Socket closed by remote user");
					}
					
					sendSocket = socket;
					
					if (serverSocket != null && !serverSocket.isClosed())
					{
						serverSocket.close();
					}
					done = true;
				}
			}
//			catch (IOException e)
//			{
//				status = Status.error;
//				exception = new XMPPException(e);
//				e.printStackTrace();
//			}
//			catch (XMPPException e)
//			{
//				status = Status.error;
//				exception = e;
//				e.printStackTrace();
//			}
			catch (Exception e)
			{
//				status = Status.error;
//				exception = e;
//				e.printStackTrace();
			}
		}

	}
}