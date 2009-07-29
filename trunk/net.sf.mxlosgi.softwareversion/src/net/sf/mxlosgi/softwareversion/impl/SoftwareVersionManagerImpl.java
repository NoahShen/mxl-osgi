package net.sf.mxlosgi.softwareversion.impl;

import net.sf.mxlosgi.core.ServerTimeoutException;
import net.sf.mxlosgi.core.StanzaCollector;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.core.filter.PacketIDFilter;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.softwareversion.SoftwareVersionExtension;
import net.sf.mxlosgi.softwareversion.SoftwareVersionManager;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.XmlStanza;

/**
 * @author noah
 *
 */
public class SoftwareVersionManagerImpl implements SoftwareVersionManager, StanzaReceListener
{	
	private String name;
	
	private String version;
	
	/**
	 * @param connection
	 */
	public SoftwareVersionManagerImpl()
	{
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the version
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}

	private String getOS()
	{
		return System.getProperty("os.name") + " "+ System.getProperty("os.arch") + " "+ System.getProperty("os.version");
	}

	@Override
	public SoftwareVersionExtension getSoftwareVersion(XmppConnection connection, JID to) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.get);
		iq.setTo(to);
		SoftwareVersionExtension softwareversion = new SoftwareVersionExtension();
		iq.addExtension(softwareversion);
		
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XmlStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (stanza == null)
		{
			throw new ServerTimeoutException("remote server no response");
		}
		if (stanza instanceof Iq)
		{
			Iq iqResponse = (Iq) stanza;
			Iq.Type type = iqResponse.getType();
			if (type == Iq.Type.result)
			{
				SoftwareVersionExtension softwareversionResponse
					= (SoftwareVersionExtension) iqResponse.getExtension(SoftwareVersionExtension.ELEMENTNAME, 
													SoftwareVersionExtension.NAMESPACE);
				return softwareversionResponse;
			}
			else if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
		return null;
	}

	@Override
	public void processReceStanza(XmppConnection connection, XmlStanza data)
	{
		Iq iq = (Iq) data;
		JID from = iq.getFrom();
		
		Iq iqResponse = new Iq(Iq.Type.result);
		iqResponse.setTo(from);
		iqResponse.setStanzaID(iq.getStanzaID());
		
		SoftwareVersionExtension softwareversion = new SoftwareVersionExtension();
		softwareversion.setName(getName());
		softwareversion.setVersion(getVersion());
		String os = getOS();
		if (os != null && !os.isEmpty())
		{
			softwareversion.setOs(os);
		}
		
		iqResponse.addExtension(softwareversion);
		
		connection.sendStanza(iqResponse);
	}

}