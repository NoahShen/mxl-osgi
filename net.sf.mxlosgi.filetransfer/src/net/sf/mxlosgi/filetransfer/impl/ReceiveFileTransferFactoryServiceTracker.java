/**
 * 
 */
package net.sf.mxlosgi.filetransfer.impl;

import net.sf.mxlosgi.filetransfer.ReceiveFileTransferFactory;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class ReceiveFileTransferFactoryServiceTracker extends ServiceTracker
{

	public ReceiveFileTransferFactoryServiceTracker(BundleContext context)
	{
		super(context, ReceiveFileTransferFactory.class.getName(), null);
	}
	
	public ReceiveFileTransferFactory getReceiveFileTransferFactory(String method)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return null;
		}
		
		for (Object obj : services)
		{
			ReceiveFileTransferFactory factory = (ReceiveFileTransferFactory) obj;
			if (method.equals(factory.getStreamMethod()))
			{
				return factory;
			}
		}
		
		return null;
	}

}
