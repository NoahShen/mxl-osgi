package net.sf.mxlosgi.mxlosgivcardbundle.parser;

import org.xmlpull.v1.XmlPullParser;

import net.sf.mxlosgi.mxlosgivcardbundle.VCardTempUpdatePacketExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser;
import net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser;

/**
 * @author noah
 *
 */
public class VCardTempUpdateExtensionParser implements ExtensionParser
{
	
	public VCardTempUpdateExtensionParser()
	{
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlmainbundle.ExtensionParser#getElementName()
	 */
	@Override
	public String getElementName()
	{
		return "x";
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlmainbundle.ExtensionParser#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return "vcard-temp:x:update";
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlmainbundle.ExtensionParser#parseExtension(java.lang.Object)
	 */
	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XMPPParser xmppParser) throws Exception
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