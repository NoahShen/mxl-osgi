package net.sf.mxlosgi.core.filter;

import net.sf.mxlosgi.core.XMPPConnection;
import net.sf.mxlosgi.xmpp.XMLStanza;


/**
 * @author noah
 *
 */
public class StanzaTypeFilter implements StanzaFilter
{
	private Class<? extends XMLStanza> classType;
	
	
	public StanzaTypeFilter(Class<? extends XMLStanza> classType)
	{
		this.classType = classType;
	}



	@Override
	public boolean accept(XMPPConnection connection, XMLStanza data)
	{
		return classType.isInstance(data);
	}

}
