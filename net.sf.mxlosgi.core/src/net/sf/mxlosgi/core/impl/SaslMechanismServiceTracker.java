/**
 * 
 */
package net.sf.mxlosgi.core.impl;

import net.sf.mxlosgi.sasl.SaslMechanism;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class SaslMechanismServiceTracker extends ServiceTracker
{

	public SaslMechanismServiceTracker(BundleContext context)
	{
		super(context, SaslMechanism.class.getName(), null);
	}

	public SaslMechanism getSaslMechanism(String mechanism)
	{
		Object[] services = getServices();
		if (services == null)
		{
			return null;
		}
		
		for (Object obj : services)
		{
			SaslMechanism saslMechanism = (SaslMechanism) obj;
			if (saslMechanism.getName().equals(mechanism))
			{
				return saslMechanism;
			}
		}
		return null;
	}
}
