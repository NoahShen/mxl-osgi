/**
 * 
 */
package net.sf.mxlosgi.core;

import net.sf.mxlosgi.xmpp.XMPPError;


/**
 * @author noah
 *
 */
public class TlsRequiredExcetpion extends XmppException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5660907305466125734L;

	/**
	 * 
	 */
	public TlsRequiredExcetpion()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public TlsRequiredExcetpion(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public TlsRequiredExcetpion(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public TlsRequiredExcetpion(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param error
	 */
	public TlsRequiredExcetpion(XMPPError error)
	{
		super(error);
	}
	
	

}
