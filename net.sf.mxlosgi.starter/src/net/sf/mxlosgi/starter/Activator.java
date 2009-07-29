package net.sf.mxlosgi.starter;

import java.util.Hashtable;

import net.sf.mxlosgi.chat.Chat;
import net.sf.mxlosgi.chat.XmppChatManager;
import net.sf.mxlosgi.chat.listener.ChatListener;
import net.sf.mxlosgi.chat.listener.ChatManagerListener;
import net.sf.mxlosgi.core.Future;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.core.XmppMainManager;
import net.sf.mxlosgi.disco.DiscoInfoManager;
import net.sf.mxlosgi.disco.DiscoInfoPacketExtension;
import net.sf.mxlosgi.disco.DiscoItemsManager;
import net.sf.mxlosgi.disco.DiscoItemsPacketExtension;
import net.sf.mxlosgi.lastactivity.LastActivityManager;
import net.sf.mxlosgi.lastactivity.LastActivityPacketExtension;
import net.sf.mxlosgi.lastactivity.listener.LastActivityListener;
import net.sf.mxlosgi.muc.MucManager;
import net.sf.mxlosgi.muc.RoomInfo;
import net.sf.mxlosgi.muc.listener.MucListener;
import net.sf.mxlosgi.privacy.PrivacyManager;
import net.sf.mxlosgi.privatedata.PrivateDataManager;
import net.sf.mxlosgi.registration.RegisterExtension;
import net.sf.mxlosgi.registration.RegistrationManager;
import net.sf.mxlosgi.search.SearchExtension;
import net.sf.mxlosgi.search.SearchManager;
import net.sf.mxlosgi.softwareversion.SoftwareVersionExtension;
import net.sf.mxlosgi.softwareversion.SoftwareVersionManager;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Message;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmpp.Presence;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	private ServiceTracker mainManagerServiceTracker;
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		mainManagerServiceTracker = new ServiceTracker(context, XmppMainManager.class.getName(), null);
		mainManagerServiceTracker.open();
		XmppMainManager mainManager = (XmppMainManager) mainManagerServiceTracker.getService();

//		String serviceName = "pidgin.im";
//		String serviceName = "tigase.org";
		String serviceName = "gmail.com";
//		String serviceName = "jabber.org";
//		String serviceName = "jabbercn.org";
//		String serviceName = "szsport.org";
		
		//context.registerService(ConnectionListener.class.getName(), this, null);
		XmppConnection connection = mainManager.createConnection(serviceName);
		// another login method
//		connection.setAttribute("username", "username");
//		connection.setAttribute("password", "password");
		
		Future future = connection.connect();
		future.complete();
		
//		connection.login("username", "password");
		
		String username = null;
		String password= null;
		if ("gmail.com".equals(serviceName))
		{
			username = "Noah.Shen87";
			password = "159357noah";
			
		}
		else if ("jabbercn.org".equals(serviceName))
		{
			username = "Noah";
			password = "159357";
		}
		else if ("jabber.org".equals(serviceName))
		{
			username = "NoahShen";
			password = "159357";
		}
		
		Future futureLogin = connection.login(username, password);
		futureLogin.complete();
		
		
