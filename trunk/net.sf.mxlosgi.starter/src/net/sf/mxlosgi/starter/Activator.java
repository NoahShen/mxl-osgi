package net.sf.mxlosgi.starter;

import java.util.Hashtable;

import net.sf.mxlosgi.chat.Chat;
import net.sf.mxlosgi.chat.XmppChatManager;
import net.sf.mxlosgi.chat.listener.ChatListener;
import net.sf.mxlosgi.chat.listener.ChatManagerListener;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.core.XmppMainManager;
import net.sf.mxlosgi.core.Future;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.disco.DiscoInfoManager;
import net.sf.mxlosgi.disco.DiscoInfoPacketExtension;
import net.sf.mxlosgi.disco.DiscoItemsManager;
import net.sf.mxlosgi.disco.DiscoItemsPacketExtension;
import net.sf.mxlosgi.lastactivity.LastActivityManager;
import net.sf.mxlosgi.lastactivity.LastActivityPacketExtension;
import net.sf.mxlosgi.lastactivity.listener.LastActivityListener;
import net.sf.mxlosgi.privacy.PrivacyManager;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.Message;
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
//		String serviceName = "gmail.com";
		String serviceName = "jabber.org";
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
