package net.sf.mxlosgi.privacy;


import java.util.Hashtable;

import net.sf.mxlosgi.core.XmppMainManager;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.privacy.impl.PrivacyListenerServiceTracker;
import net.sf.mxlosgi.privacy.impl.PrivacyManagerImpl;
import net.sf.mxlosgi.privacy.impl.PrivacyPushFilter;

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
	private PrivacyListenerServiceTracker privacyListenerServiceTracker;
	private ServiceRegistration privacyManagerRegistration;
	private ServiceRegistration privacyListenerRegistration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();

		privacyListenerServiceTracker = new PrivacyListenerServiceTracker(context);
		privacyListenerServiceTracker.open();
		
		PrivacyManagerImpl privacyManager = new PrivacyManagerImpl(privacyListenerServiceTracker);
		privacyManagerRegistration = context.registerService(PrivacyManager.class.getName(), privacyManager, null);
		
		
		PrivacyPushFilter privacyPushFilter = new PrivacyPushFilter();
		Hashtable<String, StanzaFilter> properties = new Hashtable<String, StanzaFilter>();
		properties.put("filter", privacyPushFilter);
		privacyListenerRegistration = context.registerService(StanzaReceListener.class.getName(), privacyManager, properties);
		
		
		String filter = "(objectclass=" + XmppMainManager.class.getName() + ")";  
		context.addServiceListener(this, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		if (privacyListenerServiceTracker != null)
		{
			privacyListenerServiceTracker.close();
			privacyListenerServiceTracker = null;
		}
		
		if (privacyManagerRegistration != null)
		{
			privacyManagerRegistration.unregister();
			privacyManagerRegistration = null;
		}
		
		if (privacyListenerRegistration != null)
		{
			privacyListenerRegistration.unregister();
			privacyListenerRegistration = null;
		}
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
