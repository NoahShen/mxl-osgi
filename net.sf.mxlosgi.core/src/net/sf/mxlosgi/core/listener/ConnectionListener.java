package net.sf.mxlosgi.core.listener;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.xmpp.Failure;
import net.sf.mxlosgi.xmpp.StreamError;


public interface ConnectionListener
{

	/**
	 * Notification that the connection was closed normally or that the
	 * reconnection process has been aborted.
	 */
	public void connectionClosed(XmppConnection connection);
	
	/**
	 * 
	 * @param connection
	 */
	public void connectionCreated(XmppConnection connection);
	
	/**
	 * 
	 * @param connection
	 */
	public void connectionConnected(XmppConnection connection);
	
	/**
	 * 
	 * @param connection
	 * @param streamError
	 */
	public void connectionStreamError(XmppConnection connection, StreamError streamError);
	
	/**
	 * 
	 * @param connection
	 * @param cause
	 */
	public void exceptionCaught(XmppConnection connection, Throwable cause);
	
	/**
	 * 
	 * @param connection
	 */
	public void saslSuccessful(XmppConnection connection);
	
	/**
	 * 
	 * @param connection
	 * @param failure
	 */
	public void saslFailed(XmppConnection connection, Failure failure);
	
	/**
	 * 
	 * @param connection
	 */
	public void sessionBinded(XmppConnection connection);
	
	/**
	 * 
	 * @param connection
	 */
	public void resourceBinded(XmppConnection connection);
}