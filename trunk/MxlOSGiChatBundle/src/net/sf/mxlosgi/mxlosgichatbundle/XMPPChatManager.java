package net.sf.mxlosgi.mxlosgichatbundle;

import net.sf.mxlosgi.mxlosgichatbundle.listener.ChatListener;
import net.sf.mxlosgi.mxlosgichatbundle.listener.ChatManagerListener;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

/**
 * @author noah
 * 
 */
public interface XMPPChatManager
{
	
	/**
	 * 
	 * @param chatManagerListener
	 */
	public void addChatManagerListener(ChatManagerListener chatManagerListener);

	/**
	 * 
	 * @param chatManagerListener
	 */
	public void removeChatManagerListener(ChatManagerListener chatManagerListener);

	/**
	 * 
	 * @param connection
	 * @param bareJID
	 * @param chatListener
	 * @return
	 */
	public Chat createChat(XMPPConnection connection, JID bareJID, ChatListener chatListener);

	/**
	 * 
	 * @param connection
	 * @param bareJID
	 * @param chatResource
	 * @param chatListener
	 * @return
	 */
	public Chat createChat(XMPPConnection connection, JID bareJID, String chatResource, ChatListener chatListener);

	/**
	 * 
	 * @param connection
	 * @param bareJID
	 * @return
	 */
	public Chat getChat(XMPPConnection connection, JID bareJID);

	/**
	 * 
	 * @return
	 */
	public Chat[] getAllChat();

	/**
	 * 
	 * @param chat
	 * @return
	 */
	public boolean containChat(Chat chat);
	
	/**
	 * 
	 * @param connection
	 * @param bareJID
	 * @return
	 */
	public boolean containChat(XMPPConnection connection, JID bareJID);
	
	/**
	 * 
	 * @param connection
	 * @param bareJID
	 */
	public void removeChat(XMPPConnection connection, JID bareJID);
	
	/**
	 * 
	 * @param chat
	 */
	public void removeChat(Chat chat);

	/**
	 * 
	 */
	public void removeAllChat();
}