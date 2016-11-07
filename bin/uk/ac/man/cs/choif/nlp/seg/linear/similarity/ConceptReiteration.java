package uk.ac.man.cs.choif.nlp.seg.linear.similarity;

import uk.ac.man.cs.choif.nlp.doc.*;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.nlp.statistics.count.*;
/**
 * Using concepts as features.
 * Creation date: (08/23/99 10:35:45)
 * @author: Freddy Choi
 */
public class ConceptReiteration implements BlockSimilarity {
	private Document doc;
	private Occurrence[] feature;
/**
 * ConceptReiteration constructor comment.
 */
public ConceptReiteration(Document doc) {
	this.doc = doc;
	Debugx.msg("ConceptReiteration", "Extracting features...");
}
/**
 * 
 * Creation date: (09/01/99 03:58:12)
 * @return float
 * @param i int
 * @param j int
 */
public float dot(int i, int j) {
	return (float) Occurrence.dot(feature[i], feature[j]);
}
/**
 * 
 * Creation date: (08/23/99 10:38:34)
 */
private void extractFeatures() {
	feature = new Occurrence[doc.sentence.length];
	for (int i=feature.length; i-->0;) {
		feature[i] = new Occurrence();
		feature[i].add(doc.sentence[i].concept);
	}
}
/**
 * 
 * Creation date: (09/01/99 03:00:27)
 * @return int
 * @param i int
 * @param j int
 */
public int insideArea(int i, int j) {
	return (int) (feature[i].sum() * feature[j].sum());
}
/**
 * similarity method comment.
 */
public float similarity(int i, int j) {
	return Occurrence.cosine(feature[i], feature[j]);
}
}
