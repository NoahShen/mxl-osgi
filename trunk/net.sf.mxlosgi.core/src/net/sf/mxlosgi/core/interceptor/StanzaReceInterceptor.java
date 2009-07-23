package net.sf.mxlosgi.core.interceptor;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.xmpp.XmlStanza;


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
	public boolean interceptReceStanza(XmppConnection connection, XmlStanza data);
}
