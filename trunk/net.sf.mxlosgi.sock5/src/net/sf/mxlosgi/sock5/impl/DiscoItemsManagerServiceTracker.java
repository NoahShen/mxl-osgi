/**
 * 
 */
package net.sf.mxlosgi.sock5.impl;

import net.sf.mxlosgi.disco.DiscoItemsManager;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class DiscoItemsManagerServiceTracker extends ServiceTracker
{

	public DiscoItemsManagerServiceTracker(BundleContext context)
	{
		super(context, DiscoItemsManager.class.getName(), null);
	}

	public DiscoItemsManager getDiscoItemsManager()
	{
		return (DiscoItemsManager) getService();
	}
}
