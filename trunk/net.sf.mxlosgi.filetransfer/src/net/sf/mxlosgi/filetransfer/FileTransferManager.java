package net.sf.mxlosgi.filetransfer;

import java.io.File;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.xmpp.JID;


/**
 * @author noah
 * 
 */
public interface FileTransferManager
{

	/**
	 * negotiate with the peer and create SendFileTransfer
	 * @param connection
	 * @param to
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public SendFileTransfer createSendFileTransfer(XmppConnection connection, JID to, File file) throws Exception;

	/**
	 * negotiate with the peer and create SendFileTransfer
	 * @param connection
	 * @param to
	 * @param filepath
	 * @return
	 * @throws Exception
	 */
	public SendFileTransfer createSendFileTransfer(XmppConnection connection, JID to, String filepath) throws Exception;

	/**
	 * 
	 * @param connection
	 * @param to
	 * @param file
	 * @param description
	 * @param mimeType
	 * @return
	 * @throws Exception
	 */
	public SendFileTransfer createSendFileTransfer(XmppConnection connection, JID to, File file, String description, String mimeType) throws Exception;

}