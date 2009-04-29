package net.sf.mxlosgi.mxlosgichatbundle;

import net.sf.mxlosgi.mxlosgichatbundle.impl.ChatImpl;
import net.sf.mxlosgi.mxlosgichatbundle.impl.XMPPChatManagerImpl;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager;
import net.sf.mxlosgi.mxlosgimainbundle.filter.StanzaFilter;
import net.sf.mxlosgi.mxlosgimainbundle.listener.StanzaReceListener;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.Message;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator, StanzaReceListener, ServiceListener
{
	private XMPPChatManagerImpl chatManager;

	private ServiceRegistration chatManagerRegistration;

	private ServiceTracker mainManagerServiceTracker;

	private XMPPMainManager mainManager;

	private Bundle bundle;

	private BundleContext context;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();
		this.context = context;
		
		mainManagerServiceTracker = new ServiceTracker(context, XMPPMainManager.class.getName(), null);
		mainManagerServiceTracker.open();
		mainManager = (XMPPMainManager) mainManagerServiceTracker.getService();
		
		chatManager = new XMPPChatManagerImpl(mainManager);
		chatManagerRegistration = context.registerService(XMPPChatManager.class.getName(), chatManager, null);
		
		mainManager.addStanzaReceListener(this, new AllChatMessageFilter());
		
		String filter = "(objectclass=" + XMPPMainManager.class.getName() + ")";  
		context.addServiceListener(this, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		mainManager.removeStanzaReceListener(this);
		
		if (mainManagerServiceTracker != null)
		{
			mainManagerServiceTracker.close();
			mainManagerServiceTracker = null;
		}
		
		if (chatManagerRegistration != null)
		{
			chatManagerRegistration.unregister();
			chatManagerRegistration = null;
		}
		
		mainManager = null;
		chatManager = null;
		bundle = null;
		this.context = null;
	}
	
	private class AllChatMessageFilter implements StanzaFilter
	{

		@Override
		public boolean accept(XMPPConnection connection, XMLStanza stanza)
		{
			if (stanza instanceof Message)
			{
				Message message = (Message) stanza;
				if (message.getType() == Message.Type.chat)
				{
					return true;
				}
			}
			return false;
		}
		
	}

	@Override
	public void processReceStanza(XMPPConnection connection, XMLStanza stanza)
	{
		Message message = (Message) stanza;
		JID from = message.getFrom();
		JID bareJID = new JID(from.getNode(), from.getDomain(), null);
		String resource = from.getResource();
		Chat chat = chatManager.getChat(connection, bareJID);		
		if (chat == null)
		{
			// maybe chat state notification
			if (message.getBody() != null || !message.getBodies().isEmpty() 
					|| message.getSubject() != null || !message.getSubjects().isEmpty())
			{
				chat = chatManager.createChat(connection, bareJID, resource, null);
				ChatImpl chatImpl = (ChatImpl) chat;
				chatImpl.processReceStanza(connection, message);
			}
		}
		

	}

	@Override
	public void serviceChanged(ServiceEvent event)
	{
		int eventType = event.getType();
		Object obj = context.getService(event.getServiceReference());
		if (eventType == ServiceEvent.UNREGISTERING)
		{
			if (obj == mainManager)
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
