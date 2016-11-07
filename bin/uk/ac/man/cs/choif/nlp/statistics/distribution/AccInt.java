package uk.ac.man.cs.choif.nlp.statistics.distribution;

import uk.ac.man.cs.choif.extend.io.LineInput;
import java.util.Vector;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.extend.sort.*;

/**
 * Accumulator for datasets of integers.
 * Creation date: (07/31/99 21:20:16)
 * @author: Freddy Choi
 */
public class AccInt {
	private long mean=0;
	private long var=0;
	private long count=0;
	private int min=Integer.MAX_VALUE;
	private int max=Integer.MIN_VALUE;

	/* For median computation */
	private Vector dataset = new Vector();
	private boolean medianRequired = true;
	private boolean sorted = true;
/**
 * 
 * Creation date: (09/06/99 22:32:04)
 */
public AccInt() {}
/**
 * 
 * Creation date: (09/06/99 22:31:56)
 * @param medianRequired boolean Is median required, else it can save a considerable amount of storage
 */
public AccInt(boolean medianRequired) {
	this.medianRequired = medianRequired;
}
/**
 * 
 * Creation date: (09/12/99 07:12:19)
 * @param medianRequired boolean
 * @param in uk.ac.man.cs.choif.extend.io.LineInput
 */
public AccInt(boolean medianRequired, LineInput in) {
	this.medianRequired = medianRequired;
	while (in.hasMoreElements()) acc(Integer.valueOf((String) in.nextElement()).intValue());
}
/**
 * 
 * Creation date: (08/15/99 01:07:33)
 * @param val int
 */
public void acc(int val) {
	mean += val;
	var += val * val;
	if (min > val) min = val;
	if (max < val) max = val;
	if (medianRequired) {
		dataset.addElement(new Integer(val));
		sorted = false;
	}
	count ++;
}
/**
 * Add dataset to accumulator
 * Creation date: (07/31/99 21:21:55)
 * @param dataset int[]
 */
public void acc(int[] dataset) {
	for (int i=dataset.length; i-->0;) acc(dataset[i]);
}
/**
 * Return the number of samples observed
 * Creation date: (09/12/99 06:50:10)
 * @return long
 */
public long count() {
	return count;
}
/**
 * 
 * Creation date: (09/12/99 04:06:09)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	Debugx.header("This is JAccInt for measuring the statistical properties of an integer dataset. Expects a list of integers as input. Output format : <dataset name> <mean> <median> <s.d.> <variance> <min> <max> <count>");
	Argx arg = new Argx(args);
	String name = arg.get("-n", "No name", "Name of the dataset");
	arg.displayHelp();
	name.replace(' ', '_');
	
	LineInput in = new LineInput(System.in);
	AccInt acc = new AccInt(true, in);

	// Print results
	System.out.println(name + " " + acc.mean() + " " + acc.median() + " " + acc.sd() + " " + acc.variance() + " " + acc.min() + " " + acc.max() + " " + acc.count());
}
/**
 * Get the maximum value of the dataset.
 * Creation date: (07/31/99 21:36:33)
 * @return int
 */
public int max() {
	return max;
}
/**
 * Compute the mean value of the dataset.
 * Creation date: (07/31/99 21:23:12)
 * @return double
 */
public double mean() {
	return mean / (double) count;
}
/**
 * Get the median of the dataset
 * Creation date: (09/06/99 22:28:29)
 * @return int
 */
public int median() {
	if (!sorted) {
		dataset = Vectorx.sort(dataset, new IntAsc());
		sorted = true;
	}
	if (dataset.size() == 0) return 0;
	else return ((Integer) dataset.elementAt(dataset.size() / 2)).intValue();
}
/**
 * Get the minimum value of the dataset.
 * Creation date: (07/31/99 21:36:07)
 * @return int
 */
public int min() {
	return min;
}
/**
 * Compute the standard deviation for the dataset.
 * Creation date: (07/31/99 21:24:34)
 * @return double
 */
public double sd() {
	return Math.sqrt(variance());
}
/**
 * 
 * Creation date: (08/15/99 01:20:02)
 * @return java.lang.String
 */
public String toString() {
	return "m=" + mean() + ",sd=" + sd();
}
/**
 * Compute the variance of the dataset.
 * Creation date: (07/31/99 21:23:42)
 * @return double
 */
public double variance() {
	return Math.abs((var / (double) count) - (mean() * mean()));
}
}
