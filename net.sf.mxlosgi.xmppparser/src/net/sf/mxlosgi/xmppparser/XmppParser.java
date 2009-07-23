/**
 * 
 */
package net.sf.mxlosgi.xmppparser;

import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmpp.XmlStanza;

import org.xmlpull.v1.XmlPullParser;



/**
 * @author noah
 *
 */
public interface XmppParser
{
	
	/**
	 * 
	 * @param xml
	 * @return
	 * @throws XmlPullParserException
	 */
	public XmlStanza parseXML(String xml) throws Exception;
	
	/**
	 * 
	 * @param parser
	 * @param elementName
	 * @param namespace
	 * @return
	 * @throws Exception
	 */
	public PacketExtension parseUnknownExtension(XmlPullParser parser, String elementName, String namespace) throws Exception;
	
	/**
	 * 
	 * @param elementName
	 * @param namespace
	 * @return
	 */
	public ExtensionParser getExtensionParser(String elementName, String namespace);
}
