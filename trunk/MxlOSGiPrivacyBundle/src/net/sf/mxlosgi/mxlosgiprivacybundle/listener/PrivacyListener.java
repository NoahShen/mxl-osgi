package net.sf.mxlosgi.mxlosgiprivacybundle.listener;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;


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
	public void privacyListUpdated(XMPPConnection connection, String listName);
}