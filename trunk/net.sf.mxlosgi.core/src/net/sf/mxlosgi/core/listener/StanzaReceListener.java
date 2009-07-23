package net.sf.mxlosgi.core.listener;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.xmpp.XmlStanza;


public interface StanzaReceListener
{

	/**
	 * Process the next packet sent to this stanza listener.
	 * 
	 * @param connection
	 * @param stanza
	 *                  the stanza to process.
	 */
	public void processReceStanza(XmppConnection connection, XmlStanza stanza);

}
