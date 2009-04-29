package net.sf.mxlosgi.mxlosgivcardbundle;

import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoFeature;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoManager;
import net.sf.mxlosgi.mxlosgivcardbundle.impl.VCardManagerImpl;
import net.sf.mxlosgi.mxlosgivcardbundle.parser.VCardExtensionParser;
import net.sf.mxlosgi.mxlosgivcardbundle.parser.VCardTempUpdateExtensionParser;
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

	private VCardManagerImpl vCardManager;

	private ServiceRegistration vCardManagerRegistration;

	private VCardExtensionParser vCardParser;

	private VCardTempUpdateExtensionParser vCardTempParser;

	private DiscoInfoFeature vCardFeature;

	private ServiceTracker parserServiceTracker;

	private XMPPParser parser;

	private ServiceTracker discoInfoManagerServiceTracker;

	private DiscoInfoManager discoInfoManager;

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
		
		vCardParser = new VCardExtensionParser();
		parser.addExtensionParser(vCardParser);

		vCardTempParser = new VCardTempUpdateExtensionParser();
		parser.addExtensionParser(vCardTempParser);
		
		discoInfoManagerServiceTracker = new ServiceTracker(context, DiscoInfoManager.class.getName(), null);
		discoInfoManagerServiceTracker.open();
		discoInfoManager = (DiscoInfoManager) discoInfoManagerServiceTracker.getService();

		vCardFeature = new DiscoInfoFeature(null, "vcard-temp");
		discoInfoManager.addFeature(vCardFeature);
		
		vCardManager = new VCardManagerImpl();
		vCardManagerRegistration = context.registerService(VCardManager.class.getName(), vCardManager, null);

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
		parser.removeExtensionParser(vCardParser);
		parser.removeExtensionParser(vCardTempParser);
		
		discoInfoManager.removeFeature(vCardFeature);
		
		if (vCardManagerRegistration != null)
		{
			vCardManagerRegistration.unregister();
			vCardManagerRegistration = null;
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
		
		parser = null;
		discoInfoManager = null;
		
		vCardFeature = null;
		vCardParser = null;
		vCardTempParser = null;
		vCardManager = null;
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
				discoInfoManager.addFeature(vCardFeature);
			}
		}
	}

}
