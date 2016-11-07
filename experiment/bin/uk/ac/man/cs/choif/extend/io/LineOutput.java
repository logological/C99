package uk.ac.man.cs.choif.extend.io;

import java.io.*;
import uk.ac.man.cs.choif.extend.*;
/**
 * Text line output, no need to close stream explicitly, it cleans itself up.
 * Creation date: (08/19/99 18:50:30)
 * @author: Freddy Choi
 */
public class LineOutput {
	private PrintWriter out;
/**
 * 
 * Creation date: (08/19/99 18:51:09)
 * @param file java.io.File
 */
public LineOutput(File file) {
	try {
		out = new PrintWriter(new BufferedWriter(new FileWriter(file)), true);
	}
	catch (IOException e) {
		Debugx.msg("LineOutput", e);
	}
}
/**
 * 
 * Creation date: (08/19/99 18:58:28)
 * @param out java.io.OutputStream
 */
public LineOutput(OutputStream out) {
	this.out = new PrintWriter(out, true);
}
/**
 * 
 * Creation date: (08/19/99 19:01:29)
 * @param out java.io.PrintStream
 */
public LineOutput(PrintStream out) {
	this.out = new PrintWriter(out, true);
}
/**
 * Close the output stream, not necessary unless you are paranoid programmer.
 * The object closes the stream when it gets garbage collected.
 * Creation date: (08/19/99 19:02:13)
 */
public void close() {
	out.flush();
	out.close();
}
/**
 * Automatic stream closing before garbage collection
 * Creation date: (08/19/99 19:03:59)
 */
protected void finalize() throws Throwable {
	close();
	out = null;
	super.finalize();
}
/**
 * Print object.
 * Creation date: (08/19/99 19:06:15)
 * @param obj java.lang.Object
 */
public void println(Object obj) {
	out.println(obj.toString());
}
/**
 * Save a list of strings to out.
 * Creation date: (11/23/99 11:21:39)
 * @param T java.lang.String[]
 * @param out uk.ac.man.cs.choif.extend.io.LineOutput
 */
public final static void save(final String[] T, LineOutput out) {
	for (int i=0, ie=T.length; i<ie; i++) out.println(T[i]);
}
}
