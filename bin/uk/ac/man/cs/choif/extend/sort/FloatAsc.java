package uk.ac.man.cs.choif.extend.sort;

/**
 * Vector of Floats, sort ascending order.
 * Creation date: (08/15/99 07:32:54)
 * @author: Freddy Choi
 */
public class FloatAsc implements Comparator {
/**
 * IntDec constructor comment.
 */
public FloatAsc() {
	super();
}
public int doCompare(Object arg1, Object arg2) {
	final float v1 = ((Float) arg1).floatValue();
	final float v2 = ((Float) arg2).floatValue();

	if (v1 > v2) return greater;
	if (v1 == v2) return equal;
	return less;
}
}
