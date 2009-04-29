/**
 * 
 */
package net.sf.mxlosgi.mxlosgimainbundle.listener;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;

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
