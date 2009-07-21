/**
 * 
 */
package net.sf.mxlosgi.core.listener;

import net.sf.mxlosgi.core.UserResource;
import net.sf.mxlosgi.core.XMPPOwner;



/**
 * @author noah
 *
 */
public interface XMPPOwnerListener
{
	/**
	 * 
	 * @param owner
	 */
	public void ownerStatusChanged(XMPPOwner owner);
	
	/**
	 * 
	 * @param owner
	 * @param userResource
	 */
	public void ownerOtherResourceStatusChanged(XMPPOwner owner, UserResource userResource);
}
