package net.sf.mxlosgi.mxlosgientitycapabilitiesbundle;

import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoFeature;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoManager;
import net.sf.mxlosgi.mxlosgientitycapabilitiesbundle.parser.EntityCapabilitiesExtensionParser;
import net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator, ServiceListener
{

	private ServiceTracker parserServiceTracker;

	private XMPPParser parser;

	private EntityCapabilitiesExtensionParser extensionParser;

	private ServiceTracker discoInfoManagerServiceTracker;

	private DiscoInfoManager discoInfoManager;

	private DiscoInfoFeature capFeature;

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

		extensionParser = new EntityCapabilitiesExtensionParser();
		parser.addExtensionParser(extensionParser);

		discoInfoManagerServiceTracker = new ServiceTracker(context, DiscoInfoManager.class.getName(), null);
		discoInfoManagerServiceTracker.open();
		discoInfoManager = (DiscoInfoManager) discoInfoManagerServiceTracker.getService();

		capFeature = new DiscoInfoFeature(null, "http://jabber.org/protocol/caps'");
		discoInfoManager.addFeature(capFeature);
		
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
		parser.removeExtensionParser(extensionParser);
		discoInfoManager.removeFeature(capFeature);
		
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

		capFeature = null;
		discoInfoManager = null;
		parser = null;
		extensionParser = null;
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
		else if (eventType == ServiceEvent.REGISTERED)
		{
			if (obj == discoInfoManager)
			{
				discoInfoManager.addFeature(capFeature);
			}
		}
	}

}
