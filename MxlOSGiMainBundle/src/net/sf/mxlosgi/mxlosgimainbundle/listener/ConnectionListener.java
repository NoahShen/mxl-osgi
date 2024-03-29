package net.sf.mxlosgi.mxlosgimainbundle.listener;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgixmppbundle.Failure;
import net.sf.mxlosgi.mxlosgixmppbundle.StreamError;



public interface ConnectionListener
{

	/**
	 * Notification that the connection was closed normally or that the
	 * reconnection process has been aborted.
	 */
	public void connectionClosed(XMPPConnection connection);
	
	/**
	 * 
	 * @param connection
	 */
	public void connectionCreated(XMPPConnection connection);
	
	/**
	 * 
	 * @param connection
	 */
	public void connectionConnected(XMPPConnection connection);
	
	/**
	 * 
	 * @param connection
	 * @param streamError
	 */
	public void connectionStreamError(XMPPConnection connection, StreamError streamError);
	
	/**
	 * 
	 * @param connection
	 * @param cause
	 */
	public void exceptionCaught(XMPPConnection connection, Throwable cause);
	
	/**
	 * 
	 * @param connection
	 */
	public void saslSuccessful(XMPPConnection connection);
	
	/**
	 * 
	 * @param connection
	 * @param failure
	 */
	public void saslFailed(XMPPConnection connection, Failure failure);
	
	/**
	 * 
	 * @param connection
	 */
	public void sessionBinded(XMPPConnection connection);
	
	/**
	 * 
	 * @param connection
	 */
	public void resourceBinded(XMPPConnection connection);
}