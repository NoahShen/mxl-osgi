/**
 * 
 */
package net.sf.mxlosgi.core.listener;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.xmpp.XmlStanza;


/**
 * @author noah
 *
 */
public interface StanzaSendListener
{
	/**
	 * 
	 * @param connection
	 * @param stanza
	 */
	public void processSendStanza(XmppConnection connection, XmlStanza stanza);
}
