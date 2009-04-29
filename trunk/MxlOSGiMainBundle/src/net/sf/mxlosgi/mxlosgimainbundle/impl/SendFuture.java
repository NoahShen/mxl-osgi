/**
 * 
 */
package net.sf.mxlosgi.mxlosgimainbundle.impl;

import org.apache.mina.common.WriteFuture;

import net.sf.mxlosgi.mxlosgimainbundle.Future;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;

/**
 * @author noah
 *
 */
public class SendFuture implements Future
{
	private WriteFuture writeFuture;
	
	private XMPPConnection connection;
	
	private boolean intercepted = false;
	/**
	 * @param writeFuture
	 * @param connection
	 */
	public SendFuture(XMPPConnection connection)
	{
		this.connection = connection;
	}

	/**
	 * @param writeFuture the writeFuture to set
	 */
	void setWriteFuture(WriteFuture writeFuture)
	{
		this.writeFuture = writeFuture;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.Future#complete()
	 */
	@Override
	public void complete()
	{
		if (intercepted)
		{
			return;
		}
		
		writeFuture.join();
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgimainbundle.Future#complete(int)
	 */
	@Override
	public boolean complete(int timeout)
	{
		if (intercepted)
		{
			return false;
		}
		return writeFuture.join(timeout);
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
		if (intercepted)
		{
			return false;
		}
		return writeFuture.isWritten();
	}
	
	public boolean isIntercepted()
	{
		return intercepted;
	}

	/**
	 * @param intercepted the intercepted to set
	 */
	void setIntercepted(boolean intercepted)
	{
		this.intercepted = intercepted;
	}

}
