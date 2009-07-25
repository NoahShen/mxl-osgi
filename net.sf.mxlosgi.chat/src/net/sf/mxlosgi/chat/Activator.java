package net.sf.mxlosgi.chat;

import java.util.Hashtable;

import net.sf.mxlosgi.chat.impl.AllChatMessageFilter;
import net.sf.mxlosgi.chat.impl.ChatListenerServiceTracker;
import net.sf.mxlosgi.chat.impl.ChatManagerListenerServiceTracker;
import net.sf.mxlosgi.chat.impl.XmppChatManagerImpl;
import net.sf.mxlosgi.core.XmppMainManager;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.core.listener.StanzaReceListener;

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
	private ChatListenerServiceTracker chatListenerServiceTracker;
	private ChatManagerListenerServiceTracker chatManagerListenerServiceTracker;
	private ServiceRegistration chatManagerRegistration;
	private ServiceRegistration chatListenerRegistration;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();
		
		chatListenerServiceTracker = new ChatListenerServiceTracker(context);
		chatListenerServiceTracker.open();
		
		chatManagerListenerServiceTracker = new ChatManagerListenerServiceTracker(context);
		chatManagerListenerServiceTracker.open();
		
		XmppChatManagerImpl chatManager = new XmppChatManagerImpl(chatListenerServiceTracker, chatManagerListenerServiceTracker);
		chatManagerRegistration = context.registerService(XmppChatManager.class.getName(), chatManager, null);
		
		AllChatMessageFilter allChatMessageFilter = new AllChatMessageFilter();
		Hashtable<String, StanzaFilter> properties = new Hashtable<String, StanzaFilter>();
		properties.put("filter", allChatMessageFilter);
		chatListenerRegistration = context.registerService(StanzaReceListener.class.getName(), chatManager, properties);
		
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
		if (chatListenerServiceTracker != null)
		{
			chatListenerServiceTracker.close();
			chatListenerServiceTracker = null;
		}
		
		if (chatManagerListenerServiceTracker != null)
		{
			chatManagerListenerServiceTracker.close();
			chatManagerListenerServiceTracker = null;
		}
		
		if (chatManagerRegistration != null)
		{
			chatManagerRegistration.unregister();
			chatManagerRegistration = null;
		}
		
		if (chatListenerRegistration != null)
		{
			chatListenerRegistration.unregister();
			chatListenerRegistration = null;
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
