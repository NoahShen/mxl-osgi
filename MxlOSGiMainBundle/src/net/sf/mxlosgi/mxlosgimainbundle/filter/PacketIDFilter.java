package net.sf.mxlosgi.mxlosgimainbundle.filter;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgixmppbundle.Packet;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;


public class PacketIDFilter implements StanzaFilter
{

	private String stanzaID;

	/**
	 * Creates a new stanza ID filter using the specified stanza ID.
	 * 
	 * @param stanzaID
	 *                  the stanza ID to filter for.
	 */
	public PacketIDFilter(String stanzaID)
	{
		if (stanzaID == null)
		{
			throw new IllegalArgumentException("Packet ID cannot be null.");
		}
		this.stanzaID = stanzaID;
	}

	@Override
	public boolean accept(XMPPConnection connection, XMLStanza stanza)
	{
		if (stanza instanceof Packet)
		{
			Packet packet = (Packet) stanza;
			if (stanzaID.equals(packet.getStanzaID()))
			{
				return true;
			}
		}
		return false;
	}
	

}
