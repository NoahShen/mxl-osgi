/**
 * 
 */
package net.sf.mxlosgi.mxlosgilastactivitybundle.impl;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.filter.StanzaFilter;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 * 
 */
public class LastActivityFilter implements StanzaFilter
{

	@Override
	public boolean accept(XMPPConnection connection, XMLStanza data)
	{
		if (data instanceof IQ)
		{
			IQ iq = (IQ) data;
			if (iq.getType() == IQ.Type.get && iq.getExtension("query", "jabber:iq:last") != null)
			{
				return true;
			}
		}
		return false;
	}

}
