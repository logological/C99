package uk.ac.man.cs.choif.nlp.parse;

import java.util.Vector;
import uk.ac.man.cs.choif.extend.*;
/**
 * A chunk of text
 * Creation date: (07/22/99 09:27:53)
 * @author: Freddy Choi
 */
public class Chunk {
	/* Chunk types */
	/** Object type chunk, e.g. NP, AP, PP */
	public final static int OBJECT = 1;

	/** Action type chunk, e.g. VP */
	public final static int ACTION = 2;

	/** Glue type chunk, e.g. stopwords */
	public final static int GLUE = 3;

	private Vector text = new Vector();
	private int type = GLUE;
/**
 * Chunk constructor comment.
 */
public Chunk() {
	super();
}
/**
 * 
 * Creation date: (07/22/99 09:34:22)
 * @param type int
 */
public Chunk(int type) {
	super();
	if (type > 0 && type < 4) this.type = type;
}
/**
 * Append token to the end of the chunk.
 * Creation date: (07/22/99 09:33:41)
 * @param token java.lang.String
 */
public void append(String token) {
	text.addElement(token);
}
/**
 * Make the phrase into a chunk
 * Creation date: (07/24/99 02:33:53)
 * @return uk.ac.man.cs.choif.nlp.parse.Chunk
 * @param phrase java.lang.String
 */
public static Chunk defineChunk(String phrase) {
	Chunk chunk = new Chunk();
	chunk.text = Stringx.tokenize(phrase, " ");
	return chunk;
}
/**
 * Test two phrases for equality. This only considers
 * the surface form. If two phrases have the same surface
 * form then they are equal regardless of their phrase type.
 * Creation date: (07/24/99 18:47:00)
 * @return boolean
 * @param obj java.lang.Object
 */
public boolean equals(Object obj) {
	String p = text.toString();
	String pp = ((Chunk) obj).text.toString();
	return p.equals(pp);
}
/**
 * Get text.
 * Creation date: (07/22/99 09:33:06)
 * @return java.util.Vector A vector of strings.
 */
public Vector getText() {
	return text;
}
/**
 * Get the type of the chunk.
 * Creation date: (07/22/99 09:35:21)
 * @return int Encoded type: OBJECT, ACTION or GLUE.
 */
public int getType() {
	return type;
}
/**
 * Get the size of the chunk in number of tokens.
 * Creation date: (07/22/99 11:06:49)
 * @return int
 */
public int length() {
	return text.size();
}
/**
 * 
 * Creation date: (07/22/99 11:07:54)
 * @return java.lang.String
 */
public String toString() {
	String output = text.toString();
	switch (type) {
		case OBJECT:	output = "O:" + output; break;
		case ACTION:	output = "A:" + output; break;
		case GLUE:		output = "G:" + output; break;
	}
	return output;
}
}
