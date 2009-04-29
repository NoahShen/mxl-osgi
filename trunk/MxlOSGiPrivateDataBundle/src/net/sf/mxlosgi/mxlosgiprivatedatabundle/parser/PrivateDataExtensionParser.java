/**
 * 
 */
package net.sf.mxlosgi.mxlosgiprivatedatabundle.parser;

import org.xmlpull.v1.XmlPullParser;

import net.sf.mxlosgi.mxlosgiprivatedatabundle.PrivateDataExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser;
import net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser;

/**
 * @author noah
 *
 */
public class PrivateDataExtensionParser implements ExtensionParser
{

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser#getElementName()
	 */
	@Override
	public String getElementName()
	{
		return "query";
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return "jabber:iq:private";
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser#parseExtension(org.xmlpull.v1.XmlPullParser, net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser)
	 */
	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XMPPParser xmppParser) throws Exception
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
