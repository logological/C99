package uk.ac.man.cs.choif.extend;

import java.io.*;
import uk.ac.man.cs.choif.extend.io.*;
/**
 * Interface to GnuPlot
 * Creation date: (07/20/99 09:21:53)
 * @author: Freddy Choi
 */
public class GnuPlotx {
/**
 * Print data to output stream for use with GNUPlot.
 * Creation date: (07/20/99 09:23:05)
 * @param list double[]
 * @param out java.io.PrintStream
 */
public static void dataout(double[] list, java.io.PrintStream out) {
	for (int i=0; i<list.length; i++) out.println(list[i]);
}
/**
 * Print data to output stream for use with GNUPlot.
 * Creation date: (07/20/99 09:23:05)
 * @param list float[]
 * @param out java.io.PrintStream
 */
public static void dataout(float[] list, java.io.PrintStream out) {
	for (int i=0; i<list.length; i++) out.println(list[i]);
}
/**
 * Print data to output stream for use with GNUPlot.
 * Creation date: (07/20/99 09:23:05)
 * @param list int[]
 * @param out java.io.PrintStream
 */
public static void dataout(int[] list, java.io.PrintStream out) {
	for (int i=0; i<list.length; i++) out.println(list[i]);
}
/**
 * Print data to output stream for use with GNUPlot.
 * Creation date: (07/20/99 09:23:05)
 * @param list boolean[]
 * @param out java.io.PrintStream
 */
public static void dataout(boolean[] list, java.io.PrintStream out) {
	for (int i=0; i<list.length; i++) out.println((list[i] ? 1 : 0));
}
/**
 * Print data to output stream for use with GNUPlot.
 * Creation date: (07/20/99 09:23:05)
 * @param list double[][]
 * @param out java.io.PrintStream
 */
public static void dataout(double[][] table, java.io.PrintStream out) {
	for (int i=0, ie=table.length; i<ie; i++) {
		for (int j=0, je=table.length; j<je; j++) {
			out.print(table[i][j] + " ");
		}
		out.println();
	}
}
/**
 * Print data to output stream for use with GNUPlot.
 * Creation date: (07/20/99 09:23:05)
 * @param list float[][]
 * @param out java.io.PrintStream
 */
public static void dataout(float[][] table, java.io.PrintStream out) {
	for (int i=0, ie=table.length; i<ie; i++) {
		for (int j=0, je=table.length; j<je; j++) {
			out.print(table[i][j] + " ");
		}
		out.println();
	}
}
/**
 * Print data to output stream for use with GNUPlot.
 * Creation date: (07/20/99 09:23:05)
 * @param list int[][]
 * @param out java.io.PrintStream
 */
public static void dataout(int[][] table, java.io.PrintStream out) {
	for (int i=0, ie=table.length; i<ie; i++) {
		for (int j=0, je=table.length; j<je; j++) {
			out.print(table[i][j] + " ");
		}
		out.println();
	}
}
/**
 * Print data to output stream for use with GNUPlot.
 * Creation date: (07/20/99 09:23:05)
 * @param list boolean[][]
 * @param out java.io.PrintStream
 */
public static void dataout(boolean[][] table, java.io.PrintStream out) {
	for (int i=0, ie=table.length; i<ie; i++) {
		for (int j=0, je=table.length; j<je; j++) {
			out.print((table[i][j] ? 1 : 0) + " ");
		}
		out.println();
	}
}
/**
 * Write data list D to file F.
 * Creation date: (11/23/99 11:39:19)
 * @param D float[] Data list (y axis)
 * @param offset int Index offset (for x axis)
 * @param F java.io.File Data file
 */
public final static void write(final float[] D, final int offset, File F) {
	LineOutput out = new LineOutput(F);
	for (int i=0, ie=D.length; i<ie; i++) out.println((i+offset) + " " + D[i]);
	out.close();
}
/**
 * Write data list D to file F.
 * Creation date: (11/23/99 11:39:19)
 * @param D int[] Data list (y axis)
 * @param offset int Index offset (for x axis)
 * @param F java.io.File Data file
 */
public final static void write(final int[] D, final int offset, File F) {
	LineOutput out = new LineOutput(F);
	for (int i=0, ie=D.length; i<ie; i++) out.println((i+offset) + " " + D[i]);
	out.close();
}
}
