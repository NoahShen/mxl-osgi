package net.sf.mxlosgi.starter;

import net.sf.mxlosgi.core.XmppMainManager;
import net.sf.mxlosgi.core.Future;
import net.sf.mxlosgi.core.XmppConnection;

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
		
		if ("gmail.com".equals(serviceName))
		{
			connection.login("Noah.Shen87", "159357noah");
		}
		else if ("jabbercn.org".equals(serviceName))
		{
			connection.login("Noah", "159357");
		}
		else if ("jabber.org".equals(serviceName))
		{
			connection.login("NoahShen", "159357");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}

}
