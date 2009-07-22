/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoFilterChain;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.ThreadModel;
import org.apache.mina.filter.CompressionFilter;
import org.apache.mina.filter.SSLFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;

import net.sf.mxlosgi.core.BindResourceException;
import net.sf.mxlosgi.core.BindSessionException;
import net.sf.mxlosgi.core.CompressFailureException;
import net.sf.mxlosgi.core.ConnectionConfig;
import net.sf.mxlosgi.core.Future;
import net.sf.mxlosgi.core.ServerTimeoutException;
import net.sf.mxlosgi.core.StanzaCollector;
import net.sf.mxlosgi.core.TlsFailedException;
import net.sf.mxlosgi.core.TlsRequiredExcetpion;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppContactManager;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.core.XmppOwner;
import net.sf.mxlosgi.core.ConnectionConfig.SecurityMode;
import net.sf.mxlosgi.core.filter.PacketIDFilter;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.sasl.SaslMechanism;
import net.sf.mxlosgi.utils.AbstractPropertied;
import net.sf.mxlosgi.xmpp.Auth;
import net.sf.mxlosgi.xmpp.Challenge;
import net.sf.mxlosgi.xmpp.CloseStream;
import net.sf.mxlosgi.xmpp.Compress;
import net.sf.mxlosgi.xmpp.Compressed;
import net.sf.mxlosgi.xmpp.Failure;
import net.sf.mxlosgi.xmpp.IQ;
import net.sf.mxlosgi.xmpp.IQBind;
import net.sf.mxlosgi.xmpp.IQRoster;
import net.sf.mxlosgi.xmpp.IQSession;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmpp.Presence;
import net.sf.mxlosgi.xmpp.Proceed;
import net.sf.mxlosgi.xmpp.Response;
import net.sf.mxlosgi.xmpp.StartTLS;
import net.sf.mxlosgi.xmpp.Stream;
import net.sf.mxlosgi.xmpp.StreamError;
import net.sf.mxlosgi.xmpp.StreamFeature;
import net.sf.mxlosgi.xmpp.Success;
import net.sf.mxlosgi.xmpp.XMLStanza;
import net.sf.mxlosgi.xmpp.StreamFeature.Feature;

/**
 * @author noah
 *
 */
public class XmppConnectionImpl extends AbstractPropertied implements XmppConnection, IoHandler
{
	
	private XmppOwnerImpl owner;
	
	private XmppContactManagerImpl contactManager;

	private JID jid;
	
	private String inputedLoginUsername;
	
	private String inputedLoginResource;
	
	private String password;
	
	private ConnectionConfig config;
	
	private boolean connected = false;

	private boolean authenticated = false;

	private boolean sessionBinded = false;
	
	private boolean resourceBinded = false;
	
	private boolean usingCompression = false;
	
	private String connectionID;
	
	private SaslMechanismServiceTracker saslMechanismServiceTracker;
	
	private Set<String> serviceAllowedMechanisms = new HashSet<String>();

	private SaslMechanism currentsSASLMechanism;

	private SocketConnector connector;

	private IoSession iosession;
	
	private final Collection<StanzaCollector> collectors = new ConcurrentLinkedQueue<StanzaCollector>();

	private ExecutorService executorService = Executors.newCachedThreadPool();

	private ConnectFuture connectFuture;

	private LoginFuture loginFuture;

	private CloseFuture closeFurture;

	private SendFuture sendFuture;
	
	private ConnectionListenerServiceTracker connectionListenerServiceTracker;
	
	private StanzaSendInterceptorServiceTracker stanzaSendInterceptorServiceTracker;
	
	private XmlStringListenerServiceTracker xmlStringListenerServiceTracker;
	
	private StanzaReceInterceptorServiceTracker stanzaReceInterceptorServiceTracker;
	
	private StanzaReceListenerServiceTracker stanzaReceListenerServiceTracker;
	
	private StanzaSendListenerServiceTracker stanzaSendListenerServiceTracker;
	
	private ContactListenerServiceTracker contactListenerServiceTracker;
	
