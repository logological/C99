package uk.ac.man.cs.choif.nlp.seg.linear;

import uk.ac.man.cs.choif.extend.sort.*;
import java.util.*;
import uk.ac.man.cs.choif.nlp.doc.*;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.extend.structure.*;
import uk.ac.man.cs.choif.nlp.pos.*;
import uk.ac.man.cs.choif.nlp.statistics.count.*;
import uk.ac.man.cs.choif.nlp.seg.linear.similarity.*;
/**
 * Block-based dot-plotting algorithm for experimenting with similarity measures.
 * Creation date: (09/01/99 02:45:01)
 * @author: Freddy Choi
 */
public class JDotPlotB {
	/** The topic boundaries */
	public int[] boundaries;

	/** The cache */
	private Cache cache;

	/** Data structure for cache */
	private static class CacheData {
		public float dots = 0;
		public int area = 0;

		CacheData (float dots, int area) {
			this.dots = dots;
			this.area = area;
		}
	}
/**
 * Given a document, find N topic segments.
 * Creation date: (09/01/99 02:45:52)
 * @param D uk.ac.man.cs.choif.nlp.doc.Document The document
 * @param N int Number of segments to find
 * @param S uk.ac.man.cs.choif.nlp.similarity.BlockSimilarity Similarity measure
 */
public JDotPlotB(Document D, int N, BlockSimilarity S) {
	/* Initialise cache */
	cache = new Cache(D.sentence.length * 3);
	
	/* B, a vector of indices of the sentences in D.
	B is sorted in ascending order. Initially, B contains
	only the implicit boundary present at the start and
	end of each document. B[0] = 0, B[1] = D.sentence.length */
	Vector B = new Vector();
	B.addElement(new Integer(0));
	B.addElement(new Integer(D.sentence.length));

	/* A, a vector containing potential boundaries. */
	Debugx.msg("JDotPlotB", "Identifying potential boundaries...");
	Vector A = getPotentialBoundaries(D);

	/* Begin segmentation algorithm */
	Debugx.msg("JDotPlotB", "Finding " + N + " segments...");
	Vector[] P;	// Table P containing possible segmentations
	float[] M; 	// List M containing the corresponding density score
	int l;		// The index of the smallest value in M

	for (int i=N; i-->1 && A.size() > 0;) {
		P = getPotentialSegmentation(B, A);

		/* Maximization algorithm */
		M = computeScoreMax(S, P);
		l = Statx.maxIndex(M);
			

		// Update
		//Debugx.msg(A.elementAt(l) + " - " + P[l] + " - " + Arrayx.toString(M));
		B = P[l];
		A.removeElementAt(l);
	}

	Debugx.msg("JDotPlotB", "Ready.");
	boundaries = Vectorx.toIntArray(B);
}
/**
 * 
 * Creation date: (09/01/99 03:19:28)
 * @return float[]
 * @param S uk.ac.man.cs.choif.nlp.similarity.BlockSimilarity
 * @param P java.util.Vector[]
 */
private float[] computeScoreMax(BlockSimilarity S, Vector[] P) {
	float[] M = new float[P.length];
	int dots, area, start, end;
	CacheData data;
	
	for (int i=M.length; i-->0;) {
		dots = 0;
		area = 0;
		for (int j=1, je=P[i].size(); j<je; j++) {
			start = ((Integer) P[i].elementAt(j-1)).intValue();
			end = ((Integer) P[i].elementAt(j)).intValue();

			data = regionData(S, start, end);

			dots += data.dots;
			area += data.area;
		}
		M[i] = dots / (float) area;
	}
	return M;
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
	int boundary=0;
	String sentence;
	for (int i=0, ie=D.sentence.length; i<ie; i++) {
		if (i == B[boundary]) {
			out.println("==========");
			if (boundary < B.length-1) boundary++;
		}
		sentence = "";
		for (int j=0, je=D.sentence[i].token.length; j<je; j++) {
			sentence += (D.sentence[i].token[j] + " ");
		}
		out.println(sentence.trim());
	}
	out.println("==========");
}
/**
 * Given a document, get the indices of the potential boundaries.
 * Creation date: (09/01/99 03:07:04)
 * @return java.util.Vector
 * @param D uk.ac.man.cs.choif.nlp.doc.Document
 */
private static Vector getPotentialBoundaries(Document D) {
	final int L = D.sentence.length;
	Vector V = new Vector(L-1);
	for (int i=1; i<L; i++) V.addElement(new Integer(i));
	return V;
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
 * 
 * Creation date: (08/22/99 07:58:39)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	Debugx.header("This is JDotPlotB, a block-based Java implementation of Jeff Reynar's dot-plotting algorithm for experimenting with different similarity measures.");

	/* Get program parameter */
	Argx arg = new Argx(args);
	int N = arg.get("-n", 0, "Number of segments");
	int A = arg.get("-a", 0, "Similarity measure");
	arg.displayHelp();

	Debugx.msg("JDotPlotB", "Loading document...");
	Set options = new Set();
	options.put(Document.STEM);
	//options.put(Document.POS);
	Document D = new Document(System.in, options);

	Debugx.msg("JDotPlotB", "Initialising similarity measure (type " + A + ")...");
	BlockSimilarity S;
	switch (A) {
		case 0:		S = new StemReiteration(D); break;
		case 1:		S = new StemReiterationSW(D); break;
		case 2:		S = new WordNetRelation(D); break;
		case 3:		S = new ConceptReiteration(D); break;
		case 4:		S = new SpreadActivation(D); break;
		default:	S = new StemReiterationSW(D); break;
	}

	Debugx.msg("JDotPlotB", "Segmenting...");
	JDotPlotB dotplot = new JDotPlotB(D, N, S);

	genOutput(dotplot.boundaries, D, System.out);
}
/**
 * Compute data only when necessary by accessing via cache.
 * Creation date: (09/01/99 05:08:52)
 * @param S uk.ac.man.cs.choif.nlp.similarity.BlockSimilarity
 * @param start int
 * @param end int
 */
private CacheData regionData(BlockSimilarity S, int start, int end) {
	final PairInt pair = new PairInt(start, end);
	
	CacheData data = (CacheData) cache.get(pair);
	if (data == null) {
		float dots = 0;
		int area = 0;
		for (int x=start, xe=end; x<xe; x++) {
			/* The diagonal */
			dots += S.dot(x,x);
			area += S.insideArea(x,x);
			/* Off-diagonal */
			for (int y=x+1; y<xe; y++) {
				dots += (2 * S.dot(x,y));
				area += (2 * S.insideArea(x,y));
			}
		}
		data = new CacheData(dots,area);
		cache.put(pair, data);
	}
	return data;
}
}
