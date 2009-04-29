package net.sf.mxlosgi.mxlosgisoftwareversionbundle;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

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
	public SoftwareVersionExtension getSoftwareVersion(XMPPConnection connection, JID to) throws XMPPException;
}