package net.sf.mxlosgi.mxlosgisoftwareversionbundle;

import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;

/**
 * @author noah
 *
 */
public class SoftwareVersionExtension implements PacketExtension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8431095076048229346L;
	
	public static final String ELEMENTNAME = "query";

	public static final String NAMESPACE = "jabber:iq:version";
	
	private String name;
	
	private String version;
	
	private String os;
	
	public SoftwareVersionExtension()
	{
	}

	public SoftwareVersionExtension(String name, String version)
	{
		this();
		this.name = name;
		this.version = version;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the version
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}

	/**
	 * @return the os
	 */
	public String getOs()
	{
		return os;
	}

	/**
	 * @param os the os to set
	 */
	public void setOs(String os)
	{
		this.os = os;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlcorebundle.PacketExtension#getElementName()
	 */
	@Override
	public String getElementName()
	{
		return ELEMENTNAME;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlcorebundle.PacketExtension#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return NAMESPACE;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlcorebundle.PacketExtension#toXML()
	 */
	@Override
	public String toXML()
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
		
		if (getName() != null)
		{
			buf.append("<name>").append(getName()).append("</name>");
		}
		
		if (getVersion() != null)
		{
			buf.append("<version>").append(getVersion()).append("</version>");
		}
		
		if (getOs() != null)
		{
			buf.append("<os>").append(getOs()).append("</os>");
		}
		
		buf.append("</").append(getElementName()).append(">");
		return buf.toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		SoftwareVersionExtension extension = (SoftwareVersionExtension) super.clone();
		extension.name = this.name;
		extension.version = this.version;
		extension.os = this.os;
		return extension;
	}
	
	

}