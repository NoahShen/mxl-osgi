package net.sf.mxlosgi.mxlosgimucbundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * 
 * @author noah
 *
 */
public class MucAdminExtension implements PacketExtension
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1486303526328961748L;

	public static final String ELEMENTNAME = "query";

	public static final String NAMESPACE = "http://jabber.org/protocol/muc#admin";
	
	private List<Item> items = new ArrayList<Item>();

	/**
	 * Returns an Iterator for item childs that holds information about
	 * roles, affiliation, jids and nicks.
	 * 
	 * @return an Iterator for item childs that holds information about
	 *         roles, affiliation, jids and nicks.
	 */
	public Collection<Item> getItems()
	{
		synchronized (items)
		{
			return Collections.unmodifiableList(new ArrayList<Item>(items));
		}
	}

	/**
	 * Adds an item child that holds information about roles, affiliation,
	 * jids and nicks.
	 * 
	 * @param item
	 *                  the item child that holds information about roles,
	 *                  affiliation, jids and nicks.
	 */
	public void addItem(Item item)
	{
		synchronized (items)
		{
			items.add(item);
		}
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
		buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
		synchronized (items)
		{
			for (Item item : items)
			{
				buf.append(item.toXML());
			}
		}
		buf.append("</").append(getElementName()).append(">");
		return buf.toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		MucAdminExtension extension = (MucAdminExtension) super.clone();
		extension.items = new ArrayList<Item>();
		for (Item item : this.items)
		{
			extension.items.add((Item) item.clone());
		}
		return extension;
	}


	public static class Item implements XMLStanza
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7390328063229909553L;

		private String actor;

		private String reason;

		private String affiliation;

		private JID jid;

		private String nick;

		private String role;

		/**
		 * Creates a new item child.
		 * 
		 * @param affiliation
		 *                  the actor's affiliation to the room
		 */
		public Item(String affiliation)
		{
			this.affiliation = affiliation;
		}

		/**
		 * Returns the actor (JID of an occupant in the room) that
		 * was kicked or banned.
		 * 
		 * @return the JID of an occupant in the room that was
		 *         kicked or banned.
		 */
		public String getActor()
		{
			return actor;
		}

		/**
		 * Returns the reason for the item child. The reason is
		 * optional and could be used to explain the reason why a
		 * user (occupant) was kicked or banned.
		 * 
		 * @return the reason for the item child.
		 */
		public String getReason()
		{
			return reason;
		}

		/**
		 * Returns the occupant's affiliation to the room. The
		 * affiliation is a semi-permanent association or connection
		 * with a room. The possible affiliations are "owner",
		 * "admin", "member", and "outcast" (naturally it is also
		 * possible to have no affiliation). An affiliation lasts
		 * across a user's visits to a room.
		 * 
		 * @return the actor's affiliation to the room
		 */
		public String getAffiliation()
		{
			return affiliation;
		}

		/**
		 * Returns the <room@service/nick> by which an occupant is
		 * identified within the context of a room. If the room is
		 * non-anonymous, the JID will be included in the item.
		 * 
		 * @return the room JID by which an occupant is identified
		 *         within the room.
		 */
		public JID getJid()
		{
			return jid;
		}

		/**
		 * Returns the new nickname of an occupant that is changing
		 * his/her nickname. The new nickname is sent as part of the
		 * unavailable presence.
		 * 
		 * @return the new nickname of an occupant that is changing
		 *         his/her nickname.
		 */
		public String getNick()
		{
			return nick;
		}

		/**
		 * Returns the temporary position or privilege level of an
		 * occupant within a room. The possible roles are
		 * "moderator", "participant", and "visitor" (it is also
		 * possible to have no defined role). A role lasts only for
		 * the duration of an occupant's visit to a room.
		 * 
		 * @return the privilege level of an occupant within a room.
		 */
		public String getRole()
		{
			return role;
		}

		/**
		 * Sets the actor (JID of an occupant in the room) that was
		 * kicked or banned.
		 * 
		 * @param actor
		 *                  the actor (JID of an occupant in the
		 *                  room) that was kicked or banned.
		 */
		public void setActor(String actor)
		{
			this.actor = actor;
		}

		/**
		 * Sets the reason for the item child. The reason is
		 * optional and could be used to explain the reason why a
		 * user (occupant) was kicked or banned.
		 * 
		 * @param reason
		 *                  the reason why a user (occupant) was
		 *                  kicked or banned.
		 */
		public void setReason(String reason)
		{
			this.reason = reason;
		}

		/**
		 * Sets the <room@service/nick> by which an occupant is
		 * identified within the context of a room. If the room is
		 * non-anonymous, the JID will be included in the item.
		 * 
		 * @param jid
		 *                  the JID by which an occupant is
		 *                  identified within a room.
		 */
		public void setJid(JID jid)
		{
			this.jid = jid;
		}

		/**
		 * Sets the new nickname of an occupant that is changing
		 * his/her nickname. The new nickname is sent as part of the
		 * unavailable presence.
		 * 
		 * @param nick
		 *                  the new nickname of an occupant that is
		 *                  changing his/her nickname.
		 */
		public void setNick(String nick)
		{
			this.nick = nick;
		}

		public void setAffiliation(String affiliation)
		{
			this.affiliation = affiliation;
		}

		public void setRole(String role)
		{
			this.role = role;
		}

		public String toXML()
		{
			StringBuilder buf = new StringBuilder();
			buf.append("<item");
			if (getAffiliation() != null)
			{
				buf.append(" affiliation=\"").append(getAffiliation()).append("\"");
			}
			if (getJid() != null)
			{
				buf.append(" jid=\"").append(getJid()).append("\"");
			}
			if (getNick() != null)
			{
				buf.append(" nick=\"").append(getNick()).append("\"");
			}
			if (getRole() != null)
			{
				buf.append(" role=\"").append(getRole()).append("\"");
			}
			if (getReason() == null && getActor() == null)
			{
				buf.append("/>");
			}
			else
			{
				buf.append(">");
				if (getReason() != null)
				{
					buf.append("<reason>").append(getReason()).append("</reason>");
				}
				if (getActor() != null)
				{
					buf.append("<actor jid=\"").append(getActor()).append("\"/>");
				}
				buf.append("</item>");
			}
			return buf.toString();
		}

		@Override
		public Object clone() throws CloneNotSupportedException
		{
			Item item = (Item) super.clone();
			item.actor = this.actor;
			item.reason = this.reason;
			item.affiliation = this.affiliation;
			item.jid = this.jid;
			item.nick = this.nick;
			item.role = this.role;
			
			return item;
		}
		
		
	}

}
