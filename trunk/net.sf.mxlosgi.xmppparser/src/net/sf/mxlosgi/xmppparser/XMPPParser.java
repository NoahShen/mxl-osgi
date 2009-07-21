/**
 * 
 */
package net.sf.mxlosgi.xmppparser;

import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmpp.XMLStanza;

import org.xmlpull.v1.XmlPullParser;



/**
 * @author noah
 *
 */
public interface XMPPParser
{
	
	/**
	 * 
	 * @param xml
	 * @return
	 * @throws XmlPullParserException
	 */
	public XMLStanza parseXML(String xml) throws Exception;
	
	/**
	 * 
	 * @param parser
	 * @param elementName
	 * @param namespace
	 * @return
	 * @throws Exception
	 */
	public PacketExtension parseUnknownExtension(XmlPullParser parser, String elementName, String namespace) throws Exception;
}
