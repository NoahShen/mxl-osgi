/**
 * 
 */
package net.sf.mxlosgi.mxlosgidiscobundle.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoFeature;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoIdentity;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoManager;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoPacketExtension;
import net.sf.mxlosgi.mxlosgidiscobundle.listener.DiscoInfoFeatureListener;
import net.sf.mxlosgi.mxlosgimainbundle.ServerTimeoutException;
import net.sf.mxlosgi.mxlosgimainbundle.StanzaCollector;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgimainbundle.filter.PacketIDFilter;
import net.sf.mxlosgi.mxlosgimainbundle.listener.StanzaReceListener;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class DiscoInfoManagerImpl implements DiscoInfoManager, StanzaReceListener
{

	private List<DiscoInfoFeature> features = Collections.synchronizedList(new LinkedList<DiscoInfoFeature>());
	
	private List<DiscoInfoIdentity> identities = Collections.synchronizedList(new LinkedList<DiscoInfoIdentity>());
	
	private List<DiscoInfoFeatureListener> discoInfoFeatureListeners = new ArrayList<DiscoInfoFeatureListener>();
	
	/**
	 * 
	 * @param connection
	 */
	public DiscoInfoManagerImpl()
	{
	}

	@Override
	public void addDiscoInfoFeatureListener(DiscoInfoFeatureListener listener)
	{
		if (!discoInfoFeatureListeners.contains(listener))
		{
			discoInfoFeatureListeners.add(listener);
		}
	}

	@Override
	public DiscoInfoFeatureListener[] getDiscoInfoFeatureListeners()
	{
		return discoInfoFeatureListeners.toArray(new DiscoInfoFeatureListener[]{});
	}

	@Override
	public void removeDiscoInfoFeatureListener(DiscoInfoFeatureListener listener)
	{
		discoInfoFeatureListeners.remove(listener);
	}
	
	private void fireDiscoInfoFeatureListenerAdded(DiscoInfoFeature feature)
	{
		for (DiscoInfoFeatureListener listener : getDiscoInfoFeatureListeners())
		{
			listener.discoInfoFeatureAdded(feature);
		}
	}
	
	private void fireDiscoInfoFeatureListenerRemoved(DiscoInfoFeature feature)
	{
		for (DiscoInfoFeatureListener listener : getDiscoInfoFeatureListeners())
		{
			listener.discoInfoFeatureRemoved(feature);
		}
	}
	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoManager#addFeature(net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoFeature)
	 */
	@Override
	public void addFeature(DiscoInfoFeature feature)
	{
		if (!features.contains(feature))
		{
			features.add(feature);
			fireDiscoInfoFeatureListenerAdded(feature);
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoManager#addIdentity(net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoIdentity)
	 */
	@Override
	public void addIdentity(DiscoInfoIdentity identity)
	{
		if (!identities.contains(identity))
		{
			identities.add(identity);
		}
	}

	@Override
	public DiscoInfoPacketExtension getDiscoInfo(XMPPConnection connection, JID to) throws XMPPException
	{
		return getDiscoInfo(connection, to, null);
	}

	@Override
	public DiscoInfoPacketExtension getDiscoInfo(XMPPConnection connection, JID to, String node) throws XMPPException
	{
		IQ iq = new IQ(IQ.Type.get);
		iq.setTo(to);
		DiscoInfoPacketExtension discoInfo = new DiscoInfoPacketExtension();
		discoInfo.setNode(node);
		iq.addExtension(discoInfo);
		
		StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
		connection.sendStanza(iq);
		XMLStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
		collector.cancel();
		
		if (stanza == null)
		{
			throw new ServerTimeoutException("no response from server");
		}
		if (stanza instanceof IQ)
		{
			IQ iqResponse = (IQ) stanza;
			IQ.Type type = iqResponse.getType();
			if (type == IQ.Type.result)
			{
				DiscoInfoPacketExtension discoInfoResponse 
					= (DiscoInfoPacketExtension) iqResponse.getExtension(DiscoInfoPacketExtension.ELEMENTNAME, 
															DiscoInfoPacketExtension.NAMESPACE);
				return discoInfoResponse;
			}
			else if (type == IQ.Type.error)
			{
				throw new XMPPException(iqResponse.getError());
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoManager#removeFeature(net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoFeature)
	 */
	@Override
	public void removeFeature(DiscoInfoFeature feature)
	{
		if (features.remove(feature))
		{
			fireDiscoInfoFeatureListenerRemoved(feature);
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoManager#removeIdentity(net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoIdentity)
	 */
	@Override
	public void removeIdentity(DiscoInfoIdentity identity)
	{
		identities.remove(identity);
	}

	@Override
	public void processReceStanza(XMPPConnection connection, XMLStanza stanza)
	{
		IQ iq = (IQ) stanza;
		JID from = iq.getFrom();
		DiscoInfoPacketExtension discoInfo 
			= (DiscoInfoPacketExtension) iq.getExtension(DiscoInfoPacketExtension.ELEMENTNAME, 
												DiscoInfoPacketExtension.NAMESPACE);
		
		String node = discoInfo.getNode();
		
		IQ iqResponse = new IQ(IQ.Type.result);
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
		for (DiscoInfoFeature feature : features)
		{
			if (node == null)
			{
				if (feature.getNode() == null)
				{
					DiscoInfoPacketExtension.Feature featureXML 
						= new DiscoInfoPacketExtension.Feature(feature.getFeature());
					discoInfoResponse.addFeature(featureXML);
				}
			}
			else
			{
				if (node.equals(feature.getNode()))
				{
					DiscoInfoPacketExtension.Feature featureXML 
						= new DiscoInfoPacketExtension.Feature(feature.getFeature());
					discoInfoResponse.addFeature(featureXML);
				}
			}
		}
		
	}

	private void processIdentity(DiscoInfoPacketExtension discoInfoResponse, String node)
	{
		for (DiscoInfoIdentity identity : identities)
		{
			if (node == null)
			{
				if (identity.getNode() == null)
				{
					DiscoInfoPacketExtension.Identity identityXML 
						= new DiscoInfoPacketExtension.Identity(identity.getCategory(),
														identity.getType(),
														identity.getName());
					discoInfoResponse.addIdentity(identityXML);
					
				}
			}
			else
			{
				if (node.equals(identity.getNode()))
				{
					DiscoInfoPacketExtension.Identity identityXML 
						= new DiscoInfoPacketExtension.Identity(identity.getCategory(), 
														identity.getType(),
														identity.getName());
					discoInfoResponse.addIdentity(identityXML);
				}
			}
		}
	}


}
