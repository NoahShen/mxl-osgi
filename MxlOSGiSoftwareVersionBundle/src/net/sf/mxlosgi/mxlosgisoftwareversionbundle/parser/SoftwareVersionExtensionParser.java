package net.sf.mxlosgi.mxlosgisoftwareversionbundle.parser;

import org.xmlpull.v1.XmlPullParser;

import net.sf.mxlosgi.mxlosgisoftwareversionbundle.SoftwareVersionExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser;
import net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser;

/**
 * @author noah
 * 
 */
public class SoftwareVersionExtensionParser implements ExtensionParser
{

	public SoftwareVersionExtensionParser()
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
		return "jabber:iq:version";
	}

	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XMPPParser xmppParser) throws Exception
	{
		SoftwareVersionExtension sv = new SoftwareVersionExtension();

		boolean done = false;

		while (!done)
		{
			int eventType = parser.next();

			if (eventType == XmlPullParser.START_TAG)
			{
				if ("name".equals(parser.getName()))
				{
					sv.setName(parser.nextText());
				}
				else if ("version".equals(parser.getName()))
				{
					sv.setVersion(parser.nextText());
				}
				else if ("os".equals(parser.getName()))
				{
					sv.setOs(parser.nextText());
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
		
		return sv;
	}

}