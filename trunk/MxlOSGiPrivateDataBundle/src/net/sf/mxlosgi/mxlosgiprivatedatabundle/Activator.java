package net.sf.mxlosgi.mxlosgiprivatedatabundle;

import net.sf.mxlosgi.mxlosgiprivatedatabundle.impl.PrivateDataManagerImpl;
import net.sf.mxlosgi.mxlosgiprivatedatabundle.parser.PrivateDataExtensionParser;
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

	private ServiceRegistration privateDataManagerRegistration;

	private ServiceTracker parserServiceTracker;

	private XMPPParser parser;

	private PrivateDataExtensionParser privateDataParser;

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

		parserServiceTracker = new ServiceTracker(context, XMPPParser.class.getName(), null);
		parserServiceTracker.open();
		parser = (XMPPParser) parserServiceTracker.getService();

		privateDataParser = new PrivateDataExtensionParser();
		parser.addExtensionParser(privateDataParser);

		PrivateDataManagerImpl privateDataManager = new PrivateDataManagerImpl();
		privateDataManagerRegistration = context.registerService(PrivateDataManager.class.getName(), privateDataManager, null);
		
		String filter = "(objectclass=" + XMPPParser.class.getName() + ")";  
		context.addServiceListener(this, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		parser.removeExtensionParser(privateDataParser);

		if (parserServiceTracker != null)
		{
			parserServiceTracker.close();
			parserServiceTracker = null;
		}
		if (privateDataManagerRegistration != null)
		{
			privateDataManagerRegistration.unregister();
			privateDataManagerRegistration = null;
		}
		
		parser = null;
		privateDataParser = null;
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
			if (obj == parser)
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
