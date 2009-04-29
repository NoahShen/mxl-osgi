package net.sf.mxlosgi.mxlosgichatstatebundle;

import net.sf.mxlosgi.mxlosgichatstatebundle.parser.ActiveChatStateExtensionParser;
import net.sf.mxlosgi.mxlosgichatstatebundle.parser.ComposingChatStateExtensionParser;
import net.sf.mxlosgi.mxlosgichatstatebundle.parser.GoneChatStateExtensionParser;
import net.sf.mxlosgi.mxlosgichatstatebundle.parser.InactiveChatStateExtensionParser;
import net.sf.mxlosgi.mxlosgichatstatebundle.parser.PausedChatStateExtensionParser;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoFeature;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoManager;
import net.sf.mxlosgi.mxlosgixmppparserbundle.XMPPParser;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator, ServiceListener
{
	private ActiveChatStateExtensionParser activeChatParser;

	private ComposingChatStateExtensionParser composingChatParser;

	private GoneChatStateExtensionParser goneChatParser;

	private InactiveChatStateExtensionParser inactiveChatParser;

	private PausedChatStateExtensionParser pausedChatParser;

	private DiscoInfoFeature chatstatesFeature;

	private ServiceTracker parserServiceTracker;

	private XMPPParser parser;

	private ServiceTracker discoInfoManagerServiceTracker;

	private DiscoInfoManager discoInfoManager;

	private Bundle bundle;

	private BundleContext context;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();
		this.context = context;
		
		parserServiceTracker = new ServiceTracker(context, XMPPParser.class.getName(), null);
		parserServiceTracker.open();
		parser = (XMPPParser) parserServiceTracker.getService();
		
		discoInfoManagerServiceTracker = new ServiceTracker(context, DiscoInfoManager.class.getName(), null);
		discoInfoManagerServiceTracker.open();
		discoInfoManager = (DiscoInfoManager) discoInfoManagerServiceTracker.getService();
		
		activeChatParser = new ActiveChatStateExtensionParser();
		composingChatParser = new ComposingChatStateExtensionParser();
		goneChatParser = new GoneChatStateExtensionParser();
		inactiveChatParser = new InactiveChatStateExtensionParser();
		pausedChatParser = new PausedChatStateExtensionParser();

		chatstatesFeature = new DiscoInfoFeature(null, "http://jabber.org/protocol/chatstates");
		discoInfoManager.addFeature(chatstatesFeature);
	
		parser.addExtensionParser(activeChatParser);
		parser.addExtensionParser(composingChatParser);
		parser.addExtensionParser(goneChatParser);
		parser.addExtensionParser(inactiveChatParser);
		parser.addExtensionParser(pausedChatParser);
		
		context.addServiceListener(this);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{		
		parser.removeExtensionParser(activeChatParser);
		parser.removeExtensionParser(composingChatParser);
		parser.removeExtensionParser(goneChatParser);
		parser.removeExtensionParser(inactiveChatParser);
		parser.removeExtensionParser(pausedChatParser);
		
		discoInfoManager.removeFeature(chatstatesFeature);
		
		if (parserServiceTracker != null)
		{
			parserServiceTracker.close();
			parserServiceTracker = null;
		}
		
		if (discoInfoManagerServiceTracker != null)
		{
			discoInfoManagerServiceTracker.close();
			discoInfoManagerServiceTracker = null;
		}
		
		parser = null;
		discoInfoManager = null;
		
		chatstatesFeature = null;
		activeChatParser = null;
		composingChatParser = null;
		goneChatParser = null;
		inactiveChatParser = null;
		pausedChatParser = null;
		bundle = null;
		this.context = null;
	}

	@Override
	public void serviceChanged(ServiceEvent event)
	{
		int eventType = event.getType();
		Object obj = context.getService(event.getServiceReference());
		if (eventType == ServiceEvent.UNREGISTERING)
		{
			if (obj == parser)
			{
				try
				{
					bundle.uninstall();
				}
				catch (BundleException e)
				{
					//e.printStackTrace();
				}
			}
		}
		else if (eventType == ServiceEvent.REGISTERED)
		{
			if (obj == discoInfoManager)
			{
				discoInfoManager.addFeature(chatstatesFeature);
			}
		}
	}
}