	private XmppOwnerListenerServiceTracker xmppOwnerListenerServiceTracker;
	
	private XmppParserServiceTracker xmppParserServiceTracker;
	/**
	 * @param parser
	 * @param saslManager
	 */
	public XmppConnectionImpl(XmppParserServiceTracker xmppParserServiceTracker, 
							SaslMechanismServiceTracker saslMechanismServiceTracker,
							ConnectionListenerServiceTracker connectionListenerServiceTracker,
							XmlStringListenerServiceTracker xmlStringListenerServiceTracker,
							StanzaReceInterceptorServiceTracker stanzaReceInterceptorServiceTracker,
							StanzaSendInterceptorServiceTracker stanzaSendInterceptorServiceTracker,
							StanzaReceListenerServiceTracker stanzaReceListenerServiceTracker,
							StanzaSendListenerServiceTracker stanzaSendListenerServiceTracker,
							ContactListenerServiceTracker contactListenerServiceTracker,
							XmppOwnerListenerServiceTracker xmppOwnerListenerServiceTracker)
	{
		this.xmppParserServiceTracker = xmppParserServiceTracker;
		this.saslMechanismServiceTracker = saslMechanismServiceTracker;
		this.connectionListenerServiceTracker = connectionListenerServiceTracker;
		this.xmlStringListenerServiceTracker = xmlStringListenerServiceTracker;
		this.stanzaReceInterceptorServiceTracker = stanzaReceInterceptorServiceTracker;
		this.stanzaSendInterceptorServiceTracker = stanzaSendInterceptorServiceTracker;
		this.stanzaReceListenerServiceTracker = stanzaReceListenerServiceTracker;
		this.stanzaSendListenerServiceTracker = stanzaSendListenerServiceTracker;
		this.contactListenerServiceTracker = contactListenerServiceTracker;
		this.xmppOwnerListenerServiceTracker = xmppOwnerListenerServiceTracker;
	}
	
	@Override
	public synchronized Future connect()
	{
		if (isConnected())
		{
			throw new IllegalStateException("already connected");
		}
		
		if (config == null)
		{
			throw new NullPointerException("ConnectionConfig is null");
		}
		
		String serviceName = config.getServiceName();
		if (serviceName == null && serviceName.isEmpty()	)
		{
			throw new NullPointerException("serviceName has not been set");
		
		}
		
		executorService.execute(new Runnable(){

			@Override
			public void run()
			{
				String host = config.getHost();
				
				SocketConnectorConfig socketConnectorConfig = new SocketConnectorConfig();
				socketConnectorConfig.setConnectTimeout(getConnectionConfig().getConnectionTimeout());
				socketConnectorConfig.getFilterChain().addLast("XMPPCodec", new ProtocolCodecFilter(new XmppCodecFactory()));
				socketConnectorConfig.setThreadModel(ThreadModel.MANUAL);
				
				connector = new SocketConnector(Runtime.getRuntime().availableProcessors() + 1, Executors.newCachedThreadPool());
				InetSocketAddress address =new InetSocketAddress(host, config.getPort());
				
				connector.connect(address, XmppConnectionImpl.this, socketConnectorConfig);
			}
			
		});
		
		connectFuture = new ConnectFuture(this);
		return connectFuture;
	}

	@Override
	public StanzaCollector createStanzaCollector(StanzaFilter stanzaFilter)
	{
		StanzaCollector collector = new StanzaCollectorImpl(this, stanzaFilter);
		collectors.add(collector);
		
		return collector;
	}

	@Override
	public synchronized Future close()
	{
		return close(null);
	}

	@Override
	public synchronized Future close(Presence unavailablePresence)
	{
		if (iosession == null || !iosession.isConnected())
		{
			return closeFurture;
		}
		
		if (unavailablePresence != null)
		{
			sendStanza(unavailablePresence);
		}
		
		sendStanza(new CloseStream());
		closeFurture = new CloseFuture(this, iosession.close());
		return closeFurture;
	}


	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiconnectionbundle.XMPPConnection#getConnectionConfig()
	 */
	@Override
	public ConnectionConfig getConnectionConfig()
	{
		if (config == null)
		{
			synchronized(this)
			{
				if (config == null)
				{
					config = new ConnectionConfig();
				}
			}
		}
		return config;
	}

