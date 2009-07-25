/**
 * 
 */
package net.sf.mxlosgi.chat.impl;

import net.sf.mxlosgi.chat.Chat;
import net.sf.mxlosgi.chat.XmppChatManager;
import net.sf.mxlosgi.chat.listener.ChatManagerListener;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class ChatManagerListenerServiceTracker extends ServiceTracker
{

	public ChatManagerListenerServiceTracker(BundleContext context)
	{
		super(context, ChatManagerListener.class.getName(), null);
	}


	public void fireChatCreated(XmppChatManager chatManager, Chat chat)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ChatManagerListener listener = (ChatManagerListener) obj;
			listener.chatCreated(chatManager, chat);
		}
	}
	
	public void fireChatClosed(XmppChatManager chatManager, Chat chat)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ChatManagerListener listener = (ChatManagerListener) obj;
			listener.chatClosed(chatManager, chat);
		}
	}
}
