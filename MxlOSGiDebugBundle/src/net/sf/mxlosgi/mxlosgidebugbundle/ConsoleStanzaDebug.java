/**
 * 
 */
package net.sf.mxlosgi.mxlosgidebugbundle;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.listener.StanzaReceListener;
import net.sf.mxlosgi.mxlosgimainbundle.listener.StanzaSendListener;
import net.sf.mxlosgi.mxlosgimainbundle.listener.XMLStringListener;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class ConsoleStanzaDebug implements StanzaReceListener, StanzaSendListener, XMLStringListener
{

	@Override
	public void processReceStanza(XMPPConnection connection,XMLStanza stanza)
	{
		System.out.println("<<<<<<<<<<<<connection : " + connection.getConnectionID() + 
						" receive stanza from server:\n" + stanza.toXML());
	}

	@Override
	public void processSendStanza(XMPPConnection connection,XMLStanza stanza)
	{
		System.out.println(">>>>>>>>>>>>connection : " + connection.getConnectionID() +
						" send stanza to server:\n" + stanza.toXML());
	}

	@Override
	public void processXMLString(XMPPConnection connection,String xml)
	{
		System.out.println("<<<<<<<<<<<<connection : " + connection.getConnectionID() + 
						" receive xml from server:\n" + xml);
	}

	
}
