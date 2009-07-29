/**
 * 
 */
package net.sf.mxlosgi.softwareversion.impl;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.XmlStanza;

/**
 * @author noah
 *
 */
public class SoftwareVersionFilter implements StanzaFilter
{

	@Override
	public boolean accept(XmppConnection connection, XmlStanza data)
	{
		if (data instanceof Iq)
		{
			Iq iq = (Iq) data;
			if (iq.getType() == Iq.Type.get && iq.getExtension("query", "jabber:iq:version") != null)
			{
				return true;
			}
		}
		return false;
	}

}
