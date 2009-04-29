package net.sf.mxlosgi.mxlosgifiletransferbundle;

import java.util.Map;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgistreaminitiationbundle.StreamInitiation;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

/**
 * @author noah
 * 
 */
public interface ReceiveFileTransferFactory
{
	/**
	 * 
	 * @return
	 */
	public String getStreamMethod();

	/**
	 * 
	 * @param peerJID
	 * @param packetID
	 * @param streamInitiation
	 * @param connection
	 * @param properties
	 * @return
	 */
	public ReceiveFileTransfer createReceiveFileTransfer(JID peerJID, 
											String packetID, 
											StreamInitiation streamInitiation,
											XMPPConnection connection,
											Map<String, Object> properties);
}