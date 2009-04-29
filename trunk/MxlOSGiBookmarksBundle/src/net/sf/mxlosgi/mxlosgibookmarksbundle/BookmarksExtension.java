/**
 * 
 */
package net.sf.mxlosgi.mxlosgibookmarksbundle;

import java.util.ArrayList;
import java.util.List;

import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;

/**
 * @author noah
 *
 */
public class BookmarksExtension implements PacketExtension
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3746379162896688693L;

	public static final String ELEMENTNAME = "storage";
	
	public static final String NAMESPACE = "storage:bookmarks";
	
	private List<BookmarkedConference> bookmarkedConferences = new ArrayList<BookmarkedConference>();
	
	private List<BookmarkedURL> bookmarkedURLs = new ArrayList<BookmarkedURL>();
	
	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension#getElementName()
	 */
	@Override
	public String getElementName()
	{
		return ELEMENTNAME;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return NAMESPACE;
	}

	public void addBookmarkedConference(BookmarkedConference conference)
	{
		bookmarkedConferences.add(conference);
	}
	
	public void removeBookmarkedConference(BookmarkedConference conference)
	{
		bookmarkedConferences.remove(conference);
	}
	
	public BookmarkedConference[] getBookmarkedConferences()
	{
		return bookmarkedConferences.toArray(new BookmarkedConference[]{});
	}
	
	public boolean containBookmarkedConference(JID jid)
	{
		for (BookmarkedConference conference : getBookmarkedConferences())
		{
			if (jid.equals(conference.getJid()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public BookmarkedConference getBookmarkedConferenceByJID(JID jid)
	{
		for (BookmarkedConference conference : getBookmarkedConferences())
		{
			if (jid.equals(conference.getJid()))
			{
				return conference;
			}
		}
		
		return null;
	}
	
	public void addBookmarkedURL(BookmarkedURL url)
	{
		bookmarkedURLs.add(url);
	}
	
	public void removeBookmarkedURL(BookmarkedURL url)
	{
		bookmarkedURLs.remove(url);
	}
	
	public BookmarkedURL[] getBookmarkedURLs()
	{
		return bookmarkedURLs.toArray(new BookmarkedURL[]{});
	}
	
	public boolean containBookmarkedURL(String url)
	{
		for (BookmarkedURL bookmarkedURL : getBookmarkedURLs())
		{
			if (url.equals(bookmarkedURL.getUrl()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public BookmarkedURL getBookmarkedURLByURL(String url)
	{
		for (BookmarkedURL bookmarkedURL : getBookmarkedURLs())
		{
			if (url.equals(bookmarkedURL.getUrl()))
			{
				return bookmarkedURL;
			}
		}
		
		return null;
	}
	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza#toXML()
	 */
	@Override
	public String toXML()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\"");
		if (bookmarkedConferences.isEmpty() && bookmarkedURLs.isEmpty())
		{
			buf.append("/>");
		}
		else
		{
			buf.append(">");
			for (BookmarkedConference conference : getBookmarkedConferences())
			{
				buf.append(conference.toXML());
			}
			for (BookmarkedURL url : getBookmarkedURLs())
			{
				buf.append(url.toXML());
			}
			buf.append("</").append(getElementName()).append(">");
		}
		
		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		BookmarksExtension extension = (BookmarksExtension) super.clone();
		extension.bookmarkedConferences = new ArrayList<BookmarkedConference>();
		extension.bookmarkedURLs = new ArrayList<BookmarkedURL>();
		for (BookmarkedConference conference : getBookmarkedConferences())
		{
			extension.bookmarkedConferences.add((BookmarkedConference) conference.clone());
		}
		for (BookmarkedURL url : getBookmarkedURLs())
		{
			extension.bookmarkedURLs.add((BookmarkedURL) url.clone());
		}
		return extension;
	}
	
}
