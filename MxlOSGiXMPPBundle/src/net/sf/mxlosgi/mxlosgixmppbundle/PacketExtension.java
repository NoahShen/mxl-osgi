package net.sf.mxlosgi.mxlosgixmppbundle;

/**
 * 
 * @author noah
 * 
 */
public interface PacketExtension extends XMLStanza
{

	/**
	 * Returns the root element name.
	 * 
	 * @return the element name.
	 */
	public String getElementName();

	/**
	 * Returns the root element XML namespace.
	 * 
	 * @return the namespace.
	 */
	public String getNamespace();

}