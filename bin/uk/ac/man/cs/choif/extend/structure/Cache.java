package uk.ac.man.cs.choif.extend.structure;

import java.util.*;
/**
 * A general purpose cache - uses least recently added policy for removal
 * Creation date: (09/01/99 04:22:10)
 * @author: Freddy Choi
 */
public class Cache {
	/** Size of the cache */
	private int N = 20;
	
	/** Maps input to output */
	private Hashtable map = new Hashtable();

	/** Access queue */
	private Vector queue = new Vector();
/**
 * Default cache (size = 20).
 */
public Cache() {
	this(20);
}
/**
 * Create a cache of size N. If N = -1 then the cache does not resize. It becomes a dictionary.
 * Creation date: (09/01/99 04:27:56)
 * @param N int
 */
public Cache(int N) {
	this.N = N;
	if (N > 0) {
		map = new Hashtable(N);
		queue = new Vector(N);
	}
}
/**
 * Get an object from the cache
 * Creation date: (09/01/99 04:36:44)
 * @return java.lang.Object
 * @param key java.lang.Object
 */
public Object get(Object key) {
	return map.get(key);
}
/**
 * Put information into cache.
 * Creation date: (09/01/99 04:42:18)
 * @param key java.lang.Object
 * @param output java.lang.Object
 */
public void put(Object key, Object output) {
	map.put(key, output);
	if (N != -1) {
		if (map.size() > N) {
			map.remove(queue.lastElement());
			queue.removeElementAt(queue.size()-1);
		}
		queue.insertElementAt(key, 0);
	}
}
}
