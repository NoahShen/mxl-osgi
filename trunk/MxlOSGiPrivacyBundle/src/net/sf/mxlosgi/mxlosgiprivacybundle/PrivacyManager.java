package net.sf.mxlosgi.mxlosgiprivacybundle;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgiprivacybundle.listener.PrivacyListener;
import net.sf.mxlosgi.mxlosgixmppbundle.Privacy;
import net.sf.mxlosgi.mxlosgixmppbundle.PrivacyList;

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
	 * @throws XMPPException
	 */
	public PrivacyList getPrivacyList(XMPPConnection connection, String listName) throws XMPPException;

	/**
	 * get all privacylist's name
	 * @param connection
	 * @return
	 * @throws XMPPException
	 */
	public Privacy getPrivacyLists(XMPPConnection connection) throws XMPPException;

	/**
	 * @param connection
	 * @param listName
	 * @throws XMPPException
	 */
	public void setActiveListName(XMPPConnection connection, String listName) throws XMPPException;

	/**
	 * @param connection
	 * @throws XMPPException
	 */
	public void declineActiveList(XMPPConnection connection) throws XMPPException;

	/**
	 * @param connection
	 * @param listName
	 * @throws XMPPException
	 */
	public void setDefaultListName(XMPPConnection connection, String listName) throws XMPPException;

	/**
	 * @param connection
	 * @throws XMPPException
	 */
	public void declineDefaultList(XMPPConnection connection) throws XMPPException;

	/**
	 * @param connection
	 * @param privacyList
	 * @throws XMPPException
	 */
	public void createOrUpdatePrivacyList(XMPPConnection connection, PrivacyList privacyList) throws XMPPException;

	/**
	 * @param connection
	 * @param listName
	 * @throws XMPPException
	 */
	public void deletePrivacyList(XMPPConnection connection, String listName) throws XMPPException;
	
	/**
	 * 
	 * @param privacyListener
	 */
	public void addPrivacyListener(PrivacyListener privacyListener);
	
	/**
	 * 
	 * @param privacyListener
	 */
	public void removePrivacyListener(PrivacyListener privacyListener);
}