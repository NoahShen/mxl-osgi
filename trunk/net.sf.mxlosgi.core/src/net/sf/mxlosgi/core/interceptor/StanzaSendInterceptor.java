/**
 * 
 */
package net.sf.mxlosgi.core.interceptor;

import net.sf.mxlosgi.core.XMPPConnection;
import net.sf.mxlosgi.xmpp.XMLStanza;


/**
 * @author noah
 *
 */
public interface StanzaSendInterceptor
{
	/**
	 * @param connection
	 * @param stanza
	 */
	public boolean interceptSendStanza(XMPPConnection connection, XMLStanza stanza);
}
