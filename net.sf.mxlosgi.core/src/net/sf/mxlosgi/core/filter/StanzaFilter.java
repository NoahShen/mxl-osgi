package net.sf.mxlosgi.core.filter;

import net.sf.mxlosgi.core.XMPPConnection;
import net.sf.mxlosgi.xmpp.XMLStanza;



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
	public boolean accept(XMPPConnection connection, XMLStanza stanza);
}
