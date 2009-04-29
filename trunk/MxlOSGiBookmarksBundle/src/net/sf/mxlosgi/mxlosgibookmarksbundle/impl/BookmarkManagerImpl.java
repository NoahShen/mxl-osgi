/**
 * 
 */
package net.sf.mxlosgi.mxlosgibookmarksbundle.impl;


import net.sf.mxlosgi.mxlosgibookmarksbundle.BookmarkManager;
import net.sf.mxlosgi.mxlosgibookmarksbundle.BookmarkedConference;
import net.sf.mxlosgi.mxlosgibookmarksbundle.BookmarkedURL;
import net.sf.mxlosgi.mxlosgibookmarksbundle.BookmarksExtension;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgiprivatedatabundle.PrivateDataManager;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

/**
 * @author noah
 *
 */
public class BookmarkManagerImpl implements BookmarkManager
{
	private PrivateDataManager privateDataManager;
	
	/**
	 * @param privateDataManager
	 */
	public BookmarkManagerImpl(PrivateDataManager privateDataManager)
	{
		this.privateDataManager = privateDataManager;
	}


	@Override
	public void addBookmarkedConference(XMPPConnection connection, 
									String name, 
									JID jid, 
									boolean isAutoJoin, 
									String nickname, 
									String password) throws XMPPException
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
		
		privateDataManager.storePrivateData(connection, bookmarks);
	}

	@Override
	public void addBookmarkedURL(XMPPConnection connection, String url, String name) throws XMPPException
	{
		BookmarksExtension bookmarks = getBookmarks(connection);
		BookmarkedURL bookmarkedURL = bookmarks.getBookmarkedURLByURL(url);
		if (bookmarkedURL == null)
		{
			bookmarkedURL = new BookmarkedURL(url);
			bookmarks.addBookmarkedURL(bookmarkedURL);
		}
		bookmarkedURL.setName(name);
		
		privateDataManager.storePrivateData(connection, bookmarks);
	}

	@Override
	public void removeBookmarkedConference(XMPPConnection connection, JID jid) throws XMPPException
	{
		BookmarksExtension bookmarks = getBookmarks(connection);
		BookmarkedConference conference = bookmarks.getBookmarkedConferenceByJID(jid);
		if (conference != null)
		{
			bookmarks.removeBookmarkedConference(conference);
			privateDataManager.storePrivateData(connection, bookmarks);
		}
	}

	@Override
	public void removeBookmarkedURL(XMPPConnection connection, String bookmarkURL) throws XMPPException
	{
		BookmarksExtension bookmarks = getBookmarks(connection);
		BookmarkedURL bookmarkedURL = bookmarks.getBookmarkedURLByURL(bookmarkURL);
		if (bookmarkedURL != null)
		{
			bookmarks.removeBookmarkedURL(bookmarkedURL);
			privateDataManager.storePrivateData(connection, bookmarks);
		}
	}


	@Override
	public BookmarksExtension getBookmarks(XMPPConnection connection) throws XMPPException
	{
		BookmarksExtension bookmarksExtension = 
			(BookmarksExtension) privateDataManager.getPrivateData(connection, 
														BookmarksExtension.ELEMENTNAME, 
														BookmarksExtension.NAMESPACE);
		return bookmarksExtension;
	}

}
