package net.sf.mxlosgi.lastactivity.impl;


import net.sf.mxlosgi.core.ServerTimeoutException;
import net.sf.mxlosgi.core.StanzaCollector;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.core.filter.PacketIDFilter;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.lastactivity.LastActivityManager;
import net.sf.mxlosgi.lastactivity.LastActivityPacketExtension;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.XmlStanza;

import org.jdesktop.jdic.systeminfo.SystemInfo;


/**
 * @author noah
 * 
 */
public class LastActivityManagerImpl implements LastActivityManager, StanzaReceListener, Runnable
{

	private Thread idleThread;
	
	private boolean stopIdleThread = false;
	
	private LastActivityListenerServiceTracker lastActivityListenerServiceTracker;
	
	
	public LastActivityManagerImpl(LastActivityListenerServiceTracker lastActivityListenerServiceTracker)
	{
		this.lastActivityListenerServiceTracker = lastActivityListenerServiceTracker;
		idleThread = new Thread(this, "idleThread");
	}

	@Override
	public long getIdleTime()
	{
		long now = SystemInfo.getSessionIdleTime();
		return (now / 1000);
	}

	@Override
	public LastActivityPacketExtension getLastActivity(XmppConnection connection, JID to) throws XmppException
	{

		Iq iq = new Iq(Iq.Type.get);
		iq.setTo(to);

		LastActivityPacketExtension activity = new LastActivityPacketExtension();
		iq.addExtension(activity);

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
				LastActivityPacketExtension activityXML
					= (LastActivityPacketExtension) iqResponse.getExtension(
							LastActivityPacketExtension.ELEMENTNAME, LastActivityPacketExtension.NAMESPACE);
				return activityXML;
			}
			else if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
		return null;
	}

	public void updateIdleThread()
	{
		if (lastActivityListenerServiceTracker.size() > 0 && !idleThread.isAlive())
		{
			idleThread.start();
		}
		else if (lastActivityListenerServiceTracker.size() <= 0 && idleThread.isAlive())
		{
			stopIdleThread = true;
		}
	}
	
	@Override
	public void processReceStanza(XmppConnection connection, XmlStanza data)
	{
		Iq iq = (Iq) data;
		JID from = iq.getFrom();
		String stanzaID = iq.getStanzaID();
		
		Iq iqResponse = new Iq(Iq.Type.result);
		iqResponse.setTo(from);
		iqResponse.setStanzaID(stanzaID);
		
		LastActivityPacketExtension activity = new LastActivityPacketExtension();
		activity.setSeconds(getIdleTime());
		
		iqResponse.addExtension(activity);
		
		connection.sendStanza(iqResponse);
	}

	@Override
	public void run()
	{
		while (!stopIdleThread)
		{
			long idleSecond = getIdleTime();
			lastActivityListenerServiceTracker.fireLastActivity(idleSecond);
			
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
			}
		}
	}


}