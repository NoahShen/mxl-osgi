/**
 * 
 */
package net.sf.mxlosgi.disco.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.mxlosgi.disco.DiscoInfoFeature;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class DiscoInfoFeatureServiceTracker extends ServiceTracker
{

	public DiscoInfoFeatureServiceTracker(BundleContext context)
	{
		super(context, DiscoInfoFeature.class.getName(), null);
	}

	public DiscoInfoFeature[] getDiscoInfoFeatures()
	{
		return getDiscoInfoFeatures(null);
	}
	
	public DiscoInfoFeature[] getDiscoInfoFeatures(String node)
	{
		List<DiscoInfoFeature> features = new ArrayList<DiscoInfoFeature>();
		Object[] services = getServices();
		if (services == null)
		{
			return features.toArray(new DiscoInfoFeature[]{});
		}
		
		for (Object obj : services)
		{
			DiscoInfoFeature feature = (DiscoInfoFeature) obj;
			if (node == null)
			{
				if (feature.getNode() == null)
				{
					features.add(feature);
				}
			}
			else
			{
				if (node.equals(feature.getNode()))
				{
					features.add(feature);
				}
			}
		}
		
		return features.toArray(new DiscoInfoFeature[]{});
		
	}
}
