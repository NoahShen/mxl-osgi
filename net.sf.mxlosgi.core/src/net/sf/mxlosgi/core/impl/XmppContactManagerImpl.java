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
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppContact;
import net.sf.mxlosgi.core.XmppContactManager;
import net.sf.mxlosgi.core.XmppException;
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
public class XmppContactManagerImpl implements XmppContactManager
{
	private XmppConnection connection;
	
	private final Map<JID, XmppContact> contacts = Collections.synchronizedMap(new HashMap<JID, XmppContact>());
	
	private ContactListenerServiceTracker contactListenerServiceTracker;
	
	/**
	 * @param connection
	 */
	public XmppContactManagerImpl(XmppConnection connection, 
								ContactListenerServiceTracker contactListenerServiceTracker)
	{
		this.connection = connection;
		this.contactListenerServiceTracker = contactListenerServiceTracker;
	}

	@Override
	public XmppContact getContact(JID bareJID)
	{
		return contacts.get(bareJID);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.XMPPContactManager#getContacts()
	 */
	@Override
	public XmppContact[] getContacts()
	{
		return contacts.values().toArray(new XmppContact[]{});
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
				contactListenerServiceTracker.fireContactRemoved(connection, jid);
			}
			else
			{
				XmppContact contact = getContact(jid);
				if (contact == null)
				{
					contact = new XmppContactImpl(item.getJid(), item);
				}
				XmppContactImpl contactImpl = (XmppContactImpl) contact;
				contactImpl.setItem(item);
				contactImpl.setName(item.getName());
				contactImpl.clearGroup();
				contactImpl.addGroups(item.getGroupNames());
				contacts.put(jid, contact);
				
				contactListenerServiceTracker.fireContactUpdated(connection, contact);
			}
			
		}
	}


	@Override
	public void removeContactFromRoster(JID jid) throws XmppException
	{
		XmppContact contact = getContact(jid);
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
				throw new XmppException(iqResponse.getError());
			}
		}
	}


	@Override
	public void addContact(JID jid, String name, String... groups) throws XmppException
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
				throw new XmppException(iqResponse.getError());
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
	public void updateContactName(JID jid, String name) throws XmppException
	{
		XmppContact contact = getContact(jid);
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
				throw new XmppException(iqResponse.getError());
			}
		}
	}

	public void updateContactGroup(JID jid, String ... groups) throws XmppException
	{
		XmppContact contact = getContact(jid);
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
				throw new XmppException(iqResponse.getError());
			}
		}
	}
	
	void contactUnsubscribed(Presence presence)
	{
		JID jid = presence.getFrom();
		contactListenerServiceTracker.fireContactUnsubscribed(connection, jid);
	}

	void contactUnsubscribeMe(Presence presence)
	{
		JID jid = presence.getFrom();
		contactListenerServiceTracker.fireContactUnsubscribeMe(connection, jid);
	}

	void contactSubscribed(Presence presence)
	{
		JID jid = presence.getFrom();
		contactListenerServiceTracker.fireContactSubscribed(connection, jid);
	}

	void contactSubscribeMe(Presence presence)
	{
		JID jid = presence.getFrom();
		SubscriptionRequestImpl subsRequest = new SubscriptionRequestImpl(jid, connection);
		contactListenerServiceTracker.fireContactSubscribeMe(connection, subsRequest);
	}

	void handleStatusChanged(Presence presence)
	{
		JID jid = presence.getFrom();
		JID bareJID = new JID(jid.toBareJID());
		String resource = jid.getResource();
		
		XmppContact contact = getContact(bareJID);
		
		if (contact == null)
		{
			return;
		}
		
		XmppContactImpl contactImpl = (XmppContactImpl) contact;
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
		contactListenerServiceTracker.fireContactStatusChanged(connection, contact, userResource);
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
	public XmppConnection getConnection()
	{
		return connection;
	}

}
