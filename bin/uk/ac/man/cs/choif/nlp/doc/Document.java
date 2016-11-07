package uk.ac.man.cs.choif.nlp.doc;

import uk.ac.man.cs.choif.extend.structure.*;
import uk.ac.man.cs.choif.nlp.statistics.distribution.*;
import java.util.*;
import uk.ac.man.cs.choif.nlp.doc.basic.*;
import uk.ac.man.cs.choif.nlp.surface.*;
import uk.ac.man.cs.choif.nlp.parse.*;
import uk.ac.man.cs.choif.nlp.entity.*;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.nlp.location.*;
import uk.ac.man.cs.choif.nlp.pos.*;
/**
 * 
 * Creation date: (08/12/99 23:08:17)
 * @author: Freddy Choi
 */
public class Document {
	/** A list of sentences */
	public Sentence[] sentence = new Sentence[0];

	/** The set of unique entities in the document */
	public ConceptDatabase concepts = new ConceptDatabase();

	/** The distance model for concept pairs */
	public DistanceModel distanceModel = new DistanceModel();

	/** Information available */
	public final static Integer STEM = new Integer(0);
	public final static Integer POS = new Integer(1);
	public final static Integer PARSE = new Integer(2);
	public final static Integer CONCEPT = new Integer(3);
	public final static Integer DISTANCE = new Integer(4);
/**
 * 
 * Creation date: (08/15/99 06:20:27)
 * @param in java.io.InputStream
 * @param options Set
 */
public Document(java.io.InputStream in, Set options) {
	Sentences S = new RawText(in);
	initialise(S, options);
}
/**
 * 
 * Creation date: (08/15/99 06:20:27)
 * @param filename java.lang.String
 * @param options Set
 */
public Document(String filename, Set options) {
	Sentences S = new RawText(filename);
	initialise(S, options);
}
/**
 * 
 * Creation date: (08/15/99 05:39:50)
 * @param T uk.ac.man.cs.choif.nlp.doc.basic.Sentences
 * @param options Set
 */
public Document(Sentences T, Set options) {
	initialise(T, options);
}
/**
 * Apply concept identifier to the text.
 * Creation date: (08/15/99 06:10:48)
 */
private void applyConceptIdentifier() {
	Chunk[] phrase;
	Vector concept;
	for (int i=0, ie=sentence.length; i<ie; i++) {
		phrase = sentence[i].phrase;
		concept = new Vector();
		for (int j=0, je=phrase.length; j<je; j++) {
			concept.addElement(concepts.addPhrase(phrase[j]));
		}
		sentence[i].concept = new Concept[concept.size()];
		concept.copyInto(sentence[i].concept);
	}	
}
/**
 * Apply shallow parser to sentences
 * Creation date: (08/15/99 06:04:28)
 */
private void applyParser() {
	ShallowParser parser = new ShallowParser();
	for (int i=sentence.length; i-->0;) {
		sentence[i].phrase = parser.filterGlue(parser.parse(sentence[i].token, sentence[i].pos));
	}
}
/**
 * Apply stemming algorithm to the entire text.
 * Creation date: (08/15/99 05:55:51)
 */
private void applyStemmer() {
	for (int i=sentence.length; i-->0;) {
		sentence[i].stem = new String[sentence[i].token.length];
		for (int j=sentence[i].token.length; j-->0;) {
			sentence[i].stem[j] = Stemmer.stemOf(sentence[i].token[j]);
		}
	}
}
/**
 * 
 * Creation date: (08/22/99 13:14:19)
 */
private void applyTagger() {
	Tagger tagger = new Tagger();
	for (int i=sentence.length; i-->0;) {
		sentence[i].pos = tagger.tag(sentence[i].token);
	}
}
/**
 * Given a piece of tokenised text with sentence boundaries,
 * reconstruct the structure with sentence objects.
 * Creation date: (08/15/99 05:40:42)
 * @param T uk.ac.man.cs.choif.nlp.doc.basic.Sentences
 */
private void constructSentences(Sentences T) {
	Vector text = T.tokens();
	Vector bounds = T.sentenceBoundaries();
	sentence = new Sentence[bounds.size()-1];

	int start, end;
	for (int i=0, ie=bounds.size()-1; i<ie; i++) {
		start = ((Integer) bounds.elementAt(i)).intValue();
		end = ((Integer) bounds.elementAt(i+1)).intValue()-1;
		sentence[i] = new Sentence();
		sentence[i].token = new String[end - start + 1];
		for (int j=end+1; j-->start;) sentence[i].token[j-start] = (String) text.elementAt(j);
	}
}
/**
 * Generate concept pair distance model.
 * Creation date: (08/15/99 08:10:24)
 */
private void generateDistanceModel() {
	for (int i=0, ie=sentence.length; i<ie; i++) {
		for (int j=0, je=sentence[i].concept.length; j<je; j++) {
			distanceModel.observed(sentence[i].concept[j], i);
		}
	}
}
/**
 * 
 * Creation date: (08/15/99 06:21:17)
 * @param T uk.ac.man.cs.choif.nlp.doc.basic.Sentences
 */
private void initialise(Sentences T, Set options) {
	Debugx.msg("Document", "Constructing sentences...");
	constructSentences(T);
	if (options.member(STEM)) {
		Debugx.msg("Document", "Applying stemming algorithm...");
		applyStemmer();
	}
	if (options.member(POS) || options.member(PARSE) || options.member(CONCEPT) || options.member(DISTANCE)) {
		Debugx.msg("Document", "Applying POS tagger...");
		applyTagger();
	}
	if (options.member(PARSE) || options.member(CONCEPT) || options.member(DISTANCE)) {
		Debugx.msg("Document", "Applying shallow parser...");
		applyParser();
	}
	if (options.member(CONCEPT) || options.member(DISTANCE)) {
		Debugx.msg("Document", "Identifying concepts...");
		applyConceptIdentifier();
	}
	if (options.member(DISTANCE)) {
		Debugx.msg("Document", "Generating distance model...");
		generateDistanceModel();
	}
	Debugx.msg("Document", "Ready.");
}
/**
 * 
 * Creation date: (08/15/99 09:19:16)
 * @param out java.io.PrintStream
 */
public void printDistanceModel(java.io.PrintStream out) {
	Object first, second;
	AccInt model;
	for (Enumeration i=concepts.getConcepts(); i.hasMoreElements();) {
		first = i.nextElement();
		for (Enumeration j=concepts.getConcepts(); j.hasMoreElements();) {
			second = j.nextElement();
			model = distanceModel.getModel(first, second);
			if (model != null) {
				out.println(first.toString() + "\t" + second.toString() + "\t" + model.toString());
			}
		}
	}
}
}
