package net.sf.mxlosgi.sasl.impl;

import net.sf.mxlosgi.sasl.SaslMechanism;
import net.sf.mxlosgi.utils.Base64;


/**
 * @author noah
 * 
 */
public class PlainSaslMechanism implements SaslMechanism
{

	public PlainSaslMechanism()
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