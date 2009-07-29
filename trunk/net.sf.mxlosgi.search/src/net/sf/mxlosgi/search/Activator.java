package net.sf.mxlosgi.search;

import net.sf.mxlosgi.core.XmppMainManager;
import net.sf.mxlosgi.search.impl.DiscoInfoManagerServiceTracker;
import net.sf.mxlosgi.search.impl.SearchManagerImpl;
import net.sf.mxlosgi.search.parser.SearchExtensionParser;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator, ServiceListener
{

	private ServiceRegistration searchManagerRegistration;

	private Bundle bundle;

	private ServiceRegistration searchExtensionParserRegistration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();

		SearchExtensionParser searchExtensionParser = new SearchExtensionParser();
		searchExtensionParserRegistration = 
				context.registerService(ExtensionParser.class.getName(), searchExtensionParser, null);

		DiscoInfoManagerServiceTracker discoInfoManagerServiceTracker = new DiscoInfoManagerServiceTracker(context);
		discoInfoManagerServiceTracker.open();
		
		SearchManagerImpl searchManager = new SearchManagerImpl(discoInfoManagerServiceTracker);
		searchManagerRegistration = context.registerService(SearchManager.class.getName(), searchManager, null);
		
		String filter = "(|(objectclass=" + XmppMainManager.class.getName() + ")" +
					"(objectclass=" + XmppParser.class.getName() + "))";  
		context.addServiceListener(this, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{

		if (searchExtensionParserRegistration != null)
		{
			searchExtensionParserRegistration.unregister();
			searchExtensionParserRegistration = null;
		}
		
		if (searchManagerRegistration != null)
		{
			searchManagerRegistration.unregister();
			searchManagerRegistration = null;
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
