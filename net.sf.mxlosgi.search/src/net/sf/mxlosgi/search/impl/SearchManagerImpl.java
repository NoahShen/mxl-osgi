/**
 * 
 */
package net.sf.mxlosgi.search.impl;

import net.sf.mxlosgi.core.ServerTimeoutException;
import net.sf.mxlosgi.core.StanzaCollector;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.core.filter.PacketIDFilter;
import net.sf.mxlosgi.disco.DiscoInfoManager;
import net.sf.mxlosgi.disco.DiscoInfoPacketExtension;
import net.sf.mxlosgi.search.SearchExtension;
import net.sf.mxlosgi.search.SearchManager;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.XmlStanza;


/**
 * @author noah
 *
 */
public class SearchManagerImpl implements SearchManager
{
	private DiscoInfoManagerServiceTracker discoInfoManagerServiceTracker;

	/**
	 * 
	 * @param discoInfoManager
	 */
	public SearchManagerImpl(DiscoInfoManagerServiceTracker discoInfoManagerServiceTracker)
	{
		this.discoInfoManagerServiceTracker = discoInfoManagerServiceTracker;
	}

	@Override
	public SearchExtension getSearchExtension(XmppConnection connection, JID serverJID) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.get);
		iq.setTo(serverJID);
		SearchExtension searchExtension = new SearchExtension();
		iq.addExtension(searchExtension);
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XmlStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (stanza == null)
		{
			throw new ServerTimeoutException("remote server no response");
		}
		if (stanza instanceof Iq)
		{
			Iq iqResponse = (Iq) stanza;
			Iq.Type type = iqResponse.getType();
			if (type == Iq.Type.result)
			{
				return (SearchExtension) iqResponse.getExtension(SearchExtension.ELEMENTNAME, 
													SearchExtension.NAMESPACE);
			}
			else if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
		
		return null;
	}

	@Override
	public boolean isSupportSearch(XmppConnection connection, JID serverJID) throws XmppException
	{
		DiscoInfoManager discoInfoManager = discoInfoManagerServiceTracker.getDiscoInfoManager();
		DiscoInfoPacketExtension discoInfo = discoInfoManager.getDiscoInfo(connection, serverJID);
		for (DiscoInfoPacketExtension.Feature feature : discoInfo.getFeatures())
		{
			if ("jabber:iq:search".equals(feature.getFeature()))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public SearchExtension search(XmppConnection connection, SearchExtension extension, JID serverJID) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.set);
		iq.setTo(serverJID);
		iq.addExtension(extension);
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XmlStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (stanza == null)
		{
			throw new ServerTimeoutException("remote server no response");
		}
		if (stanza instanceof Iq)
		{
			Iq iqResponse = (Iq) stanza;
			Iq.Type type = iqResponse.getType();
			if (type == Iq.Type.result)
			{
				return (SearchExtension) iqResponse.getExtension(SearchExtension.ELEMENTNAME, 
													SearchExtension.NAMESPACE);
			}
			else if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
		
		return null;
	}

}
