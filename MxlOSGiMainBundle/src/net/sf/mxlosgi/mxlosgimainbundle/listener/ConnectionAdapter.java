/**
 * 
 */
package net.sf.mxlosgi.mxlosgimainbundle.listener;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgixmppbundle.Failure;
import net.sf.mxlosgi.mxlosgixmppbundle.StreamError;

/**
 * @author noah
 *
 */
public abstract class ConnectionAdapter implements ConnectionListener
{

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.listener.ConnectionListener#connectionCreated(net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection)
	 */
	@Override
	public void connectionCreated(XMPPConnection connection)
	{
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiconnectionbundle.listener.ConnectionListener#connectionClosed(net.sf.mxlosgi.mxlosgiconnectionbundle.XMPPConnection)
	 */
	@Override
	public void connectionClosed(XMPPConnection connection)
	{
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiconnectionbundle.listener.ConnectionListener#connectionConnected(net.sf.mxlosgi.mxlosgiconnectionbundle.XMPPConnection)
	 */
	@Override
	public void connectionConnected(XMPPConnection connection)
	{
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiconnectionbundle.listener.ConnectionListener#exceptionCaught(net.sf.mxlosgi.mxlosgiconnectionbundle.XMPPConnection, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(XMPPConnection connection, Throwable cause)
	{
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiconnectionbundle.listener.ConnectionListener#resourceBinded(net.sf.mxlosgi.mxlosgiconnectionbundle.XMPPConnection)
	 */
	@Override
	public void resourceBinded(XMPPConnection connection)
	{
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiconnectionbundle.listener.ConnectionListener#saslFailed(net.sf.mxlosgi.mxlosgiconnectionbundle.XMPPConnection, net.sf.mxlosgi.mxlosgixmppbundle.Failure)
	 */
	@Override
	public void saslFailed(XMPPConnection connection, Failure failure)
	{
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiconnectionbundle.listener.ConnectionListener#saslSuccessful(net.sf.mxlosgi.mxlosgiconnectionbundle.XMPPConnection)
	 */
	@Override
	public void saslSuccessful(XMPPConnection connection)
	{
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiconnectionbundle.listener.ConnectionListener#sessionBinded(net.sf.mxlosgi.mxlosgiconnectionbundle.XMPPConnection)
	 */
	@Override
	public void sessionBinded(XMPPConnection connection)
	{
	}
	
	@Override
	public void connectionStreamError(XMPPConnection connection, StreamError streamError)
	{
	}

}
