package net.sf.mxlosgi.lastactivity;


import java.util.Hashtable;

import net.sf.mxlosgi.core.XmppMainManager;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.disco.DiscoInfoFeature;
import net.sf.mxlosgi.lastactivity.impl.LastActivityFilter;
import net.sf.mxlosgi.lastactivity.impl.LastActivityListenerServiceTracker;
import net.sf.mxlosgi.lastactivity.impl.LastActivityManagerImpl;
import net.sf.mxlosgi.lastactivity.listener.LastActivityListener;
import net.sf.mxlosgi.lastactivity.parser.LastActivityExtensionParser;
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

	private Bundle bundle;
	private ServiceRegistration lastActivityFeatureRegistration;
	private LastActivityListenerServiceTracker lastActivityListenerServiceTracker;
	private ServiceRegistration lastActivityManagerRegistration;
	private ServiceRegistration lastActivityListenerRegistration;
	private ServiceRegistration activityParserRegistration;
	private LastActivityManagerImpl lastActivityManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();
		
		DiscoInfoFeature lastActivityFeature = new DiscoInfoFeature(null, "jabber:iq:last");
		lastActivityFeatureRegistration = context.registerService(DiscoInfoFeature.class.getName(), lastActivityFeature, null);
		
		lastActivityListenerServiceTracker = new LastActivityListenerServiceTracker(context);
		lastActivityListenerServiceTracker.open();
		
		lastActivityManager = new LastActivityManagerImpl(lastActivityListenerServiceTracker);
		lastActivityManagerRegistration = context.registerService(LastActivityManager.class.getName(), lastActivityManager, null);

		LastActivityFilter lastActivityFilter = new LastActivityFilter();
		Hashtable<String, StanzaFilter> properties = new Hashtable<String, StanzaFilter>();
		properties.put("filter", lastActivityFilter);
		lastActivityListenerRegistration = context.registerService(StanzaReceListener.class.getName(), lastActivityManager, properties);
		
		
		LastActivityExtensionParser activityParser = new LastActivityExtensionParser();
		activityParserRegistration = context.registerService(ExtensionParser.class.getName(), activityParser, null);

		String filter = "(|(objectclass=" + XmppMainManager.class.getName() + ")" +
					"(objectclass=" + XmppParser.class.getName() + ")" +
					"(objectclass=" + LastActivityListener.class.getName() + "))";  
		context.addServiceListener(this, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		if (activityParserRegistration != null)
		{
			activityParserRegistration.unregister();
			activityParserRegistration = null;
		}
		
		if (lastActivityListenerRegistration != null)
		{
			lastActivityListenerRegistration.unregister();
			lastActivityListenerRegistration = null;
		}
		
		if (lastActivityManagerRegistration != null)
		{
			lastActivityManagerRegistration.unregister();
			lastActivityManagerRegistration = null;
		}
		
		if (lastActivityFeatureRegistration != null)
		{
			lastActivityFeatureRegistration.unregister();
			lastActivityFeatureRegistration = null;
		}
		
		if (lastActivityManagerRegistration != null)
		{
			lastActivityManagerRegistration.unregister();
			lastActivityManagerRegistration = null;
		}

		bundle = null;
	}
	

	@Override
	public void serviceChanged(ServiceEvent event)
	{
		Object service = bundle.getBundleContext().getService(event.getServiceReference());
		int eventType = event.getType();
		
		if (service instanceof LastActivityListener)
		{
			if (lastActivityManager != null)
			{
				lastActivityManager.updateIdleThread();
			}
			return;
		}
		
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
