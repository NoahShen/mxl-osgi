package net.sf.mxlosgi.bookmarks;

import net.sf.mxlosgi.bookmarks.impl.BookmarkManagerImpl;
import net.sf.mxlosgi.bookmarks.impl.PrivateDataManagerServiceTracker;
import net.sf.mxlosgi.bookmarks.parser.BookmarksExtensionParser;
import net.sf.mxlosgi.privatedata.PrivateDataManager;
import net.sf.mxlosgi.xmppparser.ExtensionParser;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator, ServiceListener
{

	private ServiceRegistration bookManagerRegistration;

	private Bundle bundle;

	private ServiceRegistration bookmarksExtensionParserRegistration;

	private PrivateDataManagerServiceTracker privateDataManagerServiceTracker;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();

		BookmarksExtensionParser bookmarksExtensionParser = new BookmarksExtensionParser();
		bookmarksExtensionParserRegistration = 
				context.registerService(ExtensionParser.class.getName(), bookmarksExtensionParser, null);

		privateDataManagerServiceTracker = new PrivateDataManagerServiceTracker(context);
		privateDataManagerServiceTracker.open();
		
		BookmarkManagerImpl bookManager = new BookmarkManagerImpl(privateDataManagerServiceTracker);
		bookManagerRegistration = context.registerService(BookmarkManager.class.getName(), bookManager, null);
		
		String filter = "(objectclass=" + PrivateDataManager.class.getName() + ")";  
		context.addServiceListener(this, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		if (bookManagerRegistration != null)
		{
			bookManagerRegistration.unregister();
			bookManagerRegistration = null;
		}
		
		if (bookmarksExtensionParserRegistration != null)
		{
			bookmarksExtensionParserRegistration.unregister();
			bookmarksExtensionParserRegistration = null;
		}
		
		if (privateDataManagerServiceTracker != null)
		{
			privateDataManagerServiceTracker.close();
			privateDataManagerServiceTracker = null;
		}
		bundle = null;

	}
	

	@Override
	public void serviceChanged(ServiceEvent event)
	{
		int eventType = event.getType();
		if (eventType == ServiceEvent.UNREGISTERING)
		{
			try
			{
				bundle.uninstall();
			}
			catch (BundleException e)
			{
				//e.printStackTrace();
			}
		}
	}

}
