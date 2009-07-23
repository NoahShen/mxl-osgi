/**
 * 
 */
package net.sf.mxlosgi.core.interceptor;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.xmpp.XmlStanza;


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
	public boolean interceptSendStanza(XmppConnection connection, XmlStanza stanza);
}
