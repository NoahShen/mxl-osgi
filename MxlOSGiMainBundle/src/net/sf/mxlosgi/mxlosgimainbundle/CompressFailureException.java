/**
 * 
 */
package net.sf.mxlosgi.mxlosgimainbundle;

import net.sf.mxlosgi.mxlosgixmppbundle.Failure;
import net.sf.mxlosgi.mxlosgixmppbundle.StreamError;
import net.sf.mxlosgi.mxlosgixmppbundle.XMPPError;

/**
 * @author noah
 *
 */
public class CompressFailureException extends XMPPException
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
	public CompressFailureException(XMPPError error)
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
