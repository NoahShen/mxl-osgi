/**
 * 
 */
package net.sf.mxlosgi.core;

import net.sf.mxlosgi.xmpp.Failure;
import net.sf.mxlosgi.xmpp.XmppError;


/**
 * @author noah
 *
 */
public class TlsFailedException extends XmppException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2992260597792369913L;

	private Failure failure;
	
	/**
	 * 
	 */
	public TlsFailedException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public TlsFailedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public TlsFailedException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public TlsFailedException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param error
	 */
	public TlsFailedException(XmppError error)
	{
		super(error);
	}

	/**
	 * @param failure
	 */
	public TlsFailedException(Failure failure)
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
