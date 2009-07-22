/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.listener.ConnectionListener;
import net.sf.mxlosgi.xmpp.Failure;
import net.sf.mxlosgi.xmpp.StreamError;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class ConnectionListenerServiceTracker extends ServiceTracker
{

	public ConnectionListenerServiceTracker(BundleContext context)
	{
		super(context, ConnectionListener.class.getName(), null);
	}

	
	public void fireStreamError(XmppConnection connection, StreamError streamError)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ConnectionListener listener  = (ConnectionListener) obj;
			listener.connectionStreamError(connection, streamError);
		}
	}
	
	public void fireSaslFailed(XmppConnection connection, Failure failure)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ConnectionListener listener  = (ConnectionListener) obj;
			listener.saslFailed(connection, failure);
		}
	}
	
	public void fireConnectionCreated(XmppConnection connection)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ConnectionListener listener  = (ConnectionListener) obj;
			listener.connectionCreated(connection);
		}
	}
	
	public void fireConnectionClosed(XmppConnection connection)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ConnectionListener listener  = (ConnectionListener) obj;
			listener.connectionClosed(connection);
		}
	}
	
	public void fireSaslSuccess(XmppConnection connection)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ConnectionListener listener  = (ConnectionListener) obj;
			listener.saslSuccessful(connection);
		}
	}
	
	public void fireSessionBinded(XmppConnection connection)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ConnectionListener listener  = (ConnectionListener) obj;
			listener.sessionBinded(connection);
		}
	}
	
	public void fireConnected(XmppConnection connection)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ConnectionListener listener  = (ConnectionListener) obj;
			listener.connectionConnected(connection);
		}
	}
	
	public void fireExceptionCaught(XmppConnection connection, Throwable cause)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ConnectionListener listener  = (ConnectionListener) obj;
			listener.exceptionCaught(connection, cause);
		}
	}
	
	public void fireResourceBinded(XmppConnection connection)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			ConnectionListener listener  = (ConnectionListener) obj;
			listener.resourceBinded(connection);
		}
	}
}
