/*
 * CementBlock.java
 *
 * Created on 25 September 2007, 11:19
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
public class CementBlock implements Block{
    
    private int xPos = 0;
    private int yPos = 0;
    private int zPos = 0;
    
    private int velX = 0;
    private int velY = 0;
    private int velZ = 0;
    private double windStrength = 0;
//    private double yWindStrength = 0;
//    private double zWindStrength = 0;
//
    private double pheromoneLost = 0.0;
    
    private double queenPheromone = 0.0;
    private double cementPheromone = 0.0;
    private double trialPheromone = 0.0;
    
    private double windCementPheromone = 0.0;
    private double windQueenPheromone = 0.0;
    
    private double percentageOfPher = 0.0;
    
    
    private Block [] neighbours = null;
    
    private int blockState = 2;
    
    private Color col = Color.RED;
    
    
    /** Creates a new instance of CementBlock */
    public CementBlock(double percentageOfPher, double cementPheromone) {
        this.percentageOfPher = percentageOfPher;
        this.cementPheromone = cementPheromone;
    }
    
    public Block [] upateState(){
        return emission();
    }
    
    /**
     *  emits a percentage of the phermone to surrounding blocks
     *  removes the emited pheromone from the total
     *  returns any that is emited into a non-AirBlock
     */
    private Block [] emission(){
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
        if (cementPheromone > 6){
            double pherEmitted = cementPheromone*percentageOfPher;
            //System.out.println(pherEmitted);
            n[n.length-1].addPheromoneLost(-pherEmitted);
            //System.out.println(n.length);
            double sharedPher = pherEmitted/(n.length-1);
            //System.out.println("pher: "+sharedPher);
            for (int i = 0; i < n.length-1; i++) {
                if (neighbours[i].getBlockState() == 0){
                    n[i].addCementPheromone(sharedPher);
                }else{
                    n[n.length-1].addPheromoneLost(sharedPher);
                }
            }
        }
        //System.out.println(n[n.length-1].getPheromoneLost());
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
        
        boolean xEmptyFlag = false;
        boolean yEmptyFlag = false;
        boolean zEmptyFlag = false;
        
        for (int i = 0; i < neighbours.length; i++) {
            Block b = neighbours[i];
            if(b.getXPos() != -1){
                if( (b.getXPos()+b.getVelX() == this.getXPos())&&(b.getYPos()+b.getVelY() == this.getYPos())&&(b.getZPos()+b.getVelZ() == this.getZPos())){
                    //System.out.println("boo");
//                    xWind = xWind+b.getXWindStrength()*b.getVelX();
//                    yWind = yWind+b.getYWindStrength()*b.getVelY();
//                    zWind = zWind+b.getZWindStrength()*b.getVelZ();
//                    if(b.getVelX() != 0) xcounter++;
//                    if(b.getVelY() != 0) ycounter++;
//                    if(b.getVelZ() != 0) zcounter++;
                    if(b.getVelX() != 0 && b.getVelY() == 0 && b.getVelZ() == 0){
                        tempVelX = tempVelX-b.getVelX();
                        tempVelY = tempVelY+b.getVelY();
                        tempVelZ = tempVelZ+b.getVelZ();
                    }else if(b.getVelX() == 0 && b.getVelY() != 0 && b.getVelZ() == 0){
                        tempVelX = tempVelX+b.getVelX();
                        tempVelY = tempVelY-b.getVelY();
                        tempVelZ = tempVelZ+b.getVelZ();
                    }else if(b.getVelX() == 0 && b.getVelY() == 0 && b.getVelZ() != 0){
                        tempVelX = tempVelX+b.getVelX();
                        tempVelY = tempVelY+b.getVelY();
                        tempVelZ = tempVelZ-b.getVelZ();
                    }else if(b.getVelX() == 0 && b.getVelY() != 0 && b.getVelZ() != 0){
                        // find if neighbours are blocking flow
                        for (int j = 0; j < neighbours.length; j++) {
                            if(neighbours[j].getXPos() == b.getXPos() && neighbours[j].getYPos() == b.getYPos() && neighbours[j].getZPos() == b.getZPos()+b.getVelZ()){
                                if(neighbours[j].getBlockState() == 0){
                                    zEmptyFlag = true;
                                }
                            }
                            if(neighbours[j].getXPos() == b.getXPos() && neighbours[j].getYPos() == b.getYPos()+b.getVelY() && neighbours[j].getZPos() == b.getZPos()){
                                if(neighbours[j].getBlockState() == 0){
                                    yEmptyFlag = true;
                                }
                            }
                        }
                        // both empty so inverse one with a prob
                        if(zEmptyFlag && yEmptyFlag){
                            if(Math.random() < 0.5){
                                tempVelX = tempVelX+b.getVelX();
                                tempVelY = tempVelY+b.getVelY();
                                tempVelZ = tempVelZ-b.getVelZ();
                            }else{
                                tempVelX = tempVelX+b.getVelX();
                                tempVelY = tempVelY-b.getVelY();
                                tempVelZ = tempVelZ+b.getVelZ();
                            }
                            // inverse the blocked space
                        }else if(zEmptyFlag && !yEmptyFlag){
                            tempVelX = tempVelX+b.getVelX();
                            tempVelY = tempVelY-b.getVelY();
                            tempVelZ = tempVelZ+b.getVelZ();
                        }else if(!zEmptyFlag && yEmptyFlag){
                            tempVelX = tempVelX+b.getVelX();
                            tempVelY = tempVelY+b.getVelY();
                            tempVelZ = tempVelZ-b.getVelZ();
                        }else{
                            tempVelX = tempVelX+b.getVelX();
                            tempVelY = tempVelY-b.getVelY();
                            tempVelZ = tempVelZ-b.getVelZ();
                        }
                    }else if(b.getVelX() != 0 && b.getVelY() != 0 && b.getVelZ() == 0){
                        // find if neighbours are blocking flow
                        for (int j = 0; j < neighbours.length; j++) {
                            if(neighbours[j].getXPos() == b.getXPos()+b.getVelX() && neighbours[j].getYPos() == b.getYPos() && neighbours[j].getZPos() == b.getZPos()){
                                if(neighbours[j].getBlockState() == 0){
                                    xEmptyFlag = true;
                                }
                            }
                            if(neighbours[j].getXPos() == b.getXPos() && neighbours[j].getYPos() == b.getYPos()+b.getVelY() && neighbours[j].getZPos() == b.getZPos()){
                                if(neighbours[j].getBlockState() == 0){
                                    yEmptyFlag = true;
                                }
                            }
                        }
                        // both empty so inverse one with a prob
                        if(xEmptyFlag && yEmptyFlag){
                            if(Math.random() < 0.5){
                                tempVelX = tempVelX-b.getVelX();
                                tempVelY = tempVelY+b.getVelY();
                                tempVelZ = tempVelZ+b.getVelZ();
                            }else{
                                tempVelX = tempVelX+b.getVelX();
                                tempVelY = tempVelY-b.getVelY();
                                tempVelZ = tempVelZ+b.getVelZ();
                            }
                            // inverse the blocked space
                        }else if(xEmptyFlag && !yEmptyFlag){
                            tempVelX = tempVelX+b.getVelX();
                            tempVelY = tempVelY-b.getVelY();
                            tempVelZ = tempVelZ+b.getVelZ();
                        }else if(!xEmptyFlag && yEmptyFlag){
                            tempVelX = tempVelX-b.getVelX();
                            tempVelY = tempVelY+b.getVelY();
                            tempVelZ = tempVelZ+b.getVelZ();
                        }else{
                            tempVelX = tempVelX-b.getVelX();
                            tempVelY = tempVelY-b.getVelY();
                            tempVelZ = tempVelZ+b.getVelZ();
                        }
                    }else if(b.getVelX() != 0 && b.getVelY() == 0 && b.getVelZ() != 0){
                        //System.out.println("hello");
                        // find if neighbours are blocking flow
                        for (int j = 0; j < neighbours.length; j++) {
                            if(neighbours[j].getXPos() == b.getXPos()+b.getVelX() && neighbours[j].getYPos() == b.getYPos() && neighbours[j].getZPos() == b.getZPos()){
                                if(neighbours[j].getBlockState() == 0){
                                    xEmptyFlag = true;
                                }
                            }
                            if(neighbours[j].getXPos() == b.getXPos() && neighbours[j].getYPos() == b.getYPos() && neighbours[j].getZPos() == b.getZPos()+b.getVelZ()){
                                if(neighbours[j].getBlockState() == 0){
                                    zEmptyFlag = true;
                                }
                            }
                        }
                        // both empty so inverse one with a prob
                        if(xEmptyFlag && zEmptyFlag){
                            if(Math.random() < 0.5){
                                tempVelX = tempVelX-b.getVelX();
                                tempVelY = tempVelY+b.getVelY();
                                tempVelZ = tempVelZ+b.getVelZ();
                            }else{
                                tempVelX = tempVelX+b.getVelX();
                                tempVelY = tempVelY+b.getVelY();
                                tempVelZ = tempVelZ-b.getVelZ();
                            }
                            // inverse the blocked space
                        }else if(xEmptyFlag && !zEmptyFlag){
                            tempVelX = tempVelX+b.getVelX();
                            tempVelY = tempVelY+b.getVelY();
                            tempVelZ = tempVelZ-b.getVelZ();
                        }else if(!xEmptyFlag && zEmptyFlag){
                            tempVelX = tempVelX-b.getVelX();
                            tempVelY = tempVelY+b.getVelY();
                            tempVelZ = tempVelZ+b.getVelZ();
                        }else{
                            tempVelX = tempVelX-b.getVelX();
                            tempVelY = tempVelY+b.getVelY();
                            tempVelZ = tempVelZ-b.getVelZ();
                        }
                    }else if(b.getVelX() != 0 && b.getVelY() != 0 && b.getVelZ() != 0){
                        for (int j = 0; j < neighbours.length; j++) {
                            if(neighbours[j].getXPos() == b.getXPos()+b.getVelX() && neighbours[j].getYPos() == b.getYPos() && neighbours[j].getZPos() == b.getZPos()){
                                if(neighbours[j].getBlockState() == 0){
                                    xEmptyFlag = true;
                                }
                            }
                            if(neighbours[j].getXPos() == b.getXPos() && neighbours[j].getYPos() == b.getYPos() && neighbours[j].getZPos() == b.getZPos()+b.getVelZ()){
                                if(neighbours[j].getBlockState() == 0){
                                    zEmptyFlag = true;
                                }
                            }
                            if(neighbours[j].getXPos() == b.getXPos() && neighbours[j].getYPos() == b.getYPos()+b.getVelY() && neighbours[j].getZPos() == b.getZPos()){
                                if(neighbours[j].getBlockState() == 0){
                                    yEmptyFlag = true;
                                }
                            }
                        }
                        if(xEmptyFlag && yEmptyFlag && zEmptyFlag){
                            double r = Math.random();
                            if(r > 0.66){
                                tempVelX = tempVelX-b.getVelX();
                                tempVelY = tempVelY+b.getVelY();
                                tempVelZ = tempVelZ+b.getVelZ();
                            }else if(r > 0.33){
                                tempVelX = tempVelX+b.getVelX();
                                tempVelY = tempVelY-b.getVelY();
                                tempVelZ = tempVelZ+b.getVelZ();
                            }else{
                                tempVelX = tempVelX+b.getVelX();
                                tempVelY = tempVelY+b.getVelY();
                                tempVelZ = tempVelZ-b.getVelZ();
                            }
                        }else{
                            if(!xEmptyFlag){
                                tempVelX = tempVelX-b.getVelX();
                            }else{
                                tempVelX = tempVelX+b.getVelX();
                            }
                            xcounter++;
                            if(!yEmptyFlag){
                                tempVelY = tempVelY-b.getVelY();
                            }else{
                                tempVelY = tempVelY+b.getVelY();
                            }
                            ycounter++;
                            if(!zEmptyFlag){
                                tempVelZ = tempVelZ-b.getVelZ();
                            }else{
                                tempVelZ = tempVelZ+b.getVelZ();
                            }
                            zcounter++;
                        }
                    }
                    n[n.length-1].addCementPheromone(b.getWindCementPheromone());
                    n[n.length-1].addQueenPheromone(b.getWindQueenPheromone());
                    n[i].addCementPheromone(-b.getWindCementPheromone());
                    n[i].addQueenPheromone(-b.getWindQueenPheromone());
                }
                
            }
        }
        
        //System.out.println("xwind: "+xWind+","+xcounter);
        //System.out.println("ywind: "+yWind+","+ycounter);
        //System.out.println("zWind: "+zWind+","+zcounter);
        
        //if(xcounter != 0 ) n[n.length-1].setXWindStrength(Math.abs(xWind));
        //if(ycounter != 0 ) n[n.length-1].setYWindStrength(Math.abs(yWind));
        //if(zcounter != 0 ) n[n.length-1].setZWindStrength(Math.abs(zWind));
        //System.out.println("new cement wind strength: "+n[n.length-1].getWindStrength());
        
        //System.out.println("tempX: "+tempVelX);
        //System.out.println("tempY: "+tempVelY);
        //System.out.println("tempZ: "+tempVelZ);
        
        
        n = updateVelocities(n, tempVelX, tempVelY, tempVelZ);
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
            double r = Math.random();
            if(r < 0.1){
                r = Math.random();
                if(r<0.5) n[n.length-1].setVelX(1);
                else n[n.length-1].setVelX(-1);
            }else{
                n[n.length-1].setVelX(0);
            }
        }
        if(tempVelY > 0){
            n[n.length-1].setVelY(1);
        }else if(tempVelY <0){
            n[n.length-1].setVelY(-1);
        }else{
            double r = Math.random();
            if(r < 0.1){
                r = Math.random();
                if(r<0.5) n[n.length-1].setVelY(1);
                else n[n.length-1].setVelY(-1);
            }else{
                n[n.length-1].setVelY(0);
            }
        }
        if(tempVelZ > 0){
            n[n.length-1].setVelZ(1);
        }else if(tempVelZ <0){
            n[n.length-1].setVelZ(-1);
        }else{
            double r = Math.random();
            if(r < 0.1){
                r = Math.random();
                if(r<0.5) n[n.length-1].setVelZ(1);
                else n[n.length-1].setVelZ(-1);
            }else{
                n[n.length-1].setVelZ(0);
            }
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
    
    public double getCementPheromone(){
        return cementPheromone;
    }
    
    public void setCementPheromone(double cementPheromone){
        this.cementPheromone = this.cementPheromone;
    }
    
    public void setPercentageOfPher(double per){
        this.percentageOfPher = per;
    }
    
    public double getQueenPheromone() {
        return queenPheromone;
    }
    
    public Color getColor(){
        return col;
    }
    
    public void addQueenPheromone(double queenPheromone) {
        this.queenPheromone = this.queenPheromone+queenPheromone;
    }
    
    public void addCementPheromone(double cementPheromone) {
        this.cementPheromone = this.cementPheromone+cementPheromone;
    }
    
    public void removeQueenPheromone(double queenPheromone){
        
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
        return windQueenPheromone*getWindStrength();
    }
    
    public double getWindCementPheromone(){
        return windCementPheromone*getWindStrength();
    }
    
    public void setWindCementPheromone(double p){
        if(p > 0){
            this.windCementPheromone = p;
        }else{
            this.windCementPheromone = 0;
        }
    }
    
    public void setWindQueenPheromone(double p){
        if(p > 0){
            this.windQueenPheromone = p;
        }else{
            this.windQueenPheromone = 0;
        }
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
    
    public void addPheromoneLost(double l){
        //this.pheromoneLost = pheromoneLost +l;
    }
    
    public double getPheromoneLost(){
        return pheromoneLost;
    }
    
    public static void main(String[] args) {
        CementBlock c = new CementBlock(0.5,7);
        AirBlock a1 = new AirBlock(0,0,0,0);
        AirBlock a2 = new AirBlock(0,0,0,0);
        Block [] n  = {a1,a2};
        c.setNeighbours(n);
        n = c.emission();
        System.out.println("emission: "+n[0].getCementPheromone()+","+n[1].getCementPheromone()+","+n[2].getCementPheromone());
        
    }
}
