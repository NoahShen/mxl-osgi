package net.sf.mxlosgi.disco;


import java.util.Hashtable;

import net.sf.mxlosgi.core.XmppMainManager;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.disco.impl.DiscoInfoFeatureServiceTracker;
import net.sf.mxlosgi.disco.impl.DiscoInfoFilter;
import net.sf.mxlosgi.disco.impl.DiscoInfoIdentityServiceTracker;
import net.sf.mxlosgi.disco.impl.DiscoInfoManagerImpl;
import net.sf.mxlosgi.disco.impl.DiscoItemsManagerImpl;
import net.sf.mxlosgi.disco.parser.DiscoInfoExtensionParser;
import net.sf.mxlosgi.disco.parser.DiscoItemsExtensionParser;
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
	
	private Bundle bundle;
	private ServiceRegistration discoInfoFeatureRegistration;
	private ServiceRegistration discoInfoRegistration;
	private ServiceRegistration discoListenerRegistration;
	private ServiceRegistration discoInfoParserRegistration;
	private ServiceRegistration discoItemsRegistration;
	private ServiceRegistration discoItemsParserRegistration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();
		
		DiscoInfoFeatureServiceTracker discoInfoFeatureServiceTracker = new DiscoInfoFeatureServiceTracker(context);
		discoInfoFeatureServiceTracker.open();
		
		DiscoInfoIdentityServiceTracker discoInfoIdentityServiceTracker = new DiscoInfoIdentityServiceTracker(context);
		discoInfoIdentityServiceTracker.open();
		
		DiscoInfoManagerImpl discoInfoManager = new DiscoInfoManagerImpl(discoInfoFeatureServiceTracker,
															discoInfoIdentityServiceTracker);
		discoInfoRegistration = context.registerService(DiscoInfoManager.class.getName(), discoInfoManager, null);
		
		
		DiscoInfoFilter discoInfoFilter = new DiscoInfoFilter();
		Hashtable<String, StanzaFilter> properties = new Hashtable<String, StanzaFilter>();
		properties.put("filter", discoInfoFilter);
		discoListenerRegistration = context.registerService(StanzaReceListener.class.getName(), discoInfoManager, properties);
		
		
		DiscoInfoFeature discoInfoFeature = new DiscoInfoFeature(null, "http://jabber.org/protocol/disco#info");
		discoInfoFeatureRegistration = context.registerService(DiscoInfoFeature.class.getName(), discoInfoFeature, null);
		
		
		DiscoInfoExtensionParser discoInfoParser = new DiscoInfoExtensionParser();
		discoInfoParserRegistration = context.registerService(ExtensionParser.class.getName(), discoInfoParser, null);

		DiscoItemsManagerImpl discoItemsManager = new DiscoItemsManagerImpl();
		discoItemsRegistration = context.registerService(DiscoItemsManager.class.getName(), discoItemsManager, null);

		DiscoItemsExtensionParser discoItemsParser = new DiscoItemsExtensionParser();
		discoItemsParserRegistration = context.registerService(ExtensionParser.class.getName(), discoItemsParser, null);
		
		String filter = "(objectClass=" + XmppMainManager.class.getName() + " )";
		context.addServiceListener(this, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		if (discoInfoFeatureRegistration != null)
		{
			discoInfoFeatureRegistration.unregister();
			discoInfoFeatureRegistration = null;
		}
		
		if (discoListenerRegistration != null)
		{
			discoListenerRegistration.unregister();
			discoListenerRegistration = null;
		}
		
		if (discoInfoRegistration != null)
		{
			discoInfoRegistration.unregister();
			discoInfoRegistration = null;
		}
		
		if (discoInfoParserRegistration != null)
		{
			discoInfoParserRegistration.unregister();
			discoInfoParserRegistration = null;
		}
		
		if (discoItemsRegistration != null)
		{
			discoItemsRegistration.unregister();
			discoItemsRegistration = null;
		}
		
		if (discoItemsParserRegistration != null)
		{
			discoItemsParserRegistration.unregister();
			discoItemsParserRegistration = null;
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
