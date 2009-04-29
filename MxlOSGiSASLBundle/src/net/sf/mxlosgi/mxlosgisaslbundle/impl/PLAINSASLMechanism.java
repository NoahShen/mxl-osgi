package net.sf.mxlosgi.mxlosgisaslbundle.impl;

import net.sf.mxlosgi.mxlosgisaslbundle.SASLMechanism;
import net.sf.mxlosgi.mxlosgiutilsbundle.Base64;

/**
 * @author noah
 * 
 */
public class PLAINSASLMechanism implements SASLMechanism
{

	public PLAINSASLMechanism()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlsaslbundle.SASLMechanism#getAuthenticationText(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String getAuthenticationText(String username, String password, String host)
	{
		return Base64.encodeBytes(('\0' + username + '\0' + password).getBytes());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxl.mxlsaslbundle.SASLMechanism#getName()
	 */
	@Override
	public String getName()
	{
		return "PLAIN";
	}

	@Override
	public String getChallengeResponse(String challenge)
	{
		return null;
	}

}