/**
 * 
 */
package net.sf.mxlosgi.mxlosgidiscobundle.listener;

import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoFeature;

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
