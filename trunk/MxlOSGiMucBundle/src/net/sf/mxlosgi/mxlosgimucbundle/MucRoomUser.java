/**
 * 
 */
package net.sf.mxlosgi.mxlosgimucbundle;

import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.Presence;

/**
 * @author noah
 * 
 */
public class MucRoomUser
{
	private String nickname;

	private JID jid;
	
	private Affiliation affiliation;
	
	private Role role;
	
	private Presence presence;


	/**
	 * @param nickname
	 */
	public MucRoomUser(String nickname)
	{
		this.nickname = nickname;
	}

	/**
	 * @param nickname
	 * @param affiliation
	 * @param role
	 * @param presence
	 */
	public MucRoomUser(String nickname, Affiliation affiliation, Role role, Presence presence)
	{
		this.nickname = nickname;
		this.affiliation = affiliation;
		this.role = role;
		this.presence = presence;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public Affiliation getAffiliation()
	{
		return affiliation;
	}

	public void setAffiliation(Affiliation affiliation)
	{
		this.affiliation = affiliation;
	}

	public Role getRole()
	{
		return role;
	}

	public void setRole(Role role)
	{
		this.role = role;
	}

	public Presence getPresence()
	{
		return presence;
	}

	public void setPresence(Presence presence)
	{
		this.presence = presence;
	}

	public JID getJid()
	{
		return jid;
	}

	public void setJid(JID jid)
	{
		this.jid = jid;
	}
	
	public enum Affiliation
	{
		/**
		 * 
		 */
		Owner,

		/**
		 * 
		 */
		Admin,

		/**
		 * 
		 */
		Member,

		/**
		 * 
		 */
		Outcast,

		/**
		 * 
		 */
		None;
		
		public static Affiliation fromString(String str)
		{
			if ("Owner".equalsIgnoreCase(str))
			{
				return Owner;
			}
			else if ("Admin".equalsIgnoreCase(str))
			{
				return Admin;
			}
			else if ("Member".equalsIgnoreCase(str))
			{
				return Member;
			}
			else if ("Outcast".equalsIgnoreCase(str))
			{
				return Outcast;
			}
			else if ("None".equalsIgnoreCase(str))
			{
				return None;
			}
			
			return null;
		}

	}

	public enum Role
	{
		None,

		Visitor,

		Participant,

		Moderator;
		
		public static Role fromString(String str)
		{
			if ("None".equalsIgnoreCase(str))
			{
				return None;
			}
			else if ("Visitor".equalsIgnoreCase(str))
			{
				return Visitor;
			}
			else if ("Participant".equalsIgnoreCase(str))
			{
				return Participant;
			}
			else if ("Moderator".equalsIgnoreCase(str))
			{
				return Moderator;
			}
			
			return null;
		}
	}

	@Override
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("nickname : " + nickname + "\n")
			.append("affiliation : " + affiliation + "\n")
			.append("role : " + role + "\n")
			.append("presence : " + presence + "\n");
		
		return buf.toString();
	}

	
	
}
