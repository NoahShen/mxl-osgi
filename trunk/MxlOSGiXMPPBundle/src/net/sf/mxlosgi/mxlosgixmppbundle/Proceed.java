package net.sf.mxlosgi.mxlosgixmppbundle;

/**
 * TLS proceed element
 * @author noah
 *
 */
public class Proceed implements XMLStanza
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8229318942917414106L;


	public Proceed()
	{
	}
	
	
	@Override
	public String toXML()
	{
		return "<proceed xmlns=\"urn:ietf:params:xml:ns:xmpp-tls\"/>";
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		Proceed proceed = (Proceed) super.clone();
		return proceed;
	}
	
	
}
