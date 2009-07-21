/**
 * 
 */
package net.sf.mxlosgi.core;

import net.sf.mxlosgi.utils.Propertied;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Presence;


/**
 * @author noah
 *
 */
public interface XMPPOwner extends Propertied
{
	/**
	 * 
	 * @return
	 */
	public Presence getCurrentPresence();
	
	/**
	 * 
	 * @param presence
	 */
	public void changePresence(Presence presence);
	
	/**
	 * 
	 * @return
	 */
	public XMPPConnection getConnection();
	
	/**
	 * Returns the jid.
	 * @return 
	 */
	public JID getJid();
	
	/**
	 * 
	 * @param presence
	 */
	public void setInitPresence(Presence presence);
	
	/**
	 * 
	 * @return
	 */
	public Presence getInitPresence();
	
	/**
	 * get all resources
	 * 
	 * @return collection
	 */
	public UserResource[] getOtherResources();

	/**
	 * 
	 * @param resource
	 * @return
	 */
	public UserResource getOtherResource(String resource);
}
