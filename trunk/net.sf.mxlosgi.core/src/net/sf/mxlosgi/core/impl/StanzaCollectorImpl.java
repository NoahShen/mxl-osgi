package net.sf.mxlosgi.core.impl;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.sf.mxlosgi.core.StanzaCollector;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.xmpp.XMLStanza;




/**
 * @author noah
 * 
 */
public class StanzaCollectorImpl implements StanzaCollector
{

	private static final int MAX = 65536;

	private XmppConnectionImpl connection;

	private Queue<XMLStanza> resultQueue;

	private StanzaFilter stanzaFilter;

	private Object lock = new Object();
	
	StanzaCollectorImpl(XmppConnectionImpl connection, StanzaFilter stanzaFilter)
	{
		this.connection = connection;
		this.stanzaFilter = stanzaFilter;
		this.resultQueue = new ConcurrentLinkedQueue<XMLStanza>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlconnectionbundle.PacketCollector#cancel()
	 */
	@Override
	public void cancel()
	{
		connection.cancelStanzaCollector(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlconnectionbundle.PacketCollector#getPacketFilter()
	 */
	@Override
	public StanzaFilter getStanzaFilter()
	{
		return stanzaFilter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlconnectionbundle.PacketCollector#nextResult()
	 */
	@Override
	public XMLStanza nextResult()
	{
		// Wait indefinitely until there is a result to return.

			while (resultQueue.isEmpty())
			{
				try
				{
					synchronized(lock)
					{
						lock.wait();
					}
				}
				catch (InterruptedException ie)
				{
					// Ignore.
				}
		}
		return resultQueue.poll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlconnectionbundle.PacketCollector#nextResult(long)
	 */
	@Override
	public XMLStanza nextResult(long timeout)
	{
		// Wait up to the specified amount of time for a result.
		if (resultQueue.isEmpty())
		{
			long waitTime = timeout;
			long start = System.currentTimeMillis();
			try
			{
				// Keep waiting until the specified
				// amount of time has elapsed, or
				// a packet is available to return.
				while (resultQueue.isEmpty())
				{
					if (waitTime <= 0)
					{
						break;
					}
					synchronized(lock)
					{
						lock.wait(waitTime);
					}
					long now = System.currentTimeMillis();
					waitTime -= (now - start);
					start = now;
				}
			}
			catch (InterruptedException ie)
			{
				// Ignore.
			}
			// Still haven't found a result, so return null.
			if (resultQueue.isEmpty())
			{
				return null;
			}
			// Return the packet that was found.
			else
			{
				return resultQueue.poll();
			}
		}
		// There's already a packet waiting, so return it.
		else
		{
			return resultQueue.poll();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlconnectionbundle.PacketCollector#pollResult()
	 */
	@Override
	public XMLStanza pollResult()
	{
		if (resultQueue.isEmpty())
		{
			return null;
		}
		else
		{
			return resultQueue.poll();
		}
	}

	@Override
	public void processPacket(XmppConnection connection, XMLStanza data)
	{
		if (data == null)
		{
			return;
		}
		if (stanzaFilter == null || stanzaFilter.accept(connection, data))
		{
			// If the max number of packets has been reached,
			// remove the oldest one.
			if (resultQueue.size() == MAX)
			{
				resultQueue.poll();
			}
			// Add the new packet.
			resultQueue.add(data);
			// Notify waiting threads a result is available.
			synchronized(lock)
			{
				lock.notifyAll();
			}
		}
	}

}