package uk.ac.man.cs.choif.nlp.location;

import uk.ac.man.cs.choif.nlp.statistics.distribution.*;
import uk.ac.man.cs.choif.nlp.entity.*;
import uk.ac.man.cs.choif.nlp.dictionary.*;
import java.util.*;
import uk.ac.man.cs.choif.nlp.doc.*;
import uk.ac.man.cs.choif.extend.*;
/**
 * Construct a location map
 * Creation date: (08/15/99 23:32:59)
 * @author: Freddy Choi
 */
public class EntityMap {
	/** Source document */
	public Document doc;

	/** Codec for concept and indices */
	public IndexCodec codec;

	/** Entity last occurrence map */
	public int[][] map;

	/** Inter-concept distance violation map */
	public int[][] violations;

	/** Concept chains */
	public int[][] chainMap;

	/** Constants for score values */
	public final static int front = 10;
	public final static int during = -3;
	public final static int rear = 8;

	/** Zero sum score board */
	public float[][] scoreBoard;

	/** Total value */
	public float[] sumScore;

	/** Local maximas */
	public float[] localMaxima;
	
/**
 * 
 * Creation date: (08/15/99 23:34:34)
 * @param doc uk.ac.man.cs.choif.nlp.doc.Document
 */
public EntityMap(Document doc) {
	this.doc = doc;
	Debugx.msg("EntityMap", "Constructing entity map...");
	constructMap();
/*	Debugx.msg("EntityMap", "Generating inter-concept distance violation map...");
	computeViolations(); */
	Debugx.msg("EntityMap", "Generating concept chain map...");
	computeChains();
	Debugx.msg("EntityMap", "Computing boundary scores....");
	computeScore();
	Debugx.msg("EntityMap", "Ready.");
}
/**
 * 
 * Creation date: (08/15/99 23:34:34)
 * @param doc uk.ac.man.cs.choif.nlp.doc.Document
 */
public EntityMap(Document doc, boolean adaptive) {
	this.doc = doc;
	Debugx.msg("EntityMap", "Constructing entity map...");
	constructMap();
/*	Debugx.msg("EntityMap", "Generating inter-concept distance violation map...");
	computeViolations(); */
	if (!adaptive) {
		Debugx.msg("EntityMap", "Generating concept chain map (fixed distance)...");
		computeChainsFixed();
	}
	else {
		Debugx.msg("EntityMap", "Generating concept chain map (adaptive distance model)...");
		computeChains();
	}
	Debugx.msg("EntityMap", "Computing boundary scores....");
	computeScore();
	Debugx.msg("EntityMap", "Ready.");
}
/**
 * 
 * Creation date: (08/16/99 04:43:22)
 */
private void computeChains() {
	/* Initialise table */
	chainMap = new int[map.length][map[0].length];

	/* Get concepts */
	Concept[] concept = getConcepts();

	/* For each concept, identify the chains */
	int chainNumber;
	int diff;
	AccInt model;
	double threshold;
	for (int j=concept.length; j-->0;) {
		chainNumber = 0;
		/* Get the distance model */
		model = doc.distanceModel.getModel(concept[j], concept[j]);
		/* Only trace chain if model exists */
		if (model != null) {
			/* Compute distance threshold */
			threshold = model.median() + model.sd();
//			threshold = model.mean() + model.sd();
			/* Trace concept through the entire document */
			for (int i=1, ie=chainMap.length; i<ie; i++) {
				diff = map[i][j] - map[i-1][j];
				if (diff > 0) {
					if (map[i-1][j] == 0) chainNumber = 1;
					else if (diff > threshold) chainNumber++;
					chainMap[i][j] = chainNumber;
				}
			}
		}
	}

	/* Link the chains with the same number together */
	for (int j=concept.length; j-->0;) {
		chainNumber = -1;
		for (int i=chainMap.length; i-->0;) {
			if (chainMap[i][j] != 0) chainNumber = chainMap[i][j];
			else if (chainNumber != -1 && chainMap[map[i][j]][j] == chainNumber) {
				chainMap[i][j] = chainNumber;
			}
		}
	}
}
/**
 * 
 * Creation date: (08/16/99 04:43:22)
 */
private void computeChainsFixed() {
	/* Initialise table */
	chainMap = new int[map.length][map[0].length];

	/* Get concepts */
	Concept[] concept = getConcepts();

	/* For each concept, identify the chains */
	int chainNumber;
	int diff;
	float threshold = 6;
	for (int j=concept.length; j-->0;) {
		chainNumber = 0;
		/* Trace concept through the entire document */
		for (int i=1, ie=chainMap.length; i<ie; i++) {
			diff = map[i][j] - map[i-1][j];
			if (diff > 0) {
				if (map[i-1][j] == 0) chainNumber = 1;
				else if (diff > threshold) chainNumber++;
				chainMap[i][j] = chainNumber;
			}
		}
	}

	/* Link the chains with the same number together */
	for (int j=concept.length; j-->0;) {
		chainNumber = -1;
		for (int i=chainMap.length; i-->0;) {
			if (chainMap[i][j] != 0) chainNumber = chainMap[i][j];
			else if (chainNumber != -1 && chainMap[map[i][j]][j] == chainNumber) {
				chainMap[i][j] = chainNumber;
			}
		}
	}
}
/**
 * Follows McKeown's idea on computing boundary probabilities
 * Creation date: (08/16/99 05:49:29)
 */
private void computeScore() {
	scoreBoard = new float[chainMap.length][chainMap[0].length];
	sumScore = new float[chainMap.length];

	int nullCount;
	float sum;
	for (int j=chainMap[0].length; j-->0;) {
		nullCount = 0;
		sum = 0;
		/* Compute score for chains */
		for (int i=1, ie=chainMap.length; i<ie; i++) {
			if (chainMap[i][j] == 0) nullCount++;
			else {
				/* Front */
				if (chainMap[i][j] > chainMap[i-1][j]) scoreBoard[i][j] += front;
				/* Rear */
				else if (i+1 == ie || chainMap[i][j] > chainMap[i+1][j]) scoreBoard[i][j] += rear;
				/* During */
				else scoreBoard[i][j] += during;
				sum += scoreBoard[i][j];
			}
		}
		/* Zero weight the score */
		if (nullCount > 0) {
			sum /= nullCount;
			for (int i=scoreBoard.length; i-->1;) {
				if (scoreBoard[i][j] == 0) scoreBoard[i][j] = sum;
				sumScore[i] += scoreBoard[i][j];
			}
		}
	}

	localMaxima = Statx.localMaxima(sumScore);
}
/**
 * Identify exceptions to the distance model
 * Creation date: (08/16/99 00:11:49)
 */
private void computeViolations() {
	/* Initialise table */
	violations = new int[map.length][map[0].length];

	/* Get concepts */
	Concept[] concept = getConcepts();

	/* Identify violations from each pair */
	AccInt distribution;
	double threshold;
	for (int i=0, ie=concept.length; i<ie; i++) {
		for (int j=0, je=concept.length; j<je; j++) {
			distribution = doc.distanceModel.getModel(concept[i], concept[j]);
			if (distribution != null) {
				threshold = distribution.mean() + distribution.sd();
				if (i == j) identifyViolations(concept[i], (float) threshold);
				else identifyViolations(concept[i], concept[j], (float) threshold);
			}
		}
	}
}
/**
 * Given a document construct a table of last location for each entity
 * Creation date: (08/15/99 23:35:23)
 */
private void constructMap() {
	/* Get the list of concepts and encode them */
	codec = new IndexCodec();
	for (Enumeration e=doc.concepts.getConcepts(); e.hasMoreElements();) codec.encode(e.nextElement());

	/* Define data structure for map */
	map = new int[doc.sentence.length+1][codec.size()];

	/* Construct map */
	Concept[] sentence;
	for (int i=1, ie=map.length; i<ie; i++) {
		sentence = doc.sentence[i-1].concept;
		/* Concepts that occurred in the current sentence */
		for (int j=0, je=sentence.length; j<je; j++) {
			map[i][codec.encode(sentence[j])] = i;
		}
		/* Concepts that did not occur in the current sentence
		inherits the location value from the previous sentence */
		for (int j=map[i].length; j-->0;) {
			if (map[i][j] == 0) map[i][j]=map[i-1][j];
		}
	}
	
}
/**
 * Get the list of known concepts.
 * Creation date: (08/16/99 04:50:38)
 * @return uk.ac.man.cs.choif.nlp.entity.Concept[]
 */
private Concept[] getConcepts() {
	Concept[] concept = new Concept[codec.size()];
	for (int i=concept.length; i-->0;) concept[i] = (Concept) codec.decode(i);
	return concept;
}
/**
 * 
 * Creation date: (08/16/99 00:18:45)
 * @param c uk.ac.man.cs.choif.nlp.entity.Concept
 * @param threshold float
 */
private void identifyViolations(Concept c, float threshold) {
	final int j = codec.encode(c);

	int loc1, loc2;
	for (int i=violations.length; i-->2;) {
		loc1 = map[i-1][j];
		loc2 = map[i][j];
		if (loc1 != 0 && loc2 != 0 && (loc2 - loc1) > threshold) {
			violations[i][j]++;
		}
	}
}
/**
 * 
 * Creation date: (08/16/99 00:18:45)
 * @param c1 uk.ac.man.cs.choif.nlp.entity.Concept
 * @param c2 uk.ac.man.cs.choif.nlp.entity.Concept
 * @param threshold float
 */
private void identifyViolations(Concept c1, Concept c2, float threshold) {
	final int i1 = codec.encode(c1);
	final int i2 = codec.encode(c2);

	int loc1, loc2;
	for (int i=violations.length; i-->1;) {
		loc1 = map[i][i1];
		loc2 = map[i][i2];
		if (loc1 != 0 && loc2 != 0 && (loc2 - loc1 > threshold)) {
			violations[i][i2]++;
		}
	}
}
/**
 * Print a table
 * Creation date: (08/16/99 06:07:39)
 * @param table float[]
 * @param out java.io.PrintStream
 */
public void printTable(float[] table, java.io.PrintStream out) {
	for (int i=1, ie=table.length; i<ie; i++) {
		out.println(i + "\t" + table[i]);
	}
}
/**
 * Print a table
 * Creation date: (08/15/99 23:50:57)
 * @param table float[][]
 * @param out java.io.PrintStream
 */
public void printTable(float[][] table, java.io.PrintStream out) {
	/* Print header */
	Concept[] concept = new Concept[codec.size()];
	for (int i=concept.length; i-->0;) concept[i] = (Concept) codec.decode(i);
	String[] column = new String[concept.length];
	out.print("\t");
	for (int i=0, ie=column.length; i<ie; i++) {
		column[i] = concept[i].toString();
		column[i] = column[i].substring(3, 5);
		out.print(column[i]+"\t");
	}
	out.println();

	/* Print map */
	for (int i=1, ie=table.length; i<ie; i++) {
		out.print(i + "\t");
		for (int j=0, je=table[i].length; j<je; j++) {
			if (table[i][j] == 0) out.print("\t");
			else out.print(table[i][j] +"\t");
		}
		out.println();
	}
}
/**
 * Print a table
 * Creation date: (08/15/99 23:50:57)
 * @param table int[][]
 * @param out java.io.PrintStream
 */
public void printTable(int[][] table, java.io.PrintStream out) {
	/* Print header */
	Concept[] concept = new Concept[codec.size()];
	for (int i=concept.length; i-->0;) concept[i] = (Concept) codec.decode(i);
	String[] column = new String[concept.length];
	out.print("\t");
	for (int i=0, ie=column.length; i<ie; i++) {
		column[i] = concept[i].toString();
		column[i] = column[i].substring(3, 5);
		out.print(column[i]+"\t");
	}
	out.println();

	/* Print map */
	for (int i=1, ie=table.length; i<ie; i++) {
		out.print(i + "\t");
		for (int j=0, je=table[i].length; j<je; j++) {
			if (table[i][j] == 0) out.print("\t");
			else out.print(table[i][j] +"\t");
		}
		out.println();
	}
}
}
