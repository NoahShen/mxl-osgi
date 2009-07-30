/**
 * 
 */
package net.sf.mxlosgi.vcard.impl;

import net.sf.mxlosgi.core.ServerTimeoutException;
import net.sf.mxlosgi.core.StanzaCollector;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.core.filter.PacketIDFilter;
import net.sf.mxlosgi.vcard.VCardManager;
import net.sf.mxlosgi.vcard.VCardPacketExtension;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.XmlStanza;


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
	public VCardPacketExtension getVCard(XmppConnection connection, JID to) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.get);
		iq.setTo(to);
		VCardPacketExtension vCard = new VCardPacketExtension();
		iq.addExtension(vCard);
		
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XmlStanza data = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (data == null)
		{
			throw new ServerTimeoutException("remote server no response");
		}
		if (data instanceof Iq)
		{
			Iq iqResponse = (Iq) data;
			Iq.Type type = iqResponse.getType();
			if (type == Iq.Type.result)
			{
				VCardPacketExtension vCardResponse 
					= (VCardPacketExtension) iqResponse.getExtension("vCard", "vcard-temp");
				return vCardResponse;
			}
			else if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
		
		return null;
	}

	@Override
	public boolean updateOwnerVCard(XmppConnection connection, VCardPacketExtension vCard) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.set);
		iq.addExtension(vCard);
		
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XmlStanza data = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (data == null)
		{
			throw new ServerTimeoutException("remote server no response");
		}
		if (data instanceof Iq)
		{
			Iq iqResponse = (Iq) data;
			Iq.Type type = iqResponse.getType();
			if (type == Iq.Type.result)
			{
				return true;
			}
			else if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
		
		return false;
	}

}
