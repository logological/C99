package uk.ac.man.cs.choif.nlp.dictionary;

import java.util.*;
/**
 * A codec for object-index maps. Handy for creating object based tables.
 * Each unique object is assigned an unique serial number (starts at 0).
 * Creation date: (08/15/99 07:13:27)
 * @author: Freddy Choi
 */
public class IndexCodec {
	/** Object to index map */
	private Hashtable objectToIndex = new Hashtable();

	/** Index to object map */
	private Vector indexToObject = new Vector();

/**
 * IndexCodec constructor comment.
 */
public IndexCodec() {
	super();
}
/**
 * Given a serial number, get the object. Returns null if
 * serial number is not assigned to any object.
 * Creation date: (08/15/99 07:21:37)
 * @return java.lang.Object
 * @param index int
 */
public Object decode(int index) {
	if (index >= indexToObject.size()) return null;
	return indexToObject.elementAt(index);
}
/**
 * Map object to index.
 * Creation date: (08/15/99 07:16:19)
 * @return int
 * @param obj java.lang.Object
 */
public int encode(Object obj) {
	Integer I = (Integer) objectToIndex.get(obj);
	if (I == null) {
		I = new Integer(objectToIndex.size());
		objectToIndex.put(obj, I);
		indexToObject.addElement(obj);
	}
	return I.intValue();
}
/**
 * Ge all the unique objects in the codec.
 * Creation date: (08/15/99 07:26:37)
 * @return java.util.Enumeration
 */
public Enumeration getObjects() {
	return indexToObject.elements();
}
/**
 * Get the number of unique objects in the codec.
 * Creation date: (08/15/99 07:25:36)
 * @return int
 */
public int size() {
	return indexToObject.size();
}
}
