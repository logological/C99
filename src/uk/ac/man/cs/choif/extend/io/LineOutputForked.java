package uk.ac.man.cs.choif.extend.io;

import java.io.*;
import java.util.*;
/**
 * A text print stream with multiple parallel output paths.
 * Creation date: (10/11/99 14:56:00)
 * @author: Freddy Choi
 */
public class LineOutputForked {
	private Vector output = new Vector();
/**
 * 
 * Creation date: (10/11/99 14:57:47)
 */
public LineOutputForked() {}
/**
 * Create a stream with a default output
 * Creation date: (10/11/99 14:57:47)
 * @param out java.io.PrintStream Default print stream
 */
public LineOutputForked(PrintStream out) {
	attach(out);
}
/**
 * Attach a file to the output stream list
 * Creation date: (10/11/99 15:00:46)
 * @return LineOutput
 * @param out java.io.File
 */
public LineOutput attach(File out) {
	try {
		return attach(new LineOutput(out));
	}
	catch (Exception e) { return null; }
}
/**
 * Attach a stream to the output stream list
 * Creation date: (10/11/99 15:00:46)
 * @return LineOutput
 * @param out PrintStream
 */
public LineOutput attach(PrintStream out) {
	try {
		return attach(new LineOutput(out));
	}
	catch (Exception e) { return null; }
}
/**
 * Attach a new stream to the stream list
 * Creation date: (10/11/99 14:57:38)
 * @return LineOutput
 * @param out LineOutput
 */
public LineOutput attach(LineOutput out) {
	output.addElement(out);
	return (LineOutput) output.lastElement();
}
/**
 * Close all the output streams, not necessary unless you are paranoid programmer.
 * The object closes the stream when it gets garbage collected.
 * Creation date: (08/19/99 19:02:13)
 */
public void close() {
	for (int i=output.size(); i-->0;) ((LineOutput) output.elementAt(i)).close();
}
/**
 * Detach a output stream from the stream list
 * Creation date: (10/11/99 15:11:34)
 * @param stream uk.ac.man.cs.choif.extend.io.LineOutput
 */
public void detach(LineOutput stream) {
	stream.close();
	output.removeElement(stream);
}
/**
 * Automatic stream closing before garbage collection
 * Creation date: (08/19/99 19:03:59)
 */
protected void finalize() throws Throwable {
	close();
	output = null;
	super.finalize();
}
/**
 * 
 * Creation date: (10/11/99 15:12:51)
 * @param text java.lang.String
 */
public void println(String text) {
	for (int i=output.size(); i-->0;) ((LineOutput) output.elementAt(i)).println(text);
}
}
