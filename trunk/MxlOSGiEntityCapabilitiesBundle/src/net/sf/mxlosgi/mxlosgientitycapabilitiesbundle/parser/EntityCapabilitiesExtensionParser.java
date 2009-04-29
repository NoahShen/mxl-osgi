/**
 * 
 */
package net.sf.mxlosgi.mxlosgientitycapabilitiesbundle.parser;

import org.xmlpull.v1.XmlPullParser;

import net.sf.mxlosgi.mxlosgientitycapabilitiesbundle.EntityCapabilitiesExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser;
import net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser;

/**
 * @author noah
 *
 */
public class EntityCapabilitiesExtensionParser implements ExtensionParser
{

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser#getElementName()
	 */
	@Override
	public String getElementName()
	{
		return "c";
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return "http://jabber.org/protocol/caps";
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser#parseExtension(org.xmlpull.v1.XmlPullParser, net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser)
	 */
	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XMPPParser xmppParser) throws Exception
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
