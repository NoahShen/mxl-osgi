package net.sf.mxlosgi.core.listener;

import net.sf.mxlosgi.core.XMPPConnection;
import net.sf.mxlosgi.xmpp.XMLStanza;


public interface StanzaReceListener
{

	/**
	 * Process the next packet sent to this stanza listener.
	 * 
	 * @param connection
	 * @param stanza
	 *                  the stanza to process.
	 */
	public void processReceStanza(XMPPConnection connection, XMLStanza stanza);

}
