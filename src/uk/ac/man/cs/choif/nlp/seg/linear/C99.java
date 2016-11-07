package uk.ac.man.cs.choif.nlp.seg.linear;

import java.util.Vector;
import uk.ac.man.cs.choif.extend.Argx;
import uk.ac.man.cs.choif.extend.Arrayx;
import uk.ac.man.cs.choif.extend.Debugx;
import uk.ac.man.cs.choif.extend.Stringx;
import uk.ac.man.cs.choif.extend.Vectorx;
import uk.ac.man.cs.choif.extend.io.LineInput;
import uk.ac.man.cs.choif.extend.io.LineOutput;
import uk.ac.man.cs.choif.extend.structure.ContextVector;
import uk.ac.man.cs.choif.nlp.surface.Punctuation;
import uk.ac.man.cs.choif.nlp.surface.Stemmer;
import uk.ac.man.cs.choif.nlp.surface.WordList;

//import uk.ac.man.cs.choif.extend.GnuPlotx;
import uk.ac.man.cs.choif.nlp.statistics.distribution.*;
import uk.ac.man.cs.choif.extend.Statx;
/**
 * C99 algorithm for linear text segmentation
 * Creation date: (11/05/99 03:30:09)
 * @author: Freddy Choi
 */
public class C99 {
	private final static class Region {
		protected int start, end, area = 1;
		float sum = 0;
		protected Region left = null;
		protected Region right = null;

		/**
		 * Construct a new region with boundaries start and end.
		 * @param start int
		 * @param end int
		 * @param M float[][]
		 */
		public Region(final int start, final int end, final float[][] M) {
			this.start = start;
			this.end = end;
			area = (end - start + 1) * (end - start + 1);
			sum = M[end][start];
		}

