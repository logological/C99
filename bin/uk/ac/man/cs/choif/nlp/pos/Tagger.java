package uk.ac.man.cs.choif.nlp.pos;

import java.rmi.*;
import java.util.*;
import uk.ac.bham.clg.cue.modules.tagger.*;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.extend.io.*;

/**
 * POS tagger client.
 */
public class Tagger implements PosTaggerClientI {
	private PosTaggerServerI server;
/**
 * Default remote session.
 * Creation date: (09/08/99 20:56:18)
 */
public Tagger() {
	this(Resourcex.rmiHost);
}
/**
 * Start a local session.
 * Creation date: (09/08/99 20:54:54)
 * @param datafile java.io.File
 */
public Tagger(java.io.File datafile) {
	Debugx.msg("JPos", "Starting local session...");
	try {
		server = (PosTaggerServerI) new PosTaggerLocal(datafile);
	}
	catch (Throwable e) {
		Debugx.msg("JPos", "Local initialisation failed, trying default server " + Resourcex.rmiHost + " ...");
		server = (PosTaggerServerI) RMIx.connect("PosTaggerServer");
		if (server == null) System.exit(1);
	}
}
/**
 * Start a remote session.
 * Creation date: (09/08/99 20:53:13)
 * @param host java.net.URL
 */
public Tagger(java.net.URL host) {
	Debugx.msg("JPos", "Starting remote session...");
	server = (PosTaggerServerI) RMIx.connect(host, "PosTaggerServer");
	if (server == null) System.exit(1);
}
/**
 * Tag an input stream where each line is a sentence of tokenised text.
 * Parameters : -h "host:port"
 * Creation date: (08/16/99 18:34:17)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	Debugx.header("This is JPos, a Client/Server POS tagger based on Oliver Mason's QTAG.");
	
	try {
		Tagger tagger = new Tagger();

		/* Process file line by line */
		Debugx.msg("JPos", "Tagging input...");
		LineInput in = new LineInput(System.in);
		StringBuffer output;
		String[] token, pos;
		while (in.hasMoreElements()) {
			token = Vectorx.toStringArray(Stringx.tokenize((String) in.nextElement(), " "));
			pos = tagger.tag(token);
			output = new StringBuffer();
			for (int i=0, ie=token.length; i<ie; i++) output.append(" " + token[i] + "/" + pos[i]);
			System.out.println(output.toString().trim());
		}
		System.exit(0);
	}
	catch (Throwable e) {
		Debugx.handle(e);
		System.exit(1);
	}
}
/**
 * Tag a sentence.
 * @return java.lang.String[] List of POS tags.
 * @param sentence java.lang.String[] List of tokens.
 */
public String[] tag (final String[] sentence) {
	try {
		return server.tag(sentence);
	}
	catch (Throwable e) {
		Debugx.handle(e);
		return null;
	}
}
}
