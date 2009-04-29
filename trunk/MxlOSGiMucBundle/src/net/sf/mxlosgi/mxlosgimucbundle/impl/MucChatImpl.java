/**
 * 
 */
package net.sf.mxlosgi.mxlosgimucbundle.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager;
import net.sf.mxlosgi.mxlosgimainbundle.listener.StanzaReceListener;
import net.sf.mxlosgi.mxlosgimucbundle.MucChat;
import net.sf.mxlosgi.mxlosgimucbundle.MucInitialPresenceExtension;
import net.sf.mxlosgi.mxlosgimucbundle.MucRoomUser;
import net.sf.mxlosgi.mxlosgimucbundle.MucUserExtension;
import net.sf.mxlosgi.mxlosgimucbundle.MucInitialPresenceExtension.History;
import net.sf.mxlosgi.mxlosgimucbundle.listener.MucChatListener;
import net.sf.mxlosgi.mxlosgiutilsbundle.AbstractHasAttribute;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.Message;
import net.sf.mxlosgi.mxlosgixmppbundle.Packet;
import net.sf.mxlosgi.mxlosgixmppbundle.Presence;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class MucChatImpl extends AbstractHasAttribute implements MucChat, StanzaReceListener
{
	
	private XMPPConnection connection;
	
	private MucManagerImpl mucManager;
	
	private JID roomJID;
	
	private String nickname;
	
	private final Map<String, MucRoomUser> mucRoomUsers = new ConcurrentHashMap<String, MucRoomUser>();
	
	private final List<MucChatListener> chatListeners = new CopyOnWriteArrayList<MucChatListener>();

	private XMPPMainManager mainManager;

	

	/**
	 * @param mainManager
	 * @param connection
	 * @param mucManager
	 * @param roomJID
	 */
	public MucChatImpl(XMPPMainManager mainManager, XMPPConnection connection, MucManagerImpl mucManager, JID roomJID)
	{
		this.mainManager = mainManager;
		this.connection = connection;
		this.mucManager = mucManager;
		this.roomJID = roomJID;
		mainManager.addStanzaReceListener(this, new MucFilter(connection, roomJID));
	}

	@Override
	public void addMucChatListener(MucChatListener listener)
	{
		chatListeners.add(listener);
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
		mainManager.removeStanzaReceListener(this);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimuc.MucChat#getRoomJID()
	 */
	@Override
	public JID getRoomJID()
	{
		return roomJID;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimuc.MucChat#getRoomUsers()
	 */
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

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimuc.MucChat#removeMucChatListener(net.sf.mxlosgi.mxlosgimuc.listener.MucChatListener)
	 */
	@Override
	public void removeMucChatListener(MucChatListener listener)
	{
		chatListeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimuc.MucChat#sendMessage(java.lang.String)
	 */
	@Override
	public void sendMessage(String text)
	{
		Message message = new Message(roomJID, Message.Type.groupchat);
		message.setBody(text);
		sendMessage(message);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimuc.MucChat#sendMessage(net.sf.mxlosgi.mxlosgixmppbundle.Message)
	 */
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
	public XMPPConnection getConnection()
	{
		return connection;
	}

	@Override
	public void processReceStanza(XMPPConnection connection, XMLStanza stanza)
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
			fireSubjectUpdated(subject, from);
		}
		
		fireMessageReceived(message);
	}

	private void fireSubjectUpdated(String subject, JID from)
	{
		for (MucChatListener listener : chatListeners)
		{
			listener.subjectUpdated(subject, from);
		}
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
						fireUserNicknameChanged(resource, item.getNick());
					}
					else
					{
						fireUserUnavailable(user);
					}
				}
				else
				{
					fireUserUnavailable(user);
				}
				
			}
		}
		
	}

	private void fireUserNicknameChanged(String oldNickName, String newNickname)
	{
		for (MucChatListener listener : chatListeners)
		{
			listener.userNicknameChanged(oldNickName, newNickname);
		}
	}

	private void fireUserUnavailable(MucRoomUser user)
	{
		for (MucChatListener listener : chatListeners)
		{
			listener.userUnavaliable(user);
		}
	}

	private void handleError(Packet packet)
	{
		for (MucChatListener listener : chatListeners)
		{
			listener.error(packet);
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
			fireOwnerStatusChanged(user, statusCodes);
			return;
		}
		fireUserStatusChanged(user);
	}

	private void fireOwnerStatusChanged(MucRoomUser user, Set<String> statusCodes)
	{
		for (MucChatListener listener : chatListeners)
		{
			listener.ownerStatusChanged(user, statusCodes);
		}
	}

	private void fireUserStatusChanged(MucRoomUser user)
	{
		for (MucChatListener listener : chatListeners)
		{
			listener.userStatusChanged(user);
		}
	}

	private void fireMessageReceived(Message message)
	{
		for (MucChatListener listener : chatListeners)
		{
			listener.processMessage(message);
		}
	}


}
