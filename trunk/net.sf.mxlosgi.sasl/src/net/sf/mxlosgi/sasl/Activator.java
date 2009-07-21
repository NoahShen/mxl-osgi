package net.sf.mxlosgi.sasl;


import net.sf.mxlosgi.sasl.impl.DigestMd5SaslMechanism;
import net.sf.mxlosgi.sasl.impl.PlainSaslMechanism;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

	private ServiceRegistration srPlain;

	private ServiceRegistration srDigestMd5;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		PlainSaslMechanism plain = new PlainSaslMechanism();
		DigestMd5SaslMechanism digestMd5 = new DigestMd5SaslMechanism();
		srPlain = context.registerService(SaslMechanism.class.getName(), plain, null);
		srDigestMd5 = context.registerService(SaslMechanism.class.getName(), digestMd5, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		if (srPlain != null)
		{
			srPlain.unregister();
			srPlain = null;
		}
		
		if (srDigestMd5 != null)
		{
			srDigestMd5.unregister();
			srDigestMd5 = null;
		}
	}
}
