package net.sf.mxlosgi.mxlosgimainbundle.listener;

import net.sf.mxlosgi.mxlosgimainbundle.SubscriptionRequest;
import net.sf.mxlosgi.mxlosgimainbundle.UserResource;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPContact;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

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
	public void contactStatusChanged(XMPPConnection connection, XMPPContact contact, UserResource resource);

	/**
	 * Contact status changed such as friend's name changed or subscribe
	 * successfully
	 * 
	 * @param connection
	 * @param contact
	 */
	public void contactUpdated(XMPPConnection connection, XMPPContact contact);

	/**
	 * 
	 * @param connection
	 * @param request
	 */
	public void contactSubscribeMe(XMPPConnection connection, SubscriptionRequest request);

	/**
	 * 
	 * @param connection
	 * @param jid
	 */
	public void contactSubscribed(XMPPConnection connection, JID jid);
	
	/**
	 * 
	 * @param connection
	 * @param jid
	 */
	public void contactUnsubscribeMe(XMPPConnection connection, JID jid);
	
	/**
	 * 
	 * @param connection
	 * @param jid
	 */
	public void contactUnsubscribed(XMPPConnection connection, JID jid);
	
	/**
	 * A contact you requested to be removed has been removed from the
	 * server.
	 * 
	 * @param connection
	 *                  MyXMPPConnection
	 * @param jid
	 * 
	 */
	public void contactRemovedCompleted(XMPPConnection connection, JID jid);
}