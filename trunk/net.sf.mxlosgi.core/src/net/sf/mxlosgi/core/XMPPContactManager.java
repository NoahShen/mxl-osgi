/**
 * 
 */
package net.sf.mxlosgi.core;

import net.sf.mxlosgi.xmpp.JID;


/**
 * @author noah
 *
 */
public interface XMPPContactManager
{
	/**
	 * 
	 * @return
	 */
	public XMPPConnection getConnection();
	
	/**
	 * Get all contacts.
	 * 
	 * @return all contacts
	 */
	public XMPPContact[] getContacts();

	/**
	 * Get the contact by bareJID.
	 * 
	 * @param bareJID
	 *                  contact's bareJID
	 * @return contact XMPPContact
	 */
	public XMPPContact getContact(JID bareJID);
	
	/**
	 * 
	 * @param jid
	 * @param name
	 */
	public void updateContactName(JID jid, String name) throws XMPPException;
	
	/**
	 * 
	 * @param jid
	 * @param groups
	 * @throws Exception
	 */
	public void updateContactGroup(JID jid, String ... groups) throws XMPPException;
	
	/**
	 * 
	 * @param jid
	 */
	public void addContact(JID jid, String name, String... groups) throws Exception;
	
	/**
	 * 
	 * @param jid
	 */
	public void subscribeContact(JID jid);
	
	/**
	 * 
	 * @param jid
	 */
	public void subscribedContact(JID jid);
	
	/**
	 * 
	 * @param jid
	 */
	public void unsubscribeContact(JID jid);
	
	/**
	 * 
	 * @param jid
	 */
	public void unsubscribedContact(JID jid);
	
	/**
	 * 
	 * @param jid
	 */
	public void removeContactFromRoster(JID jid) throws XMPPException;


}
