/**
 * 
 */
package net.sf.mxlosgi.core;

import net.sf.mxlosgi.xmpp.XMPPError;

/**
 * @author noah
 *
 */
public class BindSessionException extends XmppException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4493519185635102363L;

	/**
	 * 
	 */
	public BindSessionException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BindSessionException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public BindSessionException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public BindSessionException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param error
	 */
	public BindSessionException(XMPPError error)
	{
		super(error);
	}

	
}
