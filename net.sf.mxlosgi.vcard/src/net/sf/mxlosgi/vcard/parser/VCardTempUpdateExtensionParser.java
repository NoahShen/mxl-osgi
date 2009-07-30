package net.sf.mxlosgi.vcard.parser;

import net.sf.mxlosgi.vcard.VCardTempUpdatePacketExtension;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.v1.XmlPullParser;


/**
 * @author noah
 *
 */
public class VCardTempUpdateExtensionParser implements ExtensionParser
{
	public static final String ELEMENTNAME = "x";

	public static final String NAMESPACE = "vcard-temp:x:update";
	
	public VCardTempUpdateExtensionParser()
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
		VCardTempUpdatePacketExtension vCardTempUpdate = null;

		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			if (eventType == XmlPullParser.START_TAG)
			{
				if ("photo".equals(elementName))
				{
					vCardTempUpdate = new VCardTempUpdatePacketExtension(parser.nextText());
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

		return vCardTempUpdate;
	}


}