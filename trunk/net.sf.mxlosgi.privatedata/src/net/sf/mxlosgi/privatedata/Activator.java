package net.sf.mxlosgi.privatedata;

import net.sf.mxlosgi.privatedata.impl.PrivateDataManagerImpl;
import net.sf.mxlosgi.privatedata.parser.PrivateDataExtensionParser;
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

	private ServiceRegistration privateDataManagerRegistration;
	
	private ServiceRegistration privateDataParserRegistration;
	
	private Bundle bundle;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();

		PrivateDataExtensionParser privateDataParser = new PrivateDataExtensionParser();
		privateDataParserRegistration = context.registerService(ExtensionParser.class.getName(), privateDataParser, null);

		PrivateDataManagerImpl privateDataManager = new PrivateDataManagerImpl();
		privateDataManagerRegistration = context.registerService(PrivateDataManager.class.getName(), privateDataManager, null);
		
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
		
		if (privateDataParserRegistration != null)
		{
			privateDataParserRegistration.unregister();
			privateDataParserRegistration = null;
		}
		if (privateDataManagerRegistration != null)
		{
			privateDataManagerRegistration.unregister();
			privateDataManagerRegistration = null;
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
