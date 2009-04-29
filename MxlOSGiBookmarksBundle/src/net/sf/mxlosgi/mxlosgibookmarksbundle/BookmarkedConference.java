package net.sf.mxlosgi.mxlosgibookmarksbundle;

import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

public class BookmarkedConference implements XMLStanza
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2753370708810554548L;

	private String name;

	private boolean autoJoin;

	private JID jid;

	private String nickname;

	private String password;

	public BookmarkedConference(JID jid)
	{
		this.jid = jid;
	}

	public BookmarkedConference(String name, JID jid, boolean autoJoin, String nickname, String password)
	{
		this.name = name;
		this.jid = jid;
		this.autoJoin = autoJoin;
		this.nickname = nickname;
		this.password = password;
	}

	/**
	 * Returns the display label representing the Conference room.
	 * 
	 * @return the name of the conference room.
	 */
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Returns true if this conference room should be auto-joined on
	 * startup.
	 * 
	 * @return true if room should be joined on startup, otherwise false.
	 */
	public boolean isAutoJoin()
	{
		return autoJoin;
	}

	public void setAutoJoin(boolean autoJoin)
	{
		this.autoJoin = autoJoin;
	}

	/**
	 * Returns the full JID of this conference room.
	 * (ex.dev@conference.jivesoftware.com)
	 * 
	 * @return the full JID of this conference room.
	 */
	public JID getJid()
	{
		return jid;
	}

	/**
	 * Returns the nickname to use when joining this conference room. This
	 * is an optional value and may return null.
	 * 
	 * @return the nickname to use when joining, null may be returned.
	 */
	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	/**
	 * Returns the password to use when joining this conference room. This
	 * is an optional value and may return null.
	 * 
	 * @return the password to use when joining this conference room, null
	 *         may be returned.
	 */
	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza#toXML()
	 */
	@Override
	public String toXML()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("<conference");
		if (getName() != null && !getName().isEmpty())
		{
			buf.append(" name=\"").append(getName()).append("\"");
		}
		
		buf.append(" autojoin=\"").append(isAutoJoin()).append("\"");
		if (getJid() != null)
		{
			buf.append(" jid=\"").append(getJid().toFullJID()).append("\"");
		}
		buf.append(">");
		if (getNickname() != null && !getNickname().isEmpty())
		{
			buf.append("<nick>").append(getNickname()).append("</nick>");
		}
		if (getPassword() != null && !getPassword().isEmpty())
		{
			buf.append("<password>").append(getPassword()).append("</password>");
		}
		
		buf.append("</conference>");
		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		BookmarkedConference conference = (BookmarkedConference) super.clone();
		conference.name = this.name;
		conference.autoJoin = this.autoJoin;
		conference.jid = this.jid;
		conference.nickname = this.nickname;
		conference.password = this.password;
		return conference;
	}
}
