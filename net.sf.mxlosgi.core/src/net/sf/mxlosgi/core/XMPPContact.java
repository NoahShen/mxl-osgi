/**
 * 
 */
package net.sf.mxlosgi.core;

import java.util.Collection;

import net.sf.mxlosgi.utils.Propertied;
import net.sf.mxlosgi.xmpp.IQRoster;
import net.sf.mxlosgi.xmpp.JID;

/**
 * @author noah
 *
 */
public interface XMPPContact extends Propertied
{
	/**
	 * 
	 * @return bareJID
	 */
	public JID getBareJID();

	/**
	 * Display name ,can be changed by owner
	 * 
	 * @return display name
	 */
	public String getName();
	
	/**
	 * Get the groups which the user belongs.
	 * 
	 * @return the groups which the user belongs to
	 */
	public Collection<String> getGroups();
	
	/**
	 * get all resources
	 * 
	 * @return collection
	 */
	public Collection<UserResource> getResources();

	/**
	 * 
	 * @return
	 */
	public boolean isResourceAvailable();
	
	/**
	 * 
	 * @param resource
	 * @return
	 */
	public UserResource getResource(String resource);

	/**
	 * 
	 * @return
	 */
	public IQRoster.Item getRosterItem();
	
	/**
	 * 
	 * @return
	 */
	public UserResource getMaxPriorityResource();

}
