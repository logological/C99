package uk.ac.man.cs.choif.extend;

/**
 * Basic matrix functions
 * Creation date: (08/12/99 18:05:09)
 * @author: Freddy Choi
 */
public class Matrixx {
/**
 * Matrix addition, C = A + B
 * Creation date: (10/28/99 12:55:11)
 * @return float[][] C
 * @param A float[][]
 * @param B float[][]
 */
public final static float[][] add(final float[][] A, final float[][] B) throws IllegalArgumentException {
	try {
		float[][] C = new float[A.length][A[0].length];
		for (int i=A.length; i-->0;) {
			for (int j=A[0].length; j-->0;) C[i][j] = A[i][j] + B[i][j];
		}
		return C;
	}
	catch (Exception e) {
		throw new IllegalArgumentException("Matrix A and B have incompatible size for addition");
	}
}
/**
 * Make a copy of the matrix M
 * Creation date: (10/29/99 16:03:56)
 * @return float[][]
 * @param M float[][]
 */
public final static float[][] makeCopy(final float[][] M) {
	try {
		float[][] R = new float[M.length][M[0].length];
		for (int i=M.length; i-->0;) System.arraycopy(M[i], 0, R[i], 0, M[i].length);
		return R;
	}
	catch (Exception e) {
		return null;
	}
}
/**
 * Given a matrix, find the maximum value.
 * Creation date: (08/12/99 18:05:56)
 * @param M float[][]
 */
public final static float maximum(final float[][] M) {
	float value = Float.NEGATIVE_INFINITY;
	for (int i=M.length; i-->0;) {
		for (int j=M[i].length; j-->0;) {
			if (M[i][j] > value) value = M[i][j];
		}
	}
	return value;
}
/**
 * Given a matrix, find the minimum value.
 * Creation date: (08/12/99 18:05:56)
 * @param M float[][]
 */
public final static float minimum(final float[][] M) {
	float value = Float.POSITIVE_INFINITY;
	for (int i=M.length; i-->0;) {
		for (int j=M[i].length; j-->0;) {
			if (M[i][j] < value) value = M[i][j];
		}
	}
	return value;
}
/**
 * Matrix multiplication, C = A * B
 * Creation date: (10/28/99 12:30:53)
 * @return float[][] C
 * @param A float[][]
 * @param B float[][]
 */
public final static float[][] multiply(final float[][] A, final float[][] B) throws IllegalArgumentException {
	try {
		float[][] C = new float[A.length][B[0].length];
		for (int i=A.length; i-->0;) {
			for (int j=B[0].length; j-->0;) {
				C[i][j] = 0;
				for (int k=B.length; k-->0;) C[i][j] += (A[i][k] * B[k][j]);
			}
		}
		return C;
	}
	catch (Exception e) {
		throw new IllegalArgumentException("Matrix A and B have incompatible size for multiplication");
	}
}
/**
 * Given a matrix, normalise its value to range {0,1}
 * Creation date: (08/12/99 18:08:43)
 * @param M float[][]
 */
public final static void normalise(final float[][] M) {
	/* Find the mimimum and maximum values */
	float min = Float.POSITIVE_INFINITY;
	float max = Float.NEGATIVE_INFINITY;
	for (int i=M.length; i-->0;) {
		for (int j=M[i].length; j-->0;) {
			if (M[i][j] < min) min = M[i][j];
			if (M[i][j] > max) max = M[i][j];
		}
	}

	/* Determine modification */
	final float scale = (((max-min)==0) ? 1 : 1 / (max - min));

	/* Normalise M */
	for (int i=M.length; i-->0;) {
		for (int j=M[i].length; j-->0;) {
			M[i][j] = (M[i][j] - min) * scale;
		}
	}
}
/**
 * Print a matrix in a human readable form to the output stream.
 * Creation date: (08/12/99 18:40:13)
 * @param M float[][]
 * @param out java.io.PrintStream
 */
public final static void print(final float[][] M, java.io.PrintStream out) {
	for (int i=0, ie=M.length; i<ie; i++) {
		for (int j=0, je=M[i].length; j<je; j++) {
			out.println((i+1) + "\t" + (j+1) + "\t" + M[i][j]);
		}
	}
}
/**
 * Turn a matrix M into a stochastic matrix (a destructive function).
 * @param M float[][]
 */
public final static void stochastic (float[][] M) {
	float[] sum_of_row = new float[M.length];

	for (int i=M.length; i-->0;) {
		for (int j=M.length; j-->0;) {
			sum_of_row[i] += M[i][j];
		}
	}

	for (int i=M.length; i-->0;) {
		for (int j=M.length; j-->0;) {
			M[i][j] = M[i][j] / sum_of_row[i];
		}
	}
}
/**
 * Sum of all values in matrix M
 * Creation date: (11/05/99 07:41:30)
 * @return float
 * @param M float[][]
 */
public final static float sum(final float[][] M) {
	float sum = 0;
	for (int i=M.length; i-->0;) {
		for (int j=M[i].length; j-->0;) sum += M[i][j];
	}
	return sum;
}
/**
 * Make a matrix M into a string
 * Creation date: (11/05/99 05:14:52)
 * @return java.lang.String
 * @param M float[][]
 */
public final static String toString(final float[][] M) {
	StringBuffer S = new StringBuffer();
	for (int i=0; i<M.length; i++) {
		for (int j=0, je=M[i].length; j<je; j++) {
			S.append(M[i][j]);
			S.append('\t');
		}
		S.append('\n');
	}
	return S.toString();
}
/**
 * Multiply each value in M with the corresponding value in W.
 * Creation date: (11/02/99 20:32:12)
 * @return float[][]
 * @param M float[][]
 * @param W float[][]
 */
public final static float[][] weight(final float[][] M, final float[][] W) {
	try {
		float[][] R = new float[M.length][];
		for (int i=M.length; i-->0;) {
			R[i] = new float[M[i].length];
			for (int j=M[i].length; j-->0;) R[i][j] = M[i][j] * W[i][j];
		}
		return R;
	}
	catch (Throwable e) {
		Debugx.handle(e);
		return null;
	}
}
}
