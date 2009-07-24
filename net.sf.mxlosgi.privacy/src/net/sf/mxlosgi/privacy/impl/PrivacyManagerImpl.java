/**
 * 
 */
package net.sf.mxlosgi.privacy.impl;

import net.sf.mxlosgi.core.ServerTimeoutException;
import net.sf.mxlosgi.core.StanzaCollector;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.core.filter.PacketIDFilter;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.privacy.PrivacyManager;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.Privacy;
import net.sf.mxlosgi.xmpp.PrivacyList;
import net.sf.mxlosgi.xmpp.XmlStanza;


/**
 * @author noah
 *
 */
public class PrivacyManagerImpl implements PrivacyManager, StanzaReceListener
{
	private PrivacyListenerServiceTracker privacyListenerServiceTracker;
	/**
	 * @param connection
	 */
	public PrivacyManagerImpl(PrivacyListenerServiceTracker privacyListenerServiceTracker)
	{
		this.privacyListenerServiceTracker = privacyListenerServiceTracker;
	}

	@Override
	public void createOrUpdatePrivacyList(XmppConnection connection, PrivacyList privacyList) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.set);
		Privacy privacy = new Privacy();
		privacy.addPrivacyList(privacyList);
		iq.addExtension(privacy);
		
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
			
			if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
	}

	@Override
	public void declineActiveList(XmppConnection connection) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.set);
		Privacy privacy = new Privacy();
		privacy.setDeclineActiveList(true);
		iq.addExtension(privacy);
		
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
			
			if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
	}


	@Override
	public void declineDefaultList(XmppConnection connection) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.set);
		Privacy privacy = new Privacy();
		privacy.setDeclineDefaultList(true);
		iq.addExtension(privacy);
		
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
			
			if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
	}

	@Override
	public void deletePrivacyList(XmppConnection connection, String listName) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.set);
		Privacy privacy = new Privacy();
		PrivacyList list = new PrivacyList(listName);
		privacy.addPrivacyList(list);
		iq.addExtension(privacy);
		
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
			
			if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
	}

	@Override
	public PrivacyList getPrivacyList(XmppConnection connection, String listName) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.get);
		Privacy privacy = new Privacy();
		PrivacyList list = new PrivacyList(listName);
		privacy.addPrivacyList(list);
		iq.addExtension(privacy);
		
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
			
			if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
			else if (type == Iq.Type.result)
			{
				Privacy privacyResponse = 
					(Privacy) iqResponse.getExtension(Privacy.ELEMENTNAME, Privacy.NAMESPACE);
				if (privacyResponse != null)
				{
					return privacyResponse.getPrivacyLists().iterator().next();
				}
			}
		}
		return null;
	}

	@Override
	public Privacy getPrivacyLists(XmppConnection connection) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.get);
		Privacy privacy = new Privacy();
		iq.addExtension(privacy);
		
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
			
			if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
			else if (type == Iq.Type.result)
			{
				Privacy privacyResponse = 
					(Privacy) iqResponse.getExtension(Privacy.ELEMENTNAME, Privacy.NAMESPACE);
				return privacyResponse;
			}
		}
		return null;
	}

	@Override
	public void setActiveListName(XmppConnection connection, String listName) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.set);
		Privacy privacy = new Privacy();
		privacy.setActiveName(listName);
		iq.addExtension(privacy);
		
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
			
			if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
	}

	@Override
	public void setDefaultListName(XmppConnection connection, String listName) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.set);
		Privacy privacy = new Privacy();
		privacy.setDefaultName(listName);
		iq.addExtension(privacy);
		
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
			
			if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
	}

	@Override
	public void processReceStanza(XmppConnection connection, XmlStanza stanza)
	{
		Iq iq = (Iq) stanza;
		Privacy privacy = 
			(Privacy) iq.getExtension(Privacy.ELEMENTNAME, Privacy.NAMESPACE);

		if (privacy == null)
		{
			return;
		}
		for (PrivacyList list : privacy.getPrivacyLists())
		{
			privacyListenerServiceTracker.firePrivacyListCreatedOrUpdated(connection, list.getListName());
		}
	}


}
