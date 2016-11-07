package uk.ac.man.cs.choif.nlp.pos;

import java.util.*;
import java.io.*;
import uk.ac.bham.clg.cue.modules.tagger.*;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.extend.io.*;

/**
 * Local wrapper for QTAG, the Birmingham POS tagger.
 */
public class PosTaggerLocal implements PosTaggerServerI {
	protected QTag qtag = null;
/**
 * Create a new tagger for the tagset.
 * @param tagset java.lang.String The tagset file (e.g. BLT.qtag)
 */
public PosTaggerLocal (final File tagset) {
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
