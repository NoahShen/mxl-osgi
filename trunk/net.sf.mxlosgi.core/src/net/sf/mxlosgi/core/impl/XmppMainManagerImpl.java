/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mxlosgi.core.ConnectionConfig;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppMainManager;
import net.sf.mxlosgi.xmpp.JID;

/**
 * @author noah
 *
 */
public class XmppMainManagerImpl implements XmppMainManager
{
	private final Logger logger = LoggerFactory.getLogger(XmppMainManagerImpl.class);
	
	private XmppParserServiceTracker xmppParserServiceTracker;
	
	private SaslMechanismServiceTracker saslMechanismServiceTracker;
	
	private ConnectionListenerServiceTracker connectionListenerServiceTracker;
	
	private StanzaSendInterceptorServiceTracker stanzaSendInterceptorServiceTracker;
	
	private XmlStringListenerServiceTracker xmlStringListenerServiceTracker;
	
	private StanzaReceInterceptorServiceTracker stanzaReceInterceptorServiceTracker;
	
	private StanzaReceListenerServiceTracker stanzaReceListenerServiceTracker;
	
	private StanzaSendListenerServiceTracker stanzaSendListenerServiceTracker;
	
	private ContactListenerServiceTracker contactListenerServiceTracker;
	
	private XmppOwnerListenerServiceTracker xmppOwnerListenerServiceTracker;
	
	private final List<XmppConnection> connections = new CopyOnWriteArrayList<XmppConnection>();
	
	/**
	 * @param parser
	 * @param saslManager
	 */
	public XmppMainManagerImpl(XmppParserServiceTracker xmppParserServiceTracker, 
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
	public XmppConnection createConnection(String serviceName)
	{
		if (serviceName == null || serviceName.isEmpty())
		{
			throw new IllegalArgumentException("serviceName cannot be null");
		}
		
		ConnectionConfig config = new ConnectionConfig(serviceName);
		return createConnection(config);
	}

	@Override
	public XmppConnection createConnection(ConnectionConfig config)
	{
		XmppConnection connection = new XmppConnectionImpl(xmppParserServiceTracker, 
													saslMechanismServiceTracker,
													connectionListenerServiceTracker,
													xmlStringListenerServiceTracker,
													stanzaReceInterceptorServiceTracker,
													stanzaSendInterceptorServiceTracker,
													stanzaReceListenerServiceTracker,
													stanzaSendListenerServiceTracker,
													contactListenerServiceTracker,
													xmppOwnerListenerServiceTracker);
		if (config != null)
		{
			connection.setConnectionConfig(config);
		}
		connections.add(connection);
		
		logger.info("connection created");
		
		connectionListenerServiceTracker.fireConnectionCreated(connection);
		return connection;
	}

	@Override
	public XmppConnection[] getAllConnections()
	{
		return connections.toArray(new XmppConnection[]{});
	}

	@Override
	public XmppConnection[] getConnections(String serviceName)
	{
		ArrayList<XmppConnection> list = new ArrayList<XmppConnection>();
		for (XmppConnection connection : connections)
		{
			if (serviceName.equals(connection.getConnectionConfig().getServiceName()))
			{
				list.add(connection);
			}
		}
		return list.toArray(new XmppConnection[]{});
	}

	@Override
	public XmppConnection getConnections(JID jid)
	{
		for (XmppConnection connection : connections)
		{
			if (jid.equals(connection.getJid()))
			{
				return connection;
			}
		}
		return null;
	}
}
