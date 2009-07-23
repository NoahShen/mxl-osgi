/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.core.interceptor.StanzaSendInterceptor;
import net.sf.mxlosgi.xmpp.XmlStanza;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class StanzaSendInterceptorServiceTracker extends ServiceTracker
{

	public StanzaSendInterceptorServiceTracker(BundleContext context)
	{
		super(context, StanzaSendInterceptor.class.getName(), null);
	}

	public boolean fireStanzaSendInterceptor(XmppConnection connection, XmlStanza stanza)
	{
		ServiceReference[] references = getServiceReferences();
		if (references == null)
		{
			return false;
		}
		
		for (ServiceReference reference : references)
		{
			StanzaFilter filter = (StanzaFilter) reference.getProperty("filter");
			
			if (filter == null || filter.accept(connection, stanza))
			{
				StanzaSendInterceptor interceptor = (StanzaSendInterceptor) getService(reference);
				if (interceptor.interceptSendStanza(connection, stanza))
				{
					return true;
				}
			}
		}
		
		return false;
	}
}
