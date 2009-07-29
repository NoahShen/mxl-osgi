package net.sf.mxlosgi.softwareversion;


import java.util.Hashtable;

import net.sf.mxlosgi.core.XmppMainManager;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.disco.DiscoInfoFeature;
import net.sf.mxlosgi.softwareversion.impl.SoftwareVersionFilter;
import net.sf.mxlosgi.softwareversion.impl.SoftwareVersionManagerImpl;
import net.sf.mxlosgi.softwareversion.parser.SoftwareVersionExtensionParser;
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
	private ServiceRegistration softwareVersionManagerRegistration;
	private ServiceRegistration softwareVersionManagerListener;
	private ServiceRegistration versionParserRegistration;
	private ServiceRegistration softwareVersionFeatureListener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();
		
		SoftwareVersionManagerImpl softwareVersionManager = new SoftwareVersionManagerImpl();
		softwareVersionManager.setName("Mxl-OSGi");
		softwareVersionManager.setVersion("v0.6");
		softwareVersionManagerRegistration = context.registerService(SoftwareVersionManager.class.getName(), softwareVersionManager, null);

		SoftwareVersionFilter softwareVersionFilter = new SoftwareVersionFilter();
		Hashtable<String, StanzaFilter> properties = new Hashtable<String, StanzaFilter>();
		properties.put("filter", softwareVersionFilter);
		softwareVersionManagerListener = context.registerService(StanzaReceListener.class.getName(), softwareVersionManager, properties);
		
		SoftwareVersionExtensionParser versionParser = new SoftwareVersionExtensionParser();
		versionParserRegistration = context.registerService(ExtensionParser.class.getName(), versionParser, null);

		DiscoInfoFeature softwareVersionFeature = new DiscoInfoFeature(null, "jabber:iq:version");
		softwareVersionFeatureListener = context.registerService(DiscoInfoFeature.class.getName(), softwareVersionFeature, null);
		
		String filter = "(|(objectclass=" + XmppMainManager.class.getName() + ")" +
					"(objectclass=" + XmppParser.class.getName() + "))";  
		
		context.addServiceListener(this, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		if (softwareVersionManagerRegistration != null)
		{
			softwareVersionManagerRegistration.unregister();
			softwareVersionManagerRegistration = null;
		}
		
		if (softwareVersionFeatureListener != null)
		{
			softwareVersionFeatureListener.unregister();
			softwareVersionFeatureListener = null;
		}
		
		if (softwareVersionManagerListener != null)
		{
			softwareVersionManagerListener.unregister();
			softwareVersionManagerListener = null;
		}
		
		if (versionParserRegistration != null)
		{
			versionParserRegistration.unregister();
			versionParserRegistration = null;
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