	@Override
	public void setConnectionConfig(ConnectionConfig connectionConfig)
	{
		this.config = connectionConfig;
	}

	@Override
	public String getHost()
	{
		return config.getHost();
	}

	@Override
	public int getPort()
	{
		return config.getPort();
	}

	@Override
	public boolean isAuthenticated()
	{
		return authenticated;
	}

	@Override
	public boolean isConnected()
	{
		return connected;
	}

	@Override
	public boolean isUsingCompression()
	{
		return usingCompression;
	}

	@Override
	public boolean isUsingTLS()
	{
		if (iosession != null)
		{
			SSLFilter filter = (SSLFilter) iosession.getFilterChain().get("TLS");
			if (filter != null)
				return filter.isSSLStarted(iosession);
		}
		
		return false;
	}

	@Override
	public boolean isSessionBinded()
	{
		return sessionBinded;
	}

	@Override
	public boolean isResourceBinded()
	{
		return resourceBinded;
	}

	@Override
	public Future login(String username, String password)
	{
		return login(username, password, "MxlOSGi");
	}

	@Override
	public Future login(String username, String password, String resource)
	{
		return login(username, password, resource, new Presence(Presence.Type.available));
	}

	@Override
	public Future login(String username, String password, String resource, Presence initPresence)
	{
		if (!isConnected())
		{
			throw new IllegalStateException("not connected");
		}
		if (isAuthenticated())
		{
			throw new IllegalStateException("sasl has completed");
		}
		
		if (username == null)
		{
			throw new NullPointerException("username is null");
		}
		if (password == null)
		{
			throw new NullPointerException("password is null");
		}
		if (resource == null)
		{
			throw new NullPointerException("resource is null");
		}
		
		this.inputedLoginUsername = username;
		this.inputedLoginResource = resource;
		this.password = password;
		
		loginFuture = new LoginFuture(this);
		
		getOwner().setInitPresence(initPresence);
		
		String plain = "PLAIN";
		for (String mechanism : serviceAllowedMechanisms)
		{
			String upperMechanism = mechanism.toUpperCase();
			if (!plain.equals(upperMechanism))
			{
				currentsSASLMechanism = saslMechanismServiceTracker.getSaslMechanism(upperMechanism);
				if (currentsSASLMechanism != null)
				{
					handleSASL(currentsSASLMechanism);
					
					return loginFuture;
				}
			}
		}
		
		currentsSASLMechanism = saslMechanismServiceTracker.getSaslMechanism(plain);
		if (currentsSASLMechanism == null)
		{
			connectionListenerServiceTracker.fireExceptionCaught(this, new XmppException("SASL failed:no SASLMechanism supported"));
			close();
			return loginFuture;
		}
		
		handleSASL(currentsSASLMechanism);
		return loginFuture;
	}

	private void handleSASL(SaslMechanism saslMechanism)
	{
		Auth auth = new Auth();
		auth.setMechanism(saslMechanism.getName());
		try
		{
			auth.setContent(saslMechanism.getAuthenticationText(inputedLoginUsername.toLowerCase(), password, config.getHost()));
		}
		catch (Exception e)
		{
			connectionListenerServiceTracker.fireExceptionCaught(this, e);
			return;
		}
		
		sendStanza(auth);
	}

	@Override
	public synchronized Future sendStanza(XMLStanza stanza)
	{
		sendFuture = new SendFuture(this);
		if (iosession == null || !iosession.isConnected())
		{
			throw new IllegalStateException("not connected");
		}
		
		if (stanzaSendInterceptorServiceTracker.fireStanzaSendInterceptor(this, stanza))
		{
			sendFuture.setIntercepted(true);
			return sendFuture;
		}
		sendFuture.setWriteFuture(iosession.write(stanza));
		return sendFuture;
	}

