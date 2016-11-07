package uk.ac.man.cs.choif.nlp.seg.linear;

import uk.ac.man.cs.choif.extend.structure.Set;
import uk.ac.man.cs.choif.nlp.doc.*;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.extend.sort.*;
import uk.ac.man.cs.choif.nlp.seg.linear.similarity.*;
import java.util.*;
/**
 * An implementation of Jeff Reynar's Dot plotting algorithm with my own interpretation.
 * Creation date: (08/20/99 16:45:49)
 * @author: Freddy Choi
 */
public class JDotPlotF {
	/** The document */
	public Document doc;

	/** The topic boundaries */
	public int[] boundaries;
	
	private static class Region {
		protected int start, end, area=1;
		float sum=0;
		protected Region left=null;
		protected Region right=null;

		/**
		 * Construct a new region with boundaries start and end.
		 * @param start int
		 * @param end int
		 * @param M float[][]
		 */
		public Region(final int start, final int end, final float[][] M) {
			this.start = start;
			this.end = end;
			area = (end-start+1)*(end-start+1);
			sum = M[end][start];
		}

		/**
		 * For the region find a boundary that maximises the inside density
		 * of the region. The result is stored in the region itself.
		 * @param M float[][]
		 * @param R Region
		 */
		public static void bestBoundary_max (final float[][] M, Region R) {
			final int start = R.start;
			final int end = R.end;

			if (start < end) {
				/* For each possible boundary, compute the density to
				find the best boundary */
				int B=start;
				float D_max=0, D=0;
				for (int i=end, lower=end-start, upper=1; i-->start; lower--, upper++) {
					/* Compute the inside density */
					D = (M[i][start] + M[end][i+1]) / (float) ((lower*lower)+(upper*upper));
					/* Test if it is the maximum density */
					if (D > D_max) {
						D_max = D;
						B = i;
					}
				}

				/* Construct result */
				R.left = new Region(start, B, M);
				R.right = new Region(B+1, end, M);
			}
		}

