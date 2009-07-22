/**
 * 
 */
package net.sf.mxlosgi.core;

import net.sf.mxlosgi.xmpp.StreamError;
import net.sf.mxlosgi.xmpp.XMPPError;


/**
 * @author noah
 *
 */
public class XmppException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2059295743079750074L;
	
	protected XMPPError error;
	
	protected StreamError streamError;
	
	/**
	 * @param error
	 */
	public XmppException(XMPPError error)
	{
		this.error = error;
	}

	/**
	 * 
	 */
	public XmppException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public XmppException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public XmppException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public XmppException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param streamError
	 */
	public XmppException(StreamError streamError)
	{
		this.streamError = streamError;
	}

	public XMPPError getError()
	{
		return error;
	}

	public void setError(XMPPError error)
	{
		this.error = error;
	}

	/**
	 * @return the streamError
	 */
	public StreamError getStreamError()
	{
		return streamError;
	}

	/**
	 * @param streamError the streamError to set
	 */
	public void setStreamError(StreamError streamError)
	{
		this.streamError = streamError;
	}

}
