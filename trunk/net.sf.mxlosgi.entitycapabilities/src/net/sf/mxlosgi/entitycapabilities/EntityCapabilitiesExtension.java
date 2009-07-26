/**
 * 
 */
package net.sf.mxlosgi.entitycapabilities;

import net.sf.mxlosgi.xmpp.PacketExtension;


/**
 * @author noah
 *
 */
public class EntityCapabilitiesExtension implements PacketExtension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1751441629321794782L;

	public static final String ELEMENTNAME = "c";

	public static final String NAMESPACE = "http://jabber.org/protocol/caps";
	
	private String ext;
	
	private String hash;
	
	private String node;
	
	private String ver;
	
	
	/**
	 * @param node
	 * @param ver
	 */
	public EntityCapabilitiesExtension(String node, String ver)
	{
		this.node = node;
		this.ver = ver;
	}

	/**
	 * @return the ext
	 */
	public String getExt()
	{
		return ext;
	}

	/**
	 * @return the hash
	 */
	public String getHash()
	{
		return hash;
	}

	/**
	 * @return the node
	 */
	public String getNode()
	{
		return node;
	}

	/**
	 * @return the ver
	 */
	public String getVer()
	{
		return ver;
	}

	/**
	 * @param ext the ext to set
	 */
	public void setExt(String ext)
	{
		this.ext = ext;
	}

	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash)
	{
		this.hash = hash;
	}

	/**
	 * @param node the node to set
	 */
	public void setNode(String node)
	{
		this.node = node;
	}

	/**
	 * @param ver the ver to set
	 */
	public void setVer(String ver)
	{
		this.ver = ver;
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
		if (getExt() != null)
		{
			buf.append(" ext=\"").append(getExt()).append("\"");
		}
		
		if (getHash() != null)
		{
			buf.append(" hash=\"").append(getHash()).append("\"");
		}
		
		if (getNode() != null)
		{
			buf.append(" node=\"").append(getNode()).append("\"");
		}
		
		if (getVer() != null)
		{
			buf.append(" ver=\"").append(getVer()).append("\"");
		}
		
		buf.append("/>");

		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		EntityCapabilitiesExtension extension = (EntityCapabilitiesExtension) super.clone();
		extension.ext =this.ext;
		extension.hash = this.hash;
		extension.node = this.node;
		extension.ver = this.ver;
		
		return extension;
	}

}
