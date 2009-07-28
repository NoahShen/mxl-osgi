/**
 * 
 */
package net.sf.mxlosgi.privatedata.parser;

import net.sf.mxlosgi.privatedata.PrivateDataExtension;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.v1.XmlPullParser;


/**
 * @author noah
 *
 */
public class PrivateDataExtensionParser implements ExtensionParser
{

	public static final String ELEMENTNAME = "query";

	public static final String NAMESPACE = "jabber:iq:private";
	
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
		PrivateDataExtension privateData = new PrivateDataExtension();

		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			if (eventType == XmlPullParser.START_TAG)
			{
				String namespace = parser.getNamespace(null);
				
				ExtensionParser xparser = xmppParser.getExtensionParser(elementName, namespace);
				if (xparser != null)
				{
					PacketExtension packetX = xparser.parseExtension(parser, xmppParser);
					privateData.setPrivateDataExtension(packetX);
				}
				else
				{
					PacketExtension packetX = xmppParser.parseUnknownExtension(parser, elementName, namespace);
					privateData.setPrivateDataExtension(packetX);
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
		return privateData;
	}

}
