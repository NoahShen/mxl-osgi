package net.sf.mxlosgi.lastactivity;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.xmpp.JID;


/**
 * @author noah
 *
 */
public interface LastActivityManager
{
	/**
	 * 
	 * @param connection
	 * @param to
	 * @return
	 * @throws XmppException
	 */
	public LastActivityPacketExtension getLastActivity(XmppConnection connection, JID to) throws XmppException;
	
	/**
	 * 
	 * @return
	 */
	public long getIdleTime();
}