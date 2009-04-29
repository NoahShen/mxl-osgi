/**
 * 
 */
package net.sf.mxlosgi.mxlosgifiletransferbundle.impl;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.filter.StanzaFilter;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class SiFilter implements StanzaFilter
{


	@Override
	public boolean accept(XMPPConnection connection, XMLStanza stanza)
	{
		if (stanza instanceof IQ)
		{
			IQ iq = (IQ) stanza;
			if (iq.getType() == IQ.Type.set && iq.getExtension("si", "http://jabber.org/protocol/si") != null)
			{
				return true;
			}
		}
		return false;
	}

}
