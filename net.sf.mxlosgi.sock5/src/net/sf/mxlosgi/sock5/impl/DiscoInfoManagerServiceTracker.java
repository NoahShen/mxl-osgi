/**
 * 
 */
package net.sf.mxlosgi.sock5.impl;

import net.sf.mxlosgi.disco.DiscoInfoManager;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class DiscoInfoManagerServiceTracker extends ServiceTracker
{

	public DiscoInfoManagerServiceTracker(BundleContext context)
	{
		super(context, DiscoInfoManager.class.getName(), null);
	}

	public DiscoInfoManager getDiscoInfoManager()
	{
		return (DiscoInfoManager) getService();
	}
}
