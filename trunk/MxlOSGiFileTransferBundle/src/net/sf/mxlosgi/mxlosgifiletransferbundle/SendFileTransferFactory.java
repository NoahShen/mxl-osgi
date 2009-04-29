package net.sf.mxlosgi.mxlosgifiletransferbundle;

import java.io.File;
import java.util.Map;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgistreaminitiationbundle.StreamInitiation;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

/**
 * @author noah
 *
 */
public interface SendFileTransferFactory
{
	/**
	 * 
	 * @return
	 */
	public String getStreamMethod();
	
	/**
	 * 
	 * @param file
	 * @param peerJID
	 * @param sid
	 * @param si
	 * @param fileTransferManager
	 * @param connection
	 * @return
	 */
	public SendFileTransfer createSendFileTransfer(File file, 
										JID peerJID, 
										String sid,
										StreamInitiation streamInitiation, 
										FileTransferManager fileTransferManager, 
										XMPPConnection connection,
										Map<String, Object> properties);
}