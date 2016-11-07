package uk.ac.man.cs.choif.nlp.pos;

import java.rmi.*;
/**
 * Interface for the POS tagger server.
 * Creation date: (09/08/99 20:28:00)
 * @author: Freddy Choi
 */
public interface PosTaggerServerI extends Remote {
/**
 * Given a sentence, tag each word and return the pos tags.
 * Creation date: (09/08/99 20:29:16)
 * @return java.lang.String[]
 * @param sentence java.lang.String[]
 */
String[] tag(String[] sentence) throws RemoteException;
}
