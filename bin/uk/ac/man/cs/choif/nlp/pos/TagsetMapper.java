package uk.ac.man.cs.choif.nlp.pos;

import uk.ac.man.cs.choif.extend.*;
import java.util.Hashtable;
/**
 * Maps a tagset to another.
 * Creation date: (07/23/99 07:45:23)
 * @author: Freddy Choi
 */
public class TagsetMapper {
	private Hashtable map = new Hashtable();
/**
 * TagsetMapper constructor comment.
 */
public TagsetMapper() {
	super();
}
/**
 * Declare a mapping between all the symbols in src (space separated) to dest
 * Creation date: (07/23/99 07:17:49)
 * @param src java.lang.String Space separated tags
 * @param dest java.lang.String Tag to map to
 */
public void declareMap(String src, String dest) {
	String[] source = Vectorx.toStringArray(Stringx.tokenize(src, " "));
	for (int i=source.length; i-->0;) map.put(source[i], dest);
}
/**
 * Map the given tag to another according to how this map is set up.
 * Creation date: (07/23/99 07:48:33)
 * @return java.lang.String
 * @param tag java.lang.String
 */
public String map(String tag) {
	String mtag = (String) map.get(tag);
	return (mtag == null ? tag : mtag);
}
/**
 * Map a list of tags.
 * Creation date: (07/23/99 07:49:36)
 * @return java.lang.String[]
 * @param tags java.lang.String[]
 */
public String[] map(String[] tags) {
	String[] output = new String[tags.length];
	String mtag;
	for (int i=output.length; i-->0;) {
		mtag = (String) map.get(tags[i]);
		output[i] = (mtag == null ? tags[i] : mtag);
	}
	return output;
}
}
