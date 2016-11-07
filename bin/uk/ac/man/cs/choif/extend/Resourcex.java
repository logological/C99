package uk.ac.man.cs.choif.extend;

/**
 * Central place for defining external resources
 * Creation date: (09/23/99 16:28:37)
 * @author: Freddy Choi
 */
public class Resourcex {
	/** User's home directory */
	public static String home = "/root/";
	//public static String home = "/home/M97/mphil/choif/";

	/** Host address of all RMI processes */
	public final static java.net.URL rmiHost = RMIx.ermintrude;

	/** Home directory of resource files */
	public static String base = home + "work/resources/";
	public static String Stopword_defaultResourceFile = base + "STOPWORD.list";
	public static String OxfordAdvanced_defaultResourceFile = base + "OxfordAdvanced.dat";
	public static String PosTagger_defaultResourceFile = base + "QTAG.tagset";
	public static String Compound_defaultResourceFile = base + "Compound.list";
	public static String Heading_defaultResourceFile = base + "Heading.dat";
	public static String Pattern_defaultResourceFile = base + "Pattern.junk";
}
