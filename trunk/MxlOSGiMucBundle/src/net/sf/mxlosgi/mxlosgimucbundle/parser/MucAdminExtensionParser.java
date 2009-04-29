/**
 * 
 */
package net.sf.mxlosgi.mxlosgimucbundle.parser;

import org.xmlpull.v1.XmlPullParser;

import net.sf.mxlosgi.mxlosgimucbundle.MucAdminExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser;
import net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser;

/**
 * @author noah
 * 
 */
public class MucAdminExtensionParser implements ExtensionParser
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser#getElementName()
	 */
	@Override
	public String getElementName()
	{
		return "query";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return "http://jabber.org/protocol/muc#admin";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser#parseExtension(org.xmlpull.v1.XmlPullParser,
	 *      net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser)
	 */
	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XMPPParser xmppParser) throws Exception
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
