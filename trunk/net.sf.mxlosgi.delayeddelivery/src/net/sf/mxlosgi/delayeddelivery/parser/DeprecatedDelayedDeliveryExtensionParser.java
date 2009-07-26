package net.sf.mxlosgi.delayeddelivery.parser;

import net.sf.mxlosgi.delayeddelivery.DeprecatedDelayedDeliveryPacketExtension;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.v1.XmlPullParser;


/**
 * @author noah
 * 
 */
public class DeprecatedDelayedDeliveryExtensionParser implements ExtensionParser
{

	
	public static final String ELEMENTNAME = "x";
	
	public static final String NAMESPACE = "jabber:x:delay";
	
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
		return ELEMENTNAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlmainbundle.ExtensionParser#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return NAMESPACE;
	}


	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XmppParser xmppParser) throws Exception
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