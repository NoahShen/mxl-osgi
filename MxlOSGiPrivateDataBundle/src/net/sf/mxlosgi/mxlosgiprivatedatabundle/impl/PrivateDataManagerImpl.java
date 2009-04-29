/**
 * 
 */
package net.sf.mxlosgi.mxlosgiprivatedatabundle.impl;

import net.sf.mxlosgi.mxlosgimainbundle.ServerTimeoutException;
import net.sf.mxlosgi.mxlosgimainbundle.StanzaCollector;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgimainbundle.filter.PacketIDFilter;
import net.sf.mxlosgi.mxlosgiprivatedatabundle.PrivateDataExtension;
import net.sf.mxlosgi.mxlosgiprivatedatabundle.PrivateDataManager;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

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

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiprivatedatabundle.PrivateDataManager#getPrivateData(java.lang.String, java.lang.String)
	 */
	@Override
	public PacketExtension getPrivateData(XMPPConnection connection, final String elementName, final String namespace) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.get);
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
				PrivateDataExtension privateDataResult = (PrivateDataExtension) iqResponse.getExtension(PrivateDataExtension.ELEMENTNAME, 
													PrivateDataExtension.NAMESPACE);
				return privateDataResult.getPrivateDataExtension();
			}
			else if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiprivatedatabundle.PrivateDataManager#storePrivateData(net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension)
	 */
	@Override
	public void storePrivateData(XMPPConnection connection, PacketExtension extension) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.set);
		PrivateDataExtension privateData = new PrivateDataExtension();
		privateData.setPrivateDataExtension(extension);
		iq.addExtension(privateData);
		
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

			}
			else if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
		}
	}


}
