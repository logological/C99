package uk.ac.man.cs.choif.extend.sort;

/**
 * Vector of Integers, sort ascending order.
 * Creation date: (08/15/99 07:32:54)
 * @author: Freddy Choi
 */
public class StringAsc implements Comparator {
	private final static int greater = 1;
	private final static int equal = 0;
	private final static int less = -1;

/**
 * IntDec constructor comment.
 */
public StringAsc() {
	super();
}
public int doCompare(Object arg1, Object arg2) {
	return ((String) arg1).compareTo((String) arg2);
}
}
