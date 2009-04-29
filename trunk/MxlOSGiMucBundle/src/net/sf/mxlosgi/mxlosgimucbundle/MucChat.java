/**
 * 
 */
package net.sf.mxlosgi.mxlosgimucbundle;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimucbundle.listener.MucChatListener;
import net.sf.mxlosgi.mxlosgiutilsbundle.IHasAttribute;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.Message;
import net.sf.mxlosgi.mxlosgixmppbundle.Presence;


/**
 * @author noah
 *
 */
public interface MucChat extends IHasAttribute
{

	public static final String ABOUTOWNER_CODE = "110";
	
	public static final String NICKNAMECHANGED_CODE = "303";
	
	/**
	 * 
	 * @return
	 */
	public JID getRoomJID();
	
	/**
	 * 
	 * @param nickName
	 * @return
	 */
	public MucRoomUser getRoomUser(String nickName);
	
	/**
	 * 
	 * @return
	 */
	public MucRoomUser[] getRoomUsers();

	/**
	 * 
	 * @param text
	 */
	public void sendMessage(String text);

	/**
	 * 
	 * @param message
	 */
	public void sendMessage(Message message);
	
	/**
	 * 
	 * @param listener
	 */
	public void addMucChatListener(MucChatListener listener);

	/**
	 * 
	 * @param listener
	 */
	public void removeMucChatListener(MucChatListener listener);

	/**
	 * 
	 */
	public void enterRoom();
	
	/**
	 * 
	 * @param password
	 */
	public void enterRoom(String password);
	
	/**
	 * 
	 * @param nickname
	 * @param password
	 */
	public void enterRoom(String nickname, String password);
	
	/**
	 * 
	 * @param history
	 */
	public void enterRoom(MucInitialPresenceExtension.History history);
	
	/**
	 * 
	 * @param nickname
	 * @param history
	 */
	public void enterRoom(String nickname, MucInitialPresenceExtension.History history);
	
	/**
	 * 
	 * @param nickname
	 * @param password
	 * @param history
	 */
	public void enterRoom(String nickname, String password, MucInitialPresenceExtension.History history);
	

//	public void changeNickname(String newNickname);
	
	/**
	 * 
	 * @param presence
	 */
	public void changeStatus(Presence presence);
	
	/**
	 * 
	 * @param jid
	 */
	public void inviteUser(JID jid);
	
	/**
	 * 
	 * @param jid
	 * @param reason
	 */
	public void inviteUser(JID jid, String reason);
	
	/**
	 * 
	 * @param subject
	 */
	public void changeSubject(String subject);
//	
//	public boolean banUser(String jid)  throws XMPPException;
//	
//	public boolean banUser(String jid, String reason)  throws XMPPException;
	
	/**
	 * 
	 */
	public void close();

	/**
	 * 
	 * @param status
	 */
	public void close(String status);
	
	/**
	 * 
	 * @return
	 */
	public XMPPConnection getConnection();
	
}
