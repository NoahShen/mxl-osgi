package net.sf.mxlosgi.mxlosgimainbundle;

import net.sf.mxlosgi.mxlosgimainbundle.filter.StanzaFilter;
import net.sf.mxlosgi.mxlosgiutilsbundle.IHasAttribute;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.Presence;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 * 
 */
public interface XMPPConnection extends IHasAttribute
{
	
	/**
	 * 
	 * @return
	 */
	public JID getJid();

	/**
	 * Returns the host name of the server where the XMPP server is
	 * running. This would be the IP address of the server or a name that
	 * may be resolved by a DNS server.
	 * 
	 * @return the host name of the server where the XMPP server is
	 *         running.
	 */
	public String getHost();

	/**
	 * Returns the port number of the XMPP server for this connection. The
	 * default port is 5222. 
	 * 
	 * @return the port number of the XMPP server.
	 */
	public int getPort();

	/**
	 * Logs in to the server using the strongest authentication mode
	 * supported by the server, then sets presence to available. If more
	 * than five seconds (default timeout) elapses in each step of the
	 * authentication process without a response from the server, or if an
	 * error occurs, a XMPPException will be thrown.
	 * 
	 * @param username
	 *                  the username.
	 * @param password
	 *                  the password.
	 * @throws XMPPException
	 *                   if an error occurs.
	 */
	public Future login(String username, String password);

	/**
	 * Logs in to the server using the strongest authentication mode
	 * supported by the server, then sets presence to available. If more
	 * than five seconds (default timeout) elapses in each step of the
	 * authentication process without a response from the server, or if an
	 * error occurs, a XMPPException will be thrown.
	 * 
	 * @param username
	 *                  the username.
	 * @param password
	 *                  the password.
	 * @param resource
	 *                  the resource.
	 * @throws XMPPException
	 *                   if an error occurs.
	 * @throws IllegalStateException
	 *                   if not connected to the server, or already logged
	 *                   in to the serrver.
	 */
	public Future login(String username, String password, String resource);

	/**
	 * Logs in to the server using the strongest authentication mode
	 * supported by the server. If the server supports SASL authentication
	 * then the user will be authenticated using SASL if not Non-SASL
	 * authentication will be tried. An available presence may optionally
	 * be sent. If <tt>sendPresence</tt> is false, a presence packet
	 * must be sent manually later. If more than five seconds (default
	 * timeout) elapses in each step of the authentication process without
	 * a response from the server, or if an error occurs, a XMPPException
	 * will be thrown.
	 * <p>
	 * <p/> Before logging in (i.e. authenticate) to the server the
	 * connection must be connected. For compatibility and easiness of use
	 * the connection will automatically connect to the server if not
	 * already connected.
	 * 
	 * @param username
	 *                  the username.
	 * @param password
	 *                  the password.
	 * @param resource
	 *                  the resource.
	 * @param sendPresence
	 *                  if <tt>true</tt> an available presence will be
	 *                  sent automatically after login is completed.
	 * @throws XMPPException
	 *                   if an error occurs.
	 * @throws IllegalStateException
	 *                   if not connected to the server, or already logged
	 *                   in to the serrver.
	 */
	public Future login(String username, String password, String resource, Presence initPresence);

	/**
	 * Returns a ConnectionConfig
	 * 
	 * @return ConnectionConfig
	 */
	public ConnectionConfig getConnectionConfig();

	/**
	 * 
	 * @param connectionConfig
	 */
	public void setConnectionConfig(ConnectionConfig connectionConfig);
	
	/**
	 * Returns true if currently connected to the XMPP server.
	 * 
	 * @return true if connected.
	 */
	public boolean isConnected();

	/**
	 * Returns true if currently authenticated by successfully calling the
	 * login method.
	 * 
	 * @return true if authenticated.
	 */
	public boolean isAuthenticated();
	
	/**
	 * Closes the connection by setting presence to unavailable then
	 * closing the stream to the XMPP server
	 */
	public Future close();

	/**
	 * Closes the connection. A custom unavailable presence is sent to the
	 * server
	 * 
	 * @param unavailablePresence
	 *                  the presence packet to send during shutdown.
	 */
	public Future close(Presence unavailablePresence);

	/**
	 * Sends the specified stanza to the server.
	 * 
	 * @param stanza
	 *                  the stanza to send.
	 */
	public Future sendStanza(XMLStanza data);

	/**
	 * Creates a new stanza collector for this connection. A stanza filter
	 * determines which stanzas will be accumulated by the collector.
	 * 
	 * @param stanzaFilter
	 *                  the stanza filter to use.
	 * @return a new stanza collector.
	 */
	public StanzaCollector createStanzaCollector(StanzaFilter stanzaFilter);
	
	/*********************************************************************
	 * TLS code below
	 ********************************************************************/

	/**
	 * Returns true if the connection to the server has successfully
	 * negotiated TLS. Once TLS has been negotiatied the connection has
	 * been secured.
	 * 
	 * @return true if the connection to the server has successfully
	 *         negotiated TLS.
	 */
	public boolean isUsingTLS();

	/**
	 * Returns true if network traffic is being compressed. When using
	 * stream compression network traffic can be reduced up to 90%.
	 * Therefore, stream compression is ideal when using a slow speed
	 * network connection. However, the server will need to use more CPU
	 * time in order to un/compress network data so under high load the
	 * server performance might be affected.
	 * <p>
	 * <p/> Note: to use stream compression the smackx.jar file has to be
	 * present in the classpath.
	 * 
	 * @return true if network traffic is being compressed.
	 */
	public boolean isUsingCompression();

	/**
	 * 
	 * @return
	 */
	public boolean isSessionBinded();
	
	/**
	 * 
	 * @return
	 */
	public boolean isResourceBinded();
	
	/**
	 * Establishes a connection to the XMPP server and performs an
	 * automatic login only if the previous connection state was logged
	 * (authenticated). It basically creates and maintains a socket
	 * connection to the server.
	 * <p>
	 * <p/> Listeners will be preserved from a previous connection if the
	 * reconnection occurs after an abrupt termination.
	 * 
	 * @throws XMPPException
	 *                   if an error occurs while trying to establish the
	 *                   connection. Two possible errors can occur which
	 *                   will be wrapped by an XMPPException --
	 *                   UnknownHostException (XMPP error code 504), and
	 *                   IOException (XMPP error code 502). The error
	 *                   codes and wrapped exceptions can be used to
	 *                   present more appropiate error messages to
	 *                   end-users.
	 */
	public Future connect();
	
	/**
	 * 
	 * @return
	 */
	public String getConnectionID();

	/**
	 * 
	 * @return
	 */
	public long getLastActiveTime();
	
	/**
	 * 
	 * @return
	 */
	public long getLastReadTime();
	
	/**
	 * 
	 * @return
	 */
	public long getLastWriteTime();
	
	/**
	 * 
	 * @return
	 */
	public XMPPOwner getOwner();
	
	/**
	 * 
	 * @return
	 */
	public XMPPContactManager getContactManager();
}