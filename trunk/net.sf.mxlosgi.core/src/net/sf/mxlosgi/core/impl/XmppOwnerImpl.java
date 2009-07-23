/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mxlosgi.core.UserResource;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppOwner;
import net.sf.mxlosgi.utils.AbstractPropertied;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Presence;

/**
 * @author noah
 *
 */
public class XmppOwnerImpl  extends AbstractPropertied implements XmppOwner
{

	private final Logger logger = LoggerFactory.getLogger(XmppOwnerImpl.class);
	
	private XmppConnection connection;
	
	private Presence currentPresence;
	
	private Presence initPresence;
	
	private final Map<String, UserResource> otherResources = new ConcurrentHashMap<String, UserResource>();
	
	private XmppOwnerListenerServiceTracker xmppOwnerListenerServiceTracker;
	/**
	 * @param connection
	 */
	public XmppOwnerImpl(XmppConnection connection, 
						XmppOwnerListenerServiceTracker xmppOwnerListenerServiceTracker)
	{
		this.connection = connection;
		this.xmppOwnerListenerServiceTracker = xmppOwnerListenerServiceTracker;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.XMPPOwner#changePresence(net.sf.mxlosgi.mxlosgixmppbundle.Presence)
	 */
	@Override
	public void changePresence(Presence presence)
	{
		Presence.Type type = presence.getType();
		if (type != Presence.Type.available && type != Presence.Type.unavailable)
		{
			throw new IllegalArgumentException("presence type must be available or unavailable");
		}
		
		connection.sendStanza(presence);
		currentPresence = presence;
		
		logger.debug("owner status changed");
		
		xmppOwnerListenerServiceTracker.fireOwenrStatusChanged(this);
	}


	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.XMPPOwner#getConnection()
	 */
	@Override
	public XmppConnection getConnection()
	{
		return connection;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.XMPPOwner#getCurrentPresence()
	 */
	@Override
	public Presence getCurrentPresence()
	{
		return currentPresence;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.XMPPOwner#getOtherResource(java.lang.String)
	 */
	@Override
	public UserResource getOtherResource(String resource)
	{
		return otherResources.get(resource);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.XMPPOwner#getOtherResources()
	 */
	@Override
	public UserResource[] getOtherResources()
	{
		return otherResources.values().toArray(new UserResource[]{});
	}


	@Override
	public JID getJid()
	{
		return connection.getJid();
	}


	void handleOtherResource(Presence presence, String resource)
	{
		UserResource userResource = getOtherResource(resource);
		if (userResource == null)
		{
			userResource = new UserResourceImpl(resource);
			otherResources.put(resource, userResource);
		}
		
		UserResourceImpl userResourceImpl = (UserResourceImpl) userResource;
		userResourceImpl.setCurrentPresence(presence);
		
		if (presence.getType() == Presence.Type.unavailable)
		{
			otherResources.remove(resource);
		}
		
		logger.debug("owner other resource status changed");
		xmppOwnerListenerServiceTracker.fireOtherChanged(this, userResource);
	}

	void connectionClosed()
	{
		currentPresence = null;
		otherResources.clear();
	}

	@Override
	public Presence getInitPresence()
	{
		return initPresence;
	}

	@Override
	public void setInitPresence(Presence presence)
	{
		this.initPresence = presence;
	}
	
}
