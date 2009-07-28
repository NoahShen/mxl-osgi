/**
 * 
 */
package net.sf.mxlosgi.privatedata.impl;

import net.sf.mxlosgi.core.ServerTimeoutException;
import net.sf.mxlosgi.core.StanzaCollector;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.core.filter.PacketIDFilter;
import net.sf.mxlosgi.privatedata.PrivateDataExtension;
import net.sf.mxlosgi.privatedata.PrivateDataManager;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmpp.XmlStanza;

/**
 * @author noah
 *
 */
public class PrivateDataManagerImpl implements PrivateDataManager
{
	
	/**
	 * @param connection
	 */
	public PrivateDataManagerImpl()
	{
	}


	@Override
	public PacketExtension getPrivateData(XmppConnection connection, final String elementName, final String namespace) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.get);
		PrivateDataExtension privateData = new PrivateDataExtension();
		privateData.setPrivateDataExtension(new PacketExtension(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 0L;

			@Override
			public String getElementName()
			{
				return elementName;
			}

			@Override
			public String getNamespace()
			{
				return namespace;
			}

			@Override
			public String toXML()
			{
				StringBuilder buf = new StringBuilder();
				buf.append("<").append(getElementName())
					.append(" xmlns=\"").append(getNamespace()).append("\"/>");
				return buf.toString();
			}
			
			@Override
			public Object clone() throws CloneNotSupportedException
			{
				return super.clone();
			}
		});
		iq.addExtension(privateData);
		
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
				PrivateDataExtension privateDataResult = (PrivateDataExtension) iqResponse.getExtension(PrivateDataExtension.ELEMENTNAME, 
													PrivateDataExtension.NAMESPACE);
				return privateDataResult.getPrivateDataExtension();
			}
			else if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
		
		return null;
	}

	@Override
	public void storePrivateData(XmppConnection connection, PacketExtension extension) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.set);
		PrivateDataExtension privateData = new PrivateDataExtension();
		privateData.setPrivateDataExtension(extension);
		iq.addExtension(privateData);
		
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

			}
			else if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
	}


}
