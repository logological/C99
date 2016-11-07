package uk.ac.man.cs.choif.nlp.doc;

import uk.ac.man.cs.choif.nlp.parse.*;
import uk.ac.man.cs.choif.nlp.entity.*;
import uk.ac.man.cs.choif.extend.*;

/**
 * A sentence
 * Creation date: (08/12/99 22:56:42)
 * @author: Freddy Choi
 */
public class Sentence {
	/** A list of tokens */
	public String[] token = new String[0];

	/** A list of stems */
	public String[] stem = new String[0];

	/** A list of POS */
	public String[] pos = new String[0];

	/** Block expressed as a list of phrases */
	public Chunk[] phrase = new Chunk[0];

	/** Block expressed as a list of entities */
	public Concept[] concept = new Concept[0];
/**
 * 
 * Creation date: (08/15/99 05:50:40)
 * @return java.lang.String
 */
public String toString() {
	String output = "";
	for (int i=0, ie=token.length; i<ie; i++) output += (token[i] + " ");
	return output;
}
}
