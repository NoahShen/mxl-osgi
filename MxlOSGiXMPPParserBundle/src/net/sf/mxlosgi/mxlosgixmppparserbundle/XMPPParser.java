/**
 * 
 */
package net.sf.mxlosgi.mxlosgixmppparserbundle;

import java.util.Collection;

import org.xmlpull.v1.XmlPullParser;

import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;


/**
 * @author noah
 *
 */
public interface XMPPParser
{
	/**
	 * 
	 * @param parser
	 */
	public void addExtensionParser(ExtensionParser parser);
	
	/**
	 * 
	 * @param parser
	 */
	public void removeExtensionParser(ExtensionParser parser);
	
	/**
	 * 
	 * @param elementName
	 * @param namespace
	 * @return
	 */
	public ExtensionParser getExtensionParser(String elementName, String namespace);
	
	/**
	 * 
	 * @return
	 */
	public Collection<ExtensionParser> getExtensionParsers();
	
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
