/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.mxlosgi.core.ConnectionConfig;
import net.sf.mxlosgi.core.SubscriptionRequest;
import net.sf.mxlosgi.core.UserResource;
import net.sf.mxlosgi.core.XMPPConnection;
import net.sf.mxlosgi.core.XMPPContact;
import net.sf.mxlosgi.core.XMPPMainManager;
import net.sf.mxlosgi.core.XMPPOwner;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.core.interceptor.StanzaReceInterceptor;
import net.sf.mxlosgi.core.interceptor.StanzaSendInterceptor;
import net.sf.mxlosgi.core.listener.ConnectionListener;
import net.sf.mxlosgi.core.listener.ContactListener;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.core.listener.StanzaSendListener;
import net.sf.mxlosgi.core.listener.XMLStringListener;
import net.sf.mxlosgi.core.listener.XMPPOwnerListener;
import net.sf.mxlosgi.xmpp.Failure;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.StreamError;
import net.sf.mxlosgi.xmpp.XMLStanza;
import net.sf.mxlosgi.xmppparser.XMPPParser;

/**
 * @author noah
 *
 */
public class XMPPMainManagerImpl implements XMPPMainManager
{
	private XMPPParser parser;
	
	private SaslMechanismServiceTracker saslMechanismServiceTracker;
	
	private final List<XMPPConnection> connections = new CopyOnWriteArrayList<XMPPConnection>();
	
	private final List<ConnectionListener> connectionListeners = new CopyOnWriteArrayList<ConnectionListener>();
	
	private final List<XMLStringListener> xmlStringListeners = new CopyOnWriteArrayList<XMLStringListener>();
	
	private final Map<StanzaReceListener, StanzaFilter> stanzaReceListeners = new ConcurrentHashMap<StanzaReceListener, StanzaFilter>();

	private final Map<StanzaReceInterceptor, StanzaFilter> stanzaReceInterceptors = new ConcurrentHashMap<StanzaReceInterceptor, StanzaFilter>();

	private final Map<StanzaSendListener, StanzaFilter> stanzaSendListeners = new ConcurrentHashMap<StanzaSendListener, StanzaFilter>();

	private final Map<StanzaSendInterceptor, StanzaFilter> stanzaSendInterceptors = new ConcurrentHashMap<StanzaSendInterceptor, StanzaFilter>();
	
	private final List<ContactListener> contactListeners = new CopyOnWriteArrayList<ContactListener>();
	
	private final List<XMPPOwnerListener> ownerListeners = new CopyOnWriteArrayList<XMPPOwnerListener>();
	
	private static StanzaFilter emptyStanzaFilter = new StanzaFilter(){

		@Override
		public boolean accept(XMPPConnection connection, XMLStanza stanza)
		{
			return true;
		}

	};
	
	
	/**
	 * @param parser
	 * @param saslManager
	 */
	public XMPPMainManagerImpl(XMPPParser parser, SaslMechanismServiceTracker saslMechanismServiceTracker)
	{
		this.parser = parser;
		this.saslMechanismServiceTracker = saslMechanismServiceTracker;
	}
	
	private StanzaFilter checkFilter(StanzaFilter stanzaFilter)
	{
		if (stanzaFilter == null)
		{
			return emptyStanzaFilter;
		}
		return stanzaFilter;
	}

	@Override
	public void addConnectionListener(ConnectionListener connectionListener)
	{
		connectionListeners.add(connectionListener);
	}


	@Override
	public void addContactListener(ContactListener contactListener)
	{
		contactListeners.add(contactListener);
	}

