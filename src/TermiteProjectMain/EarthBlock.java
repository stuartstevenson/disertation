/*
 * EarthBlock.java
 *
 * Created on 25 September 2007, 11:10
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
public class EarthBlock implements Block {
    
    private int xPos = -1;
    private int yPos = -1;
    private int zPos = -1;
    
    private int velX = 0;
    private int velY = 0;
    private int velZ = 0;
    private double windStrength = 0;
    private double yWindStrength = 0;
    private double zWindStrength = 0;
    
    private double queenPheromone = 0.0;
    private double cementPheromone = 0.0;
    private double trialPheromone = 0.0;
    
    private double pheromoneLost = 0.0;
    
    private Block [] neighbours = null;
    
    private int blockState = 1;
    
    private Color col = Color.GREEN;
    
    // private double windPheromone = 0.0;
    
    /** Creates a new instance of EarthBlock */
    public EarthBlock() {
    }
    
    public Block [] upateState(){
        Block [] n = new Block[neighbours.length+1];
        for (int i = 0; i < n.length; i++) {
            if(i == n.length - 1){
                n[i] = new EarthBlock();
                n[i].setXPos(this.getXPos());
                n[i].setYPos(this.getYPos());
                n[i].setZPos(this.getZPos());
            }else{
                n[i] = new EarthBlock();
                n[i].setXPos(neighbours[i].getXPos());
                n[i].setYPos(neighbours[i].getYPos());
                n[i].setZPos(neighbours[i].getZPos());
            }
            
        }
        //n = updateWind(n);
        return n;
    }
    
    /**
     *  preforms propogation and collision detected amongst local neighoubourhood
     */
    public Block [] updateWind(){
        Block [] n = new Block[neighbours.length+1];
        for (int i = 0; i < n.length; i++) {
            if(i == n.length - 1){
                n[i] = new EarthBlock();
                n[i].setXPos(this.getXPos());
                n[i].setYPos(this.getYPos());
                n[i].setZPos(this.getZPos());
            }else{
                n[i] = new EarthBlock();
                n[i].setXPos(neighbours[i].getXPos());
                n[i].setYPos(neighbours[i].getYPos());
                n[i].setZPos(neighbours[i].getZPos());
            }
            
        }
        int tempVelX = 0;
        int tempVelY = 0;
        int tempVelZ = 0;
        
        int xcounter = 0;
        int ycounter = 0;
        int zcounter = 0;
        
        double xWind = 0.0;
        double yWind = 0.0;
        double zWind = 0.0;
        
        for (int i = 0; i < neighbours.length; i++) {
            Block b = neighbours[i];
            if( b.getXPos() != -1){
                //System.out.println("Velocitites: "+b.getVelY());
                if( (b.getXPos()+b.getVelX() == this.getXPos())&&(b.getYPos()+b.getVelY() == this.getYPos())&&(b.getZPos()+b.getVelZ() == this.getZPos())){
                    tempVelX = tempVelX+b.getVelX();
                    tempVelY = tempVelY-b.getVelY();
                    tempVelZ = tempVelZ+b.getVelZ();
                    
//                    if(b.getVelX() != 0) xcounter++;
//                    if(b.getVelY() != 0) ycounter++;
//                    if(b.getVelZ() != 0) zcounter++;
//
//                    xWind = xWind+b.getXWindStrength()*b.getVelX();
//                    yWind = yWind+b.getYWindStrength()*b.getVelY();
//                    zWind = zWind+b.getZWindStrength()*b.getVelZ();
                    
                    n[n.length-1].addCementPheromone(b.getWindCementPheromone());
                    n[n.length-1].addQueenPheromone(b.getWindQueenPheromone());
                    n[i].addCementPheromone(-b.getWindCementPheromone());
                    n[i].addQueenPheromone(-b.getWindQueenPheromone());
                    
                    //System.out.println(b.getBlockState());
                    //System.out.println("Earth Collisions: "+tempVelX+","+tempVelY+","+tempVelZ);
                }
                
            }
        }
        
        //if(xcounter != 0 ) n[n.length-1].setXWindStrength(Math.abs(xWind));
        //if(ycounter != 0 ) n[n.length-1].setYWindStrength(Math.abs(yWind));
        //if(zcounter != 0 ) n[n.length-1].setZWindStrength(Math.abs(zWind));
        //System.out.println("new earth wind strength: "+n[n.length-1].getWindStrength());
        
        // use average velocities to calculate update
        //if(tempVelY != 0) System.out.println("temp y: "+tempVelY);
        n = updateVelocities(n,tempVelX, tempVelY, tempVelZ);
        
        return n;
    }
    
    /**
     *  performs collision resolution of velocity components
     */
    public Block [] updateVelocities(Block [] n, int tempVelX, int tempVelY, int tempVelZ){
        if(tempVelX > 0){
            n[n.length-1].setVelX(1);
        }else if(tempVelX <0){
            n[n.length-1].setVelX(-1);
        }else{
            n[n.length-1].setVelX(0);
        }
        if(tempVelY > 0){
            n[n.length-1].setVelY(1);
        }else if(tempVelY <0){
            //System.out.println("Y inverted");
            n[n.length-1].setVelY(1);
        }else{
            n[n.length-1].setVelY(0);
        }
        if(tempVelZ > 0){
            n[n.length-1].setVelZ(1);
        }else if(tempVelZ <0){
            n[n.length-1].setVelZ(-1);
        }else{
            n[n.length-1].setVelZ(0);
        }
        
        return n;
    }
    
    public int getBlockState(){
        return blockState;
    }
    
    public void setBlockState(int blockState){
        this.blockState = blockState;
    }
    
    public Block[] getNeighbours(){
        return neighbours;
    }
    
    public void setNeighbours(Block[] neighbours){
        this.neighbours = neighbours;
    }
    
    public int getXPos() {
        return xPos;
    }
    
    public Color getColor(){
        return col;
    }
    
    public void setXPos(int xPos) {
        this.xPos = xPos;
    }
    
    public int getYPos() {
        return yPos;
    }
    
    public void setYPos(int yPos) {
        this.yPos = yPos;
    }
    
    public int getZPos() {
        return zPos;
    }
    
    public void setZPos(int zPos) {
        this.zPos = zPos;
    }
    
    public double getQueenPheromone(){
        /*
        if(queenPheromone>0){
        System.out.println("getQP: "+ queenPheromone);
        }*/
        return queenPheromone;
    }
    
    
    public void addQueenPheromone(double queenPheromone) {
        this.queenPheromone = this.queenPheromone+queenPheromone;
        //System.out.println(this.queenPheromone);
    }
    
    public void setQueenPheromone(double queenPheromone){
        this.queenPheromone = this.queenPheromone;
    }
    
    public double getCementPheromone() {
        return cementPheromone;
    }
    
    public void removeQueenPheromone(double queenPheromone){
        
    }
    
    public void removeCementPheromone(double cementPheromone){
        
    }
    
    public void addCementPheromone(double cementPheromone) {
        this.cementPheromone = this.cementPheromone+cementPheromone;
    }
    
    public double getTrialPheromone() {
        return trialPheromone;
    }
    
    public void addTrialPheromone(double trialPheromone) {
        this.trialPheromone = this.trialPheromone+trialPheromone;
    }
    public int getVelX() {
        return velX;
    }
    
    public void setVelX(int velX) {
        this.velX = velX;
    }
    
    public int getVelY() {
        return velY;
    }
    
    public void setVelY(int velY) {
        this.velY = velY;
    }
    
    public int getVelZ() {
        return velZ;
    }
    
    public void setVelZ(int velZ) {
        this.velZ = velZ;
    }
    
    public double getWindStrength() {
        double wind = 0.0;
        if(this.velX != 0 || this.velY != 0 || this.velZ != 0){
            wind = windStrength;
        }
//        if(wind != 0){
//            wind = (xWindStrength*Math.abs(velX))+(yWindStrength*Math.abs(velY))+(zWindStrength*Math.abs(velZ))/3;
//        }
        //System.out.println(wind);
        return wind;
    }
    
    public void setWindStrength(double windStrength) {
        this.windStrength = windStrength;
    }
    
    public double getWindQueenPheromone(){
        //System.out.println(windStrength);
        return queenPheromone*getWindStrength();
    }
    
    public double getWindCementPheromone(){
        return cementPheromone*getWindStrength();
    }
    
//    public double getXWindStrength(){
//        return this.xWindStrength;
//    }
//
//    public double getYWindStrength(){
//        return this.yWindStrength;
//    }
//
//    public double getZWindStrength(){
//        return this.zWindStrength;
//    }
//
//    public void setXWindStrength(double xWind){
//        this.xWindStrength = xWind;
//    }
//
//    public void setYWindStrength(double yWind){
//        this.yWindStrength = yWind;
//    }
//
//    public void setZWindStrength(double zWind){
//        this.zWindStrength = zWind;
//    }
    
    
    
    public void setWindCementPheromone(double p){
        //this.windCementPheromone = p;
    }
    
    public void setWindQueenPheromone(double p){
        //this.windQueenPheromone = p;
    }
    
    public void addPheromoneLost(double l){
        this.pheromoneLost = pheromoneLost +l;
    }
    
    public double getPheromoneLost(){
        return pheromoneLost;
    }
}
