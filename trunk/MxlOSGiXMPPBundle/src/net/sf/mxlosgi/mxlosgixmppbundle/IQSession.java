package net.sf.mxlosgi.mxlosgixmppbundle;

/**
 * IQ packet that will be sent to the server to establish a session.
 * 
 * @see http://www.xmpp.org/specs/rfc3921.html
 * @author noah
 */
public class IQSession implements PacketExtension
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1308104909346824034L;

	public IQSession()
	{
	}

	@Override
	public String getElementName()
	{
		return "session";
	}

	@Override
	public String getNamespace()
	{
		return "urn:ietf:params:xml:ns:xmpp-session";
	}

	@Override
	public String toXML()
	{
		return "<" + getElementName() + " xmlns=\"" + getNamespace() + "\"/>";
	}


	@Override
	public Object clone() throws CloneNotSupportedException
	{
		IQSession session = (IQSession) super.clone();
		return session;
	}
	
	
}