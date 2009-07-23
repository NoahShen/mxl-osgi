/**
 * 
 */
package net.sf.mxlosgi.core;

import net.sf.mxlosgi.xmpp.XmppError;


/**
 * @author noah
 *
 */
public class BindResourceException extends XmppException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2464823878695460067L;

	/**
	 * 
	 */
	public BindResourceException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BindResourceException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public BindResourceException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public BindResourceException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param error
	 */
	public BindResourceException(XmppError error)
	{
		super(error);
	}

}
