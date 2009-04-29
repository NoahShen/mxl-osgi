/**
 * 
 */
package net.sf.mxlosgi.mxlosgisearchbundle;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

/**
 * @author noah
 *
 */
public interface SearchManager
{
	/**
	 * 
	 * @param connection
	 * @param serverJID
	 * @return
	 * @throws XMPPException
	 */
	public boolean isSupportSearch(XMPPConnection connection, JID serverJID) throws XMPPException;
	
	/**
	 * 
	 * @param connection
	 * @param serverJID
	 * @return
	 * @throws XMPPException
	 */
	public SearchExtension getSearchExtension(XMPPConnection connection, JID serverJID) throws XMPPException;
	
	/**
	 * 
	 * @param connection
	 * @param extension
	 * @param serverJID
	 * @return
	 * @throws XMPPException
	 */
	public SearchExtension search(XMPPConnection connection, SearchExtension extension, JID serverJID) throws XMPPException;
}
