// GenericsNote: Converted.
package net.sf.mxlosgi.utils.collections;

/**
 * Abstract pair class to assist with creating KeyValue and MapEntry
 * implementations.
 * 
 * @author James Strachan
 * @author Michael A. Smith
 * @author Neil O'Toole
 * @author Matt Hall, John Watkinson, Stephen Colebourne
 * @version $Revision: 1.1 $ $Date: 2005/10/11 17:05:32 $
 * @since Commons Collections 3.0
 */
public abstract class AbstractKeyValue<K, V> implements KeyValue<K, V>
{

	/**
	 * The key
	 */
	protected K key;

	/**
	 * The value
	 */
	protected V value;

	/**
	 * Constructs a new pair with the specified key and given value.
	 * 
	 * @param key
	 *                  the key for the entry, may be null
	 * @param value
	 *                  the value for the entry, may be null
	 */
	protected AbstractKeyValue(K key, V value)
	{
		super();
		this.key = key;
		this.value = value;
	}

	/**
	 * Gets the key from the pair.
	 * 
	 * @return the key
	 */
	public K getKey()
	{
		return key;
	}

	/**
	 * Gets the value from the pair.
	 * 
	 * @return the value
	 */
	public V getValue()
	{
		return value;
	}

	/**
	 * Gets a debugging String view of the pair.
	 * 
	 * @return a String view of the entry
	 */
	public String toString()
	{
		return new StringBuffer().append(getKey()).append('=').append(getValue()).toString();
	}

}
