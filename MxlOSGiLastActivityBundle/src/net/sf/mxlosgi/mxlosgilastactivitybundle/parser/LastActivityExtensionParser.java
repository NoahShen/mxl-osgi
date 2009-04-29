package net.sf.mxlosgi.mxlosgilastactivitybundle.parser;

import org.xmlpull.v1.XmlPullParser;

import net.sf.mxlosgi.mxlosgilastactivitybundle.LastActivityPacketExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser;
import net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser;

/**
 * @author noah
 *
 */
public class LastActivityExtensionParser implements ExtensionParser
{
	
	public LastActivityExtensionParser()
	{
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlmainbundle.ExtensionParser#getElementName()
	 */
	@Override
	public String getElementName()
	{
		return "query";
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlmainbundle.ExtensionParser#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return "jabber:iq:last";
	}


	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XMPPParser xmppParser) throws Exception
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