/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import net.sf.mxlosgi.core.UserResource;
import net.sf.mxlosgi.utils.AbstractPropertied;
import net.sf.mxlosgi.xmpp.Presence;

/**
 * @author noah
 *
 */
public class UserResourceImpl extends AbstractPropertied implements UserResource
{
	private String resource;
	
	private Presence currentPresence;
	
	private Presence oldPresence;
	
	/**
	 * @param resource
	 */
	public UserResourceImpl(String resource)
	{
		this.resource = resource;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.UserResource#getOldPresence()
	 */
	@Override
	public Presence getOldPresence()
	{
		return oldPresence;
	}

	void setCurrentPresence(Presence currentPresence)
	{
		this.oldPresence = this.currentPresence;
		this.currentPresence = currentPresence;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.UserResource#getPresence()
	 */
	@Override
	public Presence getCurrentPresence()
	{
		return currentPresence;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.UserResource#getResource()
	 */
	@Override
	public String getResource()
	{
		return resource;
	}

}
