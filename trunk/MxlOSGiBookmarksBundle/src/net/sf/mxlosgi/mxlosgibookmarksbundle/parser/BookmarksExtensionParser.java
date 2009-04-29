/**
 * 
 */
package net.sf.mxlosgi.mxlosgibookmarksbundle.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import net.sf.mxlosgi.mxlosgibookmarksbundle.BookmarkedConference;
import net.sf.mxlosgi.mxlosgibookmarksbundle.BookmarkedURL;
import net.sf.mxlosgi.mxlosgibookmarksbundle.BookmarksExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser;
import net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser;

/**
 * @author noah
 * 
 */
public class BookmarksExtensionParser implements ExtensionParser
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser#getElementName()
	 */
	@Override
	public String getElementName()
	{
		return "storage";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return "storage:bookmarks";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser#parseExtension(org.xmlpull.v1.XmlPullParser,
	 *      net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser)
	 */
	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XMPPParser xmppParser) throws Exception
	{
		BookmarksExtension extension = new BookmarksExtension();
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			if (eventType == XmlPullParser.START_TAG)
			{
				if ("conference".equals(elementName))
				{
					BookmarkedConference conference = getConferenceStorage(parser);
					extension.addBookmarkedConference(conference);
				}
				else if ("url".equals(elementName))
				{
					BookmarkedURL url = getURLStorage(parser);
					if (url != null)
					{
						extension.addBookmarkedURL(url);
					}
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

		return extension;
	}

	private BookmarkedURL getURLStorage(XmlPullParser parser) throws IOException, XmlPullParserException
	{
		String name = parser.getAttributeValue("", "name");
		String url = parser.getAttributeValue("", "url");

		BookmarkedURL bookmarkedURL = new BookmarkedURL(name, url);
		return bookmarkedURL;
	}

	private BookmarkedConference getConferenceStorage(XmlPullParser parser) throws Exception
	{
		String name = parser.getAttributeValue("", "name");
		String autojoin = parser.getAttributeValue("", "autojoin");
		String jid = parser.getAttributeValue("", "jid");

		BookmarkedConference conference = new BookmarkedConference(new JID(jid));
		conference.setName(name);
		conference.setAutoJoin(Boolean.valueOf(autojoin).booleanValue());

		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			if (eventType == XmlPullParser.START_TAG)
			{
				if ("nick".equals(elementName))
				{
					conference.setNickname(parser.nextText());
				}
				else if ("password".equals(elementName))
				{
					conference.setPassword(parser.nextText());
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("conference".equals(elementName))
				{
					done = true;
				}
			}
		}

		return conference;
	}
}
