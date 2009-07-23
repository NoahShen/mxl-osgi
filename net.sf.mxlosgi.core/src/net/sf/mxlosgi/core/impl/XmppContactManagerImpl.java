/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import net.sf.mxlosgi.core.ServerTimeoutException;
import net.sf.mxlosgi.core.StanzaCollector;
import net.sf.mxlosgi.core.UserResource;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppContact;
import net.sf.mxlosgi.core.XmppContactManager;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.core.filter.PacketIDFilter;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.IqRoster;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Presence;
import net.sf.mxlosgi.xmpp.XmlStanza;


/**
 * @author noah
 *
 */
public class XmppContactManagerImpl implements XmppContactManager
{
	private final Logger logger = LoggerFactory.getLogger(XmppContactManagerImpl.class);
	
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


	void updateContact(IqRoster iqRoster)
	{
		for (IqRoster.Item item  : iqRoster.getRosterItems())
		{
			IqRoster.Subscription subs = item.getSubscription();
			JID jid = item.getJid();
			if (subs == IqRoster.Subscription.remove)
			{
				contacts.remove(jid);
				
				logger.debug(jid + " has been removed");
				
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
				
				logger.debug(jid + " has been updated");
				
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
		
		logger.debug("removeing " + jid);
		
		Iq iq = new Iq(Iq.Type.set);
		IqRoster roster = new IqRoster();
		IqRoster.Item item = new IqRoster.Item(jid);
		item.setSubscription(IqRoster.Subscription.remove);
		roster.addRosterItem(item);
		iq.addExtension(roster);
		
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
				logger.debug("remove " + jid + " failed");
				throw new XmppException(iqResponse.getError());
			}
			
			logger.debug("remove " + jid + " completed");
		}
	}


	@Override
	public void addContact(JID jid, String name, String... groups) throws XmppException
	{
		logger.debug("adding " + jid);
		
		Iq iq = new Iq(Iq.Type.set);
		IqRoster roster = new IqRoster();
		IqRoster.Item item = new IqRoster.Item(jid);
		item.setName(name);
		for (String group : groups)
		{
			item.addGroupName(group);
		}
		roster.addRosterItem(item);
		iq.addExtension(roster);
		
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XmlStanza data = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (data == null)
		{
			logger.debug("adding " + jid + " failed");
			throw new ServerTimeoutException("remote server no response");
		}
		
		if (data instanceof Iq)
		{
			Iq iqResponse = (Iq) data;
			Iq.Type type = iqResponse.getType();
			if (type == Iq.Type.error)
			{
				logger.debug("adding " + jid + " failed");
				throw new XmppException(iqResponse.getError());
			}
			
			logger.debug("adding " + jid + " completed");
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
		
		logger.debug("updating " + jid + " name");
		
		Iq iq = new Iq(Iq.Type.set);
		IqRoster roster = new IqRoster();
		IqRoster.Item item = new IqRoster.Item(jid);
		
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
		XmlStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (stanza == null)
		{
			logger.debug("updating " + jid + " name failed");
			throw new ServerTimeoutException("remote server no response");
		}
		
		if (stanza instanceof Iq)
		{
			Iq iqResponse = (Iq) stanza;
			Iq.Type type = iqResponse.getType();
			if (type == Iq.Type.error)
			{
				logger.debug("updating " + jid + " name failed");
				throw new XmppException(iqResponse.getError());
			}
			
			logger.debug("updating " + jid + " name completed");
		}
	}

	public void updateContactGroup(JID jid, String ... groups) throws XmppException
	{
		XmppContact contact = getContact(jid);
		if (contact == null)
		{
			return;
		}
		
		logger.debug("updating " + jid + " group");
		
		Iq iq = new Iq(Iq.Type.set);
		IqRoster roster = new IqRoster();
		IqRoster.Item item = new IqRoster.Item(jid);
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
		XmlStanza data = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (data == null)
		{
			logger.debug("updating " + jid + " group failed");
			
			throw new ServerTimeoutException("remote server no response");
		}
		
		if (data instanceof Iq)
		{
			Iq iqResponse = (Iq) data;
			Iq.Type type = iqResponse.getType();
			if (type == Iq.Type.error)
			{
				logger.debug("updating " + jid + " group failed");
				throw new XmppException(iqResponse.getError());
			}
			
			logger.debug("updating " + jid + " group completed");
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
		
		logger.debug(jid + " status changed");
		
		contactListenerServiceTracker.fireContactStatusChanged(connection, contact, userResource);
	}

	void connectionClosed()
	{
		contacts.clear();
	}
	
	void queryRoster()
	{
		Iq iq = new Iq(Iq.Type.get);
		IqRoster roster = new IqRoster();
		iq.addExtension(roster);

		connection.sendStanza(iq);
	}

	@Override
	public XmppConnection getConnection()
	{
		return connection;
	}

}
