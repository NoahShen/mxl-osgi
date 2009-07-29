package net.sf.mxlosgi.softwareversion.parser;

import net.sf.mxlosgi.softwareversion.SoftwareVersionExtension;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.v1.XmlPullParser;


/**
 * @author noah
 * 
 */
public class SoftwareVersionExtensionParser implements ExtensionParser
{

	
	public static final String ELEMENTNAME = "query";

	public static final String NAMESPACE = "jabber:iq:version";
	
	public SoftwareVersionExtensionParser()
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