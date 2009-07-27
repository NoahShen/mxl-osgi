/**
 * 
 */
package net.sf.mxlosgi.muc.impl;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.muc.listener.MucListener;
import net.sf.mxlosgi.xmpp.Message;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class MucListenerServiceTracker extends ServiceTracker
{

	public MucListenerServiceTracker(BundleContext context)
	{
		super(context, MucListener.class.getName(), null);
	}


	public void fireDeclineReceived(XmppConnection connection, Message message)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			MucListener listener = (MucListener) obj;
			listener.declineReceived(connection, message);
		}
	}

	public void fireInvitationReceived(XmppConnection connection, Message message)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			MucListener listener = (MucListener) obj;
			listener.invitationReceived(connection, message);
		}
		
	}

}
