package uk.ac.man.cs.choif.extend.structure;

import java.util.*;
/**
 * Implements a set.
 * Note: Don't forget to override both "equals" and "hashCode" methods.
 * Creation date: (10/05/99 10:43:34)
 * @author: Freddy Choi
 */
public class Set extends java.util.Hashtable {
	private final static Boolean dummy = new Boolean(true);
/**
 * Set constructor comment.
 */
public Set() {
	super();
}
/**
 * Set constructor comment.
 * @param initialCapacity int
 */
public Set(int initialCapacity) {
	super(initialCapacity);
}
/**
 * Set constructor comment.
 * @param initialCapacity int
 * @param loadFactor float
 */
public Set(int initialCapacity, float loadFactor) {
	super(initialCapacity, loadFactor);
}
/**
 * Set equals
 * Creation date: (10/05/99 10:48:24)
 * @return boolean
 * @param obj java.lang.Object
 */
public boolean equals(Object obj) {
	try {
		Set S = (Set) obj;
		if (S.size() != size()) return false;
		if (S.size() < size()) {
			for (Enumeration e=S.keys(); e.hasMoreElements();) {
				if (!containsKey(e.nextElement())) return false;
			}
			return true;
		}
		else {
			for (Enumeration e=keys(); e.hasMoreElements();) {
				if (!S.containsKey(e.nextElement())) return false;
			}
			return true;
		}
	}
	catch (Exception e) {
		return false;
	}
}
/**
 * Intersect of this set and S
 * Creation date: (10/05/99 12:01:03)
 * @return uk.ac.man.cs.choif.extend.structure.Set
 * @param S uk.ac.man.cs.choif.extend.structure.Set
 */
public Set intersect(Set S) {
	Set X;
	Object obj;
	if (S.size() < size()) {
		X = new Set(S.size());
		for (Enumeration e=S.keys(); e.hasMoreElements();) {
			obj = e.nextElement();
			if (containsKey(obj)) X.put(obj, dummy);
		}
	}
	else {
		X = new Set(size());
		for (Enumeration e=keys(); e.hasMoreElements();) {
			obj = e.nextElement();
			if (S.containsKey(obj)) X.put(obj, dummy);
		}
	}		
	return X;
}
/**
 * 
 * Creation date: (10/11/99 13:37:22)
 * @return boolean
 * @param obj java.lang.Object
 */
public boolean member(Object obj) {
	return containsKey(obj);
}
/**
 * Put object into this set
 * Creation date: (10/05/99 10:44:29)
 * @param obj java.lang.Object
 */
public void put(Object obj) {
	put(obj, dummy);
}
/**
 * Subtract S from this set.
 * Creation date: (10/05/99 10:51:37)
 * @param S uk.ac.man.cs.choif.extend.structure.Set
 */
public void subtract(Set S) {
	if (S.size() < size()) {
		for (Enumeration e=S.keys(); e.hasMoreElements();) remove(e.nextElement());
	}
	else {
		Object obj;
		for (Enumeration e=keys(); e.hasMoreElements();) {
			obj = e.nextElement();
			if (S.containsKey(obj)) remove(obj);
		}
	}
}
/**
 * Add S to this set.
 * Creation date: (10/05/99 10:46:31)
 * @param A uk.ac.man.cs.choif.extend.structure.Set
 */
public void union(Set S) {
	for (Enumeration e=S.keys(); e.hasMoreElements();) put(e.nextElement(), dummy);
}
}
