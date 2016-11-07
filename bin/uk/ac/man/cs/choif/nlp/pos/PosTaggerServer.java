package uk.ac.man.cs.choif.nlp.pos;

import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import uk.ac.bham.clg.cue.modules.tagger.*;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.extend.io.*;

/**
 * Client/Server version of QTAG, the Birmingham POS tagger.
 */
public class PosTaggerServer extends UnicastRemoteObject implements PosTaggerServerI {
	public final static String defaultResourceFile = Resourcex.PosTagger_defaultResourceFile;
	protected QTag qtag = null;
/**
 * Create a new tagger for the tagset.
 * @param tagset java.lang.String The tagset file (e.g. BLT.qtag)
 */
public PosTaggerServer (final File tagset) throws RemoteException {
	super();
	try {
		Debugx.msg("JPos", "Initialising tagger...");
		qtag = new QTag(tagset.getPath(), false);
		if (qtag == null || !qtag.isOK()) {
			Debugx.msg("JPos", "Unable to initialise tagger with tagset " + tagset.getPath());
			System.exit(1);
		}
	}
	catch (Throwable e) {
		Debugx.handle(e);
		System.exit(1);
	}
}
/**
 * Start the pos tagger server with a resource file
 * Expected arguments : -r "datafile" -h "host:port"
 * Creation date: (09/08/99 03:12:35)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	Debugx.header("This is JPos (server), a Client/Server POS tagger based on Oliver Mason's QTAG.");
	if (System.getSecurityManager() == null) System.setSecurityManager(new RMISecurityManager());

	Argx arg = new Argx(args);
	String datafile = arg.get("-r", defaultResourceFile, "Path and name of the tag set resource file, e.g. QTAG.tagset");
	arg.displayHelp();

	try {
		if (!RMIx.startServer("PosTaggerServer", new PosTaggerServer(new File(datafile)))) throw new Exception("Unable to start server");
	}
	catch (Throwable e) { Debugx.handle(e); }
}
/**
 * Tag a sentence.
 * @return java.lang.String[] List of POS tags.
 * @param sentence java.lang.String[] List of tokens.
 */
public String[] tag (final String[] sentence) {
	/* Qtag has a buffer of 3 items internally,
	so there is a delay of 3 between firing the
	method tag(token, markup) and actually getting
	the output */

	/* Support for SGML markup in QTAG.
	We have no use for that. Just a dummy
	variable to keep the tagget happy. */
	StringBuffer markup = null;


	/* Lets tag the sentence */
	String[] tag = new String[sentence.length];
	String token;
	Word word;
	for (int i=0, ie=sentence.length+3; i<ie; i++) {
		/* Decide what the token is going to be */
		if (i < sentence.length) {
			token = sentence[i];
			/* Pre-processing to handle odd bods */
			if (token.equals("''")) token = "\"";
			if (token.equals("``")) token = "\"";
		}
		else token = ".";

		/* Feed the tagger */
		word = qtag.tag(token, markup);
		if (i > 2) tag[i-3] = word.getTag();
	}

	return tag;
}
}
