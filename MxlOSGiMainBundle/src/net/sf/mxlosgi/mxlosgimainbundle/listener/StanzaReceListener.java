package net.sf.mxlosgi.mxlosgimainbundle.listener;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

public interface StanzaReceListener
{

	/**
	 * Process the next packet sent to this stanza listener.
	 * 
	 * @param connection
	 * @param stanza
	 *                  the data to process.
	 */
	public void processReceStanza(XMPPConnection connection, XMLStanza stanza);

}
