// GenericsNote: Converted.
package net.sf.mxlosgi.utils.collections;

/**
 * Defines a simple key value pair. <p/> A Map Entry has considerable additional
 * semantics over and above a simple key-value pair. This interface defines the
 * minimum key value, with just the two get methods.
 * 
 * @author Matt Hall, John Watkinson, Stephen Colebourne
 * @version $Revision: 1.1 $ $Date: 2005/10/11 17:05:19 $
 * @since Commons Collections 3.0
 */
public interface KeyValue<K, V>
{

	/**
	 * Gets the key from the pair.
	 * 
	 * @return the key
	 */
	K getKey();

	/**
	 * Gets the value from the pair.
	 * 
	 * @return the value
	 */
	V getValue();

}
