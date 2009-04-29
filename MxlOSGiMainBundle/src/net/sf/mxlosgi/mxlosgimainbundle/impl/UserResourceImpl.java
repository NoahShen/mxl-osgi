/**
 * 
 */
package net.sf.mxlosgi.mxlosgimainbundle.impl;

import net.sf.mxlosgi.mxlosgimainbundle.UserResource;
import net.sf.mxlosgi.mxlosgiutilsbundle.AbstractHasAttribute;
import net.sf.mxlosgi.mxlosgixmppbundle.Presence;

/**
 * @author noah
 *
 */
public class UserResourceImpl extends AbstractHasAttribute implements UserResource
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
