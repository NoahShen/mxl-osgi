/**
 * 
 */
package net.sf.mxlosgi.mxlosgimainbundle.impl;

import net.sf.mxlosgi.mxlosgimainbundle.Future;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;

/**
 * @author noah
 *
 */
public class ConnectFuture implements Future
{
	private XMPPConnection connection;
	
	/**
	 * @param connection
	 */
	public ConnectFuture(XMPPConnection connection)
	{
		this.connection = connection;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.Future#complete()
	 */
	@Override
	public void complete()
	{
		if (connection.isConnected())
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
		if (connection.isConnected())
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
		return connection.isConnected();
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.Future#getConnection()
	 */
	@Override
	public XMPPConnection getConnection()
	{
		return connection;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.Future#isCompleted()
	 */
	@Override
	public boolean isCompleted()
	{
		return connection.isConnected();
	}

}
