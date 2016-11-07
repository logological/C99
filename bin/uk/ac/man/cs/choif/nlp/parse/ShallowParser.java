package uk.ac.man.cs.choif.nlp.parse;

import uk.ac.man.cs.choif.extend.io.LineInput;
import java.util.*;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.nlp.pos.*;
/**
 * A syntactic based shallow parser
 * Creation date: (07/22/99 09:18:32)
 * @author: Freddy Choi
 */
public class ShallowParser {
	private Tagger tagger = null;
	private TagsetMapper mapper = new TagsetMapper();
/**
 * Shallow parser for pre-tagged text
 */
public ShallowParser() {
	initMap();
}
/**
 * Given a list of phrases, keep only those that are concepts.
 * All syntactic glue is removed.
 * Creation date: (07/24/99 06:18:57)
 * @return uk.ac.man.cs.choif.nlp.parse.Chunk[]
 * @param P uk.ac.man.cs.choif.nlp.parse.Chunk[]
 */
public static Chunk[] filterGlue(Chunk[] P) {
	Vector v = new Vector();
	for (int i=0, ie=P.length; i<ie; i++) {
		if (P[i].getType() != Chunk.GLUE) v.addElement(P[i]);
	}
	Chunk[] output = new Chunk[v.size()];
	v.copyInto(output);
	return output;
}
/**
 * Initialise tag set mappings
 * Creation date: (07/23/99 07:19:14)
 */
private void initMap() {
/*	mapper.declareMap("DT CC EX , ! . ; ( ) : ? - \" ... IN SYM POS TO UH WDT WP WRB WP$ MD RP PDT", "C");
	mapper.declareMap("NN NNS CD NP FW NPS PP", "O");
	mapper.declareMap("JJ JJS JJR", "O");
	mapper.declareMap("VB VBG VBZ VBN VBD HV HVZ HVD", "A");
	mapper.declareMap("RB RBR RBS", "A"); */

	
	/* Closed class or glue - C */
	mapper.declareMap("BE BEDR BEDZ BEG BEM BEN BER BEZ CC CS DO DOD DOG DON DOZ EX HV HVD HVG HVN HVZ IN JJR MD PDT PN POS RB RBR RP TO UH WDT WP WP$ WRB XNOT ! \" ' ( ) , - . ... : ; ?", "C");
	/* Object - O */
	mapper.declareMap("FW NN NNS NP NPS PP PPX SYM ???", "O");
	/* Object modifier - O */
	mapper.declareMap("CD DT JJ JJS OD PP$", "OM");
	/* Action - A */
	mapper.declareMap("VB VBD VBG VBN VBZ", "A");
	/* Action modifier - A */
	mapper.declareMap("RBS", "AM");
}
/**
 * Shallow parse an input stream where each line is a sentence of tokenised text.
 * Creation date: (08/16/99 18:34:17)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	Debugx.header("This is JShallowParser, a simple rule-based shallow parser for high NP VP recall.");
	
	/* Initialise tagger */
	Debugx.msg("Parser", "Initialising parser...");
	ShallowParser parser = new ShallowParser();

	/* Process file line by line */
	Debugx.msg("Parser", "Parsing input...");
	LineInput in = new LineInput(System.in);
	StringBuffer output;
	String[] token;
	Chunk[] chunk;
	Vector text;
	while (in.hasMoreElements()) {
		token = Vectorx.toStringArray(Stringx.tokenize((String) in.nextElement(), " "));
		chunk = parser.parse(token);
		output = new StringBuffer();
		for (int i=0, ie=chunk.length; i<ie; i++) {
			if (chunk[i].getType() == Chunk.OBJECT) output.append(" [o");
			if (chunk[i].getType() == Chunk.ACTION) output.append(" [a");
			text = chunk[i].getText();
			for (int j=0, je=text.size(); j<je; j++) output.append(" " + (String) text.elementAt(j));
			if (chunk[i].getType() != Chunk.GLUE) output.append("]");
		}
		System.out.println(output.toString().trim());
	}
}
/**
 * Create a new chunk of type according to tag.
 * Creation date: (08/16/99 20:49:06)
 * @return uk.ac.man.cs.choif.nlp.parse.Chunk
 * @param tag java.lang.String
 */
private static Chunk newChunk(String tag) {
	Chunk chunk;
	if (tag.equals("C")) chunk = new Chunk(Chunk.GLUE);
	else if (tag.startsWith("O")) chunk = new Chunk(Chunk.OBJECT);
	else chunk = new Chunk(Chunk.ACTION);
	return chunk;
}
/**
 * Given a sentence, shallow parse to produce a list of chunks.
 * Creation date: (07/22/99 09:37:21)
 * @return uk.ac.man.cs.choif.nlp.parse.Chunk[]
 * @param sentence java.lang.String[]
 */
