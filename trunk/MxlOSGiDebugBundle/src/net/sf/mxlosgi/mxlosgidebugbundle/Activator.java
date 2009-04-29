package net.sf.mxlosgi.mxlosgidebugbundle;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator, ServiceListener
{

	private ConsoleStanzaDebug debug;

	private ConnectionDebug connDebug;

	private ServiceTracker mainManagerServiceTracker;

	private XMPPMainManager mainManager;

	private Bundle bundle;

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
		
		debug = new ConsoleStanzaDebug();
		connDebug = new ConnectionDebug();
		
		String filter = "(objectclass=" + XMPPMainManager.class.getName() + ")";  
		context.addServiceListener(this, filter);
		
		mainManager.addConnectionListener(connDebug);
		mainManager.addStanzaReceListener(debug, null);
		mainManager.addStanzaSendListener(debug, null);
		mainManager.addXMLStringListener(debug);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		mainManager.removeConnectionListener(connDebug);
		mainManager.removeStanzaReceListener(debug);
		mainManager.removeStanzaSendListener(debug);
		mainManager.removeXMLStringListener(debug);
		
		if (mainManagerServiceTracker != null)
		{
			mainManagerServiceTracker.close();
			mainManagerServiceTracker = null;
		}
		
		connDebug = null;
		debug = null;
		mainManager = null;
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
