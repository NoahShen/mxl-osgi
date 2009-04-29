/**
 * 
 */
package net.sf.mxlosgi.mxlosgimainbundle;

import net.sf.mxlosgi.mxlosgixmppbundle.XMPPError;

/**
 * @author noah
 *
 */
public class ServerTimeoutException extends XMPPException
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
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServerTimeoutException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ServerTimeoutException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ServerTimeoutException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param error
	 */
	public ServerTimeoutException(XMPPError error)
	{
		super(error);
		// TODO Auto-generated constructor stub
	}

}
