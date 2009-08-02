package net.sf.mxlosgi.sock5;

import java.io.File;
import java.util.Map;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.filetransfer.SendFileTransfer;
import net.sf.mxlosgi.filetransfer.SendFileTransferFactory;
import net.sf.mxlosgi.sock5.impl.DiscoInfoManagerServiceTracker;
import net.sf.mxlosgi.sock5.impl.DiscoItemsManagerServiceTracker;
import net.sf.mxlosgi.sock5.impl.ProxyServiceTracker;
import net.sf.mxlosgi.streaminitiation.StreamInitiation;
import net.sf.mxlosgi.xmpp.JID;


/**
 * @author noah
 *
 */
public class SOCKS5SendFileTransferFactory implements SendFileTransferFactory
{
	
	private DiscoInfoManagerServiceTracker discoInfoManagerServiceTracker;
	
	private DiscoItemsManagerServiceTracker discoItemsManagerServiceTracker;
	
	private ProxyServiceTracker proxyServiceTracker;
	
	private String streamMethod;
	
	public SOCKS5SendFileTransferFactory(DiscoInfoManagerServiceTracker discoInfoManagerServiceTracker,
								DiscoItemsManagerServiceTracker discoItemsManagerServiceTracker,
								ProxyServiceTracker proxyServiceTracker)
	{
		this.streamMethod = "http://jabber.org/protocol/bytestreams";
		this.discoInfoManagerServiceTracker = discoInfoManagerServiceTracker;
		this.discoItemsManagerServiceTracker = discoItemsManagerServiceTracker;
		this.proxyServiceTracker = proxyServiceTracker;
		
	}

	@Override
	public SendFileTransfer createSendFileTransfer(File file, 
										JID peerJID, 
										String sid, 
										StreamInitiation si, 
										XmppConnection connection, 
										Map<String, Object> properties)
	{
		return new SOCKS5SendFileTransfer(file, peerJID, sid, si, connection, 
									discoInfoManagerServiceTracker, 
									discoItemsManagerServiceTracker, 
									proxyServiceTracker);
	}

	@Override
	public String getStreamMethod()
	{
		return streamMethod;
	}

}