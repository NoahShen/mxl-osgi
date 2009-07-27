/**
 * 
 */
package net.sf.mxlosgi.muc.listener;

import java.util.Set;

import net.sf.mxlosgi.muc.MucChat;
import net.sf.mxlosgi.muc.MucRoomUser;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Message;
import net.sf.mxlosgi.xmpp.Packet;


/**
 * @author noah
 *
 */
public interface MucChatListener
{
	/**
	 * 
	 * @param mucChat
	 * @param message
	 */
	public void processMessage(MucChat mucChat, Message message);
	
	/**
	 * 
	 * @param mucChat
	 * @param user
	 */
	public void userStatusChanged(MucChat mucChat, MucRoomUser user);
	
	/**
	 * 
	 * @param mucChat
	 * @param user
	 */
	public void userUnavaliable(MucChat mucChat, MucRoomUser user);
	
	/**
	 * 
	 * @param mucChat
	 * @param oldNickName
	 * @param newNickName
	 */
	public void userNicknameChanged(MucChat mucChat, String oldNickName, String newNickName);
	
	/**
	 * statusCodes may contain 100 , 110, 170, 210
	 * @param mucChat
	 * @param user
	 */
	public void ownerStatusChanged(MucChat mucChat, MucRoomUser user, Set<String> statusCodes);
	
	/**
	 * 
	 * @param mucChat
	 * @param subject
	 * @param from
	 */
	public void subjectUpdated(MucChat mucChat, String subject, JID from);
	
	/**
	 * 
	 * @param mucChat
	 * @param packet
	 */
	public void error(MucChat mucChat, Packet packet);
}