		/**
		 * For the region find a boundary that maximises the inside density
		 * of the region. The result is stored in the region itself.
		 * @param M float[][]
		 * @param R Region
		 */
		public final static void bestBoundary_max(final float[][] M, Region R) {
			final int start = R.start;
			final int end = R.end;
			if (start < end) {
				/* For each possible boundary, compute the density to
				find the best boundary */
				int B = start;
				float D_max = 0, D = 0;
				for (int i = end, lower = end - start, upper = 1; i-- > start; lower--, upper++) {
					/* Compute the inside density */
					D = (M[i][start] + M[end][i + 1]) / (float) ((lower * lower) + (upper * upper));
					/* Test if it is the maximum density */
					if (D > D_max) {
						D_max = D;
						B = i;
					}
				}

				/* Construct result */
				R.left = new Region(start, B, M);
				R.right = new Region(B + 1, end, M);
			}
		}
	}
/**
 * For the similarity matrix M, find the boundaries for n regions that
 * maximizes the inside density. M is obtained with 
 * Functions.computeSum(float[][] S).
 * @return Vector A list of boundary in the order that they were selected
 * @param M float[][]
 * @param n int Number of regions to find, if -1, the algorithm will decide.
 */
private final static int[] boundaries (final float[][] M, final int n) {
	/* A list of regions that can be split. Initialise
	it with the entire document as the first region. */
	Vector R = new Vector();					// List of splitable regions
	Region r = new Region(0, M.length-1, M);	// The entire document as the first region
	Region.bestBoundary_max(M, r);				// Find the best split point
	R.addElement(r);							// Initialise R with the entire document

	/* Split the document right down to elementary units,
	i.e. sentences. The boundary selected at each step of
	this divisive clustering process is placed in a list B.
	Each step increases the inside density, this increase 
	is placed in a list dD. */
	
	int[] B = new int[M.length-1];				// A list of boundary locations
	float[] dD = new float[M.length-1]	;		// Increase in density due to the split

	/* Temporary program variables */
	float sum_region=r.sum;						// Sum of similarity values for segmentation
	float sum_area = r.area;					// Inside area of segmentation
	float D = sum_region / sum_area;			// The current density
	float tempD;								// Density of test segmentation
	float maxD;									// Maximum density
	int index=0;								// Index of region to split

	/* Divisive clustering (user or automatic termination) */
	for (int i=0, ie=(n != -1 ? n-1 : M.length-1); i<ie; i++) {
		/* Find the best region to split. It is
		one that maximizes the overall density */
		maxD = Float.MIN_VALUE;
		for (int j=R.size(); j-->0;) {
			r = (Region) R.elementAt(j);
			/* Test if region can be split */
			if (r.start < r.end) {
				/* Divide the new sum of region by the new area */
				tempD = (sum_region-r.sum+r.left.sum+r.right.sum) / (float) (sum_area-r.area+r.left.area+r.right.area);
				/* Maximize */			
				if (tempD > maxD) { maxD = tempD; index = j; }
			}
		}

		/* Split region at index into two */
		r = (Region) R.elementAt(index);
		Region.bestBoundary_max(M, r.left);		// Find best split point for left region
		Region.bestBoundary_max(M, r.right);	// Find best split point for right region
		R.setElementAt(r.right, index);
		R.insertElementAt(r.left, index);

		/* Store results from a step of divisive clustering */
		B[i] = r.right.start;					// Boundary location
		dD[i] = maxD - D;						// Compute change in density
		D = maxD;								// Update current density

		/* Update sums */
		sum_region = sum_region - r.sum + r.left.sum + r.right.sum;
		sum_area = sum_area - r.area + r.left.area + r.right.area;
	}

	/* Now that we have an ordered list of boundary locations,
	the next problem is to decide the number of segments to
	have. Granularity is task dependent. The following method
	computes # segments by tracking the change in density
	caused by each split. */

	int[] result;								// Output boundaries
	if (n != -1) {
		/* User specified # segments */
		result = new int[n-1];
		System.arraycopy(B, 0, result, 0, n-1);
		return result;
	}
	else {
		/* Determine # segments */
		dD = Statx.convolve(dD, new float[]{1,2,4,8,4,2,1});	// Smooth gradient
		Acc dist = new Acc();									// Capture distribution of gradient
		dist.acc(dD);
		double threshold = dist.mean() + 1.2 * dist.sd();		// Threshold to capture only unusually large gradient
		int number = 0;											// Determine the number of boundaries to have
		for (; number<dD.length && dD[number] > threshold; number++);
		result = new int[number];								// Generate result
		System.arraycopy(B, 0, result, 0, number);
		return result;
	}
}
/**
 * Segment a piece of text using the C99 algorithm
 * Creation date: (11/05/99 06:05:36)
 * @param args java.lang.String[]
 */
public final static void main(String[] args) {
	Debugx.header("This is C99, an algorithm for linear text segmentation.");

	/* Command line arguments processing */
	Argx arg = new Argx(args);
	int n = arg.get("-n", -1, "Number of segments (default automatic = -1)");
	arg.displayHelp();

	/* Load document */
	Debugx.msg("C99", "Loading document...");
	String[] D = LineInput.load(new LineInput(System.in));

	/* Perform segmentation */
	String[][] S = segment(D, n);
	Debugx.msg("C99", "Ready.");

	/* Print output */
	final String sep = "==========";
	LineOutput out = new LineOutput(System.out);
	for (int i=0, ie=S.length; i<ie; i++) {
		out.println(sep);
		for (int j=0, je=S[i].length; j<je; j++) out.println(S[i][j]);
	}
	out.println(sep);
	out.close();
}
/**
 * Given a document as a list of sentences, this function produces
 * a list of stem frequency tables, or context vector
 * Creation date: (11/05/99 03:43:34)
 * @return uk.ac.man.cs.choif.extend.structure.ContextVector[]
 * @param S java.lang.String[]
 */
private final static ContextVector[] normalize(final String[] S) {
	WordList stopword = WordList.stopwordList();
	ContextVector[] V = new ContextVector[S.length];

	Vector tokens;
	String token, stem;
	for (int i=S.length; i-->0;) {
		tokens = Stringx.tokenize(S[i].toLowerCase(), " ");
		V[i] = new ContextVector();
		for (int j=tokens.size(); j-->0;) {
			token = (String) tokens.elementAt(j);
			if (Punctuation.isWord(token) && !stopword.has(token)) {
				stem = Stemmer.stemOf(token);
				ContextVector.inc(stem, 1, V[i]);
			}
		}
	}
		
	return V;
}
/**
 * Apply hard ranking (replace pixel value with the proportion 
 * of neighbouring values its greater than) to matrix using a S x S mask.
 * Creation date: (11/02/99 00:05:01)
 * @param M float[][]
 * @param S int
 */
private final static float[][] rank(final float[][] F, final int S) {
	float[][] M = new float[F.length][F.length];
	
	/* Compute the offset used for mask */
	final int dS = (S % 2 == 1 ? S / 2 : (S-1) / 2);

	/* Work on M, refers to F */
	int K_is, K_ie, K_js, K_je;
	float v, sum;
	for (int M_i=M.length; M_i-->0;) {
		for (int M_j=M_i+1; M_j-->0;) {
			v = F[M_i][M_j]; // Grab pixel value
			M[M_i][M_j] = 0; // Set it to 0

			/* Compute effective mask range */
			K_is = M_i - dS; if (K_is < 0) K_is = 0;
			K_ie = M_i + dS + 1; if (K_ie > F.length) K_ie = F.length;
			K_js = M_j - dS; if (K_js < 0) K_js = 0;
			K_je = M_j + dS + 1; if (K_je > F.length) K_je = F.length;

			/* Compute active mask region area for normalization. Subtract 1
			because we ignore the middle pixel which will always be rank 0. */
			sum = (K_ie - K_is) * (K_je - K_js) - 1;

			/* Perform ranking */
			for (int K_i=K_ie; K_i-- > K_is;) {
				for (int K_j=K_je; K_j-- > K_js;) {
					if (v > F[K_i][K_j]) M[M_i][M_j]++;
				}
			}

			M[M_i][M_j] /= sum;
			M[M_j][M_i] = M[M_i][M_j];
		}
	}

	return M;
}
/**
 * Given a document as a list of elementary text blocks
 * (usually sentences), segment the document into n 
 * coherent topic segments. If n is -1, the algorithm
 * will decide the appropriate number of segments by
 * monitoring the rate of increase in segment density.
 * Creation date: (11/05/99 05:55:46)
 * @return String[][] A list of coherent topic segments
 * @param String[] A list of elementary text blocks (usually sentences). Each block is a string of space separated tokens.
 * @param n int Number of segments to make, if -1 then let the algorithm decide.
 */
public final static String[][] segment(String[] document, int n) {
	Debugx.msg("C99", "Context vectors...");
	ContextVector[] vectors = normalize(document);
	Debugx.msg("C99", "Similarity matrix...");
	float[][] sim = similarity(vectors);
	vectors = null;
	Debugx.msg("C99", "Rank matrix (11x11 rank mask)...");
	float[][] rank = rank(sim, 11);
	sim = null;
	Debugx.msg("C99", "Sum of rank matrix...");
	float[][] sum = sum(rank);
	rank = null;
	Debugx.msg("C99", "Divisive clustering (" + (n==-1 ? "automatic" : "user") + " termination)...");
	int[] B = Arrayx.sortAsc(boundaries(sum, n));
	sum = null;
	Debugx.msg("C99", "Found " + (B.length+1) + " segments...");
	return split(document, B);
}
/**
 * Given a list fo context vector, compute the similarity matrix
 * Creation date: (11/05/99 04:45:51)
 * @return float[][]
 * @param v uk.ac.man.cs.choif.extend.structure.ContextVector[]
 */
private final static float[][] similarity(final ContextVector[] v) {
	float[][] S = new float[v.length][v.length];
	
	for (int i=v.length; i-->0;) {
		for (int j=i+1; j-->0;) {
			S[i][j] = ContextVector.cos(v[i], v[j]);
			S[j][i] = S[i][j];
		}
	}
	return S;
}
/**
 * Given the input text and the topic boundaries,
 * split the text into segment blocks.
 * Creation date: (08/16/99 06:56:26)
 * @return String[][] Topic segments
 * @param T String[] Source text
 * @param B int[] Boundaries
 */
private final static String[][] split (final String[] T, final int[] B) {
	/* Add the implicit boundaries (start and end) to B */
	int[] b = new int[B.length+2];	// A list of boundaries, includes implicit boundaries
	b[0] = 0;
	b[b.length-1] = T.length;
	System.arraycopy(B, 0, b, 1, B.length);

	/* Make the topic segments */
	String[][] seg = new String[b.length-1][];
	for (int i=seg.length; i-->0;) {
		seg[i] = new String[b[i+1] - b[i]];
		System.arraycopy(T, b[i], seg[i], 0, b[i+1] - b[i]);
	}

	return seg;
}
/**
 * Compute the sum of rank matrix
 * Creation date: (11/05/99 04:56:32)
 * @return float[][]
 * @param M float[][]
 */
private final static float[][] sum(final float[][] M) {
	float[][] S = new float[M.length][M.length];

	/* Step 1 */
	for (int i=0, ie=M.length; i<ie; i++) S[i][i] = M[i][i];

	/* Step 2 */
	for (int i=0, ie=M.length-1, ip; i<ie; i++) {
		ip = i+1;
		S[ip][i] = M[ip][i] * 2 + S[i][i] + S[ip][ip];
		S[i][ip] = S[ip][i];
	}

	/* Step 3 */
	for (int j=2, ij, ip; j<M.length; j++) {
		for (int i=0, ie=M.length-j; i<ie; i++) {
			ij = i+j;
			ip = i+1;
			S[ij][i] = M[ij][i] * 2 + S[ij-1][i] + S[ij][ip] - S[ij-1][ip];
			S[i][ij] = S[ij][i];
		}
	}

	return S;
}
}
