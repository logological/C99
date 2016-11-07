package uk.ac.man.cs.choif.nlp.location;

import java.util.*;
import uk.ac.man.cs.choif.extend.structure.*;
import uk.ac.man.cs.choif.nlp.statistics.distribution.*;
import uk.ac.man.cs.choif.nlp.dictionary.*;
/**
 * Distance model for estimating the average minimum distance between objects.
 * Creation date: (08/15/99 06:52:08)
 * @author: Freddy Choi
 */
public class DistanceModel {
	/** Stores the previous occurrence of objects */
	public Hashtable prev = new Hashtable();

	/** Stores the distance model for object pairs */
	private Hashtable model = new Hashtable();
/**
 * DistanceModel constructor comment.
 */
public DistanceModel() {
	super();
}
/**
 * Get the distance model for a pair of objects. Returns null
 * if model does not exist.
 * Creation date: (08/15/99 08:06:45)
 * @return uk.ac.man.cs.choif.nlp.statistics.distribution.AccInt
 * @param first java.lang.Object
 * @param second java.lang.Object
 */
public AccInt getModel(Object first, Object second) {
	PairObj pair = new PairObj(first, second);
	return (AccInt) model.get(pair);
}
/**
 * Observed an object at a particular location. This method
 * assumes the user is going through the object sequence in
 * ascending order.
 * Creation date: (08/15/99 07:58:14)
 * @param obj java.lang.Object
 * @param loc int
 */
public void observed(Object obj, int loc) {
	/* Accumulate distance information */
	Object object;	// A known object
	int location;	// The last location of a known object
	PairObj pair;	// A pair of objects
	AccInt acc; // Accumulator for the distance model of a pair of objects
	for (Enumeration e=prev.keys(); e.hasMoreElements();) {
		object = e.nextElement();
		location = ((Integer) prev.get(object)).intValue();
		pair = new PairObj(object, obj);

		/* Get model for the pair */
		acc = (AccInt) model.get(pair);
		if (acc == null) {
			acc = new AccInt();
			model.put(pair, acc);
		}

		/* Update model parameters */
		acc.acc(loc - location);
	}

	/* Update known object list */
	prev.put(obj, new Integer(loc));
}
/**
 * Print model to output stream
 * Creation date: (08/15/99 08:13:21)
 * @param out java.io.PrintStream
 */
public void print(java.io.PrintStream out) {
	uk.ac.man.cs.choif.extend.Hashtablex.print(model, out);
}
}
