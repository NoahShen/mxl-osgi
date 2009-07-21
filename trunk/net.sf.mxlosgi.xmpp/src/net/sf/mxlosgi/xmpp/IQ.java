package net.sf.mxlosgi.xmpp;

import net.sf.mxlosgi.utils.StringUtils;


/**
 * IQ packet
 * 
 * @see http://www.ietf.org/rfc/rfc3920.txt
 * @author noah
 * 
 */
public class IQ extends Packet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1249533838467522028L;

	private Type type = Type.get;

	public IQ()
	{
	}

	public IQ(Type type)
	{
		this.type = type;
	}

	/**
	 * Returns the type of the IQ packet.
	 * 
	 * @return the type of the IQ packet.
	 */
	public Type getType()
	{
		return type;
	}

	/**
	 * Sets the type of the IQ packet.
	 * 
	 * @param type
	 *                  the type of the IQ packet.
	 */
	public void setType(Type type)
	{
		if (type == null)
		{
			this.type = Type.get;
		}
		else
		{
			this.type = type;
		}
	}

	public String toXML()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("<iq ");
		if (getStanzaID() != null)
		{
			buf.append("id=\"" + getStanzaID() + "\" ");
		}
		if (getTo() != null)
		{
			buf.append("to=\"").append(StringUtils.escapeForXML(getTo().toFullJID())).append("\" ");
		}
		if (getFrom() != null)
		{
			buf.append("from=\"").append(StringUtils.escapeForXML(getFrom().toFullJID())).append("\" ");
		}
		if (type == null)
		{
			buf.append("type=\"get\">");
		}
		else
		{
			buf.append("type=\"").append(getType()).append("\">");
		}

		String extension = getExtensionsXML();
		if (extension != null)
		{
			buf.append(extension);
		}
		// Add the error sub-packet, if there is one.
		XMPPError error = getError();
		if (error != null)
		{
			buf.append(error.toXML());
		}
		buf.append("</iq>");
		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgixmppbundle.Packet#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		IQ iq = (IQ) super.clone();
		iq.setType(this.type);
		return iq;
	}
	
	/**
	 * A class to represent the type of the IQ packet. The types are:
	 * 
	 * <ul>
	 * <li>IQ.Type.GET
	 * <li>IQ.Type.SET
	 * <li>IQ.Type.RESULT
	 * <li>IQ.Type.ERROR
	 * </ul>
	 */
	public static enum Type
	{
		get,

		set,

		result,

		error;
		/**
		 * Converts a String into the corresponding types. Valid
		 * String values that can be converted to types are: "get",
		 * "set", "result", and "error".
		 * 
		 * @param type
		 *                  the String value to covert.
		 * @return the corresponding Type.
		 */

		public static Type fromString(String name)
		{
			try
			{
				return Type.valueOf(name);
			}
			catch (Exception e)
			{
				return null;
			}
		}
	}
}
