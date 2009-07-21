package net.sf.mxlosgi.core;

import net.sf.mxlosgi.xmpp.JID;


/**
 * @author noah
 *
 */
public interface SubscriptionRequest
{
	/**
	 * subscriber's bare jid
	 * @return
	 */
	public JID getFrom();
	
	/**
	 * 
	 */
	public void accept();
	
	/**
	 * 
	 */
	public void reject();
}