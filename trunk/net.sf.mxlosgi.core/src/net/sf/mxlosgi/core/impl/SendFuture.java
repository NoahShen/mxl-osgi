/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import net.sf.mxlosgi.core.Future;
import net.sf.mxlosgi.core.XmppConnection;

import org.apache.mina.common.WriteFuture;


/**
 * @author noah
 *
 */
public class SendFuture implements Future
{
	private WriteFuture writeFuture;
	
	private XmppConnection connection;
	
	private boolean intercepted = false;
	/**
	 * @param writeFuture
	 * @param connection
	 */
	public SendFuture(XmppConnection connection)
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
