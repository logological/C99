package uk.ac.man.cs.choif.extend.structure;

import java.io.*;
/**
 * A pair of integers.
 * Creation date: (09/01/99 04:17:03)
 * @author: Freddy Choi
 */
public class PairInt implements Externalizable {
	private int first = 0;
	private int second = 0;
/**
 * PairInt constructor comment.
 */
public PairInt() {
	super();
}
/**
 * Define a new pair of integers.
 * Creation date: (08/15/99 07:53:14)
 * @param first int
 * @param second int
 */
public PairInt(int first, int second) {
	this.first = first;
	this.second = second;
}
/**
 * 
 * Creation date: (08/15/99 07:53:50)
 * @return boolean
 * @param obj java.lang.Object
 */
public boolean equals(Object obj) {
	try {
		return (((PairInt) obj).first == first) && (((PairInt) obj).second == second);
	}
	catch (Exception e) {
		return false;
	}
}
/**
 * Get the first element of the pair.
 * Creation date: (08/15/99 07:56:03)
 * @return int
 */
public int getFirst() {
	return first;
}
/**
 * Get the second element of the pair
 * Creation date: (08/15/99 07:56:22)
 * @return int
 */
public int getSecond() {
	return second;
}
/**
 * 
 * Creation date: (08/15/99 08:52:16)
 * @return int
 */
public int hashCode() {
	return first * second;
}
/**
 * 
 * Creation date: (10/26/99 10:28:07)
 * @param in java.io.ObjectInput
 * @exception java.io.IOException The exception description.
 */
public void readExternal(ObjectInput in) throws IOException {
	first = in.readInt();
	second = in.readInt();
}
/**
 * Get string representation of the pair.
 * Creation date: (08/15/99 07:55:15)
 * @return java.lang.String
 */
public String toString() {
	return "(" + first + "," + second + ")";
}
/**
 * 
 * Creation date: (10/26/99 10:27:30)
 * @param out java.io.ObjectOutput
 * @exception java.io.IOException The exception description.
 */
public void writeExternal(ObjectOutput out) throws IOException {
	out.writeInt(first);
	out.writeInt(second);
}
}
