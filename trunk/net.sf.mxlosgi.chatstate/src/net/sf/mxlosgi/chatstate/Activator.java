package net.sf.mxlosgi.chatstate;


import net.sf.mxlosgi.chatstate.parser.ActiveChatStateExtensionParser;
import net.sf.mxlosgi.chatstate.parser.ComposingChatStateExtensionParser;
import net.sf.mxlosgi.chatstate.parser.GoneChatStateExtensionParser;
import net.sf.mxlosgi.chatstate.parser.InactiveChatStateExtensionParser;
import net.sf.mxlosgi.chatstate.parser.PausedChatStateExtensionParser;
import net.sf.mxlosgi.disco.DiscoInfoFeature;
import net.sf.mxlosgi.xmppparser.ExtensionParser;
import net.sf.mxlosgi.xmppparser.XmppParser;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator, ServiceListener
{

	private Bundle bundle;
	private ServiceRegistration chatstatesFeatureRegistration;
	private ServiceRegistration activeChatParserRegistration;
	private ServiceRegistration composingChatParserRegistration;
	private ServiceRegistration goneChatParserRegistration;
	private ServiceRegistration inactiveChatParserRegistration;
	private ServiceRegistration pausedChatParserRegistration;;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		this.bundle = context.getBundle();
		
		ActiveChatStateExtensionParser activeChatParser = new ActiveChatStateExtensionParser();
		activeChatParserRegistration = context.registerService(ExtensionParser.class.getName(), activeChatParser, null);
		
		ComposingChatStateExtensionParser composingChatParser = new ComposingChatStateExtensionParser();
		composingChatParserRegistration = context.registerService(ExtensionParser.class.getName(), composingChatParser, null);
		
		GoneChatStateExtensionParser goneChatParser = new GoneChatStateExtensionParser();
		goneChatParserRegistration = context.registerService(ExtensionParser.class.getName(), goneChatParser, null);
		
		InactiveChatStateExtensionParser inactiveChatParser = new InactiveChatStateExtensionParser();
		inactiveChatParserRegistration = context.registerService(ExtensionParser.class.getName(), inactiveChatParser, null);
		
		PausedChatStateExtensionParser pausedChatParser = new PausedChatStateExtensionParser();
		pausedChatParserRegistration = context.registerService(ExtensionParser.class.getName(), pausedChatParser, null);
		
		DiscoInfoFeature chatstatesFeature = new DiscoInfoFeature(null, "http://jabber.org/protocol/chatstates");
		chatstatesFeatureRegistration = context.registerService(DiscoInfoFeature.class.getName(), chatstatesFeature, null);
		
		String filter = "(objectclass=" + XmppParser.class.getName() + ")";  
		context.addServiceListener(this, filter);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		if (chatstatesFeatureRegistration != null)
		{
			chatstatesFeatureRegistration.unregister();
			chatstatesFeatureRegistration = null;
		}
		
		if (activeChatParserRegistration != null)
		{
			activeChatParserRegistration.unregister();
			activeChatParserRegistration = null;
		}
		
		if (composingChatParserRegistration != null)
		{
			composingChatParserRegistration.unregister();
			composingChatParserRegistration = null;
		}
		
		if (goneChatParserRegistration != null)
		{
			goneChatParserRegistration.unregister();
			goneChatParserRegistration = null;
		}
		
		if (inactiveChatParserRegistration != null)
		{
			inactiveChatParserRegistration.unregister();
			inactiveChatParserRegistration = null;
		}
		
		if (pausedChatParserRegistration != null)
		{
			pausedChatParserRegistration.unregister();
			pausedChatParserRegistration = null;
		}
		
		bundle = null;
	}

	@Override
	public void serviceChanged(ServiceEvent event)
	{
		int eventType = event.getType();
		if (eventType == ServiceEvent.UNREGISTERING)
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
}
