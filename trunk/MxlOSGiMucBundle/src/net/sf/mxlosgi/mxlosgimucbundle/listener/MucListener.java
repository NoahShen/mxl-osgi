/**
 * 
 */
package net.sf.mxlosgi.mxlosgimucbundle.listener;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgixmppbundle.Message;

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
	public void invitationReceived(XMPPConnection connection, Message message);
	
	/**
	 * 
	 * @param message
	 */
	public void declineReceived(XMPPConnection connection, Message message);
}
