package net.sf.mxlosgi.mxlosgifiletransferbundle;

import java.io.File;

/**
 * @author noah
 *
 */
public interface ReceiveFileTransfer extends FileTransfer
{
	/**
	 * 
	 * @param file
	 * @throws XMPPException
	 */
	public void receiveFile(File file) throws Exception;
}