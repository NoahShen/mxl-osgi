/**
 * 
 */
package net.sf.mxlosgi.privatedata;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.xmpp.PacketExtension;


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
	 * @throws XmppException
	 */
	public void storePrivateData(XmppConnection connection, PacketExtension extension) throws XmppException;
	
	/**
	 * 
	 * @param connection
	 * @param elementName
	 * @param namespace
	 * @return
	 * @throws XmppException
	 */
	public PacketExtension getPrivateData(XmppConnection connection, String elementName, String namespace) throws XmppException;
}
