/**
 * 
 */
package net.sf.mxlosgi.mxlosgifiletransferbundle.listener;

import net.sf.mxlosgi.mxlosgifiletransferbundle.FileTransferRequest;

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
