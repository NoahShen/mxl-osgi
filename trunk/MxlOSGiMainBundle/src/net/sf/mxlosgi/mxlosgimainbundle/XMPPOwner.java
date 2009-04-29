/**
 * 
 */
package net.sf.mxlosgi.mxlosgimainbundle;

import net.sf.mxlosgi.mxlosgiutilsbundle.IHasAttribute;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.Presence;

/**
 * @author noah
 *
 */
public interface XMPPOwner extends IHasAttribute
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
