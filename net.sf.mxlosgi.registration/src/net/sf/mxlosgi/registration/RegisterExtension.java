/**
 * 
 */
package net.sf.mxlosgi.registration;

import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.mxlosgi.xmpp.PacketExtension;


/**
 * @author noah
 *
 */
public class RegisterExtension implements PacketExtension
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5868182867344639693L;
	
	public static final String ELEMENTNAME = "query";

	public static final String NAMESPACE = "jabber:iq:register";
	
	private String instructions;
	
	private Map<String, String> fields = new LinkedHashMap<String, String>();
	
	private PacketExtension extension;
	/**
	 * 
	 */
	public RegisterExtension()
	{
	}

	/**
	 * @return the instructions
	 */
	public String getInstructions()
	{
		return instructions;
	}

	/**
	 * @param instructions the instructions to set
	 */
	public void setInstructions(String instructions)
	{
		this.instructions = instructions;
	}

	/**
	 * @return the fields
	 */
	public Map<String, String> getFields()
	{
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(Map<String, String> fields)
	{
		this.fields = fields;
	}

	/**
	 * @return the extension
	 */
	public PacketExtension getExtension()
	{
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(PacketExtension extension)
	{
		this.extension = extension;
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
		StringBuffer buf = new StringBuffer();

		buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
		
		if (getInstructions() != null)
		{
			buf.append("<instructions>").append(getInstructions()).append("</instructions>");
		}
		
		for (Map.Entry<String, String> entry : fields.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			if (key != null && !key.isEmpty())
			{
				buf.append("<").append(key);
				if (value != null && !value.isEmpty())
				{
					buf.append(">").append(value).append("</").append(key).append(">");
				}
				else
				{
					buf.append("/>");
				}
			}
		}
		
		if (getExtension() != null)
		{
			buf.append(getExtension().toXML());
		}
		
		buf.append("</").append(getElementName()).append(">");
		return buf.toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		RegisterExtension extension = (RegisterExtension) super.clone();
		extension.instructions = this.instructions;
		extension.fields = new LinkedHashMap<String, String>();
		extension.fields.putAll(this.fields);
		if (this.extension != null)
		{
			extension.extension = (PacketExtension) this.extension.clone();
		}
		
		return extension;
	}

}
