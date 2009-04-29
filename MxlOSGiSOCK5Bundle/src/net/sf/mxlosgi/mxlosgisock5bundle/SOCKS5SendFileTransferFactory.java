package net.sf.mxlosgi.mxlosgisock5bundle;

import java.io.File;
import java.util.Map;

import net.sf.mxlosgi.mxlosgifiletransferbundle.FileTransferManager;
import net.sf.mxlosgi.mxlosgifiletransferbundle.SendFileTransfer;
import net.sf.mxlosgi.mxlosgifiletransferbundle.SendFileTransferFactory;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgistreaminitiationbundle.StreamInitiation;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

/**
 * @author noah
 *
 */
public class SOCKS5SendFileTransferFactory implements SendFileTransferFactory
{
	private String streamMethod;
	
	
	public SOCKS5SendFileTransferFactory()
	{
		streamMethod = "http://jabber.org/protocol/bytestreams";
	}

	@Override
	public SendFileTransfer createSendFileTransfer(File file, 
										JID peerJID, 
										String sid, 
										StreamInitiation si, 
										FileTransferManager fileTransferManager,
										XMPPConnection connection, 
										Map<String, Object> properties)
	{
		return new SOCKS5SendFileTransfer(file, peerJID, sid, si, fileTransferManager, connection, properties);
	}

	@Override
	public String getStreamMethod()
	{
		return streamMethod;
	}

}