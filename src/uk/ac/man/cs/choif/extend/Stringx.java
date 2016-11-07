package uk.ac.man.cs.choif.extend;

import java.io.*;
import java.util.*;
/**
 * Functions for string manipulation
 * Creation date: (07/18/99 11:16:17)
 * @author: Freddy Choi
 */
public class Stringx implements sun.misc.Compare {
	public final static String[] commonPrefix = new String[]{"re", "un"};
/**
 * String compare for use with sort.
 * Creation date: (07/18/99 13:22:37)
 * @return int
 * @param x java.lang.Object
 * @param y java.lang.Object
 */
public int doCompare(Object x, Object y) {
	return ((String) x).compareTo((String) y);
}
/**
 * Test if T is a word, i.e [a-zA-Z]+
 * Creation date: (07/19/99 11:23:57)
 * @return boolean
 * @param T java.lang.String
 */
public static boolean isWord(String T) {
	for (int i=T.length(); i-->0;) if (!Character.isLetter(T.charAt(i))) return false;
	return true;
}
/**
 * Given a piece of text, create a string that is a concatenation of
 * n occurrences of the text.
 * Creation date: (08/16/99 19:07:01)
 * @return java.lang.String
 * @param text java.lang.String
 * @param n int
 */
public static String makeString(String text, int n) {
	StringBuffer buffer = new StringBuffer();
	for (int i=n; i-->0;) buffer.append(text);
	return buffer.toString();
}
/**
 * Remove common prefixes from the string
 * Creation date: (09/10/99 12:29:40)
 * @return java.lang.String
 * @param str java.lang.String
 */
public static String removePrefix(String str) {
	// Simple case when there is a hypen
	int index = str.indexOf("-");
	if (index != -1) return str.substring(index+1, str.length());
	// Not hyphenated, slow and painful
	for (int i=0, ie=commonPrefix.length; i<ie; i++) {
		if (str.startsWith(commonPrefix[i])) return str.substring(commonPrefix[i].length(), str.length());
	}
	return str;
}
/**
 * Replace the last occurrence of a pattern.
 * Creation date: (09/10/99 12:15:48)
 * @return java.lang.String
 * @param str java.lang.String
 * @param pattern java.lang.String
 * @param replacement java.lang.String
 */
public static String replaceLast(String str, String pattern, String replacement) {
	final int index = str.lastIndexOf(pattern);
	if (index == -1) return str;
	return str.substring(0, index) + replacement + str.substring(index+pattern.length(), str.length());
}
/**
 * Sort a vector of strings.
 * Creation date: (07/18/99 13:28:51)
 * @return java.util.Vector
 * @param list java.util.Vector
 */
public static Vector sort(Vector list) {
	String[] array = new String[list.size()];
	list.copyInto(array);
	sun.misc.Sort.quicksort(array, new Stringx());
	return Arrayx.toVector(array);
}
/**
 * Sort an array of strings.
 * Creation date: (07/18/99 13:27:35)
 * @return String[]
 * @param list java.lang.String[]
 */
public static String[] sort(String[] list) {
	sun.misc.Sort.quicksort(list, new Stringx());
	return list;
}
/**
 * 
 * Creation date: (09/02/99 04:11:01)
 * @return java.lang.String
 * @param T java.lang.String
 */
public static String toAlphaNumeric(String T) {
	String out = "";
	char c;
	for (int i=0, ie=T.length(); i<ie; i++) {
		c = T.charAt(i);
		if (Character.isJavaLetterOrDigit(c)) out += c;
	}
		
	return out;
}
/**
 * Tokenize a space separated string and return the tokens as a list.
 * Creation date: (07/18/99 11:17:52)
 * @return java.util.Vector
 * @param T java.lang.String
 * @param delimiters java.lang.String
 */
public static Vector tokenize(String T, String delimiters) {
	if (T!=null) {
		StringTokenizer tk = new StringTokenizer(T, delimiters, false);
		Vector tokens = new Vector();
		while (tk.hasMoreTokens()) tokens.addElement(tk.nextToken());
		return tokens;
	}
	else return new Vector();
}
/**
 * Tokenize a string according to UPenn's convention and return the tokens as a list.
 * Creation date: (07/18/99 11:17:52)
 * @return java.util.Vector
 * @param T java.lang.String
 */
public static Vector tokenizeUPenn(String T) {
	// Pre-process 's
	StringBuffer B;
	for (int index=T.indexOf("'s"); index!=-1; index=T.indexOf("'s", index+2)) {
		B = new StringBuffer(T);
		B.insert(index, ' ');
		T = B.toString();
	}
		
	
	StreamTokenizer tk = new StreamTokenizer(new StringReader(T));

	// Setup the syntax table
	tk.resetSyntax();
	tk.wordChars('a', 'z');
	tk.wordChars('A', 'Z');
	tk.wordChars('0', '9');
	tk.wordChars('\'', '\'');
	tk.whitespaceChars(' ', ' ');
	tk.lowerCaseMode(false);
	
	Vector tokens = new Vector();
	try {
		int ttype = tk.nextToken();
		while (ttype != tk.TT_EOF) {
			if (ttype == tk.TT_WORD) tokens.addElement(tk.sval);
			else tokens.addElement((new Character((char) ttype)).toString());
			ttype = tk.nextToken();
		}
	}
	catch (Throwable e) { Debugx.handle(e); }
		
	return tokens;
}
/**
 * Given a string and the maximum width, break the string
 * down into blocks of string which is narrower then width.
 * Creation date: (08/16/99 18:43:02)
 * @return java.lang.String[]
 * @param text java.lang.String
 * @param width int
 */
public static String[] wordWrap(String text, int width) {
	/* Turn string into a list of tokens */
	Vector v = tokenize(text, " ");

	/* Buffer for constructing the strings */
	Vector buffer = new Vector();

	StringBuffer line = new StringBuffer((String) v.elementAt(0));
	String token, left, right;
	for (int i=1, ie=v.size(); i<ie; i++) {
		token = (String) v.elementAt(i);
		/* Hyphenate if necessary */
		if (token.length() > width) {
			left = token.substring(0, width-1) + "-";
			right = token.substring(width-1);
			buffer.addElement(line);
			buffer.addElement(new StringBuffer(left));
			line = new StringBuffer(right);
		}
		else {
			/* Carriage return */
			if (line.length() + 1 + token.length() > width) {
				buffer.addElement(line);
				line = new StringBuffer(token);
			}
			/* Normal append */
			else line.append(" " + token);
		}
	}
	if (line.length() > 0) buffer.addElement(line);
	
	/* Make buffer into an array */
	String[] output = new String[buffer.size()];

	for (int i=output.length; i-->0;) {
		line = (StringBuffer) buffer.elementAt(i);
		/* JDK for linux doesn't like this 
		line.setLength(width); */
		while (line.length() < width) line.append(" ");
		output[i] = line.toString();
	}
		
	return output;
}
}
