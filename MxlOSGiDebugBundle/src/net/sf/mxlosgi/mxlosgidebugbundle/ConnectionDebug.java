/**
 * 
 */
package net.sf.mxlosgi.mxlosgidebugbundle;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.listener.ConnectionListener;
import net.sf.mxlosgi.mxlosgixmppbundle.Failure;
import net.sf.mxlosgi.mxlosgixmppbundle.StreamError;

/**
 * @author noah
 *
 */
public class ConnectionDebug implements ConnectionListener
{

	@Override
	public void connectionCreated(XMPPConnection connection)
	{
		System.out.println("*********connection:" + connection.getConnectionID() + " created*********");
	}
	
	@Override
	public void connectionClosed(XMPPConnection connection)
	{
		System.out.println("*********connection:" + connection.getConnectionID() + " closed*********");
	}

	@Override
	public void connectionConnected(XMPPConnection connection)
	{
		System.out.println("*********connection:" + connection.getConnectionID() + " connected*********");
	}

	@Override
	public void exceptionCaught(XMPPConnection connection, Throwable cause)
	{
		System.out.println("*********connection:" + connection.getConnectionID() + " exceptionCaught*********");
		cause.printStackTrace();
	}

	@Override
	public void resourceBinded(XMPPConnection connection)
	{
		System.out.println("*********connection:" + connection.getConnectionID() + " resourceBinded*********");
	}

	@Override
	public void saslFailed(XMPPConnection connection, Failure failure)
	{
		System.out.println("*********connection:" + connection.getConnectionID() + " saslFailed*********");
	}

	@Override
	public void saslSuccessful(XMPPConnection connection)
	{
		System.out.println("*********connection:" + connection.getConnectionID() + " saslSuccessful*********");
	}

	@Override
	public void sessionBinded(XMPPConnection connection)
	{
		System.out.println("*********connection:" + connection.getConnectionID() + " sessionBinded*********");
	}

	@Override
	public void connectionStreamError(XMPPConnection connection, StreamError streamError)
	{
		System.out.println("*********connection:" + connection.getConnectionID() + " StreamError*********");
	}


}
