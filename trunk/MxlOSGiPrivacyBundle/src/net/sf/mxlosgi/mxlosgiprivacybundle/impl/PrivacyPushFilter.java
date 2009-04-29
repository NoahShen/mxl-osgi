/**
 * 
 */
package net.sf.mxlosgi.mxlosgiprivacybundle.impl;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.filter.StanzaFilter;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class PrivacyPushFilter implements StanzaFilter
{

	@Override
	public boolean accept(XMPPConnection connection, XMLStanza stamza)
	{
		if (stamza instanceof IQ)
		{
			IQ iq = (IQ) stamza;
			PacketExtension extension = iq.getExtension("query", "jabber:iq:privacy");
			if (iq.getType() == IQ.Type.set || extension != null)
			{
				return true;
			}
		}
		return false;
	}

}
