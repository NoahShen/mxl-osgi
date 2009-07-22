/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.core.interceptor.StanzaReceInterceptor;
import net.sf.mxlosgi.xmpp.XMLStanza;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class StanzaReceInterceptorServiceTracker extends ServiceTracker
{

	public StanzaReceInterceptorServiceTracker(BundleContext context)
	{
		super(context, StanzaReceInterceptor.class.getName(), null);
	}

	public boolean fireStanzaReceInterceptor(XmppConnection connection, XMLStanza stanza)
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
				StanzaReceInterceptor interceptor = (StanzaReceInterceptor) getService(reference);
				if (interceptor.interceptReceStanza(connection, stanza))
				{
					return true;
				}
			}
		}
		
		return false;
	}
}
