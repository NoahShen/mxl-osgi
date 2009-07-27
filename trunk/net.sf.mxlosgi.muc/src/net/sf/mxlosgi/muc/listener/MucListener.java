/**
 * 
 */
package net.sf.mxlosgi.muc.listener;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.xmpp.Message;


/**
 * @author noah
 *
 */
public interface MucListener
{
	/**
	 * 
	 * @param message
	 */
	public void invitationReceived(XmppConnection connection, Message message);
	
	/**
	 * 
	 * @param message
	 */
	public void declineReceived(XmppConnection connection, Message message);
}
