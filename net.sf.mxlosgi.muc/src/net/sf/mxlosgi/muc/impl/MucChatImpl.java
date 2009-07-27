/**
 * 
 */
package net.sf.mxlosgi.muc.impl;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.muc.MucChat;
import net.sf.mxlosgi.muc.MucInitialPresenceExtension;
import net.sf.mxlosgi.muc.MucRoomUser;
import net.sf.mxlosgi.muc.MucUserExtension;
import net.sf.mxlosgi.muc.MucInitialPresenceExtension.History;
import net.sf.mxlosgi.utils.AbstractPropertied;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Message;
import net.sf.mxlosgi.xmpp.Packet;
import net.sf.mxlosgi.xmpp.Presence;
import net.sf.mxlosgi.xmpp.XmlStanza;

/**
 * @author noah
 *
 */
public class MucChatImpl extends AbstractPropertied implements MucChat
{
	
	private XmppConnection connection;
	
	private MucManagerImpl mucManager;
	
	private JID roomJID;
	
	private String nickname;
	
	private final Map<String, MucRoomUser> mucRoomUsers = new ConcurrentHashMap<String, MucRoomUser>();

	private MucChatListenerServiceTracker mucChatListenerServiceTracker;
	
	/**
	 * 
	 * @param connection
	 * @param mucManager
	 * @param roomJID
	 */
	public MucChatImpl(XmppConnection connection, MucManagerImpl mucManager, JID roomJID,
						MucChatListenerServiceTracker mucChatListenerServiceTracker)
	{
		this.connection = connection;
		this.mucManager = mucManager;
		this.roomJID = roomJID;
		this.mucChatListenerServiceTracker = mucChatListenerServiceTracker;
	}
	
	@Override
	public void close()
	{
		close(null);
	}

	@Override
	public void close(String status)
	{
		Presence presence = new Presence(Presence.Type.unavailable);
		presence.setTo(new JID(roomJID.getNode(), roomJID.getDomain(), nickname));
		if (status != null && !status.isEmpty())
		{
			presence.setStatus(status);
		}
		connection.sendStanza(presence);
		mucManager.removeMucChat(this);
	}
	
	@Override
	public JID getRoomJID()
	{
		return roomJID;
	}

	@Override
	public MucRoomUser[] getRoomUsers()
	{
		return mucRoomUsers.values().toArray(new MucRoomUser[]{});
	}

	@Override
	public MucRoomUser getRoomUser(String nickName)
	{
		return mucRoomUsers.get(nickName);
	}

	@Override
	public void sendMessage(String text)
	{
		Message message = new Message(roomJID, Message.Type.groupchat);
		message.setBody(text);
		sendMessage(message);
	}

	@Override
	public void sendMessage(Message message)
	{
		JID to = message.getTo();
		if (to == null)
		{
			message.setTo(roomJID);
		}
		connection.sendStanza(message);
	}

	@Override
	public void enterRoom(String password)
	{
		enterRoom(connection.getJid().getNode(), password);
	}

	@Override
	public void enterRoom()
	{
		enterRoom("");
	}

	@Override
	public void enterRoom(String nickname, String password)
	{
		enterRoom(nickname, password, null);
	}

	@Override
	public void enterRoom(History history)
	{
		enterRoom(connection.getJid().getNode(), history);
	}

	@Override
	public void enterRoom(String nickname, History history)
	{
		enterRoom(nickname, null, history);
	}

	@Override
	public void enterRoom(String nickname, String password, History history)
	{
		this.nickname = nickname;
		Presence presence = new Presence(Presence.Type.available);
		presence.setTo(new JID(roomJID.getNode(), roomJID.getDomain(), nickname));
		
		MucInitialPresenceExtension initialPresence = new MucInitialPresenceExtension();
		if (password != null && !password.isEmpty())
		{
			initialPresence.setPassword(password);
		}
		if (history != null)
		{
			initialPresence.setHistory(history);
		}
		presence.addExtension(initialPresence);
		connection.sendStanza(presence);
	}
	

//	@Override
//	public void changeNickname(String newNickname)
//	{
//		Presence presence = new Presence(Presence.Type.available);
//		presence.setTo(roomJID + "/" + newNickname);
//		connection.sendStanza(presence);
//	}
	
	@Override
	public void changeStatus(Presence presence)
	{
		JID to = presence.getTo();
		if (to == null)
		{
			presence.setTo(new JID(roomJID.getNode(), roomJID.getDomain(), nickname));
		}
		connection.sendStanza(presence);
	}
	
	@Override
	public void inviteUser(JID jid)
	{
		inviteUser(jid, null);
	}

	@Override
	public void inviteUser(JID jid, String reason)
	{
		Message message = new Message();
		message.setTo(roomJID);
		MucUserExtension userExtension = new MucUserExtension();
		MucUserExtension.Invite invite = new MucUserExtension.Invite();
		invite.setTo(jid);
		if (reason != null && !reason.isEmpty())
		{
			invite.setReason(reason);
		}
		userExtension.setInvite(invite);
		message.addExtension(userExtension);
		connection.sendStanza(message);
	}
	

