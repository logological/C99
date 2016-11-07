package uk.ac.man.cs.choif.nlp.seg.linear.similarity;

import uk.ac.man.cs.choif.nlp.pos.*;
import uk.ac.man.cs.choif.nlp.doc.*;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.nlp.statistics.count.*;
/**
 * Stopword removal and using WordNet to determine whether two words are related or not.
 * Creation date: (08/20/99 17:54:42)
 * @author: Freddy Choi
 */
public class WordNetRelation implements BlockSimilarity {
	private Document doc;
	private Occurrence[] feature;
/**
 * 
 * Creation date: (08/20/99 17:55:44)
 * @param doc uk.ac.man.cs.choif.nlp.doc.Document
 */
public WordNetRelation(Document doc) {
	this.doc = doc;
	Debugx.msg("WordNetRelation", "Extracting features...");
	extractFeatures();
	Debugx.msg("WordNetRelation", "Ready.");
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
 * For each sentence extract the features.
 * Creation date: (08/20/99 18:00:12)
 */
private void extractFeatures() {
	/* Initialise mapper */
	TagsetMapper mapper = new TagsetMapper();
	mapper.declareMap("BE BEDR BEDZ BEG BEM BEN BER BEZ CC CS DO DOD DOG DON DOZ DT EX HV HVD HVG HVN HVZ MD PDT PN POS TO UH WDT WP WP$ WRB XNOT ! \" ' ( ) , - . ... : ; ?", "C");
	
	feature = new Occurrence[doc.sentence.length];
	for (int i=feature.length; i-->0;) {
		feature[i] = new Occurrence();
		for (int j=doc.sentence[i].stem.length; j-->0;) {
			if (!mapper.map(doc.sentence[i].pos[j]).equals("C")) {
				feature[i].add(doc.sentence[i].stem[j]);
			}
		}
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
 * Given the the number of the sentences, i and j,
 * this method use the cosine similarity measure
 * to compute the similarity between two sentences.
 * Creation date: (08/20/99 17:56:21)
 * @return float
 * @param i int
 * @param j int
 */
public float similarity(int i, int j) {
	return Occurrence.cosine(feature[i], feature[j]);
}
}