	void cancelStanzaCollector(StanzaCollector stanzaCollector)
	{
		collectors.remove(stanzaCollector);
	}

	private void resetState()
	{
		connector = null;
		serviceAllowedMechanisms.clear();
		currentsSASLMechanism = null;
		connectionID = null;
		connected = false;
		authenticated = false;
		sessionBinded = false;
		resourceBinded = false;
		usingCompression = false;
	}
	
	
	@Override
	public String getConnectionID()
	{
		return connectionID;
	}

	@Override
	public long getLastActiveTime()
	{
		long lastRead = getLastReadTime();
		long lastWrite = getLastWriteTime();
		
		return lastRead > lastWrite ? lastRead : lastWrite;
	}
	
	public long getLastReadTime()
	{
		if (iosession != null)
		{
			return iosession.getLastReadTime();
		}
		return -1;
	}

	public long getLastWriteTime()
	{
		if (iosession != null)
		{
			return iosession.getLastWriteTime();
		}
		return -1;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception
	{
		notifyConnectFuture();
		notifyLoginFuture();
		notifyCloseFuture();
		connectionListenerServiceTracker.fireExceptionCaught(this, cause);
	}


	@Override
	public void messageReceived(IoSession session, Object message) throws Exception
	{
		if (message instanceof String)
		{
			String xml = message.toString();
			handleXML(xml);
		}
		
	}


	private void handleXML(String xml) throws Exception
	{
		xmlStringListenerServiceTracker.fireXmlStringListener(this, xml);

		if (xml.startsWith("<?xml"))
		{
			return;
		}

		XMLStanza stanza = xmppParserServiceTracker.getParser().parseXML(xml);

		if (stanza == null)
		{
			return;
		}

		if (stanzaReceInterceptorServiceTracker.fireStanzaReceInterceptor(this, stanza))
		{
			return;
		}

		fireCollector(stanza);
		
		stanzaReceListenerServiceTracker.fireStanzaReceListener(this, stanza);
		
		if (stanza instanceof Stream)
		{
			handleStream((Stream) stanza);
		}
		else if (stanza instanceof StreamFeature)
		{
			handleStreamFeature((StreamFeature) stanza);
		}
		else if (stanza instanceof StreamError)
		{
			handleStreamError((StreamError) stanza);
		}
		else if (stanza instanceof Proceed)
		{
			proceedTLS();
		}
		else if (stanza instanceof Challenge)
		{
			handleChallenge((Challenge) stanza);
		}
		else if (stanza instanceof Success)
		{
			handleSASLSuccess();
		}
		else if (stanza instanceof CloseStream)
		{
			handleCloseStream();
		}
		else if (stanza instanceof Failure)
		{
			handleFailure((Failure) stanza);
		}
		if (stanza instanceof StreamError)
		{
			handleStreamError((StreamError) stanza);
		}
		else if (stanza instanceof CloseStream)
		{
			handleCloseStream();
		}
		else if (stanza instanceof Failure)
		{
			handleFailure((Failure) stanza);
		}
		else if (stanza instanceof Presence)
		{
			handlePresence((Presence) stanza);
		}
		else if (stanza instanceof IQ)
		{
			handleIQ((IQ) stanza);
		}
	}


	private void handleIQ(IQ iq)
	{
		PacketExtension extension = iq.getExtension("query", "jabber:iq:roster");
		if (extension != null && iq.getType() == IQ.Type.result)
		{
			((XmppContactManagerImpl)getContactManager()).updateContact((IQRoster) extension);
		}
	}


	private void handlePresence(Presence presence)
	{
		if (jid == null)
		{
			return;
		}
		
		JID from = presence.getFrom();
		Presence.Type type = presence.getType();
		if (type == Presence.Type.available || type == Presence.Type.unavailable)
		{
			if (from.equalsWithBareJid(owner.getJid()))
			{
				if (!jid.getResource().equals(from.getResource()))
				{
					((XmppOwnerImpl)getOwner()).handleOtherResource(presence, from.getResource());
				}
			}
			else
			{
				((XmppContactManagerImpl)getContactManager()).handleStatusChanged(presence);
			}
		}
		else if (type == Presence.Type.subscribe)
		{
			((XmppContactManagerImpl)getContactManager()).contactSubscribeMe(presence);
		}
		else if (type == Presence.Type.subscribed)
		{
			((XmppContactManagerImpl)getContactManager()).contactSubscribed(presence);
		}
		else if (type == Presence.Type.unsubscribe)
		{
			((XmppContactManagerImpl)getContactManager()).contactUnsubscribeMe(presence);
		}
		else if (type == Presence.Type.unsubscribed)
		{
			((XmppContactManagerImpl)getContactManager()).contactUnsubscribed(presence);
		}
	}


	private void handleSASLSuccess()
	{
		authenticated = true;
		connectionListenerServiceTracker.fireSaslSuccess(this);
		openStream();
	}


	private void proceedTLS() throws IOException, TlsFailedException
	{
		try
		{
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, 
					new javax.net.ssl.TrustManager[] { new ServerTrustManager(getConnectionConfig().getServiceName()) }, 
					new java.security.SecureRandom());
			SSLFilter sslFilter = new SSLFilter(context);
			sslFilter.setUseClientMode(true);
			iosession.getFilterChain().addFirst("TLS", sslFilter);
			
			sslFilter.startSSL(iosession);
			openStream();
		}
		catch (KeyManagementException e)
		{
			throw new TlsFailedException(e);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new TlsFailedException(e);
		}
	}
	
