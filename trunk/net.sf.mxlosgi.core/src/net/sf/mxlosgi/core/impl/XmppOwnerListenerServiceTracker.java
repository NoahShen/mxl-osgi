/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import net.sf.mxlosgi.core.UserResource;
import net.sf.mxlosgi.core.XmppOwner;
import net.sf.mxlosgi.core.listener.XmppOwnerListener;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class XmppOwnerListenerServiceTracker extends ServiceTracker
{

	public XmppOwnerListenerServiceTracker(BundleContext context)
	{
		super(context, XmppOwnerListener.class.getName(), null);
	}

	public void fireOwenrStatusChanged(XmppOwner owner)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			XmppOwnerListener listener  = (XmppOwnerListener) obj;
			listener.ownerStatusChanged(owner);
		}
	}
	
	public void fireOtherChanged(XmppOwner owner,  UserResource userResource)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			XmppOwnerListener listener  = (XmppOwnerListener) obj;
			listener.ownerOtherResourceStatusChanged(owner, userResource);
		}
	}
}
