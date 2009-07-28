/**
 * 
 */
package net.sf.mxlosgi.registration.parser;

import net.sf.mxlosgi.registration.RegisterExtension;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.v1.XmlPullParser;


/**
 * @author noah
 *
 */
public class RegisterExtensionParser implements ExtensionParser
{
	
	public static final String ELEMENTNAME = "query";

	public static final String NAMESPACE = "jabber:iq:register";

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
		RegisterExtension registerExtension = new RegisterExtension();

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
					registerExtension.setInstructions(parser.nextText());
				}
				else if (RegisterExtension.NAMESPACE.equals(namespace))
				{
					registerExtension.getFields().put(elementName, parser.nextText());
				}
				else
				{
					ExtensionParser xparser = xmppParser.getExtensionParser(elementName, namespace);
					if (xparser != null)
					{
						PacketExtension packetX = xparser.parseExtension(parser, xmppParser);
						registerExtension.setExtension(packetX);
					}
					else
					{
						PacketExtension packetX = xmppParser.parseUnknownExtension(parser, elementName, namespace);
						registerExtension.setExtension(packetX);
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

		return registerExtension;
	}

}
