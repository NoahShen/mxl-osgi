/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import net.sf.mxlosgi.core.SubscriptionRequest;
import net.sf.mxlosgi.core.XMPPConnection;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Presence;


/**
 * @author noah
 * 
 */
public class SubscriptionRequestImpl implements SubscriptionRequest
{

	private JID from;

	private XMPPConnection connection;

	public SubscriptionRequestImpl(JID from, XMPPConnection connection)
	{
		this.from = from;
		this.connection = connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.SubscriptionRequest#accept()
	 */
	@Override
	public void accept()
	{
		Presence presence = new Presence(Presence.Type.subscribed);
		presence.setTo(from);
		connection.sendStanza(presence);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.SubscriptionRequest#getFrom()
	 */
	@Override
	public JID getFrom()
	{
		return from;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgiownerrosterbundle.SubscriptionRequest#reject()
	 */
	@Override
	public void reject()
	{
		Presence presence = new Presence(Presence.Type.unsubscribed);
		presence.setTo(from);
		connection.sendStanza(presence);
	}

}
