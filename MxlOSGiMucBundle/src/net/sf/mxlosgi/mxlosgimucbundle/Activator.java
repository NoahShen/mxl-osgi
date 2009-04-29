package net.sf.mxlosgi.mxlosgimucbundle;

import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoFeature;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoManager;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoItemsManager;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager;
import net.sf.mxlosgi.mxlosgimainbundle.filter.StanzaTypeFilter;
import net.sf.mxlosgi.mxlosgimucbundle.impl.MucManagerImpl;
import net.sf.mxlosgi.mxlosgimucbundle.parser.MucAdminExtensionParser;
import net.sf.mxlosgi.mxlosgimucbundle.parser.MucUserExtensionParser;
import net.sf.mxlosgi.mxlosgixmppbundle.Message;
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
	
	private MucManagerImpl mucManager;
	
	private ServiceRegistration mucManagerRegistration;

	private MucUserExtensionParser mucUserExtensionParser;

	private MucAdminExtensionParser mucAdminExtensionParser;

	private DiscoInfoFeature mucFeature;

	private ServiceTracker discoInfoManagerServiceTracker;

	private DiscoInfoManager discoInfoManager;

	private ServiceTracker discoItemsManagerServiceTracker;

	private DiscoItemsManager discoItemsManager;

	private ServiceTracker parserServiceTracker;

	private XMPPParser parser;

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

		discoInfoManagerServiceTracker = new ServiceTracker(context, DiscoInfoManager.class.getName(), null);
		discoInfoManagerServiceTracker.open();
		discoInfoManager = (DiscoInfoManager) discoInfoManagerServiceTracker.getService();
	
		mucFeature = new DiscoInfoFeature(null, "http://jabber.org/protocol/muc");
		discoInfoManager.addFeature(mucFeature);
		
		
		discoItemsManagerServiceTracker = new ServiceTracker(context, DiscoItemsManager.class.getName(), null);
		discoItemsManagerServiceTracker.open();
		discoItemsManager = (DiscoItemsManager) discoItemsManagerServiceTracker.getService();
		
		parserServiceTracker = new ServiceTracker(context, XMPPParser.class.getName(), null);
		parserServiceTracker.open();
		parser = (XMPPParser) parserServiceTracker.getService();

		mucUserExtensionParser = new MucUserExtensionParser();
		parser.addExtensionParser(mucUserExtensionParser);
	
		mucAdminExtensionParser = new MucAdminExtensionParser();
		parser.addExtensionParser(mucAdminExtensionParser);
		
		
		mucManager = new MucManagerImpl(mainManager, discoInfoManager, discoItemsManager);
		mucManagerRegistration = context.registerService(MucManager.class.getName(), mucManager, null);
		
		StanzaTypeFilter stanzaTypeFilter = new StanzaTypeFilter(Message.class);
		mainManager.addStanzaReceListener(mucManager, stanzaTypeFilter);

		context.addServiceListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		mainManager.removeStanzaReceListener(mucManager);
		
		parser.removeExtensionParser(mucUserExtensionParser);
		parser.removeExtensionParser(mucAdminExtensionParser);
		
		discoInfoManager.removeFeature(mucFeature);
		
		if (mucManagerRegistration != null)
		{
			mucManagerRegistration.unregister();
			mucManagerRegistration = null;
		}
		
		if (mainManagerServiceTracker != null)
		{
			mainManagerServiceTracker.close();
			mainManagerServiceTracker = null;
		}
		
		if (discoInfoManagerServiceTracker != null)
		{
			discoInfoManagerServiceTracker.close();
			discoInfoManagerServiceTracker = null;
		}
		
		if (discoItemsManagerServiceTracker != null)
		{
			discoItemsManagerServiceTracker.close();
			discoItemsManagerServiceTracker = null;
		}
		
		if (parserServiceTracker != null)
		{
			parserServiceTracker.close();
			parserServiceTracker = null;
		}
		
		parser = null;
		discoItemsManager = null;
		discoInfoManager = null;
		mainManager = null;
		
		mucFeature = null;
		mucUserExtensionParser = null;
		mucAdminExtensionParser = null;
		mucManager = null;
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
				discoInfoManager.addFeature(mucFeature);
			}
		}
	}

}
