package uk.ac.man.cs.choif.extend.sort;

/**
 * Vector of Integers, sort ascending order.
 * Creation date: (08/15/99 07:32:54)
 * @author: Freddy Choi
 */
public class IntAsc implements Comparator {
/**
 * IntDec constructor comment.
 */
public IntAsc() {
	super();
}
public int doCompare(Object arg1, Object arg2) {
	final int v1 = ((Integer) arg1).intValue();
	final int v2 = ((Integer) arg2).intValue();

	if (v1 > v2) return greater;
	if (v1 == v2) return equal;
	return less;
}
}
