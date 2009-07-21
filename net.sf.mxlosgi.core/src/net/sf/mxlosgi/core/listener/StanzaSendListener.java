/**
 * 
 */
package net.sf.mxlosgi.core.listener;

import net.sf.mxlosgi.core.XMPPConnection;
import net.sf.mxlosgi.xmpp.XMLStanza;


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
	public void processSendStanza(XMPPConnection connection, XMLStanza stanza);
}
