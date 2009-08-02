/**
 * 
 */
package net.sf.mxlosgi.filetransfer.impl;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.XmlStanza;


/**
 * @author noah
 *
 */
public class SiFilter implements StanzaFilter
{


	@Override
	public boolean accept(XmppConnection connection, XmlStanza stanza)
	{
		if (stanza instanceof Iq)
		{
			Iq iq = (Iq) stanza;
			if (iq.getType() == Iq.Type.set && iq.getExtension("si", "http://jabber.org/protocol/si") != null)
			{
				return true;
			}
		}
		return false;
	}

}
