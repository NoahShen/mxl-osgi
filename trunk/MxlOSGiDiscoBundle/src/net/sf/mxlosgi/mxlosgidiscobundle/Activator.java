package net.sf.mxlosgi.mxlosgidiscobundle;

import net.sf.mxlosgi.mxlosgidiscobundle.impl.DiscoInfoFilter;
import net.sf.mxlosgi.mxlosgidiscobundle.impl.DiscoInfoManagerImpl;
import net.sf.mxlosgi.mxlosgidiscobundle.impl.DiscoItemsManagerImpl;
import net.sf.mxlosgi.mxlosgidiscobundle.parser.DiscoInfoExtensionParser;
import net.sf.mxlosgi.mxlosgidiscobundle.parser.DiscoItemsExtensionParser;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager;
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
	private DiscoInfoManagerImpl discoInfoManager;

	private ServiceRegistration discoInfoRegistration;

	private DiscoItemsManagerImpl discoItemsManager;

	private ServiceRegistration discoItemsRegistration;

	private DiscoInfoExtensionParser discoInfoparser;

	private DiscoItemsExtensionParser discoItemsParser;

	private ServiceTracker parserServiceTracker;

	private XMPPParser parser;

	private Bundle bundle;

	private ServiceTracker mainManagerServiceTracker;

	private XMPPMainManager mainManager;

	private BundleContext context;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();
		this.context = context;
		
		mainManagerServiceTracker = new ServiceTracker(context, XMPPMainManager.class.getName(), null);
		mainManagerServiceTracker.open();
		mainManager = (XMPPMainManager) mainManagerServiceTracker.getService();

		parserServiceTracker = new ServiceTracker(context, XMPPParser.class.getName(), null);
		parserServiceTracker.open();
		parser = (XMPPParser) parserServiceTracker.getService();
		
		discoInfoManager = new DiscoInfoManagerImpl();
		discoInfoManager.addFeature(new DiscoInfoFeature(null, "http://jabber.org/protocol/disco#info"));	
		
		discoInfoRegistration = context.registerService(DiscoInfoManager.class.getName(), discoInfoManager, null);
		
		DiscoInfoFilter discoInfoFilter = new DiscoInfoFilter();
		mainManager.addStanzaReceListener(discoInfoManager, discoInfoFilter);
		
		discoInfoparser = new DiscoInfoExtensionParser();
		parser.addExtensionParser(discoInfoparser);

		discoItemsManager = new DiscoItemsManagerImpl();
		discoItemsRegistration = context.registerService(DiscoItemsManager.class.getName(), discoItemsManager, null);

		discoItemsParser = new DiscoItemsExtensionParser();
		parser.addExtensionParser(discoItemsParser);
		
		context.addServiceListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		mainManager.removeStanzaReceListener(discoInfoManager);

		parser.removeExtensionParser(discoInfoparser);
		parser.removeExtensionParser(discoItemsParser);
		
		if (discoInfoRegistration != null)
		{
			discoInfoRegistration.unregister();
			discoInfoRegistration = null;
		}
		
		if (discoItemsRegistration != null)
		{
			discoItemsRegistration.unregister();
			discoItemsRegistration = null;
		}
		
		if (mainManagerServiceTracker != null)
		{
			mainManagerServiceTracker.close();
			mainManagerServiceTracker = null;
		}
		
		if (parserServiceTracker != null)
		{
			parserServiceTracker.close();
			parserServiceTracker = null;
		}
		
		parser = null;
		mainManager = null;
		
		discoItemsParser = null;
		discoInfoparser = null;
		discoInfoManager = null;
		discoItemsManager = null;
		bundle = null;
		this.context = null;
	}

	@Override
	public void serviceChanged(ServiceEvent event)
	{
		int eventType = event.getType();
		Object obj = context.getService(event.getServiceReference());
		if (eventType == ServiceEvent.UNREGISTERING)
		{
			if (obj == mainManager || obj == parser)
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
