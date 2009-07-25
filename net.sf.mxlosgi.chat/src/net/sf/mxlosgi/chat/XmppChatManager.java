package net.sf.mxlosgi.chat;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.xmpp.JID;


/**
 * @author noah
 * 
 */
public interface XmppChatManager
{
	/**
	 * 
	 * @param connection
	 * @param bareJID
	 * @return
	 */
	public Chat createChat(XmppConnection connection, JID bareJID);

	/**
	 * 
	 * @param connection
	 * @param bareJID
	 * @param chatResource
	 * @return
	 */
	public Chat createChat(XmppConnection connection, JID bareJID, String chatResource);

	/**
	 * 
	 * @param connection
	 * @param bareJID
	 * @return
	 */
	public Chat getChat(XmppConnection connection, JID bareJID);

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
	public boolean containChat(XmppConnection connection, JID bareJID);
	
	/**
	 * 
	 * @param connection
	 * @param bareJID
	 */
	public void removeChat(XmppConnection connection, JID bareJID);
	
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