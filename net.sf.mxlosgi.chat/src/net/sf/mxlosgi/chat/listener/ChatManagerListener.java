package net.sf.mxlosgi.chat.listener;

import net.sf.mxlosgi.chat.Chat;
import net.sf.mxlosgi.chat.XmppChatManager;


/**
 * @author noah
 * 
 */
public interface ChatManagerListener
{
	/**
	 * 
	 * @param chatManager
	 * @param chat
	 */
	public void chatCreated(XmppChatManager chatManager, Chat chat);
	
	/**
	 * 
	 * @param chatManager
	 * @param chat
	 */
	public void chatClosed(XmppChatManager chatManager, Chat chat);
}