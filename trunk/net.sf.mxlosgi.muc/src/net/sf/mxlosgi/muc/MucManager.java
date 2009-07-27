/**
 * 
 */
package net.sf.mxlosgi.muc;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.disco.DiscoItemsPacketExtension;
import net.sf.mxlosgi.xmpp.JID;


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
	 * @throws XmppException
	 */
	public boolean isServerSupportMuc(XmppConnection connection, JID jid) throws XmppException;
	
	/**
	 * 
	 * @param connection
	 * @param jid
	 * @return
	 * @throws XmppException
	 */
	public DiscoItemsPacketExtension.Item[] getRoomList(XmppConnection connection, JID jid) throws XmppException;
	
	/**
	 * 
	 * @param connection
	 * @param jid
	 * @return
	 * @throws XmppException
	 */
	public RoomInfo getRoomInfo(XmppConnection connection, JID jid) throws XmppException;
	
	/**
	 * 
	 * @param connection
	 * @param jid
	 * @return
	 * @throws XmppException
	 */
	public DiscoItemsPacketExtension.Item[] getUsers(XmppConnection connection, JID jid) throws XmppException;
	
	/**
	 * 
	 * @param connection
	 * @param jid
	 * @return
	 * @throws XmppException
	 */
	public boolean isUserSupportMuc(XmppConnection connection, JID jid) throws XmppException;
	
	/**
	 * 
	 * @param connection
	 * @param jid
	 * @return
	 * @throws XmppException
	 */
	public DiscoItemsPacketExtension.Item[] getContactCurrentRooms(XmppConnection connection, JID jid) throws XmppException;
	
	/**
	 * 
	 * @param connection
	 * @param roomJID
	 * @return
	 */
	public MucChat createMucChat(XmppConnection connection, JID roomJID);
	
	/**
	 * 
	 * @param connection
	 * @param roomJID
	 * @return
	 */
	public MucChat getMucChat(XmppConnection connection, JID roomJID);

	
}
