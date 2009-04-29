package net.sf.mxlosgi.mxlosgisaslbundle;

import net.sf.mxlosgi.mxlosgisaslbundle.impl.DIGESTMD5SASLMechanism;
import net.sf.mxlosgi.mxlosgisaslbundle.impl.PLAINSASLMechanism;
import net.sf.mxlosgi.mxlosgisaslbundle.impl.SASLManagerImpl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator
{

	private ServiceRegistration sr;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		SASLManagerImpl saslManager = new SASLManagerImpl();
		saslManager.addSASLMechanism(new PLAINSASLMechanism());
		saslManager.addSASLMechanism(new DIGESTMD5SASLMechanism());
		sr = context.registerService(SASLManager.class.getName(), saslManager, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		if (sr != null)
		{
			sr.unregister();
			sr = null;
		}
	}
}
