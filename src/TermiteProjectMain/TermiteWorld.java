/*
 * TermiteWorld.java
 *
 * Created on 17 October 2007, 11:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package TermiteProjectMain;

import TermiteInstances.WorkerTermite;
import TermiteInstances.Termite;
/**
 *
 * @author ug87sjs
 */
public class TermiteWorld {
    
    private Block [][][] world = null;
    private WorkerTermite [] termites = null;
    
    private int xDim;
    private int yDim;
    private int zDim;
    private double qpLevel;
    private double cpLevel;
    private double cpLossRate;
    private double qpDiffRate;
    private double qpEvapRate;
    private double cpDiffRate;
    private double cpEvapRate;
    
    private int numberOfTermites;
    private int numOfMoves;
    private int queenSize;
    
    private double cementPheromoneThreshold = 0.0;
    private double maxQueenPheremoneThreshold = 0.0;
    private double lowerQueenPheremoneThresholdMin = 0.0;
    private double lowerQueenPheremoneThresholdMax = 0.0;
    private double trialPheromoneThreshold = 0.0;
    
    private double probOfPlacingMaterial = 0.0;
    private double probOfPickingMaterial = 0.0;
    
    private double windStrength = 0.0;
    
    private int numberOfDeposits = 0;
    private int timeToFirstDeposit = 0;
    private int timeForMeanHeight = 0;
    private int timeForComplete = 0;
    
    private boolean flagForFirstDep = false;
    private boolean flagForMeanHeight = false;
    private boolean flagForComplete = false;
    
    /** Creates a new instance of TermiteWorld
     *
     *
     */
    public TermiteWorld(int x,
            int y ,
            int z,
            double qpLevel,
            double cpLevel,
            double cpLossRate,
            double qpDiffRate,
            double qpEvapRate,
            double cpDiffRate,
            double cpEvapRate,
            
            int numOfTermites,
            int numOfMoves,
            int queenSize,
            double cPT,
            double lQPTMx,
            double lQPTMn,
            double mQPT,
            double pickingProb,
            double placingProb,
            double windStrength
            ) {
        
        setWorld(new Block[x][y][z]);
        setXDim(x);
        setYDim(y);
        setZDim(z);
        this.setQpLevel(qpLevel);
        this.setCpLevel(cpLevel);
        this.setCpLossRate(cpLossRate);
        this.setQpDiffRate(qpDiffRate);
        this.setQpEvapRate(qpEvapRate);
        this.setCpDiffRate(cpDiffRate);
        this.setCpEvapRate(cpEvapRate);
        
        this.setCementPheromoneThreshold(cPT);
        this.setLowerQueenPheremoneThresholdMax(lQPTMx);
        this.setLowerQueenPheremoneThresholdMin(lQPTMn);
        this.setMaxQueenPheremoneThreshold(mQPT);
        this.setProbOfPickingMaterial(pickingProb);
        this.setProbOfPlacingMaterial(placingProb);
        
        this.windStrength = windStrength;
        
        this.queenSize = queenSize;
        
        this.numberOfTermites = numOfTermites;
        
        this.numOfMoves = numOfMoves;
        termites = new WorkerTermite[numberOfTermites];
        createWorld();
    }
    
    /**
     *
     *
     */
    public void update(int r){        
        if(r>10){
            updateTermites();
        }
        updateWind();
        updateWorld();

    }
    
    /** calls the updatewind methods for the components of the world
     *  
     */    
    public void updateWind(){
        Block [][][] newWorld = world;
        
        Block [][][][] neighbouringBlocks = new Block[xDim][yDim][zDim][27];
        
        for (int i = 0; i < getXDim(); i++) {
            for (int j = 0; j < getYDim(); j++) {
                for (int k = 0; k < getZDim(); k++) {
                    // takes a block
                    Block current = world[i][j][k];
                    //updates the block and recieves a neighbourhood of pheromone values
                    Block [] neighbours = current.updateWind();
                    neighbouringBlocks[i][j][k] = neighbours;

                }
            }
        }
        
        for (int i = 0; i < getXDim(); i++) {
            for (int j = 0; j < getYDim(); j++) {
                for (int k = 0; k < getZDim(); k++) {
                    // calls method to add values in neighbours to the newWorld
                    Block current = world[i][j][k];
                    Block [] neighbours = neighbouringBlocks[i][j][k];
                    if(neighbours != null && neighbours.length > 0){
                        newWorld = addWindNeighbourhood(newWorld,neighbours,current);
                    }
                }
            }
        }
        setWorld(newWorld);
    }
    
