package net.sf.mxlosgi.mxlosgisock5bundle;

import java.util.Map;

import net.sf.mxlosgi.mxlosgifiletransferbundle.ReceiveFileTransfer;
import net.sf.mxlosgi.mxlosgifiletransferbundle.ReceiveFileTransferFactory;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgistreaminitiationbundle.StreamInitiation;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;


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

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlfiletransferextension.ReceiveFileTransferFactory#createReceiveFileTransfer()
	 */
	@Override
	public ReceiveFileTransfer createReceiveFileTransfer(JID peerJID, 
											String packetID, 
											StreamInitiation si, 
											XMPPConnection connection,
											Map<String, Object> properties)
	{
		return new SOCKS5ReceiveFileTransfer( peerJID, packetID, si, connection);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlfiletransferextension.ReceiveFileTransferFactory#getStreamMethod()
	 */
	@Override
	public String getStreamMethod()
	{
		return streamMethod;
	}

}