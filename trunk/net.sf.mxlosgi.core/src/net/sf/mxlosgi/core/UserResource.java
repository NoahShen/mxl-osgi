package net.sf.mxlosgi.core;

import net.sf.mxlosgi.utils.Propertied;
import net.sf.mxlosgi.xmpp.Presence;


/**
 * @author noah
 *
 */
public interface UserResource extends Propertied
{
	/**
	 * 
	 * @return
	 */

	public String getResource();

	/**
	 * 
	 * @return
	 */
	public Presence getCurrentPresence();

	/**
	 * 
	 * @return
	 */
	public Presence getOldPresence();
}
