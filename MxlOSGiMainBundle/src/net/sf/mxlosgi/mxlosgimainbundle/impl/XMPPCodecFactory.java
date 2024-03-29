package net.sf.mxlosgi.mxlosgimainbundle.impl;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * Factory that specifies the encode and decoder to use for parsing XMPP
 * stanzas.
 * 
 * @author Gaston Dombiak
 */
public class XMPPCodecFactory implements ProtocolCodecFactory
{	
	private final XMPPEncoder encoder;

	private final XMPPDecoder decoder;
	
	public XMPPCodecFactory()
	{
		encoder = new XMPPEncoder();
		decoder = new XMPPDecoder();
		
	}

	public ProtocolEncoder getEncoder() throws Exception
	{
		return encoder;
	}

	public ProtocolDecoder getDecoder() throws Exception
	{
		return decoder;
	}
}
