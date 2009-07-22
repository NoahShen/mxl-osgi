/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import net.sf.mxlosgi.core.Future;
import net.sf.mxlosgi.core.XmppConnection;


/**
 * @author noah
 *
 */
public class LoginFuture implements Future
{
	
	private XmppConnection connection;
	
	
	/**
	 * @param connection
	 */
	public LoginFuture(XmppConnection connection)
	{
		this.connection = connection;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.Future#complete()
	 */
	@Override
	public void complete()
	{
		if (!connection.isConnected())
		{
			return;
		}
		
		if (connection.isSessionBinded())
		{
			return;
		}
		synchronized (this)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.Future#complete(int)
	 */
	@Override
	public boolean complete(int timeout)
	{
		if (!connection.isConnected())
		{
			return false;
		}
		
		if (connection.isSessionBinded())
		{
			return true;
		}
		synchronized (this)
		{
			try
			{
				wait(timeout);
			}
			catch (InterruptedException e)
			{
			}
		}
		return connection.isSessionBinded();
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.Future#getConnection()
	 */
	@Override
	public XmppConnection getConnection()
	{
		return connection;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.Future#isCompleted()
	 */
	@Override
	public boolean isCompleted()
	{
		return connection.isSessionBinded();
	}

}
