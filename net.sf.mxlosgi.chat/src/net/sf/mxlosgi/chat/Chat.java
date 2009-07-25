package net.sf.mxlosgi.chat;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.utils.Propertied;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Message;

public interface Chat extends Propertied
{
	
	/**
	 * 
	 * @return
	 */
	public XmppConnection getConnection();
	
	/**
	 * 
	 * @return
	 */
	public JID getBareJID();

	/**
	 * 
	 * @return
	 */
	public String getCurrentChatResource();
	
	/**
	 * 
	 * @param currentChatResource
	 */
	public void setCurrentChatResource(String currentChatResource);
	
	/**
	 * 
	 * @param text
	 */
	public void sendMessage(String text);

	/**
	 * 
	 * @param message
	 */
	public void sendMessage(Message message);

	/**
	 * 
	 */
	public void close();

}