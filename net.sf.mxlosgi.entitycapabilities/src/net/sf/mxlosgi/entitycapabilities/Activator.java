package net.sf.mxlosgi.entitycapabilities;

import net.sf.mxlosgi.disco.DiscoInfoFeature;
import net.sf.mxlosgi.entitycapabilities.parser.EntityCapabilitiesExtensionParser;
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
	private ServiceRegistration extensionParserRegistration;
	private ServiceRegistration capFeatureRegistration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();

		EntityCapabilitiesExtensionParser extensionParser = new EntityCapabilitiesExtensionParser();
		extensionParserRegistration = context.registerService(ExtensionParser.class.getName(), extensionParser, null);


		DiscoInfoFeature capFeature = new DiscoInfoFeature(null, "http://jabber.org/protocol/caps'");
		capFeatureRegistration = context.registerService(DiscoInfoFeature.class.getName(), capFeature, null);
		
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
		if (extensionParserRegistration != null)
		{
			extensionParserRegistration.unregister();
			extensionParserRegistration = null;
		}
		
		if (capFeatureRegistration != null)
		{
			capFeatureRegistration.unregister();
			capFeatureRegistration = null;
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
