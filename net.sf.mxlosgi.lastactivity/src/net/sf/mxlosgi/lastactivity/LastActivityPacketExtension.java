package net.sf.mxlosgi.lastactivity;

import net.sf.mxlosgi.xmpp.PacketExtension;

/**
 * @author noah
 *
 */
public class LastActivityPacketExtension implements PacketExtension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7385908935253747118L;

	public static final String ELEMENTNAME = "query";

	public static final String NAMESPACE = "jabber:iq:last";
	
	private long seconds = -1;

	private String message;
	    
	
	public LastActivityPacketExtension()
	{
	}

	
	public LastActivityPacketExtension(long seconds)
	{
		this();
		this.seconds = seconds;
	}

	/**
	 * @return the seconds
	 */
	public long getSeconds()
	{
		return seconds;
	}


	/**
	 * @param seconds the seconds to set
	 */
	public void setSeconds(long seconds)
	{
		this.seconds = seconds;
	}


	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}


	/**
	 * @param message the message to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
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
		if (getSeconds() != -1)
		{
			buf.append(" seconds=\"").append(getSeconds()).append("\"");

		}
		
		if (getMessage() != null)
		{
			buf.append(">").append(getMessage()).append("</").append(getElementName()).append(">");
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
		LastActivityPacketExtension extension = (LastActivityPacketExtension) super.clone();
		extension.seconds = this.seconds;
		extension.message = this.message;
		return extension;
	}

}