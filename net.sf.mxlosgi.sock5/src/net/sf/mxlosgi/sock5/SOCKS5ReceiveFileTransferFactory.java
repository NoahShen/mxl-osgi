package net.sf.mxlosgi.sock5;

import java.util.Map;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.filetransfer.ReceiveFileTransfer;
import net.sf.mxlosgi.filetransfer.ReceiveFileTransferFactory;
import net.sf.mxlosgi.streaminitiation.StreamInitiation;
import net.sf.mxlosgi.xmpp.JID;


/**
 * @author noah
 *
 */
public class SOCKS5ReceiveFileTransferFactory implements ReceiveFileTransferFactory
{

	private String streamMethod;
	
	
	public SOCKS5ReceiveFileTransferFactory()
	{
		streamMethod = "http://jabber.org/protocol/bytestreams";
	}

	@Override
	public ReceiveFileTransfer createReceiveFileTransfer(JID peerJID, 
											String packetID, 
											StreamInitiation si, 
											XmppConnection connection,
											Map<String, Object> properties)
	{
		return new SOCKS5ReceiveFileTransfer( peerJID, packetID, si, connection);
	}

	@Override
	public String getStreamMethod()
	{
		return streamMethod;
	}

}