package net.sf.mxlosgi.core.filter;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.xmpp.XmlStanza;


/**
 * @author noah
 *
 */
public class StanzaTypeFilter implements StanzaFilter
{
	private Class<? extends XmlStanza> classType;
	
	
	public StanzaTypeFilter(Class<? extends XmlStanza> classType)
	{
		this.classType = classType;
	}



	@Override
	public boolean accept(XmppConnection connection, XmlStanza data)
	{
		return classType.isInstance(data);
	}

}
