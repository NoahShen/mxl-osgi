package net.sf.mxlosgi.mxlosgimucbundle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * 
 * @author noah
 * 
 */
public class MucInitialPresenceExtension implements PacketExtension
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6047151757887499163L;

	public static final String ELEMENTNAME = "x";

	public static final String NAMESPACE = "http://jabber.org/protocol/muc";
	
	private String password;

	private History history;

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

	public String toXML()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
		if (getPassword() != null)
		{
			buf.append("<password>").append(getPassword()).append("</password>");
		}
		if (getHistory() != null)
		{
			buf.append(getHistory().toXML());
		}
		buf.append("</").append(getElementName()).append(">");
		return buf.toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		MucInitialPresenceExtension extension = (MucInitialPresenceExtension) super.clone();
		extension.password = this.password;
		if (this.history != null)
		{
			extension.history = (History) this.history.clone();
		}
		return extension;
	}

	/**
	 * Returns the history that manages the amount of discussion history
	 * provided on entering a room.
	 * 
	 * @return the history that manages the amount of discussion history
	 *         provided on entering a room.
	 */
	public History getHistory()
	{
		return history;
	}

	/**
	 * Returns the password to use when the room requires a password.
	 * 
	 * @return the password to use when the room requires a password.
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * Sets the History that manages the amount of discussion history
	 * provided on entering a room.
	 * 
	 * @param history
	 *                  that manages the amount of discussion history
	 *                  provided on entering a room.
	 */
	public void setHistory(History history)
	{
		this.history = history;
	}

	/**
	 * Sets the password to use when the room requires a password.
	 * 
	 * @param password
	 *                  the password to use when the room requires a
	 *                  password.
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * The History class controls the number of characters or messages to
	 * receive when entering a room.
	 * 
	 * @author noah
	 */
	public static class History implements XMLStanza
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 2444186875131832644L;

		private int maxChars = -1;

		private int maxStanzas = -1;

		private int seconds = -1;

		private Date since;

		/**
		 * Returns the total number of characters to receive in the
		 * history.
		 * 
		 * @return total number of characters to receive in the
		 *         history.
		 */
		public int getMaxChars()
		{
			return maxChars;
		}

		/**
		 * Returns the total number of messages to receive in the
		 * history.
		 * 
		 * @return the total number of messages to receive in the
		 *         history.
		 */
		public int getMaxStanzas()
		{
			return maxStanzas;
		}

		/**
		 * Returns the number of seconds to use to filter the
		 * messages received during that time. In other words, only
		 * the messages received in the last "X" seconds will be
		 * included in the history.
		 * 
		 * @return the number of seconds to use to filter the
		 *         messages received during that time.
		 */
		public int getSeconds()
		{
			return seconds;
		}

		/**
		 * Returns the since date to use to filter the messages
		 * received during that time. In other words, only the
		 * messages received since the datetime specified will be
		 * included in the history.
		 * 
		 * @return the since date to use to filter the messages
		 *         received during that time.
		 */
		public Date getSince()
		{
			return since;
		}

		/**
		 * Sets the total number of characters to receive in the
		 * history.
		 * 
		 * @param maxChars
		 *                  the total number of characters to
		 *                  receive in the history.
		 */
		public void setMaxChars(int maxChars)
		{
			this.maxChars = maxChars;
		}

		/**
		 * Sets the total number of messages to receive in the
		 * history.
		 * 
		 * @param maxStanzas
		 *                  the total number of messages to receive
		 *                  in the history.
		 */
		public void setMaxStanzas(int maxStanzas)
		{
			this.maxStanzas = maxStanzas;
		}

		/**
		 * Sets the number of seconds to use to filter the messages
		 * received during that time. In other words, only the
		 * messages received in the last "X" seconds will be
		 * included in the history.
		 * 
		 * @param seconds
		 *                  the number of seconds to use to filter
		 *                  the messages received during that time.
		 */
		public void setSeconds(int seconds)
		{
			this.seconds = seconds;
		}

		/**
		 * Sets the since date to use to filter the messages
		 * received during that time. In other words, only the
		 * messages received since the datetime specified will be
		 * included in the history.
		 * 
		 * @param since
		 *                  the since date to use to filter the
		 *                  messages received during that time.
		 */
		public void setSince(Date since)
		{
			this.since = since;
		}

		public String toXML()
		{
			StringBuilder buf = new StringBuilder();
			buf.append("<history");
			if (getMaxChars() != -1)
			{
				buf.append(" maxchars=\"").append(getMaxChars()).append("\"");
			}
			if (getMaxStanzas() != -1)
			{
				buf.append(" maxstanzas=\"").append(getMaxStanzas()).append("\"");
			}
			if (getSeconds() != -1)
			{
				buf.append(" seconds=\"").append(getSeconds()).append("\"");
			}
			if (getSince() != null)
			{
				SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				buf.append(" since=\"").append(utcFormat.format(getSince())).append("\"");
			}
			buf.append("/>");
			return buf.toString();
		}

		@Override
		public Object clone() throws CloneNotSupportedException
		{
			History history = (History) super.clone();
			history.maxChars = this.maxChars;
			history.maxStanzas = this.maxStanzas;
			history.seconds = this.seconds;
			history.since = this.since;
			return history;
		}
		
		
	}
}
