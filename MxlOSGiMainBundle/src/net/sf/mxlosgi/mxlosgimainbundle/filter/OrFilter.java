package net.sf.mxlosgi.mxlosgimainbundle.filter;

import java.util.ArrayList;
import java.util.List;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * 
 * @author noah
 * 
 */
public class OrFilter implements StanzaFilter
{

	private List<StanzaFilter> filters = new ArrayList<StanzaFilter>();

	/**
	 * Creates an empty OR filter.
	 */
	public OrFilter()
	{
	}

	/**
	 * Creates an OR filter using the two specified filters.
	 * 
	 * @param filters
	 */
	public OrFilter(StanzaFilter... filters)
	{
		if (filters == null)
		{
			throw new IllegalArgumentException("Parameter cannot be null.");
		}
		for (StanzaFilter filter : filters)
		{
			if (filter == null)
			{
				throw new IllegalArgumentException("Parameter cannot be null.");
			}
			this.filters.add(filter);
		}
	}

	/**
	 * Adds a filter to the filter list for the OR operation. A packet
	 * will pass the filter if any filter in the list accepts it.
	 * 
	 * @param filter
	 *                  a filter to add to the filter list.
	 */
	public void addFilter(StanzaFilter filter)
	{
		if (filter == null)
		{
			throw new IllegalArgumentException("Parameter cannot be null.");
		}
		filters.add(filter);
	}

	@Override
	public boolean accept(XMPPConnection connection, XMLStanza data)
	{
		for (int i = 0; i < filters.size(); ++i)
		{
			StanzaFilter filter = filters.get(i);
			if (filter.accept(connection, data))
			{
				return true;
			}
		}
		return false;
	}
}