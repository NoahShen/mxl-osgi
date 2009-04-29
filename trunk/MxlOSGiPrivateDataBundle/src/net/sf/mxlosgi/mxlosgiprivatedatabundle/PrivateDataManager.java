/**
 * 
 */
package net.sf.mxlosgi.mxlosgiprivatedatabundle;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;

/**
 * @author noah
 *
 */
public interface PrivateDataManager
{
	/**
	 * 
	 * @param connection
	 * @param extension
	 * @throws XMPPException
	 */
	public void storePrivateData(XMPPConnection connection, PacketExtension extension) throws XMPPException;
	
	/**
	 * 
	 * @param connection
	 * @param elementName
	 * @param namespace
	 * @return
	 * @throws XMPPException
	 */
	public PacketExtension getPrivateData(XMPPConnection connection, String elementName, String namespace) throws XMPPException;
}
