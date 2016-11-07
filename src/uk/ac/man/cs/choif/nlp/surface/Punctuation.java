package uk.ac.man.cs.choif.nlp.surface;

/**
 * Identifies punctuations.
 * Creation date: (07/22/99 10:17:49)
 * @author: Freddy Choi
 */
public final class Punctuation {
/**
 * Test if T is a word, i.e [a-zA-Z]+
 * Creation date: (07/19/99 11:23:57)
 * @return boolean
 * @param T java.lang.String
 */
public final static boolean isPunc(String T) {
	return !isWord(T);
}
/**
 * Given a list of tokens, identify the punctuations.
 * Creation date: (07/22/99 10:18:22)
 * @return boolean[]
 * @param T java.lang.String[]
 */
public final static boolean[] isPunc(String[] T) {
	boolean[] punc = new boolean[T.length];
	for (int i=T.length; i-->0;) punc[i] = !isWord(T[i]);
	return punc;
}
/**
 * Test if T is a word, i.e [a-zA-Z]+
 * Creation date: (07/19/99 11:23:57)
 * @return boolean
 * @param T java.lang.String
 */
public final static boolean isWord(String T) {
	if (T.length() == 0) return false;
	for (int i=T.length(); i-->0;) if (!Character.isLetter(T.charAt(i))) return false;
	return true;
}
}
