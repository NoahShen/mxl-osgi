/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.mxlosgi.core.ServerTimeoutException;
import net.sf.mxlosgi.core.StanzaCollector;
import net.sf.mxlosgi.core.UserResource;
import net.sf.mxlosgi.core.XMPPConnection;
import net.sf.mxlosgi.core.XMPPContact;
import net.sf.mxlosgi.core.XMPPContactManager;
import net.sf.mxlosgi.core.XMPPException;
import net.sf.mxlosgi.core.filter.PacketIDFilter;
import net.sf.mxlosgi.xmpp.IQ;
import net.sf.mxlosgi.xmpp.IQRoster;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Presence;
import net.sf.mxlosgi.xmpp.XMLStanza;


/**
 * @author noah
 *
 */
public class XMPPContactManagerImpl implements XMPPContactManager
{
	private XMPPConnection connection;
	
	private XMPPMainManagerImpl mainManager;
	
	private final Map<JID, XMPPContact> contacts = Collections.synchronizedMap(new HashMap<JID, XMPPContact>());
	
	/**
	 * @param connection
	 */
	public XMPPContactManagerImpl(XMPPConnection connection, XMPPMainManagerImpl mainManager)
	{
		this.connection = connection;
		this.mainManager = mainManager;
	}

	@Override
	public XMPPContact getContact(JID bareJID)
	{
		return contacts.get(bareJID);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.XMPPContactManager#getContacts()
	 */
	@Override
	public XMPPContact[] getContacts()
	{
		return contacts.values().toArray(new XMPPContact[]{});
	}


	void updateContact(IQRoster iqRoster)
	{
		for (IQRoster.Item item  : iqRoster.getRosterItems())
		{
			IQRoster.Subscription subs = item.getSubscription();
			JID jid = item.getJid();
			if (subs == IQRoster.Subscription.remove)
			{
				contacts.remove(jid);
				mainManager.fireContactRemoved(connection, jid);
			}
			else
			{
				XMPPContact contact = getContact(jid);
				if (contact == null)
				{
					contact = new XMPPContactImpl(item.getJid(), item);
				}
				XMPPContactImpl contactImpl = (XMPPContactImpl) contact;
				contactImpl.setItem(item);
				contactImpl.setName(item.getName());
				contactImpl.clearGroup();
				contactImpl.addGroups(item.getGroupNames());
				contacts.put(jid, contact);
				
				mainManager.fireContactUpdated(connection, contact);
			}
			
		}
	}


	@Override
	public void removeContactFromRoster(JID jid) throws XMPPException
	{
		XMPPContact contact = getContact(jid);
		if (contact == null)
		{
			return;
		}
		
		IQ iq = new IQ(IQ.Type.set);
		IQRoster roster = new IQRoster();
		IQRoster.Item item = new IQRoster.Item(jid);
		item.setSubscription(IQRoster.Subscription.remove);
		roster.addRosterItem(item);
		iq.addExtension(roster);
		
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XMLStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (stanza == null)
		{
			throw new ServerTimeoutException("remote server no response");
		}
		
		if (stanza instanceof IQ)
		{
			IQ iqResponse = (IQ) stanza;
			IQ.Type type = iqResponse.getType();
			if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
		}
	}


	@Override
	public void addContact(JID jid, String name, String... groups) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.set);
		IQRoster roster = new IQRoster();
		IQRoster.Item item = new IQRoster.Item(jid);
		item.setName(name);
		for (String group : groups)
		{
			item.addGroupName(group);
		}
		roster.addRosterItem(item);
		iq.addExtension(roster);
		
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
			if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
		}
	}

	@Override
	public void subscribeContact(JID jid)
	{
		Presence presence = new Presence(Presence.Type.subscribe);
		presence.setTo(jid);
		connection.sendStanza(presence);
	}
	
	@Override
	public void subscribedContact(JID jid)
	{
		Presence presence = new Presence(Presence.Type.subscribed);
		presence.setTo(jid);
		connection.sendStanza(presence);
	}
	
	@Override
	public void unsubscribeContact(JID jid)
	{
		Presence presence = new Presence(Presence.Type.unsubscribe);
		presence.setTo(jid);
		connection.sendStanza(presence);
	}

	@Override
	public void unsubscribedContact(JID jid)
	{
		Presence presence = new Presence(Presence.Type.unsubscribed);
		presence.setTo(jid);
		connection.sendStanza(presence);
	}

	@Override
	public void updateContactName(JID jid, String name) throws XMPPException
	{
		XMPPContact contact = getContact(jid);
		if (contact == null)
		{
			return;
		}
		
		IQ iq = new IQ(IQ.Type.set);
		IQRoster roster = new IQRoster();
		IQRoster.Item item = new IQRoster.Item(jid);
		
		if (name == null)
		{
			name =  "";
		}
		
		item.setName(name);
		
		for (String group : contact.getGroups())
		{
			item.addGroupName(group);
		}
		
		roster.addRosterItem(item);
		iq.addExtension(roster);
		
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XMLStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (stanza == null)
		{
			throw new ServerTimeoutException("remote server no response");
		}
		
		if (stanza instanceof IQ)
		{
			IQ iqResponse = (IQ) stanza;
			IQ.Type type = iqResponse.getType();
			if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
		}
	}

	public void updateContactGroup(JID jid, String ... groups) throws XMPPException
	{
		XMPPContact contact = getContact(jid);
		if (contact == null)
		{
			return;
		}
		
		IQ iq = new IQ(IQ.Type.set);
		IQRoster roster = new IQRoster();
		IQRoster.Item item = new IQRoster.Item(jid);
		for (String group : groups)
		{
			if (group != null)
			{
				item.addGroupName(group);
			}
		}
		
		roster.addRosterItem(item);
		iq.addExtension(roster);
		
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
			if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
		}
	}
	
	void contactUnsubscribed(Presence presence)
	{
		JID jid = presence.getFrom();
		mainManager.fireContactUnsubscribed(connection, jid);
	}

	void contactUnsubscribeMe(Presence presence)
	{
		JID jid = presence.getFrom();
		mainManager.fireContactUnsubscribeMe(connection, jid);
	}

	void contactSubscribed(Presence presence)
	{
		JID jid = presence.getFrom();
		mainManager.fireContactSubscribed(connection, jid);
	}

	void contactSubscribeMe(Presence presence)
	{
		JID jid = presence.getFrom();
		SubscriptionRequestImpl subsRequest = new SubscriptionRequestImpl(jid, connection);
		mainManager.fireContactSubscribeMe(connection, subsRequest);
	}

	void handleStatusChanged(Presence presence)
	{
		JID jid = presence.getFrom();
		JID bareJID = new JID(jid.toBareJID());
		String resource = jid.getResource();
		
		XMPPContact contact = getContact(bareJID);
		
		if (contact == null)
		{
			return;
		}
		
		XMPPContactImpl contactImpl = (XMPPContactImpl) contact;
		UserResource userResource = contactImpl.getResource(resource);
		if (userResource == null)
		{
			userResource = new UserResourceImpl(resource);
			contactImpl.addResource(userResource);
		}

		UserResourceImpl userResourceImpl = (UserResourceImpl) userResource;
		userResourceImpl.setCurrentPresence(presence);
		
		if (presence.getType() == Presence.Type.unavailable)
		{
			contactImpl.removeResource(userResource.getResource());
		}
		mainManager.fireContactStatusChanged(connection, contact, userResource);
	}

	void connectionClosed()
	{
		contacts.clear();
	}
	
	void queryRoster()
	{
		IQ iq = new IQ(IQ.Type.get);
		IQRoster roster = new IQRoster();
		iq.addExtension(roster);

		connection.sendStanza(iq);
	}

	@Override
	public XMPPConnection getConnection()
	{
		return connection;
	}

}
