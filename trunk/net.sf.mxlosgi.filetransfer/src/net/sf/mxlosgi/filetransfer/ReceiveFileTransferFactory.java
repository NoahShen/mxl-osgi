package net.sf.mxlosgi.filetransfer;

import java.util.Map;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.streaminitiation.StreamInitiation;
import net.sf.mxlosgi.xmpp.JID;


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
											XmppConnection connection,
											Map<String, Object> properties);
}