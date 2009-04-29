package net.sf.mxlosgi.mxlosgichatbundle;

import net.sf.mxlosgi.mxlosgichatbundle.listener.ChatListener;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgiutilsbundle.IHasAttribute;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.Message;

public interface Chat extends IHasAttribute
{
	
	/**
	 * 
	 * @return
	 */
	public XMPPConnection getConnection();
	
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
	 * @param chatListener
	 */
	public void addChatListener(ChatListener chatListener);

	/**
	 * 
	 * @param chatListener
	 */
	public void removeChatListener(ChatListener chatListener);

	/**
	 * 
	 */
	public void close();

}