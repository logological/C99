package uk.ac.man.cs.choif.nlp.seg.linear.eval;

import java.io.File;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.extend.io.*;
import uk.ac.man.cs.choif.nlp.doc.basic.*;
/**
 * Analytical versions of the Beeferman metric.
 * 1. Propose none
 * 2. Propose all
 * 3. Random (#boundaries unknown)
 * 4. Random (#boundaries known)
 * Creation date: (09/02/99 18:35:32)
 * @author: Freddy Choi
 */
public class BeefermanSim extends Beeferman {
	private static final int PROPOSE_NONE = 1;
	private static final int PROPOSE_ALL = 2;
	private static final int RANDOM_UNKNOWN = 3;
	private static final int RANDOM_KNOWN = 4;
/**
 * Given a reference segmentation and the algorithm to use,
 * compute the error.
 * Creation date: (09/02/99 18:38:05)
 * @param ref uk.ac.man.cs.choif.nlp.doc.basic.Segmented
 * @param alg int
 */
public BeefermanSim(Segmented ref, int alg) {
	Debugx.msg("BeefermanSim", "Constructing boundary lists...");
	final int[] refB = Vectorx.toIntArray(ref.topicBoundaries());
	BeefermanSim metric = new BeefermanSim(refB, alg);
	score = metric.score;
}
/**
 * 
 * Creation date: (09/02/99 20:36:38)
 * @param refB int[]
 * @param alg int
 */
public BeefermanSim(int[] refB, int alg) {
	Debugx.msg("BeefermanSim", "Computing mean segment length in reference segmentation...");
	final int K = computeK(refB);

	switch (alg) {
		case PROPOSE_NONE: {
			Debugx.msg("BeefermanSim", "Algorithm = Propose none.");
			score = algProposeNone(refB, K);
			break;
		}
		case PROPOSE_ALL: {
			Debugx.msg("BeefermanSim", "Algorithm = Propose all.");
			score = algProposeAll(refB, K);
			break;
		}
		case RANDOM_UNKNOWN: {
			Debugx.msg("BeefermanSim", "Algorithm = Random with #boundaries unknown.");
			score = algRandomUnknown(refB, K);
			break;
		}
		case RANDOM_KNOWN: {
			Debugx.msg("BeefermanSim", "Algorithm = Random with #boundaries known.");
			score = algRandomKnown(refB, K);
			break;
		}
	}

	Debugx.msg("BeefermanSim", "Ready.");
}
/**
 * Algorithm : Propose all
 * Creation date: (09/02/99 18:40:35)
 * @return float
 * @param refB int[]
 * @param K int
 */
private static float algProposeAll(int[] refB, int K) {
	final int[] refMap = computeMap(refB);

	int[] hypMap = new int[refMap.length];
	for (int i=hypMap.length; i-->0;) hypMap[i] = i;
	
	evalStats stats = computeStats(refMap, hypMap, K);
	return computeScore(stats);
}
/**
 * Algorithm : Propose none
 * Creation date: (09/02/99 18:40:35)
 * @return float
 * @param refB int[]
 * @param K int
 */
private static float algProposeNone(int[] refB, int K) {
	final int[] refMap = computeMap(refB);

	/* Hypothesised segmentation has no boundaries except
	for the implicit beginning and end boundaries */
	final int[] hypB = new int[]{0,refB[refB.length-1]};
	final int[] hypMap = computeMap(hypB);

	evalStats stats = computeStats(refMap, hypMap, K);
	return computeScore(stats);
}
/**
 * Algorithm : Random with known # boundaries
 * Verificatoin, see log book entry 3rd Sept, 1999
 * Creation date: (09/02/99 19:16:43)
 * @return float
 * @param refB int[]
 * @param K int
 */
private static float algRandomKnown(int[] refB, int K) {
	/* Accumulate statistics */
	final int[] refMap = computeMap(refB);
	int diff = 0;
	int same = 0;
	boolean refSame;
	for (int i=0, ik=K, ie=refMap.length-K; i<ie; i++, ik++) {
		if ((refMap[ik] - refMap[i]) == 0) same++;
		else diff++;
	}

	/* Compute probabilities */
	final double p_diff = diff / (double) (diff + same); 	// P(diff)
	final double p_same = same / (double) (diff + same); 	// P(same)

	/* Given a document of length n with B boundaries,
	there are P = n-1 potential boundaries, there are
	P over B (pick B items from n-1 items, unordered 
	without replacement) number of possible	segmentations
	in total. For a region of length K to have no boundaries,
	all the boundaries must lie outside	the region.
	There are P-K potential boundaries outside the region.
	Therefore, number of configurations is P-K over B.
	The probability of two sentences which are K sentences
	apart begin in the same segment is therefore
	p(same|hyp,k) = (P-K over B) / (P over B).
	Thus, the probability of these two occuring in different
	segments p(diff|hyp,k) = 1 - p(same|hyp,k) */
	final int P = refMap.length - 1;
	final int B = refB.length - 2; // Subtracting the two implicit boundaries
	
	final double p_same_hyp_k = Statx.comb(P-K, B) / (double) Statx.comb(P, B);
	final double p_diff_hyp_k = 1 - p_same_hyp_k;

	final double p_miss = p_same_hyp_k;
	final double p_false = p_diff_hyp_k;
	
	/* p(error| ref, hyp, k) */
	double p_error = (p_miss * p_diff) + (p_false * p_same);

	return (float) p_error;
}
/**
 * Algorithm : Random with unknown # boundaries
 * Verification, see logbook entry 3rd Sept 1999
 * Creation date: (09/02/99 19:16:43)
 * @return float
 * @param refB int[]
 * @param K int
 */
private static float algRandomUnknown(int[] refB, int K) {
	/* Accumulate statistics */
	final int[] refMap = computeMap(refB);
	int diff = 0;
	int same = 0;
	boolean refSame;
	for (int i=0, ik=K, ie=refMap.length-K; i<ie; i++, ik++) {
		if ((refMap[ik] - refMap[i]) == 0) same++;
		else diff++;
	}

	/* Compute probabilities */
	final double p_diff = diff / (double) (diff + same); 	// P(diff)
	final double p_same = same / (double) (diff + same); 	// P(same)

	/* There are 2^K number of possible segmentations
	between two sentences that are K sentences apart.
	Only 1 of which say the two sentences are in the
	same segment. The rest of the segmentations say
	the two sentences belong to different segments.
	p(miss|ref=diff,hyp,k) = p(same|hyp,k) = 1 / 2^K,
	p(false alarm|ref=same,hyp,k) = p(diff|hyp,k) =
	(2^K - 1) / 2^K  = 1 - p(same|hyp,k) */
	
	final double p_same_hyp_k = 1 / Math.pow(2, K);
	final double p_diff_hyp_k = 1 - p_same_hyp_k;

	final double p_miss = p_same_hyp_k;
	final double p_false = p_diff_hyp_k;
	
	/* p(error| ref, hyp, k) */
	double p_error = (p_miss * p_diff) + (p_false * p_same);

	return (float) p_error;
}
/**
 * Takes two arguments, -r reference segmentation file, -a algorithm type
 * Creation date: (08/19/99 18:04:11)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	/* Print header */
	Debugx.header("This is JBeefermanSim, for measuring the performance of four abstract degenerate segmentation algorithms using D.Beeferman, A.Berger and J.Lafferty's evaluation metric for linear segmentation.");
	
	/* Get parameters */
	Argx arg = new Argx(args);
	String refFile = arg.get("--ref", "Not specified", "Path and name of reference segmentation file");
	int algorithm =  arg.get("-a", PROPOSE_ALL, "Algorithm to use for simulation");
	arg.displayHelp();

	/* Load documents */
	Segmented ref;
	if (refFile.equals("Not specified")) {
		Debugx.msg("BeefermanSim", "Loading reference segmentation from stdin...");
		ref = new SegmentedText(System.in);
	}
	else {
		Debugx.msg("BeefermanSim", "Loading reference segmentation file <"+refFile+">...");
		ref = new SegmentedText(refFile);
	}
	
	/* Lets do it */
	Debugx.msg("BeefermanSim", "Evaluating...");
	BeefermanSim eval = new BeefermanSim(ref, algorithm);

	/* Print results */
	System.out.println(eval.score);

	System.exit(0);
}
}
