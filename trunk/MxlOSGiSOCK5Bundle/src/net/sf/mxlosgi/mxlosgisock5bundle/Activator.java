package net.sf.mxlosgi.mxlosgisock5bundle;

import net.sf.mxlosgi.mxlosgifiletransferbundle.FileTransferManager;
import net.sf.mxlosgi.mxlosgisock5bundle.parser.BytestreamExtensionParser;
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
	private BytestreamExtensionParser bytestreamExtensionParser;

	private SOCKS5SendFileTransferFactory sock5SendFactory;

	private SOCKS5ReceiveFileTransferFactory sock5ReceiveFactory;

	private ServiceTracker parserServiceTracker;

	private XMPPParser parser;

	private ServiceTracker fileTransferManagerServiceTracker;

	private FileTransferManager fileTransferManager;

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
		
		bytestreamExtensionParser = new BytestreamExtensionParser();
		parser.addExtensionParser(bytestreamExtensionParser);
		
		
		fileTransferManagerServiceTracker = new ServiceTracker(context, FileTransferManager.class.getName(), null);
		fileTransferManagerServiceTracker.open();
		fileTransferManager = (FileTransferManager) fileTransferManagerServiceTracker.getService();
		
		sock5SendFactory = new SOCKS5SendFileTransferFactory();
		fileTransferManager.addSendFileTransferFactory(sock5SendFactory);

		sock5ReceiveFactory = new SOCKS5ReceiveFileTransferFactory();
		fileTransferManager.addReceiveFileTransferFactory(sock5ReceiveFactory);
		
		
		context.addServiceListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		parser.removeExtensionParser(bytestreamExtensionParser);
		
		fileTransferManager.removeReceiveFileTransferFactory(sock5ReceiveFactory);
		fileTransferManager.removeSendFileTransferFactory(sock5SendFactory);
		
		if (fileTransferManagerServiceTracker != null)
		{
			fileTransferManagerServiceTracker.close();
			fileTransferManagerServiceTracker = null;
		}
		
		parser = null;
		fileTransferManager = null;
		
		sock5ReceiveFactory = null;
		sock5SendFactory = null;
		bytestreamExtensionParser = null;
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
			if (obj == parser || obj == fileTransferManager)
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