	private void handleChallenge(Challenge challenge) throws Exception
	{
		if (currentsSASLMechanism != null)
		{
			String content = currentsSASLMechanism.getChallengeResponse(challenge.getContent());
			Response response = new Response();
			response.setContent(content);
			sendStanza(response);
		}
	}


	private void handleStream(Stream stream) throws XmppException
	{
		JID from = stream.getFrom();
		String version = stream.getVersion();
		String connectionID = stream.getStanzaID();
		
		if (version != null && !"1.0".equals(version))
		{
			close();
			throw new XmppException("stream version has not been support");
		}
		
		if (from != null)
		{
			config.setServiceName(from.toFullJID());
		}
		
		this.connectionID = connectionID;
	}
	
	private void handleStreamFeature(StreamFeature streamFeature) throws IOException, XmppException
	{
		if (!streamFeature.getCompressionMethod().isEmpty()
				&& getConnectionConfig().isCompressionEnabled() 
				&& isAuthenticated() 
				&& !isUsingCompression())
		{
			for (String compressionMethod : streamFeature.getCompressionMethod())
			{
				if ("zlib".equals(compressionMethod))
				{
					startCompress(compressionMethod);
					break;
				}
			}
			return;
		}
		
		for (StreamFeature.Feature feature : streamFeature.getFeatures())
		{
			String elementName = feature.getElementName();
			if ("starttls".equals(elementName))
			{
				if (handleTLS(feature))
				{
					return;
				}
			}
			else if ("bind".equals(elementName))
			{
				bindResource(streamFeature);
				return;
			}
		}
		
		SecurityMode mode = getConnectionConfig().getSecurityMode();
		if (!isUsingTLS() && mode == SecurityMode.required)
		{
			close();
			throw new TlsRequiredExcetpion("The server not support TLS");
		}
		
		if (!streamFeature.getMechanisms().isEmpty())
		{
			serviceAllowedMechanisms.clear();
			serviceAllowedMechanisms.addAll(streamFeature.getMechanisms());
		}
		
		connected = true;
		
		notifyConnectFuture();
		
		connectionListenerServiceTracker.fireConnected(this);
	}
	

	private void notifyConnectFuture()
	{
		if (connectFuture != null)
		{
			synchronized (connectFuture)
			{
				connectFuture.notify();
			}
		}
		
	}

