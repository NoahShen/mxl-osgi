/**
 * 
 */
package net.sf.mxlosgi.mxlosgiutilsbundle;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author noah
 * 
 */
public abstract class AbstractHasAttribute implements IHasAttribute
{
	private Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

	private List<AttributeListener> attributeListeners = new CopyOnWriteArrayList<AttributeListener>();
	
	
	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiutilsbundle.IHasAttribute#addAttributeListener(net.sf.mxlosgi.mxlosgiutilsbundle.IHasAttribute.AttributeListener)
	 */
	@Override
	public void addAttributeListener(AttributeListener listener)
	{
		attributeListeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiutilsbundle.IHasAttribute#getAttributeListeners()
	 */
	@Override
	public AttributeListener[] getAttributeListeners()
	{
		return attributeListeners.toArray(new AttributeListener[]{});
	}

	/* (non-Javadoc)
	 * @see net.sf.mxlosgi.mxlosgiutilsbundle.IHasAttribute#removeAttributeListener(net.sf.mxlosgi.mxlosgiutilsbundle.IHasAttribute.AttributeListener)
	 */
	@Override
	public void removeAttributeListener(AttributeListener listener)
	{
		attributeListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.joah.joahutilsbundle.IHasAttribute#containsAttribute(java.lang.String)
	 */
	@Override
	public boolean containsAttribute(String key)
	{
		return attributes.containsKey(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.joah.joahutilsbundle.IHasAttribute#getAttribute(java.lang.String)
	 */
	@Override
	public Object getAttribute(String key)
	{
		return attributes.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.joah.joahutilsbundle.IHasAttribute#getAttributeKeys()
	 */
	@Override
	public Set<String> getAttributeKeys()
	{
		synchronized (attributes)
		{
			return new HashSet<String>(attributes.keySet());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.joah.joahutilsbundle.IHasAttribute#removeAttribute(java.lang.String)
	 */
	@Override
	public Object removeAttribute(String key)
	{
		return attributes.remove(key);
	}

	/* (non-Javadoc)
	 * @see net.sf.joah.joahutilsbundle.IHasAttribute#removeAllAttributes()
	 */
	@Override
	public void removeAllAttributes()
	{
		attributes.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.joah.joahutilsbundle.IHasAttribute#setAttribute(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public Object setAttribute(String key, Object value)
	{
		if (value == null)
		{
			return removeAttribute(key);
		}
		else
		{
			return attributes.put(key, value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.joah.joahutilsbundle.IHasAttribute#setAttribute(java.lang.String)
	 */
	@Override
	public Object setAttribute(String key)
	{
		return setAttribute(key, Boolean.TRUE);
	}
	
	@Override
	public void fireAttributeUpdated(String attributeKey, Object newValue, Object oldValue)
	{
		for (AttributeListener listener : attributeListeners)
		{
			listener.attributeUpdated(this, attributeKey, newValue, oldValue);
		}
	}
	
	@Override
	public void fireAttributeRemoved(String attributeKey, Object oldValue)
	{
		for (AttributeListener listener : attributeListeners)
		{
			listener.attributeRemoved(this, attributeKey, oldValue);
		}
	}

}
