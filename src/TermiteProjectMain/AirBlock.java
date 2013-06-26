/*
 * AirBlock.java
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
public class AirBlock implements Block {
    
    private int xPos = -1;
    private int yPos = -1;
    private int zPos = -1;
    
    private int velX = 1;
    private int velY = 0;
    private int velZ = 0;
    
    private double windStrength = 0;
    private double yWindStrength = 0;
    private double zWindStrength = 0;
    
    private double queenPheromone = 0.0;
    private double cementPheromone = 0.0;
    private double trialPheromone = 0.0;
    private double pheromoneLost = 0.0;
    private double percentageQPDiff = 0.0;
    private double percentageQPEvap = 0.0;
    
    private double percentageCPDiff = 0.0;
    private double percentageCPEvap = 0.0;
    
    private Block [] neighbours = null;
    
    private int blockState = 0;
    
    private Color col = Color.WHITE;
    
    /** Creates a new instance of AirBlock */
    public AirBlock(double qpDiffRate, double qpEvapRate, double cpDiffRate, double cpEvapRate) {
        percentageQPDiff = qpDiffRate;
        percentageQPEvap = qpEvapRate;
        percentageCPDiff = cpDiffRate;
        percentageCPEvap = cpEvapRate;
    }
    
    public Block [] upateState(){
        
        // which comes first??? does it matter?
        Block [] n = diffusion();
        //System.out.println("updated in air block: "+n[0]);
        n = evaporation(n);
        return n;
    }
    
    
    
    /**
     *  spreads the contained pheromones to neighbouring blocks
     *  returning any that are diffused into occupied blocks
     */
    private Block [] diffusion(){
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

        n = diffuseQueen(n);
        n = diffuseCement(n);
        //n = updateWind(n);
        return n;
    }
    
    /**
     *  diffuse pheromone by gradient strength
     */
    public Block [] diffuseQueen(Block [] n){
        double diffusingPher = this.getQueenPheromone()*this.getPercentageQPDiff();
        //System.out.println("diffusing pher: "+diffusingPher);
        double totalGradLost = 0.0;
        double [] gradients = new double[n.length-1];
        if (queenPheromone > 0){
            for (int i = 0; i < gradients.length; i++) {
                if (neighbours[i].getBlockState() == 0){
                    double gradient = this.getQueenPheromone() - neighbours[i].getQueenPheromone();
                    //System.out.println("grad: "+gradient);
                    if(gradient > 0){
                        gradients[i] = gradient;
                        totalGradLost = totalGradLost+gradient;
                    }else{
                        gradients[i] = -1;
                    }
                }
            }
        }
        
        for (int i = 0; i < gradients.length; i++) {
            gradients[i] = gradients[i]/totalGradLost;
            if(gradients[i] > 0){
                n[i].addQueenPheromone(gradients[i]*diffusingPher);
            }
        }
        
        
        //this.removeQueenPheromone(totalGradLost);
        n[n.length-1].addQueenPheromone(-diffusingPher);
        //System.out.println("diff: "+this.queenPheromone+","+diffusingPher);
        return n;
    }
    
    /**
     *  diffuse pheromone by gradient strength
     */
    public Block [] diffuseCement(Block [] n){
        double diffusingPher = this.getCementPheromone()*this.getPercentageCPDiff();
        double totalGradLost = 0.0;
        double [] gradients = new double[n.length-1];
        if (cementPheromone > 0){
            for (int i = 0; i < gradients.length; i++) {
                if (neighbours[i].getBlockState() == 0){
                    double gradient = this.getCementPheromone() - neighbours[i].getCementPheromone();
                    if(gradient > 0){
                        gradients[i] = gradient;
                        totalGradLost = totalGradLost+gradient;
                    }else{
                        gradients[i] = -1;
                    }
                }
            }
        }
        
        for (int i = 0; i < gradients.length; i++) {
            gradients[i] = gradients[i]/totalGradLost;
            if(gradients[i] > 0){
                n[i].addCementPheromone(gradients[i]*diffusingPher);
            }
        }
        n[n.length-1].addCementPheromone(-diffusingPher);
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
        int windcounter = 0;
        
        for (int i = 0; i < n.length-1; i++) {
            Block b = neighbours[i];
            //System.out.println("boo "+b.getXPos()+","+b.getVelX()+","+b.getBlockState());
            if( b.getXPos() != -1){
                //System.out.println("boo "+b.getXPos()+","+b.getVelX()+","+b.getBlockState());
                if( (b.getXPos()+b.getVelX() == this.getXPos())&&(b.getYPos()+b.getVelY() == this.getYPos())&&(b.getZPos()+b.getVelZ() == this.getZPos())){
                    
                    tempVelX = tempVelX+b.getVelX();
                    tempVelY = tempVelY+b.getVelY();
                    tempVelZ = tempVelZ+b.getVelZ();
                    
//                    xWind = xWind+b.getXWindStrength()*b.getVelX();
//                    yWind = yWind+b.getYWindStrength()*b.getVelY();
//                    zWind = zWind+b.getZWindStrength()*b.getVelZ();
                    //System.out.println("hello");
                    
                    //windcounter++;
                    // add transported pheromones
                    //System.out.println("transfered: "+b.getWindQueenPheromone()+","+b.getWindStrength()+","+b.getQueenPheromone());
                    n[n.length-1].addCementPheromone(b.getWindCementPheromone());
                    n[n.length-1].addQueenPheromone(b.getWindQueenPheromone());
                    n[i].addCementPheromone(-b.getWindCementPheromone());
                    n[i].addQueenPheromone(-b.getWindQueenPheromone());
                    
                    //System.out.println(b.getBlockState());
                }
//                if(b.getVelX() != this.getVelX() && b.getVelX() != 0) xcounter++;
//                if(b.getVelY() != this.getVelY()&& b.getVelY() != 0) ycounter++;
//                if(b.getVelZ() != this.getVelZ()&& b.getVelZ() != 0) zcounter++;
            }
        }
        
        //System.out.println(xWind);
        //System.out.println(windcounter);
        //System.out.println("currentStrength: "+this.getWindStrength());
        //if(windcounter != 0 ) n[n.length-1].setXWindStrength(Math.abs(xWind));
        //if(windcounter != 0 ) n[n.length-1].setYWindStrength(Math.abs(yWind));
        //if(windcounter != 0 ) n[n.length-1].setZWindStrength(Math.abs(zWind));
        //System.out.println("new air wind strength: "+n[n.length-1].getWindStrength());
        // use average velocities to calculate update
        //System.out.println("Air Collisions: "+tempVelX+","+tempVelY+","+tempVelZ);
        n = xVelocity(tempVelX, xcounter, n);
        n = yVelocity(tempVelY, ycounter, n);
        n = zVelocity(tempVelZ, zcounter, n);
        
        // keeps a constant flow accross x plane
        if(this.getXPos() == 0) n[n.length-1].setVelX(1);
        if(this.getXPos() == 19){
            n[n.length-1].addCementPheromone(-this.getWindCementPheromone());
            n[n.length-1].addQueenPheromone(-this.getWindQueenPheromone());
        }
        
        return n;
    }
    
    /**
     *  removes a proportion of the contained pheromones
     */
    private Block [] evaporation(Block [] n){
        //System.out.println("evap: "+((queenPheromone-(queenPheromone*this.getPercentageQPDiff()))*getPercentageQPEvap()));
        n[n.length-1].addCementPheromone(-((cementPheromone-(cementPheromone*this.getPercentageCPDiff()))*getPercentageCPEvap()));
        n[n.length-1].addQueenPheromone(-((queenPheromone-(queenPheromone*this.getPercentageQPDiff()))*getPercentageQPEvap()));
        return n;
    }
    
    /**
     *  performs collision resolution of velocity component
     */
    public Block [] xVelocity(int tempVelX, double xcounter, Block [] n){
        //System.out.println(xcounter/n.length);
        if(tempVelX > 0){
            //System.out.println("tmep");
            
//            if(Math.random() < xcounter/n.length){
//                n[n.length-1].setVelX(0);
//            }else{
            n[n.length-1].setVelX(1);
//            }
        }else if(tempVelX <0){
//            if(Math.random() < xcounter/n.length){
//                n[n.length-1].setVelX(0);
//            }else{
            n[n.length-1].setVelX(-1);
//            }
        }else{
            n[n.length-1].setVelX(0);
        }
        return n;
    }
    
    /**
     *  performs collision resolution of velocity component
     */
    public Block [] yVelocity(int tempVelY, double ycounter, Block [] n){
        if(tempVelY > 0){
//            if(Math.random() < ycounter/n.length){
//                n[n.length-1].setVelY(0);
//            }else{
            n[n.length-1].setVelY(1);
//            }
        }else if(tempVelY <0){
//            if(Math.random() < ycounter/n.length){
//                n[n.length-1].setVelY(0);
//            }else{
            n[n.length-1].setVelY(-1);
//            }
        }else{
            n[n.length-1].setVelY(0);
        }
        return n;
    }
    
    /**
     *  performs collision resolution of velocity component
     */
    public Block [] zVelocity(int tempVelZ, double zcounter, Block [] n){
        if(tempVelZ > 0){
//            if(Math.random() < zcounter/n.length){
//                n[n.length-1].setVelZ(0);
//            }else{
            n[n.length-1].setVelZ(1);
//            }
        }else if(tempVelZ <0){
//            if(Math.random() < zcounter/n.length){
//                n[n.length-1].setVelZ(0);
//            }else{
            n[n.length-1].setVelZ(-1);
//            }
        }else{
            n[n.length-1].setVelZ(0);
        }
        return n;
    }
    
    public double getQueenPheromone() {
        return queenPheromone;
    }
    
    public void addQueenPheromone(double queenPheromone) {
        if(this.queenPheromone+queenPheromone < 26.0){
            this.queenPheromone = this.queenPheromone+queenPheromone;
        }
    }
    
    public void removeQueenPheromone(double queenPheromone){
        this.queenPheromone = this.queenPheromone - queenPheromone;
        if (this.queenPheromone < 0){
            this.queenPheromone = 0;
        }
    }
    
    public double getCementPheromone() {
        return cementPheromone;
    }
    
    public void addCementPheromone(double cementPheromone) {
        if(this.cementPheromone+cementPheromone < 26.0){
            this.cementPheromone = this.cementPheromone+cementPheromone;
        }
        
    }
    
    public void removeCementPheromone(double cementPheromone){
        this.cementPheromone = this.cementPheromone - cementPheromone;
        if( this.cementPheromone <0){
            this.cementPheromone = 0;
        }
    }
    
    public double getTrialPheromone() {
        return trialPheromone;
    }
    
    public void addTrialPheromone(double trialPheromone) {
        this.trialPheromone = this.trialPheromone+trialPheromone;
    }
    
    public int getBlockState(){
        return blockState;
    }
    
    public Color getColor(){
        return col;
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
    
    public double getPercentageQPDiff() {
        return percentageQPDiff;
    }
    
    public void setPercentageQPDiff(double percentageQPDiff) {
        this.percentageQPDiff = percentageQPDiff;
    }
    
    public double getPercentageQPEvap() {
        return percentageQPEvap;
    }
    
    public void setPercentageQPEvap(double percentageQPEvap) {
        this.percentageQPEvap = percentageQPEvap;
    }
    
    public double getPercentageCPDiff() {
        return percentageCPDiff;
    }
    
    public void setPercentageCPDiff(double percentageCPDiff) {
        this.percentageCPDiff = percentageCPDiff;
    }
    
    public double getPercentageCPEvap() {
        return percentageCPEvap;
    }
    
    public void setPercentageCPEvap(double percentageCPEvap) {
        this.percentageCPEvap = percentageCPEvap;
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
//System.out.println("setting: "+windStrength);
        this.windStrength = windStrength;
        
//        this.yWindStrength = windStrength;
//        this.zWindStrength = windStrength;
        //System.out.println("x: "+xWindStrength);
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
    
//    public void setXWindStrength(double xWind){
//        this.xWindStrength = xWind;
//    }
    
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
        //this.pheromoneLost = pheromoneLost +l;
    }
    
    public double getPheromoneLost(){
        return pheromoneLost;
    }
    
    
    public static void main(String[] args) {
        
        
    }
}
