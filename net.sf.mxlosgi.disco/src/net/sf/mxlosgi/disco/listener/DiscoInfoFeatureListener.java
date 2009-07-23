/**
 * 
 */
package net.sf.mxlosgi.disco.listener;

import net.sf.mxlosgi.disco.DiscoInfoFeature;


/**
 * @author noah
 *
 */
public interface DiscoInfoFeatureListener
{
	/**
	 * 
	 * @param feature
	 */
	public void discoInfoFeatureAdded(DiscoInfoFeature feature);
	
	/**
	 * 
	 * @param feature
	 */
	public void discoInfoFeatureRemoved(DiscoInfoFeature feature);
}
