package net.sf.mxlosgi.core;

import net.sf.mxlosgi.core.impl.ConnectionListenerServiceTracker;
import net.sf.mxlosgi.core.impl.ContactListenerServiceTracker;
import net.sf.mxlosgi.core.impl.SaslMechanismServiceTracker;
import net.sf.mxlosgi.core.impl.StanzaReceInterceptorServiceTracker;
import net.sf.mxlosgi.core.impl.StanzaReceListenerServiceTracker;
import net.sf.mxlosgi.core.impl.StanzaSendInterceptorServiceTracker;
import net.sf.mxlosgi.core.impl.StanzaSendListenerServiceTracker;
import net.sf.mxlosgi.core.impl.XmlStringListenerServiceTracker;
import net.sf.mxlosgi.core.impl.XmppMainManagerImpl;
import net.sf.mxlosgi.core.impl.XmppOwnerListenerServiceTracker;
import net.sf.mxlosgi.core.impl.XmppParserServiceTracker;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator, ServiceListener
{

	private ServiceRegistration sr;

	private SaslMechanismServiceTracker saslMechanismServiceTracker;

	private Bundle bundle;

	private XmppParserServiceTracker xmppParserServiceTracker;

	private ConnectionListenerServiceTracker connectionListenerServiceTracker;

	private StanzaSendInterceptorServiceTracker stanzaSendInterceptorServiceTracker;

	private XmlStringListenerServiceTracker xmlStringListenerServiceTracker;

	private StanzaReceInterceptorServiceTracker stanzaReceInterceptorServiceTracker;

	private StanzaReceListenerServiceTracker stanzaReceListenerServiceTracker;

	private StanzaSendListenerServiceTracker stanzaSendListenerServiceTracker;

	private ContactListenerServiceTracker contactListenerServiceTracker;

	private XmppOwnerListenerServiceTracker xmppOwnerListenerServiceTracker;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();
		
		String filter = "(objectClass=" + XmppParser.class.getName() + ")";
		context.addServiceListener(this, filter);
		
		xmppParserServiceTracker = new XmppParserServiceTracker(context);
		xmppParserServiceTracker.open();
		
		saslMechanismServiceTracker = new SaslMechanismServiceTracker(context);
		saslMechanismServiceTracker.open();

		connectionListenerServiceTracker = new ConnectionListenerServiceTracker(context);
		connectionListenerServiceTracker.open();
		
		stanzaSendInterceptorServiceTracker = new StanzaSendInterceptorServiceTracker(context);
		stanzaSendInterceptorServiceTracker.open();
		
		xmlStringListenerServiceTracker = new XmlStringListenerServiceTracker(context);
		xmlStringListenerServiceTracker.open();
		
		stanzaReceInterceptorServiceTracker = new StanzaReceInterceptorServiceTracker(context);
		stanzaReceInterceptorServiceTracker.open();
		
		stanzaReceListenerServiceTracker = new StanzaReceListenerServiceTracker(context);
		stanzaReceListenerServiceTracker.open();
		
		stanzaSendListenerServiceTracker = new StanzaSendListenerServiceTracker(context);
		stanzaSendListenerServiceTracker.open();
		
		contactListenerServiceTracker = new ContactListenerServiceTracker(context);
		contactListenerServiceTracker.open();
		
		xmppOwnerListenerServiceTracker = new XmppOwnerListenerServiceTracker(context);
		xmppOwnerListenerServiceTracker.open();
		
		XmppMainManagerImpl mainManager = new XmppMainManagerImpl(xmppParserServiceTracker, 
															saslMechanismServiceTracker,
															connectionListenerServiceTracker,
															xmlStringListenerServiceTracker,
															stanzaReceInterceptorServiceTracker,
															stanzaSendInterceptorServiceTracker,
															stanzaReceListenerServiceTracker,
															stanzaSendListenerServiceTracker,
															contactListenerServiceTracker,
															xmppOwnerListenerServiceTracker);
		
		sr = context.registerService(XmppMainManager.class.getName(), mainManager, null);
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
		
		if (saslMechanismServiceTracker != null)
		{
			saslMechanismServiceTracker.close();
			saslMechanismServiceTracker = null;
		}
		
		if (xmppParserServiceTracker != null)
		{
			xmppParserServiceTracker.close();
			xmppParserServiceTracker = null;
		}

		if (connectionListenerServiceTracker != null)
		{
			connectionListenerServiceTracker.close();
			connectionListenerServiceTracker = null;
		}
		
		if (stanzaSendInterceptorServiceTracker != null)
		{
			stanzaSendInterceptorServiceTracker.close();
			stanzaSendInterceptorServiceTracker = null;
		}
		
		if (xmlStringListenerServiceTracker != null)
		{
			xmlStringListenerServiceTracker.close();
			xmlStringListenerServiceTracker = null;
		}
		
		if (stanzaReceInterceptorServiceTracker != null)
		{
			stanzaReceInterceptorServiceTracker.close();
			stanzaReceInterceptorServiceTracker = null;
		}
		
		if (stanzaReceListenerServiceTracker != null)
		{
			stanzaReceListenerServiceTracker.close();
			stanzaReceListenerServiceTracker = null;
		}
		
		if (stanzaSendListenerServiceTracker != null)
		{
			stanzaSendListenerServiceTracker.close();
			stanzaSendListenerServiceTracker = null;
		}
		
		if (contactListenerServiceTracker != null)
		{
			contactListenerServiceTracker.close();
			contactListenerServiceTracker = null;
		}
		
		if (xmppOwnerListenerServiceTracker != null)
		{
			xmppOwnerListenerServiceTracker.close();
			xmppOwnerListenerServiceTracker = null;
		}

		
		bundle = null;
	}

	@Override
	public void serviceChanged(ServiceEvent event)
	{
		int eventType = event.getType();
		if (eventType == ServiceEvent.UNREGISTERING)
		{
			if (xmppParserServiceTracker.getTrackingCount() <= 0)
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
