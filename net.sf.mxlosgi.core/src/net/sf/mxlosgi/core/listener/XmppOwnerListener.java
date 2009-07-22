/**
 * 
 */
package net.sf.mxlosgi.core.listener;

import net.sf.mxlosgi.core.UserResource;
import net.sf.mxlosgi.core.XmppOwner;



/**
 * @author noah
 *
 */
public interface XmppOwnerListener
{
	/**
	 * 
	 * @param owner
	 */
	public void ownerStatusChanged(XmppOwner owner);
	
	/**
	 * 
	 * @param owner
	 * @param userResource
	 */
	public void ownerOtherResourceStatusChanged(XmppOwner owner, UserResource userResource);
}
