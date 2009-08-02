package net.sf.mxlosgi.streaminitiation;


import net.sf.mxlosgi.streaminitiation.parser.StreamInitiationExtensionParser;
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
	private ServiceRegistration streamInitiationExtensionParserRegistration;


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();

		StreamInitiationExtensionParser streamInitiationExtensionParser = new StreamInitiationExtensionParser();
		streamInitiationExtensionParserRegistration = context.registerService(ExtensionParser.class.getName(), streamInitiationExtensionParser, null);
		
		String filter = "(objectclass=" + XmppParser.class.getName() + ")";  
		context.addServiceListener(this, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		if (streamInitiationExtensionParserRegistration != null)
		{
			streamInitiationExtensionParserRegistration.unregister();
			streamInitiationExtensionParserRegistration = null;
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
