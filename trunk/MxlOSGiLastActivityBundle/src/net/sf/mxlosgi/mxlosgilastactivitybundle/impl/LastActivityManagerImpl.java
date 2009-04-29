package net.sf.mxlosgi.mxlosgilastactivitybundle.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jdesktop.jdic.systeminfo.SystemInfo;

import net.sf.mxlosgi.mxlosgilastactivitybundle.LastActivityManager;
import net.sf.mxlosgi.mxlosgilastactivitybundle.LastActivityPacketExtension;
import net.sf.mxlosgi.mxlosgilastactivitybundle.listener.LastActivityListener;
import net.sf.mxlosgi.mxlosgimainbundle.ServerTimeoutException;
import net.sf.mxlosgi.mxlosgimainbundle.StanzaCollector;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgimainbundle.filter.PacketIDFilter;
import net.sf.mxlosgi.mxlosgimainbundle.listener.StanzaReceListener;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 * 
 */
public class LastActivityManagerImpl implements LastActivityManager, StanzaReceListener, Runnable
{

	private Thread idleThread;
	
	private boolean stopIdleThread = false;
	
	private Map<LastActivityListener, Long> lastActivityListeners = new ConcurrentHashMap<LastActivityListener, Long>();
	
	public LastActivityManagerImpl()
	{
		idleThread = new Thread(this);
	}

	/**
	 * The idle time is the lapsed time between the last message sent and
	 * now.
	 * 
	 * @return the lapsed time between the last message sent and now.
	 */
	private long getIdleTime()
	{
		long now = SystemInfo.getSessionIdleTime();
		return (now / 1000);
	}

	@Override
	public LastActivityPacketExtension getLastActivity(XMPPConnection connection, JID to) throws XMPPException
	{

		IQ iq = new IQ(IQ.Type.get);
		iq.setTo(to);

		LastActivityPacketExtension activity = new LastActivityPacketExtension();
		iq.addExtension(activity);

		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XMLStanza data = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (data == null)
		{
			throw new ServerTimeoutException("remote server no response");
		}
		if (data instanceof IQ)
		{
			IQ iqResponse = (IQ) data;
			IQ.Type type = iqResponse.getType();
			if (type == IQ.Type.result)
			{
				LastActivityPacketExtension activityXML
					= (LastActivityPacketExtension) iqResponse.getExtension(
							LastActivityPacketExtension.ELEMENTNAME, LastActivityPacketExtension.NAMESPACE);
				return activityXML;
			}
			else if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
		}
		return null;
	}

	@Override
	public void addLastActivityListener(LastActivityListener listener, long idleTime)
	{
		lastActivityListeners.put(listener, idleTime);
		updateIdleThread();
	}

	private void updateIdleThread()
	{
		if (!lastActivityListeners.isEmpty() && !idleThread.isAlive())
		{
			idleThread.start();
		}
		else if (lastActivityListeners.isEmpty() && idleThread.isAlive())
		{
			stopIdleThread = true;
		}
	}

	@Override
	public void removeLastActivityListener(LastActivityListener listener)
	{
		lastActivityListeners.remove(listener);
		updateIdleThread();
	}

	private void fireLastActivity(long idleSecond)
	{
		for (Map.Entry<LastActivityListener, Long> entry : lastActivityListeners.entrySet())
		{
			Long time = entry.getValue();
			if (time.longValue() == idleSecond)
			{
				entry.getKey().idle();
			}
		}
	}
	
	@Override
	public void processReceStanza(XMPPConnection connection, XMLStanza data)
	{
		IQ iq = (IQ) data;
		JID from = iq.getFrom();
		String stanzaID = iq.getStanzaID();
		
		IQ iqResponse = new IQ(IQ.Type.result);
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
			long idleSeconds = getIdleTime();
			fireLastActivity(idleSeconds);
			
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