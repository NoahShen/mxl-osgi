/**
 * 
 */
package net.sf.mxlosgi.vcard;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.xmpp.JID;


/**
 * @author noah
 *
 */
public interface VCardManager
{
	/**
	 * 
	 * @param connection
	 * @param to
	 * @return
	 * @throws XmppException
	 */
	public VCardPacketExtension getVCard(XmppConnection connection, JID to) throws XmppException;
	
	/**
	 * 
	 * @param connection
	 * @param vCard
	 * @return
	 * @throws XmppException
	 */
	public boolean updateOwnerVCard(XmppConnection connection, VCardPacketExtension vCard) throws XmppException;
}
