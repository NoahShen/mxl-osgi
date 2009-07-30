/**
 * 
 */
package net.sf.mxlosgi.bookmarks.parser;

import java.io.IOException;

import net.sf.mxlosgi.bookmarks.BookmarkedConference;
import net.sf.mxlosgi.bookmarks.BookmarkedURL;
import net.sf.mxlosgi.bookmarks.BookmarksExtension;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author noah
 * 
 */
public class BookmarksExtensionParser implements ExtensionParser
{

	public static final String ELEMENTNAME = "storage";
	
	public static final String NAMESPACE = "storage:bookmarks";
	
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
