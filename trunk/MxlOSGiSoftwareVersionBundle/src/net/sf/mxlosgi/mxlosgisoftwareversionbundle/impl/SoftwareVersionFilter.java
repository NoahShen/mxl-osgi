/**
 * 
 */
package net.sf.mxlosgi.mxlosgisoftwareversionbundle.impl;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.filter.StanzaFilter;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class SoftwareVersionFilter implements StanzaFilter
{

	@Override
	public boolean accept(XMPPConnection connection, XMLStanza data)
	{
		if (data instanceof IQ)
		{
			IQ iq = (IQ) data;
			if (iq.getType() == IQ.Type.get && iq.getExtension("query", "jabber:iq:version") != null)
			{
				return true;
			}
		}
		return false;
	}

}
