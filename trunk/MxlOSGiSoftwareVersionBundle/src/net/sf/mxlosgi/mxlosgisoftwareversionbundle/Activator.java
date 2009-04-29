package net.sf.mxlosgi.mxlosgisoftwareversionbundle;

import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoFeature;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoManager;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager;
import net.sf.mxlosgi.mxlosgisoftwareversionbundle.impl.SoftwareVersionFilter;
import net.sf.mxlosgi.mxlosgisoftwareversionbundle.impl.SoftwareVersionManagerImpl;
import net.sf.mxlosgi.mxlosgisoftwareversionbundle.parser.SoftwareVersionExtensionParser;
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
	private ServiceRegistration versionManagerRegistration;

	private SoftwareVersionManagerImpl softwareVersionManager;

	private SoftwareVersionExtensionParser versionParser;

	private DiscoInfoFeature softwareVersionFeature;

	private ServiceTracker parserServiceTracker;

	private XMPPParser parser;

	private ServiceTracker discoInfoManagerServiceTracker;

	private DiscoInfoManager discoInfoManager;

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

		parserServiceTracker = new ServiceTracker(context, XMPPParser.class.getName(), null);
		parserServiceTracker.open();
		parser = (XMPPParser) parserServiceTracker.getService();
		
		discoInfoManagerServiceTracker = new ServiceTracker(context, DiscoInfoManager.class.getName(), null);
		discoInfoManagerServiceTracker.open();
		discoInfoManager = (DiscoInfoManager) discoInfoManagerServiceTracker.getService();
		
		softwareVersionManager = new SoftwareVersionManagerImpl();
		softwareVersionManager.setName("Mxl-OSGi");
		softwareVersionManager.setVersion("v0.5");
		
		versionManagerRegistration = context.registerService(SoftwareVersionManager.class.getName(), softwareVersionManager, null);

		SoftwareVersionFilter softwareVersionFilter = new SoftwareVersionFilter();
		
		mainManager.addStanzaReceListener(softwareVersionManager, softwareVersionFilter);
		
		versionParser = new SoftwareVersionExtensionParser();
		parser.addExtensionParser(versionParser);

		softwareVersionFeature = new DiscoInfoFeature(null, "jabber:iq:version");
		discoInfoManager.addFeature(softwareVersionFeature);
		
		context.addServiceListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		mainManager.removeStanzaReceListener(softwareVersionManager);
		parser.removeExtensionParser(versionParser);
		discoInfoManager.removeFeature(softwareVersionFeature);
		
		if (versionManagerRegistration != null)
		{
			versionManagerRegistration.unregister();
			versionManagerRegistration = null;
		}		
		
		if (discoInfoManagerServiceTracker != null)
		{
			discoInfoManagerServiceTracker.close();
			discoInfoManagerServiceTracker = null;
		}
		
		if (parserServiceTracker != null)
		{
			parserServiceTracker.close();
			parserServiceTracker = null;
		}
		
		if (mainManagerServiceTracker != null)
		{
			mainManagerServiceTracker.close();
			mainManagerServiceTracker = null;
		}
		
		mainManager = null;
		parser = null;
		discoInfoManager = null;
		
		softwareVersionFeature = null;
		softwareVersionManager = null;
		versionParser = null;
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
			if (obj == parser || obj == mainManager)
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
				discoInfoManager.addFeature(softwareVersionFeature);
			}
		}
	}

}
