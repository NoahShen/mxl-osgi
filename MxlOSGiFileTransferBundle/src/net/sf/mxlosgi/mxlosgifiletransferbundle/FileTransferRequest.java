package net.sf.mxlosgi.mxlosgifiletransferbundle;

import java.util.Collection;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

/**
 * @author noah
 * 
 */
public interface FileTransferRequest
{
	/**
	 * 
	 * @return
	 */
	public XMPPConnection getConnection();
	
	/**
	 * 
	 * @return
	 */
	public JID getSenderJID();
	
	/**
	 * 
	 * @return
	 */
	public String getFileName();
	
	/**
	 * 
	 * @return
	 */
	public long getFileSize();
	
	/**
	 * 
	 * @return
	 */
	public String getHash();
	
	/**
	 * 
	 * @return
	 */
	public String getLastModifyDate();
	/**
	 * 
	 * @return
	 */
	public String getDescription();

	
	/**
	 * 
	 * @return
	 */
	public String getMimeType();

	/**
	 * 
	 * @return
	 */
	public String getStreamID();

	/**
	 * 
	 * @return
	 */
	public boolean isRanged();

	/**
	 * 
	 * @return
	 */
	public Collection<String> getStreamMethods();
	

	/**
	 * 
	 * @param streamMethod
	 * @return
	 * @throws Exception
	 */
	public ReceiveFileTransfer accept(String streamMethod) throws Exception;

	/**
	 * 
	 */
	public void reject() throws Exception;
}