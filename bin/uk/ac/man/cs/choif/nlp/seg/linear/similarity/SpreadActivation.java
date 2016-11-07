package uk.ac.man.cs.choif.nlp.seg.linear.similarity;

import uk.ac.man.cs.choif.nlp.surface.*;
import uk.ac.man.cs.choif.nlp.statistics.count.Occurrence;
import java.util.*;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.nlp.pos.TagsetMapper;
import uk.ac.man.cs.choif.nlp.dictionary.IndexCodec;
import uk.ac.man.cs.choif.nlp.doc.*;
/**
 * Stem reiteration without stopword removal (Stopword list rather than POS)
 * Creation date: (08/20/99 17:54:42)
 * @author: Freddy Choi
 */
public class SpreadActivation implements BlockSimilarity {
	private IndexCodec codec;
	private int[][] code;
	private float[][] P;
/**
 * 
 * Creation date: (10/28/99 12:50:18)
 * @param doc uk.ac.man.cs.choif.nlp.doc.Document
 */
public SpreadActivation(Document doc) {
	Debugx.msg("SpreadActivation", "Identifying informative stems and making dictionary...");
	makeTable(doc);
	Debugx.msg("SpreadActivation", "Cooccurrence frequency and stochastic matrix...");
	P = cooccurence(code, codec);
	Matrixx.stochastic(P);
	Debugx.msg("SpreadActivation", "5 Step spread activation (" + P.length + "x" + P.length + ")...");
	P = activate(5, P);
	Matrixx.stochastic(P);
}
/**
 * Compute the accumulated result of activating the transition matrix T for n steps
 * @return double[][]
 * @param n int
 * @param T double[][]
 */
private static float[][] activate (final int n, final float[][] T) {
	/* Copy the content of T into A, the accumulator for the
	sum of probabilities for each step of spread activation,
	and M, the accumulator for the probabilities of n steps of
	spread activation */
	//float[][] M = new float[T.length][T.length];
	//float[][] A = new float[T.length][T.length];
	//for (int i=T.length; i-->0;) {
		//for (int j=T.length; j-->0;) {
			//M[i][j]=T[i][j];
			//A[i][j]=T[i][j];
		//}
	//}
	float[][] M = Matrixx.makeCopy(T);
	float[][] A = Matrixx.makeCopy(T);
	
	// Do n steps of spread activation
	for (int step=0; step<n; step++) {
		/* Lets do one step of spread activation.
		M = M * T, A = A + M */
		float[] row=new float[M.length];
		for (int i=M.length; i-->0;) {
			for (int j=M.length; j-->0;) row[j]=M[i][j];
			for (int j=M.length; j-->0;) {
				M[i][j]=0;
				for (int k=M.length; k-->0;) M[i][j]+=(row[k]*T[k][j]);
				A[i][j] += M[i][j];
			}
		}
	}
	
	return A;
}
/**
 * Compute the cooccurrence frequency of each stem pair
 * Creation date: (10/29/99 15:15:27)
 * @return float[][]
 * @param stem java.lang.String[][]
 * @param D uk.ac.man.cs.choif.nlp.dictionary.IndexCodec
 */
private static float[][] cooccurence(final int[][] stem, final IndexCodec D) {
	float[][] R = new float[D.size()][D.size()];

	int[] C;
	for (int i=stem.length; i-->0;) {
		/* Accumulate count */
		for (int j=stem[i].length; j-->0;) {
			for (int k=j; k-->0;) {
				R[stem[i][j]][stem[i][k]]++;
				R[stem[i][k]][stem[i][j]]++;
			}
		}
	}
				
	return R;
}
public float dot(int i, int j) {
	float R = 0;
	for (int x=code[i].length; x-->0;) {
		for (int y=code[j].length; y-->0;) {
			R += P[code[i][x]][code[j][y]];
			R += P[code[j][y]][code[i][x]];
		}
	}
	return R;
}
public int insideArea(int i, int j) {
	return code[i].length * code[j].length;
}
/**
 * Convert a document (list of sentences) into a list of list of informative stems
 * Creation date: (10/29/99 15:01:54)
 * @return java.util.Vector[]
 * @param D uk.ac.man.cs.choif.nlp.doc.Document
 */
private void makeTable(final Document D) {
	/* Make a dictionary of stems */
	Stopword sw = new Stopword();
	Occurrence occ = new Occurrence();
	for (int i=D.sentence.length; i-->0;) {
		for (int j=D.sentence[i].stem.length; j-->0;) {
			if (Punctuation.isWord(D.sentence[i].token[j]) && !sw.isStopword(D.sentence[i].token[j].toLowerCase())) {
				occ.add(D.sentence[i].stem[j]);
			}
		}
	}

	code = new int[D.sentence.length][];
	codec = new IndexCodec();
	Vector TEMP = new Vector(50);

	for (int i=code.length; i-->0;) {
		TEMP.removeAllElements();
		for (int j=D.sentence[i].stem.length; j-->0;) {
			if (occ.foo(D.sentence[i].stem[j]) > 1) {
				TEMP.addElement(new Integer(codec.encode(D.sentence[i].stem[j])));
			}
		}
		code[i] = Vectorx.toIntArray(TEMP);
	}
	
}
/**
 * Average similarity value
 */
public float similarity(int i, int j) {
	float R = 0;
	int sum = 0;
	for (int x=code[i].length; x-->0;) {
		for (int y=code[j].length; y-->0;) {
			R += P[code[i][x]][code[j][y]];
			R += P[code[j][y]][code[i][x]];
			sum += 2;
		}
	}
	if (sum == 0) return 0;
	else return R / sum;
}
}
