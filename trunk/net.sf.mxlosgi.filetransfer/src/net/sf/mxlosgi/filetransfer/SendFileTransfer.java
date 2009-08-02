package net.sf.mxlosgi.filetransfer;

/**
 * @author noah
 *
 */
public interface SendFileTransfer extends FileTransfer
{
	
	/**
	 * start send file
	 */
	public void sendFile() throws Exception;

}