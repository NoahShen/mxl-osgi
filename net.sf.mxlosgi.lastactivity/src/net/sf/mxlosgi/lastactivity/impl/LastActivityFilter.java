/**
 * 
 */
package net.sf.mxlosgi.lastactivity.impl;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.lastactivity.LastActivityPacketExtension;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.XmlStanza;

/**
 * @author noah
 * 
 */
public class LastActivityFilter implements StanzaFilter
{

	@Override
	public boolean accept(XmppConnection connection, XmlStanza data)
	{
		if (data instanceof Iq)
		{
			Iq iq = (Iq) data;
			if (iq.getType() == Iq.Type.get && iq.getExtension(LastActivityPacketExtension.ELEMENTNAME, 
												LastActivityPacketExtension.NAMESPACE) != null)
			{
				return true;
			}
		}
		return false;
	}

}
