/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.mxlosgi.core.UserResource;
import net.sf.mxlosgi.core.XMPPContact;
import net.sf.mxlosgi.utils.AbstractPropertied;
import net.sf.mxlosgi.xmpp.IQRoster;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Presence;
import net.sf.mxlosgi.xmpp.IQRoster.Item;

/**
 * @author noah
 *
 */
public class XMPPContactImpl extends AbstractPropertied implements XMPPContact
{
	private JID bareJID;
	
	private String name;
	
	private IQRoster.Item item;
	
	private Set<String> groups = Collections.synchronizedSet(new HashSet<String>());
	
	private final Map<String, UserResource> userResources = Collections.synchronizedMap(new HashMap<String, UserResource>());
	
	/**
	 * @param bareJID
	 * @param item
	 */
	public XMPPContactImpl(JID bareJID,  Item item)
	{
		this.bareJID = bareJID;
		this.item = item;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.XMPPContact#getBareJID()
	 */
	@Override
	public JID getBareJID()
	{
		return bareJID;
	}

	void setName(String name)
	{
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.XMPPContact#getDisplayName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.XMPPContact#getGroups()
	 */
	@Override
	public Collection<String> getGroups()
	{
		return Collections.unmodifiableCollection(groups);
	}

	void addGroup(String group)
	{
		groups.add(group);
	}
	
	void addGroups(Collection<String> groups)
	{
		this.groups.addAll(groups);
	}
	
	void clearGroup()
	{
		groups.clear();
	}
	
	void addResource(UserResource userResource)
	{
		userResources.put(userResource.getResource(), userResource);
	}
	
	void removeResource(String resource)
	{
		userResources.remove(resource);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.XMPPContact#getResource(java.lang.String)
	 */
	@Override
	public UserResource getResource(String resource)
	{
		return userResources.get(resource);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.XMPPContact#getResources()
	 */
	@Override
	public Collection<UserResource> getResources()
	{
		return Collections.unmodifiableCollection(userResources.values());
	}


	@Override
	public boolean isResourceAvailable()
	{
		return !getResources().isEmpty();
	}
	
	public void setItem(IQRoster.Item item)
	{
		this.item = item;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.XMPPContact#getRosterItem()
	 */
	@Override
	public Item getRosterItem()
	{
		return item;
	}


	@Override
	public UserResource getMaxPriorityResource()
	{
		int currentPriority = Integer.MIN_VALUE;
		UserResource res = null;
		for (UserResource resource : getResources())
		{
			Presence presence = resource.getCurrentPresence();
			int resPriority = presence.getPriority();
			if (resPriority >= currentPriority)
			{
				res = resource;
				currentPriority = resPriority;
			}
		}
		return res;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("bareJID:" + getBareJID() + "\n")
			.append("displayName:" + getName() + "\n")
			.append("group:" + getGroups() + "\n")
			.append("resource:" + getResources() + "\n");
		return buf.toString();
	}



	
}
