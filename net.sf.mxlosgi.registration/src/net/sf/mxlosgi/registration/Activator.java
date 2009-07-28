package net.sf.mxlosgi.registration;


import java.util.Hashtable;

import net.sf.mxlosgi.core.XmppMainManager;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.core.filter.StanzaTypeFilter;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.registration.impl.RegistrationManagerImpl;
import net.sf.mxlosgi.registration.parser.RegisterExtensionParser;
import net.sf.mxlosgi.xmpp.StreamFeature;
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


	private ServiceRegistration registrationManagerRegistration;

	private Bundle bundle;

	private ServiceRegistration registerExtensionParserRegistration;

	private ServiceRegistration registrationManagerListener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();
		
		RegisterExtensionParser registerExtensionParser = new RegisterExtensionParser();
		registerExtensionParserRegistration = 
				context.registerService(ExtensionParser.class.getName(), registerExtensionParser, null);
		
		RegistrationManagerImpl registrationManager = new RegistrationManagerImpl();
		registrationManagerRegistration = 
			context.registerService(RegistrationManager.class.getName(), registrationManager, null);
		
		Hashtable<String, StanzaFilter> properties = new Hashtable<String, StanzaFilter>();
		properties.put("filter", new StanzaTypeFilter(StreamFeature.class));
		registrationManagerListener = 
			context.registerService(StanzaReceListener.class.getName(), registrationManager, properties);
		
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
		if (registerExtensionParserRegistration != null)
		{
			registerExtensionParserRegistration.unregister();
			registerExtensionParserRegistration = null;
		}
		
		if (registrationManagerRegistration != null)
		{
			registrationManagerRegistration.unregister();
			registrationManagerRegistration = null;
		}

		if (registrationManagerListener != null)
		{
			registrationManagerListener.unregister();
			registrationManagerListener = null;
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
