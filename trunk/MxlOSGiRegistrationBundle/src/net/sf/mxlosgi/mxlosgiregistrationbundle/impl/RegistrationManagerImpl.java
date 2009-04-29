/**
 * 
 */
package net.sf.mxlosgi.mxlosgiregistrationbundle.impl;

import net.sf.mxlosgi.mxlosgimainbundle.ServerTimeoutException;
import net.sf.mxlosgi.mxlosgimainbundle.StanzaCollector;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgimainbundle.filter.PacketIDFilter;
import net.sf.mxlosgi.mxlosgimainbundle.listener.StanzaReceListener;
import net.sf.mxlosgi.mxlosgiregistrationbundle.RegisterExtension;
import net.sf.mxlosgi.mxlosgiregistrationbundle.RegistrationManager;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.StreamFeature;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

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
	public RegisterExtension getRegisterExtension(XMPPConnection connection) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.get);
		RegisterExtension register = new RegisterExtension();
		iq.addExtension(register);
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
				return (RegisterExtension) iqResponse.getExtension(RegisterExtension.ELEMENTNAME, 
														RegisterExtension.NAMESPACE);
			}
			else if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
		}
		
		return null;
	}

	@Override
	public boolean isSupportRegistration(XMPPConnection connection)
	{
		return connection.getAttribute("supportRegistration") == Boolean.TRUE;
	}

	@Override
	public void registerAccount(XMPPConnection connection, RegisterExtension registerExtension) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.set);
		iq.addExtension(registerExtension);
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
			if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
		}
		
	}

	@Override
	public void unregister(XMPPConnection connection) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.set);
		RegisterExtension register = new RegisterExtension();
		register.getFields().put("remove", null);
		iq.addExtension(register);
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
			if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
		}
		
	}

	@Override
	public void changePassword(XMPPConnection connection, String newPassword) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.set);
		RegisterExtension register = new RegisterExtension();
		register.getFields().put("username", connection.getJid().getNode());
		register.getFields().put("password", newPassword);
		iq.addExtension(register);
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
			if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
		}
	}
	
	@Override
	public void processReceStanza(XMPPConnection connection, XMLStanza stanza)
	{
		StreamFeature streamFeature = (StreamFeature) stanza;
		for (StreamFeature.Feature feature : streamFeature.getFeatures())
		{
			if ("register".equals(feature.getElementName())
					&& "http://jabber.org/features/iq-register".equals(feature.getNamespace()))
			{
				connection.setAttribute("supportRegistration");
				break;
			}
		}
	}


}
