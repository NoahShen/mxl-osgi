/**
 * 
 */
package net.sf.mxlosgi.filetransfer.listener;

import net.sf.mxlosgi.filetransfer.FileTransferRequest;


/**
 * @author noah
 * 
 */
public interface FileTransferListener
{
	/**
	 * A request to send a file has been recieved from another user.
	 * 
	 * @param request
	 *                  The request from the other user.
	 */
	public void fileTransferRequest(FileTransferRequest request);
}