	@Override
	public void addStanzaReceInterceptor(StanzaReceInterceptor stanzaReceInterceptor, StanzaFilter stanzaFilter)
	{
		stanzaFilter = checkFilter(stanzaFilter);
		stanzaReceInterceptors.put(stanzaReceInterceptor, stanzaFilter);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager#addStanzaReceListener(net.sf.mxlosgi.mxlosgimainbundle.listener.StanzaReceListener, net.sf.mxlosgi.mxlosgimainbundle.filter.StanzaFilter)
	 */
	@Override
	public void addStanzaReceListener(StanzaReceListener stanzaReceListener, StanzaFilter stanzaFilter)
	{
		stanzaFilter = checkFilter(stanzaFilter);
		stanzaReceListeners.put(stanzaReceListener, stanzaFilter);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager#addStanzaSendInterceptor(net.sf.mxlosgi.mxlosgimainbundle.interceptor.StanzaSendInterceptor, net.sf.mxlosgi.mxlosgimainbundle.filter.StanzaFilter)
	 */
	@Override
	public void addStanzaSendInterceptor(StanzaSendInterceptor stanzaSendInterceptor, StanzaFilter stanzaFilter)
	{
		stanzaFilter = checkFilter(stanzaFilter);
		stanzaSendInterceptors.put(stanzaSendInterceptor, stanzaFilter);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager#addStanzaSendListener(net.sf.mxlosgi.mxlosgimainbundle.listener.StanzaSendListener, net.sf.mxlosgi.mxlosgimainbundle.filter.StanzaFilter)
	 */
	@Override
	public void addStanzaSendListener(StanzaSendListener stanzaSendListener, StanzaFilter stanzaFilter)
	{
		stanzaFilter = checkFilter(stanzaFilter);
		stanzaSendListeners.put(stanzaSendListener, stanzaFilter);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager#addXMLStringListener(net.sf.mxlosgi.mxlosgimainbundle.listener.XMLStringListener)
	 */
	@Override
	public void addXMLStringListener(XMLStringListener xmlStringListener)
	{
		xmlStringListeners.add(xmlStringListener);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager#addXMPPOwnerListener(net.sf.mxlosgi.mxlosgimainbundle.listener.XMPPOwnerListener)
	 */
	@Override
	public void addXMPPOwnerListener(XMPPOwnerListener ownerListener)
	{
		ownerListeners.add(ownerListener);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager#createConnection()
	 */
	@Override
	public XMPPConnection createConnection()
	{
		return createConnection("");
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager#createConnection(java.lang.String)
	 */
	@Override
	public XMPPConnection createConnection(String serviceName)
	{
		XMPPConnection connection = new XMPPConnectionImpl(this, parser, saslMechanismServiceTracker);
		if (serviceName != null && !serviceName.isEmpty())
		{
			connection.getConnectionConfig().setServiceName(serviceName);
		}
		connections.add(connection);
		fireConnectionCreated(connection);
		return connection;
	}

	@Override
	public XMPPConnection createConnection(ConnectionConfig config)
	{
		XMPPConnection connection = new XMPPConnectionImpl(this, parser, saslMechanismServiceTracker);
		if (config != null)
		{
			connection.setConnectionConfig(config);
		}
		connections.add(connection);
		fireConnectionCreated(connection);
		return connection;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager#getAllConnections()
	 */
	@Override
	public XMPPConnection[] getAllConnections()
	{
		return connections.toArray(new XMPPConnection[]{});
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager#getConnections(java.lang.String)
	 */
	@Override
	public XMPPConnection[] getConnections(String serviceName)
	{
		ArrayList<XMPPConnection> list = new ArrayList<XMPPConnection>();
		for (XMPPConnection connection : connections)
		{
			if (serviceName.equals(connection.getConnectionConfig().getServiceName()))
			{
				list.add(connection);
			}
		}
		return list.toArray(new XMPPConnection[]{});
	}

	@Override
	public XMPPConnection getConnections(JID jid)
	{
		for (XMPPConnection connection : connections)
		{
			if (jid.equals(connection.getJid()))
			{
				return connection;
			}
		}
		return null;
	}

	@Override
	public void removeConnectionListener(ConnectionListener connectionListener)
	{
		connectionListeners.remove(connectionListener);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager#removeContactListener(net.sf.mxlosgi.mxlosgimainbundle.listener.ContactListener)
	 */
	@Override
	public void removeContactListener(ContactListener contactListener)
	{
		contactListeners.remove(contactListener);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager#removeStanzaReceInterceptor(net.sf.mxlosgi.mxlosgimainbundle.interceptor.StanzaReceInterceptor)
	 */
	@Override
	public void removeStanzaReceInterceptor(StanzaReceInterceptor stanzaReceInterceptor)
	{
		stanzaReceInterceptors.remove(stanzaReceInterceptor);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager#removeStanzaReceListener(net.sf.mxlosgi.mxlosgimainbundle.listener.StanzaReceListener)
	 */
	@Override
	public void removeStanzaReceListener(StanzaReceListener stanzaReceListener)
	{
		stanzaReceListeners.remove(stanzaReceListener);
	}


	@Override
	public void removeStanzaSendInterceptor(StanzaSendInterceptor stanzaSendInterceptor)
	{
		stanzaSendInterceptors.remove(stanzaSendInterceptor);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager#removeStanzaSendListener(net.sf.mxlosgi.mxlosgimainbundle.listener.StanzaSendListener)
	 */
	@Override
	public void removeStanzaSendListener(StanzaSendListener stanzaSendListener)
	{
		stanzaSendListeners.remove(stanzaSendListener);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager#removeXMLStringListener(net.sf.mxlosgi.mxlosgimainbundle.listener.XMLStringListener)
	 */
	@Override
	public void removeXMLStringListener(XMLStringListener xmlStringListener)
	{
		xmlStringListeners.remove(xmlStringListener);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager#removeXMPPOwnerListener(net.sf.mxlosgi.mxlosgimainbundle.listener.XMPPOwnerListener)
	 */
	@Override
	public void removeXMPPOwnerListener(XMPPOwnerListener ownerListener)
	{
		ownerListeners.remove(ownerListener);
	}
	
	void fireStanzaSendListener(XMPPConnection connection, XMLStanza stanza)
	{
		for (Map.Entry<StanzaSendListener, StanzaFilter> entry : stanzaSendListeners.entrySet())
		{
			StanzaSendListener listener = entry.getKey();
			StanzaFilter filter = entry.getValue();
			if (filter == null || filter.accept(connection, stanza))
			{
				listener.processSendStanza(connection, stanza);
			}
		}
	}
	
	
	void fireStreamError(XMPPConnection connection, StreamError streamError)
	{
		for (ConnectionListener listener : connectionListeners)
		{
			listener.connectionStreamError(connection, streamError);
		}
	}


	void fireSASLFailed(XMPPConnection connection, Failure failure)
	{
		for (ConnectionListener listener : connectionListeners)
		{
			listener.saslFailed(connection, failure);
		}
	}

	private void fireConnectionCreated(XMPPConnection connection)
	{
		for (ConnectionListener listener : connectionListeners)
		{
			listener.connectionCreated(connection);
		}
	}
	
	void fireConnectionClosed(XMPPConnection connection)
	{
		connections.remove(connection);
		
		for (ConnectionListener listener : connectionListeners)
		{
			listener.connectionClosed(connection);
		}
	}

	void fireSASLSuccess(XMPPConnection connection)
	{
		for (ConnectionListener listener : connectionListeners)
		{
			listener.saslSuccessful(connection);
		}
	}
	

	void fireSessionBinded(XMPPConnection connection)
	{
		for (ConnectionListener listener : connectionListeners)
		{
			listener.sessionBinded(connection);
		}
	}
	
	void fireConnected(XMPPConnection connection)
	{
		for (ConnectionListener listener : connectionListeners)
		{
			listener.connectionConnected(connection);
		}
	}


	void fireExceptionCaught(XMPPConnection connection, Throwable cause)
	{
		for (ConnectionListener listener : connectionListeners)
		{
			listener.exceptionCaught(connection, cause);
		}
	}
	

	void fireResourceBinded(XMPPConnection connection)
	{
		for (ConnectionListener listener : connectionListeners)
		{
			listener.resourceBinded(connection);
		}
	}


	void fireXMLStringListener(XMPPConnection connection, String xml)
	{
		for (XMLStringListener listener : xmlStringListeners)
		{
			listener.processXMLString(connection, xml);
		}
	}


	boolean fireStanzaReceInterceptor(XMPPConnection connection, XMLStanza stanza)
	{
		for (Map.Entry<StanzaReceInterceptor, StanzaFilter> entry : stanzaReceInterceptors.entrySet())
		{
			StanzaReceInterceptor interceptor = entry.getKey();
			StanzaFilter filter = entry.getValue();
			if (filter == null || filter.accept(connection, stanza))
			{
				if (interceptor.interceptReceStanza(connection, stanza))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	boolean fireStanzaSendInterceptor(XMPPConnection connection, XMLStanza stanza)
	{
		for (Map.Entry<StanzaSendInterceptor, StanzaFilter> entry : stanzaSendInterceptors.entrySet())
		{
			StanzaSendInterceptor interceptor = entry.getKey();
			StanzaFilter filter = entry.getValue();
			if (filter == null || filter.accept(connection, stanza))
			{
				if (interceptor.interceptSendStanza(connection, stanza))
				{
					return true;
				}
			}
		}
		return false;
	}

	
	void fireStanzaReceListener(XMPPConnection connection, XMLStanza stanza)
	{
		for (Map.Entry<StanzaReceListener, StanzaFilter> entry : stanzaReceListeners.entrySet())
		{
			StanzaReceListener listener = entry.getKey();
			StanzaFilter filter = entry.getValue();
			if (filter == null || filter.accept(connection, stanza))
			{
				listener.processReceStanza(connection, stanza);
			}
		}
	}
	

	void fireOwenrStatusChanged(XMPPOwner owner)
	{
		for (XMPPOwnerListener listener : ownerListeners)
		{
			listener.ownerStatusChanged(owner);
		}
	}
	
	void fireOtherChanged(XMPPOwner owner,  UserResource userResource)
	{
		for (XMPPOwnerListener listener : ownerListeners)
		{
			listener.ownerOtherResourceStatusChanged(owner, userResource);
		}
	}
	

	void fireContactRemoved(XMPPConnection connection, JID jid)
	{
		for (ContactListener listener : contactListeners)
		{
			listener.contactRemovedCompleted(connection, jid);
		}
	}

	void fireContactUpdated(XMPPConnection connection, XMPPContact contact)
	{
		for (ContactListener listener : contactListeners)
		{
			listener.contactUpdated(connection, contact);
		}
	}
	
	void fireContactUnsubscribed(XMPPConnection connection, JID jid)
	{
		for (ContactListener listener : contactListeners)
		{
			listener.contactUnsubscribed(connection, jid);
		}
	}
	

	void fireContactUnsubscribeMe(XMPPConnection connection, JID jid)
	{
		for (ContactListener listener : contactListeners)
		{
			listener.contactUnsubscribeMe(connection, jid);
		}
	}
	
	void fireContactSubscribed(XMPPConnection connection, JID jid)
	{
		for (ContactListener listener : contactListeners)
		{
			listener.contactSubscribed(connection, jid);
		}
	}
	
	void fireContactSubscribeMe(XMPPConnection connection, SubscriptionRequest request)
	{
		for (ContactListener listener : contactListeners)
		{
			listener.contactSubscribeMe(connection, request);
		}
	}
	
	void fireContactStatusChanged(XMPPConnection connection, XMPPContact contact, UserResource resource)
	{
		for (ContactListener listener : contactListeners)
		{
			listener.contactStatusChanged(connection, contact, resource);
		}
	}
	

}
