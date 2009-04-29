/**
 * 
 */
package net.sf.mxlosgi.mxlosgisaslbundle;

/**
 * @author noah
 *
 */
public interface SASLMechanism
{
	/**
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public String getAuthenticationText(String username, String password, String host) throws Exception;
	
	/**
	 * 
	 * @return
	 */
	public String getChallengeResponse(String challenge) throws Exception;
}
