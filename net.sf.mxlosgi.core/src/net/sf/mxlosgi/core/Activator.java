package net.sf.mxlosgi.core;

import net.sf.mxlosgi.core.impl.SaslMechanismServiceTracker;
import net.sf.mxlosgi.core.impl.XMPPMainManagerImpl;
import net.sf.mxlosgi.xmppparser.XMPPParser;

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

	private ServiceRegistration sr;

	private ServiceTracker parserServiceTracker;

	private ServiceTracker saslManagerServiceTracker;

	private XMPPParser parser;

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
		
		context.addServiceListener(this);
		parserServiceTracker = new ServiceTracker(context, XMPPParser.class.getName(), null);
		parserServiceTracker.open();
		parser = (XMPPParser) parserServiceTracker.getService();

		SaslMechanismServiceTracker saslMechanismServiceTracker = new SaslMechanismServiceTracker(context);
		saslMechanismServiceTracker.open();

		XMPPMainManagerImpl mainManager = new XMPPMainManagerImpl(parser, saslMechanismServiceTracker);
		sr = context.registerService(XMPPMainManager.class.getName(), mainManager, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		if (sr != null)
		{
			sr.unregister();
			sr = null;
		}

		if (parserServiceTracker != null)
		{
			parserServiceTracker.close();
			parserServiceTracker = null;
		}

		if (saslManagerServiceTracker != null)
		{
			saslManagerServiceTracker.close();
			saslManagerServiceTracker = null;
		}
		
		parser = null;
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
