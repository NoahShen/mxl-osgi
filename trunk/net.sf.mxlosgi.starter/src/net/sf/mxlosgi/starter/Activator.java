package net.sf.mxlosgi.starter;

import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.core.XmppMainManager;
import net.sf.mxlosgi.core.Future;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.disco.DiscoInfoManager;
import net.sf.mxlosgi.disco.DiscoInfoPacketExtension;
import net.sf.mxlosgi.disco.DiscoItemsManager;
import net.sf.mxlosgi.disco.DiscoItemsPacketExtension;
import net.sf.mxlosgi.xmpp.JID;
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
		
		
		//testDisconnect(connection);
//		testDisco(connection, context);
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
