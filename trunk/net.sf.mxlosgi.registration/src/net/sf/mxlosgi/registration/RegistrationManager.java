/**
 * 
 */
package net.sf.mxlosgi.registration;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;


/**
 * @author noah
 *
 */
public interface RegistrationManager
{
	
	/**
	 * 
	 * @return
	 */
	public boolean isSupportRegistration(XmppConnection connection);
	
	/**
	 * 
	 * @param registerExtension
	 */
	public void registerAccount(XmppConnection connection, RegisterExtension registerExtension) throws XmppException;
	
	/**
	 * 
	 * @return
	 */
	public RegisterExtension getRegisterExtension(XmppConnection connection) throws XmppException;
	
	/**
	 * 
	 * @param newPassword
	 */
	public void changePassword(XmppConnection connection, String newPassword) throws XmppException;
	
	/**
	 * 
	 */
	public void unregister(XmppConnection connection) throws XmppException;
}
