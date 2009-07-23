/**
 * 
 */
package net.sf.mxlosgi.core;

import net.sf.mxlosgi.xmpp.Failure;
import net.sf.mxlosgi.xmpp.StreamError;
import net.sf.mxlosgi.xmpp.XmppError;

/**
 * @author noah
 *
 */
public class CompressFailureException extends XmppException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4792781646458870304L;

	private Failure failure;

	/**
	 * 
	 */
	public CompressFailureException()
	{
		super();
	}

	/**
	 * @param streamError
	 */
	public CompressFailureException(StreamError streamError)
	{
		super(streamError);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CompressFailureException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public CompressFailureException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public CompressFailureException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param error
	 */
	public CompressFailureException(XmppError error)
	{
		super(error);
	}

	
	/**
	 * @param error
	 * @param failure
	 */
	public CompressFailureException(Failure failure)
	{
		super();
		this.failure = failure;
	}

	/**
	 * @return the failure
	 */
	public Failure getFailure()
	{
		return failure;
	}
	
	
}
