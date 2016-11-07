package uk.ac.man.cs.choif.nlp.seg.linear;

import uk.ac.man.cs.choif.nlp.doc.*;
import uk.ac.man.cs.choif.extend.structure.Set;
import uk.ac.man.cs.choif.nlp.location.*;
import java.util.Vector;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.nlp.pos.*;
/**
 * An implementation of McKeown's segmenter
 * Creation date: (08/16/99 06:32:29)
 * @author: Freddy Choi
 */
public class JSegmenter {
/**
 * JSegmenter constructor comment.
 */
public JSegmenter() {
	super();
}
/**
 * 
 * Creation date: (08/16/99 06:56:26)
 * @param v float[]
 * @param s uk.ac.man.cs.choif.nlp.doc.Sentence[]
 * @param out java.io.PrintStream
 */
private static void genOutput(float[] v, Sentence[] s, java.io.PrintStream out) {
	String text;
	String[] token;
	for (int i=0, ie=s.length; i<ie; i++) {
		if (v[i+1] != 0 || i==0) out.println("==========");
		text = "";
		token = s[i].token;
		for (int j=0, je=token.length; j<je; j++) text += (token[j] + " ");
		out.println(text.trim());
	}
	out.println("==========");
}
/**
 * First argument, tagset, second argument, adaptive or fixed distance model
 * Creation date: (08/16/99 06:35:18)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	/* Print header */
	Debugx.header("This is JSegmenter, a Java implementation of Min-Yen Kan, Judith L. Klavans and Kathleen R. McKeown Segmenter algorithm. The original fixed distance approach to chain breaking has been replaced by an adaptive distance model.");

	Argx arg = new Argx(args);
	boolean adaptive = arg.has("--adaptive", false, "Use adaptive rather than fixed distance model for chain breaking");
	arg.displayHelp();
	
	/* Load data */
	Set options = new Set();
	options.put(Document.DISTANCE);
	Document doc = new Document(System.in, options);

	/* Compute boundaries */
	EntityMap map = new EntityMap(doc, adaptive);

	/* Print output */
	genOutput(map.localMaxima, doc.sentence, System.out);

}
}
