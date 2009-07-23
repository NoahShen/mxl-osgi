package net.sf.mxlosgi.core.filter;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.xmpp.XmlStanza;



public interface StanzaFilter
{

	/**
	 * Tests whether or not the specified data should pass the filter.
	 * 
	 * @param connection
	 * @param stanza
	 *                  the stanza to test.
	 * @return true if and only if <tt>stanza</tt> passes the filter.
	 */
	public boolean accept(XmppConnection connection, XmlStanza stanza);
}
