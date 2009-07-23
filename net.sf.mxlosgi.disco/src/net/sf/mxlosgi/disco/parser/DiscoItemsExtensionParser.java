package net.sf.mxlosgi.disco.parser;

import net.sf.mxlosgi.disco.DiscoItemsPacketExtension;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.v1.XmlPullParser;


/**
 * @author noah
 *
 */
public class DiscoItemsExtensionParser implements ExtensionParser
{
	
	@Override
	public String getElementName()
	{
		return "query";
	}


	@Override
	public String getNamespace()
	{
		return "http://jabber.org/protocol/disco#items";
	}


	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XmppParser xmppParser) throws Exception
	{
		DiscoItemsPacketExtension discoItem = new DiscoItemsPacketExtension();
		String node = parser.getAttributeValue("", "node");
		discoItem.setNode(node);
		
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			if (eventType == XmlPullParser.START_TAG)
			{
				String namespace = parser.getNamespace(null);
				if ("item".equals(elementName))
				{
					DiscoItemsPacketExtension.Item item =
						new DiscoItemsPacketExtension.Item(new JID(parser.getAttributeValue("", "jid")));
					item.setName(parser.getAttributeValue("", "name"));
					item.setNode(parser.getAttributeValue("", "node"));
					discoItem.addItem(item);
				}
				else
				{
					ExtensionParser xparser = xmppParser.getExtensionParser(elementName, namespace);
					if (xparser != null)
					{
						PacketExtension packetX = xparser.parseExtension(parser, xmppParser);
						discoItem.addExtension(packetX);
					}
					else
					{
						PacketExtension packetX = xmppParser.parseUnknownExtension(parser, elementName, namespace);
						discoItem.addExtension(packetX);
					}
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if (getElementName().equals(elementName))
				{
					done = true;
				}
			}
			
		}
		
		return discoItem;
	}

}
