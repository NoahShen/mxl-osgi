package net.sf.mxlosgi.xmppparser;

import net.sf.mxlosgi.xmppparser.impl.ExtensionParserServiceTracker;
import net.sf.mxlosgi.xmppparser.impl.XMPPParserImpl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator
{

	private ServiceRegistration sr;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		ExtensionParserServiceTracker tracker = new ExtensionParserServiceTracker(context);
		tracker.open();
		XMPPParserImpl parser = new XMPPParserImpl(tracker);
		sr = context.registerService(XMPPParser.class.getName(), parser, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		if (sr != null)
		{
			sr.unregister();
			sr = null;
		}
	}

}
