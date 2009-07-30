/**
 * 
 */
package net.sf.mxlosgi.bookmarks.impl;

import net.sf.mxlosgi.bookmarks.BookmarkManager;
import net.sf.mxlosgi.bookmarks.BookmarkedConference;
import net.sf.mxlosgi.bookmarks.BookmarkedURL;
import net.sf.mxlosgi.bookmarks.BookmarksExtension;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.privatedata.PrivateDataManager;
import net.sf.mxlosgi.xmpp.JID;


/**
 * @author noah
 *
 */
public class BookmarkManagerImpl implements BookmarkManager
{
	private PrivateDataManagerServiceTracker privateDataManagerServiceTracker;
	
	/**
	 * 
	 * @param privateDataManagerServiceTracker
	 */
	public BookmarkManagerImpl(PrivateDataManagerServiceTracker privateDataManagerServiceTracker)
	{
		this.privateDataManagerServiceTracker = privateDataManagerServiceTracker;
	}


	@Override
	public void addBookmarkedConference(XmppConnection connection, 
									String name, 
									JID jid, 
									boolean isAutoJoin, 
									String nickname, 
									String password) throws XmppException
	{
		BookmarksExtension bookmarks = getBookmarks(connection);
		BookmarkedConference conference = bookmarks.getBookmarkedConferenceByJID(jid);
		if (conference == null)
		{
			conference = new BookmarkedConference(jid);
			bookmarks.addBookmarkedConference(conference);
		}
		conference.setName(name);
		conference.setAutoJoin(isAutoJoin);
		conference.setNickname(nickname);
		conference.setPassword(password);
		
		PrivateDataManager privateDataManager = privateDataManagerServiceTracker.getPrivateDataManager();
		privateDataManager.storePrivateData(connection, bookmarks);
	}

	@Override
	public void addBookmarkedURL(XmppConnection connection, String url, String name) throws XmppException
	{
		BookmarksExtension bookmarks = getBookmarks(connection);
		BookmarkedURL bookmarkedURL = bookmarks.getBookmarkedURLByURL(url);
		if (bookmarkedURL == null)
		{
			bookmarkedURL = new BookmarkedURL(url);
			bookmarks.addBookmarkedURL(bookmarkedURL);
		}
		bookmarkedURL.setName(name);
		
		PrivateDataManager privateDataManager = privateDataManagerServiceTracker.getPrivateDataManager();
		privateDataManager.storePrivateData(connection, bookmarks);
	}

	@Override
	public void removeBookmarkedConference(XmppConnection connection, JID jid) throws XmppException
	{
		BookmarksExtension bookmarks = getBookmarks(connection);
		BookmarkedConference conference = bookmarks.getBookmarkedConferenceByJID(jid);
		if (conference != null)
		{
			bookmarks.removeBookmarkedConference(conference);
			
			PrivateDataManager privateDataManager = privateDataManagerServiceTracker.getPrivateDataManager();
			privateDataManager.storePrivateData(connection, bookmarks);
		}
	}

	@Override
	public void removeBookmarkedURL(XmppConnection connection, String bookmarkURL) throws XmppException
	{
		BookmarksExtension bookmarks = getBookmarks(connection);
		BookmarkedURL bookmarkedURL = bookmarks.getBookmarkedURLByURL(bookmarkURL);
		if (bookmarkedURL != null)
		{
			bookmarks.removeBookmarkedURL(bookmarkedURL);
			
			PrivateDataManager privateDataManager = privateDataManagerServiceTracker.getPrivateDataManager();
			privateDataManager.storePrivateData(connection, bookmarks);
		}
	}


	@Override
	public BookmarksExtension getBookmarks(XmppConnection connection) throws XmppException
	{
		PrivateDataManager privateDataManager = privateDataManagerServiceTracker.getPrivateDataManager();
		BookmarksExtension bookmarksExtension = 
			(BookmarksExtension) privateDataManager.getPrivateData(connection, 
														BookmarksExtension.ELEMENTNAME, 
														BookmarksExtension.NAMESPACE);
		return bookmarksExtension;
	}

}
