/**
 * 
 */
package net.sf.mxlosgi.mxlosgisaslbundle.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.mxlosgi.mxlosgisaslbundle.SASLManager;
import net.sf.mxlosgi.mxlosgisaslbundle.SASLMechanism;

/**
 * @author noah
 *
 */
public class SASLManagerImpl implements SASLManager
{
	private Map<String, SASLMechanism> mechanisms = Collections.synchronizedMap(new HashMap<String, SASLMechanism>());
	
	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgisaslbundle.SASLManager#addSASLMechanism(net.sf.mxlosgi.mxlosgisaslbundle.SASLMechanism)
	 */
	@Override
	public void addSASLMechanism(SASLMechanism saslMechanism)
	{
		mechanisms.put(saslMechanism.getName().toUpperCase(), saslMechanism);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgisaslbundle.SASLManager#getSASLMechanism(java.lang.String)
	 */
	@Override
	public SASLMechanism getSASLMechanism(String mechanism)
	{
		return mechanisms.get(mechanism.toUpperCase());
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgisaslbundle.SASLManager#removeSASLMechanism(net.sf.mxlosgi.mxlosgisaslbundle.SASLMechanism)
	 */
	@Override
	public void removeSASLMechanism(SASLMechanism saslMechanism)
	{
		for (Iterator<SASLMechanism> it = mechanisms.values().iterator(); it.hasNext();)
		{
			SASLMechanism mechanism = it.next();
			if (mechanism.getName().equals(saslMechanism.getName().toUpperCase()))
			{
				it.remove();
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgisaslbundle.SASLManager#removeSASLMechanism(java.lang.String)
	 */
	@Override
	public void removeSASLMechanism(String mechanism)
	{
		mechanisms.remove(mechanism.toUpperCase());
	}

}