    /** calls the updateState methods for the components of the world
     *  
     */
    public void updateWorld(){
        
        if(!flagForFirstDep) this.timeToFirstDeposit++;
        if(!this.flagForMeanHeight) this.timeForMeanHeight++;
        if(!this.flagForComplete) this.timeForComplete++;
        // creates a new world to replce the old
        Block [][][] newWorld = world;
        
        Block [][][][] neighbouringBlocks = new Block[xDim][yDim][zDim][27];
        
        for (int i = 0; i < getXDim(); i++) {
            for (int j = 0; j < getYDim(); j++) {
                for (int k = 0; k < getZDim(); k++) {
                    // takes a block
                    Block current = world[i][j][k];
                    //updates the block and recieves a neighbourhood of pheromone values                    
                    Block [] neighbours = current.upateState();
                    neighbouringBlocks[i][j][k] = neighbours;
                }
            }
        }
        
        for (int i = 0; i < getXDim(); i++) {
            for (int j = 0; j < getYDim(); j++) {
                for (int k = 0; k < getZDim(); k++) {
                    // calls method to add values in neighbours to the newWorld
                    Block current = world[i][j][k];
                    Block [] neighbours = neighbouringBlocks[i][j][k];
                    if(neighbours.length > 0){
                        newWorld = addNeighbourhood(newWorld,neighbours,current);
                    }
                }
            }
        }
        setWorld(newWorld);        
        calcMeanHeight();
        calcIfComplete();
    }
    
    /**  updates the wind components of a block
     */
    public Block [][][] addWindNeighbourhood(Block [][][] newWorld, Block [] neighbours, Block current){
        int xPos;
        int yPos;
        int zPos;
        Block n;
        // goes through the neighbours array
        for (int i = 0; i < neighbours.length; i++) {
            n = neighbours[i];
            xPos = n.getXPos();
            yPos = n.getYPos();
            zPos = n.getZPos();
            // checks that the neighbour is a legal block
            
            if (((xPos >= 0 || yPos >= 0 || zPos >= 0) && (xPos < getXDim() || yPos < getYDim() || zPos <getZDim()))){
                // adds the pheromone from the neighbour block to the newWorld block
                if(world[xPos][yPos][zPos].getBlockState() < 2){
                    if(n.getCementPheromone() >= 0) {
                        newWorld[xPos][yPos][zPos].addCementPheromone(n.getCementPheromone());
                    }else{
                        newWorld[xPos][yPos][zPos].removeCementPheromone(-n.getCementPheromone());
                    }
                    if(n.getQueenPheromone() >= 0){
                        newWorld[xPos][yPos][zPos].addQueenPheromone(n.getQueenPheromone());
                    }else{
                        newWorld[xPos][yPos][zPos].removeQueenPheromone(-n.getQueenPheromone());
                    }
                }else{
                    newWorld[xPos][yPos][zPos].setWindCementPheromone(n.getCementPheromone());
                    newWorld[xPos][yPos][zPos].setWindQueenPheromone(n.getQueenPheromone());
                }
                
                if(xPos == current.getXPos() && yPos == current.getYPos() && zPos == current.getZPos()){
                    //newWorld[xPos][yPos][zPos].setWindStrength(n.getWindStrength());
                    newWorld[xPos][yPos][zPos].setVelX(n.getVelX());
                    newWorld[xPos][yPos][zPos].setVelY(n.getVelY());
                    newWorld[xPos][yPos][zPos].setVelZ(n.getVelZ());
                }
            }
            
        }
        xPos = current.getXPos();
        yPos = current.getYPos();
        zPos = current.getZPos();
        
        return newWorld;
    }
    
