/**
 * 
 */
package net.sf.mxlosgi.privatedata;

import net.sf.mxlosgi.xmpp.PacketExtension;


/**
 * @author noah
 *
 */
public class PrivateDataExtension implements PacketExtension
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8527645959723637738L;

	public static final String ELEMENTNAME = "query";

	public static final String NAMESPACE = "jabber:iq:private";
	
	private PacketExtension privateDataExtension;
	
	/**
	 * 
	 */
	public PrivateDataExtension()
	{
	}

	/**
	 * @param privateDataExtension
	 */
	public PrivateDataExtension(PacketExtension privateDataExtension)
	{
		this.privateDataExtension = privateDataExtension;
	}

	@Override
	public String getElementName()
	{
		return ELEMENTNAME;
	}

	@Override
	public String getNamespace()
	{
		return NAMESPACE;
	}

	@Override
	public String toXML()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\"");
		if (getPrivateDataExtension() != null)
		{
			buf.append(">");
			buf.append(getPrivateDataExtension().toXML());
			buf.append("</").append(getElementName()).append(">");
		}
		else
		{
			buf.append("/>");
		}
		return buf.toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		PrivateDataExtension privateData = (PrivateDataExtension) super.clone();
		if (privateData != null)
		{
			privateData.setPrivateDataExtension((PacketExtension) privateData.clone());
		}
		return privateData;
	}

	/**
	 * @return the privateDataExtension
	 */
	public PacketExtension getPrivateDataExtension()
	{
		return privateDataExtension;
	}

	/**
	 * @param privateDataExtension the privateDataExtension to set
	 */
	public void setPrivateDataExtension(PacketExtension privateDataExtension)
	{
		this.privateDataExtension = privateDataExtension;
	}
}
