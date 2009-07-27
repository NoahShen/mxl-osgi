package net.sf.mxlosgi.muc.parser;

import net.sf.mxlosgi.muc.MucUserExtension;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.v1.XmlPullParser;


public class MucUserExtensionParser implements ExtensionParser
{

	@Override
	public String getElementName()
	{
		return "x";
	}

	@Override
	public String getNamespace()
	{
		return "http://jabber.org/protocol/muc#user";
	}

	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XmppParser xmppParser) throws Exception
	{
		MucUserExtension mucUser = new MucUserExtension();
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG)
			{
				if (parser.getName().equals("invite"))
				{
					mucUser.setInvite(parseInvite(parser));
				}
				if (parser.getName().equals("item"))
				{
					mucUser.setItem(parseItem(parser));
				}
				if (parser.getName().equals("password"))
				{
					mucUser.setPassword(parser.nextText());
				}
				if (parser.getName().equals("status"))
				{
					mucUser.addStatusCode(parser.getAttributeValue("", "code"));
				}
				if (parser.getName().equals("decline"))
				{
					mucUser.setDecline(parseDecline(parser));
				}
				if (parser.getName().equals("destroy"))
				{
					mucUser.setDestroy(parseDestroy(parser));
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

		return mucUser;
	}

	private MucUserExtension.Item parseItem(XmlPullParser parser) throws Exception
	{
		boolean done = false;
		MucUserExtension.Item item = new MucUserExtension.Item(parser.getAttributeValue("", "affiliation"), parser.getAttributeValue("", "role"));
		item.setNick(parser.getAttributeValue("", "nick"));
		String jidStr = parser.getAttributeValue("", "jid");
		if (jidStr != null && !jidStr.isEmpty())
		{
			item.setJid(new JID(jidStr));
		}
		while (!done)
		{
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG)
			{
				if (parser.getName().equals("actor"))
				{
					String actorJID = parser.getAttributeValue("", "jid");
					if (actorJID != null && !actorJID.isEmpty())
					{
						item.setActor(new JID(actorJID));
					}
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

	private MucUserExtension.Invite parseInvite(XmlPullParser parser) throws Exception
	{
		boolean done = false;
		MucUserExtension.Invite invite = new MucUserExtension.Invite();
		String fromJIDStr = parser.getAttributeValue("", "from");
		if (fromJIDStr != null && !fromJIDStr.isEmpty())
		{
			invite.setFrom(new JID(fromJIDStr));
		}
		
		String toJIDStr = parser.getAttributeValue("", "to");
		if (toJIDStr != null && !toJIDStr.isEmpty())
		{
			invite.setTo(new JID(toJIDStr));
		}
		
		while (!done)
		{
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG)
			{
				if (parser.getName().equals("reason"))
				{
					invite.setReason(parser.nextText());
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if (parser.getName().equals("invite"))
				{
					done = true;
				}
			}
		}
		return invite;
	}

	private MucUserExtension.Decline parseDecline(XmlPullParser parser) throws Exception
	{
		boolean done = false;
		MucUserExtension.Decline decline = new MucUserExtension.Decline();
		String fromJIDStr = parser.getAttributeValue("", "from");
		if (fromJIDStr != null && !fromJIDStr.isEmpty())
		{
			decline.setFrom(new JID(fromJIDStr));
		}
		
		String toJIDStr = parser.getAttributeValue("", "to");
		if (toJIDStr != null && !toJIDStr.isEmpty())
		{
			decline.setTo(new JID(toJIDStr));
		}
		
		while (!done)
		{
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG)
			{
				if (parser.getName().equals("reason"))
				{
					decline.setReason(parser.nextText());
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if (parser.getName().equals("decline"))
				{
					done = true;
				}
			}
		}
		return decline;
	}

	private MucUserExtension.Destroy parseDestroy(XmlPullParser parser) throws Exception
	{
		boolean done = false;
		MucUserExtension.Destroy destroy = new MucUserExtension.Destroy();
		String jidStr = parser.getAttributeValue("", "jid");
		if (jidStr != null && !jidStr.isEmpty())
		{
			destroy.setJid(new JID(jidStr));
		}
		
		while (!done)
		{
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG)
			{
				if (parser.getName().equals("reason"))
				{
					destroy.setReason(parser.nextText());
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if (parser.getName().equals("destroy"))
				{
					done = true;
				}
			}
		}
		return destroy;
	}
}
