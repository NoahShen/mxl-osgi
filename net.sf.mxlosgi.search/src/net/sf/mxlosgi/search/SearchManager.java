/**
 * 
 */
package net.sf.mxlosgi.search;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.xmpp.JID;


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
	 * @throws XmppException
	 */
	public boolean isSupportSearch(XmppConnection connection, JID serverJID) throws XmppException;
	
	/**
	 * 
	 * @param connection
	 * @param serverJID
	 * @return
	 * @throws XmppException
	 */
	public SearchExtension getSearchExtension(XmppConnection connection, JID serverJID) throws XmppException;
	
	/**
	 * 
	 * @param connection
	 * @param extension
	 * @param serverJID
	 * @return
	 * @throws XmppException
	 */
	public SearchExtension search(XmppConnection connection, SearchExtension extension, JID serverJID) throws XmppException;
}
