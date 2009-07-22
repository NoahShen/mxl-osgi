/**
 * 
 */
package net.sf.mxlosgi.core.listener;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.xmpp.Failure;
import net.sf.mxlosgi.xmpp.StreamError;


/**
 * @author noah
 *
 */
public abstract class ConnectionAdapter implements ConnectionListener
{

	@Override
	public void connectionCreated(XmppConnection connection)
	{
	}

	@Override
	public void connectionClosed(XmppConnection connection)
	{
	}

	@Override
	public void connectionConnected(XmppConnection connection)
	{
	}

	@Override
	public void exceptionCaught(XmppConnection connection, Throwable cause)
	{
	}

	@Override
	public void resourceBinded(XmppConnection connection)
	{
	}

	@Override
	public void saslFailed(XmppConnection connection, Failure failure)
	{
	}

	@Override
	public void saslSuccessful(XmppConnection connection)
	{
	}

	@Override
	public void sessionBinded(XmppConnection connection)
	{
	}
	
	@Override
	public void connectionStreamError(XmppConnection connection, StreamError streamError)
	{
	}

}
