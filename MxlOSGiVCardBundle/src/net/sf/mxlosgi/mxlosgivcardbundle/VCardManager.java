/**
 * 
 */
package net.sf.mxlosgi.mxlosgivcardbundle;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

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
	 * @throws XMPPException
	 */
	public VCardPacketExtension getVCard(XMPPConnection connection, JID to) throws XMPPException;
	
	/**
	 * 
	 * @param connection
	 * @param vCard
	 * @return
	 * @throws XMPPException
	 */
	public boolean updateOwnerVCard(XMPPConnection connection, VCardPacketExtension vCard) throws XMPPException;
}
