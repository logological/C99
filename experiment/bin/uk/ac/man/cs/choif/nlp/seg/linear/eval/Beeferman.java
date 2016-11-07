package uk.ac.man.cs.choif.nlp.seg.linear.eval;

import java.io.File;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.extend.io.*;
import uk.ac.man.cs.choif.nlp.doc.basic.*;
/**
 * An implementation of the Beeferman evaluation metric
 * Creation date: (08/19/99 17:14:24)
 * @author: Freddy Choi
 */
public class Beeferman {
	/** The segmentation score */
	public float score=0;
	
	/* Evaluation statistics */
	protected static class evalStats {
		public int okay=0;
		public int miss=0;
		public int falseAlarm=0;
		public int same=0;
		public int diff=0;
	}

	/** Functions of analysing properties of the Beeferman metric */
	private static class MetricAnalysis {
		/**
		 * A simulation to analyse the properties of the Beeferman metric.
		 * Given a document and the reference boundaries, we compute the
		 * score given by the metric when we propose a boundary at each of
		 * the positions. Use resource file #1 to interface output with
		 * GnuPlot.
		 * Creation date: (08/19/99 18:41:00)
		 * @param docLength int
		 * @param refB int[]
		 * @param outPath String
		 */
		public static void simulateOne(int docLength, int[] refB, String outPath) {
			final int K = computeK(refB);

			Debugx.msg("k", K);
			
			final int[] refMap = computeMap(refB);
			float score;
			
			LineOutput out = new LineOutput(new File(outPath+"/ref.data"));
			
			/* Initialise hypothesised boundaries */
			final int[] hypB = new int[3];
			hypB[0] = 0;
			hypB[2] = docLength;

			out.println("# ---------- Reference boundaries");
			for (int i=1; i<docLength; i++) {
				out.println(i + " " + (refMap[i]-refMap[i-1]==0 ? 0 : 1));
			}


			out = new LineOutput(new File(outPath+"/hyp.data"));
			out.println("# ---------- Hypothesised boundaries");
			for (int i=1; i<docLength; i++) {
				hypB[1] = i;
				score = computeScore(computeStats(refMap, computeMap(hypB), K));
				out.println(i + " " + score);
			}

		}

