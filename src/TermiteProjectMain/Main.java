/*
 * Main.java
 *
 * Created on 25 September 2007, 10:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package TermiteProjectMain;
import TermiteGraphics.TermiteFrame;
import javax.swing.Timer;
/**
 *
 * @author ug87sjs
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // instance of interface with predefined variables
        TermiteFrame t = new TermiteFrame(new TermiteWorld(
                // x dimension
                20,
                // y dimension
                20,
                // z dim
                20,
                // amount of queen pheromone in a queen block, controls rate of emission
                26,
                // amount of cement pheromone in a cement block, needs to last for equivalent of two minutes
                20,
                // rate at which cement pheromone is lost in the cement block
                0.01,
                // rate at which qp diffuses
                0.5,
                // rate at which qp evaporates
                0.2,
                // rate at which cp diffuses
                0.5,
                // rate at which cp evaporates
                0.1,
                // number of termites
                160,
                // number of moves
                1,
                // size of queen
                4,
                // cement pheromone threshold
                0.5,
                // upper bound of qp threshold for building
                0.7,
                // lower bound of qp threshold for building
                0.5,
                // queen pheromone threshold
                13.0,
                // probability of picking up material
                0.50,
                // probability of placing material
                0.50,
                //
                0.2
                ),
                // number of runs
                200);
        while(true){
            t.run();
        }
        /*
         
         
        // experimenting with different components 
        double queenDiff = 0.1;
        double queenEvap = 0.1;
        double queenEmis = 20.0;
        
        double cementDiff = 0.1;
        double cementEvap = 0.1;
        double cementEmis = 0.1;
        */
//        int num = 20;
//        
//        //double prob = 0.1;
//        for (int i = 0; i < 6; i++) {
//            t.getSBP().setNumberOfTermites(num);
//            t.reset();
//            for (int j = 0; j < 10; j++) {
//                t.run();
//                t.reset();
//            }
//            t.saveToFile();
//            num = num*2;
//        }
        
      
//        for (int i = 0; i < 10; i++) {
//            t.getSBP().setPlacingProb(prob);
//            t.reset();
//            for (int j = 0; j < 10; j++) {
//                t.run();
//                t.reset();
//            }
//            t.saveToFile();
//            prob = prob+0.1;
//        }
        /*
        queenDiff = 0.5;
        t.getSBP().setQueenDiffusion(queenDiff);
        for (int i = 0; i < 9; i++) {
            t.getSBP().setQueenEvaporation(queenEvap);
            t.reset();
            for (int j = 0; j < 10; j++) {
                t.run();
                t.reset();
            }
            t.saveToFile();
            queenEvap = queenEvap+0.1;
        }
        
        queenEvap = 0.1;
        t.getSBP().setQueenEvaporation(queenEvap);
        for (int i = 0; i < 10; i++) {
            t.getSBP().setQueenEmission(queenEmis);
            t.reset();
            for (int j = 0; j < 10; j++) {
                t.run();
                t.reset();
            }
            t.saveToFile();
            queenEmis = queenEmis+1.0;
        }
        
        
        
        for (int i = 0; i < 9; i++) {
            t.getSBP().setCementDiffusion(cementDiff);
            t.reset();
            for (int j = 0; j < 20; j++) {
                t.run();
                t.reset();
            }
            t.saveToFile();
            cementDiff = cementDiff+0.1;
        }
        
        cementDiff = 0.5;
        t.getSBP().setCementDiffusion(cementDiff);
        for (int i = 0; i < 9; i++) {
            t.getSBP().setCementEvaporation(cementEvap);
            t.reset();
            for (int j = 0; j < 20; j++) {
                t.run();
                t.reset();
            }
            t.saveToFile();
            cementEvap = cementEvap+0.1;
        }
        
        cementEvap = 0.1;
        //t.getSBP().setCementEvaporation(cementEvap);
        for (int i = 0; i < 9; i++) {
            t.getSBP().setCementEmission(cementEmis);
            t.reset();
            for (int j = 0; j < 20; j++) {
                t.run();
                t.reset();
            }
            t.saveToFile();
            cementEmis = cementEmis+0.1;
        }
        
        
        
        /*
        int num = 20;
        double lower = 0.1;
        double upper = 0.3;
        for (int i = 0; i < 10; i++) {
            t.getSBP().setLowerQueenBound(lower);
            t.getSBP().setUpperQueenBound(upper);
            t.reset();
            for (int j = 0; j < 10; j++) {
                t.run();
                t.reset();
            }
            t.saveToFile();
            lower = lower+0.1;
            upper = upper+0.1;
        }
         
        upper = 1.0;
        lower = 0.1;
         
        for (int i = 0; i < 9; i++) {
            t.getSBP().setLowerQueenBound(lower);
            t.getSBP().setUpperQueenBound(upper);
            t.reset();
            for (int j = 0; j < 10; j++) {
                t.run();
                t.reset();
            }
            t.saveToFile();
            lower = lower+0.1;
        }
         
        upper = 0.2;
        lower = 0.1;
         
        for (int i = 0; i < 9; i++) {
            t.getSBP().setLowerQueenBound(lower);
            t.getSBP().setUpperQueenBound(upper);
            t.reset();
            for (int j = 0; j < 10; j++) {
                t.run();
                t.reset();
            }
            t.saveToFile();
         
            upper = upper+0.1;
        }
         */
        
    }
    
}



