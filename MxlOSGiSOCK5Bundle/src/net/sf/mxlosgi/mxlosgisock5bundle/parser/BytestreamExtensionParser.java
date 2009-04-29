package net.sf.mxlosgi.mxlosgisock5bundle.parser;

import net.sf.mxlosgi.mxlosgisock5bundle.BytestreamPacketExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser;
import net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser;

import org.xmlpull.v1.XmlPullParser;

/**
 * @author noah
 * 
 */
public class BytestreamExtensionParser implements ExtensionParser
{

	public BytestreamExtensionParser()
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
		return "query";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlmainbundle.ExtensionParser#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return "http://jabber.org/protocol/bytestreams";
	}

	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XMPPParser xmppParser) throws Exception
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