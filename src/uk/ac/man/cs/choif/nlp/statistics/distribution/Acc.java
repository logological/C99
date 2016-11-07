package uk.ac.man.cs.choif.nlp.statistics.distribution;

import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.extend.io.*;
/**
 * General purpose accumulator for datasets of numbers
 * Creation date: (07/31/99 21:20:16)
 * @author: Freddy Choi
 */
public class Acc {
	private double mean=0;
	private double var=0;
	private long count=0;
	private double min=Double.POSITIVE_INFINITY;
	private double max=Double.NEGATIVE_INFINITY;
/**
 * Accumulator constructor comment.
 */
public Acc() {
	super();
}
/**
 * 
 * Creation date: (08/22/99 16:07:14)
 * @param in uk.ac.man.cs.choif.extend.io.LineInput
 */
public Acc(LineInput in) {
	while (in.hasMoreElements()) {
		acc(Double.valueOf(((String) in.nextElement())).doubleValue());
	}
}
/**
 * 
 * Creation date: (08/15/99 01:07:33)
 * @param val double
 */
public void acc(double val) {
	mean += val;
	var += val * val;
	if (min > val) min = val;
	if (max < val) max = val;
	count ++;
}
/**
 * 
 * Creation date: (08/15/99 01:07:33)
 * @param val float
 */
public void acc(float val) {
	mean += val;
	var += val * val;
	if (min > val) min = val;
	if (max < val) max = val;
	count ++;
}
/**
 * Add dataset to accumulator
 * Creation date: (07/31/99 21:21:55)
 * @param dataset double[]
 */
public void acc(double[] dataset) {
	for (int i=dataset.length; i-->0;) acc(dataset[i]);
}
/**
 * Add dataset to accumulator
 * Creation date: (07/31/99 21:21:55)
 * @param dataset float[]
 */
public void acc(float[] dataset) {
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
 * Take a file of numbers and compute the statistical properties of the dataset.
 * Creation date: (08/22/99 16:09:35)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	Debugx.header("This is JAcc for computing the distribution of a dataset.");

	Debugx.msg("JAcc", "Reading dataset...");
	LineInput in = new LineInput(System.in);
	Acc acc = new Acc(in);

	Debugx.msg("JAcc", "Ready.");
	Debugx.msg("JAcc", "Output format : <mean> <variance> <s.d.> <min> <max>");

	System.out.println(acc.mean() + "\t" + acc.variance() + "\t" + acc.sd() + "\t" + acc.min() + "\t" + acc.max());

	
}
/**
 * Get the maximum value of the dataset.
 * Creation date: (07/31/99 21:36:33)
 * @return double
 */
public double max() {
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
 * Get the minimum value of the dataset.
 * Creation date: (07/31/99 21:36:07)
 * @return double
 */
public double min() {
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
