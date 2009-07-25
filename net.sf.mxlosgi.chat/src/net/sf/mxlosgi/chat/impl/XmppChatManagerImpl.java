/**
 * 
 */
package net.sf.mxlosgi.chat.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.mxlosgi.chat.Chat;
import net.sf.mxlosgi.chat.XmppChatManager;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Message;
import net.sf.mxlosgi.xmpp.XmlStanza;

/**
 * @author noah
 *
 */
public class XmppChatManagerImpl implements XmppChatManager, StanzaReceListener
{
	
	private final List<Chat> chats = new CopyOnWriteArrayList<Chat>();
	
	private ChatListenerServiceTracker chatListenerServiceTracker;
	
	private ChatManagerListenerServiceTracker chatManagerListenerServiceTracker;
	
	/**
	 * @param connection
	 */
	public XmppChatManagerImpl(ChatListenerServiceTracker chatListenerServiceTracker,
							ChatManagerListenerServiceTracker chatManagerListenerServiceTracker)
	{
		this.chatListenerServiceTracker = chatListenerServiceTracker;
		this.chatManagerListenerServiceTracker = chatManagerListenerServiceTracker;
	}

	@Override
	public synchronized Chat createChat(XmppConnection connection, JID bareJID)
	{
		return createChat(connection, bareJID, null);
	}

	@Override
	public synchronized Chat createChat(XmppConnection connection, 
								JID bareJID, 
								String chatResource)
	{
		Chat chat = getChat(connection, bareJID);
		if (chat != null)
		{
			return chat;
		}
		
		ChatImpl chatImpl = new ChatImpl(bareJID, chatResource, connection, this, chatListenerServiceTracker);
		
		chats.add(chatImpl);
		
		chatManagerListenerServiceTracker.fireChatCreated(this, chatImpl);
		return chatImpl;
	}
	

	@Override
	public Chat[] getAllChat()
	{
		return chats.toArray(new Chat[]{});
	}

	@Override
	public synchronized Chat getChat(XmppConnection connection, JID bareJID)
	{
		for (Chat chat : chats)
		{
			XmppConnection conn = chat.getConnection();
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
	public boolean containChat(XmppConnection connection, JID bareJID)
	{
		return getChat(connection, bareJID) != null;
	}

	@Override
	public synchronized void removeAllChat()
	{
		for (Chat chat : getAllChat())
		{
			removeChat(chat);
		}
	}

	@Override
	public synchronized void removeChat(XmppConnection connection, JID bareJID)
	{
		for (Chat chat : chats)
		{
			XmppConnection conn = chat.getConnection();
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
		chatManagerListenerServiceTracker.fireChatClosed(this, chat);
	}
	

	@Override
	public void processReceStanza(XmppConnection connection, XmlStanza stanza)
	{
		Message message = (Message) stanza;
		JID from = message.getFrom();
		JID bareJID = new JID(from.getNode(), from.getDomain(), null);
		String resource = from.getResource();
		Chat chat = getChat(connection, bareJID);		
		if (chat == null)
		{
			//maybe chat state notification
//			if (message.getBody() != null || !message.getBodies().isEmpty() 
//					|| message.getSubject() != null || !message.getSubjects().isEmpty())
//			{
//				chat = createChat(connection, bareJID, resource);
//			}
			chat = createChat(connection, bareJID, resource);
		}
		
		ChatImpl chatImpl = (ChatImpl) chat;
		chatImpl.messageReceived(connection, message);
	}


}
