/*
 * Termite.java
 *
 * Created on 25 September 2007, 10:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package TermiteInstances;

/**
 *  interface allows more than one termite to be impemented if necessary
 * @author ug87sjs
 */
public interface Termite {
    
    public int state = 0;
    
    /**
     *  uses information from the termite and space around it to update the state of the termtite
     */
    public boolean updateState();
    

  
}


