/**
 * 
 */
package net.sf.mxlosgi.mxlosgimucbundle.listener;

import java.util.Set;

import net.sf.mxlosgi.mxlosgimucbundle.MucRoomUser;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.Message;
import net.sf.mxlosgi.mxlosgixmppbundle.Packet;

/**
 * @author noah
 *
 */
public interface MucChatListener
{
	/**
	 * 
	 * @param message
	 */
	public void processMessage(Message message);
	
	/**
	 * 
	 * @param user
	 */
	public void userStatusChanged(MucRoomUser user);
	
	/**
	 * 
	 * @param user
	 */
	public void userUnavaliable(MucRoomUser user);
	
	/**
	 * 
	 * @param oldNickName
	 * @param newNickName
	 */
	public void userNicknameChanged(String oldNickName, String newNickName);
	
	/**
	 * statusCodes may contain 100 , 110, 170, 210
	 * @param user
	 */
	public void ownerStatusChanged(MucRoomUser user, Set<String> statusCodes);
	
	/**
	 * 
	 * @param subject
	 * @param from
	 */
	public void subjectUpdated(String subject, JID from);
	
	/**
	 * 
	 * @param error
	 */
	public void error(Packet packet);
}
