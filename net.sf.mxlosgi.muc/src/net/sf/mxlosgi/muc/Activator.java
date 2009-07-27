package net.sf.mxlosgi.muc;


import net.sf.mxlosgi.core.XmppMainManager;
import net.sf.mxlosgi.disco.DiscoInfoFeature;
import net.sf.mxlosgi.muc.impl.DiscoInfoManagerServiceTracker;
import net.sf.mxlosgi.muc.impl.DiscoItemsManagerServiceTracker;
import net.sf.mxlosgi.muc.impl.MucChatListenerServiceTracker;
import net.sf.mxlosgi.muc.impl.MucListenerServiceTracker;
import net.sf.mxlosgi.muc.impl.MucManagerImpl;
import net.sf.mxlosgi.muc.parser.MucAdminExtensionParser;
import net.sf.mxlosgi.muc.parser.MucUserExtensionParser;
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
	private ServiceRegistration mucFeatureRegistration;
	private ServiceRegistration mucUserExtensionParserRegistration;
	private ServiceRegistration mucAdminExtensionParserRegistration;
	private DiscoInfoManagerServiceTracker discoInfoManagerServiceTracker;
	private DiscoItemsManagerServiceTracker discoItemsManagerServiceTracker;
	private MucListenerServiceTracker mucListenerServiceTracker;
	private MucChatListenerServiceTracker mucChatListenerServiceTracker;
	private ServiceRegistration mucManagerRegistration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();
		
		
		DiscoInfoFeature mucFeature = new DiscoInfoFeature(null, "http://jabber.org/protocol/muc");
		mucFeatureRegistration = context.registerService(DiscoInfoFeature.class.getName(), mucFeature, null);

		MucUserExtensionParser mucUserExtensionParser = new MucUserExtensionParser();
		mucUserExtensionParserRegistration = 
			context.registerService(ExtensionParser.class.getName(), mucUserExtensionParser, null);

	
		MucAdminExtensionParser mucAdminExtensionParser = new MucAdminExtensionParser();
		mucAdminExtensionParserRegistration = 
			context.registerService(ExtensionParser.class.getName(), mucAdminExtensionParser, null);		
		
		discoInfoManagerServiceTracker = new DiscoInfoManagerServiceTracker(context);
		discoInfoManagerServiceTracker.open();
		
		discoItemsManagerServiceTracker = new DiscoItemsManagerServiceTracker(context);
		discoItemsManagerServiceTracker.open();
		
		mucListenerServiceTracker = new MucListenerServiceTracker(context);
		mucListenerServiceTracker.open();
		
		mucChatListenerServiceTracker = new MucChatListenerServiceTracker(context);
		mucChatListenerServiceTracker.open();
		
		MucManagerImpl mucManager = new MucManagerImpl(discoInfoManagerServiceTracker, 
													discoItemsManagerServiceTracker, 
													mucListenerServiceTracker, 
													mucChatListenerServiceTracker);
		mucManagerRegistration = context.registerService(MucManager.class.getName(), mucManager, null);


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
		if (mucFeatureRegistration != null)
		{
			mucFeatureRegistration.unregister();
			mucFeatureRegistration = null;
		}
		
		if (mucUserExtensionParserRegistration != null)
		{
			mucUserExtensionParserRegistration.unregister();
			mucUserExtensionParserRegistration = null;
		}
		
		if (mucAdminExtensionParserRegistration != null)
		{
			mucAdminExtensionParserRegistration.unregister();
			mucAdminExtensionParserRegistration = null;
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
		
		if (mucListenerServiceTracker != null)
		{
			mucListenerServiceTracker.close();
			mucListenerServiceTracker = null;
		}
		
		if (mucChatListenerServiceTracker != null)
		{
			mucChatListenerServiceTracker.close();
			mucChatListenerServiceTracker = null;
		}
		
		if (mucManagerRegistration != null)
		{
			mucManagerRegistration.unregister();
			mucManagerRegistration = null;
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
