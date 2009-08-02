/**
 * 
 */
package net.sf.mxlosgi.filetransfer.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.mxlosgi.filetransfer.SendFileTransferFactory;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class SendFileTransferFactoryServiceTracker extends ServiceTracker
{

	public SendFileTransferFactoryServiceTracker(BundleContext context)
	{
		super(context, SendFileTransferFactory.class.getName(), null);
	}
	
	public SendFileTransferFactory getSendFileTransferFactory(String method)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return null;
		}
		
		for (Object obj : services)
		{
			SendFileTransferFactory factory = (SendFileTransferFactory) obj;
			if (method.equals(factory.getStreamMethod()))
			{
				return factory;
			}
		}
		
		return null;
	}
	
	public String[] getStreamMethods()
	{
		List<String> methods = new ArrayList<String>();
		
		Object[] services = getServices();
		if (services == null)
		{
			return null;
		}
		
		for (Object obj : services)
		{
			SendFileTransferFactory factory = (SendFileTransferFactory) obj;
			methods.add(factory.getStreamMethod());
		}
		
		return methods.toArray(new String[]{});
	}

}
