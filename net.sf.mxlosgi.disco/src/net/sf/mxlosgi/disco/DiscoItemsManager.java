package net.sf.mxlosgi.disco;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.xmpp.JID;


/**
 * @author noah
 *
 */
public interface DiscoItemsManager
{
	/**
	 * 
	 * @param connection
	 * @param to
	 * @return
	 * @throws XMPPException
	 */
	public DiscoItemsPacketExtension getDiscoItems(XmppConnection connection, JID to)  throws XmppException;
	
	/**
	 * 
	 * @param connection
	 * @param to
	 * @param node
	 * @return
	 * @throws XMPPException
	 */
	public DiscoItemsPacketExtension getDiscoItems(XmppConnection connection, JID to, String node)  throws XmppException;
}