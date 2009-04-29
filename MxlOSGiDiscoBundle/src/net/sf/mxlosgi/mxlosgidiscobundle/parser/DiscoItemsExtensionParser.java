package net.sf.mxlosgi.mxlosgidiscobundle.parser;

import org.xmlpull.v1.XmlPullParser;

import net.sf.mxlosgi.mxlosgidiscobundle.DiscoItemsPacketExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser;
import net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser;

/**
 * @author noah
 *
 */
public class DiscoItemsExtensionParser implements ExtensionParser
{

	/* (non-Javadoc)
	 * @see net.sf.christy.christyxmppparserbundle.ExtensionParser#getElementName()
	 */
	@Override
	public String getElementName()
	{
		return "query";
	}

	/* (non-Javadoc)
	 * @see net.sf.christy.christyxmppparserbundle.ExtensionParser#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return "http://jabber.org/protocol/disco#items";
	}

	/* (non-Javadoc)
	 * @see net.sf.christy.christyxmppparserbundle.ExtensionParser#parseExtension(org.xmlpull.v1.XmlPullParser, net.sf.christy.christyxmppparserbundle.XMPPParser)
	 */
	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XMPPParser xmppParser) throws Exception
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
