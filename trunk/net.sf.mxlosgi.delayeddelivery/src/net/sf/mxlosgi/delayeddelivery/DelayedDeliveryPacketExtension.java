package net.sf.mxlosgi.delayeddelivery;

import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.PacketExtension;

/**
 * @author noah
 *
 */
public class DelayedDeliveryPacketExtension implements PacketExtension
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5094407141101898425L;

	public static final String ELEMENTNAME = "delay";

	public static final String NAMESPACE = "urn:xmpp:delay";
	
	private JID from;
	
	private String stamp;
	
	private String reason;
	
	public DelayedDeliveryPacketExtension()
	{
	}

	public DelayedDeliveryPacketExtension(JID from, String stamp)
	{
		this();
		this.from = from;
		this.stamp = stamp;
	}

	/**
	 * @return the from
	 */
	public JID getFrom()
	{
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(JID from)
	{
		this.from = from;
	}

	/**
	 * @return the stamp
	 */
	public String getStamp()
	{
		return stamp;
	}

	/**
	 * @param stamp the stamp to set
	 */
	public void setStamp(String stamp)
	{
		this.stamp = stamp;
	}

	/**
	 * @return the reason
	 */
	public String getReason()
	{
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason)
	{
		this.reason = reason;
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
		StringBuffer buf=  new StringBuffer();
		
		buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\"");
		
		if (getFrom() != null)
		{
			buf.append(" from=\"").append(getFrom()).append("\"");
		}
		if (getStamp() != null)
		{
			buf.append(" stamp=\"").append(getStamp()).append("\"");
		}
		buf.append(">");
		
		if (getReason() != null)
		{
			buf.append(getReason());
		}
		buf.append("</").append(getElementName()).append(">");
		return buf.toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		DelayedDeliveryPacketExtension extension = (DelayedDeliveryPacketExtension) super.clone();
		extension.from = this.from;
		extension.stamp = this.stamp;
		extension.reason = this.reason;
		return extension;
	}
	
	

}