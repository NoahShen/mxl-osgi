/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.listener.XmlStringListener;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class XmlStringListenerServiceTracker extends ServiceTracker
{

	public XmlStringListenerServiceTracker(BundleContext context)
	{
		super(context, XmlStringListener.class.getName(), null);
	}
	
	public void fireXmlStringListener(XmppConnection connection, String xml)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			XmlStringListener listener  = (XmlStringListener) obj;
			listener.processXmlString(connection, xml);
		}
	}
}
