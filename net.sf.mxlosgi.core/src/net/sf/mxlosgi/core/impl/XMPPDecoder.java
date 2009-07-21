package net.sf.mxlosgi.core.impl;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * Decoder class that parses ByteBuffers and generates XML stanzas. Generated
 * stanzas are then passed to the next filters.
 * 
 * @author Gaston Dombiak
 */
public class XMPPDecoder extends CumulativeProtocolDecoder
{

	protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception
	{
		// Get the XML light parser from the IoSession
		XMLLightweightParser lightWeightparser = (XMLLightweightParser) session.getAttribute("XMLLightweightParser");
		if (lightWeightparser == null)
		{
			lightWeightparser = new XMLLightweightParser("UTF-8");
			session.setAttribute("XMLLightweightParser", lightWeightparser);
		}
		// Parse as many stanzas as possible from the received data
		lightWeightparser.read(in);

		if (lightWeightparser.areThereMsgs())
		{
			for (String stanzaStr : lightWeightparser.getMsgs())
			{
				out.write(stanzaStr);
			}
		}
		return true;
	}
}
