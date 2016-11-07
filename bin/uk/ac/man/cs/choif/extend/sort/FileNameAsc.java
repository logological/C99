package uk.ac.man.cs.choif.extend.sort;

import java.io.File;
/**
 * Ascending filenames
 * Creation date: (02/07/00 03:09:36)
 * @author: Freddy Choi
 */
public class FileNameAsc implements Comparator {
/**
 * FileNameAsc constructor comment.
 */
public FileNameAsc() {
	super();
}
/**
 * doCompare method comment.
 */
public int doCompare(Object arg1, Object arg2) {
	final File f1 = (File) arg1;
	final File f2 = (File) arg2;
	return f1.getName().compareTo(f2.getName());
}
}
