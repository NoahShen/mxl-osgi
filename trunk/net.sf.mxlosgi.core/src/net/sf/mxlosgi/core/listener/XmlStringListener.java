/**
 * 
 */
package net.sf.mxlosgi.core.listener;

import net.sf.mxlosgi.core.XmppConnection;


/**
 * @author noah
 *
 */
public interface XmlStringListener
{
	/**
	 * 
	 * @param connection
	 * @param xml
	 */
	public void processXmlString(XmppConnection connection, String xml);
}
