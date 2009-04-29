/**
 * 
 */
package net.sf.mxlosgi.mxlosgimucbundle.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoManager;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoPacketExtension;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoItemsManager;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoItemsPacketExtension;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPMainManager;
import net.sf.mxlosgi.mxlosgimainbundle.listener.StanzaReceListener;
import net.sf.mxlosgi.mxlosgimucbundle.MucChat;
import net.sf.mxlosgi.mxlosgimucbundle.MucManager;
import net.sf.mxlosgi.mxlosgimucbundle.MucUserExtension;
import net.sf.mxlosgi.mxlosgimucbundle.RoomInfo;
import net.sf.mxlosgi.mxlosgimucbundle.listener.MucListener;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.Message;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class MucManagerImpl implements MucManager, StanzaReceListener
{

	private XMPPMainManager mainManager;
	
	private DiscoInfoManager discoInfoManager;
	
	private DiscoItemsManager discoItemsManager;
	
	private final List<MucChatImpl> mucChats = new CopyOnWriteArrayList<MucChatImpl>();
	
	private final List<MucListener> mucListeners = new CopyOnWriteArrayList<MucListener>();
	
	/**
	 * @param connection
	 * @param discoInfoManager
	 * @param discoItemsManager
	 */
	public MucManagerImpl(XMPPMainManager mainManager, DiscoInfoManager discoInfoManager, DiscoItemsManager discoItemsManager)
	{
		this.mainManager = mainManager;
		this.discoInfoManager = discoInfoManager;
		this.discoItemsManager = discoItemsManager;
	}


	@Override
	public void addMucListener(MucListener listener)
	{
		mucListeners.add(listener);
	}

	@Override
	public void removeMucListener(MucListener listener)
	{
		mucListeners.remove(listener);
	}

	@Override
	public DiscoItemsPacketExtension.Item[] getUsers(XMPPConnection connection, JID jid) throws XMPPException
	{
		DiscoItemsPacketExtension items  = discoItemsManager.getDiscoItems(connection, jid);
		return items.getItems().toArray(new DiscoItemsPacketExtension.Item[]{});
	}


	@Override
	public RoomInfo getRoomInfo(XMPPConnection connection, JID jid) throws XMPPException
	{
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
	public DiscoItemsPacketExtension.Item[] getRoomList(XMPPConnection connection, JID jid) throws XMPPException
	{
		DiscoItemsPacketExtension items  = discoItemsManager.getDiscoItems(connection, jid);
		
		return items.getItems().toArray(new DiscoItemsPacketExtension.Item[]{});
	}


	@Override
	public boolean isServerSupportMuc(XMPPConnection connection, JID jid) throws XMPPException
	{
		DiscoInfoPacketExtension info = discoInfoManager.getDiscoInfo(connection, jid);
		if (info.containFeature("http://jabber.org/protocol/muc"))
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean isUserSupportMuc(XMPPConnection connection, JID jid) throws XMPPException
	{
		DiscoInfoPacketExtension info = discoInfoManager.getDiscoInfo(connection, jid);
		if (info.containFeature("http://jabber.org/protocol/muc"))
		{
			return true;
		}
		return false;
	}

	@Override
	public DiscoItemsPacketExtension.Item[] getContactCurrentRooms(XMPPConnection connection, JID jid) throws XMPPException
	{
		DiscoItemsPacketExtension items  = discoItemsManager.getDiscoItems(connection, jid, "http://jabber.org/protocol/muc#rooms");
		return items.getItems().toArray(new DiscoItemsPacketExtension.Item[]{});
	}

	@Override
	public MucChat createMucChat(XMPPConnection connection, JID roomJID)
	{
		MucChatImpl mucChat = new MucChatImpl(mainManager, connection, this, roomJID);		
		mucChats.add(mucChat);
		
		return mucChat;
	}
	
	void removeMucChat(MucChat mucChat)
	{
		mucChats.remove(mucChat);
	}

	@Override
	public void processReceStanza(XMPPConnection connection, XMLStanza stanza)
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
				fireInvitationReceived(connection, message);
				return;
			}
			
			MucUserExtension.Decline decline = userExtension.getDecline();
			if (decline != null)
			{
				fireDeclineReceived(connection, message);
				return;
			}
		}
	}

	private void fireDeclineReceived(XMPPConnection connection, Message message)
	{
		for (MucListener listener : mucListeners)
		{
			listener.declineReceived(connection, message);
		}
	}

	private void fireInvitationReceived(XMPPConnection connection, Message message)
	{
		for (MucListener listener : mucListeners)
		{
			listener.invitationReceived(connection, message);
		}
	}


}
