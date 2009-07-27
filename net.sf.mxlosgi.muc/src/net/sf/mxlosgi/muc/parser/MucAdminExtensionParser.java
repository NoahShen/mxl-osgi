/**
 * 
 */
package net.sf.mxlosgi.muc.parser;

import net.sf.mxlosgi.muc.MucAdminExtension;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.v1.XmlPullParser;


/**
 * @author noah
 * 
 */
public class MucAdminExtensionParser implements ExtensionParser
{

	@Override
	public String getElementName()
	{
		return "query";
	}


	@Override
	public String getNamespace()
	{
		return "http://jabber.org/protocol/muc#admin";
	}

	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XmppParser xmppParser) throws Exception
	{
		MucAdminExtension mucAdmin = new MucAdminExtension();
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG)
			{
				if (parser.getName().equals("item"))
				{
					mucAdmin.addItem(parseItem(parser));
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if (parser.getName().equals(getElementName()))
				{
					done = true;
				}
			}
		}

		return mucAdmin;
	}

	private MucAdminExtension.Item parseItem(XmlPullParser parser) throws Exception
	{

		MucAdminExtension.Item item = new MucAdminExtension.Item(parser.getAttributeValue("", "affiliation"));
		item.setNick(parser.getAttributeValue("", "nick"));
		String jid = parser.getAttributeValue("", "jid");
		if (jid != null && !jid.isEmpty())
		{
			item.setJid(new JID(jid));
		}
		item.setRole(parser.getAttributeValue("", "role"));
		
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG)
			{
				if (parser.getName().equals("actor"))
				{
					item.setActor(parser.getAttributeValue("", "jid"));
				}
				if (parser.getName().equals("reason"))
				{
					item.setReason(parser.nextText());
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if (parser.getName().equals("item"))
				{
					done = true;
				}
			}
		}
		return item;
	}
}
