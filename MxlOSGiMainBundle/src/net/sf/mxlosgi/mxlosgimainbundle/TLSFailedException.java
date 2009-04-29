/**
 * 
 */
package net.sf.mxlosgi.mxlosgimainbundle;

import net.sf.mxlosgi.mxlosgixmppbundle.Failure;
import net.sf.mxlosgi.mxlosgixmppbundle.XMPPError;

/**
 * @author noah
 *
 */
public class TLSFailedException extends XMPPException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2992260597792369913L;

	private Failure failure;
	
	/**
	 * 
	 */
	public TLSFailedException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public TLSFailedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public TLSFailedException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public TLSFailedException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param error
	 */
	public TLSFailedException(XMPPError error)
	{
		super(error);
	}

	/**
	 * @param failure
	 */
	public TLSFailedException(Failure failure)
	{
		this.failure = failure;
	}

	/**
	 * @return the failure
	 */
	public Failure getFailure()
	{
		return failure;
	}

	/**
	 * @param failure the failure to set
	 */
	public void setFailure(Failure failure)
	{
		this.failure = failure;
	}
	
}
