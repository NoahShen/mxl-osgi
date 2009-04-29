/**
 * 
 */
package net.sf.mxlosgi.mxlosgimainbundle;

import net.sf.mxlosgi.mxlosgixmppbundle.XMPPError;

/**
 * @author noah
 *
 */
public class TLSRequiredExcetpion extends XMPPException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5660907305466125734L;

	/**
	 * 
	 */
	public TLSRequiredExcetpion()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public TLSRequiredExcetpion(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public TLSRequiredExcetpion(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public TLSRequiredExcetpion(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param error
	 */
	public TLSRequiredExcetpion(XMPPError error)
	{
		super(error);
		// TODO Auto-generated constructor stub
	}
	
	

}