	private void startCompress(final String compressionMethod) throws ServerTimeoutException, CompressFailureException
	{
		executorService.execute(new Runnable(){

			@Override
			public void run()
			{
				Compress compress = new Compress(compressionMethod);
				
				StanzaCollector collector = createStanzaCollector(null);
				sendStanza(compress);
				
				XMLStanza stanza = collector.nextResult(getConnectionConfig().getResponseStanzaTimeout());
				collector.cancel();
				
				if (stanza == null)
				{
					connectionListenerServiceTracker.fireExceptionCaught(XmppConnectionImpl.this, 
							new ServerTimeoutException("remote server no response"));
					return;
				}
				else if (stanza instanceof Compressed)
				{
					IoFilterChain chain = iosession.getFilterChain();
					chain.addBefore("XMPPCodec", 
								"compression", 
								new CompressionFilter(true, true, CompressionFilter.COMPRESSION_MAX));
					usingCompression = true;
					openStream();
				}
				else if (stanza instanceof Failure)
				{
					connectionListenerServiceTracker.fireExceptionCaught(XmppConnectionImpl.this, 
							new CompressFailureException((Failure) stanza));
				}
			}
		});
		
	}


	private void bindResource(final StreamFeature streamFeature) throws ServerTimeoutException, 
													BindResourceException, 
													BindSessionException
	{
		executorService.execute(new Runnable(){

			@Override
			public void run()
			{
				IQ iq = new IQ(IQ.Type.set);
				IQBind bind = new IQBind();
				bind.setResource(inputedLoginResource);
				iq.addExtension(bind);

				StanzaCollector collector = createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
				sendStanza(iq);
				XMLStanza stanza = collector.nextResult(getConnectionConfig().getResponseStanzaTimeout());
				collector.cancel();

				if (stanza == null)
				{
					connectionListenerServiceTracker.fireExceptionCaught(XmppConnectionImpl.this, 
											new ServerTimeoutException("remote server no response"));
					return;
				}

				if (stanza instanceof IQ)
				{
					IQ iqResponse = (IQ) stanza;
					IQ.Type type = iqResponse.getType();
					if (type == IQ.Type.result)
					{
						IQBind iqBind = (IQBind) iqResponse.getExtension("bind", "urn:ietf:params:xml:ns:xmpp-bind");
						JID jid = iqBind.getJid();
						XmppConnectionImpl.this.jid = jid;
						resourceBinded = true;
						connectionListenerServiceTracker.fireResourceBinded(XmppConnectionImpl.this);
						for (StreamFeature.Feature feature : streamFeature.getFeatures())
						{
							String elementName = feature.getElementName();
							if ("session".equals(elementName))
							{
								try
								{
									bindSession();
								}
								catch (Exception e)
								{
									connectionListenerServiceTracker.fireExceptionCaught(XmppConnectionImpl.this, e);
									return;
								}
							}
						}
					}
					else if (type == IQ.Type.error)
					{
						connectionListenerServiceTracker.fireExceptionCaught(XmppConnectionImpl.this, 
													new BindResourceException(iqResponse.getError()));
					}
				}
			}
			
		});
		
	}


