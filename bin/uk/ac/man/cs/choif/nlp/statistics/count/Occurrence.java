package uk.ac.man.cs.choif.nlp.statistics.count;

import java.util.*;
/**
 * A dictionary for storing the frequency of occurrence
 * Creation date: (07/18/99 07:38:40)
 * @author: Freddy Choi
 */
public class Occurrence extends Hashtable {
	protected long sum = 0; // Sum of foo
/**
 * FreqDictionary constructor comment.
 */
public Occurrence() {
	super();
}
/**
 * FreqDictionary constructor comment.
 * @param initialCapacity int
 */
public Occurrence(int initialCapacity) {
	super(initialCapacity);
}
/**
 * FreqDictionary constructor comment.
 * @param initialCapacity int
 * @param loadFactor float
 */
public Occurrence(int initialCapacity, float loadFactor) {
	super(initialCapacity, loadFactor);
}
/**
 * Add an object to the frequency dictionary.
 * Creation date: (07/18/99 07:39:48)
 * @param T java.lang.Object
 */
public void add(final Object T) {
	Integer frequency = (Integer) get(T);
	if (frequency == null) put(T, new Integer(1));
	else put(T, new Integer(frequency.intValue() + 1));
	sum ++;
}
/**
 * Add object to the frequency dictionary.
 * Creation date: (07/18/99 07:39:48)
 * @param T java.lang.Object
 * @param n int
 */
public void add(final Object T, final int n) {
	Integer frequency = (Integer) get(T);
	if (frequency == null) put(T, new Integer(n));
	else put(T, new Integer(frequency.intValue() + n));
	sum += n;
}
/**
 * Add objects to the frequency dictionary.
 * Creation date: (07/18/99 07:39:48)
 * @param T java.util.Vector
 */
public void add(final java.util.Vector T) {
	Integer frequency;

	for (int i=T.size(); i-->0;) {
		frequency = (Integer) get(T.elementAt(i));
		if (frequency == null) put(T.elementAt(i), new Integer(1));
		else put(T.elementAt(i), new Integer(frequency.intValue() + 1));
	}

	sum += T.size();
		
}
/**
 * Add a range of objects to the frequency dictionary.
 * Creation date: (07/18/99 07:39:48)
 * @param T java.util.Vector
 */
public void add(final java.util.Vector T, final int s, int e) {
	Integer frequency;

	for (int i=e; i-->s;) {
		frequency = (Integer) get(T.elementAt(i));
		if (frequency == null) put(T.elementAt(i), new Integer(1));
		else put(T.elementAt(i), new Integer(frequency.intValue() + 1));
	}

	sum += (e - s);
}
/**
 * Add objects to the frequency dictionary.
 * Creation date: (07/18/99 07:39:48)
 * @param T java.lang.Object[]
 */
public void add(final Object[] T) {
	Integer frequency;

	for (int i=T.length; i-->0;) {
		frequency = (Integer) get(T[i]);
		if (frequency == null) put(T[i], new Integer(1));
		else put(T[i], new Integer(frequency.intValue() + 1));
	}
	sum += T.length;	
}
/**
 * Treat this table as a vector and compute the cosine
 * measure for the similarity between table A and B.
 * Creation date: (07/24/99 20:04:21)
 * @return float
 * @param A uk.ac.man.cs.choif.nlp.statistics.count.Occurrence
 * @param B uk.ac.man.cs.choif.nlp.statistics.count.Occurrence
 */
public static float cosine(Occurrence A, Occurrence B) {
	final Occurrence x = (A.size() < B.size() ? A : B);
	final Occurrence y = (A.size() < B.size() ? B : A);

	Object obj;

	/* Compute the dot product of x and y, while we are
	at it, we will compute the sum of squares of x */
	int xFoo, dot=0, xM=0;
	for (Enumeration xe=x.keys(); xe.hasMoreElements();) {
		obj = xe.nextElement();
		xFoo = x.foo(obj);
		dot += xFoo * y.foo(obj);
		xM += (xFoo * xFoo);
	}

	/* Compute the sum of squares of y */
	int yFoo, yM = 0;
	for (Enumeration ye=y.keys(); ye.hasMoreElements();) {
		yFoo = y.foo(ye.nextElement());
		yM += (yFoo * yFoo);
	}

	/* Compute cosine measure */
	float M = (float) Math.sqrt(xM * yM); // Magnitude of x and y
	
	if (M == 0) return 0;	// In case of wierd cases
	else return dot / M;	// Normal case
}
/**
 * 
 * Creation date: (09/01/99 03:59:05)
 * @return int
 * @param A uk.ac.man.cs.choif.nlp.statistics.count.Occurrence
 * @param B uk.ac.man.cs.choif.nlp.statistics.count.Occurrence
 */
public static int dot(Occurrence A, Occurrence B) {
	final Occurrence x = (A.size() < B.size() ? A : B);
	final Occurrence y = (A.size() < B.size() ? B : A);

	Object obj;

	/* Compute the dot product of x and y */
	int xFoo, dot=0, xM=0;
	for (Enumeration xe=x.keys(); xe.hasMoreElements();) {
		obj = xe.nextElement();
		xFoo = x.foo(obj);
		dot += xFoo * y.foo(obj);
	}

	return dot;
}
/**
 * Get the frequency of occurrence of an object T.
 * Creation date: (07/18/99 07:45:54)
 * @return int
 * @param T java.lang.Object
 */
public int foo(final Object T) {
	Integer frequency = (Integer) get(T);
	if (frequency == null) return 0;
	else return frequency.intValue();
}
/**
 * Get the frequency of cooccurrence for object A and B
 * assuming they come from the same object pool.
 * Creation date: (07/19/99 04:06:58)
 * @return int
 * @param A java.lang.Object
 * @param B java.lang.Object
 */
public int foo(Object A, Object B) {
	/* The case when A == B, foo(A,B) = 0.5n(n+1),
	n being foo(A) or foo(B) */
	if (A.equals(B)) {
		int n = foo(A);
		return (n * (n + 1)) / 2;
	}
	/* Normal case where it is simply foo(A) * foo(B) */
	else return foo(A) * foo(B);
}
/**
 * Merge a cooccurrence table with this table.
 * Creation date: (07/24/99 19:44:35)
 * @param T uk.ac.man.cs.choif.nlp.statistics.count.Occurrence
 */
public void merge(Occurrence T) {
	Object key;
	Integer count;
	for (java.util.Enumeration e=T.keys(); e.hasMoreElements();) {
		key = e.nextElement();
		count = (Integer) get(key);
		if (count == null) put(key, T.get(key));
		else put(key, new Integer(((Integer) T.get(key)).intValue() + count.intValue()));
	}
	sum += T.sum;
}
/**
 * Subtract Object from the frequency dictionary.
 * Creation date: (07/18/99 07:39:48)
 * @param T java.lang.Object
 */
public void subtract(final Object T) {
	Integer frequency = (Integer) get(T);

	if (frequency != null) {
		if (frequency.intValue() == 1) remove(T);
		else put(T, new Integer(frequency.intValue() - 1));
	}

	sum--;
}
/**
 * Subtract object from the frequency dictionary.
 * Creation date: (07/18/99 07:39:48)
 * @param T java.util.Vector
 */
public void subtract(final java.util.Vector T) {
	Integer frequency;

	for (int i=T.size(); i-->0;) {
		frequency = (Integer) get(T.elementAt(i));
		if (frequency != null) {
			if (frequency.intValue() == 1) remove(T.elementAt(i));
			else put(T.elementAt(i), new Integer(frequency.intValue() - 1));
		}
	}

	sum -= T.size();
}
/**
 * Subtract objects from the frequency dictionary.
 * Creation date: (07/18/99 07:39:48)
 * @param T java.util.Vector
 */
public void subtract(final java.util.Vector T, final int s, final int e) {
	Integer frequency;

	for (int i=e; i-->s;) {
		frequency = (Integer) get(T.elementAt(i));
		if (frequency != null) {
			if (frequency.intValue() == 1) remove(T.elementAt(i));
			else put(T.elementAt(i), new Integer(frequency.intValue() - 1));
		}
	}

	sum -= (e - s);
}
/**
 * Subtract objects from the frequency dictionary.
 * Creation date: (07/18/99 07:39:48)
 * @param T java.lang.Object[]
 */
public void subtract(final Object[] T) {
	Integer frequency;

	for (int i=T.length; i-->0;) {
		frequency = (Integer) get(T[i]);
		if (frequency != null) {
			if (frequency.intValue() == 1) remove(T[i]);
			else put(T[i], new Integer(frequency.intValue() - 1));
		}
	}

	sum -= T.length;
}
/**
 * Get the sum of occurrence frequencies.
 * Creation date: (07/20/99 09:11:23)
 * @return long
 */
public long sum() {
	return sum;
}
/**
 * Print dictionary
 * Creation date: (07/18/99 11:03:43)
 * @return java.lang.String
 */
public String toString() {
	Object key;
	String text = "";
	for (java.util.Enumeration e=keys(); e.hasMoreElements();) {
		key = e.nextElement();
		text += (key + "," + get(key) + "\n");
	}
	return "{" + text.trim() + "}";
}
}
