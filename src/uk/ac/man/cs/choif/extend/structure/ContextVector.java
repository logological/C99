package uk.ac.man.cs.choif.extend.structure;

/**
 * Context vector, maps string to frequency count
 * Creation date: (11/05/99 02:50:59)
 * @author: Freddy Choi
 */
public final class ContextVector implements java.io.Externalizable {
	private java.util.Hashtable table=new java.util.Hashtable();
	private int sum=0;
	
	private static final class Frequency implements java.io.Externalizable {
		public int count=0;
		public Frequency () {}
		public void writeExternal (java.io.ObjectOutput out) throws java.io.IOException { out.writeInt(count); }
		public void readExternal (java.io.ObjectInput in) throws java.io.IOException { count = in.readInt(); }
	}
public ContextVector() {
}
/**
 * Compute the cosine of vector a and b
 * Creation date: (11/05/99 03:20:31)
 * @return float
 * @param a uk.ac.man.cs.choif.extend.structure.ContextVector
 * @param b uk.ac.man.cs.choif.extend.structure.ContextVector
 */
public final static float cos(final ContextVector a, final ContextVector b) {
	/* Use the smaller vector as the primary vector */
	final ContextVector A,B;
	if (a.table.size() < b.table.size()) { A = a; B = b; }
	else { A = b; B = a; }

	/* Compute dot product and sum of squared frequency */
	Frequency fA, fB;
	String key;
	int dot=0, sfA=0, sfB=0;
	for (java.util.Enumeration e=A.table.keys(); e.hasMoreElements();) {
		key = (String) e.nextElement();
		fA = (Frequency) A.table.get(key);
		fB = (Frequency) B.table.get(key);
		sfA += (fA.count * fA.count);
		if (fB != null) dot += (fA.count * fB.count);
	}
	for (java.util.Enumeration e=B.table.elements(); e.hasMoreElements();) {
		fB = (Frequency) e.nextElement();
		sfB += (fB.count * fB.count);
	}

	/* Compute cosine */
	float magnitude = (float) Math.sqrt(sfA * sfB);
	if (magnitude == 0) return 0;
	else return dot / magnitude;
}
/**
 * Compute the dot product of vector a and b
 * Creation date: (11/05/99 03:12:41)
 * @return int
 * @param a uk.ac.man.cs.choif.extend.structure.ContextVector
 * @param b uk.ac.man.cs.choif.extend.structure.ContextVector
 */
public final static int dot(final ContextVector a, final ContextVector b) {
	/* Use the smaller vector as the primary vector */
	final ContextVector A,B;
	if (a.table.size() < b.table.size()) { A = a; B = b; }
	else { A = b; B = a; }

	/* Compute dot product */
	Frequency fA, fB;
	String key;
	int dot = 0;
	for (java.util.Enumeration e=A.table.keys(); e.hasMoreElements();) {
		key = (String) e.nextElement();
		fA = (Frequency) A.table.get(key);
		fB = (Frequency) B.table.get(key);
		if (fB != null) dot += (fA.count * fB.count);
	}

	return dot;
}
/**
 * Get the frequency count of key in context vector
 * Creation date: (11/05/99 03:10:22)
 * @return int
 * @param key java.lang.String
 * @param vector uk.ac.man.cs.choif.extend.structure.ContextVector
 */
public final static int freq(final String key, final ContextVector vector) {
	Frequency F = (Frequency) vector.table.get(key);
	if (F != null) return F.count;
	else return 0;
}
/**
 * Increase the frequency count of key by increment
 * Creation date: (11/05/99 02:57:12)
 * @return int New frequency
 * @param key java.lang.String
 * @param increment int
 * @param vector uk.ac.man.cs.choif.extend.structure.ContextVector
 */
public final static int inc(final String key, final int increment, ContextVector vector) {
	Frequency F = (Frequency) vector.table.get(key);
	if (F == null) {
		F = new Frequency();
		F.count = increment;
		vector.table.put(key, F);
	}
	else F.count += increment;
	vector.sum += increment;
	return F.count;
}
public final void readExternal (java.io.ObjectInput in) throws java.io.IOException, ClassNotFoundException {
	table = (java.util.Hashtable) in.readObject();
	sum = in.readInt();
}
/**
 * Get the sum of frequencies
 * Creation date: (11/05/99 03:05:09)
 * @return int
 * @param vector uk.ac.man.cs.choif.extend.structure.ContextVector
 */
public final static int sum(final ContextVector vector) {
	return vector.sum;
}
public final void writeExternal (java.io.ObjectOutput out) throws java.io.IOException {
	out.writeObject(table);
	out.writeInt(sum); 
}
}
