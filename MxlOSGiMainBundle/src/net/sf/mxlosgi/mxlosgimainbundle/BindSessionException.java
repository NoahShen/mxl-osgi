/**
 * 
 */
package net.sf.mxlosgi.mxlosgimainbundle;

import net.sf.mxlosgi.mxlosgixmppbundle.XMPPError;

/**
 * @author noah
 *
 */
public class BindSessionException extends XMPPException
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
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BindSessionException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public BindSessionException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public BindSessionException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param error
	 */
	public BindSessionException(XMPPError error)
	{
		super(error);
		// TODO Auto-generated constructor stub
	}

	
}
