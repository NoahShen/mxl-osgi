package net.sf.mxlosgi.mxlosgilastactivitybundle;

import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoFeature;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoManager;
import net.sf.mxlosgi.mxlosgilastactivitybundle.impl.LastActivityFilter;
import net.sf.mxlosgi.mxlosgilastactivitybundle.impl.LastActivityManagerImpl;
import net.sf.mxlosgi.mxlosgilastactivitybundle.parser.LastActivityExtensionParser;
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

	private ServiceRegistration lastActivityManagerRegistration;

	private LastActivityManagerImpl lastActivityManager;

	private LastActivityExtensionParser activityParser;

	private DiscoInfoFeature lastActivityFeature;

	private ServiceTracker parserServiceTracker;

	private XMPPParser parser;

	private ServiceTracker discoInfoManagerServiceTracker;

	private DiscoInfoManager discoInfoManager;

	private Bundle bundle;

	private BundleContext context;

	private ServiceTracker mainManagerServiceTracker;

	private XMPPMainManager mainManager;

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
		
		discoInfoManagerServiceTracker = new ServiceTracker(context, DiscoInfoManager.class.getName(), null);
		discoInfoManagerServiceTracker.open();
		discoInfoManager = (DiscoInfoManager) discoInfoManagerServiceTracker.getService();
		
		lastActivityFeature = new DiscoInfoFeature(null, "jabber:iq:last");
		discoInfoManager.addFeature(lastActivityFeature);
		
		lastActivityManager = new LastActivityManagerImpl();
		lastActivityManagerRegistration = context.registerService(LastActivityManager.class.getName(), lastActivityManager, null);

		LastActivityFilter lastActivityFilter = new LastActivityFilter();
		mainManager.addStanzaReceListener(lastActivityManager, lastActivityFilter);
		
		activityParser = new LastActivityExtensionParser();
		parser.addExtensionParser(activityParser);

		context.addServiceListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		mainManager.removeStanzaReceListener(lastActivityManager);
		parser.removeExtensionParser(activityParser);
		discoInfoManager.removeFeature(lastActivityFeature);
		
		if (lastActivityManagerRegistration != null)
		{
			lastActivityManagerRegistration.unregister();
			lastActivityManagerRegistration = null;
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
		
		if (discoInfoManagerServiceTracker != null)
		{
			discoInfoManagerServiceTracker.close();
			discoInfoManagerServiceTracker = null;
		}
		
		mainManager = null;
		discoInfoManager = null;
		parser = null;
		
		lastActivityFeature = null;
		activityParser = null;
		lastActivityManager = null;
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
			if (obj == parser || obj == mainManager)
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
		else if (eventType == ServiceEvent.REGISTERED)
		{
			if (obj == discoInfoManager)
			{
				discoInfoManager.addFeature(lastActivityFeature);
			}
		}
	}

}
