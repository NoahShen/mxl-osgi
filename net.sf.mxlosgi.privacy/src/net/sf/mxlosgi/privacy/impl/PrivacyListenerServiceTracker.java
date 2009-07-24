/**
 * 
 */
package net.sf.mxlosgi.privacy.impl;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.privacy.listener.PrivacyListener;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class PrivacyListenerServiceTracker extends ServiceTracker
{

	public PrivacyListenerServiceTracker(BundleContext context)
	{
		super(context, PrivacyListener.class.getName(), null);
	}


	public void firePrivacyListCreatedOrUpdated(XmppConnection connection, String listName)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		for (Object obj : services)
		{
			PrivacyListener listener = (PrivacyListener) obj;
			listener.privacyListUpdated(connection, listName);
		}
	}
}
