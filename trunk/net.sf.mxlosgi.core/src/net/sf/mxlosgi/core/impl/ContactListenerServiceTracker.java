/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import net.sf.mxlosgi.core.SubscriptionRequest;
import net.sf.mxlosgi.core.UserResource;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppContact;
import net.sf.mxlosgi.core.listener.ContactListener;
import net.sf.mxlosgi.xmpp.JID;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class ContactListenerServiceTracker extends ServiceTracker
{

	public ContactListenerServiceTracker(BundleContext context)
	{
		super(context, ContactListener.class.getName(), null);
	}

	public void fireContactRemoved(XmppConnection connection, JID jid)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ContactListener listener  = (ContactListener) obj;
			listener.contactRemovedCompleted(connection, jid);
		}
	}
	
	public void fireContactUpdated(XmppConnection connection, XmppContact contact)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ContactListener listener  = (ContactListener) obj;
			listener.contactUpdated(connection, contact);
		}
	}
	
	public void fireContactUnsubscribed(XmppConnection connection, JID jid)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ContactListener listener  = (ContactListener) obj;
			listener.contactUnsubscribed(connection, jid);
		}
	}
	
	public void fireContactUnsubscribeMe(XmppConnection connection, JID jid)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ContactListener listener  = (ContactListener) obj;
			listener.contactUnsubscribeMe(connection, jid);
		}
	}
	
	public void fireContactSubscribed(XmppConnection connection, JID jid)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ContactListener listener  = (ContactListener) obj;
			listener.contactSubscribed(connection, jid);
		}
	}
	
	public void fireContactSubscribeMe(XmppConnection connection, SubscriptionRequest request)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ContactListener listener  = (ContactListener) obj;
			listener.contactSubscribeMe(connection, request);
		}
	}
	
	public void fireContactStatusChanged(XmppConnection connection, XmppContact contact, UserResource resource)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ContactListener listener  = (ContactListener) obj;
			listener.contactStatusChanged(connection, contact, resource);
		}
	}
}
