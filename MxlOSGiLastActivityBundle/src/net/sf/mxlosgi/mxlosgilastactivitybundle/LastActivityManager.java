package net.sf.mxlosgi.mxlosgilastactivitybundle;

import net.sf.mxlosgi.mxlosgilastactivitybundle.listener.LastActivityListener;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

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
	 * @throws XMPPException
	 */
	public LastActivityPacketExtension getLastActivity(XMPPConnection connection, JID to) throws XMPPException;
	
	/**
	 * 
	 * @param listener
	 * @param idleSecond
	 */
	public void addLastActivityListener(LastActivityListener listener, long idleSecond);
	
	/**
	 * 
	 * @param listener
	 */
	public void removeLastActivityListener(LastActivityListener listener);
}