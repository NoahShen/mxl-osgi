package net.sf.mxlosgi.mxlosgidiscobundle;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class DiscoItemsPacketExtension implements PacketExtension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4202116520800380238L;

	public static final String ELEMENTNAME = "query";
	
	public static final String NAMESPACE = "http://jabber.org/protocol/disco#items";
	
	private String node;
	
	private List<Item> items = Collections.synchronizedList(new LinkedList<Item>());
	
	private List<PacketExtension> packetExtensions = new CopyOnWriteArrayList<PacketExtension>();
	/**
	 * @return the node
	 */
	public String getNode()
	{
		return node;
	}

	/**
	 * @param node the node to set
	 */
	public void setNode(String node)
	{
		this.node = node;
	}

	public void addItem(Item item)
	{
		items.add(item);
	}
	
	public void removeItem(Item item)
	{
		items.remove(item);
	}
	
	public void removeAllItems()
	{
		items.clear();
	}
	
	/**
	 * Returns an unmodifiable collection of the packet extensions
	 * attached to the packet.
	 * 
	 * @return the packet extensions.
	 */
	public synchronized Collection<PacketExtension> getExtensions()
	{
		return Collections.unmodifiableCollection(packetExtensions);
	}

	/**
	 * Returns the first extension of this packet that has the given
	 * namespace.
	 * 
	 * @param namespace
	 *                  the namespace of the extension that is desired.
	 * @return the packet extension with the given namespace.
	 */
	public PacketExtension getExtension(String namespace)
	{
		return getExtension(null, namespace);
	}

	/**
	 * Returns the first packet extension that matches the specified
	 * element name and namespace, or <tt>null</tt> if it doesn't exist.
	 * 
	 * @param elementName
	 *                  the XML element name of the packet extension. (May
	 *                  be null)
	 * @param namespace
	 *                  the XML element namespace of the packet extension.
	 * @return the extension, or <tt>null</tt> if it doesn't exist.
	 */
	public PacketExtension getExtension(String elementName, String namespace)
	{
		if (namespace == null)
		{
			return null;
		}
		for (PacketExtension ext : packetExtensions)
		{
			if ((elementName == null || elementName.equals(ext.getElementName())) 
					&& namespace.equals(ext.getNamespace()))
			{
				return ext;
			}
		}
		return null;
	}

	/**
	 * Adds a packet extension to the packet.
	 * 
	 * @param extension
	 *                  a packet extension.
	 */
	public void addExtension(PacketExtension extension)
	{
		if (extension != null)
		{
			packetExtensions.add(extension);
		}
	}

	/**
	 * Removes a packet extension from the packet.
	 * 
	 * @param extension
	 *                  the packet extension to remove.
	 */
	public void removeExtension(PacketExtension extension)
	{
		packetExtensions.remove(extension);
	}
	
	/**
	 * Returns the extension sub-packets (including properties data) as an
	 * XML String, or the Empty String if there are no packet extensions.
	 * 
	 * @return the extension sub-packets as XML or the Empty String if
	 *         there are no packet extensions.
	 */
	protected synchronized String getExtensionsXML()
	{
		StringBuilder buf = new StringBuilder();
		// Add in all standard extension sub-packets.
		for (PacketExtension extension : getExtensions())
		{
			buf.append(extension.toXML());
		}

		return buf.toString();
	}
	public Collection<Item> getItems()
	{
		return Collections.unmodifiableCollection(items);
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
		if (getNode() != null)
		{
			buf.append(" node=\"");
			buf.append(getNode());
			buf.append("\"");
		}
		buf.append(">");

		for (Item item : items)
		{
			buf.append(item.toXML());
		}

		buf.append(getExtensionsXML());
		
		buf.append("</" + getElementName() + ">");
		return buf.toString();
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		DiscoItemsPacketExtension extension = (DiscoItemsPacketExtension) super.clone();
		extension.node = this.node;
		extension.items = Collections.synchronizedList(new LinkedList<Item>());
		for (Item item : this.items)
		{
			extension.items.add((Item) item.clone());
		}
		return extension;
	}


	public static class Item implements XMLStanza
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -6097450110553506370L;

		private JID jid;
		
		private String node;
		
		private String name;

		public Item(JID jid)
		{
			this.jid = jid;
		}

		public Item(JID jid, String name)
		{
			this.jid = jid;
			this.name = name;
		}

		/**
		 * @return the jid
		 */
		public JID getJid()
		{
			return jid;
		}

		/**
		 * @param jid the jid to set
		 */
		public void setJid(JID jid)
		{
			this.jid = jid;
		}

		/**
		 * @return the node
		 */
		public String getNode()
		{
			return node;
		}

		/**
		 * @param node the node to set
		 */
		public void setNode(String node)
		{
			this.node = node;
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
		
		public String toXML()
		{
			StringBuilder buf = new StringBuilder();
			buf.append("<item jid=\"").append(getJid().toBareJID()).append("\"");
			if (getName() != null)
			{
				buf.append(" name=\"").append(getName()).append("\"");
			}
			if (getNode() != null)
			{
				buf.append(" node=\"").append(getNode()).append("\"");
			}
			buf.append("/>");
			return buf.toString();
		}

		@Override
		public String toString()
		{
			StringBuilder buf = new StringBuilder();
			buf.append("jid : " + jid + "\n")
				.append("name : " + name + "\n")
				.append("node : " + node + "\n");
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
			item.node = this.node;
			item.name = this.name;
			return item;
		}
		
		
	}

}