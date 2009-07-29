package net.sf.mxlosgi.softwareversion;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.xmpp.JID;

/**
 * @author noah
 *
 */
public interface SoftwareVersionManager
{

	/**
	 * @return the name
	 */
	public String getName();

	/**
	 * @param name the name to set
	 */
	public void setName(String name);

	/**
	 * @return the version
	 */
	public String getVersion();

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version);
	
	/**
	 * 
	 * @param connection
	 * @param to
	 * @return
	 * @throws XMPPException
	 */
	public SoftwareVersionExtension getSoftwareVersion(XmppConnection connection, JID to) throws XmppException;
}