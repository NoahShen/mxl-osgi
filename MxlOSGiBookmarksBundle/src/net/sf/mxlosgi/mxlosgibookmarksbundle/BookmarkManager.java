/**
 * 
 */
package net.sf.mxlosgi.mxlosgibookmarksbundle;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

/**
 * @author noah
 * 
 */
public interface BookmarkManager
{
	/**
	 * Returns all currently bookmarks.
	 * 
	 * @param connection
	 * 
	 * @return returns all currently bookmarks
	 * @throws XMPPException
	 *                   thrown when there was an error retrieving the
	 *                   current bookmarks from the server.
	 * @see BookmarksExtension
	 */
	public BookmarksExtension getBookmarks(XMPPConnection connection) throws XMPPException;

	/**
	 * Adds or updates a conference in the bookmarks.
	 * 
	 * @param connection
	 * 
	 * @param name
	 *                  the name of the conference
	 * @param jid
	 *                  the jid of the conference
	 * @param isAutoJoin
	 *                  whether or not to join this conference
	 *                  automatically on login
	 * @param nickname
	 *                  the nickname to use for the user when joining the
	 *                  conference
	 * @param password
	 *                  the password to use for the user when joining the
	 *                  conference
	 * @throws XMPPException
	 *                   thrown when there is an issue retrieving the
	 *                   current bookmarks from the server.
	 */
	public void addBookmarkedConference(XMPPConnection connection, 
									String name, JID jid, 
									boolean isAutoJoin, 
									String nickname, 
									String password) throws XMPPException;

	/**
	 * Removes a conference from the bookmarks.
	 * 
	 * @param connection
	 * 
	 * @param jid
	 *                  the jid of the conference to be removed.
	 * @throws XMPPException
	 *                   thrown when there is a problem with the
	 *                   connection attempting to retrieve the bookmarks
	 *                   or persist the bookmarks.
	 * @throws IllegalArgumentException
	 *                   thrown when the conference being removed is a
	 *                   shared conference
	 */
	public void removeBookmarkedConference(XMPPConnection connection, JID jid) throws XMPPException;

	/**
	 * Adds a new url or updates an already existing url in the bookmarks.
	 * 
	 * @param connection
	 * 
	 * @param url
	 *                  the url of the bookmark
	 * @param name
	 *                  the name of the bookmark
	 * @throws XMPPException
	 *                   thrown when there is an error retriving or saving
	 *                   bookmarks from or to the server
	 */
	public void addBookmarkedURL(XMPPConnection connection, String url, String name) throws XMPPException;

	/**
	 * Removes a url from the bookmarks.
	 * 
	 * @param connection
	 * 
	 * @param bookmarkURL
	 *                  the url of the bookmark to remove
	 * @throws XMPPException
	 *                   thrown if there is an error retriving or saving
	 *                   bookmarks from or to the server.
	 */
	public void removeBookmarkedURL(XMPPConnection connection, String bookmarkURL) throws XMPPException;
}
