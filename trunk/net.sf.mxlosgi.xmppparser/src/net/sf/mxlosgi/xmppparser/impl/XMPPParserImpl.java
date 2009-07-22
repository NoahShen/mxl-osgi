package net.sf.mxlosgi.xmppparser.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


import net.sf.mxlosgi.xmpp.Challenge;
import net.sf.mxlosgi.xmpp.CloseStream;
import net.sf.mxlosgi.xmpp.Compressed;
import net.sf.mxlosgi.xmpp.Failure;
import net.sf.mxlosgi.xmpp.IQ;
import net.sf.mxlosgi.xmpp.IQBind;
import net.sf.mxlosgi.xmpp.IQRoster;
import net.sf.mxlosgi.xmpp.IQSession;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Message;
import net.sf.mxlosgi.xmpp.Packet;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmpp.Presence;
import net.sf.mxlosgi.xmpp.Privacy;
import net.sf.mxlosgi.xmpp.PrivacyItem;
import net.sf.mxlosgi.xmpp.PrivacyList;
import net.sf.mxlosgi.xmpp.Proceed;
import net.sf.mxlosgi.xmpp.Stream;
import net.sf.mxlosgi.xmpp.StreamError;
import net.sf.mxlosgi.xmpp.StreamFeature;
import net.sf.mxlosgi.xmpp.Success;
import net.sf.mxlosgi.xmpp.XMLStanza;
import net.sf.mxlosgi.xmpp.XMPPError;
import net.sf.mxlosgi.xmpp.StreamFeature.Feature;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.UnknownPacketExtension;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


/**
 * @author noah
 *
 */
public class XMPPParserImpl implements XmppParser
{
	private ExtensionParserServiceTracker extensionParserServiceTracker;
	/**
	 * 
	 */
	public XMPPParserImpl(ExtensionParserServiceTracker extensionParserServiceTracker)
	{
		this.extensionParserServiceTracker = extensionParserServiceTracker;
	}


	@Override
	public XMLStanza parseXML(String xml) throws Exception
	{
		XmlPullParser parser = new MXParser();
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
		
		if ("</stream:stream>".equals(xml))
		{
			return new CloseStream();
		}
		
		if (xml.startsWith("<stream:features>") || xml.startsWith("<stream:error>"))
		{
			xml = "<stream:stream xmlns:stream=\"http://etherx.jabber.org/streams\">" + xml;
			StringReader strReader = new StringReader(xml);
			parser.setInput(strReader);
			try
			{
				parser.next();
			}
			catch (Exception e)
			{
				return null;
			}
		}
		else
		{
			StringReader strReader = new StringReader(xml);
			parser.setInput(strReader);
		}
		

		try
		{
			parser.next();
		}
		catch (Exception e)
		{
			return null;
		}

		String elementName = parser.getName();
		if ("stream".equals(elementName) || "stream:stream".equals(elementName))
		{
			return parseStream(parser);
		}
		else if ("features".equals(elementName) || "stream:features".equals(elementName))
		{
			return parseStreamFeature(parser);
		}
		else if ("error".equals(elementName) || "stream:error".equals(elementName))
		{
			return parseStreamError(parser);
		}
//		else if ("starttls".equals(elementName))
//		{
//			StartTLS startTLS = new StartTLS();
//			return startTLS;
//		}
//		else if ("auth".equals(elementName))
//		{
//			String mechanism = parser.getAttributeValue("", "mechanism");
//			String content = parser.nextText();
//			Auth auth = new Auth();
//			auth.setMechanism(mechanism);
//			auth.setContent(content);
//			return auth;
//		}
		else if ("proceed".equals(elementName))
		{
			return new Proceed();
		}
		else if ("challenge".equals(elementName))
		{
			return new Challenge(parser.nextText());
		}
		else if ("success".equals(elementName))
		{
			return new Success();
		}
		else if ("compressed".equals(elementName))
		{
			return new Compressed();
		}
		else if ("failure".equals(elementName))
		{
			return parseFailure(parser);
		}
		else if ("iq".equals(elementName))
		{
			return parseIQ(parser);
		}
		else if ("presence".equals(elementName))
		{
			return parsePresence(parser);
		}
		else if ("message".equals(elementName))
		{
			return parseMessage(parser);
		}

		return null;
	}

