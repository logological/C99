package uk.ac.man.cs.choif.nlp.seg.linear;

import uk.ac.man.cs.choif.nlp.surface.*;
import uk.ac.man.cs.choif.extend.structure.Set;
import uk.ac.man.cs.choif.extend.sort.*;
import java.util.*;
import uk.ac.man.cs.choif.nlp.doc.*;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.nlp.pos.*;
import uk.ac.man.cs.choif.nlp.statistics.count.*;
/**
 * An exact implementation of Jeff Reynar's Dotplotting algorithm
 * Creation date: (08/22/99 06:33:13)
 * @author: Freddy Choi
 */
public class JDotPlot {
	/** The topic boundaries */
	public int[] boundaries;

	private static Stopword sw = new Stopword();
	private static class VFreq {
		public Hashtable table = new Hashtable();
		public int non_null = 0;

		/* Construct V_x,y */
		VFreq (Vector T, int x, int y) {
			String token;
			Integer count;
			for (int i=y; i-->x;) {
				token = (String) T.elementAt(i);
				if (token != null) {
					non_null++;
					count = (Integer) table.get(token);
					if (count == null) {
						count = new Integer(1);
						table.put(token, count);
					}
					else table.put(token, new Integer(count.intValue() + 1));
				}
			}
		}
	}

/**
 * Given a document and the number of segments expected, segment the document.
 * Creation date: (08/22/99 06:45:06)
 * @param D uk.ac.man.cs.choif.nlp.doc.Document
 * @param N int Number of segments to find
 * @param minimization boolean Using minimization algorithm?
 * @param thesis boolean Using thesis implementation?
 */
public JDotPlot(Document D, int N, boolean minimization, boolean thesis) {
	/* D, the document to be segmented. Assume D is n words long */

	/* T, a vector containing the normalized word tokens of D */
	Debugx.msg("JDotPlot", "Normalizing text...");
	Vector T = getWordTokens(D);

	/* B, a vector of indices of T corresponding to the location of topic boundaries.
	B is sorted in ascending order. Initially, B contains only the implicit boundary
	present at the start and end of each document. B[0] = 0, B[1] = T.length */
	Vector B = new Vector();
	B.addElement(new Integer(0));
	B.addElement(new Integer(T.size()));

	/* A, a vector containing potential boundaries. */
	Debugx.msg("JDotPlot", "Identifying potential boundaries...");
	Vector A = getPotentialBoundaries(D);

	/* Begin segmentation algorithm */
	Debugx.msg("JDotPlot", "Finding " + N + " segments...");
	Vector[] P; // Table P containing possible segmentations
	float[] M; // List M containing the corresponding density score
	int l; // The index of the smallest value in M

	for (int i=N; i-->1 && A.size() > 0;) {
		P = getPotentialSegmentation(B, A);

		if (thesis) {
			/* Using exact implementation as in Jeff's thesis */
			if (minimization) {
				/* Minimization algorithm */
				M = computeScoreMinThesis(T, P);
				l = Statx.minIndex(M);
			}
			else {
				/* Maximization algorithm */
				M = computeScoreMaxThesis(T, P);
				l = Statx.maxIndex(M);
			}
		}
		else {
			/* Using my interpretation of the algorithm which computes
			global density, rather than sum of strip or local density */
			if (minimization) {
				/* Minimization algorithm */
				M = computeScoreMin(T, P);
				l = Statx.minIndex(M);
			}
			else {
				/* Maximization algorithm */
				M = computeScoreMax(T, P);
				l = Statx.maxIndex(M);
			}
		}
			

		// Update
//		Debugx.msg(A.elementAt(l) + " - " + P[l] + " - " + Arrayx.toString(M));
//		GnuPlotx.dataout(M, System.out);
		B = P[l];
		A.removeElementAt(l);
	}

	Debugx.msg("JDotPlot", "Ready.");
	boundaries = Vectorx.toIntArray(B);
}
/**
 * My implementation of the maximization algorithm which computes the global density.
 * Verified to produce the same result as examples in the thesis. Also verified algorithm
 * to be correct.
 * Creation date: (08/22/99 07:11:03)
 * @return float[]
 * @param P java.util.Vector[]
 */
private static float[] computeScoreMax(Vector T, Vector[] P) {
	float[] M = new float[P.length];
	VFreq block;
	int dots, area;
	
	for (int i=M.length; i-->0;) {
		dots = 0;
		area = 0;
		for (int j=1, je=P[i].size(); j<je; j++) {
			block = new VFreq(T, ((Integer) P[i].elementAt(j-1)).intValue(), ((Integer) P[i].elementAt(j)).intValue());
			dots += dot(block, block);
			area += (block.non_null * block.non_null);
		}
		M[i] = dots / (float) area;
	}
	return M;
}
/**
 * Maximization algorithm from Jeff's thesis. I have now verified this to be incorrect.
 * Creation date: (08/22/99 07:11:03)
 * @return float[]
 * @param P java.util.Vector[]
 */
private static float[] computeScoreMaxThesis(Vector T, Vector[] P) {
	float[] M = new float[P.length];
	VFreq block;
	
	for (int i=M.length; i-->0;) {
		for (int j=1, je=P[i].size(); j<je; j++) {
			block = new VFreq(T, ((Integer) P[i].elementAt(j-1)).intValue(), ((Integer) P[i].elementAt(j)).intValue());
			M[i] += (dot(block, block) / (float) (block.non_null * block.non_null));
		}
	}
	return M;
}
/**
 * My implementation of the minimization algorithm which computes the global density.
 * Verified to produce the same result as examples in the thesis. Also verified algorithm
 * to be correct.
 * Creation date: (08/22/99 07:11:03)
 * @return float[]
 * @param P java.util.Vector[]
 */
private static float[] computeScoreMin(Vector T, Vector[] P) {
	float[] M = new float[P.length];
	int dots, area, n;
	VFreq left, right;
	
	for (int i=M.length; i-->0;) {
		dots = 0;
		area = 0;
		n = ((Integer) P[i].elementAt(P[i].size()-1)).intValue();
		for (int j=1, je=P[i].size()-1; j<je; j++) {
			left = new VFreq(T, ((Integer) P[i].elementAt(j-1)).intValue(), ((Integer) P[i].elementAt(j)).intValue());
			right = new VFreq(T, ((Integer) P[i].elementAt(j)).intValue(), n);
			dots += dot(left, right);
			area += (left.non_null * right.non_null);
		}
		M[i] = dots / (float) area;
	}
	return M;
}
/**
 * This is the exact function described in Jeff's thesis, I
 * suspect there is an error. I have now verified this to be incorrect.
 * Creation date: (08/22/99 07:11:03)
 * @return float[]
 * @param P java.util.Vector[]
 */
private static float[] computeScoreMinThesis(Vector T, Vector[] P) {
	float[] M = new float[P.length];
	VFreq left, right;
	int n;
	
	for (int i=M.length; i-->0;) {
		n = ((Integer) P[i].elementAt(P[i].size()-1)).intValue();
		for (int j=1, je=P[i].size()-1; j<je; j++) {
			left = new VFreq(T, ((Integer) P[i].elementAt(j-1)).intValue(), ((Integer) P[i].elementAt(j)).intValue());
			right = new VFreq(T, ((Integer) P[i].elementAt(j)).intValue(), n);
			M[i] += (dot(left, right) / (float) (left.non_null * right.non_null));
		}
	}
	return M;
}
/**
 * Given two frequency tables, compute the dot product
 * Creation date: (08/22/99 07:14:06)
 * @return int
 */
private static int dot (VFreq arg1, VFreq arg2) {
	String key;
	Integer v1, v2;
	int sum=0;
	for (Enumeration e=arg1.table.keys(); e.hasMoreElements();) {
		key = (String) e.nextElement();
		v1 = (Integer) arg1.table.get(key);
		v2 = (Integer) arg2.table.get(key);
		if (v2 != null) sum += (v1.intValue() * v2.intValue());
	}
		
	return sum;
}
/**
 * Given the boundary indices, the document and the output stream,
 * print the segmented output to out.
 * Creation date: (08/22/99 07:53:01)
 * @param B int[]
 * @param D uk.ac.man.cs.choif.nlp.doc.Document
 * @param out java.io.PrintStream
 */
private static void genOutput(int[] B, Document D, java.io.PrintStream out) {
	int token=0, boundary=0;
	String sentence;
	for (int i=0, ie=D.sentence.length; i<ie; i++) {
		if (token == B[boundary]) {
			out.println("==========");
			if (boundary < B.length-1) boundary++;
		}
		sentence = "";
		for (int j=0, je=D.sentence[i].token.length; j<je; j++, token++) {
			sentence += (D.sentence[i].token[j] + " ");
		}
		out.println(sentence.trim());
	}
	out.println("==========");
}
/**
 * Given a document, get the indices of the potential boundaries.
 * Creation date: (08/22/99 06:44:30)
 * @return java.util.Vector
 * @param D uk.ac.man.cs.choif.nlp.doc.Document
 */
private static Vector getPotentialBoundaries(Document D) {
	Vector A = new Vector(20,10);

	int index = 0;
	for (int i=0, ie=D.sentence.length-1; i<ie; i++) {
		index += D.sentence[i].token.length;
		A.addElement(new Integer(index));
	}
	
	return A;
}
/**
 * Given the current list of boundaries B and the potential boundaries A,
 * generate the table P where P[i] is a vector B with A[i] inserted and then
 * sorted in ascending order.
 * Creation date: (08/22/99 07:02:19)
 * @return java.util.Vector[]
 * @param B java.util.Vector
 * @param A java.util.Vector
 */
private static Vector[] getPotentialSegmentation(Vector B, Vector A) {
	Vector[] P = new Vector[A.size()];
	for (int i=P.length; i-->0;) {
		P[i] = Vectorx.makeCopy(B);
		P[i].addElement(A.elementAt(i));
		P[i] = Vectorx.sort(P[i], new IntAsc());
	}
	return P;
}
/**
 * Given a document with n words, generate a vector containing only the useful
 * word tokens in the document. Uninformative words are represented by null.
 * Creation date: (08/22/99 06:36:06)
 * @return java.util.Vector
 * @param D uk.ac.man.cs.choif.nlp.doc.Document
 */
private static Vector getWordTokens(Document D) {
	Vector T = new Vector(100,50);
	/* Loop through each token in each sentence */
	for (int i=0, ie=D.sentence.length; i<ie; i++) {
		for (int j=0, je=D.sentence[i].token.length; j<je; j++) {
			/* Apply text normalization here, words that shouldn't
			be used are replaced with a null in the vector */
			if (Punctuation.isWord(D.sentence[i].token[j]) && !sw.isStopword(D.sentence[i].token[j].toLowerCase())) {
				T.addElement(D.sentence[i].stem[j]);
			}
			else {
				T.addElement(null);
			}
		}
	}
	return T;
}
/**
 * 
 * Creation date: (08/22/99 07:58:39)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	Debugx.header("This is JDotPlot, an exact (algorithmically) Java implementation of Jeff Reynar's dot-plotting algorithm for linear text segmentation.");

	/* Get program parameter */
	Argx arg = new Argx(args);
	int N = arg.get("-n", 1, "Number of segments");
	boolean minimization = arg.has("--min", false, "Use minimization algorithm for segmentation");
	arg.displayHelp();
	