	@Override
	public void changeSubject(String subject)
	{
		Message message = new Message(roomJID, Message.Type.groupchat);
		message.setSubject(subject);
		connection.sendStanza(message);
	}

//	@Override
//	public boolean banUser(String jid)  throws XMPPException
//	{
//		return banUser(jid, null);
//	}
//	
//	@Override
//	public boolean banUser(String jid, String reason)  throws XMPPException
//	{
//		IQ iq = new IQ(IQ.Type.set);
//		iq.setTo(roomJID);
//		
//		MucAdminExtension adminExtension = new MucAdminExtension();
//		
//		MucAdminExtension.Item item = new MucAdminExtension.Item("outcast");
//		item.setJid(jid);
//		if (reason != null && !reason.isEmpty())
//		{
//			item.setReason(reason);
//		}
//		
//		adminExtension.addItem(item);
//		
//		iq.addExtension(adminExtension);
//		
//		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
//		connection.sendStanza(iq);
//		XMLData data = collector.nextResult(connection.getConnectionConfig().getConnectionTimeout());
//		collector.cancel();
//		
//		if (data == null)
//		{
//			throw new ServerTimeoutException("remote server no response");
//		}
//		if (data instanceof IQ)
//		{
//			IQ iqResponse = (IQ) data;
//			IQ.Type type = iqResponse.getType();
//			if (type == IQ.Type.result)
//			{
//				return true;
//			}
//			if (type == IQ.Type.error)
//			{
//				throw new XMPPException(iqResponse.getError());
//			}
//		}
//		
//		return false;
//	}

	@Override
	public XmppConnection getConnection()
	{
		return connection;
	}

	
	public void processReceStanza(XmppConnection connection, XmlStanza stanza)
	{
		Packet packet = (Packet) stanza;
		if (packet.getError() != null)
		{
			handleError(packet);
		}
		else if (packet instanceof Presence)
		{
			Presence presence = (Presence) packet;
			handlePresence(presence);
		}
		else if (packet instanceof Message)
		{
			Message message = (Message) packet;
			handleMessage(message);
		}
	}

	private void handleMessage(Message message)
	{
		JID from = message.getFrom();
		
		String body = message.getBody();
		String subject = message.getSubject();
		if ((body == null || body.isEmpty())
				&& message.getBodies().isEmpty()
				&& subject != null)
		{
			mucChatListenerServiceTracker.fireSubjectUpdated(this, subject, from);
		}
		
		mucChatListenerServiceTracker.fireMessageReceived(this, message);
	}


	private void handlePresence(Presence presence)
	{
		Presence.Type type = presence.getType();
		if (type == Presence.Type.available)
		{
			handleAvailable(presence);
		}
		else if (type == Presence.Type.unavailable)
		{
			handleUserUnavailable(presence);
		}
		else if (type == Presence.Type.error)
		{
			handleError(presence);
		}
		
	}

	private void handleError(Packet packet)
	{
		mucChatListenerServiceTracker.fireError(this, packet);
	}
	
	private void handleUserUnavailable(Presence presence)
	{
		JID from = presence.getFrom();
		String resource = from.getResource();
		if (resource != null || !resource.isEmpty())
		{
			MucRoomUser user = mucRoomUsers.remove(resource);
			if (user != null)
			{
				user.setPresence(presence);
				MucUserExtension extension = 
					(MucUserExtension) presence.getExtension(MucUserExtension.ELEMENTNAME, 
													MucUserExtension.NAMESPACE);
				if (extension != null)
				{
					MucUserExtension.Item item = extension.getItem();
					if (item != null)
					{
						String affiliationStr = item.getAffiliation();
						user.setAffiliation(MucRoomUser.Affiliation.fromString(affiliationStr));
						
						String roleStr = item.getRole();
						user.setRole(MucRoomUser.Role.fromString(roleStr));
						
						JID jid = item.getJid();
						if (jid != null)
						{
							user.setJid(jid);
						}
					}
					
					boolean itemContainsNick = false;
					if (item != null && item.getNick() != null)
					{
						itemContainsNick = true;
					}
					boolean containNicknameCode = extension.getStatusCodes().contains(NICKNAMECHANGED_CODE);
					
					if (itemContainsNick || containNicknameCode)
					{
						mucChatListenerServiceTracker.fireUserNicknameChanged(this, resource, item.getNick());
					}
					else
					{
						mucChatListenerServiceTracker.fireUserUnavailable(this, user);
					}
				}
				else
				{
					mucChatListenerServiceTracker.fireUserUnavailable(this, user);
				}
				
			}
		}
		
	}


	private void handleAvailable(Presence presence)
	{
		JID from = presence.getFrom();
		String resource = from.getResource();
		
		if (resource == null || resource.isEmpty())
		{
			return;
		}
		

		MucRoomUser user = mucRoomUsers.get(resource);
		if (user == null)
		{
			user = new MucRoomUser(resource);
			mucRoomUsers.put(resource, user);
		}
		
		user.setPresence(presence);
		
		MucUserExtension extension = 
			(MucUserExtension) presence.getExtension(MucUserExtension.ELEMENTNAME, 
											MucUserExtension.NAMESPACE);
		MucUserExtension.Item item = extension.getItem();
		if (item != null)
		{
			String affiliationStr = item.getAffiliation();
			user.setAffiliation(MucRoomUser.Affiliation.fromString(affiliationStr));
			
			String roleStr = item.getRole();
			user.setRole(MucRoomUser.Role.fromString(roleStr));
			
			JID jid = item.getJid();
			if (jid != null)
			{
				user.setJid(jid);
			}
		}
		
		Set<String> statusCodes = extension.getStatusCodes();
		if (statusCodes.contains(ABOUTOWNER_CODE) || resource.equals(nickname))
		{
			mucChatListenerServiceTracker.fireOwnerStatusChanged(this, user, statusCodes);
			return;
		}
		mucChatListenerServiceTracker.fireUserStatusChanged(this, user);
	}


}
