package net.sf.mxlosgi.mxlosgimainbundle;

import net.sf.mxlosgi.mxlosgixmppbundle.JID;

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