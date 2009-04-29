package net.sf.mxlosgi.mxlosgimainbundle;

import net.sf.mxlosgi.mxlosgimainbundle.filter.StanzaFilter;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;



public interface StanzaCollector
{

	/**
	 * Explicitly cancels the stanza collector so that no more results are
	 * queued up. Once a stanza collector has been cancelled, it cannot be
	 * re-enabled. Instead, a new stanza collector must be created.
	 */
	public void cancel();

	/**
	 * Returns the stanza filter associated with this stanza collector.
	 * The stanza filter is used to determine what stanzas are queued as
	 * results.
	 * 
	 * @return the stanza filter.
	 */
	public StanzaFilter getStanzaFilter();

	/**
	 * Polls to see if a stanza is currently available and returns it, or
	 * immediately returns <tt>null</tt> if no stanzas are currently in
	 * the result queue.
	 * 
	 * @return the next stanza result, or <tt>null</tt> if there are no
	 *         more results.
	 */
	public XMLStanza pollResult();

	/**
	 * Returns the next available stanza. The method call will block (not
	 * return) until a stanza is available.
	 * 
	 * @return the next available stanza.
	 */
	public XMLStanza nextResult();

	/**
	 * Returns the next available stanza. The method call will block (not
	 * return) until a stanza is available or the <tt>timeout</tt> has
	 * elapased. If the timeout elapses without a result, <tt>null</tt>
	 * will be returned.
	 * 
	 * @param timeout
	 *                  the amount of time to wait for the next stanza (in
	 *                  milleseconds).
	 * @return the next available stanza.
	 */
	public XMLStanza nextResult(long timeout);
	
	/**
	 * @param connection
	 * @param stanza
	 */
	public void processPacket(XMPPConnection connection, XMLStanza stanza);
}
