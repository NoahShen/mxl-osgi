/**
 * 
 */
package net.sf.mxlosgi.mxlosgichatbundle.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.mxlosgi.mxlosgichatbundle.Chat;
import net.sf.mxlosgi.mxlosgichatbundle.XMPPChatManager;
import net.sf.mxlosgi.mxlosgichatbundle.listener.ChatListener;
import net.sf.mxlosgi.mxlosgichatbundle.listener.ChatManagerListener;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

/**
 * @author noah
 *
 */
public class XMPPChatManagerImpl implements XMPPChatManager
{

	private XMPPMainManager mainManager;
	
	private final List<Chat> chats = new CopyOnWriteArrayList<Chat>();
	
	private final List<ChatManagerListener> chatManagerListeners = new CopyOnWriteArrayList<ChatManagerListener>();
	
	
	/**
	 * @param connection
	 */
	public XMPPChatManagerImpl(XMPPMainManager mainManager)
	{
		this.mainManager = mainManager;
	}

	@Override
	public void addChatManagerListener(ChatManagerListener chatManagerListener)
	{
		chatManagerListeners.add(chatManagerListener);
	}

	@Override
	public synchronized Chat createChat(XMPPConnection connection, JID bareJID, ChatListener chatListener)
	{
		return createChat(connection, bareJID, null, chatListener);
	}

	@Override
	public synchronized Chat createChat(XMPPConnection connection, 
								JID bareJID, 
								String chatResource, 
								ChatListener chatListener)
	{
		Chat chat = getChat(connection, bareJID);
		if (chat != null)
		{
			return chat;
		}
		
		ChatImpl chatImpl = new ChatImpl(bareJID, chatResource, connection, this, mainManager);
		
		chats.add(chatImpl);
		if (chatListener != null)
		{
			chatImpl.addChatListener(chatListener);
		}
		
		fireChatCreated(chatImpl);
		return chatImpl;
	}
	

	@Override
	public Chat[] getAllChat()
	{
		return chats.toArray(new Chat[]{});
	}

	@Override
	public synchronized Chat getChat(XMPPConnection connection, JID bareJID)
	{
		for (Chat chat : chats)
		{
			XMPPConnection conn = chat.getConnection();
			if (conn == connection && bareJID.equalsWithBareJid(chat.getBareJID()))
			{
				return chat;
			}
		}
		return null;
	}

	@Override
	public boolean containChat(Chat chat)
	{
		return chats.contains(chat);
	}
	
	@Override
	public boolean containChat(XMPPConnection connection, JID bareJID)
	{
		return getChat(connection, bareJID) != null;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgichatbundle.XMPPChatManager#removeAllChat()
	 */
	@Override
	public synchronized void removeAllChat()
	{
		for (Chat chat : getAllChat())
		{
			removeChat(chat);
		}
	}

	@Override
	public synchronized void removeChat(XMPPConnection connection, JID bareJID)
	{
		for (Chat chat : chats)
		{
			XMPPConnection conn = chat.getConnection();
			if (conn == connection && bareJID.equals(chat.getBareJID()))
			{
				removeChat(chat);
			}
		}
	}
	
	@Override
	public void removeChat(Chat chat)
	{
		chats.remove(chat);
		fireChatClosed(chat);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgichatbundle.XMPPChatManager#removeChatManagerListener(net.sf.mxlosgi.mxlosgichatbundle.listener.ChatManagerListener)
	 */
	@Override
	public void removeChatManagerListener(ChatManagerListener chatManagerListener)
	{
		chatManagerListeners.remove(chatManagerListener);
	}
//
//	@Override
//	public void processReceStanza(XMLStanza stanza)
//	{
//		Message message = (Message) stanza;
//		JID from = message.getFrom();
//		JID bareJID = new JID(from.getNode(), from.getDomain(), null);
//		String resource = from.getResource();
//		Chat chat = getChat(bareJID);		
//		if (chat == null)
//		{
//			if (message.getBody() != null || !message.getBodies().isEmpty() 
//					|| message.getSubject() != null || !message.getSubjects().isEmpty())
//			{
//				chat = createChat(bareJID, resource, null);
//			}
//			else
//			{
//				// maybe chat state notification
//				return;
//			}
//		}
//		
//		ChatImpl chatImpl = (ChatImpl) chat;
//		chatImpl.fireMessageReceived(message);
//	}

	private void fireChatCreated(Chat chat)
	{
		for (ChatManagerListener listener : chatManagerListeners)
		{
			listener.chatCreated(this, chat);
		}
	}
	
	private void fireChatClosed(Chat chat)
	{
		for (ChatManagerListener listener : chatManagerListeners)
		{
			listener.chatClosed( this, chat);
		}
	}

}
