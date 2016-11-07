package uk.ac.man.cs.choif.nlp.pos;

/**
 * 
 * Creation date: (09/08/99 20:39:38)
 * @author: Freddy Choi
 */
public interface PosTaggerClientI {
/**
 * Given a sentence, tag each word and return the pos tags.
 * Creation date: (09/08/99 20:29:16)
 * @return java.lang.String[]
 * @param sentence java.lang.String[]
 */
String[] tag(String[] sentence);
}
