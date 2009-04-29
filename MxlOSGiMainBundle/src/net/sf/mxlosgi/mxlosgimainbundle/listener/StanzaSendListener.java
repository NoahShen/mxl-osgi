/**
 * 
 */
package net.sf.mxlosgi.mxlosgimainbundle.listener;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

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
