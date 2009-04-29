/**
 * 
 */
package net.sf.mxlosgi.mxlosgidiscobundle.parser;

import org.xmlpull.v1.XmlPullParser;

import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoPacketExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser;
import net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser;

/**
 * @author noah
 *
 */
public class DiscoInfoExtensionParser implements ExtensionParser
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
		return "http://jabber.org/protocol/disco#info";
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser#parseExtension(org.xmlpull.v1.XmlPullParser, net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser)
	 */
	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XMPPParser xmppParser) throws Exception
	{
		DiscoInfoPacketExtension discoInfo = new DiscoInfoPacketExtension();
		String node = parser.getAttributeValue("", "node");
		discoInfo.setNode(node);
		
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			
			if (eventType == XmlPullParser.START_TAG)
			{
				String namespace = parser.getNamespace(null);
				if ("identity".equals(elementName))
				{
					String category = parser.getAttributeValue("", "category");
					String type = parser.getAttributeValue("", "type");
					String name = parser.getAttributeValue("", "name");
					discoInfo.addIdentity(category, type, name);
				}
				else if ("feature".equals(elementName))
				{
					String strfeature = parser.getAttributeValue("", "var");
					DiscoInfoPacketExtension.Feature feature = new DiscoInfoPacketExtension.Feature(strfeature);
					discoInfo.addFeature(feature);
				}
				else
				{
					ExtensionParser xparser = xmppParser.getExtensionParser(elementName, namespace);
					if (xparser != null)
					{
						PacketExtension packetX = xparser.parseExtension(parser, xmppParser);
						discoInfo.addExtension(packetX);
					}
					else
					{
						PacketExtension packetX = xmppParser.parseUnknownExtension(parser, elementName, namespace);
						discoInfo.addExtension(packetX);
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
		return discoInfo;
	}

}
