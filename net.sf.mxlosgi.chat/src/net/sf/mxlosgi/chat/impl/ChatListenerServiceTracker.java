package net.sf.mxlosgi.chat.impl;

import net.sf.mxlosgi.chat.Chat;
import net.sf.mxlosgi.chat.listener.ChatListener;
import net.sf.mxlosgi.xmpp.Message;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class ChatListenerServiceTracker extends ServiceTracker
{

	public ChatListenerServiceTracker(BundleContext context)
	{
		super(context, ChatListener.class.getName(), null);
	}


	public void fireResourceChanged(Chat chat, String resource)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ChatListener listener = (ChatListener) obj;
			listener.resourceChanged(chat, resource);
		}
	}
	
	
	public void fireMessageReceived(Chat chat, Message message)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ChatListener listener = (ChatListener) obj;
			listener.processMessage(chat, message);
		}
	}
}
