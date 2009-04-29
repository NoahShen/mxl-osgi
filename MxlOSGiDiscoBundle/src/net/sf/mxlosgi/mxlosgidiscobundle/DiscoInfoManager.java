package net.sf.mxlosgi.mxlosgidiscobundle;

import net.sf.mxlosgi.mxlosgidiscobundle.listener.DiscoInfoFeatureListener;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

/**
 * @author noah
 *
 */
public interface DiscoInfoManager
{
	/**
	 * 
	 * @param listener
	 */
	public void addDiscoInfoFeatureListener(DiscoInfoFeatureListener listener);
	
	/**
	 * 
	 * @param listener
	 */
	public void removeDiscoInfoFeatureListener(DiscoInfoFeatureListener listener);
	
	/**
	 * 
	 * @return
	 */
	public DiscoInfoFeatureListener[] getDiscoInfoFeatureListeners();
	
	/**
	 * 
	 * @param feature
	 */
	public void addFeature(DiscoInfoFeature feature);
	
	/**
	 * 
	 * @param feature
	 */
	public void removeFeature(DiscoInfoFeature feature);
	
	/**
	 * 
	 * @param identity
	 */
	public void addIdentity(DiscoInfoIdentity identity);
	
	/**
	 * 
	 * @param identity
	 */
	public void removeIdentity(DiscoInfoIdentity identity);
	
	/**
	 * 
	 * @param connection
	 * @param to
	 * @return
	 * @throws XMPPException
	 */
	public DiscoInfoPacketExtension getDiscoInfo(XMPPConnection connection, JID  to) throws XMPPException;
	
	/**
	 * 
	 * @param connection
	 * @param to
	 * @param node
	 * @return
	 * @throws XMPPException
	 */
	public DiscoInfoPacketExtension getDiscoInfo(XMPPConnection connection, JID  to, String node) throws XMPPException;
}