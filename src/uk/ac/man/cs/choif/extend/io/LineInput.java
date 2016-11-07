package uk.ac.man.cs.choif.extend.io;

import java.io.*;
import java.util.*;
import uk.ac.man.cs.choif.extend.*;
/**
 * Convenient class for reading a text file where each entry
 * occupies an individual line.
 * Creation date: (07/30/99 14:45:05)
 * @author: Freddy Choi
 */
public class LineInput implements Enumeration {
	private BufferedReader in;
	private String textLine = null;
public LineInput(File file) throws IOException {
	byte[] data = new byte[(int) file.length()];
	InputStream bin = new FileInputStream(file);
	bin.read(data);
	bin.close();
	in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
	nextElement();
}
/**
 * 
 * Creation date: (08/16/99 19:30:04)
 * @param in java.io.InputStream
 */
public LineInput(InputStream in) {
	this.in = new BufferedReader(new InputStreamReader(in));
	nextElement();
}
/**
 * Clean up
 * Creation date: (07/30/99 14:50:37)
 */
protected void finalize() throws Throwable {
	in.close();
	textLine = null;
	super.finalize();
}
/**
 * More lines.
 * Creation date: (07/30/99 14:48:09)
 * @return boolean
 */
public boolean hasMoreElements() {
	return textLine != null;
}
/**
 * Load all the text lines from an input stream
 * Creation date: (11/05/99 03:35:20)
 * @return java.lang.String[]
 * @param in uk.ac.man.cs.choif.extend.io.LineInput
 */
public final static String[] load(final LineInput in) {
	final Vector V = Vectorx.toVector(in);
	String[] S = new String[V.size()];
	V.copyInto(S);
	return S;
}
/**
 * Next text line. Cast output to String.
 * Creation date: (07/30/99 14:47:25)
 * @return java.lang.Object
 */
public Object nextElement() {
	String output = textLine;

	try { textLine = in.readLine(); }
	catch (IOException e) { textLine = null; }

	return output;
}
}