		/**
		 * A simulation to analyse the properties of the Beeferman metric.
		 * Given a document and the reference boundaries, we compute the
		 * score given by the metric when we propose a boundary at each of
		 * the positions. Use resource file #1 to interface output with
		 * GnuPlot.
		 * Creation date: (08/19/99 18:41:00)
		 * @param docLength int
		 * @param refB int[]
		 * @param outPath String
		 */
		public static void simulateTwo(int docLength, int[] refB, String outPath) {
			final int K = computeK(refB);

			Debugx.msg("k", K);
			
			final int[] refMap = computeMap(refB);
			float score;
			
			LineOutput out = new LineOutput(new File(outPath+"/ref.data"));
			
			/* Initialise hypothesised boundaries */
			final int[] hypB = new int[4];
			hypB[0] = 0;
			hypB[2] = 14;
			hypB[3] = docLength;

			out.println("# ---------- Reference boundaries");
			for (int i=1; i<docLength; i++) {
				out.println(i + " " + (refMap[i]-refMap[i-1]==0 ? 0 : 1));
			}


			out = new LineOutput(new File(outPath+"/hyp.data"));
			out.println("# ---------- Hypothesised boundaries");
			for (int i=1; i<docLength; i++) {
				hypB[1] = i;
				score = computeScore(computeStats(refMap, computeMap(hypB), K));
				out.println(i + " " + score);
			}

		}	
	}
public Beeferman() {}
/**
 * Given the reference and hypothesised segmentation
 * compute the score. Accessable by Beeferman.score.
 * Creation date: (08/19/99 18:00:52)
 * @param ref uk.ac.man.cs.choif.nlp.doc.basic.Segmented
 * @param hyp uk.ac.man.cs.choif.nlp.doc.basic.Segmented
 */
public Beeferman(Segmented ref, Segmented hyp) {
	Debugx.msg("Beeferman", "Constructing boundary lists...");
	final int[] refB = Vectorx.toIntArray(ref.topicBoundaries());
	final int[] hypB = Vectorx.toIntArray(hyp.topicBoundaries());
	Beeferman B = new Beeferman(refB, hypB);
	score = B.score;
}
/**
 * Given the reference and hypothesised segmentation
 * compute the score. Accessable by Beeferman.score.
 * Creation date: (08/19/99 18:00:52)
 * @param refB int[] Reference boundaries
 * @param hypB int[] Hypothesised boundaries
 */
public Beeferman(int[] refB, int[] hypB) {
	Debugx.msg("Beeferman", "Computing mean segment length in reference segmentation...");
	final int K = computeK(refB);
	Debugx.msg("Beeferman", "Computing reference segmentation map...");
	final int[] refMap = computeMap(refB);
	Debugx.msg("Beeferman", "Computing hypothesised segmentation map...");
	final int[] hypMap = computeMap(hypB);
	Debugx.msg("Beeferman", "Computing comparative statistics...");
	final Beeferman.evalStats stats = computeStats(refMap, hypMap, K);
	Debugx.msg("Beeferman", "Computing score...");
	this.score = computeScore(stats);
	Debugx.msg("Beeferman", "Ready.");
}
/**
 * Given the reference segmentation, compute K.
 * Creation date: (08/19/99 17:22:37)
 * @return int
 * @param B int[] The boundaries
 */
protected static int computeK(int[] B) {
	/* K is defined as half the mean segment length (in words) */

	/* Handle odd cases */
	if (B.length < 2) return B[B.length-1] / 2;

	/* Compute mean segment length */
	int sum = 0;
	for (int i=B.length; i-->1;) sum += (B[i] - B[i-1]);
	float mean = sum / (float) (B.length - 1);
	
	return Math.round(mean / (float) 2);
}
/**
 * Given a piece of segmented text, generate a map where
 * each array position corresponds to a token and the
 * array cell value records the segment number of the word.
 * Creation date: (08/19/99 17:31:27)
 * @return int[] The map
 * @param B int[] The boundaries
 */
protected static int[] computeMap(int[] B) {
	int[] map = new int[B[B.length-1]];

	int number=1; // Start at one, since we can ignore the first implicit boundary

	for (int i=0, ie=map.length; i<ie; i++) {
		if (i == B[number]) number++;
		map[i]=number;
	}

	return map;
}
/**
 * Given the raw statistics, compute the score.
 * Creation date: (08/19/99 17:50:31)
 * @return float
 * @param stat Beeferman.evalStats
 */
protected static float computeScore(Beeferman.evalStats stat) {
	/* p(different ref segments | ref, k) */
	float p_diff = stat.diff / (float) (stat.diff + stat.same);

	/* p(same ref segments | ref, k) */
	float p_same = stat.same / (float) (stat.diff + stat.same);

	/* p(miss | ref, hyp, different ref segment, k) */
	float p_miss = stat.miss / (float) stat.diff;

	/* p(false alarm | ref, hyp, same ref segment, k) */
	float p_false = stat.falseAlarm / (float) stat.same;

	/* p(error| ref, hyp, k) */
	float p_error = (p_miss * p_diff) + (p_false * p_same);
	
	return p_error;
}
/**
 * Given the reference and the hypothesised segmentation map, compute the
 * evaluation statistics.
 * Creation date: (08/19/99 17:40:13)
 * @returns evalStats
 * @param refMap int[]
 * @param hypMap int[]
 * @param K int
 */
protected static evalStats computeStats(int[] refMap, int[] hypMap, int K) {
	evalStats stat = new Beeferman.evalStats();
	boolean refSame, hypSame;

	for (int i=0, ik=K, ie=refMap.length-K; i<ie; i++, ik++) {
		refSame = ((refMap[ik] - refMap[i]) == 0);
		hypSame = ((hypMap[ik] - hypMap[i]) == 0);

		if (refSame) {
			stat.same++;
			if (hypSame) stat.okay++;
			else stat.miss++;
		}
		else {
			stat.diff++;
			if (hypSame) stat.falseAlarm++;
			else stat.okay++;
		}
	}	
	return stat;
}
/**
 * Takes two arguments, arg1 = reference segmentation file, arg2 = hypothesised segmentation file
 * Creation date: (08/19/99 18:04:11)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	/* Print header */
	Debugx.header("This is JBeeferman, an implementation of D.Beeferman, A.Berger and J.Lafferty's evaluation metric for linear segmentation.");
	
	/* Get parameters */
	Argx arg = new Argx(args);
	String refFile = arg.get("--ref", "Not Specified", "Path and name of reference segmentation file");
	String hypFile = arg.get("--hyp", "Not Specified", "Path and name of hypothesised segmentation file");
	arg.displayHelp("--ref");

	/* Load documents */
	Segmented hyp;
	if (hypFile.equals("Not Specified")) {
		Debugx.msg("Beeferman", "Loading hypothesised segmentation file from stdin...");
		hyp = new SegmentedText(System.in);
	}
	else {
		Debugx.msg("Beeferman", "Loading hypothesised segmentation file <"+hypFile+">...");
		hyp = new SegmentedText(hypFile);
	}
	Debugx.msg("Beeferman", "Loading reference segmentation file <"+refFile+">...");
	Segmented ref = new SegmentedText(refFile);

	/* Lets do it */
	Debugx.msg("Beeferman", "Evaluating...");
	Beeferman eval = new Beeferman(ref, hyp);

	/* Print results */
	System.out.println(eval.score);

	System.exit(0);
}
}
