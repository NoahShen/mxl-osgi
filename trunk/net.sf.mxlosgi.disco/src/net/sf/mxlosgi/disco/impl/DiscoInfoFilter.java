/**
 * 
 */
package net.sf.mxlosgi.disco.impl;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.XmlStanza;

/**
 * @author noah
 *
 */
public class DiscoInfoFilter implements StanzaFilter
{

	@Override
	public boolean accept(XmppConnection connection, XmlStanza data)
	{
		if (data instanceof Iq)
		{
			Iq iq = (Iq) data;
			if (iq.getType() == Iq.Type.get)
			{
				if (iq.getExtension("query", "http://jabber.org/protocol/disco#info") != null)
				{
					return true;
				}
			}
		}
		return false;
	}

}
