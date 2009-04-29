/**
 * 
 */
package net.sf.mxlosgi.mxlosgiprivacybundle.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.mxlosgi.mxlosgimainbundle.ServerTimeoutException;
import net.sf.mxlosgi.mxlosgimainbundle.StanzaCollector;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgimainbundle.filter.PacketIDFilter;
import net.sf.mxlosgi.mxlosgimainbundle.listener.StanzaReceListener;
import net.sf.mxlosgi.mxlosgiprivacybundle.PrivacyManager;
import net.sf.mxlosgi.mxlosgiprivacybundle.listener.PrivacyListener;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.Privacy;
import net.sf.mxlosgi.mxlosgixmppbundle.PrivacyList;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class PrivacyManagerImpl implements PrivacyManager, StanzaReceListener
{	
	private final List<PrivacyListener> privacyListeners = new CopyOnWriteArrayList<PrivacyListener>();
	
	/**
	 * @param connection
	 */
	public PrivacyManagerImpl()
	{
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiprivacybundle.PrivacyManager#addPrivacyListener(net.sf.mxlosgi.mxlosgiprivacybundle.listener.PrivacyListener)
	 */
	@Override
	public void addPrivacyListener(PrivacyListener privacyListener)
	{
		privacyListeners.add(privacyListener);
	}

	@Override
	public void createOrUpdatePrivacyList(XMPPConnection connection, PrivacyList privacyList) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.set);
		Privacy privacy = new Privacy();
		privacy.addPrivacyList(privacyList);
		iq.addExtension(privacy);
		
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
	public void declineActiveList(XMPPConnection connection) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.set);
		Privacy privacy = new Privacy();
		privacy.setDeclineActiveList(true);
		iq.addExtension(privacy);
		
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
	public void declineDefaultList(XMPPConnection connection) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.set);
		Privacy privacy = new Privacy();
		privacy.setDeclineDefaultList(true);
		iq.addExtension(privacy);
		
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
	public void deletePrivacyList(XMPPConnection connection, String listName) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.set);
		Privacy privacy = new Privacy();
		PrivacyList list = new PrivacyList(listName);
		privacy.addPrivacyList(list);
		iq.addExtension(privacy);
		
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
	public PrivacyList getPrivacyList(XMPPConnection connection, String listName) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.get);
		Privacy privacy = new Privacy();
		PrivacyList list = new PrivacyList(listName);
		privacy.addPrivacyList(list);
		iq.addExtension(privacy);
		
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
			else if (type == IQ.Type.result)
			{
				Privacy privacyResponse = 
					(Privacy) iqResponse.getExtension(Privacy.ELEMENTNAME, Privacy.NAMESPACE);
				if (privacyResponse != null)
				{
					return privacyResponse.getPrivacyLists().iterator().next();
				}
			}
		}
		return null;
	}

	@Override
	public Privacy getPrivacyLists(XMPPConnection connection) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.get);
		Privacy privacy = new Privacy();
		iq.addExtension(privacy);
		
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
			else if (type == IQ.Type.result)
			{
				Privacy privacyResponse = 
					(Privacy) iqResponse.getExtension(Privacy.ELEMENTNAME, Privacy.NAMESPACE);
				return privacyResponse;
			}
		}
		return null;
	}

	@Override
	public void removePrivacyListener(PrivacyListener privacyListener)
	{
		privacyListeners.remove(privacyListeners);
	}

	@Override
	public void setActiveListName(XMPPConnection connection, String listName) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.set);
		Privacy privacy = new Privacy();
		privacy.setActiveName(listName);
		iq.addExtension(privacy);
		
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
	public void setDefaultListName(XMPPConnection connection, String listName) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.set);
		Privacy privacy = new Privacy();
		privacy.setDefaultName(listName);
		iq.addExtension(privacy);
		
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
		IQ iq = (IQ) stanza;
		Privacy privacy = 
			(Privacy) iq.getExtension(Privacy.ELEMENTNAME, Privacy.NAMESPACE);

		if (privacy == null)
		{
			return;
		}
		for (PrivacyList list : privacy.getPrivacyLists())
		{
			firePrivacyListCreatedOrUpdated(connection, list.getListName());
		}
	}

	private void firePrivacyListCreatedOrUpdated(XMPPConnection connection, String listName)
	{
		for (PrivacyListener listener : privacyListeners)
		{
			listener.privacyListUpdated(connection, listName);
		}
	}

}
