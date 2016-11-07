package uk.ac.man.cs.choif.nlp.seg.linear.eval;

import java.io.*;
import uk.ac.man.cs.choif.extend.*;
import uk.ac.man.cs.choif.extend.io.*;
import uk.ac.man.cs.choif.extend.sort.*;
import java.util.Vector;

/**
 * Significance test for a set of data sets
 * Creation date: (02/07/00 23:20:24)
 * @author: Freddy Choi
 */
public class Significance {
	private static class SampleSet implements Comparator {
		public String range;
		public int experiment;
		public File file;

		public SampleSet () {}
		public SampleSet (final File f) {
			file = f;
			Vector v = Stringx.tokenize(f.getName(),".");
			range = (String) v.elementAt(0);
			experiment = Integer.valueOf((String) v.elementAt(1)).intValue();
		}
		public int doCompare (Object a, Object b) {
			final SampleSet s1 = (SampleSet) a;
			final SampleSet s2 = (SampleSet) b;
			final int stringComp = s1.range.compareTo(s2.range);

			if (stringComp != 0) return stringComp;
			if (s1.experiment > s2.experiment) return greater;
			if (s1.experiment < s2.experiment) return less;
			return equal;
		}
		public String toString() { return file.getName(); }
	}
		
/**
 * Given a directory of sample files, compute a table
 * of inter-file differences and significance
 * Creation date: (02/07/00 03:06:19)
 * @param dir java.io.File
 * @param out java.io.File
 */
public final static void compareData(final File dir, final File outFile) {
	LineOutput out = new LineOutput(outFile);

	// Load data
	Debugx.msg("Loading data...");
	File[] file = IOx.fileList(dir);
	SampleSet[] sample = new SampleSet[file.length];
	for (int i=file.length; i-->0;) sample[i] = new SampleSet(file[i]);
	file = null;
	Arrayx.sort(sample, new SampleSet());
	
	float[][] data = new float[sample.length][];
	for (int i=sample.length; i-->0;) data[i] = NRCx.readData(sample[i].file);

	NRCx.floatR m = new NRCx.floatR();
	NRCx.floatR v = new NRCx.floatR();
	NRCx.floatR d = new NRCx.floatR();
	NRCx.floatR prob = new NRCx.floatR();
	for (int i=0; i<sample.length; i++) {
		for (int j=i; j<sample.length; j++) {
			if (sample[i].range.equals(sample[j].range)) {
				Debugx.msg("Analysing", sample[i].file.getName() + " <-> " + sample[j].file.getName());
				out.println("====================");
				out.println("data1 : " + sample[i].file.getName());
				out.println("data2 : " + sample[j].file.getName());
				NRCx.avevar(data[i], m, v);
				out.println("data1 (m,v)    : " + m + " " + v);
				NRCx.avevar(data[j], m, v);
				out.println("data2 (m,v)    : " + m + " " + v);
				NRCx.ftest(data[i], data[j], d, prob);
				out.println("F-test (f,p)   : " + d + " " + prob);
				NRCx.tutest(data[i], data[j], d, prob);
				out.println("T-test (t,p)   : " + d + " " + prob);
				NRCx.kstwo(data[i], data[j], d, prob);
				out.println("KS-test (ks,p) : " + d + " " + prob);
			}
		}
	}
}
/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void main(java.lang.String[] args) {
	/* Print header */
	Debugx.header("This is JSignificance, measures the statistical significance of a set of sample data. It computes a table of significance values for every pair of sample sets.");
	
	/* Get parameters */
	Argx arg = new Argx(args);
	String in = arg.get("-i", "Not Specified", "The directory of sample data files");
	String out = arg.get("-o", "Not Specified", "A file of results");
	arg.displayHelp("-i -o");

	/* Load documents */
	compareData(new File(in), new File(out));
	
	System.exit(0);
}
}
