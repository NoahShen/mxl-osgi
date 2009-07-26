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
public class InactiveChatStateExtensionParser implements ExtensionParser
{

	public static final String ELEMENTNAME = "inactive";
	
	public static final String NAMESPACE = "http://jabber.org/protocol/chatstates";
	
	public InactiveChatStateExtensionParser()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlmainbundle.ExtensionParser#getElementName()
	 */
	@Override
	public String getElementName()
	{
		return ELEMENTNAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlmainbundle.ExtensionParser#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return NAMESPACE;
	}

	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XmppParser xmppParser) throws Exception
	{
		ChatStateExtension chatstate = new ChatStateExtension(ChatStateExtension.ChatState.inactive);

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