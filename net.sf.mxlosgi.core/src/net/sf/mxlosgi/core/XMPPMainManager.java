/**
 * 
 */
package net.sf.mxlosgi.core;

import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.core.interceptor.StanzaReceInterceptor;
import net.sf.mxlosgi.core.interceptor.StanzaSendInterceptor;
import net.sf.mxlosgi.core.listener.ConnectionListener;
import net.sf.mxlosgi.core.listener.ContactListener;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.core.listener.StanzaSendListener;
import net.sf.mxlosgi.core.listener.XMLStringListener;
import net.sf.mxlosgi.core.listener.XMPPOwnerListener;
import net.sf.mxlosgi.xmpp.JID;


/**
 * @author noah
 * 
 */
public interface XMPPMainManager
{
	/**
	 * 
	 * @return
	 */
	public XMPPConnection createConnection();

	/**
	 * 
	 * @param serviceName
	 * @return
	 */
	public XMPPConnection createConnection(String serviceName);

	/**
	 * 
	 * @param config
	 * @return
	 */
	public XMPPConnection createConnection(ConnectionConfig config);

	/**
	 * 
	 * @return
	 */
	public XMPPConnection[] getAllConnections();
	
	/**
	 * 
	 * @param serviceName
	 * @return
	 */
	public XMPPConnection[] getConnections(String serviceName);
	
	/**
	 * 
	 * @param jid
	 * @return
	 */
	public XMPPConnection getConnections(JID jid);
	
	/**
	 * Registers a packet listener with this connection. A packet filter
	 * determines which packets will be delivered to the listener. If the
	 * same packet listener is added again with a different filter, only
	 * the new filter will be used.
	 * 
	 * @param stanzaReceListener
	 *                  the stanza listener to notify of new stanzas.
	 * @param packetFilter
	 *                  the packet filter to use.
	 */
	public void addStanzaReceListener(StanzaReceListener stanzaReceListener, StanzaFilter stanzaFilter);

	/**
	 * Removes a stanza listener from this connection.
	 * 
	 * @param stanzaReceListener
	 *                  the stanza listener to remove.
	 */
	public void removeStanzaReceListener(StanzaReceListener stanzaReceListener);

	/**
	 * Registers a stanza listener with this connection. The listener will
	 * be notified of every stanza that this connection sends. A stanza
	 * filter determines which stanzas will be delivered to the listener.
	 * Note that the thread that writes stanzas will be used to invoke the
	 * listeners. Therefore, each stanza listener should complete all
	 * operations quickly or use a different thread for processing.
	 * 
	 * @param stanzaListener
	 *                  the stanza listener to notify of sent stanzas.
	 * @param packetFilter
	 *                  the stanza filter to use.
	 */
	public void addStanzaSendListener(StanzaSendListener stanzaSendListener, StanzaFilter stanzaFilter);

	/**
	 * Removes a stanza listener from this connection.
	 * 
	 * @param stanzaListener
	 *                  the stanza listener to remove.
	 */
	public void removeStanzaSendListener(StanzaSendListener stanzaSendListener);

	/**
	 * 
	 * @param stanzaReceInterceptor
	 * @param stanzaFilter
	 */
	public void addStanzaReceInterceptor(StanzaReceInterceptor stanzaReceInterceptor, StanzaFilter stanzaFilter);

	/**
	 * 
	 * @param stanzaReceInterceptor
	 */
	public void removeStanzaReceInterceptor(StanzaReceInterceptor stanzaReceInterceptor);

	/**
	 * Registers a stanza interceptor with this connection. The
	 * interceptor will be invoked every time a stanza is about to be sent
	 * by this connection. Interceptors may modify the stanza to be sent.
	 * A stanza filter determines which stanzas will be delivered to the
	 * interceptor.
	 * 
	 * @param stanzaSendInterceptor
	 *                  the stanza interceptor to notify of stanzas about
	 *                  to be sent.
	 * @param stanzaFilter
	 *                  the stanza filter to use.
	 */
	public void addStanzaSendInterceptor(StanzaSendInterceptor stanzaSendInterceptor, StanzaFilter stanzaFilter);

	/**
	 * Removes a stanza interceptor.
	 * 
	 * @param stanzaSendInterceptor
	 *                  the stanza interceptor to remove.
	 */
	public void removeStanzaSendInterceptor(StanzaSendInterceptor stanzaSendInterceptor);

	/**
	 * Adds a connection listener to this connection that will be notified
	 * when the connection closes or fails. The connection needs to
	 * already be connected or otherwise an IllegalStateException will be
	 * thrown.
	 * 
	 * @param connectionListener
	 *                  a connection listener.
	 */
	public void addConnectionListener(ConnectionListener connectionListener);

	/**
	 * Removes a connection listener from this connection.
	 * 
	 * @param connectionListener
	 *                  a connection listener.
	 */
	public void removeConnectionListener(ConnectionListener connectionListener);

	/**
	 * 
	 * @param xmlStringListener
	 */
	public void addXMLStringListener(XMLStringListener xmlStringListener);

	/**
	 * 
	 * @param xmlStringListener
	 */
	public void removeXMLStringListener(XMLStringListener xmlStringListener);
	
	/**
	 * 
	 * @param ownerListener
	 */
	public void addXMPPOwnerListener(XMPPOwnerListener ownerListener);
	
	/**
	 * 
	 * @param ownerListener
	 */
	public void removeXMPPOwnerListener(XMPPOwnerListener ownerListener);
	
	/**
	 * 
	 * @param contactListener
	 */
	public void addContactListener(ContactListener contactListener);
	
	/**
	 * 
	 * @param contactListener
	 */
	public void removeContactListener(ContactListener contactListener);
}
