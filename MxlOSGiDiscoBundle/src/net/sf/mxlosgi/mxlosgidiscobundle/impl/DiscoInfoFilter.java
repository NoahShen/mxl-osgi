/**
 * 
 */
package net.sf.mxlosgi.mxlosgidiscobundle.impl;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.filter.StanzaFilter;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class DiscoInfoFilter implements StanzaFilter
{

	@Override
	public boolean accept(XMPPConnection connection, XMLStanza data)
	{
		if (data instanceof IQ)
		{
			IQ iq = (IQ) data;
			if (iq.getType() == IQ.Type.get)
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
