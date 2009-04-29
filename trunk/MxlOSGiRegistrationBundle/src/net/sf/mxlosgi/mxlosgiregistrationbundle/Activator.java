package net.sf.mxlosgi.mxlosgiregistrationbundle;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager;
import net.sf.mxlosgi.mxlosgimainbundle.filter.StanzaTypeFilter;
import net.sf.mxlosgi.mxlosgiregistrationbundle.impl.RegistrationManagerImpl;
import net.sf.mxlosgi.mxlosgiregistrationbundle.parser.RegisterExtensionParser;
import net.sf.mxlosgi.mxlosgixmppbundle.StreamFeature;
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

	private RegistrationManagerImpl registrationManager;

	private ServiceRegistration registrationManagerRegistration;

	private ServiceTracker parserServiceTracker;

	private XMPPParser parser;

	private RegisterExtensionParser registerExtensionParser;

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
		
		registerExtensionParser = new RegisterExtensionParser();
		parser.addExtensionParser(registerExtensionParser);
		
		registrationManager = new RegistrationManagerImpl();
		mainManager.addStanzaReceListener(registrationManager, new StanzaTypeFilter(StreamFeature.class));
		
		registrationManagerRegistration = 
			context.registerService(RegistrationManager.class.getName(), registrationManager, null);
		
		context.addServiceListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		mainManager.removeStanzaReceListener(registrationManager);
		parser.removeExtensionParser(registerExtensionParser);
		
		if (registrationManagerRegistration != null)
		{
			registrationManagerRegistration.unregister();
			registrationManagerRegistration = null;
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
		
		parser = null;
		registerExtensionParser = null;
		registrationManager = null;
		mainManager = null;
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
	}
}
