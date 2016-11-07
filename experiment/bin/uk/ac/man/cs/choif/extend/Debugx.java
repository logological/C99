package uk.ac.man.cs.choif.extend;

import java.util.Vector;
import java.util.Date;
import java.io.*;
import uk.ac.man.cs.choif.extend.io.LineOutputForked;
/**
 * Tools for debugging
 * Creation date: (07/31/99 19:40:22)
 * @author: Freddy Choi
 */
public class Debugx {
	public final static LineOutputForked output = new LineOutputForked(System.err);

	/** Fixed information */
	private final static String comment_marker = "#";
	private final static String separator = " : ";
	private final static int header_width = 60;
	private final static String info_license = "Free for educational, research and other non-profit making uses only. Copyright 1999.";
	private final static String info_author = "Freddy Choi - Artificial Intelligence Group, Department of Computer Science, University of Manchester, Oxford Road, Manchester, M13 9PL, England";
	private final static String info_www = "Website : http://www.cs.man.ac.uk/~choif";
	private final static String info_mail = "e:mail  : choif@cs.man.ac.uk";
/**
 * 
 * Creation date: (10/11/99 14:24:24)
 * @return java.lang.String
 */
public static String date() {
	return (new Date()).toString();
}
/**
 * <li>Report elapsed time in a more friendly form.
 * @return String Elapsed time in string format.
 * @param long Start time
 * @param long End time
 */
public static String elapsed (long startTime, long endTime) {
	long elapsed = endTime - startTime;
	// Print output in different modes depending on the order of elapsed time
	// Less then a second
	if (elapsed < 1000) return elapsed + "ms";
	// Less then a minute
	if (elapsed < 60000) return (elapsed / 1000) + "s " + (elapsed % 1000) + "ms";
	// Less then an hour
	if (elapsed < 3600000) return (elapsed / 60000) + "mins " + ((elapsed % 60000) / 1000) + "s " + (elapsed % 1000) + "ms";
	// Greater than an hour
	return (elapsed / 3600000) + "hours " + ((elapsed % 3600000) / 60000) + "mins " + ((elapsed % 60000) / 1000) + "s";
}
/**
 * Get the stack trace of a throwable object
 * Creation date: (10/11/99 15:27:53)
 * @return java.lang.String
 * @param e java.lang.Throwable
 */
public static String getStackTrace(Throwable e) {
	StringWriter st = new StringWriter();
	e.printStackTrace(new PrintWriter(st));
	return st.toString();
}
/**
 * Error handling
 * Creation date: (09/01/99 18:56:25)
 * @param e java.lang.Error
 */
public static void handle(Error e) {
	msg("========== !! ERROR !! ==========");
	msg(e.getMessage());
	msg(getStackTrace(e));
	System.exit(1);
}
/**
 * Exception handling
 * Creation date: (09/01/99 18:56:25)
 * @param e java.lang.Exception
 */
public static void handle(Exception e) {
	msg("========== !! EXCEPTION !! ==========");
	msg(e.getMessage());
	msg(getStackTrace(e));
}
/**
 * Exception handling
 * Creation date: (09/01/99 18:56:25)
 * @param e java.lang.Throwable
 */
public static void handle(Throwable e) {
	if (Exception.class.isInstance(e)) handle((Exception) e);
	else handle((Error) e);
}
/**
 * Prints header information
 * Creation date: (08/16/99 18:35:32)
 * @param text java.lang.String
 */
public static void header(String text) {
	String[] line;
	String horizontal_line = Stringx.makeString(comment_marker, header_width);

	output.println("\n" + horizontal_line);
	printBlock(text);
	printBlock(info_license);
	printBlock(info_author);
	printBlock(info_www);
	printBlock(info_mail);
	output.println(horizontal_line);
}
/**
 * Print message to output.
 * Creation date: (07/31/99 19:40:52)
 * @param message java.lang.Object
 */
public static void msg(Object message) {
	msg(message.toString());
}
/**
 * Print message to output.
 * Creation date: (07/31/99 19:40:52)
 * @param message java.lang.String
 */
public static void msg(String message) {
	Vector V = Stringx.tokenize(message, "\n");
	for (int i=0, ie=V.size(); i<ie; i++) output.println(comment_marker + " " + V.elementAt(i));
}
/**
 * Print message to output.
 * Creation date: (07/31/99 19:40:52)
 * @param header java.lang.String
 * @param message double
 */
public static void msg(String header, double message) {
	output.println(comment_marker + " " + header + separator + message);
}
/**
 * Print message to output.
 * Creation date: (07/31/99 19:40:52)
 * @param header java.lang.String
 * @param message float
 */
public static void msg(String header, float message) {
	output.println(comment_marker + " " + header + separator + message);
}
/**
 * Print message to output.
 * Creation date: (07/31/99 19:40:52)
 * @param header java.lang.String
 * @param message int
 */
public static void msg(String header, int message) {
	output.println(comment_marker + " " + header + separator + message);
}
/**
 * Print message to output.
 * Creation date: (07/31/99 19:40:52)
 * @param header java.lang.String
 * @param message java.lang.Exception
 */
public static void msg(String header, Exception message) {
	output.println(comment_marker + " " + header + separator + message);
}
/**
 * Print message to output.
 * Creation date: (07/31/99 19:40:52)
 * @param header java.lang.String
 * @param message java.lang.Object
 */
public static void msg(String header, Object message) {
	output.println(comment_marker + " " + header + separator + message.toString());
}
/**
 * Print message to output.
 * Creation date: (07/31/99 19:40:52)
 * @param header java.lang.String
 * @param message java.lang.String
 */
public static void msg(String header, String message) {
	output.println(comment_marker + " " + header + separator + message);
}
/**
 * Print message to output.
 * Creation date: (07/31/99 19:40:52)
 * @param header java.lang.String
 * @param message boolean
 */
public static void msg(String header, boolean message) {
	output.println(comment_marker + " " + header + separator + message);
}
/**
 * Print a part of the header block.
 * Creation date: (08/16/99 19:13:27)
 * @param text java.lang.String
 */
private static void printBlock(String text) {
	String[] line = Stringx.wordWrap(text, header_width-4);
	for (int i=0, ie=line.length; i<ie; i++) output.println(comment_marker + " " + line[i] + " " + comment_marker);
}
/**
 * <li>The current time.
 * @return long  Current time
 */
public static long timeNow () {
	return java.lang.System.currentTimeMillis();
}
}
