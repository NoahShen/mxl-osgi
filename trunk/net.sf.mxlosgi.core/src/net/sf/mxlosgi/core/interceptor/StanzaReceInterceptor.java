package net.sf.mxlosgi.core.interceptor;

import net.sf.mxlosgi.core.XMPPConnection;
import net.sf.mxlosgi.xmpp.XMLStanza;


public interface StanzaReceInterceptor
{

	/**
	 * Process the stanza that is about to be sent to the server. The
	 * intercepted stanza can be modified by the interceptor.
	 * 
	 * @param connection
	 * @param data
	 *                  the data to is going to be sent to the server.
	 */
	public boolean interceptReceStanza(XMPPConnection connection, XMLStanza data);
}
