package uk.ac.man.cs.choif.extend;

import uk.ac.man.cs.choif.extend.sort.*;
import java.util.*;
import sun.misc.*;
/**
 * Functions for list manipulation
 * Creation date: (07/18/99 11:23:07)
 * @author: Freddy Choi
 */
public class Vectorx {
/**
 * Append B to A.
 * Creation date: (07/24/99 19:42:27)
 * @param A java.util.Vector
 * @param B java.util.Vector
 */
public static void append(Vector A, Vector B) {
	for (int i=0, ie=B.size(); i<ie; i++) A.addElement(B.elementAt(i));
}
/**
 * Concatenate two lists.
 * Creation date: (07/24/99 00:37:33)
 * @return java.util.Vector A new vector {A, B}
 * @param A java.util.Vector
 * @param B java.util.Vector
 */
public static Vector concat(Vector A, Vector B) {
	Vector C = new Vector(A.size() + B.size());
	for (int i=0, ie=A.size(); i<ie; i++) C.addElement(A.elementAt(i));
	for (int i=0, ie=B.size(); i<ie; i++) C.addElement(B.elementAt(i));
	return C;
}
/**
 * Convert hashtable keys into a vector.
 * Creation date: (07/18/99 13:36:34)
 * @return java.util.Vector
 * @param H java.util.Hashtable
 */
public static Vector keyToVector(Hashtable H) {
	Vector v = new Vector();
	for (Enumeration e=H.keys(); e.hasMoreElements();) v.addElement(e.nextElement());
	return v;
}
/**
 * Make a copy of v.
 * Creation date: (07/22/99 15:20:43)
 * @return java.util.Vector
 * @param v java.util.Vector
 */
public static Vector makeCopy(Vector v) {
	Vector nV = new Vector(v.size());
	for (int i=0, ie=v.size(); i<ie; i++) nV.addElement(v.elementAt(i));
	return nV;
}
/**
 * Compute all permutations for V
 * Creation date: (07/22/99 14:54:00)
 * @return java.util.Vector A vector of vectors, each is a permutation of V.
 * @param V java.util.Vector A list of objects
 */
public static Vector permutations(Vector V) {
	Vector P = new Vector();
	Vector nV;
	Object E;
	
	for (int i=0, ie=V.size(); i<ie; i++) {
		E = V.elementAt(i);
		/* Create copy of existing entries but with a new entry */
		for (int j=0, je=P.size(); j<je; j++) {
			nV = makeCopy((Vector) P.elementAt(j));
			nV.addElement(E);
			P.addElement(nV);
		}
		nV = new Vector();
		nV.addElement(E);
		P.addElement(nV);
	}
	return P;
}
/**
 * Compute all permutations for V, ignoring the last n objects (append to all permutations)
 * Creation date: (07/22/99 14:54:00)
 * @return java.util.Vector A vector of vectors, each is a permutation of V.
 * @param V java.util.Vector A list of objects
 * @param n int
 */
public static Vector permutations(Vector V, int n) {
	Vector P = new Vector();
	if (V.size() <= n) {
		P.addElement(makeCopy(V));
		return P;
	}

	
	Vector sub = subVector(V, 0, V.size()-n);
	P = permutations(sub);
	if (n > 0) P.addElement(new Vector());

	Object E;
	for (int i=V.size()-n, ie=V.size(); i<ie; i++) {
		E = V.elementAt(i);
		for (int j=P.size(); j-->0;) ((Vector) P.elementAt(j)).addElement(E);
	}
	return P;
}
/**
 * Sort a vector according to the comparison operator.
 * Creation date: (07/22/99 13:50:45)
 * @return java.util.Vector
 * @param v java.util.Vector
 * @param compare sun.misc.Compare
 */
public final static Vector sort(final Vector v, final Comparator compare) {
	Object[] a = new Object[v.size()];
	v.copyInto(a);
	Sort.quicksort(a, compare);
	return Arrayx.toVector(a);
}
/**
 * Get the elements from a range of a vector
 * Creation date: (07/19/99 05:00:34)
 * @return java.util.Vector
 * @param V java.util.Vector
 * @param s int Start index (inclusive)
 * @param e int End index (exclusive)
 */
public static Vector subVector(Vector V, int s, int e) {
	Vector sub = new Vector();
	if (s >= 0 && s < V.size() && e >= s && e >=0 && e <= V.size()) {
		for (int i=s; i<e; i++) sub.addElement(V.elementAt(i));
	}
	return sub;
}
/**
 * Convert a vector into an array.
 * Creation date: (07/18/99 11:19:48)
 * @return java.lang.Object[]
 * @param list java.util.Vector
 */
 
public static Object[] toArray(Vector list) {
	Object[] array = new Object[list.size()];
	list.copyInto(array);
	return array;
}
/**
 * Convert a list of Floats into an array of floats
 * Creation date: (07/19/99 06:35:57)
 * @return float[]
 * @param list java.util.Vector
 */
public static float[] toFloatArray(Vector list) {
	float[] a = new float[list.size()];
	for (int i=list.size(); i-->0;) {
		a[i] = ((Float) list.elementAt(i)).floatValue();
	}
	return a;
}
/**
 * Convert a list of Integers into an array of int
 * Creation date: (07/19/99 06:35:57)
 * @return int[]
 * @param list java.util.Vector
 */
public final static int[] toIntArray(Vector list) {
	int[] a = new int[list.size()];
	for (int i=list.size(); i-->0;) {
		a[i] = ((Integer) list.elementAt(i)).intValue();
	}
	return a;
}
/**
 * Convert a vector of strings into a single string.
 * Creation date: (07/22/99 12:04:21)
 * @return java.lang.String
 * @param V java.util.Vector
 */
public static String toString(Vector V) {
	String text = "";
	for (int i=0, ie=V.size(); i<ie; i++) text += (" " + V.elementAt(i));
	return text.trim();
}
/**
 * Convert a vector into an array.
 * Creation date: (07/18/99 11:19:48)
 * @return java.lang.String[]
 * @param list java.util.Vector
 */
 
public static String[] toStringArray(Vector list) {
	String[] array = new String[list.size()];
	list.copyInto(array);
	return array;
}
/**
 * Convert a vector of string arrays into an array of string arrays.
 * Creation date: (09/02/99 02:33:29)
 * @return java.lang.String[][]
 * @param v java.util.Vector
 */
public static String[][] toStringArrayArray(Vector v) {
	String[][] result = new String[v.size()][];
	v.copyInto(result);
	return result;
}
/**
 * Convert an enumeration to a vector
 * Creation date: (11/05/99 03:37:37)
 * @return java.util.Vector
 * @param e java.util.Enumeration
 */
public final static Vector toVector(Enumeration e) {
	Vector v = new Vector(100,100);
	while (e.hasMoreElements()) v.addElement(e.nextElement());
	return v;
}
}
