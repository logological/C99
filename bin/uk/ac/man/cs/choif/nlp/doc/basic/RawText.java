package uk.ac.man.cs.choif.nlp.doc.basic;

import java.util.*;
import java.io.*;
import uk.ac.man.cs.choif.nlp.surface.*;
/**
 * A text collection for segmentation. Use resource file 2 to
 * convert a Brown corpus file into raw text which is used by
 * this class.
 * Creation date: (07/12/99 00:37:12)
 * @author: Freddy Choi
 */
public class RawText implements Tokenised, Sentences {
	/* Information from the source */
	public Vector text = new Vector(300,50); // Source text
	public Vector boundaries = new Vector(100,20); // Sentence boundaries
/**
 * Collection constructor comment.
 */
public RawText() {
	super();
}
/**
 * Load a collection from input stream
 * Creation date: (07/12/99 00:37:40)
 * @param file java.lang.String
 */
public RawText(InputStream in) {
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
public RawText(String file) {
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
			/* Case 2. Token */
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
}
