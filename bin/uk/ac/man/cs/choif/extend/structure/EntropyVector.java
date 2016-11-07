package uk.ac.man.cs.choif.extend.structure;

import java.util.*;
/**
 * 
 * Creation date: (02/18/00 14:48:43)
 * @author: Freddy Choi
 */
public final class EntropyVector {
	private Hashtable table = new Hashtable();
	private final static class TableValue {
		public double probability;
		public double entropy;
		public double inverse;
	}
/**
 * Create an entropy vector from a context vector
 * Creation date: (02/18/00 14:49:05)
 * @param v uk.ac.man.cs.choif.extend.structure.ContextVector
 */
public EntropyVector(final ContextVector v) {
	final String[] keys = v.keys(v);
	final int sum = v.sum(v);
	TableValue kv;
	for (int i=keys.length; i-->0;) {
		kv = new TableValue();
		kv.probability = ((double) v.freq(keys[i], v)) / sum;
		kv.entropy = - Math.log(kv.probability);
		kv.inverse = 1.0 / v.freq(keys[i], v);
		table.put(keys[i], kv);
	}
}
/**
 * 
 * Creation date: (02/18/00 14:58:06)
 * @return double
 * @param s java.lang.String
 * @param v uk.ac.man.cs.choif.extend.structure.EntropyVector
 */
public final static double entropy(final String s, final EntropyVector v) {
	TableValue kv = (TableValue) v.table.get(s);
	if (kv != null) return kv.entropy;
	return 0;
}
/**
 * 
 * Creation date: (02/18/00 14:58:06)
 * @return double
 * @param s java.lang.String
 * @param v uk.ac.man.cs.choif.extend.structure.EntropyVector
 */
public final static double inverse (final String s, final EntropyVector v) {
	TableValue kv = (TableValue) v.table.get(s);
	if (kv != null) return kv.inverse;
	return 0;
}
/**
 * 
 * Creation date: (02/18/00 14:58:06)
 * @return double
 * @param s java.lang.String
 * @param v uk.ac.man.cs.choif.extend.structure.EntropyVector
 */
public final static double probability (final String s, final EntropyVector v) {
	TableValue kv = (TableValue) v.table.get(s);
	if (kv != null) return kv.probability;
	return 0;
}
}
