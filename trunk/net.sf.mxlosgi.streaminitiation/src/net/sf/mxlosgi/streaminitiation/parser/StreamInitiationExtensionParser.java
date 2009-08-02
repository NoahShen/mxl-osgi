package net.sf.mxlosgi.streaminitiation.parser;

import java.io.IOException;

import net.sf.mxlosgi.dataform.DataForm;
import net.sf.mxlosgi.streaminitiation.StreamInitiation;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


/**
 * @author noah
 * 
 */
public class StreamInitiationExtensionParser implements ExtensionParser
{

	public static final String ELEMENTNAME = "si";

	public static final String NAMESPACE = "http://jabber.org/protocol/si";
	
	public StreamInitiationExtensionParser()
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
		StreamInitiation streamInitiation = new StreamInitiation();

		boolean done = false;

		String id = parser.getAttributeValue("", "id");
		String mimeType = parser.getAttributeValue("", "mime-type");
		streamInitiation.setId(id == null ? StreamInitiation.ID_NOT_AVAILABLE : id);
		streamInitiation.setMimeType(mimeType);

		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			if (eventType == XmlPullParser.START_TAG)
			{
				if ("file".equals(elementName))
				{
					streamInitiation.setFile(parseFile(parser));
				}
				else if ("feature".equals(elementName))
				{
					streamInitiation.setFeatureForm(parseDataForm(parser, xmppParser));
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

		return streamInitiation;
	}

	private DataForm parseDataForm(XmlPullParser parser, XmppParser xmppParser) throws Exception
	{

		boolean done = false;

		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			if (eventType == XmlPullParser.START_TAG)
			{
				if ("x".equals(elementName))
				{
					String namespace = parser.getNamespace(null);
					ExtensionParser extensionParser = xmppParser.getExtensionParser("x", namespace);
					if (extensionParser != null)
					{
						return (DataForm) extensionParser.parseExtension(parser, xmppParser);
					}
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("feature".equals(elementName))
				{
					done = true;
				}
			}
		}
		return null;
	}

	private StreamInitiation.XMPPFile parseFile(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		String name = parser.getAttributeValue("", "name");
		String size = parser.getAttributeValue("", "size");
		String date = parser.getAttributeValue("", "date");
		String hash = parser.getAttributeValue("", "hash");

		StreamInitiation.XMPPFile file = null;
		if (size != null && !size.isEmpty())
		{
			file = new StreamInitiation.XMPPFile(name, Long.parseLong(size));
		}
		else
		{
			file = new StreamInitiation.XMPPFile(name);
		}

		file.setDate(date);
		file.setHash(hash);

		boolean done = false;

		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();

			if (eventType == XmlPullParser.START_TAG)
			{
				if ("desc".equals(elementName))
				{
					file.setDesc(parser.nextText());
				}
				else if ("range".equals(elementName))
				{
					file.setRanged(true);
					String offset = parser.getAttributeValue("", "offset");
					String length = parser.getAttributeValue("", "length");
					if (offset != null && !offset.isEmpty())
					{
						file.setOffset(Long.parseLong(offset));
					}
					if (length != null && !length.isEmpty())
					{
						file.setLength(Long.parseLong(length));
					}
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("file".equals(elementName))
				{
					done = true;
				}
			}
		}

		return file;
	}
}