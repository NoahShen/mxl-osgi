package net.sf.mxlosgi.mxlosgidelayeddeliverybundle.parser;

import org.xmlpull.v1.XmlPullParser;

import net.sf.mxlosgi.mxlosgidelayeddeliverybundle.DelayedDeliveryPacketExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser;
import net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser;

/**
 * @author noah
 *
 */
public class DelayedDeliveryExtensionParser implements ExtensionParser
{
	
	
	public DelayedDeliveryExtensionParser()
	{

	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlmainbundle.ExtensionParser#getElementName()
	 */
	@Override
	public String getElementName()
	{
		return "delay";
	}

	/* (non-Javadoc)
	 * @see net.sf.mxl.mxlmainbundle.ExtensionParser#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return "urn:xmpp:delay";
	}

	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XMPPParser xmppParser) throws Exception
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