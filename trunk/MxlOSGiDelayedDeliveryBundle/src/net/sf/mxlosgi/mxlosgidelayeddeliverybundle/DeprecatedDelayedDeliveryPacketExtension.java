package net.sf.mxlosgi.mxlosgidelayeddeliverybundle;

import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;

/**
 * @author noah
 * 
 */
public class DeprecatedDelayedDeliveryPacketExtension implements PacketExtension
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9183852495280221628L;

	public static final String ELEMENTNAME = "x";

	public static final String NAMESPACE = "jabber:x:delay";
	
	private JID from;

	private String stamp;

	private String reason;

	public DeprecatedDelayedDeliveryPacketExtension()
	{
	}

	public DeprecatedDelayedDeliveryPacketExtension(JID from, String stamp)
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
	 * @param from
	 *                  the from to set
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
	 * @param stamp
	 *                  the stamp to set
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
	 * @param reason
	 *                  the reason to set
	 */
	public void setReason(String reason)
	{
		this.reason = reason;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlcorebundle.PacketExtension#getElementName()
	 */
	@Override
	public String getElementName()
	{
		return ELEMENTNAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlcorebundle.PacketExtension#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return NAMESPACE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlcorebundle.PacketExtension#toXML()
	 */
	@Override
	public String toXML()
	{
		StringBuffer buf = new StringBuffer();

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
		DeprecatedDelayedDeliveryPacketExtension extension = (DeprecatedDelayedDeliveryPacketExtension) super.clone();
		extension.from = this.from;
		extension.stamp = this.stamp;
		extension.reason = this.reason;
		return extension;
	}

}