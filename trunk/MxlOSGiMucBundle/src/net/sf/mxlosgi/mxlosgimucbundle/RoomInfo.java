package net.sf.mxlosgi.mxlosgimucbundle;

import net.sf.mxlosgi.mxlosgiutilsbundle.AbstractHasAttribute;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

/**
 * 
 * @author noah
 *
 */
public class RoomInfo extends AbstractHasAttribute
{

	/**
	 * JID of the room. The node of the JID is commonly used as the ID of
	 * the room or name.
	 */
	private JID roomJID;

	/**
	 * A room is considered members-only if an invitation is required in
	 * order to enter the room. Any user that is not a member of the room
	 * won't be able to join the room unless the user decides to register
	 * with the room (thus becoming a member).
	 */
	private boolean membersOnly;

	/**
	 * Moderated rooms enable only participants to speak. Users that join
	 * the room and aren't participants can't speak (they are just
	 * visitors).
	 */
	private boolean moderated;

	/**
	 * Every presence packet can include the JID of every occupant unless
	 * the owner deactives this configuration.
	 */
	private AnonymousType anonymousType = AnonymousType.non_Anonymous;

	/**
	 * Indicates if users must supply a password to join the room.
	 */
	private boolean passwordProtected;

	/**
	 * Persistent rooms are saved to the database to make sure that rooms
	 * configurations can be restored in case the server goes down.
	 */
	private boolean persistent;
	
	/**
	 * @param roomJID
	 */
	public RoomInfo(JID roomJID)
	{
		this.roomJID = roomJID;
	}

	/**
	 * Returns the JID of the room whose information was discovered.
	 * 
	 * @return the JID of the room whose information was discovered.
	 */
	public JID getRoomJID()
	{
		return roomJID;
	}

	/**
	 * Returns true if the room has restricted the access so that only
	 * members may enter the room.
	 * 
	 * @return true if the room has restricted the access so that only
	 *         members may enter the room.
	 */
	public boolean isMembersOnly()
	{
		return membersOnly;
	}

	/**
	 * Returns true if the room enabled only participants to speak.
	 * Occupants with a role of visitor won't be able to speak in the
	 * room.
	 * 
	 * @return true if the room enabled only participants to speak.
	 */
	public boolean isModerated()
	{
		return moderated;
	}

	/**
	 * Returns true if users musy provide a valid password in order to
	 * join the room.
	 * 
	 * @return true if users musy provide a valid password in order to
	 *         join the room.
	 */
	public boolean isPasswordProtected()
	{
		return passwordProtected;
	}

	/**
	 * Returns true if the room will persist after the last occupant have
	 * left the room.
	 * 
	 * @return true if the room will persist after the last occupant have
	 *         left the room.
	 */
	public boolean isPersistent()
	{
		return persistent;
	}

	public void setMembersOnly(boolean membersOnly)
	{
		this.membersOnly = membersOnly;
	}

	public void setModerated(boolean moderated)
	{
		this.moderated = moderated;
	}


	public AnonymousType getAnonymousType()
	{
		return anonymousType;
	}

	public void setAnonymousType(AnonymousType anonymousType)
	{
		this.anonymousType = anonymousType;
	}

	public void setPasswordProtected(boolean passwordProtected)
	{
		this.passwordProtected = passwordProtected;
	}

	public void setPersistent(boolean persistent)
	{
		this.persistent = persistent;
	}

	@Override
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("room jid : " + roomJID + "\n")
			.append("membersOnly : " + membersOnly + "\n")
			.append("moderated : " + moderated + "\n")
			.append("anonymousType : " + anonymousType + "\n")
			.append("passwordProtected : " + passwordProtected + "\n")
			.append("persistent : " + persistent + "\n");
		
		return buf.toString();
	}
	
	/**
	 * 
	 * @author noah
	 *
	 */
	public enum AnonymousType
	{
		semi_Anonymous, 
		
		fully_Anonymous, 
		
		non_Anonymous
	}
	
}
