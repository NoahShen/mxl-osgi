/**
 * 
 */
package net.sf.mxlosgi.mxlosgichatbundle.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.mxlosgi.mxlosgichatbundle.Chat;
import net.sf.mxlosgi.mxlosgichatbundle.listener.ChatListener;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager;
import net.sf.mxlosgi.mxlosgimainbundle.listener.StanzaReceListener;
import net.sf.mxlosgi.mxlosgiutilsbundle.AbstractHasAttribute;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.Message;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class ChatImpl extends AbstractHasAttribute implements Chat, StanzaReceListener
{
	private JID bareJID;

	private String currentChatResource;
	
	private XMPPConnection connection;
	
	private XMPPChatManagerImpl chatManager;

	private XMPPMainManager mainManager;
	
	private final List<ChatListener> chatListeners = new CopyOnWriteArrayList<ChatListener>();

	
	/**
	 * 
	 * @param bareJID
	 * @param connection
	 * @param chatManager
	 */
	public ChatImpl(JID bareJID, 
				XMPPConnection connection, 
				XMPPChatManagerImpl chatManager, 
				XMPPMainManager mainManager)
	{
		this(bareJID, null, connection, chatManager, mainManager);
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
				XMPPConnection connection, 
				XMPPChatManagerImpl chatManager,
				XMPPMainManager mainManager)
	{
		this.bareJID = bareJID;
		this.currentChatResource = currentChatResource;
		this.connection = connection;
		this.chatManager = chatManager;
		this.mainManager = mainManager;
		mainManager.addStanzaReceListener(this, new MessageFilter(connection, bareJID));
	}


	@Override
	public void addChatListener(ChatListener chatListener)
	{
		chatListeners.add(chatListener);
	}

	@Override
	public void close()
	{
		chatManager.removeChat(this);
		mainManager.removeStanzaReceListener(this);
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
		fireResourceChanged(currentChatResource);
	}


	private void fireResourceChanged(String resource)
	{
		for (ChatListener listener : chatListeners)
		{
			listener.resourceChanged(this, resource);
		}
	}

	@Override
	public void removeChatListener(ChatListener chatListener)
	{
		chatListeners.remove(chatListener);
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
	public XMPPConnection getConnection()
	{
		return connection;
	}


	@Override
	public void processReceStanza(XMPPConnection connection, XMLStanza stanza)
	{
		Message message = (Message) stanza;
		JID from = message.getFrom();
		String resource = from.getResource();
		if (resource != null && !resource.isEmpty() && !resource.equals(currentChatResource))
		{
			setCurrentChatResource(resource);
		}
		fireMessageReceived(message);
	}
	
	private void fireMessageReceived(Message message)
	{
		for (ChatListener listener : chatListeners)
		{
			listener.processMessage(this, message);
		}
	}
}
