/**
 * 
 */
package net.sf.mxlosgi.mxlosgiregistrationbundle;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;


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
	public boolean isSupportRegistration(XMPPConnection connection);
	
	/**
	 * 
	 * @param registerExtension
	 */
	public void registerAccount(XMPPConnection connection, RegisterExtension registerExtension) throws XMPPException;
	
	/**
	 * 
	 * @return
	 */
	public RegisterExtension getRegisterExtension(XMPPConnection connection) throws XMPPException;
	
	/**
	 * 
	 * @param newPassword
	 */
	public void changePassword(XMPPConnection connection, String newPassword) throws XMPPException;
	
	/**
	 * 
	 */
	public void unregister(XMPPConnection connection) throws XMPPException;
}
