/**
 * 
 */
package net.sf.mxlosgi.registration.impl;

import net.sf.mxlosgi.core.ServerTimeoutException;
import net.sf.mxlosgi.core.StanzaCollector;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.core.filter.PacketIDFilter;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.registration.RegisterExtension;
import net.sf.mxlosgi.registration.RegistrationManager;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.StreamFeature;
import net.sf.mxlosgi.xmpp.XmlStanza;


/**
 * @author noah
 *
 */
public class RegistrationManagerImpl implements RegistrationManager, StanzaReceListener
{

	/**
	 * @param connection
	 */
	public RegistrationManagerImpl()
	{
	}

	@Override
	public RegisterExtension getRegisterExtension(XmppConnection connection) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.get);
		RegisterExtension register = new RegisterExtension();
		iq.addExtension(register);
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
				return (RegisterExtension) iqResponse.getExtension(RegisterExtension.ELEMENTNAME, 
														RegisterExtension.NAMESPACE);
			}
			else if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
		
		return null;
	}

	@Override
	public boolean isSupportRegistration(XmppConnection connection)
	{
		return connection.getProperty("supportRegistration") == Boolean.TRUE;
	}

	@Override
	public void registerAccount(XmppConnection connection, RegisterExtension registerExtension) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.set);
		iq.addExtension(registerExtension);
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
			if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
		
	}

	@Override
	public void unregister(XmppConnection connection) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.set);
		RegisterExtension register = new RegisterExtension();
		register.getFields().put("remove", null);
		iq.addExtension(register);
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
			if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
		
	}

	@Override
	public void changePassword(XmppConnection connection, String newPassword) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.set);
		RegisterExtension register = new RegisterExtension();
		register.getFields().put("username", connection.getJid().getNode());
		register.getFields().put("password", newPassword);
		iq.addExtension(register);
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
			if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
	}
	
	@Override
	public void processReceStanza(XmppConnection connection, XmlStanza stanza)
	{
		StreamFeature streamFeature = (StreamFeature) stanza;
		for (StreamFeature.Feature feature : streamFeature.getFeatures())
		{
			if ("register".equals(feature.getElementName())
					&& "http://jabber.org/features/iq-register".equals(feature.getNamespace()))
			{
				connection.setProperty("supportRegistration");
				break;
			}
		}
	}


}