		/**
		 * For the region find a boundary that minimises the outside density
		 * of the region. The result is stored in the region itself.
		 * @param M float[][]
		 * @param R Region
		 */
		private static void bestBoundary_min (final float[][] M, Region R) {
			final int start = R.start;
			final int end = R.end;

			if (start < end) {
				/* For each possible boundary, compute the density to
				find the best boundary */
				int B=start;
				float D_min=Float.MAX_VALUE, D=0;
				for (int i=end; i-->start;) {
					/* Compute the outside density */
					D = (R.sum - M[i][start] - M[end][i+1]) / (float) (2 * (i-start+1) * (end-i));
					/* Test if it is the minimum density */
					if (D < D_min) {
						D_min = D;
						B = i;
					}
				}

				/* Construct result */
				R.left = new Region(start, B, M);
				R.right = new Region(B+1, end, M);

			}
		}		
	}
/**
 * JDotPlot constructor comment.
 */
public JDotPlotF() {
	super();
}
/**
 * 
 * Creation date: (08/20/99 16:50:00)
 * @param doc uk.ac.man.cs.choif.nlp.doc.Document
 * @param n int Number of boundaries to produce
 */
public JDotPlotF(Document doc, int n, int type, int s) {
	Debugx.msg("JDotPlot", "Initialising similarity measure...");
	BlockSimilarity S;
	if (type == 5) {
		Debugx.msg("JDotPlot", "Using spread activation...");
		S = new SpreadActivation(doc);
	}
	else {
		Debugx.msg("JDotPlot", "Using stem repetition with stopword removal");
		S = new StemReiterationSW(doc);
	}
	Debugx.msg("JDotPlot", "Computing similarity matrix...");
	float[][] sim = computeSimilarity(doc, S);
	Debugx.msg("JDotPlot", "Applying image enhancement type " + type + "...");

	//GnuPlotx.dataout(Imagex.symmetricToFull(sim), System.out);
	//Imagex.writeBmp(Imagex.symmetricToFull(sim), new java.io.File("/root/before.bmp"));

	if (type == 1) sim = Imagex.smooth(sim);
	else if (type == 2) sim = Imagex.noiseReduction(sim);
	else if (type == 3) sim = Imagex.threshold_by_mean(sim);
	else if (type == 4 || type == 5) Imagex.rank(sim,s);
	else if (type == 6)	{
		float[][] weight = Imagex.copy(sim);
		Imagex.rank(weight, 11);
		sim = Matrixx.weight(sim, weight);
	}

	//Imagex.writeBmp(Imagex.symmetricToFull(sim), new java.io.File("/root/after.bmp"));

	Debugx.msg("JDotPlot", "Computing accumulated sum matrix...");
	float[][] sum = computeSum(sim);
		
	Debugx.msg("JDotPlot", "Finding topic boundaries...");
	Vector B = computeBoundariesMax(sum, n);
	boundaries = Vectorx.toIntArray(Vectorx.sort(B, new IntAsc()));

	/* Clear memory */
	S = null;
	sim = null;
	sum = null;
	B = null;
	
	Debugx.msg("JDotPlot", "Ready.");
}
/**
 * For the similarity matrix M, find the boundaries for n regions that
 * maximizes the inside density. M is obtained with 
 * Functions.computeSum(float[][] S).
 * @return Vector A list of boundary in the order that they were selected
 * @param M float[][]
 * @param n int
 */
private static Vector computeBoundariesMax (final float[][] M, final int n) {
	/* Lets create a list of regions that can be split and initialise
	it with the entire document as the first region. */
	Vector R = new Vector();
	Region r = new Region(0, M.length-1, M);
	Region.bestBoundary_max(M, r);
	R.addElement(r);
	Vector B = new Vector();
	
	float sum_region=r.sum, sum_area = r.area;
	float density_new, density_max;
	int r_index=0;

	for (int i=n; i-->0;) {
		/* Find the best region to split. It is one that
		maximizes the overall density */
		density_max = Float.MIN_VALUE;
		for (int j=R.size(); j-->0;) {
			r = (Region) R.elementAt(j);
			/* Test if region can be split */
			if (r.start < r.end) {
				/* Divide the new sum of region by the new area */
				density_new = (sum_region-r.sum+r.left.sum+r.right.sum) / (float) (sum_area-r.area+r.left.area+r.right.area);
				/* Maximize */			
				if (density_new > density_max) {
					density_max = density_new;
					r_index = j;
				}
			}
		}

		/* Lets split it */
		r = (Region) R.elementAt(r_index);
		Region.bestBoundary_max(M, r.left);
		Region.bestBoundary_max(M, r.right);
		R.setElementAt(r.right, r_index);
		R.insertElementAt(r.left, r_index);
		B.addElement(new Integer(r.right.start));

		/* Update sums */
		sum_region = sum_region - r.sum + r.left.sum + r.right.sum;
		sum_area = sum_area - r.area + r.left.area + r.right.area;
/*		System.out.println("---- " + r);
		System.out.println(r.left);
		System.out.println(r.right);*/
	}

	return B;
}
/**
 * For the similarity matrix M, find the boundaries for n regions that
 * minimizes the outside density. M is obtained with 
 * Functions.computeSum(float[][] S).
 * @return Vector A list of boundary in the order that they were selected
 * @param M float[][]
 * @param n int
 */
private static Vector computeBoundariesMin (final float[][] M, final int n) {
	/* Lets create a list of regions that can be split and initialise
	it with the entire document as the first region. */
	Vector R = new Vector();
	Region r = new Region(0, M.length-1, M);
	Region.bestBoundary_min(M, r);
	R.addElement(r);
	Vector B = new Vector();
	
	float density_min, density_new;
	float sum_region=0, sum_area=0;
	int r_index=0;


	/* NOTE: It is possible that there is a bug in this method.
	The algorithm tends to favour smaller regions. It is as if
	once it has found a good region, it will recusrively split it.
	This only occurs with real data. This algorithms pass the tests
	that are presented in Jeff Reynar's PhD thesis and it works
	fine with artificial data which are trivial to segment. However,
	when it is applied to real data with noise, it fails. There
	are several possible reasons for this. First, it might be a
	genuine	error in the design of the algorithm. Minimizing the
	outside region will naturally favour smaller regions when the
	data is noisy. Second, the implementation is incorrect, this
	is very unlikely. */
	
	
	
	/* Buggy loop. What the hell is going on? The algorithm favours
	the small regions near the ends for splitting. */
	for (int i=n; i-->0;) {

		/* The bug must be in this loop. The computation of r_index
		has gone bloody bonkers somewhere along the line */
		
		/* Find the best region to split. It is one that
		minimizes the overall density */
		density_min = Float.MAX_VALUE;

//		System.out.println("Current density : " + sum_region + " / " + sum_area + " = " + (sum_region/sum_area));
		
		for (int j=R.size(); j-->0;) {
			r = (Region) R.elementAt(j);

			/* Test if region can be split */
			if (r.start < r.end) {
				
				/* Divide the new sum of region by the new area */
				float region = sum_region+r.sum-r.left.sum-r.right.sum;
				float area = sum_area+r.area-r.left.area-r.right.area;
				density_new = region / area;

//				System.out.println("New density " + r.toString() + " : " + region + " / " + area + " = " + (region/area));

				/* Minimize */
				if (density_new < density_min) {
					density_min = density_new;
					r_index = j;
				}
			}

		}


		/* There can't be a bug here, just simple splitting.
		The bug must be in the computation of r_index. */
		/* Lets split it */
		r = (Region) R.elementAt(r_index);
//		System.out.println("Splitting " + r.toString());
		
		Region.bestBoundary_min(M, r.left);
		Region.bestBoundary_min(M, r.right);
		R.setElementAt(r.right, r_index);
		R.insertElementAt(r.left, r_index);
		B.addElement(new Integer(r.right.start));

		/* Update sums */
		sum_region += (r.sum-r.left.sum-r.right.sum);
		sum_area += (r.area-r.left.area-r.right.area);
	}

	return B;
}
/**
 * 
 * Creation date: (08/20/99 17:04:38)
 * @return float[][]
 * @param doc uk.ac.man.cs.choif.nlp.doc.Document
 */
private static float[][] computeSimilarity(Document doc, BlockSimilarity S) {
	/* Create the symmetric matrix for storing the
	similarity values between pairs of features. This
	is symmetric because S(a,b) = S(b,a). It saves
	memory to store it as a symmetric matrix but hell
	of a confusing when it is used for computation. */
	float[][] M = new float[doc.sentence.length][];

	/* Lets compute the similarity values */
	for (int i=doc.sentence.length; i-->0;) {
		/* Create the space to hold the row */
		M[i] = new float[i+1];
		for (int j=i+1; j-->0;) M[i][j] = S.similarity(i,j);
	}

	/* All done */
	return M;
}
/**
 * Compute the sum of values for all square regions of all sizes
 * along the diagonal. This pre-computing stage enables the
 * segmentation algorithm to compute the density of regions with
 * minimal redundant computation. The result is a symmetric matrix
 * with the diagonal running from the top-left down to the 
 * bottom-right corner. To obtain the sum of values in a square
 * region along the diagonal, use the coordinates of its
 * lower-left corner to reference the matrix.
 * @return float[][]
 * @param SIM float[][]
 */
private static float[][] computeSum (final float[][] SIM) {
	/* Define the symmetric matrix for recording
	the sum of values in square regions */
	float[][] M = new float[SIM.length][];

	/* No processing is required for the diagonal.
	So we just copy it. However, we might as well
	make use of the cycles to initialise M, and the
	values one cell off the diagonal */
	for (int i=M.length; i-->0;) {
		/* Initialise row */
		M[i] = new float[i+1];
		/* Copy diagonal */
		M[i][i] = SIM[i][i];
	}

	/* Compute the sum for the first cell
	off the diagonal, this is needed for
	the rest of the algorithm to work */
	int i_m1;
	for (int i=M.length; i-->1;) {
		i_m1 = i - 1;
		M[i][i_m1] = SIM[i][i_m1] * 2 + SIM[i][i] + SIM[i_m1][i_m1];
	}

	/* Now lets do the really funky bit to compute
	the sum of values for all square regions with
	minimal redundancy by working off the diagonal
	and using the accumulated values */
	int k_pn;
	for (int n=2; n < M.length; n++) {
		/* Computation for the n-th diagonal
		off the main diagonal */
		for (int k=M.length-n; k-->0;) {
			k_pn = k+n; // Reduce redundant computation for k+n
			M[k_pn][k] = SIM[k_pn][k] * 2 + M[k_pn][k+1] + M[k_pn-1][k] - M[k_pn-1][k+1];
		}
	}

	/* Oh joy! All done. Now M contains the sum of values
	for all square regions of all sizes along the diagonal.
	use the coordinates of the lower-left corner of the square
	to reference M for the sum */
	return M;
}
/**
 * 
 * Creation date: (08/16/99 06:56:26)
 * @param v int[]
 * @param s uk.ac.man.cs.choif.nlp.doc.Sentence[]
 * @param out java.io.PrintStream
 */
private static void genOutput(int[] v, Sentence[] s, java.io.PrintStream out) {
	out.println("==========");
	String text;
	String[] token;
	for (int i=0, ie=s.length, k=0; i<ie; i++) {
		if (i == v[k]) { 
			out.println("==========");
			if (k < v.length-1) k++;
		}
		text = "";
		token = s[i].token;
		for (int j=0, je=token.length; j<je; j++) text += (token[j] + " ");
		out.println(text.trim());
	}
	out.println("==========");
}
/**
 * 
 * Creation date: (08/20/99 18:14:27)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	Debugx.header("This is JDotPlot (rev. F), my Java algorithmic implementation of Jeff Reynar's dot plotting algorithm for linear segmentation");

	Argx arg = new Argx(args);
	int n = arg.get("-n", 0, "Number of segments");
	int type = arg.get("-a", 0, "Algorithm type");
	int s = arg.get("-s", 11, "Size of ranking mask (must be >= 3 and an odd number");
	arg.displayHelp();
	if (s < 3 || s % 2 != 1) Debugx.handle(new Error("Parameter -s must be >= 3 and an odd number."));
	

	Debugx.msg("JDotPlot", "Loading document...");
	Set options = new Set();
	options.put(Document.STEM);
	//options.put(Document.POS);
	Document D = new Document(System.in, options);
	//Document D = new Document("/root/temp/0.ref", options);

	Debugx.msg("JDotPlot", "Finding " + n + " segments...");
	JDotPlotF dotplot = new JDotPlotF(D, n-1, type, s);

	genOutput(dotplot.boundaries, D.sentence, System.out);
}
}
