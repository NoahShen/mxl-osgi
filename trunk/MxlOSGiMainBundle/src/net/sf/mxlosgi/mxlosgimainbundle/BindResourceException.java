/**
 * 
 */
package net.sf.mxlosgi.mxlosgimainbundle;

import net.sf.mxlosgi.mxlosgixmppbundle.XMPPError;

/**
 * @author noah
 *
 */
public class BindResourceException extends XMPPException
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
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BindResourceException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public BindResourceException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public BindResourceException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param error
	 */
	public BindResourceException(XMPPError error)
	{
		super(error);
		// TODO Auto-generated constructor stub
	}

}
