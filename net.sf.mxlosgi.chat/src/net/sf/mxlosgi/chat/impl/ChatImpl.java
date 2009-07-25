/**
 * 
 */
package net.sf.mxlosgi.chat.impl;


import net.sf.mxlosgi.chat.Chat;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.utils.AbstractPropertied;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Message;
import net.sf.mxlosgi.xmpp.XmlStanza;

/**
 * @author noah
 *
 */
public class ChatImpl extends AbstractPropertied implements Chat
{
	private JID bareJID;

	private String currentChatResource;
	
	private XmppConnection connection;
	
	private XmppChatManagerImpl chatManager;

	private ChatListenerServiceTracker chatListenerServiceTracker;
	/**
	 * 
	 * @param bareJID
	 * @param connection
	 * @param chatManager
	 */
	public ChatImpl(JID bareJID, 
				XmppConnection connection, 
				XmppChatManagerImpl chatManager,
				ChatListenerServiceTracker chatListenerServiceTracker)
	{
		this(bareJID, null, connection, chatManager, chatListenerServiceTracker);
	}

	
	/**
	 * 
	 * @param bareJID
	 * @param currentChatResource
	 * @param connection
	 * @param chatManager
	 */
	public ChatImpl(JID bareJID, 
				String currentChatResource, 
				XmppConnection connection, 
				XmppChatManagerImpl chatManager,
				ChatListenerServiceTracker chatListenerServiceTracker)
	{
		this.bareJID = bareJID;
		this.currentChatResource = currentChatResource;
		this.connection = connection;
		this.chatManager = chatManager;
		this.chatListenerServiceTracker = chatListenerServiceTracker;
	}

	@Override
	public void close()
	{
		chatManager.removeChat(this);
	}

	@Override
	public JID getBareJID()
	{
		return bareJID;
	}
	

	@Override
	public String getCurrentChatResource()
	{
		return currentChatResource;
	}
	

	@Override
	public void setCurrentChatResource(String currentChatResource)
	{
		this.currentChatResource = currentChatResource;
		chatListenerServiceTracker.fireResourceChanged(this, currentChatResource);
	}



	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgichatbundle.Chat#sendMessage(java.lang.String)
	 */
	@Override
	public void sendMessage(String text)
	{
		Message message = new Message(getMessageTo(), Message.Type.chat);
		message.setBody(text);
		sendMessage(message);
	}

	private JID getMessageTo()
	{
		if (currentChatResource != null)
		{
			return new JID(bareJID.getNode(), bareJID.getDomain(), currentChatResource);
		}
		else
		{
			return bareJID;
		}
		
	}


	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgichatbundle.Chat#sendMessage(net.sf.mxlosgi.mxlosgixmppbundle.Message)
	 */
	@Override
	public void sendMessage(Message message)
	{
		if (message.getTo() == null)
		{
			message.setTo(getMessageTo());
		}
		
		connection.sendStanza(message);
	}



	@Override
	public XmppConnection getConnection()
	{
		return connection;
	}


	void messageReceived(XmppConnection connection, XmlStanza stanza)
	{
		Message message = (Message) stanza;
		JID from = message.getFrom();
		String resource = from.getResource();
		if (resource != null && !resource.isEmpty() && !resource.equals(currentChatResource))
		{
			setCurrentChatResource(resource);
		}
		
		chatListenerServiceTracker.fireMessageReceived(this, message);
	}

}
