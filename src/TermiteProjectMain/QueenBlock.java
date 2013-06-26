/*
 * QueenBlock.java
 *
 * Created on 26 September 2007, 14:57
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
public class QueenBlock implements Block {
    
    private int xPos = 0;
    private int yPos = 0;
    private int zPos = 0;
    
    private int velX = 0;
    private int velY = 0;
    private int velZ = 0;
    private double windStrength = 0;
//    private double yWindStrength = 0;
//    private double zWindStrength = 0;
    
    private double queenPheromone = 0.0;
    private double cementPheromone = 0.0;
    private double trialPheromone = 0.0;
    private double pheromoneLost = 0.0;
    private double windCementPheromone = 0.0;
    private double windQueenPheromone = 0.0;
    
    public void addPheromoneLost(double l){
        //this.pheromoneLost = pheromoneLost +l;
    }
    
    public double getPheromoneLost(){
        return pheromoneLost;
    }
    
    private Block [] neighbours = null;
    
    private int blockState = 3;
    
    private Color col = Color.YELLOW;
    
    /** Creates a new instance of QueenBlock */
    public QueenBlock(double qp, int x, int y, int z) {
        this.queenPheromone = qp;
        this.xPos = x;
        this.yPos = y;
        this.zPos = z;
    }
    
    public Block [] upateState(){
        //System.out.println("boooooooooo");
        //System.out.println(neighbours.length);
        return emission();
    }
    
    /**
     *  emits a fixed amount of phermone into the neohgbouring blocks
     *  only into AirBlocks
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
        double sharedPheromone = queenPheromone/n.length;
        
        for (int i = 0; i < n.length-1; i++) {
            if (neighbours[i].getBlockState() == 0){
                n[i].addQueenPheromone(sharedPheromone);
                
                //System.out.println(i);
                //System.out.println("emited"+ n[i].getQueenPheromone());
                 
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
    
    public double getQueenPheromone(){
        return queenPheromone;
    }
    
    public Color getColor(){
        return col;
    }
    
    public void addQueenPheromone(double queenPheromone) {
        this.queenPheromone = this.queenPheromone+queenPheromone;
    }
    
    public void setQueenPheromone(double queenPheromone){
        this.queenPheromone = this.queenPheromone;
    }
    
    public double getCementPheromone() {
        return cementPheromone;
    }
    
    public void addCementPheromone(double cementPheromone) {
        this.cementPheromone = this.cementPheromone+cementPheromone;
    }
    
    public void removeQueenPheromone(double queenPheromone){
        
    }
    
    public void removeCementPheromone(double cementPheromone){
        
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
    
    
    public static void main(String[] args) {
        QueenBlock q = new QueenBlock(13,1,1,1);
        // 2 1
        Block a1 = new AirBlock(0,0,0,0);
        a1.setXPos(2);
        a1.setYPos(1);
        a1.setZPos(1);
        a1.setVelX(1);
        a1.setVelY(0);
        a1.setVelZ(0);
        a1.setWindStrength(1.0);
        // 0 0
        Block a2 = new AirBlock(0,0,0,0);
        a2.setXPos(0);
        a2.setYPos(1);
        a2.setZPos(0);
        a2.setVelX(1);
        a2.setVelY(0);
        a2.setVelZ(1);
        a2.setWindStrength(0.5);
        // 1 0
        Block a3 = new AirBlock(0,0,0,0);
        //Block a3 = new EarthBlock();
        a3.setXPos(1);
        a3.setYPos(1);
        a3.setZPos(0);
        a3.setVelX(0);
        a3.setVelY(0);
        a3.setVelZ(0);
        a3.setWindStrength(1.0);
        // 2 0
        Block a4 = new AirBlock(0,0,0,0);
        a4.setXPos(2);
        a4.setYPos(1);
        a4.setZPos(0);
        a4.setVelX(0);
        a4.setVelY(0);
        a4.setVelZ(1);
        a4.setWindStrength(1.0);
        // 1 2
        Block a5 = new AirBlock(0,0,0,0);
        a5.setXPos(1);
        a5.setYPos(1);
        a5.setZPos(2);
        a5.setVelX(0);
        a5.setVelY(0);
        a5.setVelZ(1);
        a5.setWindStrength(1.0);
        // 0 2
        Block a6 = new AirBlock(0,0,0,0);
        a6.setXPos(0);
        a6.setYPos(1);
        a6.setZPos(2);
        a6.setVelX(1);
        a6.setVelY(0);
        a6.setVelZ(0);
        a6.setWindStrength(1.0);
        // 0 1
        Block a7 = new AirBlock(0,0,0,0);
        //Block a7 = new EarthBlock();
        a7.setXPos(0);
        a7.setYPos(1);
        a7.setZPos(1);
        a7.setVelX(1);
        a7.setVelY(0);
        a7.setVelZ(0);
        a7.setWindStrength(1.0);
        // 2 2
        Block a8 = new AirBlock(0,0,0,0);
        a8.setXPos(2);
        a8.setYPos(1);
        a8.setZPos(2);
        a8.setVelX(-1);
        a8.setVelY(0);
        a8.setVelZ(0);
        a8.setWindStrength(1.0);
        
        
        Block [] n = {a1,a2,a3,a4,a5,a6,a6,a7,a8};
        q.setNeighbours(n);
        EarthBlock e1 = new EarthBlock();
        Block [] n2 = {e1};
       // n2 = q.updateWind(n2);
        System.out.println("simple collision 1: "+n2[0].getVelX()+","+n2[0].getVelY()+","+n2[0].getVelZ()+","+n2[0].getWindStrength());
        
    }
    
    
    
}
