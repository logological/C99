package uk.ac.man.cs.choif.nlp.surface;

import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.extend.io.*;
/**
 * 
 * Creation date: (09/21/99 01:52:33)
 * @author: Freddy Choi
 */
public class Stopword extends java.util.Hashtable {
	public final static String defaultResourceFile = Resourcex.Stopword_defaultResourceFile;
/**
 * Stop constructor comment.
 */
public Stopword() {
	this(new java.io.File(defaultResourceFile));
}
/**
 * 
 * Creation date: (09/21/99 01:54:44)
 * @param in java.io.File
 */
public Stopword(java.io.File in) {
	super(100, (float) 0.6);
	Debugx.msg("Stopword", "Loading stopword list...");
	try {
		parse(new LineInput(in));
		Debugx.msg("Stopword", "Ready.");
	}
	catch (Exception e) {
		Debugx.handle(e);
	}
}
/**
 * Test if word is a stopword.
 * Creation date: (07/12/99 23:46:08)
 * @return boolean
 * @param word java.lang.String
 */
public boolean isStopword(final String word) {
	return (get(word) != null);
}
/**
 * Given a list of tokens, identify the stopwords.
 * Creation date: (07/22/99 09:39:41)
 * @return boolean[] Each component corresponds to a token in T, true if stopword, false otherwise.
 * @param T java.lang.String[] List of tokens
 */
public boolean[] isStopword(String[] T) {
	boolean[] result = new boolean[T.length];
	for (int i=result.length; i-->0;) result[i] = (get(T[i]) != null);
	return result;
}
/**
 * Parse an input file
 * Creation date: (09/21/99 01:56:09)
 * @param in uk.ac.man.cs.choif.extend.io.LineInput
 */
private void parse(LineInput in) {
	Boolean dummy = new Boolean(true);
	while (in.hasMoreElements()) put((String) in.nextElement(), dummy);
}
}
