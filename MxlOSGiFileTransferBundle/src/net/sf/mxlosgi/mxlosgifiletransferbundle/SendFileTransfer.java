package net.sf.mxlosgi.mxlosgifiletransferbundle;

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