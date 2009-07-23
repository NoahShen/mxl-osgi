/**
 * 
 */
package net.sf.mxlosgi.core;

import net.sf.mxlosgi.xmpp.XmppError;


/**
 * @author noah
 *
 */
public class ServerTimeoutException extends XmppException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2222656231447179320L;

	/**
	 * 
	 */
	public ServerTimeoutException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServerTimeoutException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ServerTimeoutException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public ServerTimeoutException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param error
	 */
	public ServerTimeoutException(XmppError error)
	{
		super(error);
	}

}
