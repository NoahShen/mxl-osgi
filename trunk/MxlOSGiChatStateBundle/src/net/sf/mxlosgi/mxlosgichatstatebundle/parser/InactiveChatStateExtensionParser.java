package net.sf.mxlosgi.mxlosgichatstatebundle.parser;

import org.xmlpull.v1.XmlPullParser;

import net.sf.mxlosgi.mxlosgichatstatebundle.ChatStateExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppparserbundle.ExtensionParser;
import net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser;

/**
 * @author noah
 *
 */
public class InactiveChatStateExtensionParser implements ExtensionParser
{
	
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
		return "inactive";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlmainbundle.ExtensionParser#getNamespace()
	 */
	@Override
	public String getNamespace()
	{
		return "http://jabber.org/protocol/chatstates";
	}

	@Override
	public PacketExtension parseExtension(XmlPullParser parser, XMPPParser xmppParser) throws Exception
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