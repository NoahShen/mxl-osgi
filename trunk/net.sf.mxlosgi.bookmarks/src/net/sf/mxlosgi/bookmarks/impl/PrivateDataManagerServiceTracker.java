package net.sf.mxlosgi.bookmarks.impl;

import net.sf.mxlosgi.privatedata.PrivateDataManager;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class PrivateDataManagerServiceTracker extends ServiceTracker
{

	public PrivateDataManagerServiceTracker(BundleContext context)
	{
		super(context, PrivateDataManager.class.getName(), null);
	}
	
	public PrivateDataManager getPrivateDataManager()
	{
		return (PrivateDataManager) getService();
	}

}
