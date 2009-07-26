/**
 * 
 */
package net.sf.mxlosgi.chatstate.parser;

import net.sf.mxlosgi.chatstate.ChatStateExtension;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.v1.XmlPullParser;

/**
 * @author noah
 *
 */
public class ComposingChatStateExtensionParser implements ExtensionParser
{

	public static final String ELEMENTNAME = "composing";
	
	public static final String NAMESPACE = "http://jabber.org/protocol/chatstates";
	

	@Override
	public String getElementName()
	{
		return ELEMENTNAME;
	}


	@Override
	public String getNamespace()
	{
		return NAMESPACE;
	}


	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XmppParser xmppParser) throws Exception
	{
		ChatStateExtension chatstate = new ChatStateExtension(ChatStateExtension.ChatState.composing);

		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			if (eventType == XmlPullParser.END_TAG)
			{
				if (getElementName().equals(parser.getName()))
				{
					done = true;
				}
			}
		}

		return chatstate;
	}
}
