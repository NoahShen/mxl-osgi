/**
 * 
 */
package net.sf.mxlosgi.search.parser;

import java.io.IOException;

import net.sf.mxlosgi.search.SearchExtension;
import net.sf.mxlosgi.search.SearchExtension.Item;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author noah
 *
 */
public class SearchExtensionParser implements ExtensionParser
{

	@Override
	public String getElementName()
	{
		return "query";
	}

	@Override
	public String getNamespace()
	{
		return "jabber:iq:search";
	}

	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XmppParser xmppParser) throws Exception
	{
		SearchExtension searchExtension = new SearchExtension();

		boolean done = false;

		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			if (eventType == XmlPullParser.START_TAG)
			{
				String namespace = parser.getNamespace(null);
				if ("instructions".equals(elementName))
				{
					searchExtension.setInstructions(parser.nextText());
				}
				else if ("item".equals(elementName))
				{
					searchExtension.addItem(parseItem(parser));
				}
				else if (SearchExtension.NAMESPACE.equals(namespace))
				{
					searchExtension.getFields().put(elementName, parser.nextText());
				}
				else
				{
					ExtensionParser xparser = xmppParser.getExtensionParser(elementName, namespace);
					if (xparser != null)
					{
						PacketExtension packetX = xparser.parseExtension(parser, xmppParser);
						searchExtension.setExtension(packetX);
					}
					else
					{
						PacketExtension packetX = xmppParser.parseUnknownExtension(parser, elementName, namespace);
						searchExtension.setExtension(packetX);
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

		return searchExtension;
	}

	private Item parseItem(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		String jid = parser.getAttributeValue("", "jid");
		SearchExtension.Item item = new SearchExtension.Item(new JID(jid));
		boolean done = false;

		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			if (eventType == XmlPullParser.START_TAG)
			{
				item.getFields().put(elementName, parser.nextText());
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("item".equals(elementName))
				{
					done = true;
				}
			}
		}
		return item;
	}

}
