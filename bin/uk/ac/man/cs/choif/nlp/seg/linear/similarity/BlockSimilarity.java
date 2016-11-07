package uk.ac.man.cs.choif.nlp.seg.linear.similarity;

/**
 * 
 * Creation date: (08/20/99 17:56:03)
 * @author: Freddy Choi
 */
public interface BlockSimilarity {
/**
 * The number of dots in the area
 * Creation date: (09/01/99 03:57:57)
 * @return float
 * @param i int
 * @param j int
 */
float dot(int i, int j);
/**
 * Return the number of features examined to compute similarity.
 * Creation date: (09/01/99 02:59:50)
 * @return int
 * @param i int
 * @param j int
 */
int insideArea(int i, int j);
/**
 * Given the the number of the blocks, i and j, compute the similarity.
 * Creation date: (08/20/99 17:56:21)
 * @return float
 * @param i int
 * @param j int
 */
float similarity(int i, int j);
}
