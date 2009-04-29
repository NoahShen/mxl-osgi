/**
 * 
 */
package net.sf.mxlosgi.mxlosgichatbundle.impl;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.filter.StanzaFilter;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.Message;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class MessageFilter implements StanzaFilter
{
	private XMPPConnection connection;
	
	private JID bareJID;
	
	/**
	 * @param connection
	 * @param bareJID
	 */
	public MessageFilter(XMPPConnection connection, JID bareJID)
	{
		this.connection = connection;
		this.bareJID = bareJID;
	}

	@Override
	public boolean accept(XMPPConnection connection, XMLStanza stanza)
	{

		if (this.connection == connection && stanza instanceof Message)
		{
			Message message = (Message) stanza;
			JID from = message.getFrom();
			if (bareJID.equalsWithBareJid(from))
			{
				return true;
			}
		}
		return false;
	}

}