	private void bindSession() throws ServerTimeoutException, BindSessionException
	{
		IQ iq = new IQ(IQ.Type.set);
		IQSession session = new IQSession();
		iq.addExtension(session);
		StanzaCollector collector = createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		sendStanza(iq);
		XMLStanza stanza = collector.nextResult(getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (stanza == null)
		{
			throw new ServerTimeoutException("remote server no response");
		}
		if (stanza instanceof IQ)
		{
			IQ iqResponse = (IQ) stanza;
			IQ.Type type = iqResponse.getType();
			if (type == IQ.Type.result)
			{
				sessionBinded = true;
				
				notifyLoginFuture();
				
				connectionListenerServiceTracker.fireSessionBinded(this);
				
				((XmppContactManagerImpl)getContactManager()).queryRoster();
				
				if (owner.getInitPresence() != null)
				{
					owner.changePresence(owner.getInitPresence());
				}
			}
			else if (type == IQ.Type.error)
			{
				throw new BindSessionException(iqResponse.getError());
			}
		}
	}

	private void notifyLoginFuture()
	{
		if (loginFuture != null)
		{
			synchronized (loginFuture)
			{
				loginFuture.notify();
			}
		}
		
	}

	private boolean handleTLS(Feature feature) throws IOException, XmppException
	{
		ConnectionConfig.SecurityMode mode = config.getSecurityMode();
		if (feature.isRequired())
		{
			if (mode == ConnectionConfig.SecurityMode.disabled)
			{
				close();
				throw new TlsRequiredExcetpion("The service require to use TLS");
			}
			else
			{
				startTLS();
				return true;
			}
		}
		else
		{
			if (mode != ConnectionConfig.SecurityMode.disabled)
			{
				startTLS();
				return true;
			}
		}

		if (!isUsingTLS() && mode == SecurityMode.required)
		{
			close();
			throw new TlsRequiredExcetpion("The server not support TLS");
		}
		return false;
	}
	
	private void startTLS() throws IOException, XmppException
	{
		StartTLS startTLS = new StartTLS();
		sendStanza(startTLS);
	}
	
	private void fireCollector(XMLStanza data)
	{
		for (StanzaCollector collector : collectors)
		{
			collector.processPacket(this, data);
		}
	}
	
	private void handleFailure(Failure failure)
	{
		if ("urn:ietf:params:xml:ns:xmpp-tls".equals(failure.getNamespace()))
		{
			connectionListenerServiceTracker.fireExceptionCaught(this, new TlsFailedException(failure));
		}
		else if ("urn:ietf:params:xml:ns:xmpp-sasl".equals(failure.getNamespace()))
		{
			connectionListenerServiceTracker.fireSaslFailed(this, failure);
		}
	}


	private void handleStreamError(StreamError streamError)
	{
		connectionListenerServiceTracker.fireStreamError(this, streamError);
	}
	
	private void handleCloseStream()
	{
		close();
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception
	{
		if (message instanceof XMLStanza)
		{
			stanzaSendListenerServiceTracker.fireStanzaSendListener(this, (XMLStanza) message);
		}
	}


	@Override
	public void sessionClosed(IoSession session) throws Exception
	{
		notifyCloseFuture();
		((XmppOwnerImpl)getOwner()).connectionClosed();
		((XmppContactManagerImpl)getContactManager()).connectionClosed();
		resetState();
		connectionListenerServiceTracker.fireConnectionClosed(this);
	}
	
	private void notifyCloseFuture()
	{
		if (closeFurture == null)
		{
			return;
		}
		
		synchronized (closeFurture)
		{
			closeFurture.notify();
		}
		
	}


	@Override
	public void sessionCreated(IoSession session) throws Exception
	{
		session.setIdleTime(IdleStatus.READER_IDLE, getConnectionConfig().getResponseStanzaTimeout() / 1000);
		iosession = session;
	}


	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception
	{
		if (!isSessionBinded())
		{
			if (status == IdleStatus.READER_IDLE && iosession == session)
			{
				close();
				throw new XmppException("server no response");
			}
		}
		
	}


	@Override
	public void sessionOpened(IoSession session) throws Exception
	{
		openStream();
	}
	
	private void openStream()
	{
		Stream stream = new Stream();
		stream.setStanzaID(Stream.ID_NOT_AVAILABLE);
		stream.setTo(new JID(getConnectionConfig().getServiceName()));
		sendStanza(stream);
	}

	@Override
	public XmppContactManager getContactManager()
	{
		if (contactManager == null)
		{
			synchronized(this)
			{
				if (contactManager == null)
				{
					contactManager = new XmppContactManagerImpl(this, contactListenerServiceTracker);
				}
			}
		}
		return contactManager;
	}


	@Override
	public JID getJid()
	{
		return jid;
	}


	@Override
	public XmppOwner getOwner()
	{
		if (owner == null)
		{
			synchronized(this)
			{
				if (owner == null)
				{
					owner = new XmppOwnerImpl(this, xmppOwnerListenerServiceTracker);
				}
			}
		}
		return owner;
	}

}
