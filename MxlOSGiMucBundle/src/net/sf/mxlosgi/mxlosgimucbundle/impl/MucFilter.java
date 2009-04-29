/**
 * 
 */
package net.sf.mxlosgi.mxlosgimucbundle.impl;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.filter.StanzaFilter;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.Packet;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class MucFilter implements StanzaFilter
{
	private XMPPConnection connection;
	
	private JID fullJID;
	
	/**
	 * @param connection
	 * @param bareJID
	 */
	public MucFilter(XMPPConnection connection, JID fullJID)
	{
		this.connection = connection;
		this.fullJID = fullJID;
	}

	@Override
	public boolean accept(XMPPConnection connection, XMLStanza stanza)
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
