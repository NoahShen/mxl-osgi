package net.sf.mxlosgi.core.impl;


import net.sf.mxlosgi.xmpp.XmlStanza;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * Encoder that does nothing. We are already writing ByteBuffers so there is no
 * need to encode them.
 * <p>
 * 
 * This class exists as a counterpart of {@link XmppDecoder}. Unlike that class
 * this class does nothing.
 * 
 * @author Gaston Dombiak
 */
public class XmppEncoder extends ProtocolEncoderAdapter
{

	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception
	{
		String xml = null;
		if (message instanceof XmlStanza)
		{
			XmlStanza stanza = (XmlStanza) message;
			xml = stanza.toXML();
		}
		else if (message instanceof String)
		{
			xml = (String) message;
		}
		
		if (xml != null)
		{
			ByteBuffer btyeBuffer = ByteBuffer.wrap(xml.getBytes("UTF-8"));
			out.write(btyeBuffer);
			out.flush();
		}
		
	}
}
