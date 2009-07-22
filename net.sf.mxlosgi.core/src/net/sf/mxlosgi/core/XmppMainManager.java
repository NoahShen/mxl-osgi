/**
 * 
 */
package net.sf.mxlosgi.core;

import net.sf.mxlosgi.xmpp.JID;


/**
 * @author noah
 * 
 */
public interface XmppMainManager
{
	/**
	 * 
	 * @param serviceName
	 * @return
	 */
	public XmppConnection createConnection(String serviceName);

	/**
	 * 
	 * @param config
	 * @return
	 */
	public XmppConnection createConnection(ConnectionConfig config);

	/**
	 * 
	 * @return
	 */
	public XmppConnection[] getAllConnections();
	
	/**
	 * 
	 * @param serviceName
	 * @return
	 */
	public XmppConnection[] getConnections(String serviceName);
	
	/**
	 * 
	 * @param jid
	 * @return
	 */
	public XmppConnection getConnections(JID jid);
}
