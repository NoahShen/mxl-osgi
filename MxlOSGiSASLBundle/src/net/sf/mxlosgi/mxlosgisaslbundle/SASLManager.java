/**
 * 
 */
package net.sf.mxlosgi.mxlosgisaslbundle;

/**
 * @author noah
 *
 */
public interface SASLManager
{
	public void addSASLMechanism(SASLMechanism saslMechanism);
	
	public void removeSASLMechanism(SASLMechanism saslMechanism);
	
	public void removeSASLMechanism(String mechanism);
	
	public SASLMechanism getSASLMechanism(String mechanism);
}
