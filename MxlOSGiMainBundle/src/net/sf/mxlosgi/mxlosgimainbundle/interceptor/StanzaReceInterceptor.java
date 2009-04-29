package net.sf.mxlosgi.mxlosgimainbundle.interceptor;


import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

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
