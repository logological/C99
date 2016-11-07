package uk.ac.man.cs.choif.extend;

import java.util.Vector;
import sun.misc.Sort;
import uk.ac.man.cs.choif.extend.sort.*;
/**
 * 
 * Creation date: (07/19/99 07:19:24)
 * @author: Freddy Choi
 */
public class Arrayx {
/**
 * Given a list of strings, concatenate them.
 * Creation date: (11/30/99 14:44:46)
 * @return java.lang.String Concatenated string list.
 * @param listOfStrings java.lang.String[] A list of strings.
 * @param separator java.lang.String The string that separates the individual items.
 * @param terminator java.lang.String The string to append to the list as a terminator.
 */
public final static String concatenate(String[] listOfStrings, String separator, String terminator) {
	String R = "";
	if (listOfStrings.length > 0) {
		for (int i=0, ie=listOfStrings.length-1; i<ie; i++) {
			R += listOfStrings[i];
			R += separator;
		}
		R += listOfStrings[listOfStrings.length-1];
		R += terminator;
	}
	return R;
}
/**
 * Concatenate a list of arrays
 * Creation date: (11/30/99 14:29:25)
 * @return float[]
 * @param a float[][]
 */
public final static float[] flatten(final float[][] a) {
	/* Compute total length */
	int sum = 0;
	for (int i=a.length; i-->0;) sum += a[i].length;

	/* Concatenate them */
	float[] R = new float[sum];
	for (int i=0, ie=a.length, offset=0; i<ie; offset+=a[i++].length) {
		System.arraycopy(a[i], 0, R, offset, a[i].length);
	}
	return R;
}
/**
 * Concatenate a list of arrays
 * Creation date: (11/30/99 14:29:25)
 * @return String[]
 * @param a String[][]
 */
public final static String[] flatten(final String[][] a) {
	/* Compute total length */
	int sum = 0;
	for (int i=a.length; i-->0;) sum += a[i].length;

	/* Concatenate them */
	String[] R = new String[sum];
	for (int i=0, ie=a.length, offset=0; i<ie; offset+=a[i++].length) {
		System.arraycopy(a[i], 0, R, offset, a[i].length);
	}
	return R;
}
/**
 * Given a list of indices and the corresponding
 * array, grab the indexed values.
 * Creation date: (11/30/99 14:38:28)
 * @return float[]
 * @param index int[]
 * @param array float[]
 */
public final static float[] getIndices(final int[] index, final float[] array) {
	float[] R = new float[index.length];
	for (int i=index.length; i-->0;) R[i] = array[index[i]];
	return R;
}
/**
 * Given a list of indices and the corresponding
 * array, grab the indexed values.
 * Creation date: (11/30/99 14:38:28)
 * @return String[]
 * @param index int[]
 * @param array String[]
 */
public final static String[] getIndices(final int[] index, final String[] array) {
	String[] R = new String[index.length];
	for (int i=index.length; i-->0;) R[i] = array[index[i]];
	return R;
}
/**
 * Initialise array with a particular value.
 * Creation date: (08/16/99 05:05:48)
 * @param array int[]
 * @param value int
 */
public static void initArray(int[] array, int value) {
	for (int i=array.length; i-->0;) array[i] = value;
}
/**
 * Make a copy of an array
 * Creation date: (08/16/99 06:14:28)
 * @return double[]
 * @param list double[]
 */
public static double[] makeCopy(double[] list) {
	double[] out = new double[list.length];
	for (int i=out.length; i-->0;) out[i] = list[i];
	return out;
}
/**
 * Make a copy of an array
 * Creation date: (08/16/99 06:14:28)
 * @return float[]
 * @param list float[]
 */
public final static float[] makeCopy(float[] list) {
	float[] out = new float[list.length];
	System.arraycopy(list, 0, out, 0, list.length);
	return out;
}
/**
 * Make a copy of an array
 * Creation date: (08/16/99 06:14:28)
 * @return int[]
 * @param list int[]
 */
public final static int[] makeCopy(int[] list) {
	int[] out = new int[list.length];
	System.arraycopy(list, 0, out, 0, list.length);
	return out;
}
/**
 * Print the list in vertical form to out.
 * Creation date: (08/16/99 18:55:42)
 * @param list java.lang.Object[]
 * @param out java.io.PrintStream
 */
public static void printVertical(Object[] list, java.io.PrintStream out) {
	for (int i=0, ie=list.length; i<ie; i++) out.println(list[i].toString());
}
/**
 * Print the table in vertical form to out.
 * Creation date: (08/16/99 18:55:42)
 * @param list java.lang.Object[][]
 * @param out java.io.PrintStream
 */
public static void printVertical(Object[][] list, java.io.PrintStream out) {
	for (int i=0, ie=list.length; i<ie; i++) {
		for (int j=0, je=list[i].length; j<je; j++) {
			out.print(list[i][j].toString() + " ");
		}
		out.println();
	}
}
/**
 * Reverse the array
 * Creation date: (01/31/00 15:55:29)
 * @param a java.lang.Object[]
 */
public final static void reverse(Object[] a) {
	Object temp;
	for (int i=0, ie=a.length / 2, j=a.length-1; i<ie; i++, j--) {
		temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
}
/**
 * Make a list of integers
 * Creation date: (11/29/99 16:48:02)
 * @return int[] A list of integers
 * @param start int Start value (inclusive)
 * @param step int Step size
 * @param end int End value (inclusive unless last value from step is greater end)
 */
public final static int[] series(int start, int step, int end) {
	int[] R = new int[((end-start) / step)+1];
	for (int i=0; i<R.length; i++) R[i] = start + (step * i);
	return R;
}
/**
 * Sort the given array according to the given order.
 * Creation date: (01/28/00 14:00:46)
 * @param array java.lang.Object[]
 * @param order uk.ac.man.cs.choif.extend.sort.Comparator
 */
public final static void sort(Object[] array, final Comparator order) {
	Sort.quicksort(array, order);
}
/**
 * Sort a list of integers (ascending order).
 * Creation date: (11/23/99 10:46:57)
 * @return float[]
 * @param list float[]
 */
public final static float[] sortAsc(final float[] list) {
	return Vectorx.toFloatArray(Vectorx.sort(toVector(list), new FloatAsc()));
}
/**
 * Sort a list of integers (ascending order).
 * Creation date: (11/23/99 10:46:57)
 * @return int[]
 * @param list int[]
 */
public final static int[] sortAsc(final int[] list) {
	return Vectorx.toIntArray(Vectorx.sort(toVector(list), new IntAsc()));
}
/**
 * Given an array of floats, makes an array of doubles.
 * Creation date: (09/14/99 23:21:15)
 * @return double[]
 * @param a float[]
 */
public static double[] toDouble(float[] a) {
	double[] d = new double[a.length];
	for (int i=d.length; i-->0;) d[i] = (double) a[i];
	return d;
}
/**
 * Handy tool for converting an array of integers into floats
 * Creation date: (07/31/99 19:34:02)
 * @return float[]
 * @param l int[]
 */
public static float[] toFloat(double[] l) {
	float[] f = new float[l.length];
	for (int i=l.length; i-->0;) f[i] = (float) l[i];
	return f;
}
/**
 * Handy tool for converting an array of integers into floats
 * Creation date: (07/31/99 19:34:02)
 * @return float[]
 * @param l int[]
 */
public static float[] toFloat(int[] l) {
	float[] f = new float[l.length];
	for (int i=l.length; i-->0;) f[i] = (float) l[i];
	return f;
}
/**
 * Convert a list into string form
 * Creation date: (07/19/99 07:19:59)
 * @return java.lang.String
 * @param list int[]
 */
public static String toString(double[] list) {
	String text = "[";
	for (int i=0; i<list.length; i++) text += (" " + list[i]);
	return text += " ]";
}
/**
 * Convert a list into string form
 * Creation date: (07/19/99 07:19:59)
 * @return java.lang.String
 * @param list float[]
 */
public static String toString(float[] list) {
	String text = "[";
	for (int i=0; i<list.length; i++) text += (" " + list[i]);
	return text += " ]";
}
/**
 * Convert a list into string form
 * Creation date: (07/19/99 07:19:59)
 * @return java.lang.String
 * @param list int[]
 */
public static String toString(int[] list) {
	String text = "[";
	for (int i=0; i<list.length; i++) text += (" " + list[i]);
	return text += " ]";
}
/**
 * Convert a list into string form
 * Creation date: (07/19/99 07:19:59)
 * @return java.lang.String
 * @param list long[]
 */
public static String toString(long[] list) {
	String text = "[";
	for (int i=0; i<list.length; i++) text += (" " + list[i]);
	return text += " ]";
}
/**
 * Convert a list into string form
 * Creation date: (07/19/99 07:19:59)
 * @return java.lang.String
 * @param list int[]
 */
public static String toString(Object[] list) {
	String text = "[";
	for (int i=0; i<list.length; i++) text += (" " + list[i].toString());
	return text += " ]";
}
/**
 * Convert a list into string form
 * Creation date: (07/19/99 07:19:59)
 * @return java.lang.String
 * @param list short[]
 */
public static String toString(short[] list) {
	String text = "[";
	for (int i=0; i<list.length; i++) text += (" " + list[i]);
	return text += " ]";
}
/**
 * Convert a list into string form
 * Creation date: (07/19/99 07:19:59)
 * @return java.lang.String
 * @param list int[]
 */
public static String toString(boolean[] list) {
	String text = "[";
	for (int i=0; i<list.length; i++) text += (" " + list[i]);
	return text += " ]";
}
/**
 * Convert an array into a vector.
 * Creation date: (07/18/99 13:32:23)
 * @return java.util.Vector
 * @param list double[]
 */
public static Vector toVector(double[] list) {
	Vector v = new Vector(list.length);
	for (int i=0; i<list.length; i++) v.addElement(new Double(list[i]));
	return v;
}
/**
 * Convert an array into a vector.
 * Creation date: (07/18/99 13:32:23)
 * @return java.util.Vector
 * @param list float[]
 */
public static Vector toVector(float[] list) {
	Vector v = new Vector(list.length);
	for (int i=0; i<list.length; i++) v.addElement(new Float(list[i]));
	return v;
}
/**
 * Convert an array into a vector.
 * Creation date: (07/18/99 13:32:23)
 * @return java.util.Vector
 * @param list int[]
 */
public static Vector toVector(int[] list) {
	Vector v = new Vector(list.length);
	for (int i=0; i<list.length; i++) v.addElement(new Integer(list[i]));
	return v;
}
/**
 * Convert an array into a vector.
 * Creation date: (07/18/99 13:32:23)
 * @return java.util.Vector
 * @param list java.lang.Object[]
 */
public static Vector toVector(Object[] list) {
	Vector v = new Vector(list.length);
	for (int i=0; i<list.length; i++) v.addElement(list[i]);
	return v;
}
/**
 * Convert an array into a vector.
 * Creation date: (07/18/99 13:32:23)
 * @return java.util.Vector
 * @param list boolean[]
 */
public static Vector toVector(boolean[] list) {
	Vector v = new Vector(list.length);
	for (int i=0; i<list.length; i++) v.addElement(new Boolean(list[i]));
	return v;
}
}
