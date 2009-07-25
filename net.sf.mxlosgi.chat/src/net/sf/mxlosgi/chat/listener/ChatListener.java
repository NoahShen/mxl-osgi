package net.sf.mxlosgi.chat.listener;

import net.sf.mxlosgi.chat.Chat;
import net.sf.mxlosgi.xmpp.Message;


/**
 * @author noah
 *
 */
public interface ChatListener
{
	/**
	 * 
	 * @param message
	 */
	public void processMessage(Chat chat, Message message);
	
	/**
	 * 
	 * @param currentChatResource
	 */
	public void resourceChanged(Chat chat, String currentChatResource);
}