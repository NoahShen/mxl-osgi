package net.sf.mxlosgi.mxlosgiprivacybundle;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager;
import net.sf.mxlosgi.mxlosgiprivacybundle.impl.PrivacyManagerImpl;
import net.sf.mxlosgi.mxlosgiprivacybundle.impl.PrivacyPushFilter;

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

	private PrivacyManagerImpl privacyManager;

	private ServiceRegistration privacyManagerRegistration;

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

		privacyManager = new PrivacyManagerImpl();
		privacyManagerRegistration = context.registerService(PrivacyManager.class.getName(), privacyManager, null);

		PrivacyPushFilter privacyPushFilter = new PrivacyPushFilter();
		mainManager.addStanzaReceListener(privacyManager, privacyPushFilter);
		
		String filter = "(objectclass=" + XMPPMainManager.class.getName() + ")";  
		context.addServiceListener(this, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		mainManager.removeStanzaReceListener(privacyManager);
		
		if (privacyManagerRegistration != null)
		{
			privacyManagerRegistration.unregister();
			privacyManagerRegistration = null;
		}

		if (mainManagerServiceTracker != null)
		{
			mainManagerServiceTracker.close();
			mainManagerServiceTracker = null;
		}
		
		mainManager = null;
		mainManagerServiceTracker = null;
		privacyManager = null;
		this.context = null;
	}

	@Override
	public void serviceChanged(ServiceEvent event)
	{
		int eventType = event.getType();
		Object obj = context.getService(event.getServiceReference());
		if (eventType == ServiceEvent.UNREGISTERING)
		{
			if (obj == mainManager)
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
