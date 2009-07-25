package net.sf.mxlosgi.chat.impl;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.xmpp.Message;
import net.sf.mxlosgi.xmpp.XmlStanza;

public class AllChatMessageFilter implements StanzaFilter
{

	@Override
	public boolean accept(XmppConnection connection, XmlStanza stanza)
	{
		if (stanza instanceof Message)
		{
			Message message = (Message) stanza;
			if (message.getType() == Message.Type.chat)
			{
				return true;
			}
		}
		return false;
	}

}