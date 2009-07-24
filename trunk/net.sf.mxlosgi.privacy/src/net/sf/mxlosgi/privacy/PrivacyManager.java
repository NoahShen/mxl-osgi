package net.sf.mxlosgi.privacy;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.xmpp.Privacy;
import net.sf.mxlosgi.xmpp.PrivacyList;


/**
 * @author noah
 * 
 */
public interface PrivacyManager
{
	/**
	 * 
	 * @param connection
	 * @param listName
	 * @return
	 * @throws XmppException
	 */
	public PrivacyList getPrivacyList(XmppConnection connection, String listName) throws XmppException;

	/**
	 * get all privacylist's name
	 * @param connection
	 * @return
	 * @throws XmppException
	 */
	public Privacy getPrivacyLists(XmppConnection connection) throws XmppException;

	/**
	 * @param connection
	 * @param listName
	 * @throws XmppException
	 */
	public void setActiveListName(XmppConnection connection, String listName) throws XmppException;

	/**
	 * @param connection
	 * @throws XmppException
	 */
	public void declineActiveList(XmppConnection connection) throws XmppException;

	/**
	 * @param connection
	 * @param listName
	 * @throws XmppException
	 */
	public void setDefaultListName(XmppConnection connection, String listName) throws XmppException;

	/**
	 * @param connection
	 * @throws XmppException
	 */
	public void declineDefaultList(XmppConnection connection) throws XmppException;

	/**
	 * @param connection
	 * @param privacyList
	 * @throws XmppException
	 */
	public void createOrUpdatePrivacyList(XmppConnection connection, PrivacyList privacyList) throws XmppException;

	/**
	 * @param connection
	 * @param listName
	 * @throws XmppException
	 */
	public void deletePrivacyList(XmppConnection connection, String listName) throws XmppException;
}