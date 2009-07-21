/**
 * 
 */
package net.sf.mxlosgi.core.listener;

import net.sf.mxlosgi.core.XMPPConnection;
import net.sf.mxlosgi.xmpp.Failure;
import net.sf.mxlosgi.xmpp.StreamError;


/**
 * @author noah
 *
 */
public abstract class ConnectionAdapter implements ConnectionListener
{

	@Override
	public void connectionCreated(XMPPConnection connection)
	{
	}

	@Override
	public void connectionClosed(XMPPConnection connection)
	{
	}

	@Override
	public void connectionConnected(XMPPConnection connection)
	{
	}

	@Override
	public void exceptionCaught(XMPPConnection connection, Throwable cause)
	{
	}

	@Override
	public void resourceBinded(XMPPConnection connection)
	{
	}

	@Override
	public void saslFailed(XMPPConnection connection, Failure failure)
	{
	}

	@Override
	public void saslSuccessful(XMPPConnection connection)
	{
	}

	@Override
	public void sessionBinded(XMPPConnection connection)
	{
	}
	
	@Override
	public void connectionStreamError(XMPPConnection connection, StreamError streamError)
	{
	}

}