    /**  adds the values in the neighbours array to the newWorld variable
     *   it matches up the correct block and totals up the new values, added them into a temporary
     *   block or into the block itself
     *   the block itself takes the temp values and adds them to itself, then replaces the temp block
     */
    public Block [][][] addNeighbourhood(Block [][][] newWorld, Block [] neighbours, Block current){
        int xPos;
        int yPos;
        int zPos;
        Block n;
        // goes through the neighbours array
        for (int i = 0; i < neighbours.length; i++) {

            n = neighbours[i];
            xPos = n.getXPos();
            yPos = n.getYPos();
            zPos = n.getZPos();
            // checks that the neighbour is a legal block
            if (((xPos >= 0 || yPos >= 0 || zPos >= 0) && (xPos < getXDim() || yPos < getYDim() || zPos <getZDim()))){
                // adds the pheromone from the neighbour block to the newWorld block
                // if pheromone is negative then removes it

                if(n.getCementPheromone() >= 0) {
                    newWorld[xPos][yPos][zPos].addCementPheromone(n.getCementPheromone());
                }else{
                    newWorld[xPos][yPos][zPos].removeCementPheromone(-n.getCementPheromone());
                }
                if(n.getQueenPheromone() >= 0){
                    newWorld[xPos][yPos][zPos].addQueenPheromone(n.getQueenPheromone());
                }else{
                    newWorld[xPos][yPos][zPos].removeQueenPheromone(-n.getQueenPheromone());
                }
                // if the block is a cement block then needs to remove the pheromone emitted
                if(xPos == current.getXPos() && yPos == current.getYPos() && zPos == current.getZPos()){
                    if(world[xPos][yPos][zPos].getBlockState() == 2){
                        newWorld[xPos][yPos][zPos].removeCementPheromone(-n.getPheromoneLost());
                    }
                }
            }
            
        }
        xPos = current.getXPos();
        yPos = current.getYPos();
        zPos = current.getZPos();
        //newWorld[xPos][yPos][zPos] = current;
        
        return newWorld;
    }
    
    
    /** sets up the initial world
     *
     *
     */
    public void createWorld(){
        
        for (int i = 0; i < getXDim(); i++) {
            for (int j = 0; j < getYDim(); j++) {
                for (int k = 0; k < getZDim(); k++) {
                    // bottom layer as EarthBlocks
                    if(j == 0){
                        getWorld()[i][j][k] = new EarthBlock();
                        world[i][j][k].setXPos(i);
                        world[i][j][k].setYPos(j);
                        world[i][j][k].setZPos(k);
                    }else{
                        getWorld()[i][j][k] = new AirBlock( getQpDiffRate(), getQpEvapRate(), getCpDiffRate(), getCpEvapRate());
                        world[i][j][k].setXPos(i);
                        world[i][j][k].setYPos(j);
                        world[i][j][k].setZPos(k);
                    }
                    //world[i][j][k].setWindStrength(windStrength);
                }
            }
        }
        createQueen();
        //createQueenVertical();
        //createLQueen();
        
        setUpNeighbours();
        setWindStrengths();
        setUpTermites();
    }
    