	private XMLStanza parseStreamError(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		StreamError streamError = new StreamError();
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			if (eventType == XmlPullParser.START_TAG)
			{
				if ("text".equals(elementName))
				{
					streamError.setText(parser.nextText(), null);
				}
				else
				{
					try
					{
						streamError.setCondition(StreamError.Condition.fromXMPP(elementName));
					}
					catch (Exception e)
					{
						streamError.addApplicationCondition(elementName, parser.getNamespace(null));
					}
				}
				
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("error".equals(elementName) || "stream:error".equals(elementName))
				{
					done = true;
				}
			}
		}
		return streamError;

	}

	private XMLStanza parseFailure(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		String  namespace = parser.getNamespace(null);
		Failure failure = new Failure();
		if ("urn:ietf:params:xml:ns:xmpp-tls".equals(namespace))
		{
			failure.setNamespace(namespace);
		}
		else if ("urn:ietf:params:xml:ns:xmpp-sasl".equals(namespace))
		{
			failure.setNamespace(namespace);
		}
		
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			
			if (eventType == XmlPullParser.START_TAG)
			{
				Failure.Error error = Failure.Error.fromString(elementName);
				failure.setError(error);				
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("failure".equals(elementName))
				{
					done = true;
				}
			}
		}
		
		return failure;
	}

	private XMLStanza parseStreamFeature(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		StreamFeature streamFeature = new StreamFeature();

		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			
			if (eventType == XmlPullParser.START_TAG)
			{
				String namespace = parser.getNamespace(null);
				if ("compression".equals(elementName))
				{
					String compressionMethod = parseCompressionMethod(parser);
					streamFeature.addCompressionMethod(compressionMethod);
				}
				else if ("mechanisms".equals(elementName))
				{
					List<String> mechanisms = parseMechanisms(parser);
					streamFeature.addMechanismCollection(mechanisms);
				}
				else
				{
					StreamFeature.Feature feature = parseFeature(parser, elementName, namespace);
					streamFeature.addFeature(feature);
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("features".equals(elementName) || "stream:features".equals(elementName))
				{
					done = true;
				}
			}
		}
		
		return streamFeature;
	}

	private Feature parseFeature(XmlPullParser parser, String elementName, String namespace) throws XmlPullParserException, IOException
	{
		StreamFeature.Feature feature = new StreamFeature.Feature(elementName, namespace);
		
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String currentElementName = parser.getName();
			
			if (eventType == XmlPullParser.START_TAG)
			{
				if ("required".equals(currentElementName))
				{
					feature.setRequired(true);
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if (elementName.equals(currentElementName))
				{
					done = true;
				}
			}
		}
		return feature;
	}

	private List<String> parseMechanisms(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		List<String> mechanisms = new ArrayList<String>();
		
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			
			if (eventType == XmlPullParser.START_TAG)
			{
				if ("mechanism".equals(elementName))
				{
					mechanisms.add(parser.nextText());
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("mechanisms".equals(elementName))
				{
					done = true;
				}
			}
		}
		return mechanisms;
	}

	private String parseCompressionMethod(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		String compressionMethod = null;
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			
			if (eventType == XmlPullParser.START_TAG)
			{
				if ("method".equals(elementName))
				{
					compressionMethod = parser.nextText();
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("compression".equals(elementName))
				{
					done = true;
				}
			}
		}
		return compressionMethod;
	}

	private XMLStanza parseMessage(XmlPullParser parser) throws Exception
	{
		Message message = new Message();
		
		String id = parser.getAttributeValue("", "id");
		String to = parser.getAttributeValue("", "to");
		String from = parser.getAttributeValue("", "from");
		String language = getLanguageAttribute(parser);
		String strType = parser.getAttributeValue("", "type");
		if (strType != null && !strType.isEmpty())
		{
			Message.Type type = Message.Type.valueOf(strType);
			message.setType(type);
		}
		message.setStanzaID(id == null ? Packet.ID_NOT_AVAILABLE : id);
		
		if (to != null && !to.isEmpty())
		{
			message.setTo(new JID(to));
		}
		if (from != null && !from.isEmpty())
		{
			message.setFrom(new JID(from));
		}
		message.setLanguage(language);
		
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			
			if (eventType == XmlPullParser.START_TAG)
			{
				String namespace = parser.getNamespace(null);
				if ("body".equals(elementName))
				{
					String lang = getLanguageAttribute(parser);
					String body = parser.nextText();
					if (lang != null)
					{
						message.addBody(lang, body);
					}
					else
					{
						message.setBody(body);
					}
				}
				else if ("subject".equals(elementName))
				{
					String lang = getLanguageAttribute(parser);
					String subject = parser.nextText();
					if (lang != null)
					{
						message.addSubject(lang, subject);
					}
					else
					{
						message.setSubject(subject);
					}
				}
				else if ("thread".equals(elementName))
				{
					String thread = parser.nextText();
					message.setThread(thread);
				}
				else if ("error".equals(elementName))
				{
					XMPPError error = parseError(parser);
					message.setError(error);
				}
				else 
				{
					ExtensionParser xparser = extensionParserServiceTracker.getExtensionParser(elementName, namespace);
					if (xparser != null)
					{
						PacketExtension packetX = xparser.parseExtension(parser, this);
						message.addExtension(packetX);
					}
					else
					{
						PacketExtension packetX = parseUnknownExtension(parser, elementName, namespace);
						message.addExtension(packetX);
					}
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("message".equals(elementName))
				{
					done = true;
				}
			}
		}
		
		return message;
	}

	private XMLStanza parsePresence(XmlPullParser parser) throws Exception
	{
		Presence.Type type = Presence.Type.available;
		String strType = parser.getAttributeValue("", "type");
		if (strType != null && !strType.isEmpty())
		{
			type = Presence.Type.valueOf(strType);
		}
		
		Presence presence = new Presence(type);
		String id = parser.getAttributeValue("", "id");
		String to = parser.getAttributeValue("", "to");
		String from = parser.getAttributeValue("", "from");
		String language = getLanguageAttribute(parser);
		
		presence.setStanzaID(id == null ? Packet.ID_NOT_AVAILABLE : id);
		if (to != null && !to.isEmpty())
		{
			presence.setTo(new JID(to));
		}
		if (from != null && !from.isEmpty())
		{
			presence.setFrom(new JID(from));
		}
		presence.setLanguage(language);
		
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			
			if (eventType == XmlPullParser.START_TAG)
			{
				String namespace = parser.getNamespace(null);
				if ("status".equals(elementName))
				{
					presence.setStatus(parser.nextText());
				}
				else if ("show".equals(elementName))
				{
					presence.setShow(Presence.Show.valueOf(parser.nextText()));
				}
				else if ("priority".equals(elementName))
				{
					presence.setPriority(Integer.parseInt(parser.nextText()));
				}
				else if ("error".equals(elementName))
				{
					XMPPError error = parseError(parser);
					presence.setError(error);
				}
				else
				{
					ExtensionParser xparser = extensionParserServiceTracker.getExtensionParser(elementName, namespace);
					if (xparser != null)
					{
						PacketExtension packetX = xparser.parseExtension(parser, this);
						presence.addExtension(packetX);
					}
					else
					{
						PacketExtension packetX = parseUnknownExtension(parser, elementName, namespace);
						presence.addExtension(packetX);
					}
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("presence".equals(elementName))
				{
					done = true;
				}
			}
		}
		
		return presence;
	}

	private IQ parseIQ(XmlPullParser parser) throws Exception
	{
		String to = parser.getAttributeValue("", "to");
		String from = parser.getAttributeValue("", "from");
		String lang = getLanguageAttribute(parser);
		String id = parser.getAttributeValue("", "id");
		String strType = parser.getAttributeValue("", "type");
		
		IQ iq = new IQ();
		if (to != null && !to.isEmpty())
		{
			iq.setTo(new JID(to));
		}
		if (from != null && !from.isEmpty())
		{
			iq.setFrom(new JID(from));
		}
		iq.setLanguage(lang);
		iq.setStanzaID(id == null ? Packet.ID_NOT_AVAILABLE : id);
		iq.setType(IQ.Type.fromString(strType));
		
		
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			
			if (eventType == XmlPullParser.START_TAG)
			{
				String namespace = parser.getNamespace(null);
				
				if ("bind".equals(elementName))
				{
					IQBind bind = parseIQBind(parser);
					iq.addExtension(bind);
				}
				else if ("session".equals(elementName))
				{
					IQSession iqSession = new IQSession();
					iq.addExtension(iqSession);
				}
				else if ("query".equals(elementName) && "jabber:iq:roster".equals(namespace))
				{
					IQRoster iqRoster = parseIQRoster(parser);
					iq.addExtension(iqRoster);
				}
				else if ("query".equals(elementName) && "jabber:iq:privacy".equals(namespace))
				{
					Privacy privacy = parsePrivacy(parser);
					iq.addExtension(privacy);
				}
				else if ("error".equals(elementName))
				{
					XMPPError error = parseError(parser);
					iq.setError(error);
				}
				else
				{
					ExtensionParser xparser = extensionParserServiceTracker.getExtensionParser(elementName, namespace);
					if (xparser != null)
					{
						PacketExtension packetX = xparser.parseExtension(parser, this);
						iq.addExtension(packetX);
					}
					else
					{
						PacketExtension packetX = parseUnknownExtension(parser, elementName, namespace);
						iq.addExtension(packetX);
					}
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("iq".equals(parser.getName()))
				{
					done = true;
				}
			}
		}
		
		return iq;
	}

	private Privacy parsePrivacy(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		Privacy privacy = new Privacy();
		
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG)
			{
				if ("list".equals(parser.getName()))
				{
					PrivacyList privacyList = parsePrivacyList(parser);
					privacy.addPrivacyList(privacyList);
				}
				else if ("active".equals(parser.getName()))
				{
					String name = parser.getAttributeValue("", "name");
					privacy.setActiveName(name);
				}
				else if ("default".equals(parser.getName()))
				{
					String name = parser.getAttributeValue("", "name");
					privacy.setDefaultName(name);
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("query".equals(parser.getName()))
				{
					done = true;
				}
			}
		}
		return privacy;
	}

	private PrivacyList parsePrivacyList(XmlPullParser parser2) throws XmlPullParserException, IOException
	{
		String listName =parser2.getAttributeValue("", "name");
		PrivacyList privacyList = new PrivacyList(listName);
		
		boolean done = false;
		while (!done)
		{
			int eventType = parser2.next();
			if (eventType == XmlPullParser.START_TAG)
			{
				if ("item".equals(parser2.getName()))
				{
					PrivacyItem item = parsePrivacyItem(parser2);
					privacyList.addItem(item);
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("list".equals(parser2.getName()))
				{
					done = true;
				}
			}
		}
		
		return privacyList;
	}
	
	private PrivacyItem parsePrivacyItem(XmlPullParser parser2) throws XmlPullParserException, IOException
	{
		String strType = parser2.getAttributeValue("", "type");
		PrivacyItem.Type type = null;
		if (strType != null && !strType.isEmpty())
		{
			type = PrivacyItem.Type.valueOf(strType);
		}
		
		String value = parser2.getAttributeValue("", "value");
		
		boolean action = false;
		String strAction = parser2.getAttributeValue("", "action");
		if ("allow".equals(strAction))
		{
			action = true;
		}
		
		int order = Integer.parseInt(parser2.getAttributeValue("", "order"));
		PrivacyItem item = new PrivacyItem(type, value, action, order);
		
		boolean done = false;
		while (!done)
		{
			int eventType = parser2.next();
			if (eventType == XmlPullParser.START_TAG)
			{
				if ("message".equals(parser2.getName()))
				{
					item.setFilterMessage(true);
				}
				else if ("presence-in".equals(parser2.getName()))
				{
					item.setFilterPresence_in(true);
				}
				else if ("presence-out".equals(parser2.getName()))
				{
					item.setFilterPresence_out(true);
				}
				else if ("iq".equals(parser2.getName()))
				{
					item.setFilterIQ(true);
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("item".equals(parser2.getName()))
				{
					done = true;
				}
			}
		}
		
		return item;
	}
	
	@Override
	public PacketExtension parseUnknownExtension(XmlPullParser parser, String elementName, String namespace) 
														throws XmlPullParserException, IOException
	{
		
		UnknownPacketExtension packetX = new UnknownPacketExtension(elementName, namespace);
		StringBuffer buf = new StringBuffer();
		
		String prefix = parser.getPrefix();
		if (prefix != null)
		{
			buf.append("<" + prefix + ":" + elementName);
			String prefixNamespace = parser.getNamespace(prefix);
			if (prefixNamespace != null)
			{
				buf.append(" xmlns:" + prefix + "=\"" + prefixNamespace + "\"");
			}
		}
		else
		{
			buf.append("<" + elementName);
		}
		if (namespace != null)
		{
			buf.append(" xmlns=\"" + namespace + "\"");
		}
		
		for (int i = 0; i < parser.getAttributeCount(); ++i)
		{
			buf.append(" " + parser.getAttributeName(i) + "=\"" + parser.getAttributeValue(i) + "\"");
		}
		
		buf.append(">");
		
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String currentElement = parser.getName();
			if (eventType == XmlPullParser.START_TAG)
			{
				String prefix2 = parser.getPrefix();
				String nspace2 = parser.getNamespace(null);
				if (prefix2 != null)
				{
					buf.append("<" + prefix2 + ":" + currentElement);
					String prefixNamespace = parser.getNamespace(prefix2);
					if (prefixNamespace != null)
					{
						buf.append(" xmlns:" + prefix2 + "=\"" + prefixNamespace + "\"");
					}
					
				}
				else
				{
					buf.append("<" + currentElement);
				}
				
				if (!namespace.equals(nspace2))
				{
					buf.append(" xmlns=\"" + nspace2 + "\"");
				}
				for (int i = 0; i < parser.getAttributeCount(); ++i)
				{
					buf.append(" " + parser.getAttributeName(i) + "=\"" + parser.getAttributeValue(i) + "\"");
				}
				
				buf.append(">");
			}
			else if (eventType == XmlPullParser.TEXT)
			{
				buf.append(parser.getText());
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				String prefix2 = parser.getPrefix();
				if (prefix2 != null)
				{
					buf.append("</" + prefix2 + ":" + currentElement + ">");
				}
				else
				{
					buf.append("</" + currentElement + ">");
				}
				if (currentElement.equals(elementName))
				{
					done = true;
				}
			}
		}
		packetX.setContent(buf.toString());
		return packetX;
	}

	private IQRoster parseIQRoster(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		IQRoster iqRoster = new IQRoster();
		IQRoster.Item item = null;
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			
			if (eventType == XmlPullParser.START_TAG)
			{
				if ("item".equals(elementName))
				{
					String jid = parser.getAttributeValue("", "jid");
					String name = parser.getAttributeValue("", "name");
					String subscription = parser.getAttributeValue("", "subscription");
					String ask = parser.getAttributeValue("", "ask");
					
					item = new IQRoster.Item(new JID(jid), name);
					item.setSubscription(IQRoster.Subscription.valueOf(subscription));
					if (ask != null && !ask.isEmpty())
					{
						item.setAsk(IQRoster.Ask.fromString(ask));
					}
				}
				else if ("group".equals(elementName) && item != null)
				{
					item.addGroupName(parser.nextText());
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("item".equals(elementName))
				{
					if (item != null)
					{
						iqRoster.addRosterItem(item);
					}
				}
				else if ("query".equals(elementName))
				{
					done = true;
				}
			}
		}
		
		return iqRoster;
	}

	private IQBind parseIQBind(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		IQBind bind = new IQBind();
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			if (eventType == XmlPullParser.START_TAG)
			{
				if ("resource".equals(elementName))
				{
					String resource = parser.nextText();
					bind.setResource(resource);
				}
				else if ("jid".equals(elementName))
				{
					String jid = parser.nextText();
					bind.setJid(new JID(jid));
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("bind".equals(elementName))
				{
					done = true;
				}
			}
		}
		return bind;
	}

	private String getLanguageAttribute(XmlPullParser parser)
	{
		for (int i = 0; i < parser.getAttributeCount(); i++)
		{
			String attributeName = parser.getAttributeName(i);
			if ("xml:lang".equals(attributeName) || ("lang".equals(attributeName) && "xml".equals(parser.getAttributePrefix(i))))
			{
				return parser.getAttributeValue(i);
			}
		}
		return null;
	}
	
	private XMLStanza parseStream(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		
		String xmlns = parser.getNamespace(null);
		String from = parser.getAttributeValue("", "from");
		String to = parser.getAttributeValue("", "to");
		String version = parser.getAttributeValue("", "version");
		String id = parser.getAttributeValue("", "id");
		String lang = getLanguageAttribute(parser);
		
		if ("jabber:client".equals(xmlns))
		{
			Stream stream = new Stream();
			if (id == null || id.isEmpty())
			{
				stream.setStanzaID(Stream.ID_NOT_AVAILABLE);
			}
			else
			{
				stream.setStanzaID(id);
			}
			stream.setFrom(new JID(from));
			if (to != null)
			{
				stream.setTo(new JID(to));
			}
			stream.setVersion(version);
			stream.setLang(lang);
			
			return stream;
		}
		
		return null;
	}

	private XMPPError parseError(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		XMPPError error = new XMPPError();
		String code = parser.getAttributeValue("", "code");
		if (code != null && !code.isEmpty())
		{
			error.setCode(Integer.parseInt(code));
		}
		XMPPError.Type type = null;
		String strType = parser.getAttributeValue("", "type");
		if (strType != null && !strType.isEmpty())
		{
			type = XMPPError.Type.valueOf(strType.toUpperCase());
			error.setType(type);
		}

		
		boolean done = false;
		while (!done)
		{
			int eventType = parser.next();
			String elementName = parser.getName();
			String namespace = parser.getNamespace(null);
			if (eventType == XmlPullParser.START_TAG)
			{
				if (XMPPError.Condition.bad_request.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.bad_request);
				}
				else if (XMPPError.Condition.conflict.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.conflict);
				}
				else if (XMPPError.Condition.feature_not_implemented.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.feature_not_implemented);
				}
				else if (XMPPError.Condition.forbidden.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.forbidden);
				}
				else if (XMPPError.Condition.gone.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.gone);
				}
				else if (XMPPError.Condition.internal_server_error.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.internal_server_error);
				}
				else if (XMPPError.Condition.item_not_found.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.item_not_found);
				}
				else if (XMPPError.Condition.jid_malformed.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.jid_malformed);
				}
				else if (XMPPError.Condition.no_acceptable.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.no_acceptable);
				}
				else if (XMPPError.Condition.not_allowed.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.not_allowed);
				}
				else if (XMPPError.Condition.not_authorized.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.not_authorized);
				}
				else if (XMPPError.Condition.payment_required.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.payment_required);
				}
				else if (XMPPError.Condition.recipient_unavailable.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.recipient_unavailable);
				}
				else if (XMPPError.Condition.redirect.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.redirect);
				}
				else if (XMPPError.Condition.registration_required.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.registration_required);
				}
				else if (XMPPError.Condition.remote_server_error.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.remote_server_error);
				}
				else if (XMPPError.Condition.remote_server_not_found.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.remote_server_not_found);
				}
				else if (XMPPError.Condition.remote_server_timeout.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.remote_server_timeout);
				}
				else if (XMPPError.Condition.request_timeout.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.request_timeout);
				}
				else if (XMPPError.Condition.resource_constraint.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.resource_constraint);
				}
				else if (XMPPError.Condition.service_unavailable.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.service_unavailable);
				}
				else if (XMPPError.Condition.subscription_required.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.subscription_required);
				}
				else if (XMPPError.Condition.undefined_condition.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.undefined_condition);
				}
				else if (XMPPError.Condition.unexpected_condition.toString().equals(parser.getName()))
				{
					error = new XMPPError(XMPPError.Condition.unexpected_condition);
				}
				else if ("text".equals(parser.getName()))
				{
					if (error != null)
						error.setMessage(parser.nextText());
				}
				else
				{
					error.addOtherCondition(elementName, namespace);
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if ("error".equals(parser.getName()))
				{
					done = true;
				}
			}
		}

		return error;
	}
}
