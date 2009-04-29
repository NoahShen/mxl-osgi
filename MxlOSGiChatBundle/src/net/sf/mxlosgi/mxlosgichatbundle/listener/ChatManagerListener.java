package net.sf.mxlosgi.mxlosgichatbundle.listener;

import net.sf.mxlosgi.mxlosgichatbundle.Chat;
import net.sf.mxlosgi.mxlosgichatbundle.XMPPChatManager;

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
	public void chatCreated(XMPPChatManager chatManager, Chat chat);
	
	/**
	 * 
	 * @param chatManager
	 * @param chat
	 */
	public void chatClosed(XMPPChatManager chatManager, Chat chat);
}