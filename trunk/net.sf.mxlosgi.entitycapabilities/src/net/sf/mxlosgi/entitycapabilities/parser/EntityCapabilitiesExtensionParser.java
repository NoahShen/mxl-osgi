/**
 * 
 */
package net.sf.mxlosgi.entitycapabilities.parser;

import net.sf.mxlosgi.entitycapabilities.EntityCapabilitiesExtension;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.v1.XmlPullParser;


/**
 * @author noah
 *
 */
public class EntityCapabilitiesExtensionParser implements ExtensionParser
{
	
	public static final String ELEMENTNAME = "c";
	
	public static final String NAMESPACE = "http://jabber.org/protocol/caps";
	

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
		String ext = parser.getAttributeValue("", "ext");
		String hash = parser.getAttributeValue("", "hash");
		String node = parser.getAttributeValue("", "node");
		String ver = parser.getAttributeValue("", "ver");
		
		EntityCapabilitiesExtension extension = new EntityCapabilitiesExtension(node, ver);

		extension.setExt(ext);
		extension.setHash(hash);
		return extension;
	}

}
