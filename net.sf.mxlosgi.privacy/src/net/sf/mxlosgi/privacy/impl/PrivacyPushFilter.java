/**
 * 
 */
package net.sf.mxlosgi.privacy.impl;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmpp.XmlStanza;


/**
 * @author noah
 *
 */
public class PrivacyPushFilter implements StanzaFilter
{

	@Override
	public boolean accept(XmppConnection connection, XmlStanza stamza)
	{
		if (stamza instanceof Iq)
		{
			Iq iq = (Iq) stamza;
			PacketExtension extension = iq.getExtension("query", "jabber:iq:privacy");
			if (iq.getType() == Iq.Type.set || extension != null)
			{
				return true;
			}
		}
		return false;
	}

}
