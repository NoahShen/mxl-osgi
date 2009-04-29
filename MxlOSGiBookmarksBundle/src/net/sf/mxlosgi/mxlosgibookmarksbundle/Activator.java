package net.sf.mxlosgi.mxlosgibookmarksbundle;

import net.sf.mxlosgi.mxlosgibookmarksbundle.impl.BookmarkManagerImpl;
import net.sf.mxlosgi.mxlosgibookmarksbundle.parser.BookmarksExtensionParser;
import net.sf.mxlosgi.mxlosgiprivatedatabundle.PrivateDataManager;
import net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator, ServiceListener
{

	private ServiceTracker privateDataManagerServiceTracker;

	private ServiceTracker parserServiceTracker;

	private XMPPParser parser;

	private BookmarksExtensionParser bookmarksExtensionParser;

	private ServiceRegistration bookManagerRegistration;

	private Bundle bundle;

	private BundleContext context;

	private PrivateDataManager privateDataManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();
		this.context = context;
		
		privateDataManagerServiceTracker = new ServiceTracker(context, PrivateDataManager.class.getName(), null);
		privateDataManagerServiceTracker.open();
		privateDataManager = (PrivateDataManager) privateDataManagerServiceTracker.getService();

		parserServiceTracker = new ServiceTracker(context, XMPPParser.class.getName(), null);
		parserServiceTracker.open();
		parser = (XMPPParser) parserServiceTracker.getService();

		bookmarksExtensionParser = new BookmarksExtensionParser();
		parser.addExtensionParser(bookmarksExtensionParser);

		BookmarkManagerImpl bookManager = new BookmarkManagerImpl(privateDataManager);
		bookManagerRegistration = context.registerService(BookmarkManager.class.getName(), bookManager, null);
		
		context.addServiceListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		parser.removeExtensionParser(bookmarksExtensionParser);

		if (bookManagerRegistration != null)
		{
			bookManagerRegistration.unregister();
			bookManagerRegistration = null;
		}
		if (privateDataManagerServiceTracker != null)
		{
			privateDataManagerServiceTracker.close();
			privateDataManagerServiceTracker = null;
		}

		if (parserServiceTracker != null)
		{
			parserServiceTracker.close();
			parserServiceTracker = null;
		}

		privateDataManager = null;
		parser = null;
		bookmarksExtensionParser = null;
		bundle = null;
		this.context = context;
	}
	

	@Override
	public void serviceChanged(ServiceEvent event)
	{
		int eventType = event.getType();
		Object obj = context.getService(event.getServiceReference());
		if (eventType == ServiceEvent.UNREGISTERING)
		{
			if (obj == parser || obj == privateDataManager)
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

}
