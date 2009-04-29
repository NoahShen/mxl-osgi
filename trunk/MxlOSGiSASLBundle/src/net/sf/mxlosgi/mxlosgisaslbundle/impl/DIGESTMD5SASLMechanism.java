/**
 * 
 */
package net.sf.mxlosgi.mxlosgisaslbundle.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.RealmChoiceCallback;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

import net.sf.mxlosgi.mxlosgisaslbundle.SASLMechanism;
import net.sf.mxlosgi.mxlosgiutilsbundle.Base64;

/**
 * @author noah
 * 
 */
public class DIGESTMD5SASLMechanism implements SASLMechanism, CallbackHandler
{

	private SaslClient sc;

	private String username;

	private String password;

	private String hostname;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgisaslbundle.SASLMechanism#getAuthenticationText(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public String getAuthenticationText(String username, String password, String host) throws SaslException
	{
		this.username = username.toLowerCase();
		this.password = password;
		this.hostname = host;

		String[] mechanisms = { getName() };
		Map<String, String> props = new HashMap<String, String>();
		sc = Sasl.createSaslClient(mechanisms, username, "xmpp", host, props, this);

		if (sc.hasInitialResponse())
		{
			byte[] response = sc.evaluateChallenge(new byte[0]);
			String authenticationText = Base64.encodeBytes(response, Base64.DONT_BREAK_LINES);
			return authenticationText;
		}
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgisaslbundle.SASLMechanism#getChallengeResponse(java.lang.String)
	 */
	@Override
	public String getChallengeResponse(String challenge) throws SaslException
	{
		byte response[] = null;
		if (challenge != null)
		{
			response = sc.evaluateChallenge(Base64.decode(challenge));
		}
		else
		{
			response = sc.evaluateChallenge(null);
		}
		if (response == null)
		{
			return null;
		}
		String authenticationText = Base64.encodeBytes(response, Base64.DONT_BREAK_LINES);
		if (authenticationText.equals(""))
		{
			authenticationText = "=";
		}

		return authenticationText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.mxlosgi.mxlosgisaslbundle.SASLMechanism#getName()
	 */
	@Override
	public String getName()
	{
		return "DIGEST-MD5";
	}

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
	{
		for (int i = 0; i < callbacks.length; i++)
		{
			if (callbacks[i] instanceof NameCallback)
			{
				NameCallback ncb = (NameCallback) callbacks[i];
				ncb.setName(username);
			}
			else if (callbacks[i] instanceof PasswordCallback)
			{
				PasswordCallback pcb = (PasswordCallback) callbacks[i];
				pcb.setPassword(password.toCharArray());
			}
			else if (callbacks[i] instanceof RealmCallback)
			{
				RealmCallback rcb = (RealmCallback) callbacks[i];
				rcb.setText(hostname);
			}
			else if (callbacks[i] instanceof RealmChoiceCallback)
			{
				// unused
				// RealmChoiceCallback rccb =
				// (RealmChoiceCallback)callbacks[i];
			}
			else
			{
				throw new UnsupportedCallbackException(callbacks[i]);
			}
		}
	}

}
