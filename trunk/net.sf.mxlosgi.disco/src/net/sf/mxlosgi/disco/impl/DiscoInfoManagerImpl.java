/**
 * 
 */
package net.sf.mxlosgi.disco.impl;

import net.sf.mxlosgi.core.ServerTimeoutException;
import net.sf.mxlosgi.core.StanzaCollector;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.core.filter.PacketIDFilter;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.disco.DiscoInfoFeature;
import net.sf.mxlosgi.disco.DiscoInfoIdentity;
import net.sf.mxlosgi.disco.DiscoInfoManager;
import net.sf.mxlosgi.disco.DiscoInfoPacketExtension;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.XmlStanza;


/**
 * @author noah
 *
 */
public class DiscoInfoManagerImpl implements DiscoInfoManager, StanzaReceListener
{

	private DiscoInfoFeatureServiceTracker discoInfoFeatureServiceTracker;
	
	private DiscoInfoIdentityServiceTracker discoInfoIdentityServiceTracker;
	
	/**
	 * 
	 * @param connection
	 */
	public DiscoInfoManagerImpl(DiscoInfoFeatureServiceTracker discoInfoFeatureServiceTracker,
							DiscoInfoIdentityServiceTracker discoInfoIdentityServiceTracker)
	{
		this.discoInfoFeatureServiceTracker = discoInfoFeatureServiceTracker;
		this.discoInfoIdentityServiceTracker = discoInfoIdentityServiceTracker;
	}


	@Override
	public DiscoInfoPacketExtension getDiscoInfo(XmppConnection connection, JID to) throws XmppException
	{
		return getDiscoInfo(connection, to, null);
	}

	@Override
	public DiscoInfoPacketExtension getDiscoInfo(XmppConnection connection, JID to, String node) throws XmppException
	{
		Iq iq = new Iq(Iq.Type.get);
		iq.setTo(to);
		DiscoInfoPacketExtension discoInfo = new DiscoInfoPacketExtension();
		discoInfo.setNode(node);
		iq.addExtension(discoInfo);
		
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XmlStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (stanza == null)
		{
			throw new ServerTimeoutException("no response from server");
		}
		if (stanza instanceof Iq)
		{
			Iq iqResponse = (Iq) stanza;
			Iq.Type type = iqResponse.getType();
			if (type == Iq.Type.result)
			{
				DiscoInfoPacketExtension discoInfoResponse 
					= (DiscoInfoPacketExtension) iqResponse.getExtension(DiscoInfoPacketExtension.ELEMENTNAME, 
															DiscoInfoPacketExtension.NAMESPACE);
				return discoInfoResponse;
			}
			else if (type == Iq.Type.error)
			{
				throw new XmppException(iqResponse.getError());
			}
		}
		return null;
	}

	@Override
	public void processReceStanza(XmppConnection connection, XmlStanza stanza)
	{
		Iq iq = (Iq) stanza;
		JID from = iq.getFrom();
		DiscoInfoPacketExtension discoInfo 
			= (DiscoInfoPacketExtension) iq.getExtension(DiscoInfoPacketExtension.ELEMENTNAME, 
												DiscoInfoPacketExtension.NAMESPACE);
		
		String node = discoInfo.getNode();
		
		Iq iqResponse = new Iq(Iq.Type.result);
		iqResponse.setStanzaID(iq.getStanzaID());
		iqResponse.setTo(from);
		
		DiscoInfoPacketExtension discoInfoResponse = new DiscoInfoPacketExtension();
		discoInfoResponse.setNode(node);
		
		processIdentity(discoInfoResponse, node);
		
		processFeature(discoInfoResponse, node);
		
		iqResponse.addExtension(discoInfoResponse);
		
		connection.sendStanza(iqResponse);
	}

	private void processFeature(DiscoInfoPacketExtension discoInfoResponse, String node)
	{
		DiscoInfoFeature[] features = discoInfoFeatureServiceTracker.getDiscoInfoFeatures(node);
		for (DiscoInfoFeature feature : features)
		{
			DiscoInfoPacketExtension.Feature featureXML 
					= new DiscoInfoPacketExtension.Feature(feature.getFeature());
					discoInfoResponse.addFeature(featureXML);
		}
		
	}

	private void processIdentity(DiscoInfoPacketExtension discoInfoResponse, String node)
	{
		DiscoInfoIdentity[] identities = discoInfoIdentityServiceTracker.getDiscoInfoIdentities(node);
		for (DiscoInfoIdentity identity : identities)
		{
			DiscoInfoPacketExtension.Identity identityXML 
				= new DiscoInfoPacketExtension.Identity(identity.getCategory(),
												identity.getType(),
												identity.getName());
			discoInfoResponse.addIdentity(identityXML);
		}
	}


}
