package net.sf.mxlosgi.mxlosgisoftwareversionbundle.impl;

import net.sf.mxlosgi.mxlosgimainbundle.ServerTimeoutException;
import net.sf.mxlosgi.mxlosgimainbundle.StanzaCollector;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgimainbundle.filter.PacketIDFilter;
import net.sf.mxlosgi.mxlosgimainbundle.listener.StanzaReceListener;
import net.sf.mxlosgi.mxlosgisoftwareversionbundle.SoftwareVersionExtension;
import net.sf.mxlosgi.mxlosgisoftwareversionbundle.SoftwareVersionManager;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

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
	public SoftwareVersionExtension getSoftwareVersion(XMPPConnection connection, JID to) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.get);
		iq.setTo(to);
		SoftwareVersionExtension softwareversion = new SoftwareVersionExtension();
		iq.addExtension(softwareversion);
		
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XMLStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (stanza == null)
		{
			throw new ServerTimeoutException("remote server no response");
		}
		if (stanza instanceof IQ)
		{
			IQ iqResponse = (IQ) stanza;
			IQ.Type type = iqResponse.getType();
			if (type == IQ.Type.result)
			{
				SoftwareVersionExtension softwareversionResponse
					= (SoftwareVersionExtension) iqResponse.getExtension(SoftwareVersionExtension.ELEMENTNAME, 
													SoftwareVersionExtension.NAMESPACE);
				return softwareversionResponse;
			}
			else if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
		}
		return null;
	}

	@Override
	public void processReceStanza(XMPPConnection connection, XMLStanza data)
	{
		IQ iq = (IQ) data;
		JID from = iq.getFrom();
		
		IQ iqResponse = new IQ(IQ.Type.result);
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