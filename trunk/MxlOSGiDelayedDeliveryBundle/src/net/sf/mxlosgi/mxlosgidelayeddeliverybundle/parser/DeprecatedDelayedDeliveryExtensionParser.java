package net.sf.mxlosgi.mxlosgidelayeddeliverybundle.parser;

import org.xmlpull.v1.XmlPullParser;

import net.sf.mxlosgi.mxlosgidelayeddeliverybundle.DeprecatedDelayedDeliveryPacketExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser;
import net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser;

/**
 * @author noah
 * 
 */
public class DeprecatedDelayedDeliveryExtensionParser implements ExtensionParser
{

	public DeprecatedDelayedDeliveryExtensionParser()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlmainbundle.ExtensionParser#getElementName()
	 */
	@Override
	public String getElementName()
	{
		return "x";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlmainbundle.ExtensionParser#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return "jabber:x:delay";
	}


	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XMPPParser xmppParser) throws Exception
	{
		DeprecatedDelayedDeliveryPacketExtension ddde = new DeprecatedDelayedDeliveryPacketExtension();

		String from = parser.getAttributeValue("", "from");
		if (from != null && !from.isEmpty())
		{
			ddde.setFrom(new JID(from));
		}
		String stamp = parser.getAttributeValue("", "stamp");
		ddde.setStamp(stamp);

		String reason = parser.nextText();
		ddde.setReason(reason);

		return ddde;
	}

}