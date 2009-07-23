/**
 * 
 */
package net.sf.mxlosgi.disco.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.mxlosgi.disco.DiscoInfoIdentity;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class DiscoInfoIdentityServiceTracker extends ServiceTracker
{

	public DiscoInfoIdentityServiceTracker(BundleContext context)
	{
		super(context, DiscoInfoIdentity.class.getName(), null);
	}
	
	public DiscoInfoIdentity[] getDiscoInfoIdentities()
	{
		return getDiscoInfoIdentities(null);
	}
	
	public DiscoInfoIdentity[] getDiscoInfoIdentities(String node)
	{
		
		List<DiscoInfoIdentity> identities = new ArrayList<DiscoInfoIdentity>();
		Object[] services = getServices();
		if (services == null)
		{
			return identities.toArray(new DiscoInfoIdentity[]{});
		}
		
		for (Object obj : services)
		{
			DiscoInfoIdentity identity = (DiscoInfoIdentity) obj;
			if (node == null)
			{
				if (identity.getNode() == null)
				{
					identities.add(identity);
				}
			}
			else
			{
				if (node.equals(identity.getNode()))
				{
					identities.add(identity);
				}
			}
		}
		
		return identities.toArray(new DiscoInfoIdentity[]{});
	}
}
