package uk.ac.man.cs.choif.extend;

import uk.ac.man.cs.choif.extend.sort.StringAsc;
import java.util.*;
/**
 * Command line argument processing
 * Creation date: (09/08/99 21:09:58)
 * @author: Freddy Choi
 */
public class Argx {
	private Hashtable args = new Hashtable();
	private Hashtable defs = new Hashtable(); // Maps a flag to a definition
/**
 * Take command line arguments for processing
 * Creation date: (09/08/99 21:11:19)
 * @param args java.lang.String[]
 */
public Argx(String[] args) {
	int i=0;
	while (i<args.length) {
		if (args[i].startsWith("-")) {
			if (i+1 < args.length && !args[i+1].startsWith("-")) {
				this.args.put(args[i].toLowerCase(), args[i+1]);
				i++;
			}
			else this.args.put(args[i].toLowerCase(), new Boolean(true));
		}
		i++;
	}

	// Default flags
//	has("-h", false, "Display help for program parameters.");
	has("--help", false, "Display help for program parameters.");
}
/**
 * 
 * Creation date: (09/12/99 04:33:34)
 * @param flag java.lang.String
 * @param def java.lang.String
 * @param type java.lang.String
 * @param defVal java.lang.String
 */
private void declareFlag(String flag, String def, String type, String defVal) {
	defs.put(flag, new String(def + " (Type: " + type + ", Default: " + defVal + ")."));
}
/**
 * Print help message
 * Creation date: (09/12/99 04:43:06)
 */
public void displayHelp() {
	if (args.containsKey("-h") || args.containsKey("--help")) {
		Debugx.msg("Help for command Arguments");
		parameterHelp(Hashtablex.keysToVector(defs), Debugx.output);
		System.exit(1);
	}
}
/**
 * If the user didn't specify all the flags required, then display help message
 * Creation date: (09/12/99 04:52:15)
 * @param flags java.lang.String A list of required flags (space separated).
 */
public void displayHelp(String flags) {
	// Identify missing arguments
	Vector v = Stringx.tokenize(flags, " ");
	Vector missed = new Vector();
	for (int i=v.size(); i-->0;) {
		if (!args.containsKey(((String) v.elementAt(i)).toLowerCase())) {
			missed.addElement(v.elementAt(i));
		}
	}

	// Display help
	if (missed.size() > 0) {
		Debugx.msg("Missing " + missed.size() + " program parameters");
		parameterHelp(missed, Debugx.output);
		System.exit(1);
	}
}
/**
 * Get a parameter
 * Creation date: (09/08/99 21:13:41)
 * @return double
 * @param flag java.lang.String
 * @param defaultVal double
 */
public double get(String flag, double defaultVal, String def) {
	declareFlag(flag, def, "double", new Double(defaultVal).toString());
	String param = (String) args.get(flag.toLowerCase());
	if (param == null) return defaultVal;
	return Double.valueOf(param).doubleValue();
}
/**
 * Get a parameter
 * Creation date: (09/08/99 21:13:41)
 * @return float
 * @param flag java.lang.String
 * @param defaultVal float
 */
public float get(String flag, float defaultVal, String def) {
	declareFlag(flag, def, "float", new Float(defaultVal).toString());
	String param = (String) args.get(flag.toLowerCase());
	if (param == null) return defaultVal;
	return Float.valueOf(param).floatValue();
}
/**
 * Get a parameter
 * Creation date: (09/08/99 21:13:41)
 * @return int
 * @param flag java.lang.String
 * @param defaultVal int
 */
public int get(String flag, int defaultVal, String def) {
	declareFlag(flag, def, "int", new Integer(defaultVal).toString());
	String param = (String) args.get(flag.toLowerCase());
	if (param == null) return defaultVal;
	return Integer.valueOf(param).intValue();
}
/**
 * Get a parameter
 * Creation date: (09/08/99 21:13:41)
 * @return long
 * @param flag java.lang.String
 * @param defaultVal float
 */
public long get(String flag, long defaultVal, String def) {
	declareFlag(flag, def, "long", new Long(defaultVal).toString());
	String param = (String) args.get(flag.toLowerCase());
	if (param == null) return defaultVal;
	return Long.valueOf(param).longValue();
}
/**
 * Get a parameter
 * Creation date: (09/08/99 21:13:41)
 * @return java.lang.String
 * @param flag java.lang.String
 * @param defaultVal java.lang.String
 */
public String get(String flag, String defaultVal, String def) {
	declareFlag(flag, def, "String", defaultVal);
	String param = (String) args.get(flag.toLowerCase());
	if (param == null) return defaultVal;
	return param;
}
/**
 * Get a parameter
 * Creation date: (09/08/99 21:13:41)
 * @return boolean
 * @param flag java.lang.String
 * @param defaultVal boolean
 */
public boolean get(String flag, boolean defaultVal, String def) {
	declareFlag(flag, def, "boolean", new Boolean(defaultVal).toString());
	String param = (String) args.get(flag.toLowerCase());
	if (param == null) return defaultVal;
	return Boolean.valueOf(param).booleanValue();
}
/**
 * Did the user supply a particular flag?
 * Creation date: (09/12/99 04:30:00)
 * @return boolean
 * @param flag java.lang.String
 * @param defaultVal boolean
 * @param def java.lang.String
 */
public boolean has(String flag, boolean defaultVal, String def) {
	declareFlag(flag, def, "n/a", (defaultVal ? "Set" : "Not set"));
	if (args.containsKey(flag)) return true;
	else return defaultVal;
}
/**
 * 
 * Creation date: (09/12/99 04:59:50)
 * @param flag java.lang.String
 * @param out LineOutputForked
 */
private void parameterHelp(Vector flags, uk.ac.man.cs.choif.extend.io.LineOutputForked out) {
	Vector v = Vectorx.sort(flags, new StringAsc());
	String flag, message;
	for (int i=0, ie=v.size(); i<ie; i++) {
		flag = (String) v.elementAt(i);
		message = (String) defs.get(flag);
		Debugx.msg("  "+flag+"\t"+message);
	}
}
}
