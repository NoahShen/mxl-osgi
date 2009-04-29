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
public class CloseFuture implements Future
{
	private XMPPConnection connection;
	
	private org.apache.mina.common.CloseFuture future;
	
	

	/**
	 * @param connection
	 * @param future
	 */
	public CloseFuture(XMPPConnection connection, org.apache.mina.common.CloseFuture future)
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
		return future.isClosed();
	}

}
