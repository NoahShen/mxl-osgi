/**
 * 
 */
package net.sf.mxlosgi.muc.impl;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Packet;
import net.sf.mxlosgi.xmpp.XmlStanza;


/**
 * @author noah
 *
 */
public class MucFilter implements StanzaFilter
{
	private XmppConnection connection;
	
	private JID fullJID;
	
	/**
	 * @param connection
	 * @param bareJID
	 */
	public MucFilter(XmppConnection connection, JID fullJID)
	{
		this.connection = connection;
		this.fullJID = fullJID;
	}

	@Override
	public boolean accept(XmppConnection connection, XmlStanza stanza)
	{
		if (this.connection == connection && stanza instanceof Packet)
		{
			Packet packet = (Packet) stanza;
			JID from = packet.getFrom();
			if (fullJID.equalsWithBareJid(from))
			{
				return true;
			}
		}
		return false;
	}

}