public Chunk[] parse(String[] sentence) {
	if (tagger == null) tagger = new Tagger();
	/* Apply POS tagger and mapper */
	String[] pos = tagger.tag(sentence);
	String[] tag = mapper.map(pos);

	/* Selectively make words lower case.
	Everything but names should be lower case */
	String[] tokens = new String[sentence.length];
	for (int i=sentence.length; i-->0;) {
		if (!(pos[i].equals("NP") || pos[i].equals("PP"))) tokens[i] = sentence[i].toLowerCase();
		else tokens[i] = sentence[i];
	}

	/* Identify chunks */
	Vector V = new Vector();
	Chunk chunk = new Chunk();
	String pTag = "*BEGIN*";


	for (int i=0, ie=tag.length; i<ie; i++) {
		/* ================ RULES #1
		Object = (OM ... OM O) | (O)
		Action = (AM ... AM A) | (A)
		Glue   = (C ... C)
		if (pTag.equals("O") || pTag.equals("A")) {
			V.addElement(chunk);
			chunk = newChunk(tag[i]);
		}
		else if (pTag.equals("OM") && !tag[i].startsWith("O")) {
			V.addElement(chunk);
			chunk = newChunk(tag[i]);
		}
		else if (pTag.equals("AM") && !tag[i].startsWith("A")) {
			V.addElement(chunk);
			chunk = newChunk(tag[i]);
		}
		else if (pTag.equals("C") && !tag[i].equals("C")) {
			V.addElement(chunk);
			chunk = newChunk(tag[i]);
		} */

		/* ================ RULES #1
		Object = (OM ... OM O ... O)
		Action = (AM ... AM A ... A)
		Glue   = (C ... C) */
		if (pTag.startsWith("O") && !tag[i].startsWith("O")) {
			V.addElement(chunk);
			chunk = newChunk(tag[i]);
		}
		else if (pTag.startsWith("A") && !tag[i].startsWith("A")) {
			V.addElement(chunk);
			chunk = newChunk(tag[i]);
		}
		else if (pTag.equals("C") && !tag[i].equals("C")) {
			V.addElement(chunk);
			chunk = newChunk(tag[i]);
		}
		else if (i == 0) chunk = newChunk(tag[i]);
		chunk.append(tokens[i]);
		pTag = tag[i];
	}		
	if (chunk.length() != 0) V.addElement(chunk);

	Chunk[] output = new Chunk[V.size()];
	V.copyInto(output);

	return output;
}
/**
 * Given a sentence, shallow parse to produce a list of chunks.
 * Creation date: (07/22/99 09:37:21)
 * @return uk.ac.man.cs.choif.nlp.parse.Chunk[]
 * @param sentence java.lang.String[]
 * @param pos java.lang.String[] The corresponding pos tags
 */
public Chunk[] parse(String[] sentence, String[] pos) {
	/* Apply POS tagger and mapper */
	String[] tag = mapper.map(pos);

	/* Selectively make words lower case.
	Everything but names should be lower case */
	String[] tokens = new String[sentence.length];
	for (int i=sentence.length; i-->0;) {
		if (!(pos[i].equals("NP") || pos[i].equals("PP"))) tokens[i] = sentence[i].toLowerCase();
		else tokens[i] = sentence[i];
	}

	/* Identify chunks */
	Vector V = new Vector();
	Chunk chunk = new Chunk();
	String pTag = "*BEGIN*";


	for (int i=0, ie=tag.length; i<ie; i++) {
		/* ================ RULES #1
		Object = (OM ... OM O) | (O)
		Action = (AM ... AM A) | (A)
		Glue   = (C ... C)
		if (pTag.equals("O") || pTag.equals("A")) {
			V.addElement(chunk);
			chunk = newChunk(tag[i]);
		}
		else if (pTag.equals("OM") && !tag[i].startsWith("O")) {
			V.addElement(chunk);
			chunk = newChunk(tag[i]);
		}
		else if (pTag.equals("AM") && !tag[i].startsWith("A")) {
			V.addElement(chunk);
			chunk = newChunk(tag[i]);
		}
		else if (pTag.equals("C") && !tag[i].equals("C")) {
			V.addElement(chunk);
			chunk = newChunk(tag[i]);
		} */

		/* ================ RULES #1
		Object = (OM ... OM O ... O)
		Action = (AM ... AM A ... A)
		Glue   = (C ... C) */
		if (pTag.startsWith("O") && !tag[i].startsWith("O")) {
			V.addElement(chunk);
			chunk = newChunk(tag[i]);
		}
		else if (pTag.startsWith("A") && !tag[i].startsWith("A")) {
			V.addElement(chunk);
			chunk = newChunk(tag[i]);
		}
		else if (pTag.equals("C") && !tag[i].equals("C")) {
			V.addElement(chunk);
			chunk = newChunk(tag[i]);
		}
		else if (i == 0) chunk = newChunk(tag[i]);
		chunk.append(tokens[i]);
		pTag = tag[i];
	}		
	if (chunk.length() != 0) V.addElement(chunk);

	Chunk[] output = new Chunk[V.size()];
	V.copyInto(output);

	return output;
}
}
