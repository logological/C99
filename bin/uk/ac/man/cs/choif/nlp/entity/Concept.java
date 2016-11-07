package uk.ac.man.cs.choif.nlp.entity;

import java.util.*;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.nlp.parse.*;
import uk.ac.man.cs.choif.nlp.statistics.count.*;
import uk.ac.man.cs.choif.nlp.surface.*;
/**
 * A concept that is described by >= 1 phrases (chunks)
 * Creation date: (07/23/99 08:49:44)
 * @author: Freddy Choi
 */
public class Concept {
	/** Original phrases that describes this concept */
	private Vector descriptors = new Vector();

	/** Abstraction : Heads that describes this concept */
	private Occurrence heads = new Occurrence();

	/** Abstraction : Valid modifiers */
	private Occurrence modifiers = new Occurrence();
/**
 * Concept constructor comment.
 */
public Concept() {
	super();
}
/**
 * Add a descriptor to the concept.
 * Creation date: (07/23/99 09:04:25)
 * @param chunk uk.ac.man.cs.choif.nlp.parse.Chunk
 */
public void addPhrase(Chunk chunk) {
	descriptors.addElement(chunk);

	Vector T = chunk.getText();

	/* Compute abstraction */
	if (T.size() > 0) {
		/* Assuming the last word is the head */
		String head = Stemmer.stemOf((String) T.lastElement());
		heads.add(head);

		/* Assuming the rest are modifiers */
		if (T.size() > 1) {
			String modifier;
			for (int i=T.size()-1; i-->0;) {
				modifier = Stemmer.stemOf((String) T.elementAt(i));
				modifiers.add(modifier);
			}
		}
	}	
}
/**
 * Get the descriptors for this concept.
 * Creation date: (07/23/99 09:05:52)
 * @return java.util.Vector
 */
public Vector getDescriptors() {
	return descriptors;
}
/**
 * Get the head words that describes this concept.
 * Creation date: (07/24/99 19:25:58)
 * @return uk.ac.man.cs.choif.nlp.statistics.count.Occurrence
 */
protected Occurrence getHeads() {
	return heads;
}
/**
 * Get the modifiers for the heads.
 * Creation date: (07/24/99 19:26:40)
 * @return uk.ac.man.cs.choif.nlp.statistics.count.Occurrence
 */
protected Occurrence getModifiers() {
	return modifiers;
}
/**
 * Merge a concept with this concept.
 * Creation date: (07/24/99 19:40:48)
 * @param c uk.ac.man.cs.choif.nlp.entity.Concept
 */
protected void merge(Concept c) {
	Vectorx.append(getDescriptors(), c.getDescriptors());
	getHeads().merge(c.getHeads());
	getModifiers().merge(c.getModifiers());
}
/**
 * 
 * Creation date: (07/24/99 05:06:06)
 * @return java.lang.String
 */
public String toString() {
	return descriptors.elementAt(0).toString();
}
}
