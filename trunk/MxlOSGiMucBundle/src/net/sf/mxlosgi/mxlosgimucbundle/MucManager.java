/**
 * 
 */
package net.sf.mxlosgi.mxlosgimucbundle;

import net.sf.mxlosgi.mxlosgidiscobundle.DiscoItemsPacketExtension;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgimucbundle.listener.MucListener;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

/**
 * @author noah
 *
 */
public interface MucManager
{
	/**
	 * 
	 * @param connection
	 * @param jid
	 * @return
	 * @throws XMPPException
	 */
	public boolean isServerSupportMuc(XMPPConnection connection, JID jid) throws XMPPException;
	
	/**
	 * 
	 * @param listener
	 */
	public void addMucListener(MucListener listener);
	
	/**
	 * 
	 * @param listener
	 */
	public void removeMucListener(MucListener listener);
	
	/**
	 * 
	 * @param connection
	 * @param jid
	 * @return
	 * @throws XMPPException
	 */
	public DiscoItemsPacketExtension.Item[] getRoomList(XMPPConnection connection, JID jid) throws XMPPException;
	
	/**
	 * 
	 * @param connection
	 * @param jid
	 * @return
	 * @throws XMPPException
	 */
	public RoomInfo getRoomInfo(XMPPConnection connection, JID jid) throws XMPPException;
	
	/**
	 * 
	 * @param connection
	 * @param jid
	 * @return
	 * @throws XMPPException
	 */
	public DiscoItemsPacketExtension.Item[] getUsers(XMPPConnection connection, JID jid) throws XMPPException;
	
	/**
	 * 
	 * @param connection
	 * @param jid
	 * @return
	 * @throws XMPPException
	 */
	public boolean isUserSupportMuc(XMPPConnection connection, JID jid) throws XMPPException;
	
	/**
	 * 
	 * @param connection
	 * @param jid
	 * @return
	 * @throws XMPPException
	 */
	public DiscoItemsPacketExtension.Item[] getContactCurrentRooms(XMPPConnection connection, JID jid) throws XMPPException;
	
	/**
	 * 
	 * @param connection
	 * @param roomJID
	 * @return
	 */
	public MucChat createMucChat(XMPPConnection connection, JID roomJID);

	
}
