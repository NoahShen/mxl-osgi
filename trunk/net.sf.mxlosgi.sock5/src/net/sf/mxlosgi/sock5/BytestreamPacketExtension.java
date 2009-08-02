package net.sf.mxlosgi.sock5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sf.mxlosgi.utils.StringUtils;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmpp.XmlStanza;

/**
 * 
 * @author noah
 *
 */
public class BytestreamPacketExtension implements PacketExtension
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7347708643067231856L;

	public static final String ELEMENTNAME = "query";

	public static final String NAMESPACE = "http://jabber.org/protocol/bytestreams";
	
	public static final String ID_NOT_AVAILABLE = "ID_NOT_AVAILABLE";
	
	/**
	 * A prefix helps to make sure that ID's are unique across mutliple
	 * instances.
	 */
	private static String prefix = StringUtils.randomString(21) + "-";
	
	/**
	 * Keeps track of the current increment, which is appended to the
	 * prefix to forum a unique ID.
	 */
	private static long staticID = 0;
	
	/**
	 * Returns the next unique id. Each id made up of a short alphanumeric
	 * prefix along with a unique numeric value.
	 * 
	 * @return the next id.
	 */
	public static synchronized String nextID()
	{
		return prefix + Long.toString(staticID++);
	}

	private String streamID;

	private Mode mode;

	private List<StreamHost> streamHosts = new ArrayList<StreamHost>();

	private StreamHostUsed usedHost;

	private Activate activate;

	/**
	 * The default constructor
	 */
	public BytestreamPacketExtension()
	{
	}

	/**
	 * A constructor where the session ID can be specified.
	 * 
	 * @param sid
	 *                  The session ID related to the negotiation.
	 * @see #setStreamID(String)
	 */
	public BytestreamPacketExtension(String sid)
	{
		this();
		setStreamID(sid);
	}

	/**
	 * 
	 * @param streamID
	 */
	public void setStreamID(final String streamID)
	{
		this.streamID = streamID;
	}

	/**
	 * 
	 * @return
	 */
	public String getStreamID()
	{
		if (ID_NOT_AVAILABLE.equals(streamID))
		{
			return null;
		}
		
		if (streamID == null || streamID.isEmpty())
		{
			streamID = nextID();
		}
		return streamID;
	}

	/**
	 * Set the transport mode. This should be put in the initiation of the
	 * interaction.
	 * 
	 * @param mode
	 *                  the transport mode, either UDP or TCP
	 * @see Mode
	 */
	public void setMode(Mode mode)
	{
		this.mode = mode;
	}

	/**
	 * Returns the transport mode.
	 * 
	 * @return Returns the transport mode.
	 * @see #setMode(Mode)
	 */
	public Mode getMode()
	{
		return mode;
	}

	public void addStreamHost(JID jid, String address)
	{
		addStreamHost(jid, address, 0);
	}

	public void addStreamHost(JID jid, String address, int port)
	{
		StreamHost host = new StreamHost(jid, address);
		host.setPort(port);
		addStreamHost(host);
	}

	/**
	 * Adds a potential stream host that the remote user can transfer the
	 * file through.
	 * 
	 * @param host
	 *                  The potential stream host.
	 */
	public void addStreamHost(StreamHost host)
	{
		streamHosts.add(host);
	}

	/**
	 * Returns the list of stream hosts contained in the packet.
	 * 
	 * @return Returns the list of stream hosts contained in the packet.
	 */
	public Collection<StreamHost> getStreamHosts()
	{
		return Collections.unmodifiableCollection(streamHosts);
	}

	/**
	 * Returns the stream host related to the given jabber ID, or null if
	 * there is none.
	 * 
	 * @param jid
	 *                  The jabber ID of the desired stream host.
	 * @return Returns the stream host related to the given jabber ID, or
	 *         null if there is none.
	 */
	public StreamHost getStreamHost(JID jid)
	{
		if (jid == null)
		{
			return null;
		}
		for (StreamHost host : streamHosts)
		{
			if (host.getJid().equals(jid))
			{
				return host;
			}
		}

		return null;
	}

	public void setUsedHost(JID jid)
	{
		this.usedHost = new StreamHostUsed(jid);
	}

	public void setUsedHost(StreamHostUsed streamHostUsed)
	{
		this.usedHost = streamHostUsed;
	}

	/**
	 * Returns the Socks5 host connected to by the remote user.
	 * 
	 * @return Returns the Socks5 host connected to by the remote user.
	 */
	public StreamHostUsed getUsedHost()
	{
		return usedHost;
	}

	/**
	 * Returns the activate element of the packet sent to the proxy host
	 * to verify the identity of the initiator and match them to the
	 * appropriate stream.
	 * 
	 * @return Returns the activate element of the packet sent to the
	 *         proxy host to verify the identity of the initiator and
	 *         match them to the appropriate stream.
	 */
	public Activate getActivate()
	{
		return activate;
	}

	public void setActivate(final String targetID)
	{
		this.activate = new Activate(targetID);
	}

	public void setActivate(Activate activate)
	{
		this.activate = activate;
	}

	@Override
	public String getElementName()
	{
		return ELEMENTNAME;
	}

	@Override
	public String getNamespace()
	{
		return NAMESPACE;
	}

	@Override
	public String toXML()
	{
		StringBuilder buf = new StringBuilder();

		buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\"");

		if (getStreamID() != null)
		{
			buf.append(" sid=\"").append(getStreamID()).append("\"");
		}
		if (getMode() != null)
		{
			buf.append(" mode = \"").append(getMode()).append("\"");
		}
		
		buf.append(">");
		
		if (getActivate() != null)
		{
			buf.append(getActivate().toXML());
		}
		else if (getUsedHost() != null)
		{
			buf.append(getUsedHost().toXML());
		}
		else if (streamHosts.size() > 0)
		{
			for (StreamHost host : streamHosts)
			{
				buf.append(host.toXML());
			}
		}
		buf.append("</").append(getElementName()).append(">");

		return buf.toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		BytestreamPacketExtension extension = (BytestreamPacketExtension) super.clone();
		extension.streamID = this.streamID;
		extension.mode = this.mode;
		extension.streamHosts = new ArrayList<StreamHost>();
		for (StreamHost host : this.streamHosts)
		{
			extension.streamHosts.add((StreamHost) host.clone());
		}
		
		if (this.usedHost != null)
		{
			extension.usedHost = (StreamHostUsed) this.usedHost.clone();
		}
		
		if (this.activate != null)
		{
			extension.activate = (Activate) this.activate.clone();
		}
		
		return extension;
	}

	public static class StreamHost implements XmlStanza
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -374280830538812520L;

		private JID jid;

		private String host;

		private int port = 0;

		/**
		 * Default constructor.
		 * 
		 * @param JID
		 *                  The jabber ID of the stream host.
		 * @param host
		 *                  The internet address of the stream host.
		 */
		public StreamHost(JID jid, String host)
		{
			this.jid = jid;
			this.host = host;
		}

		/**
		 * Returns the jabber ID of the stream host.
		 * 
		 * @return Returns the jabber ID of the stream host.
		 */
		public JID getJid()
		{
			return jid;
		}

		/**
		 * Returns the internet address of the stream host.
		 * 
		 * @return Returns the internet address of the stream host.
		 */
		public String getHost()
		{
			return host;
		}

		/**
		 * Sets the port of the stream host.
		 * 
		 * @param port
		 *                  The port on which the potential stream
		 *                  host would accept the connection.
		 */
		public void setPort(int port)
		{
			this.port = port;
		}

		/**
		 * Returns the port on which the potential stream host would
		 * accept the connection.
		 * 
		 * @return Returns the port on which the potential stream
		 *         host would accept the connection.
		 */
		public int getPort()
		{
			return port;
		}

		public String toXML()
		{
			StringBuilder buf = new StringBuilder();

			buf.append("<streamhost");
			buf.append(" jid=\"").append(getJid().toFullJID()).append("\"");
			buf.append(" host=\"").append(getHost()).append("\"");
			if (getPort() != 0)
				buf.append(" port=\"").append(getPort()).append("\"");
			else
				buf.append(" zeroconf=\"_jabber.bytestreams\"");
			buf.append("/>");

			return buf.toString();
		}

		@Override
		public Object clone() throws CloneNotSupportedException
		{
			StreamHost streamHost = (StreamHost) super.clone();
			streamHost.jid = this.jid;
			streamHost.host = this.host;
			streamHost.port = this.port;
			return streamHost;
		}
		
		
	}

	public static class StreamHostUsed implements PacketExtension
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -8981565105557960846L;

		private JID jid;

		/**
		 * Default constructor.
		 * 
		 * @param JID
		 *                  The jabber ID of the selected stream
		 *                  host.
		 */
		public StreamHostUsed(JID jid)
		{
			this.jid = jid;
		}

		/**
		 * Returns the jabber ID of the selected stream host.
		 * 
		 * @return Returns the jabber ID of the selected stream
		 *         host.
		 */
		public JID getJid()
		{
			return jid;
		}

		public String getNamespace()
		{
			return null;
		}

		public String getElementName()
		{
			return "streamhost-used";
		}

		public String toXML()
		{
			StringBuilder buf = new StringBuilder();
			buf.append("<").append(getElementName()).append(" ");
			buf.append("jid=\"").append(getJid().toFullJID()).append("\" ");
			buf.append("/>");
			return buf.toString();
		}

		@Override
		public Object clone() throws CloneNotSupportedException
		{
			StreamHostUsed usedHost = (StreamHostUsed) super.clone();
			usedHost.jid = this.jid;
			return usedHost;
		}
		
		
	}

	public static class Activate implements PacketExtension
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 6113084539409509026L;

		public static final String ELEMENTNAME = "activate";
		
		private String target;

		/**
		 * Default constructor specifying the target of the stream.
		 * 
		 * @param target
		 *                  The target of the stream.
		 */
		public Activate(final String target)
		{
			this.target = target;
		}

		/**
		 * Returns the target of the activation.
		 * 
		 * @return Returns the target of the activation.
		 */
		public String getTarget()
		{
			return target;
		}

		public String getNamespace()
		{
			return null;
		}

		public String getElementName()
		{
			return ELEMENTNAME;
		}

		public String toXML()
		{
			StringBuilder buf = new StringBuilder();
			buf.append("<").append(getElementName()).append(">");
			buf.append(getTarget());
			buf.append("</").append(getElementName()).append(">");
			return buf.toString();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#clone()
		 */
		@Override
		public Object clone() throws CloneNotSupportedException
		{
			Activate activate = (Activate) super.clone();
			activate.target = this.target;
			return activate;
		}
		
		
	}

	public enum Mode
	{

		/**
		 * A TCP based stream.
		 */
		tcp,

		/**
		 * A UDP based stream.
		 */
		udp;

		public static Mode fromName(String name)
		{
			Mode mode;
			try
			{
				mode = Mode.valueOf(name);
			}
			catch (Exception ex)
			{
				mode = tcp;
			}

			return mode;
		}
	}

}