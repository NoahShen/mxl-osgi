/**
 * 
 */
package net.sf.mxlosgi.core.listener;

import net.sf.mxlosgi.core.XMPPConnection;


/**
 * @author noah
 *
 */
public interface XMLStringListener
{
	/**
	 * 
	 * @param connection
	 * @param xml
	 */
	public void processXMLString(XMPPConnection connection, String xml);
}
