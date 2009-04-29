/**
 * 
 */
package net.sf.mxlosgi.mxlosgimainbundle.listener;

import net.sf.mxlosgi.mxlosgimainbundle.UserResource;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPOwner;


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
