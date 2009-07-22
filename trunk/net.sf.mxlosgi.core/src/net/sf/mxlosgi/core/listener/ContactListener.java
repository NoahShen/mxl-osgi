package net.sf.mxlosgi.core.listener;

import net.sf.mxlosgi.core.SubscriptionRequest;
import net.sf.mxlosgi.core.UserResource;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppContact;
import net.sf.mxlosgi.xmpp.JID;


/**
 * @author noah
 * 
 */
public interface ContactListener
{

	/**
	 * 
	 * @param connection
	 * @param contact
	 * @param resource
	 */
	public void contactStatusChanged(XmppConnection connection, XmppContact contact, UserResource resource);

	/**
	 * Contact status changed such as friend's name changed or subscribe
	 * successfully
	 * 
	 * @param connection
	 * @param contact
	 */
	public void contactUpdated(XmppConnection connection, XmppContact contact);

	/**
	 * 
	 * @param connection
	 * @param request
	 */
	public void contactSubscribeMe(XmppConnection connection, SubscriptionRequest request);

	/**
	 * 
	 * @param connection
	 * @param jid
	 */
	public void contactSubscribed(XmppConnection connection, JID jid);
	
	/**
	 * 
	 * @param connection
	 * @param jid
	 */
	public void contactUnsubscribeMe(XmppConnection connection, JID jid);
	
	/**
	 * 
	 * @param connection
	 * @param jid
	 */
	public void contactUnsubscribed(XmppConnection connection, JID jid);
	
	/**
	 * A contact you requested to be removed has been removed from the
	 * server.
	 * 
	 * @param connection
	 *                  MyXMPPConnection
	 * @param jid
	 * 
	 */
	public void contactRemovedCompleted(XmppConnection connection, JID jid);
}