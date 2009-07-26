/**
 * 
 */
package net.sf.mxlosgi.lastactivity.impl;

import net.sf.mxlosgi.lastactivity.listener.LastActivityListener;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class LastActivityListenerServiceTracker extends ServiceTracker
{

	public LastActivityListenerServiceTracker(BundleContext context)
	{
		super(context, LastActivityListener.class.getName(), null);
	}
	
	public void fireLastActivity(long idleSecond)
	{
		ServiceReference[] references = getServiceReferences();
		if (references == null)
		{
			return;
		}
		
		for (ServiceReference reference : references)
		{
			Object obj = reference.getProperty("idleSecond");
			if (obj == null)
			{
				continue;
			}
			
			try
			{
				long time = Long.parseLong(obj.toString());
				if (time == idleSecond)
				{
					((LastActivityListener) getService(reference)).idle();
				}
			}
			catch (NumberFormatException e)
			{
				//e.printStackTrace();
			}
			
		}
	}
}
