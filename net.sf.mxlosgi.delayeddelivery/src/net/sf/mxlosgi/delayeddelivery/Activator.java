package net.sf.mxlosgi.delayeddelivery;


import net.sf.mxlosgi.delayeddelivery.parser.DelayedDeliveryExtensionParser;
import net.sf.mxlosgi.delayeddelivery.parser.DeprecatedDelayedDeliveryExtensionParser;
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
	private ServiceRegistration delayedParserRegistration;
	private ServiceRegistration deprecatedDelayedParserRegistration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();
		
		DelayedDeliveryExtensionParser delayedParser = new DelayedDeliveryExtensionParser();
		delayedParserRegistration = context.registerService(ExtensionParser.class.getName(), delayedParser, null);
		
		
		DeprecatedDelayedDeliveryExtensionParser deprecatedDelayedParser = 
										new DeprecatedDelayedDeliveryExtensionParser();
		deprecatedDelayedParserRegistration = 
						context.registerService(ExtensionParser.class.getName(), deprecatedDelayedParser, null);
		
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
		if (delayedParserRegistration != null)
		{
			delayedParserRegistration.unregister();
			delayedParserRegistration = null;
		}
		
		if (deprecatedDelayedParserRegistration != null)
		{
			deprecatedDelayedParserRegistration.unregister();
			deprecatedDelayedParserRegistration = null;
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
