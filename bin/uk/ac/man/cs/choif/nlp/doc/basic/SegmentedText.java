package uk.ac.man.cs.choif.nlp.doc.basic;

import java.util.*;
import java.io.*;
import uk.ac.man.cs.choif.nlp.surface.*;
/**
 * A pre-segmented text collection. Use resource file 3 to
 * convert segmented file for use with this class
 * Creation date: (07/12/99 00:37:12)
 * @author: Freddy Choi
 */
public class SegmentedText implements Tokenised, Sentences, Segmented {
	/* Information from the source */
	public Vector text = new Vector(300,50); // Source text
	public Vector boundaries = new Vector(100,20); // Sentence boundaries
	public Vector topicBoundaries = new Vector(100,20); // Topic boundaries
/**
 * Collection constructor comment.
 */
public SegmentedText() {
	super();
}
/**
 * Load a collection from input stream
 * Creation date: (07/12/99 00:37:40)
 * @param file java.lang.String
 */
public SegmentedText(InputStream in) {
	try {
		/* Open the file and attach stream tokenizer */
		Reader r = new BufferedReader(new InputStreamReader(in));
		parse(r);
		r.close();
	}
	catch (Exception e) {}
}
/**
 * Load a collection from disk
 * Creation date: (07/12/99 00:37:40)
 * @param file java.lang.String
 */
public SegmentedText(String file) {
	try {
		/* Open the file and attach stream tokenizer */
		Reader r = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		parse(r);
		r.close();
	}
	catch (Exception e) {}
}
/**
 * Parse a formatted stream.
 * Creation date: (07/12/99 00:40:21)
 * @param r java.io.Reader
 */
protected void parse(Reader r) {

	/* 1. Setup syntax table for the tokeniser */
	StreamTokenizer tk = new StreamTokenizer(r);
	tk.resetSyntax();
	tk.wordChars('\u0021', '\u00FF');
	tk.whitespaceChars('\u0000', '\u0020');
	tk.eolIsSignificant(true);
	tk.lowerCaseMode(false);

	/* 2. Define variables */
	int word = 0; // Absolute word count, first word is word 0
	int prev_word = -1; // The location of the previous boundary
	int sep; // Position of the / separator.

	/* 3. Add the implicit boundary at the
	beginning of the collection */
 	boundaries.addElement(new Integer(word));
		
	/* 4. Parse stream */
	try {
		while (tk.nextToken() != tk.TT_EOF) {
			/* Case 1. End of sentence */
			if (tk.ttype == tk.TT_EOL && word != prev_word) { 
				boundaries.addElement(new Integer(word));
				prev_word = word; 
			}
			/* Case 2. Topic boundary */
			else if (tk.ttype == tk.TT_WORD && tk.sval.startsWith("==")) {
				topicBoundaries.addElement(new Integer(word));
				tk.nextToken(); // Skip EOL
			}
			/* Case 3. Token */
			else if (tk.ttype == tk.TT_WORD) {
				text.addElement(tk.sval);
				word++;
			}
		}
	}
	catch (IOException e) {}
}
/**
 * Get the sentence boundaries
 * Creation date: (07/19/99 10:15:59)
 * @return java.util.Vector
 */
public Vector sentenceBoundaries() {
	return boundaries;
}
/**
 * Get the tokens
 * Creation date: (07/19/99 10:15:32)
 * @return java.util.Vector
 */
public Vector tokens() {
	return text;
}
/**
 * Get the topic boundaries.
 * Creation date: (07/19/99 10:19:03)
 * @return java.util.Vector
 */
public Vector topicBoundaries() {
	return topicBoundaries;
}
}
