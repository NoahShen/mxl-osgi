package net.sf.mxlosgi.mxlosgimainbundle.filter;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

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
