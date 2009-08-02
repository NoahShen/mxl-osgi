package net.sf.mxlosgi.sock5;


import net.sf.mxlosgi.filetransfer.ReceiveFileTransferFactory;
import net.sf.mxlosgi.filetransfer.SendFileTransferFactory;
import net.sf.mxlosgi.sock5.impl.DiscoInfoManagerServiceTracker;
import net.sf.mxlosgi.sock5.impl.DiscoItemsManagerServiceTracker;
import net.sf.mxlosgi.sock5.impl.ProxyServiceTracker;
import net.sf.mxlosgi.sock5.parser.BytestreamExtensionParser;
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
	private ServiceRegistration bytestreamExtensionParserRegistration;
	private DiscoInfoManagerServiceTracker discoInfoManagerServiceTracker;
	private DiscoItemsManagerServiceTracker discoItemsManagerServiceTracker;
	private ProxyServiceTracker proxyServiceTracker;
	private ServiceRegistration sock5SendFactoryRegistration;
	private ServiceRegistration sock5ReceiveFactoryRegistration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();
		
		
		BytestreamExtensionParser bytestreamExtensionParser = new BytestreamExtensionParser();
		bytestreamExtensionParserRegistration = 
			context.registerService(ExtensionParser.class.getName(), bytestreamExtensionParser, null);

		
		discoInfoManagerServiceTracker = new DiscoInfoManagerServiceTracker(context);
		discoInfoManagerServiceTracker.open();
		
		discoItemsManagerServiceTracker = new DiscoItemsManagerServiceTracker(context);
		discoItemsManagerServiceTracker.open();
		
		proxyServiceTracker = new ProxyServiceTracker(context);
		proxyServiceTracker.open();
		
		SOCKS5SendFileTransferFactory sock5SendFactory = 
						new SOCKS5SendFileTransferFactory(discoInfoManagerServiceTracker,
													discoItemsManagerServiceTracker,
													proxyServiceTracker);
		sock5SendFactoryRegistration =
				context.registerService(SendFileTransferFactory.class.getName(), sock5SendFactory, null);

		SOCKS5ReceiveFileTransferFactory sock5ReceiveFactory = new SOCKS5ReceiveFileTransferFactory();
		sock5ReceiveFactoryRegistration = 
				context.registerService(ReceiveFileTransferFactory.class.getName(), sock5ReceiveFactory, null);
		
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
		if (bytestreamExtensionParserRegistration != null)
		{
			bytestreamExtensionParserRegistration.unregister();
			bytestreamExtensionParserRegistration = null;
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
		
		if (proxyServiceTracker != null)
		{
			proxyServiceTracker.close();
			proxyServiceTracker = null;
		}
		
		if (sock5SendFactoryRegistration != null)
		{
			sock5SendFactoryRegistration.unregister();
			sock5SendFactoryRegistration = null;
		}
		
		if (sock5ReceiveFactoryRegistration != null)
		{
			sock5ReceiveFactoryRegistration.unregister();
			sock5ReceiveFactoryRegistration = null;
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
