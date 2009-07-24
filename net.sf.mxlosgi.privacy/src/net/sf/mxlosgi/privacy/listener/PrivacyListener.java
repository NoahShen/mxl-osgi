package net.sf.mxlosgi.privacy.listener;

import net.sf.mxlosgi.core.XmppConnection;



/**
 * @author noah
 *
 */
public interface PrivacyListener
{
	/**
	 * be called when privacyList created or updated
	 * @param connection
	 * @param listName
	 */
	public void privacyListUpdated(XmppConnection connection, String listName);
}