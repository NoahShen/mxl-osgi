package net.sf.mxlosgi.filetransfer;

import java.util.Hashtable;

import net.sf.mxlosgi.core.XmppMainManager;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.disco.DiscoInfoFeature;
import net.sf.mxlosgi.filetransfer.impl.DiscoInfoManagerServiceTracker;
import net.sf.mxlosgi.filetransfer.impl.FileTransferListenerServiceTracker;
import net.sf.mxlosgi.filetransfer.impl.FileTransferManagerImpl;
import net.sf.mxlosgi.filetransfer.impl.ReceiveFileTransferFactoryServiceTracker;
import net.sf.mxlosgi.filetransfer.impl.SendFileTransferFactoryServiceTracker;
import net.sf.mxlosgi.filetransfer.impl.SiFilter;

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
	private ServiceRegistration siFeatureRegistration;
	private ServiceRegistration siFileTransferFeatureRegistration;
	private DiscoInfoManagerServiceTracker discoInfoManagerServiceTracker;
	private FileTransferListenerServiceTracker fileTransferListenerServiceTracker;
	private SendFileTransferFactoryServiceTracker sendFileTransferFactoryServiceTracker;
	private ReceiveFileTransferFactoryServiceTracker receiveFileTransferFactoryServiceTracker;
	private ServiceRegistration fileTransferManagerRegistration;
	private ServiceRegistration fileTransferListenerRegistration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();

		DiscoInfoFeature siFeature = new DiscoInfoFeature(null, "http://jabber.org/protocol/si");
		siFeatureRegistration = context.registerService(DiscoInfoFeature.class.getName(), siFeature, null);
		
		DiscoInfoFeature siFileTransferFeature = new DiscoInfoFeature(null, "http://jabber.org/protocol/si/profile/file-transfer");
		siFileTransferFeatureRegistration = context.registerService(DiscoInfoFeature.class.getName(), siFileTransferFeature, null);
		
		discoInfoManagerServiceTracker = new DiscoInfoManagerServiceTracker(context);
		discoInfoManagerServiceTracker.open();
		
		fileTransferListenerServiceTracker = new FileTransferListenerServiceTracker(context);
		fileTransferListenerServiceTracker.open();
		
		sendFileTransferFactoryServiceTracker = new SendFileTransferFactoryServiceTracker(context);
		sendFileTransferFactoryServiceTracker.open();
		
		receiveFileTransferFactoryServiceTracker = new ReceiveFileTransferFactoryServiceTracker(context);
		receiveFileTransferFactoryServiceTracker.open();
		
		FileTransferManagerImpl fileTransferManager = new FileTransferManagerImpl(discoInfoManagerServiceTracker,
												fileTransferListenerServiceTracker,
												sendFileTransferFactoryServiceTracker,
												receiveFileTransferFactoryServiceTracker);
		
		fileTransferManagerRegistration
			= context.registerService(FileTransferManager.class.getName(), fileTransferManager, null);
		
		SiFilter siFilter = new SiFilter();
		Hashtable<String, StanzaFilter> properties = new Hashtable<String, StanzaFilter>();
		properties.put("filter", siFilter);
		
		fileTransferListenerRegistration = context.registerService(StanzaReceListener.class.getName(), fileTransferManager, properties);	
		
		String filter = "(objectclass=" + XmppMainManager.class.getName() + ")";  
		context.addServiceListener(this, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		if (siFeatureRegistration != null)
		{
			siFeatureRegistration.unregister();
			siFeatureRegistration = null;
		}
		
		if (siFileTransferFeatureRegistration != null)
		{
			siFileTransferFeatureRegistration.unregister();
			siFileTransferFeatureRegistration = null;
		}
		
		if (discoInfoManagerServiceTracker != null)
		{
			discoInfoManagerServiceTracker.close();
			discoInfoManagerServiceTracker = null;
		}
		
		if (fileTransferListenerServiceTracker != null)
		{
			fileTransferListenerServiceTracker.close();
			fileTransferListenerServiceTracker = null;
		}
		
		if (sendFileTransferFactoryServiceTracker != null)
		{
			sendFileTransferFactoryServiceTracker.close();
			sendFileTransferFactoryServiceTracker = null;
		}
		
		if (receiveFileTransferFactoryServiceTracker != null)
		{
			receiveFileTransferFactoryServiceTracker.close();
			receiveFileTransferFactoryServiceTracker = null;
		}
		
		if (fileTransferManagerRegistration != null)
		{
			fileTransferManagerRegistration.unregister();
			fileTransferManagerRegistration = null;
		}
		
		if (fileTransferListenerRegistration != null)
		{
			fileTransferListenerRegistration.unregister();
			fileTransferListenerRegistration = null;
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
