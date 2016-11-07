package uk.ac.man.cs.choif.nlp.entity;

import java.util.*;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.nlp.parse.*;
import uk.ac.man.cs.choif.nlp.statistics.count.*;
/**
 * 
 * Creation date: (07/24/99 18:42:02)
 * @author: Freddy Choi
 */
public class ConceptDatabase {
	/** A list of all concept groups */
	private Vector concepts = new Vector();

	/** Given a new concept, we compute the mean
	and the standard deviation of the similarity
	values between the concept and all the concept
	groups. If the maximum similarity value is 
	unusually larger than the mean value (mean + sd),
	then we consider merging it to the most similar
	group. This parameter sets the bare minimum
	mean similarity value that the concept must
	have with all other concepts before it is
	considered for merge. We plan to compute this
	value from the dataset in the future. */
	public final float MIN_SIM = (float) 0.1;
/**
 * ConceptID constructor comment.
 */
public ConceptDatabase() {
	super();
}
/**
 * Add a concept to the database
 * Creation date: (07/24/99 19:39:55)
 * @returns Concept The concept group that the given concept belongs to.
 * @param c uk.ac.man.cs.choif.nlp.entity.Concept
 */
public Concept addConcept(Concept c) {
	Concept g = conceptGroup(c);
	if (g != null) { 
		g.merge(c);
		return g;
	}		
	else {
		concepts.addElement(c);
		return c;
	}
}
/**
 * Add a phrase to the concept database.
 * Creation date: (07/24/99 18:44:40)
 * @returns Concept The concept group that the given phrase belongs to.
 * @param phrase uk.ac.man.cs.choif.nlp.parse.Chunk
 */
public Concept addPhrase(Chunk phrase) {
	Concept c = new Concept();
	c.addPhrase(phrase);
	return addConcept(c);
}
/**
 * Give a concept, determine its concept group.
 * Creation date: (07/24/99 19:47:58)
 * @return uk.ac.man.cs.choif.nlp.entity.Concept
 * @param c uk.ac.man.cs.choif.nlp.entity.Concept
 */
public Concept conceptGroup(Concept c) {
	/* Compute the similarity between the given
	concept and all existing concept groups */
	float[] S = new float[concepts.size()];
	for (int i=S.length; i-->0;) S[i] = sim(c, (Concept) concepts.elementAt(i));

	/* Compute the mean, standard deviation 
	and the maximum similarity value */
	float mean = Statx.mean(S);
	float sd = Statx.sd(Statx.variance(S, mean));
	float max = Statx.argmax(S);

	/* If the maximum value is massively different from the
	general similarity value, we will add the concept to
	the concept group */
//	System.out.println(c + " " + max + " " + mean + " " + sd + " " + concepts);
	if (max >= (mean + sd) && max > MIN_SIM) return (Concept) concepts.elementAt(Statx.maxIndex(S));
	else return null;
}
/**
 * Given a phrase, determine its concept group.
 * Creation date: (07/24/99 18:45:58)
 * @return uk.ac.man.cs.choif.nlp.entity.Concept
 * @param phrase uk.ac.man.cs.choif.nlp.parse.Chunk
 */
public Concept conceptGroup(Chunk phrase) {
	Concept c = new Concept();
	c.addPhrase(phrase);
	return conceptGroup(c);
}
/**
 * Feature #1 : If two concepts have similar head
 * stems, they refer to the same concept. We use
 * the cosine measure to compute the probability.
 * Creation date: (07/24/99 20:00:25)
 * @return float
 * @param A uk.ac.man.cs.choif.nlp.entity.Concept
 * @param B uk.ac.man.cs.choif.nlp.entity.Concept
 */
private float feature1(Concept A, Concept B) {
	return Occurrence.cosine(A.getHeads(), B.getHeads());
}
/**
 * Get all the concept groups
 * Creation date: (08/15/99 09:20:20)
 * @return java.util.Enumeration
 */
public Enumeration getConcepts() {
	return concepts.elements();
}
/**
 * Compute the similarity between two concepts. This is
 * expressed as a probability with range {0,1}. 1 indicates
 * the two concepts are the same, 0 indicates they are
 * totally different.
 * Creation date: (07/24/99 20:01:58)
 * @return float
 * @param A uk.ac.man.cs.choif.nlp.entity.Concept
 * @param B uk.ac.man.cs.choif.nlp.entity.Concept
 */
public float sim(Concept A, Concept B) {
	return feature1(A, B);
}
}
