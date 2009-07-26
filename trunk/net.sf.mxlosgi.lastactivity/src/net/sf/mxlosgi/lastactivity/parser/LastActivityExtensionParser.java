package net.sf.mxlosgi.lastactivity.parser;

import net.sf.mxlosgi.lastactivity.LastActivityPacketExtension;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.v1.XmlPullParser;


/**
 * @author noah
 *
 */
public class LastActivityExtensionParser implements ExtensionParser
{

	public static final String ELEMENTNAME = "query";

	public static final String NAMESPACE = "jabber:iq:last";
	
	public LastActivityExtensionParser()
	{
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
	public PacketExtension parseExtension(XmlPullParser parser, XmppParser xmppParser) throws Exception
	{
		LastActivityPacketExtension lastActivity = new LastActivityPacketExtension();

		String strSeconds = parser.getAttributeValue("", "seconds");
		if (strSeconds != null && !strSeconds.isEmpty())
		{
			lastActivity.setSeconds(Long.parseLong(strSeconds));
		}

		String message = parser.nextText();
		if (message != null && !message.isEmpty())
		{
			lastActivity.setMessage(message);
		}

//		boolean done = false;
//		while (!done)
//		{
//			int eventType = parser.next();
//			if (eventType == XmlPullParser.START_TAG)
//			{
//				
//			}
//			else if (eventType == XmlPullParser.END_TAG)
//			{
//				if (getElementName().equals(parser.getName()))
//				{
//					done = true;
//				}
//			}
//		}
		return lastActivity;
	}

}