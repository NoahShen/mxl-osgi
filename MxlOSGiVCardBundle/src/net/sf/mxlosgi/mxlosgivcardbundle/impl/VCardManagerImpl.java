/**
 * 
 */
package net.sf.mxlosgi.mxlosgivcardbundle.impl;

import net.sf.mxlosgi.mxlosgimainbundle.ServerTimeoutException;
import net.sf.mxlosgi.mxlosgimainbundle.StanzaCollector;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgimainbundle.filter.PacketIDFilter;
import net.sf.mxlosgi.mxlosgivcardbundle.VCardManager;
import net.sf.mxlosgi.mxlosgivcardbundle.VCardPacketExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class VCardManagerImpl implements VCardManager
{
	
	/**
	 * @param connection
	 */
	public VCardManagerImpl()
	{
	}

	@Override
	public VCardPacketExtension getVCard(XMPPConnection connection, JID to) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.get);
		iq.setTo(to);
		VCardPacketExtension vCard = new VCardPacketExtension();
		iq.addExtension(vCard);
		
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XMLStanza data = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (data == null)
		{
			throw new ServerTimeoutException("remote server no response");
		}
		if (data instanceof IQ)
		{
			IQ iqResponse = (IQ) data;
			IQ.Type type = iqResponse.getType();
			if (type == IQ.Type.result)
			{
				VCardPacketExtension vCardResponse 
					= (VCardPacketExtension) iqResponse.getExtension("vCard", "vcard-temp");
				return vCardResponse;
			}
			else if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
		}
		
		return null;
	}

	@Override
	public boolean updateOwnerVCard(XMPPConnection connection, VCardPacketExtension vCard) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.set);
		iq.addExtension(vCard);
		
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XMLStanza data = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (data == null)
		{
			throw new ServerTimeoutException("remote server no response");
		}
		if (data instanceof IQ)
		{
			IQ iqResponse = (IQ) data;
			IQ.Type type = iqResponse.getType();
			if (type == IQ.Type.result)
			{
				return true;
			}
			else if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
		}
		
		return false;
	}

}
