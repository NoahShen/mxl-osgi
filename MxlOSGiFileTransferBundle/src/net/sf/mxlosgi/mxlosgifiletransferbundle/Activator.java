package net.sf.mxlosgi.mxlosgifiletransferbundle;

import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoFeature;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoManager;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoItemsManager;
import net.sf.mxlosgi.mxlosgifiletransferbundle.impl.FileTransferManagerImpl;
import net.sf.mxlosgi.mxlosgifiletransferbundle.impl.SiFilter;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager;

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
	
	private FileTransferManagerImpl fileTransferManager;

	private ServiceRegistration fileTransferManagerRegistration;

	private DiscoInfoFeature siFeature;

	private DiscoInfoFeature siFileTransferFeature;

	private ServiceTracker discoInfoManagerServiceTracker;

	private DiscoInfoManager discoInfoManager;

	private ServiceTracker discoItemsManagerServiceTracker;

	private Bundle bundle;

	private BundleContext context;

	private ServiceTracker mainManagerServiceTracker;

	private XMPPMainManager mainManager;

	private DiscoItemsManager discoItemsManager;

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
		
		siFeature = new DiscoInfoFeature(null, "http://jabber.org/protocol/si");
		discoInfoManager.addFeature(siFeature);
		
		siFileTransferFeature = new DiscoInfoFeature(null, "http://jabber.org/protocol/si/profile/file-transfer");
		discoInfoManager.addFeature(siFileTransferFeature);
		
		
		discoItemsManagerServiceTracker = new ServiceTracker(context, DiscoItemsManager.class.getName(), null);
		discoItemsManagerServiceTracker.open();
		discoItemsManager = (DiscoItemsManager) discoItemsManagerServiceTracker.getService();		
		
		
		fileTransferManager = new FileTransferManagerImpl(discoInfoManager, discoItemsManager);
		
		fileTransferManagerRegistration
			= context.registerService(FileTransferManager.class.getName(), fileTransferManager, null);
		
		SiFilter siFilter = new SiFilter();
		mainManager.addStanzaReceListener(fileTransferManager, siFilter);
	
		
		context.addServiceListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		mainManager.removeStanzaReceListener(fileTransferManager);
		
		discoInfoManager.removeFeature(siFeature);
		discoInfoManager.removeFeature(siFileTransferFeature);
		
		if (fileTransferManagerRegistration != null)
		{
			fileTransferManagerRegistration.unregister();
			fileTransferManagerRegistration = null;
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
		
		discoInfoManager = null;
		discoItemsManager = null;
		mainManager = null;
		
		siFeature = null;
		siFileTransferFeature = null;
		fileTransferManager = null;
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
			if (obj == mainManager || obj == discoInfoManager || obj == discoItemsManager)
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
				discoInfoManager.addFeature(siFeature);
				discoInfoManager.addFeature(siFileTransferFeature);
			}
		}
	}
}
