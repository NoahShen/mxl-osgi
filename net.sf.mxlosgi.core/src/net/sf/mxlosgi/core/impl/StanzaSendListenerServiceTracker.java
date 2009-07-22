/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.core.listener.StanzaSendListener;
import net.sf.mxlosgi.xmpp.XMLStanza;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class StanzaSendListenerServiceTracker extends ServiceTracker
{

	public StanzaSendListenerServiceTracker(BundleContext context)
	{
		super(context, StanzaSendListener.class.getName(), null);
	}

	public void fireStanzaSendListener(XmppConnection connection, XMLStanza stanza)
	{
		ServiceReference[] references = getServiceReferences();
		if (references == null)
		{
			return;
		}
		
		for (ServiceReference reference : references)
		{
			StanzaFilter filter = (StanzaFilter) reference.getProperty("filter");
			
			if (filter == null || filter.accept(connection, stanza))
			{
				StanzaSendListener listener = (StanzaSendListener) getService(reference);
				listener.processSendStanza(connection, stanza);
			}
		}
	}
}
