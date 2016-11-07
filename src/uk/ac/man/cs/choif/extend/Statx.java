package uk.ac.man.cs.choif.extend;

/**
 * Statistical functions
 * Creation date: (07/19/99 06:01:02)
 * @author: Freddy Choi
 */
public class Statx {
/**
 * Given a list of float numbers, find the maximum value.
 * Creation date: (07/24/99 20:40:02)
 * @return float
 * @param v float[]
 */
public static float argmax(float[] v) {
	float maxVal = Float.NEGATIVE_INFINITY;
	for (int i=v.length; i-->0;) if (v[i] > maxVal) maxVal = v[i];
	return maxVal;
}
/**
 * Given a list of float numbers, find the minimum value.
 * Creation date: (07/24/99 20:40:02)
 * @return float
 * @param v float[]
 */
public static float argmin(float[] v) {
	float minVal = Float.POSITIVE_INFINITY;
	for (int i=v.length; i-->0;) if (v[i] < minVal) minVal = v[i];
	return minVal;
}
/**
 * Given n elements, compute the number of possible configurations
 * of k elements. Unordered, Without replacement. Also, known
 * as the binomial coefficient.
 * @return long
 * @param n int
 * @param k int
 */
public static long comb (final int n, final int k) {
	/* This is a nightmare function for the computer
	if it was to be implemented as described in its
	simple form, i.e. (n k) = n!/(k!(n-k)!). Using
	approach in numerical recipes in C */

	return (long) Math.floor(0.5+Math.exp(Mathx.factln(n)-Mathx.factln(k)-Mathx.factln(n-k)));
}
/**
 * Apply convolution to v with mask m
 * Creation date: (11/21/99 11:04:40)
 * @return float[] Result
 * @param v float[] A list of values
 * @param m float[] Convolution mask
 */
public final static double[] convolve(final double[] v, final double[] m) {
	final int mid = m.length / 2;
	double[] r = new double[v.length];
	double sum;

	/* Apply convolution */
	for (int i=0, ie=v.length; i<ie; i++) {
		sum = 0;
		for (int jv=i-mid, j=0, je=m.length; j<je; j++, jv++) {
			if (jv >= 0 && jv < v.length) {
				sum += m[j];
				r[i] += v[jv] * m[j];
			}
		}
		r[i] /= sum;
	}

	return r;
}
/**
 * Apply convolution to v with mask m
 * Creation date: (11/21/99 11:04:40)
 * @return float[] Result
 * @param v float[] A list of values
 * @param m float[] Convolution mask
 */
public final static float[] convolve(final float[] v, final double[] m) {
	final int mid = m.length / 2;
	float[] r = new float[v.length];
	float sum;

	/* Apply convolution */
	for (int i=0, ie=v.length; i<ie; i++) {
		sum = 0;
		for (int jv=i-mid, j=0, je=m.length; j<je; j++, jv++) {
			if (jv >= 0 && jv < v.length) {
				sum += m[j];
				r[i] += v[jv] * m[j];
			}
		}
		r[i] /= sum;
	}

	return r;
}
/**
 * Apply convolution to v with mask m
 * Creation date: (11/21/99 11:04:40)
 * @return float[] Result
 * @param v float[] A list of values
 * @param m float[] Convolution mask
 */
public final static float[] convolve(final float[] v, final float[] m) {
	final int mid = m.length / 2;
	float[] r = new float[v.length];
	float sum;

	/* Apply convolution */
	for (int i=0, ie=v.length; i<ie; i++) {
		sum = 0;
		for (int jv=i-mid, j=0, je=m.length; j<je; j++, jv++) {
			if (jv >= 0 && jv < v.length) {
				sum += m[j];
				r[i] += v[jv] * m[j];
			}
		}
		r[i] /= sum;
	}

	return r;
}
/**
 * Given the probability of an event, compute the entropy
 * Creation date: (08/20/99 13:48:57)
 * @return double
 * @param P double
 */
public static double entropy(double P) {
	return - Math.log(P);
}
/**
 * Given the probability of an event, compute the entropy
 * Creation date: (08/20/99 13:48:57)
 * @return float
 * @param P float
 */
public static float entropy(float P) {
	return (float) - Math.log(P);
}
/**
 * Given a list of values, find the local maximas.
 * Creation date: (08/16/99 06:11:21)
 * @return float[]
 * @param val float[] Non-zero values are local maximas.
 */
public static float[] localMaxima(float[] val) {
	if (val.length < 2) return Arrayx.makeCopy(val);

	float[] v = new float[val.length];

	/* Handle explicit cases */
	if (val[0] > val[1]) v[0] = val[0];
	else if (val[1] > val[0]) v[1] = val[1];

	/* Work through the rest now */
	for (int i=val.length-1; i-->1;) {
		if (val[i] > val[i-1] && val[i] > val[i+1]) v[i] = val[i];
	}
	return v;
}
/**
 * Given a list of float numbers, find the index to
 * the maximum value. If the list is empty, it will
 * return -1.
 * Creation date: (07/24/99 20:40:02)
 * @return int
 * @param v double[]
 */
public static int maxIndex (double[] v) {
	if (v.length == 0) return -1;
	int index = 0;
	double maxVal = Double.NEGATIVE_INFINITY;
	for (int i=v.length; i-->0;) if (v[i] > maxVal) { maxVal = v[i]; index = i; }
	return index;
}
/**
 * Given a list of float numbers, find the index to
 * the maximum value. If the list is empty, it will
 * return -1.
 * Creation date: (07/24/99 20:40:02)
 * @return int
 * @param v float[]
 */
public static int maxIndex (float[] v) {
	if (v.length == 0) return -1;
	int index = 0;
	float maxVal = Float.NEGATIVE_INFINITY;
	for (int i=v.length; i-->0;) if (v[i] > maxVal) { maxVal = v[i]; index = i; }
	return index;
}
/**
 * Given a dataset v, compute its mean.
 * Creation date: (07/19/99 06:01:36)
 * @return float
 * @param v float[]
 */
public static float mean(float[] v) {
	if (v.length == 0) return 0;
	float sum=0;
	for (int i=v.length; i-->0;) sum += v[i];
	return sum / (float) v.length;
}
/**
 * Compute the mean and s.d. for a dataset and return them
 * as a string.
 * Creation date: (07/31/99 19:30:18)
 * @return java.lang.String
 * @param dataset float[]
 */
public static String measure(float[] dataset) {
	float mean = mean(dataset);
	float sd = sd(variance(dataset, mean));
	float min = argmin(dataset);
	float max = argmax(dataset);
	
	String output = "\n";
	output += ("mean : " + mean + "\n");
	output += ("s.d. : " + sd + "\n");
	output += ("min. : " + min + "\n");
	output += ("max. : " + max + "\n");
		
	return output;
}
/**
 * Compute the mean and s.d. for a dataset and return them
 * as a string.
 * Creation date: (07/31/99 19:30:18)
 * @return java.lang.String
 * @param dataseti int[]
 */
public static String measure(int[] dataseti) {
	return measure(Arrayx.toFloat(dataseti));
}
/**
 * Given a list of float numbers, find the index to
 * the minimum value. If the list is empty, it will
 * return -1.
 * Creation date: (07/24/99 20:40:02)
 * @return int
 * @param v float[]
 */
public static int minIndex (float[] v) {
	if (v.length == 0) return -1;
	int index = 0;
	float minVal = Float.POSITIVE_INFINITY;
	for (int i=v.length; i-->0;) if (v[i] < minVal) { minVal = v[i]; index = i; }
	return index;
}
/**
 * The probability density of x given a normal distribution with mean and variance
 * Creation date: (10/07/99 01:25:29)
 * @return float Probability density
 * @param x float A point in the distribution
 * @param mean float The mean of the distribution
 * @param variance float The variance of the distribution
 */
public static float probNormal(float x, float mean, float variance) {
	if (variance == 0) return 0;
	return (float)  Math.exp(-(((x-mean)*(x-mean)) / (2 * variance))) / (float) (Math.sqrt(variance) * Math.sqrt(2 * Math.PI));
}
/**
 * Given the variance, compute the standard deviation.
 * Creation date: (07/19/99 06:08:29)
 * @return float
 * @param variance float
 */
public static float sd(float variance) {
	return (float) Math.sqrt(variance);
}
/**
 * Given a dataset v and its mean, compute the variance
 * Creation date: (07/19/99 06:03:54)
 * @return float
 * @param v float[]
 * @param mean float
 */
public static float variance(float[] v, float mean) {
	if (v.length == 0) return 0;
	float sum = 0;
	for (int i=v.length; i-->0;) sum += (v[i] * v[i]);
	return (sum / (float) v.length) - (mean * mean);
}
}
