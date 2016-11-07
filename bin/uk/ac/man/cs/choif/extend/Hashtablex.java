package uk.ac.man.cs.choif.extend;

import java.util.*;
import java.io.*;
/**
 * Function extension for hashtable
 * Creation date: (08/15/99 01:22:59)
 * @author: Freddy Choi
 */
public class Hashtablex {
/**
 * Given a hashtable, make the keys into a vector.
 * Creation date: (09/07/99 00:54:56)
 * @return java.util.Vector
 * @param H java.util.Hashtable
 */
public static Vector keysToVector(Hashtable H) {
	Vector v = new Vector(H.size());
	for (Enumeration e=H.keys(); e.hasMoreElements();) v.addElement(e.nextElement());
	return v;
}
/**
 * Print hashtable to out.
 * Creation date: (08/15/99 01:24:39)
 * @param H java.util.Hashtable
 * @param out java.io.PrintStream
 */
public static void print(Hashtable H, PrintStream out) {
	Object obj;
	for (Enumeration e=H.keys(); e.hasMoreElements();) {
		obj = e.nextElement();
		out.println(obj.toString() + "\t" + H.get(obj).toString());
	}
}
}