//		testDisconnect(connection);
//		testDisco(connection, context);
//		testPrivacy(connection, context);
//		testChat(connection, context);
//		testLastActivity(connection, context);
//		testMuc(connection, context);
//		testPrivateData(connection, context);
//		testRegister(connection, context);
//		testSearch(connection, context);
		testSoftwareVersion(connection, context);
	}
	

	private void testSoftwareVersion(XmppConnection connection, BundleContext context) throws InterruptedException, XmppException
	{
		Thread.sleep(10 * 1000);
		
		ServiceTracker softwareVersionManagerServiceTracker = 
			new ServiceTracker(context, SoftwareVersionManager.class.getName(), null);
		softwareVersionManagerServiceTracker.open();
		SoftwareVersionManager softwareVersionManager =
			(SoftwareVersionManager) softwareVersionManagerServiceTracker.getService();
		SoftwareVersionExtension version = 
			softwareVersionManager.getSoftwareVersion(connection, new JID("Noah", "jabbercn.org", "Pidgin"));
		
		System.out.println(version.getName());
		
		softwareVersionManagerServiceTracker.close();
	}
	
	private void testSearch(XmppConnection connection, BundleContext context) throws InterruptedException, XmppException
	{
		Thread.sleep(8 * 1000);
		
		ServiceTracker searchManagerServiceTracker = new ServiceTracker(context, SearchManager.class.getName(), null);
		searchManagerServiceTracker.open();
		SearchManager searchManager = (SearchManager) searchManagerServiceTracker.getService();
		
		boolean b = searchManager.isSupportSearch(connection, new JID("users.szsport.org"));
		System.out.println(b);
		
		SearchExtension ex = searchManager.getSearchExtension(connection, new JID("users.szsport.org"));
		System.out.println(ex.toXML());
		
		SearchExtension extension = new SearchExtension();
		extension.getFields().put("nick", "how");
		SearchExtension extensionResult = searchManager.search(connection, extension, new JID("users.szsport.org"));
		for (SearchExtension.Item item : extensionResult.getItems())
		{
			
			System.out.println(item);
			System.out.println(item.getJid());
			System.out.println(item.getFields());
		}
		
		searchManagerServiceTracker.close();
	}
	private void testRegister(XmppConnection connection, BundleContext context) throws InterruptedException, XmppException
	{
		Thread.sleep(10 * 1000);
		
		ServiceTracker registrationManagerServiceTracker = new ServiceTracker(context, RegistrationManager.class.getName(), null);
		registrationManagerServiceTracker.open();
		RegistrationManager registrationManager = (RegistrationManager) registrationManagerServiceTracker.getService();
		
		System.out.println(registrationManager.isSupportRegistration(connection));
		
//		RegisterExtension registrerExtension = new RegisterExtension();
//		registrerExtension.getFields().put("username", "Noah1234");
//		registrerExtension.getFields().put("password", "1234");
//		registrationManager.registerAccount(connection, registrerExtension);
		RegisterExtension registerE = registrationManager.getRegisterExtension(connection);
		System.out.println(registerE.toXML());
//		registrationManager.changePassword(connection, "12345");
//		registrationManager.unregister(connection);
		
		registrationManagerServiceTracker.close();
	}

	
	private void testPrivateData(XmppConnection connection, BundleContext context) throws InterruptedException, XmppException
	{
		Thread.sleep(10 * 1000);
		
		ServiceTracker privateDataManagerServiceTracker = new ServiceTracker(context, PrivateDataManager.class.getName(), null);
		privateDataManagerServiceTracker.open();
		PrivateDataManager privateDataManager = (PrivateDataManager) privateDataManagerServiceTracker.getService();
		
		PacketExtension extension = privateDataManager.getPrivateData(connection, "storage", "storage:bookmarks");
		System.out.println(extension.toXML());
		
		privateDataManagerServiceTracker.close();
	}
	
	private void testMuc(XmppConnection connection, BundleContext context) throws InterruptedException, XmppException
	{
		Thread.sleep(10 * 1000);
		
		ServiceTracker mucManagerServiceTracker = new ServiceTracker(context, MucManager.class.getName(), null);
		mucManagerServiceTracker.open();
		MucManager mucManager = (MucManager) mucManagerServiceTracker.getService();
		context.registerService(MucListener.class.getName(), new MucListener(){

			@Override
			public void declineReceived(XmppConnection connection, Message message)
			{
			}

			@Override
			public void invitationReceived(XmppConnection connection, Message message)
			{
				System.out.println("=====" + message.toXML());
			}
		
		}, null);
		
//		boolean b = mucManager.isUserSupportMuc(connection, new JID("Noah", "jabbercn.org", "Pidgin"));
//		System.out.println("==============" + b);
//		
//		b = mucManager.isServerSupportMuc(connection, new JID("conference.jabber.org"));
//		System.out.println("==============" + b);
//		
//		RoomInfo roomInfo = mucManager.getRoomInfo(connection, new JID("szsport@conference.rooyee.biz"));
//		System.out.println(roomInfo);
//		
//		mucManager.getRoomList(connection, new JID("conference.rooyee.biz"));
		

//		JID mucServer = new JID("conference.12jabber.net");
//		JID mucRoom = new JID("support@conference.12jabber.com");
		
//		JID mucServer = new JID("muc.jabber.freenet.de");
//		JID mucRoom = new JID("arcadia@muc.jabber.freenet.de");
		
//		JID mucServer = new JID("conference.jabber.dk");
//		JID mucRoom = new JID("desert@conference.jabber.dk");
		
		JID mucServer = new JID("conference.ubuntu-jabber.de");
		JID mucRoom = new JID("ubuntu@conference.ubuntu-jabber.de");
		
//		JID mucServer = new JID("conference.rooyee.biz");
//		JID mucRoom = new JID("rooyee@conference.rooyee.biz");	
		
		System.out.println(mucManager.isServerSupportMuc(connection, mucServer));
		DiscoItemsPacketExtension.Item items[] = mucManager.getRoomList(connection, mucServer);
		for (DiscoItemsPacketExtension.Item item : items)
		{
			System.out.println(item.toXML());
		}
		
		RoomInfo roomInfo = mucManager.getRoomInfo(connection, mucRoom);
		System.out.println(roomInfo.isMembersOnly());
		
		DiscoItemsPacketExtension.Item itemsUsers[] = mucManager.getUsers(connection, mucRoom);
		for (DiscoItemsPacketExtension.Item item : itemsUsers)
		{
			System.out.println("=======users : " +item.toXML());
		}
//
//		MucChat mucChat = mucManager.createMucChat(connection, mucRoom);
//		System.out.println(mucChat.getRoomJID());
//		
//		mucChat.addMucChatListener(new MucChatListener(){
//
//			@Override
//			public void error(Packet packet)
//			{
//				
//			}
//
//			@Override
//			public void ownerStatusChanged(MucRoomUser user, Set<String> statusCodes)
//			{
//				
//			}
//
//			@Override
//			public void processMessage(Message message)
//			{
//				System.out.println("message:" + message.getBody());
//			}
//
//			@Override
//			public void subjectUpdated(String subject, JID from)
//			{
//				System.out.println("subjectUpdated:" + subject);
//			}
//
//			@Override
//			public void userNicknameChanged(String oldNickName, String newNickName)
//			{
//				System.out.println("oldNickeName:" + oldNickName + " newNickeName:" + newNickName);
//			}
//
//			@Override
//			public void userStatusChanged(MucRoomUser user)
//			{
//				System.out.println("userStatusChanged:" + user);
//			}
//
//			@Override
//			public void userUnavaliable(MucRoomUser user)
//			{
//				System.out.println("userUnavaliable:" + user);
//			}
//			
//		});
//		MucInitialPresenceExtension.History history = new MucInitialPresenceExtension.History();
//		history.setSeconds(180);
//		mucChat.enterRoom(history);
//		Thread.sleep(5 * 1000);
////		mucChat.close();
//		Presence presence = new Presence(Presence.Type.available);
//		presence.setStatus("status");
//		presence.setShow(Presence.Show.away);
//		mucChat.changeStatus(presence);
//		mucChat.inviteUser(new JID("Noah@jabbercn.org"), "reason");
//		mucChat.sendMessage("text");
//		mucChat.changeSubject("subject");
//		mucChat.enterRoom();
//		Thread.sleep(3 * 1000);
//		mucChat.sendMessage("Hello!");
		
		mucManagerServiceTracker.close();
	}
	
	private void testLastActivity(XmppConnection connection, BundleContext context) throws InterruptedException, XmppException
	{
		Thread.sleep(10 * 1000);
		
		ServiceTracker lastActivityManagerServiceTracker = new ServiceTracker(context, LastActivityManager.class.getName(), null);
		lastActivityManagerServiceTracker.open();
		LastActivityManager lastActivityManager = (LastActivityManager) lastActivityManagerServiceTracker.getService();
		LastActivityPacketExtension lastA = lastActivityManager.getLastActivity(connection, new JID("Noah.Shen87", "gmail.com", "Pidgin9F6228D2"));
		System.out.println(lastA.toXML());
		
		Hashtable<String, Long> properties = new Hashtable<String, Long>();
		properties.put("idleSecond", 10L);
		context.registerService(LastActivityListener.class.getName(), new LastActivityListener(){

			@Override
			public void idle()
			{
				System.out.println("idle=====================");
			}
			
		}, properties);
		
		lastActivityManagerServiceTracker.close();
	}
	
	private void testChat(XmppConnection connection, BundleContext context) throws InterruptedException
	{
		Thread.sleep(10 * 1000);
		
		ServiceTracker chatManagerServiceTracker = new ServiceTracker(context, XmppChatManager.class.getName(), null);
		chatManagerServiceTracker.open();
		XmppChatManager chatManager = (XmppChatManager) chatManagerServiceTracker.getService();
		context.registerService(ChatManagerListener.class.getName(), new ChatManagerListener(){

			@Override
			public void chatClosed(XmppChatManager chatManager, Chat chat)
			{
				System.out.println("chatClosed " + chat);
			}

			@Override
			public void chatCreated(XmppChatManager chatManager, Chat chat)
			{
				System.out.println("chatCreated " + chat);
			}
			
		}, null);
		
		context.registerService(ChatListener.class.getName(), new ChatListener(){

			@Override
			public void processMessage(Chat chat, Message message)
			{
				System.out.println("processMessage : " + message.getBody());
				chat.sendMessage("text");
			}

			@Override
			public void resourceChanged(Chat chat, String currentChatResource)
			{
				System.out.println("resourceChanged : " + currentChatResource);
			}

			
			
		}, null);
		
		Chat chat = chatManager.createChat(connection, new JID("Noah.Shen87", "gmail.com", null));
		chat.sendMessage("Hello!");
		
		chatManagerServiceTracker.close();
	}

	private void testPrivacy(XmppConnection connection, BundleContext context) throws InterruptedException, XmppException
	{
		Thread.sleep(10 * 1000);
		
		ServiceTracker privacyManagerServiceTracker = new ServiceTracker(context, PrivacyManager.class.getName(), null);
		privacyManagerServiceTracker.open();
		PrivacyManager privacyManager = (PrivacyManager) privacyManagerServiceTracker.getService();
		privacyManager.getPrivacyLists(connection);
		privacyManager.declineActiveList(connection);
		
		privacyManagerServiceTracker.close();
	}

	private void testDisconnect(XmppConnection connection) throws Exception
	{
		Thread.sleep(5 * 1000);
		connection.close(new Presence(Presence.Type.unavailable));
	}
	

	private void testDisco(XmppConnection connection, BundleContext context) throws InterruptedException, XmppException
	{
		Thread.sleep(10 * 1000);
		
		ServiceTracker discoInfoManagerServiceTracker = new ServiceTracker(context, DiscoInfoManager.class.getName(), null);
		discoInfoManagerServiceTracker.open();
		DiscoInfoManager discoInfoManager = (DiscoInfoManager) discoInfoManagerServiceTracker.getService();
		DiscoInfoPacketExtension discoInfo = discoInfoManager.getDiscoInfo(connection, new JID("Noah.Shen87", "gmail.com", "Pidgin6CB40157"));
		System.out.println("==========" + discoInfo.toXML());
		
		discoInfo = discoInfoManager.getDiscoInfo(connection, new JID(null, "gmail.com", null));
		System.out.println("==========" + discoInfo.toXML());
		
		discoInfoManagerServiceTracker.close();
		
		ServiceTracker discoItemsManagerServiceTracker = new ServiceTracker(context, DiscoItemsManager.class.getName(), null);
		discoItemsManagerServiceTracker.open();
		
		DiscoItemsManager discoItemsManager = (DiscoItemsManager) discoItemsManagerServiceTracker.getService();
		DiscoItemsPacketExtension discoItems = discoItemsManager.getDiscoItems(connection, new JID(null, "jabber.org", null));
		
		
		
		System.out.println("==========" + discoItems.toXML());
		
		discoItemsManagerServiceTracker.close();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}

}
