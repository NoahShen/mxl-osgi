package net.sf.mxlosgi.mxlosgidiscobundle.impl;

import net.sf.mxlosgi.mxlosgidiscobundle.DiscoItemsManager;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoItemsPacketExtension;
import net.sf.mxlosgi.mxlosgimainbundle.ServerTimeoutException;
import net.sf.mxlosgi.mxlosgimainbundle.StanzaCollector;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgimainbundle.filter.PacketIDFilter;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class DiscoItemsManagerImpl implements DiscoItemsManager
{
	
	public DiscoItemsManagerImpl()
	{
	}

	@Override
	public DiscoItemsPacketExtension getDiscoItems(XMPPConnection connection, JID to) throws XMPPException
	{
		return getDiscoItems(connection, to, null);
	}

	@Override
	public DiscoItemsPacketExtension getDiscoItems(XMPPConnection connection, JID to, String node) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.get);
		iq.setTo(to);
		DiscoItemsPacketExtension queryDiscoItems = new DiscoItemsPacketExtension();
		queryDiscoItems.setNode(node);
		iq.addExtension(queryDiscoItems);
		
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XMLStanza data = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		
		collector.cancel();
		
		if (data == null)
		{
			throw new ServerTimeoutException("no response from server");
		}
		else
		{
			IQ iqResponse = (IQ) data;
			IQ.Type type = iqResponse.getType();
			if (type == IQ.Type.result)
			{
				DiscoItemsPacketExtension discoInfoResponse 
					= (DiscoItemsPacketExtension) iqResponse.getExtension(DiscoItemsPacketExtension.ELEMENTNAME, 
																DiscoItemsPacketExtension.NAMESPACE);
				return discoInfoResponse;
			}
			else if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
		}
		return null;
	}

}