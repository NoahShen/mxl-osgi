/**
 * 
 */
package net.sf.mxlosgi.sock5.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.mxlosgi.xmpp.JID;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class ProxyServiceTracker extends ServiceTracker
{

	public ProxyServiceTracker(BundleContext context)
	{
		super(context, JID.class.getName(), null);
	}

	public JID[] getProxies()
	{
		List<JID> proxis = new ArrayList<JID>();
		ServiceReference[] refernces = getServiceReferences();
		if (refernces == null)
		{
			return proxis.toArray(new JID[]{});
		}
		
		for (ServiceReference reference : refernces)
		{
			Object obj = reference.getProperty("isProxy");
			if ("true".equalsIgnoreCase(obj.toString()))
			{
				proxis.add((JID) getService(reference));
			}
		}
		
		return proxis.toArray(new JID[]{});
	}
}
