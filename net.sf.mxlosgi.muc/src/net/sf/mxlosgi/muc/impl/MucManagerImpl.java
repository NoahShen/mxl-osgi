/**
 * 
 */
package net.sf.mxlosgi.muc.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.disco.DiscoInfoManager;
import net.sf.mxlosgi.disco.DiscoInfoPacketExtension;
import net.sf.mxlosgi.disco.DiscoItemsManager;
import net.sf.mxlosgi.disco.DiscoItemsPacketExtension;
import net.sf.mxlosgi.muc.MucChat;
import net.sf.mxlosgi.muc.MucManager;
import net.sf.mxlosgi.muc.MucUserExtension;
import net.sf.mxlosgi.muc.RoomInfo;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Message;
import net.sf.mxlosgi.xmpp.Packet;
import net.sf.mxlosgi.xmpp.XmlStanza;


/**
 * @author noah
 *
 */
public class MucManagerImpl implements MucManager, StanzaReceListener
{
	private DiscoInfoManagerServiceTracker discoInfoManagerServiceTracker;
	
	private DiscoItemsManagerServiceTracker discoItemsManagerServiceTracker;
	
	private MucListenerServiceTracker mucListenerServiceTracker;
	
	private MucChatListenerServiceTracker mucChatListenerServiceTracker;
	
	private final List<MucChatImpl> mucChats = new CopyOnWriteArrayList<MucChatImpl>();
	
	/**
	 * 
	 * @param discoInfoManagerServiceTracker
	 * @param discoItemsManagerServiceTracker
	 * @param mucListenerServiceTracker
	 * @param mucChatListenerServiceTracker
	 */
	public MucManagerImpl(DiscoInfoManagerServiceTracker discoInfoManagerServiceTracker,
						DiscoItemsManagerServiceTracker discoItemsManagerServiceTracker,
						MucListenerServiceTracker mucListenerServiceTracker,
						MucChatListenerServiceTracker mucChatListenerServiceTracker)
	{
		this.discoInfoManagerServiceTracker = discoInfoManagerServiceTracker;
		this.discoItemsManagerServiceTracker = discoItemsManagerServiceTracker;
		this.mucListenerServiceTracker = mucListenerServiceTracker;
		this.mucChatListenerServiceTracker = mucChatListenerServiceTracker;
	}


	@Override
	public DiscoItemsPacketExtension.Item[] getUsers(XmppConnection connection, JID jid) throws XmppException
	{
		DiscoItemsManager discoItemsManager = discoItemsManagerServiceTracker.getDiscoItemsManager();
		DiscoItemsPacketExtension items  = discoItemsManager.getDiscoItems(connection, jid);
		return items.getItems();
	}


	@Override
	public RoomInfo getRoomInfo(XmppConnection connection, JID jid) throws XmppException
	{
		DiscoInfoManager discoInfoManager = discoInfoManagerServiceTracker.getDiscoInfoManager();
		DiscoInfoPacketExtension info = discoInfoManager.getDiscoInfo(connection, jid);
		RoomInfo roomInfo = new RoomInfo(jid);
		roomInfo.setPasswordProtected(info.containFeature("muc_passwordprotected"));
		roomInfo.setMembersOnly(info.containFeature("muc_membersonly"));
		roomInfo.setModerated(info.containFeature("muc_moderated"));
		
		if (info.containFeature("muc_nonanonymous"))
		{
			roomInfo.setAnonymousType(RoomInfo.AnonymousType.non_Anonymous);
		}
		else if (info.containFeature("muc_semianonymous"))
		{
			roomInfo.setAnonymousType(RoomInfo.AnonymousType.semi_Anonymous);
		}
		else if (info.containFeature("muc_fullyanonymous"))
		{
			roomInfo.setAnonymousType(RoomInfo.AnonymousType.fully_Anonymous);
		}
		
		roomInfo.setPasswordProtected(info.containFeature("muc_passwordprotected"));
		roomInfo.setPersistent(info.containFeature("muc_persistent"));
		
		return roomInfo;
	}

	@Override
	public DiscoItemsPacketExtension.Item[] getRoomList(XmppConnection connection, JID jid) throws XmppException
	{
		DiscoItemsManager discoItemsManager = discoItemsManagerServiceTracker.getDiscoItemsManager();
		DiscoItemsPacketExtension items  = discoItemsManager.getDiscoItems(connection, jid);
		return items.getItems();
	}


	@Override
	public boolean isServerSupportMuc(XmppConnection connection, JID jid) throws XmppException
	{
		DiscoInfoManager discoInfoManager = discoInfoManagerServiceTracker.getDiscoInfoManager();
		DiscoInfoPacketExtension info = discoInfoManager.getDiscoInfo(connection, jid);
		if (info.containFeature("http://jabber.org/protocol/muc"))
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean isUserSupportMuc(XmppConnection connection, JID jid) throws XmppException
	{
		DiscoInfoManager discoInfoManager = discoInfoManagerServiceTracker.getDiscoInfoManager();
		DiscoInfoPacketExtension info = discoInfoManager.getDiscoInfo(connection, jid);
		if (info.containFeature("http://jabber.org/protocol/muc"))
		{
			return true;
		}
		return false;
	}

	@Override
	public DiscoItemsPacketExtension.Item[] getContactCurrentRooms(XmppConnection connection, JID jid) throws XmppException
	{
		DiscoItemsManager discoItemsManager = discoItemsManagerServiceTracker.getDiscoItemsManager();
		DiscoItemsPacketExtension items  = discoItemsManager.getDiscoItems(connection, jid, "http://jabber.org/protocol/muc#rooms");
		return items.getItems();
	}

	@Override
	public MucChat createMucChat(XmppConnection connection, JID roomJID)
	{
		MucChatImpl mucChat = new MucChatImpl(connection, this, roomJID, mucChatListenerServiceTracker);		
		mucChats.add(mucChat);
		
		return mucChat;
	}
	

	@Override
	public MucChat getMucChat(XmppConnection connection, JID roomJID)
	{
		for (MucChat chat : mucChats)
		{
			if (chat.getConnection() == connection
					&& chat.getRoomJID().equalsWithBareJid(roomJID))
			{
				return chat;
			}
		}
		return null;
	}
	
	void removeMucChat(MucChat mucChat)
	{
		mucChats.remove(mucChat);
	}

	@Override
	public void processReceStanza(XmppConnection connection, XmlStanza stanza)
	{
		if (!(stanza instanceof Packet))
		{
			return;
		}
		
		Packet packet = (Packet) stanza;
		JID fromJID = packet.getFrom();
		JID roomJID = new JID(fromJID.getNode(), fromJID.getDomain(), null);
		MucChat mucChat = getMucChat(connection, roomJID);
		
		if (mucChat != null)
		{
			((MucChatImpl)mucChat).processReceStanza(connection, packet);
			return;
		}
		
		if (stanza instanceof Message)
		{
			Message message = (Message) stanza;
			MucUserExtension userExtension
				= (MucUserExtension) message.getExtension(MucUserExtension.ELEMENTNAME, 
												MucUserExtension.NAMESPACE);
			if (userExtension != null)
			{
				MucUserExtension.Invite invite = userExtension.getInvite();
				if (invite != null)
				{
					mucListenerServiceTracker.fireInvitationReceived(connection, message);
					return;
				}
				
				MucUserExtension.Decline decline = userExtension.getDecline();
				if (decline != null)
				{
					mucListenerServiceTracker.fireDeclineReceived(connection, message);
					return;
				}
			}
		}
		
	}



}
