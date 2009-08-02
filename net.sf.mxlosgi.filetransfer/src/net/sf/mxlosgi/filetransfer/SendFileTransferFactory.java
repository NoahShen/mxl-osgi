package net.sf.mxlosgi.filetransfer;

import java.io.File;
import java.util.Map;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.streaminitiation.StreamInitiation;
import net.sf.mxlosgi.xmpp.JID;

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
	 * @param connection
	 * @return
	 */
	public SendFileTransfer createSendFileTransfer(File file, 
										JID peerJID, 
										String sid,
										StreamInitiation streamInitiation,
										XmppConnection connection,
										Map<String, Object> properties);
}