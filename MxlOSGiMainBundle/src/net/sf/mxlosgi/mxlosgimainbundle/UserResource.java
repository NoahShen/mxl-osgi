package net.sf.mxlosgi.mxlosgimainbundle;

import net.sf.mxlosgi.mxlosgiutilsbundle.IHasAttribute;
import net.sf.mxlosgi.mxlosgixmppbundle.Presence;

/**
 * @author noah
 *
 */
public interface UserResource extends IHasAttribute
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