	Debugx.msg("JDotPlot", "Using " + (minimization ? "minimization" : "maximization") + " algorithm...");
	
	Debugx.msg("JDotPlot", "Loading document...");
	Set options = new Set();
	options.put(Document.STEM);
	//options.put(Document.POS);
	Document D = new Document(System.in, options);

	Debugx.msg("JDotPlot", "Segmenting...");
	JDotPlot dotplot = new JDotPlot(D, N, minimization, false);

	genOutput(dotplot.boundaries, D, System.out);
}
/**
 * 
 * Creation date: (08/23/99 08:31:14)
 * @return boolean[][]
 * @param D uk.ac.man.cs.choif.nlp.doc.Document
 */
private static boolean[][] x_computeSimilarity(Document D) {
	Vector V = getWordTokens(D);
	boolean[][] M = new boolean[V.size()][V.size()];
	String tokenA, tokenB;
	
	for (int i=M.length; i-->0;) {
		tokenA = (String) V.elementAt(i);
		for (int j=M.length; j-->0;) {
			tokenB = (String) V.elementAt(j);
			if (tokenA == null || tokenB == null || !tokenA.equals(tokenB)) M[i][j] = false;
			else M[i][j] = true;
		}
	}
	return M;
}
/**
 * Generate the data for a scatter plot in GnuPlot
 * Creation date: (08/23/99 11:33:28)
 * @param D uk.ac.man.cs.choif.nlp.doc.Document
 */
private static void x_generatePlot(Document D) {
	Vector v = getWordTokens(D);
	String A, B;
	for (int i=0, ie=v.size(); i<ie; i++) {
		A = (String) v.elementAt(i);
		if (A != null) {
			for (int j=0, je=v.size(); j<je; j++) {
				B = (String) v.elementAt(j);
				if (B != null && A.equals(B)) System.out.println(i + "\t" + j);
			}
		}
	}
}
}
