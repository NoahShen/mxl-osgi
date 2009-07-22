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
public class CloseFuture implements Future
{
	private XmppConnection connection;
	
	private org.apache.mina.common.CloseFuture future;
	
	

	/**
	 * @param connection
	 * @param future
	 */
	public CloseFuture(XmppConnection connection, org.apache.mina.common.CloseFuture future)
	{
		this.connection = connection;
		this.future = future;
	}

	@Override
	public void complete()
	{
		future.join();
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.Future#complete(int)
	 */
	@Override
	public boolean complete(int timeout)
	{
		return future.join(timeout);
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
		return future.isClosed();
	}

}
