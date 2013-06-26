/*
 * Block.java
 *
 * Created on 25 September 2007, 11:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package TermiteProjectMain;
import java.awt.*;
/**
 *
 * @author ug87sjs
 */
public interface Block {
    
    Block [] neighbours = null;
    
    double queenPheromone = 0.0;
    double cementPheromone = 0.0;
    double trialPheromone = 0.0;
    double pheromoneLost = 0.0;
    
    int xPos = 0;
    int yPos = 0;
    int zPos = 0;
    
    int velX = 1;
    int velY = 0;
    int velZ = 0;
    
    double windStrength = 0;
    //double yWindStrength = 0;
    //double zWindStrength = 0;
    
    int blockState = 0;
    
    Block [] upateState();
    
    Color col = null;
    
    public Block [] updateWind();
    
    public Color getColor();
    
    public int getBlockState();
    
    public void setBlockState(int blockState);
    
    public Block[] getNeighbours();
    
    public void setNeighbours(Block[] neighbours);
    
    public double getQueenPheromone() ;
    
    public void addQueenPheromone(double queenPheromone) ;
    
    public double getCementPheromone() ;
    
    public void addCementPheromone(double cementPheromone) ;
    
    public void removeQueenPheromone(double queenPheromone);
    
    public void removeCementPheromone(double cementPheromone);
    
    public double getTrialPheromone() ;
    
    public void addTrialPheromone(double trialPheromone) ;
    
    public int getXPos();
    
    public void setXPos(int xPos);
    
    public int getYPos();
    
    public void setYPos(int yPos);
    
    public int getZPos();
    
    public void setZPos(int zPos);
    
    public int getVelX();
    
    public int getVelY();
    
    public int getVelZ();
    
    public double getWindStrength();
    
    public void setVelX(int velX);
    
    public void setVelY(int velY);
    
    public void setVelZ(int velZ);
    
    public void setWindStrength(double windStrength);
    
    public double getWindQueenPheromone();
    
    public double getWindCementPheromone();
    
//    public double getXWindStrength();
//    
//    public double getYWindStrength();
//    
//    public double getZWindStrength();
    
//    public void setXWindStrength(double xWind);
//    
//    public void setYWindStrength(double yWind);
//    
//    public void setZWindStrength(double zWind);
    
    public void setWindCementPheromone(double p);
    
    public void setWindQueenPheromone(double p);
    
    public void addPheromoneLost(double l);
    
    public double getPheromoneLost();
    
    
}
