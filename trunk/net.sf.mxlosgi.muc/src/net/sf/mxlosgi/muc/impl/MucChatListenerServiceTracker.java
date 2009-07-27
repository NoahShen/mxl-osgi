/**
 * 
 */
package net.sf.mxlosgi.muc.impl;

import java.util.Set;

import net.sf.mxlosgi.muc.MucChat;
import net.sf.mxlosgi.muc.MucRoomUser;
import net.sf.mxlosgi.muc.listener.MucChatListener;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Message;
import net.sf.mxlosgi.xmpp.Packet;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class MucChatListenerServiceTracker extends ServiceTracker
{

	public MucChatListenerServiceTracker(BundleContext context)
	{
		super(context, MucChatListener.class.getName(), null);
	}
	

	public void fireSubjectUpdated(MucChat mucChat, String subject, JID from)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			MucChatListener listener = (MucChatListener) obj;
			listener.subjectUpdated(mucChat, subject, from);
		}
	}
	

	public void fireUserNicknameChanged(MucChat mucChat, String oldNickName, String newNickname)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			MucChatListener listener = (MucChatListener) obj;
			listener.userNicknameChanged(mucChat, oldNickName, newNickname);
		}
	}

	public void fireUserUnavailable(MucChat mucChat, MucRoomUser user)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			MucChatListener listener = (MucChatListener) obj;
			listener.userUnavaliable(mucChat, user);
		}
	}
	

	public void fireOwnerStatusChanged(MucChat mucChat, MucRoomUser user, Set<String> statusCodes)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			MucChatListener listener = (MucChatListener) obj;
			listener.ownerStatusChanged(mucChat, user, statusCodes);
		}

	}

	public void fireUserStatusChanged(MucChat mucChat, MucRoomUser user)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			MucChatListener listener = (MucChatListener) obj;
			listener.userStatusChanged(mucChat, user);
		}
	}

	public void fireMessageReceived(MucChat mucChat, Message message)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			MucChatListener listener = (MucChatListener) obj;
			listener.processMessage(mucChat, message);
		}

	}
	


	public void fireError(MucChat mucChat, Packet packet)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return;
		}
		
		for (Object obj : services)
		{
			MucChatListener listener = (MucChatListener) obj;
			listener.error(mucChat, packet);
		}
		
	}


}
