/**
 * 
 */
package net.sf.mxlosgi.mxlosgisearchbundle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class SearchExtension implements PacketExtension
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6796393078762331603L;

	public static final String ELEMENTNAME = "query";

	public static final String NAMESPACE = "jabber:iq:search";
	
	private String instructions;
	
	private List<Item> items = new ArrayList<Item>();
	
	private Map<String, String> fields = new LinkedHashMap<String, String>();
	
	private PacketExtension extension;
	/**
	 * 
	 */
	public SearchExtension()
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
	 * @return the fields
	 */
	public Map<String, String> getFields()
	{
		return fields;
	}

	/**
	 * @param instructions the instructions to set
	 */
	public void setInstructions(String instructions)
	{
		this.instructions = instructions;
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

	public Item[] getItems()
	{
		return items.toArray(new Item[]{});
	}
	
	public void addItem(Item item)
	{
		items.add(item);
	}
	
	public void removeItem(Item item)
	{
		items.remove(item);
	}
	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension#getElementName()
	 */
	@Override
	public String getElementName()
	{
		return ELEMENTNAME;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return NAMESPACE;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza#toXML()
	 */
	@Override
	public String toXML()
	{
		StringBuffer buf = new StringBuffer();

		buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
		
		if (!items.isEmpty())
		{
			for (Item item : getItems())
			{
				buf.append(item.toXML());
			}
		}
		
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

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		SearchExtension extension = (SearchExtension) super.clone();
		extension.instructions = this.instructions;
		
		extension.items = new ArrayList<Item>();
		for (Item item : getItems())
		{
			extension.items.add((Item) item.clone());
		}
		
		extension.fields = new LinkedHashMap<String, String>();
		extension.fields.putAll(this.fields);
		
		if (this.extension != null)
		{
			extension.extension = (PacketExtension) this.extension.clone();
		}
		
		return extension;
	}
	
	public static class Item implements XMLStanza
	{		
		/**
		 * 
		 */
		private static final long serialVersionUID = 5827020926346679198L;
		
		private JID jid;
		
		private Map<String, String> fields = new LinkedHashMap<String, String>();
		
		/**
		 * @param jid
		 */
		public Item(JID jid)
		{
			this.jid = jid;
		}

		/**
		 * @return the jid
		 */
		public JID getJid()
		{
			return jid;
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

		@Override
		public String toXML()
		{
			StringBuffer buf = new StringBuffer();

			buf.append("<item").append(" jid=\"").append(jid.toFullJID()).append("\">");			
			for (Map.Entry<String, String> entry : this.fields.entrySet())
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
			buf.append("</item>");
			
			return buf.toString();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#clone()
		 */
		@Override
		public Object clone() throws CloneNotSupportedException
		{
			Item item = (Item) super.clone();
			item.jid = this.jid;
			item.fields = new LinkedHashMap<String, String>();
			item.fields.putAll(this.fields);			
			return item;
		}
		
	}

}
