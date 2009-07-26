package net.sf.mxlosgi.delayeddelivery.parser;

import net.sf.mxlosgi.delayeddelivery.DelayedDeliveryPacketExtension;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.v1.XmlPullParser;


/**
 * @author noah
 *
 */
public class DelayedDeliveryExtensionParser implements ExtensionParser
{
	
	public static final String ELEMENTNAME = "delay";
	
	public static final String NAMESPACE = "urn:xmpp:delay";
	
	public DelayedDeliveryExtensionParser()
	{

	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlmainbundle.ExtensionParser#getElementName()
	 */
	@Override
	public String getElementName()
	{
		return ELEMENTNAME;
	}

	/* (non-Javadoc)
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
		DelayedDeliveryPacketExtension dde = new DelayedDeliveryPacketExtension();

		String from = parser.getAttributeValue("", "from");
		if (from != null && !from.isEmpty())
		{
			dde.setFrom(new JID(from));
		}
		
		String stamp = parser.getAttributeValue("", "stamp");
		dde.setStamp(stamp);

		String reason = parser.nextText();
		dde.setReason(reason);

//		boolean done = false;
//		while (!done)
//		{
//			int eventType = parser.next();
//			String elementName = parser.getName();
//			if (eventType == XmlPullParser.START_TAG)
//			{
//				
//			}
//			else if (eventType == XmlPullParser.END_TAG)
//			{
//				if (getElementName().equals(elementName))
//				{
//					done = true;
//				}
//			}
//			
//		}
		return dde;
	}

}