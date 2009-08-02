package net.sf.mxlosgi.sock5.parser;


import net.sf.mxlosgi.sock5.BytestreamPacketExtension;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.v1.XmlPullParser;

/**
 * @author noah
 * 
 */
public class BytestreamExtensionParser implements ExtensionParser
{
	public static final String ELEMENTNAME = "query";

	public static final String NAMESPACE = "http://jabber.org/protocol/bytestreams";
	
	public BytestreamExtensionParser()
	{
	}


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
		BytestreamPacketExtension bytestream = new BytestreamPacketExtension();

		boolean done = false;
		String sid = parser.getAttributeValue("", "sid");
		bytestream.setStreamID(sid);

		String strMode = parser.getAttributeValue("", "mode");
		if (strMode != null && !strMode.isEmpty())
		{
			BytestreamPacketExtension.Mode mode = BytestreamPacketExtension.Mode.fromName(strMode);
			bytestream.setMode(mode);
		}

		while (!done)
		{
			int eventType = parser.next();

			if (eventType == XmlPullParser.START_TAG)
			{
				if ("streamhost".equals(parser.getName()))
				{

					String jidStr = parser.getAttributeValue("", "jid");
					JID jid = new JID(jidStr);
					String host = parser.getAttributeValue("", "host");
					BytestreamPacketExtension.StreamHost streamHost = 
							new BytestreamPacketExtension.StreamHost(jid, host);

					String strPort = parser.getAttributeValue("", "port");
					if (strPort != null && !strPort.isEmpty())
					{
						streamHost.setPort(Integer.parseInt(strPort));
					}

					bytestream.addStreamHost(streamHost);
				}
				else if ("streamhost-used".equals(parser.getName()))
				{
					String jid = parser.getAttributeValue("", "jid");
					bytestream.setUsedHost(new JID(jid));
				}
				else if ("activate".equals(parser.getName()))
				{
					String targetID = parser.nextText();
					bytestream.setActivate(targetID);
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if (getElementName().equals(parser.getName()))
				{
					done = true;
				}
			}
		}

		return bytestream;
	}

}