    public void setWindStrengths(){
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                for (int k = 0; k < zDim; k++) {
                    world[i][j][k].setWindStrength(windStrength);
                }
            }
        }
        
    }
    
    /**
     *  sets location occupied by queen blocks
     */
    public void createQueen(){
        int x = this.xDim/2;
        int z = this.zDim/2;
        int y = 1;
        for (int i = 0; i < queenSize; i++) {
            getWorld()[x][y][z] = new QueenBlock(this.qpLevel,x,y,z);
            if(i > 0){
                getWorld()[x][y+1][z] = new QueenBlock(this.qpLevel,x,y+1,z);
                getWorld()[x+1][y][z] = new QueenBlock(this.qpLevel,x+1,y,z);
                getWorld()[x][y][z+i] = new QueenBlock(this.qpLevel,x,y,z+i);
                getWorld()[x][y][z-i] = new QueenBlock(this.qpLevel,x,y,z-i);
                getWorld()[x-1][y][z] = new QueenBlock(this.qpLevel,x-1,y,z);
                getWorld()[x][y+1][z+i] = new QueenBlock(this.qpLevel,x,y+1,z+i);
                getWorld()[x][y+1][z-i] = new QueenBlock(this.qpLevel,x,y+1,z-i);
                if(i>=1){
                    
                    getWorld()[x][y][z+i+1] = new QueenBlock(this.qpLevel,x,y,z+i+1);
                    getWorld()[x][y][(z-i)-1] = new QueenBlock(this.qpLevel,x,y,(z-i)-1);
                    getWorld()[x-1][y][z-i] = new QueenBlock(this.qpLevel,x-1,y,z-i);
                    getWorld()[x+1][y][z+i] = new QueenBlock(this.qpLevel,x+1,y,z+i);
                    getWorld()[x+1][y][z-i] = new QueenBlock(this.qpLevel,x+1,y,z-i);
                    getWorld()[x-1][y][z+i] = new QueenBlock(this.qpLevel,x-1,y,z+i);
                }
            }
        }
    }
    
    public void createQueenVertical(){
        int x = this.xDim/2;
        int z = this.zDim/2;
        int y = 1;
        for (int i = 0; i < queenSize; i++) {
            getWorld()[x][y][z] = new QueenBlock(this.qpLevel,x,y,z);
            getWorld()[x+1][y][z] = new QueenBlock(this.qpLevel,x+1,y,z);
            getWorld()[x][y][z+1] = new QueenBlock(this.qpLevel,x,y,z+1);
            getWorld()[x][y][z-1] = new QueenBlock(this.qpLevel,x,y,z-1);
            getWorld()[x-1][y][z] = new QueenBlock(this.qpLevel,x-1,y,z);
            if(i>0){
                getWorld()[x][y+i][z] = new QueenBlock(this.qpLevel,x,y+i,z);
                getWorld()[x+1][y+i][z] = new QueenBlock(this.qpLevel,x+1,y+i,z);
                getWorld()[x][y+i][z+1] = new QueenBlock(this.qpLevel,x,y+i,z+1);
                getWorld()[x][y+i][z-1] = new QueenBlock(this.qpLevel,x,y+i,z-1);
                getWorld()[x-1][y+i][z] = new QueenBlock(this.qpLevel,x-1,y+i,z);
            }
        }
    }
    
    public void createLQueen(){
        int x = 7;
        int z = 13;
        for (int i = 1; i < 3; i++) {
            //System.out.println(this.qpLevel);
            getWorld()[x][i][z] = new QueenBlock(this.qpLevel,x,i,z);
            getWorld()[x+1][i][z] = new QueenBlock(this.qpLevel,x+1,i,z);
            getWorld()[x+2][i][z] = new QueenBlock(this.qpLevel,x+2,i,z);
            getWorld()[x+3][i][z] = new QueenBlock(this.qpLevel,x+3,i,z);
            getWorld()[x+4][i][z] = new QueenBlock(this.qpLevel,x+4,i,z);
            getWorld()[x+5][i][z] = new QueenBlock(this.qpLevel,x+5,i,z);
            getWorld()[x+6][i][z] = new QueenBlock(this.qpLevel,x+6,i,z);
            getWorld()[x][i][z-1] = new QueenBlock(this.qpLevel,x,i,z-1);
            getWorld()[x][i][z-2] = new QueenBlock(this.qpLevel,x,i,z-2);
            getWorld()[x][i][z-3] = new QueenBlock(this.qpLevel,x,i,z-3);
            getWorld()[x][i][z-4] = new QueenBlock(this.qpLevel,x,i,z-4);
            getWorld()[x][i][z-5] = new QueenBlock(this.qpLevel,x,i,z-5);
            getWorld()[x][i][z-6] = new QueenBlock(this.qpLevel,x,i,z-6);
        }
    }
    
    public void setUpNeighbours(){
        for (int i = 0; i < this.getXDim(); i++) {
            for (int j = 0; j < this.getYDim(); j++) {
                for (int k = 0; k < this.getZDim(); k++) {
                    
                    createNeighbours(world[i][j][k]);
                }
            }
        }
    }
    
    /**
     *
     *
     */
    public void setUpTermites(){
        for (int i = 0; i < termites.length; i++) {
            termites[i] = setUpTermite();
        }
    }
    
    /**
     *  creates a new termite and assignes him a valid, random location
     *
     */
    public WorkerTermite setUpTermite(){
        WorkerTermite t = new WorkerTermite();
        t.setCementPheromoneThreshold(this.cementPheromoneThreshold);
        t.setLowerQueenPheremoneThresholdMax(this.lowerQueenPheremoneThresholdMax);
        t.setLowerQueenPheremoneThresholdMin(this.lowerQueenPheremoneThresholdMin);
        t.setMaxQueenPheremoneThreshold(this.maxQueenPheremoneThreshold);
        t.setProbOfPlacingMaterial(this.probOfPlacingMaterial);
        t.setProbOfPickingMaterial(this.probOfPickingMaterial);
        // don't want to be in same space as queen blocks
        int randomX = (int)(Math.random()*this.xDim);
        int randomZ = (int)(Math.random()*this.zDim);
        //System.out.println("random values: "+randomX);
        
        // starting at boundary
        if(Math.random() <0.5){
            randomX = (int)(Math.random()*this.xDim);
            if(Math.random()<0.5){
                randomZ = 0;
            }else{
                randomZ = zDim-1;
            }
        }else{
            if(Math.random() <0.5){
                randomX = 0;
            }else{
                randomX = xDim-1;
            }
            randomZ = (int)(Math.random()*this.zDim);
        }
        
        while(world[randomX][1][randomZ].getBlockState() == 3 || world[randomX][1][randomZ].getBlockState() == 2){
            randomX = (int)(Math.random()*this.xDim);
            randomZ = (int)(Math.random()*this.zDim);
            System.out.println("random values: "+randomX);
        }
        System.out.println("random values: "+randomX);
        t.setXPos(randomX);
        t.setYPos(1);
        t.setZPos(randomZ);
        
        
        return t;
    }
    
    /**
     *  iterates through each termite and updates synchronously
     */
    public void updateTermites(){
        //int r = (int)(Math.random()*this.numberOfTermites);
        boolean [] deposits = new boolean[termites.length];
        for (int i = 0; i < termites.length; i++) {
            WorkerTermite t = termites[i];
            Block b = null;
            Block [] neighbours = null;
            boolean deposited = false;
            deposits[i] = false;
            
            b = world[t.getXPos()][t.getYPos()][t.getZPos()];
            t.setCurrentLoc(b);
            neighbours = b.getNeighbours();
            t.setNeighbourhood(neighbours);
            //this.printNeighbours(neighbours);
            deposited = t.updateState();
            
            if(deposited){
                deposits[i] = true;
                numberOfDeposits++;
                this.flagForFirstDep = true;
            }            
            
            if(t.getCannotMove()){
                t = setUpTermite();
                termites[i] = t;
                b = world[t.getXPos()][t.getYPos()][t.getZPos()];
                t.setCurrentLoc(b);
                neighbours = b.getNeighbours();
                t.setNeighbourhood(neighbours);
                t.setCannotMove(false);
            }
        }
        
        for (int i = 0; i < deposits.length; i++) {
            if(deposits[i]) placeDeposit(termites[i]);
        }
        
    }
    
    private void placeDeposit(WorkerTermite t){
        world[t.getXPos()][t.getYPos()][t.getZPos()] = new CementBlock(this.cpLossRate,this.cpLevel);
        world[t.getXPos()][t.getYPos()][t.getZPos()].setXPos(t.getXPos());
        world[t.getXPos()][t.getYPos()][t.getZPos()].setYPos(t.getYPos());
        world[t.getXPos()][t.getYPos()][t.getZPos()].setZPos(t.getZPos());
        world[t.getXPos()][t.getYPos()][t.getZPos()].setWindStrength(this.windStrength);
        setUpNeighbours();
        //t = setUpTermite();
        //t.setCarryingMaterial(true);
    }
    
    /** sorts the 26 neighbours that surround a block into an array
     *  and then passes that to the block itself
     *
     */
    public void createNeighbours(Block b){
        
        Block [] n = new Block[26];
        int x = b.getXPos();
        int y = b.getYPos();
        int z = b.getZPos();
        // what does this do??
        int i = 0;
        // goes though each possible neighbour of b
        for (int j = -1; j <= 1; j++) {
            for (int k = -1; k <= 1; k++) {
                for (int l = -1; l <= 1; l++) {
                    
                    // doesn't add itself
                    if(!(j == 0 && k == 0 && l == 0)){
                        // cheaks the block is in the legal boudaries, if not just adds an air block to the edge

                        if(!(x+j < 0 || x+j >= this.xDim)
                        && !(y+k<0 || y+k >= this.yDim)
                        && !(z+l < 0 || z+l >= this.zDim)){
                            n[i] = getWorld()[x+j][y+k][z+l];
                            
                        }else{
                            n[i] = new AirBlock(getQpDiffRate(), getQpEvapRate(), getCpDiffRate(), getCpEvapRate());
                            
                        }
                        i++;
                        
                    }
                    
                }
            }
        }
        
        b.setNeighbours(n);
        
    }
    
    /**
     *  takes parameters from StartButtonPanel and its replaces its current vlaues with the new ones
     */
    public void updateParameters(double windStrength,
            double qPD, double qPEv, double qPEm,
            double cPD, double cPEv, double cPEm,
            double placingP, double pickingP,
            double uQP, double lQP, double mQP
            ){
        
        this.setWindStrength(windStrength);
        this.setQpDiffRate(qPD);
        this.setQpEvapRate(qPEv);
        this.setQpLevel(qPEm);
        this.setCpDiffRate(cPD);
        this.setCpEvapRate(cPEv);
        this.setCpLossRate(cPEm);
        
        this.setProbOfPlacingMaterial(placingP);
        this.setProbOfPickingMaterial(pickingP);
        this.setLowerQueenPheremoneThresholdMax(uQP);
        this.setLowerQueenPheremoneThresholdMin(lQP);
        this.setMaxQueenPheremoneThreshold(mQP);
        
        for (int i = 0; i < this.xDim; i++) {
            for (int j = 0; j < this.yDim; j++) {
                for (int k = 0; k < this.zDim; k++) {
                    Block b = world[i][j][k];
                    b.setWindStrength(this.getWindStrength());
                    //System.out.println("update: "+b.getWindStrength());
                    if(b.getBlockState() == 0){
                        AirBlock a = (AirBlock)b;
                        a.setPercentageCPDiff(this.getCpDiffRate());
                        a.setPercentageCPEvap(this.getCpEvapRate());
                        a.setPercentageQPDiff(this.getQpDiffRate());
                        a.setPercentageQPEvap(this.getQpEvapRate());
                    }else if(b.getBlockState() == 2){
                        CementBlock c = (CementBlock)b;
                        c.setPercentageOfPher(this.getCpLossRate());
                    }else if(b.getBlockState() == 3){
                        QueenBlock q = (QueenBlock)b;
                        q.setQueenPheromone(this.getQpLevel());
                    }
                    
                }
            }
        }
        
        // updates termites parameters from the new values
        for (int i = 0; i < termites.length; i++) {
            WorkerTermite t = termites[i];
            t.setLowerQueenPheremoneThresholdMax(this.getLowerQueenPheremoneThresholdMax());
            t.setLowerQueenPheremoneThresholdMin(this.getLowerQueenPheremoneThresholdMin());
            t.setMaxQueenPheremoneThreshold(this.getMaxQueenPheremoneThreshold());
            t.setProbOfPlacingMaterial(this.getProbOfPlacingMaterial());
            t.setProbOfPickingMaterial(this.getProbOfPickingMaterial());
        }
        
    }
    
    /**
     *  prints the bottom layer of the world
     */
    public void printWorld(){
        for (int i = 0; i < this.getXDim(); i++) {
            for (int j = 0; j < this.getYDim(); j++) {
                if(j == 1){
                    for (int k = 0; k < this.getZDim(); k++) {
                        System.out.print("Block: "+i+","+j+","+k+ ", QP: " + world[i][j][k].getQueenPheromone() +
                                ", CP: "+world[i][j][k].getCementPheromone()+", "+world[i][j][k].getBlockState()+". ");
                        //printNeighbours(world[i][j][k].getNeighbours());
                    }
                    System.out.println("");
                }}            
        }
    }
    
    public void printNeighbours(Block [] n){
        for (int i = 0; i < n.length; i++) {
            System.out.print("Neighbour: "+(i)+" ; "+n[i].getXPos()+","+n[i].getYPos()+","+n[i].getZPos()+" ");
            System.out.println("");
        }
    }
    
    public void printTermites(){
        for (int i = 0; i < termites.length; i++) {
            System.out.println("termite: "+i+", "+termites[i].getXPos()+", "+termites[i].getYPos()+", "+termites[i].getZPos());
        }
    }
    
    public int rateOfDeposition(){
        return numberOfDeposits;
    }
    
    public int timeToFirstDeposit(){
        return timeToFirstDeposit;
    }
    
    public boolean firstDepositMade(){
        return this.flagForFirstDep;
    }
    
    public int getTimeToMeanHeight(){
        return this.timeForMeanHeight;
    }
    
    public boolean getFlagForMeanHeight(){
        return this.flagForMeanHeight;
    }
    
    public int getTimeToComplete(){
        return this.timeForComplete;
    }
    
    public boolean getFlagForComplete(){
        return this.flagForComplete;
    }
    
    public void calcMeanHeight(){
        double mean = 0;
        double totalHeight = 0;
        int y = 1;
        
        for (int i = 0; i < this.xDim; i++) {
            for (int j = 0; j < this.zDim; j++) {
                if(world[i][y][j].getBlockState() == 2){
                    mean++;
                    while(world[i][y][j].getBlockState() == 2){
                        y++;
                        totalHeight++;
                    }
                    y = 1;
                }
            }
        }
        if(mean > 0 && totalHeight/mean  > 3){
            this.flagForMeanHeight = true;
        }
    }
    
    public void calcIfComplete(){
        int j = 1;
        int y = 1;
        for (int i = 0; i < this.xDim; i++) {
            for (int k = 0; k < this.zDim; k++) {
                if(world[i][y][k].getBlockState() == 3){
                    while(y < this.yDim && world[i][y][k].getBlockState() != 2){
                        y++;
                    }
                    if(y < yDim && world[i][y][k].getBlockState() == 2){
                        this.flagForComplete = true;
                    }
                    y = 1;
                    
                }
            }
            
        }
        //if(this.flagForComplete) System.out.println("Complete");
    }
    
    public double [] meanDistanceFromQueen(){
        double [] values = new double[4];
        
        double a = 0;
        double b = 0;
        double c = 0;
        double h = 0;
        int n = 0;
        
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                for (int k = 0; k < zDim; k++) {
                    if(world[i][j][k].getBlockState() == 2){
                        n++;
                        a = a + Math.abs(world[i][j][k].getXPos()-10);
                        b = b + world[i][j][k].getYPos()-1;
                        c = c + Math.abs(world[i][j][k].getZPos()-10);
                        h = Math.pow(a,2)+Math.pow(b,2)+Math.pow(c,2);
                    }
                }
            }
        }
        
        
        if(a > 0) a = a/n;
        if(b > 0) b = b/n;
        if(c > 0) c = c/n;
        if(h > 0) h = Math.sqrt(h)/n;
        
        values[0] = a;
        values[1] = b;
        values[2] = c;
        values[3] = h;
        
        return values;
    }
    
    public double meanWallThickness(){
        double meanThickness = 0;
        int thickness = 0;
        double totalThickness = 0;
        double groups = 0;
        
        for (int i = 1; i < yDim; i++) {
            for (int j = 0; j < xDim; j++) {
                for (int k = 0; k < zDim; k++) {
                    //System.out.println("first loop: "+j+","+i+","+k);
                    if(world[j][i][k].getBlockState() == 2){
                        thickness++;
                    }else if(thickness >0){
                        groups++;
                        totalThickness = totalThickness + thickness;
                        // do extra stuff here with group size
                        thickness = 0;
                    }
                }
                
            }
        }
        thickness = 0;
        for (int i = 1; i < yDim; i++) {
            for (int j = 0; j < xDim; j++) {
                for (int k = 0; k < zDim; k++) {
                    if(world[k][i][j].getBlockState() == 2){
                        thickness++;
                    }else if(thickness >0){
                        groups++;
                        totalThickness = totalThickness + thickness;
                        // do extra stuff here with group size
                        thickness = 0;
                    }
                }
                
            }
        }
        if(totalThickness > 0) meanThickness = (totalThickness/groups);
        //System.out.println("mean: "+meanThickness);
        return meanThickness;
        
    }
    
    public double calcAvgCP(){
        double avgCP = 0.0;
        for (int i = 0; i < this.xDim; i++) {
            for (int j = 1; j < this.yDim; j++) {
                for (int k = 0; k < this.zDim; k++) {
                    if(world[i][j][k].getBlockState() == 0){
                        avgCP = avgCP + world[i][j][k].getCementPheromone();
                    }
                }
            }
        }
        return avgCP/(xDim*yDim*zDim);
    }
    
    public double calcAvgQP(){
        double avgQP = 0.0;
        for (int i = 0; i < this.xDim; i++) {
            for (int j = 1; j < this.yDim; j++) {
                for (int k = 0; k < this.zDim; k++) {
                    if(world[i][j][k].getBlockState() == 0){
                        avgQP = avgQP + world[i][j][k].getQueenPheromone();
                    }
                }
            }
        }
        return avgQP/(xDim*yDim*zDim);
    }
    
    public Block[][][] getWorld() {
        return world;
    }
    
    public WorkerTermite [] getTermites(){
        return termites;
    }
    
    public void setWorld(Block[][][] world) {
        this.world = world;
    }
    
    public int getXDim() {
        return xDim;
    }
    
    public void setXDim(int xDim) {
        this.xDim = xDim;
    }
    
    public int getYDim() {
        return yDim;
    }
    
    public void setYDim(int yDim) {
        this.yDim = yDim;
    }
    
    public int getZDim() {
        return zDim;
    }
    
    public void setZDim(int zDim) {
        this.zDim = zDim;
    }
    
    public double getQpLevel() {
        return qpLevel;
    }
    
    public void setQpLevel(double qpLevel) {
        this.qpLevel = qpLevel;
    }
    
    public double getCpLevel() {
        return cpLevel;
    }
    
    public void setCpLevel(double cpLevel) {
        this.cpLevel = cpLevel;
    }
    
    public double getCpLossRate() {
        return cpLossRate;
    }
    
    public void setCpLossRate(double cpLossRate) {
        this.cpLossRate = cpLossRate;
    }
    
    public double getQpDiffRate() {
        return qpDiffRate;
    }
    
    public void setQpDiffRate(double qpDiffRate) {
        this.qpDiffRate = qpDiffRate;
    }
    
    public double getQpEvapRate() {
        return qpEvapRate;
    }
    
    public void setQpEvapRate(double qpEvapRate) {
        this.qpEvapRate = qpEvapRate;
    }
    
    public double getCpDiffRate() {
        return cpDiffRate;
    }
    
    public void setCpDiffRate(double cpDiffRate) {
        this.cpDiffRate = cpDiffRate;
    }
    
    public double getCpEvapRate() {
        return cpEvapRate;
    }
    
    public void setCpEvapRate(double cpEvapRate) {
        this.cpEvapRate = cpEvapRate;
    }
    
    public double getCementPheromoneThreshold() {
        return cementPheromoneThreshold;
    }
    
    public void setCementPheromoneThreshold(double cementPheromoneThreshold) {
        this.cementPheromoneThreshold = cementPheromoneThreshold;
    }
    
    public double getMaxQueenPheremoneThreshold() {
        return maxQueenPheremoneThreshold;
    }
    
    public void setMaxQueenPheremoneThreshold(double maxQueenPheremoneThreshold) {
        this.maxQueenPheremoneThreshold = maxQueenPheremoneThreshold;
    }
    
    public double getLowerQueenPheremoneThresholdMin() {
        return lowerQueenPheremoneThresholdMin;
    }
    
    public void setLowerQueenPheremoneThresholdMin(double lowerQueenPheremoneThresholdMin) {
        this.lowerQueenPheremoneThresholdMin = lowerQueenPheremoneThresholdMin;
    }
    
    public double getLowerQueenPheremoneThresholdMax() {
        return lowerQueenPheremoneThresholdMax;
    }
    
    public void setLowerQueenPheremoneThresholdMax(double lowerQueenPheremoneThresholdMax) {
        this.lowerQueenPheremoneThresholdMax = lowerQueenPheremoneThresholdMax;
    }
    
    public double getTrialPheromoneThreshold() {
        return trialPheromoneThreshold;
    }
    
    public void setTrialPheromoneThreshold(double trialPheromoneThreshold) {
        this.trialPheromoneThreshold = trialPheromoneThreshold;
    }
    
    public double getProbOfPlacingMaterial() {
        return probOfPlacingMaterial;
    }
    
    public void setProbOfPlacingMaterial(double probOfPlacingMaterial) {
        this.probOfPlacingMaterial = probOfPlacingMaterial;
    }
    
    public double getProbOfPickingMaterial() {
        return probOfPickingMaterial;
    }
    
    public void setProbOfPickingMaterial(double probOfPickingMaterial) {
        this.probOfPickingMaterial = probOfPickingMaterial;
    }
    
    public double getWindStrength(){
        return windStrength;
    }
    
    public void setWindStrength(double windStrength){
        this.windStrength = windStrength;
    }
    
    public int getNumberOfTermites(){
        return this.numberOfTermites;
    }
    
    public void setNumberOfTermites(int num){
        this.numberOfTermites = num;
    }
    
    public int getNumberOfMoves(){
        return this.numOfMoves;
    }
    
    public void setNumberOfMoves(int num){
        this.numOfMoves = num;
    }
    
    public int getSizeOfQueen(){
        return this.queenSize;
    }
    
    public void setQueenSize(int qs){
        this.queenSize = qs;
    }
    
}
