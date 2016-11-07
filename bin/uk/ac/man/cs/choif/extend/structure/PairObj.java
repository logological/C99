package uk.ac.man.cs.choif.extend.structure;

/**
 * A pair of objects (first, second)
 * Creation date: (08/15/99 07:52:31)
 * @author: Freddy Choi
 */
public class PairObj {
	private Object first = null;
	private Object second = null;
/**
 * Pair constructor comment.
 */
public PairObj() {
	super();
}
/**
 * Define a new pair of objects.
 * Creation date: (08/15/99 07:53:14)
 * @param first java.lang.Object
 * @param second java.lang.Object
 */
public PairObj(Object first, Object second) {
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
		return (((PairObj) obj).first.equals(first)) && (((PairObj) obj).second.equals(second));
	}
	catch (Exception e) {
		return false;
	}
}
/**
 * Get the first element of the pair.
 * Creation date: (08/15/99 07:56:03)
 * @return java.lang.Object
 */
public Object getFirst() {
	return first;
}
/**
 * Get the second element of the pair
 * Creation date: (08/15/99 07:56:22)
 * @return java.lang.Object
 */
public Object getSecond() {
	return second;
}
/**
 * 
 * Creation date: (08/15/99 08:52:16)
 * @return int
 */
public int hashCode() {
	return first.hashCode() + second.hashCode();
}
/**
 * Get string representation of the pair.
 * Creation date: (08/15/99 07:55:15)
 * @return java.lang.String
 */
public String toString() {
	return "(" + first.toString() + "," + second.toString() + ")";
}
}
