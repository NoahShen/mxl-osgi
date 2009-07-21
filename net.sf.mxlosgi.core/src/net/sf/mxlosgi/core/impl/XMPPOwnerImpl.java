/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.mxlosgi.core.UserResource;
import net.sf.mxlosgi.core.XMPPConnection;
import net.sf.mxlosgi.core.XMPPOwner;
import net.sf.mxlosgi.utils.AbstractPropertied;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Presence;

/**
 * @author noah
 *
 */
public class XMPPOwnerImpl  extends AbstractPropertied implements XMPPOwner
{

	private XMPPConnection connection;
	
	private XMPPMainManagerImpl mainManager;
	
	private Presence currentPresence;
	
	private Presence initPresence;
	
	private final Map<String, UserResource> otherResources = new ConcurrentHashMap<String, UserResource>();
	
	/**
	 * @param connection
	 */
	public XMPPOwnerImpl(XMPPConnection connection, XMPPMainManagerImpl mainManager)
	{
		this.connection = connection;
		this.mainManager = mainManager;
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
		
		mainManager.fireOwenrStatusChanged(this);
	}


	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.XMPPOwner#getConnection()
	 */
	@Override
	public XMPPConnection getConnection()
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
		
		mainManager.fireOtherChanged(this, userResource);
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
