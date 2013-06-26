/*
 * WorkerTermite.java
 *
 * Created on 25 September 2007, 10:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package TermiteInstances;
import TermiteProjectMain.Block;
import TermiteProjectMain.EarthBlock;
import TermiteProjectMain.AirBlock;
/**
 *
 * @author ug87sjs
 */
public class WorkerTermite implements Termite {
    
    private double cementPheromoneThreshold = 0.0;
    private double maxQueenPheremoneThreshold = 0.0;
    private double lowerQueenPheremoneThresholdMin = 0.0;
    private double lowerQueenPheremoneThresholdMax = 0.0;
    private double trialPheromoneThreshold = 0.0;
    
    private double probOfPlacingMaterial = 0.0;
    private double probOfPickingMaterial = 0.0;
    
    private Block currentLoc;
    
    private Block [] neighbourhood;
    private int nLength = 0;;
    
    private int xPos;
    private int yPos;
    private int zPos;
    
    private boolean carryingMaterial = false;
    private boolean cannotMove = false;
    
    
    
    /** Creates a new instance of WorkerTermite */
    public WorkerTermite() {
        
    }
    
    /**
     *  uses information from the termite and space around it to update the state of the termite
     */
    public boolean updateState(){
        boolean deposited = false;
        move();
        if(currentLoc.getBlockState() != 2 && carryingMaterial){
            deposited = depositMaterial();
        } else pickUpMaterial();
        
        return deposited;
    }
    
    /**
     *  uses the phermone gradients in the neighbourhood to calculate the most probably course
     *  but must obey the rules of movement
     *
     *  need to stop moving off edge of map or deal with this
     */
    private void move(){
        // creates a boolean array that corresponds to the termites neighbourign blocks
        boolean [] connectedNeighbours = new boolean[nLength];
        
        // finds all connected squares to the termites current location that are AirBlock, and are not
        // adjacent in a 3D diagonal
        
        for (int i = 0; i < nLength; i++) {
            if(neighbourhood[i].getBlockState() == 0 && neighbourhood[i].getXPos() != -1){
                connectedNeighbours[i] = true;
                // methods that remove invalid future locations
                connectedNeighbours[i] = connected(currentLoc, neighbourhood[i]);
                connectedNeighbours[i] = notBlockedMove(currentLoc, neighbourhood[i]);
            }else{
                connectedNeighbours[i] = false;
            }
            
        }
        
        //now need to check that each of these Air block has a neighbouring solid block
        // that is connected to a solid block in the termties neighbourhood
        connectedNeighbours = validMove(connectedNeighbours);
        // now create two arrays from the valid moves that contain coresponding pheromone gradients
        
        double [] neighboursCPGrad = new double[nLength];
        double [] neighboursQPGrad = new double[nLength];
        for (int i = 0; i < nLength; i++) {
            //if(this.carryingMaterial){
                if (connectedNeighbours[i] == true){
                    neighboursCPGrad[i] = calcGradient(currentLoc,neighbourhood[i],0);
                    neighboursQPGrad[i] = calcGradient(currentLoc,neighbourhood[i],1);
                }
            //}else{
               // neighboursCPGrad[i] = 0.0;
                //neighboursQPGrad[i] = 0.0;
            ///}
        }
        
        double [] probOfMoves = calcMoveProbs(neighboursCPGrad,connectedNeighbours);
        
        int move = findMove(probOfMoves, connectedNeighbours);
        if(move != -1){
            
            Block b = neighbourhood[move];
            
            this.xPos = b.getXPos();
            this.yPos = b.getYPos();
            this.zPos = b.getZPos();
        }else{
            setCannotMove(true);
        }
    }
    
    private boolean notBlockedMove(Block b1, Block b2){
        Block n1 = null;
        Block n2 = null;
        boolean notBlocked = true;
        for (int i = 0; i < neighbourhood.length; i++) {
            if(neighbourhood[i].getBlockState() != 0 &&
                    (neighbourhood[i].getXPos() == b1.getXPos()+1|| neighbourhood[i].getXPos() == b1.getXPos()-1)
                    && neighbourhood[i].getYPos() == b1.getYPos() && neighbourhood[i].getZPos() == b1.getZPos()){
                n1 = neighbourhood[i];
                for (int j = 0; j < neighbourhood.length; j++) {
                    if(neighbourhood[j].getBlockState() != 0 &&
                            (neighbourhood[j].getZPos() == b1.getZPos()+1 ||neighbourhood[j].getZPos() == b1.getZPos()-1)
                            && neighbourhood[j].getYPos() == b1.getYPos() && neighbourhood[j].getXPos() == b1.getXPos() ){
                        n2 = neighbourhood[j];
                        if(n1.getXPos() == b2.getXPos() && n2.getZPos() == b2.getZPos()){
                            notBlocked = false;
                        }
                    }
                    
                }
            }
        }
        return notBlocked;
    }
    
    /**
     *  roullette wheel implementation: calculates the probabailities of moves
     */
    private double [] calcMoveProbs(double [] neighboursCPGrad, boolean [] connectedNeighbours){
        int l = nLength;
        double [] probOfMoves = new double[nLength];
        
        double totalGrad = 0.0;
        
        for (int i = 0; i < nLength; i++) {
            if(connectedNeighbours[i] == false){
                l--;
            }else{ totalGrad = totalGrad + Math.abs(neighboursCPGrad[i]);}
            
        }
        double probSoFar = 0.0;
        for (int i = 0; i < nLength; i++) {
            if(connectedNeighbours[i] == false){
                probOfMoves[i] = -1.0;
            }else{
                if (totalGrad == 0.0){
                    probOfMoves[i] = 1.0/l + probSoFar;
                    probSoFar = probOfMoves[i];
                }else if(Math.abs(neighboursCPGrad[i]) < 0.01){
                    probOfMoves[i] = (0.01/l/totalGrad)+ probSoFar;
                    probSoFar = probOfMoves[i];
                }else{
                    probOfMoves[i] = (Math.abs(neighboursCPGrad[i])/l/totalGrad) + probSoFar;
                    probSoFar = probOfMoves[i];
                }
            }
            //System.out.println("prob of move: "+probOfMoves[i]+","+connectedNeighbours[i]+","+neighboursCPGrad[i]);
        }
        
        for (int i = 0; i < nLength; i++) {
            if(connectedNeighbours[i] == true){
                probOfMoves[i] = probOfMoves[i]/probSoFar;
            }
        //System.out.println("prob of move: "+probOfMoves[i]+","+connectedNeighbours[i]+","+neighboursCPGrad[i]);
        }
        
        //System.out.println("");
        return probOfMoves;
    }
    
    /**
     *  roullette wheel: fins move with random prob
     */
    private int findMove(double [] probOfMoves, boolean [] connectedNeighbours){
        double p = Math.random()*Math.abs(probOfMoves[probOfMoves.length-1]);
        //System.out.println("p: "+p);
        double difference = 9999999.99;
        int move = -1;
        int i = 0;
        
        while(i < probOfMoves.length && p > probOfMoves[i]){
            i++;//System.out.println(probOfMoves[i]);
        }
        //System.out.println(probOfMoves[i]);
        if(i < probOfMoves.length && connectedNeighbours[i] == true){
            if(probOfMoves[i] != -1.0){
                move = i;
            }
            //System.out.println(Math.abs(p - probOfMoves[i]));
        }
        return move;
    }
    
    
    /**
     *  valid ruls for connected neighbours
     *
     */
    public boolean connected(Block b1, Block b2){
        boolean bool = false;
        int x1 = b1.getXPos();
        int x2 = b2.getXPos();
        int y1 = b1.getYPos();
        int y2 = b2.getYPos();
        int z1 = b1.getZPos();
        int z2 = b2.getZPos();
        
        if(x1 == x2 && y1 == y2 && z1 == z2) bool = true;
        
        else if(x1 == x2 && y1 == y2 && z1 == z2+1) bool = true;
        else if(x1 == x2 && y1 == y2 && z1 == z2-1) bool = true;
//
        else if(x1 == x2 && y1 == y2+1 && z1 == z2+1) bool = true;
        else if(x1 == x2 && y1 == y2-1 && z1 == z2+1) bool = true;
        else if(x1 == x2 && y1 == y2+1 && z1 == z2-1) bool = true;
        else if(x1 == x2 && y1 == y2-1 && z1 == z2-1)bool = true;
        
        else if(x1 == x2 && y1 == y2+1 && z1 == z2)bool = true;
        else if(x1 == x2 && y1 == y2-1 && z1 == z2)bool = true;
//
        else if(x1 == x2+1 && y1 == y2+1 && z1 == z2)bool = true;
        else if(x1 == x2+1 && y1 == y2-1 && z1 == z2)bool = true;
        else if(x1 == x2-1 && y1 == y2+1 && z1 == z2)bool = true;
        else if(x1 == x2-1 && y1 == y2-1 && z1 == z2)bool = true;
//
        else if(x1 == x2+1 && y1 == y2 && z1 == z2)bool = true;
        else if(x1 == x2-1 && y1 == y2 && z1 == z2)bool = true;
        
        else if(x1 == x2+1 && y1 == y2 && z1 == z2+1)bool = true;
        else if(x1 == x2-1 && y1 == y2 && z1 == z2+1)bool = true;
        else if(x1 == x2+1 && y1 == y2 && z1 == z2-1) bool = true;
        else if(x1 == x2-1 && y1 == y2 && z1 == z2-1)bool = true;
        
        return bool;
    }
    
    /**
     *  now need to check that each of these Air block has a neighbouring solid block
     * that is connected to a solid block in the termties neighbourhood
     *
     */
    public boolean [] validMove(boolean [] connectedNeighbours){
        Block [] newN = null;
        boolean conn = false;
        // for each airblock, get the neighbourhood of that block
        for (int i = 0; i < nLength; i++) {
            conn = false;
            if (connectedNeighbours[i] == true){
                newN = neighbourhood[i].getNeighbours();
                
                // for each of the blocks neighbours, compare it to each of the airblocks neighbours that is solid
                // given that itself is solid
                for (int j = 0; j < newN.length; j++) {
                    //System.out.println("out method");
                    // if the block is solid and connected to the airblock we are checking
                    if(newN[j].getBlockState() >0 && connected(newN[j], neighbourhood[i]) && connected(newN[j], currentLoc)){
                        // loop through starting positions neighbours                        
                        conn = true;
                        
                    }else{
                        if(!conn) conn = false;
                    }
                }
                //connectedNeighbours[i] = conn;
                connectedNeighbours[i] = conn;
            }
            
        }
        
        return connectedNeighbours;
    }
    
    /** returns the difference of the gradient between the two blocks
     *  index int determines which pheromone
     *
     */
    public double calcGradient(Block b1, Block b2, int i){
        double grad = 0.0;
        if (i == 0){
            if(currentLoc.getBlockState() == 2){
                grad = b2.getCementPheromone();
            }else{
                grad = b2.getCementPheromone() - b1.getCementPheromone();
            }
        }else{
            if(currentLoc.getBlockState() == 2){
                grad = b2.getQueenPheromone();
            }else{
                grad = b2.getQueenPheromone() - b1.getQueenPheromone();
            }
        }
        
        return grad;
    }
    
    
    /**
     *
     *
     */
    public Block calcBestMove(double [] nCPGrad, double [] nQPGrad, boolean [] cN){
        Block move = null;
        int index = -1;
        double bestQP = 0.0;
        double bestCP = 0.0;
        for (int i = 0; i < cN.length; i++) {
            if((nQPGrad[i] < bestQP || nCPGrad[i] > bestCP) && cN[i]){
                bestQP = nQPGrad[i];
                bestCP = nCPGrad[i];
                index = i;
            }
            
        }
        // just move randomly if this yields nothing
        
        if(index == -1){
            index = (int)(Math.round(Math.random()*cN.length-1));
        }
        
        move = neighbourhood[index];
        return move;
    }
    
    
    
    /**
     *  an attempt by the termite to obtain building material obeying the constraints
     */
    private void pickUpMaterial(){
        // pick up material when connected to an earth block, if detected qp is lower than max-qp threshold,
        // but greater than max placement boundary
        
        boolean materialPresent = false;
        for (int i = 0; i < nLength; i++) {
            if(neighbourhood[i].getBlockState() == 1){
                //System.out.println("material present: "+i);
                materialPresent = true;
            }
        }
        // should be done with a probability
        if(materialPresent && currentLoc.getQueenPheromone() < this.maxQueenPheremoneThreshold
                && currentLoc.getQueenPheromone() > this.lowerQueenPheremoneThresholdMax){
            //System.out.println("attempting pick up");
            if (Math.random() < this.probOfPickingMaterial){
                //System.out.println("succesful pickup");
                this.carryingMaterial = true;
            }
        }
        
        
    }
    
    /**
     *  an attempt by the termite to deposit the material it is carrying, obeying the constraints
     */
    private boolean depositMaterial(){
        // see papers/notes for conditions
        if(currentLoc.getQueenPheromone() > this.lowerQueenPheremoneThresholdMin &&
                currentLoc.getQueenPheromone() < this.lowerQueenPheremoneThresholdMax && currentLoc.getBlockState() ==0){
            //System.out.println("hello: "+this.getXPos()+", "+this.getYPos()+", "+this.getZPos());
            boolean horizontal = false;
            boolean vertical = false;
            Block n = null;
            
            // rule 1
            for (int i = 0; i < nLength; i++) {
                n = neighbourhood[i];
                if(n.getXPos() == this.xPos && n.getZPos() == this.zPos
                        && (n.getYPos() == this.yPos+1 || n.getYPos() == this.yPos-1)){
                    if (n.getBlockState() != 0 && n.getBlockState() != 3){
                        vertical = true;
                    }
                }
            }
            //System.out.println("vertical: "+vertical);
            // rule 2
            int count = 0;
            for (int i = 0; i < nLength; i++) {
                n = neighbourhood[i];
                if(n.getYPos() == this.yPos){
                    if(n.getBlockState() != 0 && n.getBlockState() != 3){
                        count++;
                    }
                }
            }
            //System.out.println("count: "+count);
            if(count >=3){
                horizontal = true;
            }
            
            // how to choose between 1 and two
            //System.out.println(probOfPlacingMaterial);
            if(vertical || horizontal){
                if (Math.random() < this.probOfPlacingMaterial){
                    //System.out.println("placing block");
                    this.carryingMaterial = false;
                    //this.setCannotMove(true);
                }
            }
        }
        
        return !(this.carryingMaterial);
    }
    
    public boolean getCannotMove(){
        return cannotMove;
    }
    
    public void setCannotMove(boolean bool){
        cannotMove = bool;
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
    
    public Block[] getNeighbourhood() {
        return neighbourhood;
    }
    
    public void setNeighbourhood(Block[] neighbourhood) {
        this.neighbourhood = neighbourhood;
        //System.out.println(this.neighbourhood.length);
        nLength = this.neighbourhood.length;
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
    
    public boolean isCarryingMaterial() {
        return carryingMaterial;
    }
    
    public void setCarryingMaterial(boolean carryingMaterial) {
        this.carryingMaterial = carryingMaterial;
    }
    
    public Block getCurrentLoc() {
        return currentLoc;
    }
    
    public void setCurrentLoc(Block currentLoc) {
        this.currentLoc = currentLoc;
        this.xPos = currentLoc.getXPos();
        this.yPos = currentLoc.getYPos();
        this.zPos = currentLoc.getZPos();
    }
    
    public double getProbOfPickingMaterial() {
        return probOfPickingMaterial;
    }
    
    public void setProbOfPickingMaterial(double probOfPickingMaterial) {
        this.probOfPickingMaterial = probOfPickingMaterial;
    }
    
    /** tests methods
     *
     */
    public static void main(String[] args) {
        // tests connected method
        WorkerTermite t = new WorkerTermite();
        Block b1 = new EarthBlock();
        Block b2 = new EarthBlock();
        b1.setXPos(1);
        b1.setYPos(1);
        b1.setZPos(1);
        b2.setXPos(2);
        b2.setYPos(1);
        b2.setZPos(1);
        System.out.println("connected: "+t.connected(b1,b2));
        b1.setXPos(1);
        b1.setYPos(1);
        b1.setZPos(1);
        b2.setXPos(2);
        b2.setYPos(2);
        b2.setZPos(0);
        System.out.println("not connected: "+t.connected(b1,b2));
        
        // tests notBlockMoved method
        Block b3 = new AirBlock(0,0,0,0);
        Block b4 = new AirBlock(0,0,0,0);
        b1 = new AirBlock(0,0,0,0);
        b2 = new AirBlock(0,0,0,0);
        b1.setXPos(1);
        b1.setYPos(1);
        b1.setZPos(1);
        b2.setXPos(2);
        b2.setYPos(1);
        b2.setZPos(2);
        b3.setXPos(2);
        b3.setYPos(1);
        b3.setZPos(1);
        b4.setXPos(1);
        b4.setYPos(1);
        b4.setZPos(2);
        Block [] n = new Block[4];
        n[0] = b1;
        n[1] = b2;
        n[2] = b3;
        n[3] = b4;
        t.setNeighbourhood(n);
        System.out.println("blocked: "+t.notBlockedMove(b3,b4));
        
        // tests calcGradient method
        b1.addCementPheromone(0.5);
        b2.addCementPheromone(1.0);
        t.currentLoc = b3;
        System.out.println("gradient: "+t.calcGradient(b1,b2,0));
        
        // tests valid move
        t.currentLoc = b1;
        b2 = new EarthBlock();
        b2.setXPos(2);
        b2.setYPos(1);
        b2.setZPos(2);
        Block [] n1 = {b2,b3,b4};
        b1.setNeighbours(n1);
        Block [] n2 = {b1,b3,b4};
        b2.setNeighbours(n2);
        Block [] n3 = {b2,b1,b4};
        b3.setNeighbours(n3);
        Block [] n4 = {b2,b3,b1};
        b4.setNeighbours(n4);
        t.setNeighbourhood(n1);
        boolean [] c = {true,true,true};
        System.out.println("invalid move: "+t.validMove(c)[0]+","+t.validMove(c)[1]+","+t.validMove(c)[2]);
        
        // tests calcMoveProbs method
        t.nLength = 3;
        double [] g1 = {0,0.0000001, 0.5};
        boolean [] c1 = {true,true,true};
        System.out.println("probs: "+t.calcMoveProbs(g1,c1)[0]+","+t.calcMoveProbs(g1,c1)[1]+","+t.calcMoveProbs(g1,c1)[2]);
        t.nLength = 3;
        double [] g2 = {0, 0, 0};
        boolean [] c2 = {true,true,true};
        double [] g3 = {0, 0.5, 0};
        boolean [] c3 = {true,false,true};
        
        //System.out.println("probs: "+t.calcMoveProbs(g2,c2)[0]+","+t.calcMoveProbs(g2,c2)[1]+","+t.calcMoveProbs(g2,c2)[2]);
        //System.out.println("probs: "+t.calcMoveProbs(g3,c3)[0]+","+t.calcMoveProbs(g3,c3)[1]+","+t.calcMoveProbs(g3,c3)[2]);
        
        // test find moves
        double [] g5 = {0,0.01, 0.5, 1.0};
        System.out.println("find move: "+t.findMove(g5, c1));
        
        // test picking method
        b2 = new EarthBlock();
        //b2 = new AirBlock(0,0,0,0);
        
        b2.setXPos(2);
        b2.setYPos(1);
        b2.setZPos(2);
        t.currentLoc = b1;
        Block [] n5 = {b2,b3,b4};
        t.setNeighbourhood(n5);
        t.setProbOfPickingMaterial(1.0);
        t.setMaxQueenPheremoneThreshold(0.4);
        t.setLowerQueenPheremoneThresholdMax(0.28);
        b1.addQueenPheromone(0.25);
        //t.pickUpMaterial();
        System.out.println("picking: "+t.carryingMaterial);
        
        // test deposits method
        t.setLowerQueenPheremoneThresholdMin(0.2);
        b3 = new EarthBlock();
        b4 = new EarthBlock();
        b1 = new AirBlock(0,0,0,0);
        b2 = new EarthBlock();
        b1.setXPos(1);
        b1.setYPos(1);
        b1.setZPos(1);
        b2.setXPos(2);
        b2.setYPos(1);
        b2.setZPos(2);
        b3.setXPos(2);
        b3.setYPos(1);
        b3.setZPos(1);
        b4.setXPos(1);
        b4.setYPos(1);
        b4.setZPos(2);
        b1.addQueenPheromone(0.25);
        t.setCurrentLoc(b1);
        Block [] n6 = {b2,b3,b4};
        t.setNeighbourhood(n6);
        t.setProbOfPlacingMaterial(1.0);
        t.carryingMaterial = true;
        System.out.println("depositing: "+t.depositMaterial());
        
        
        
        double z = 20;
        for (int i = 0; i < 120; i++) {
            z = z - z*0.01;
        }
        System.out.println(z);
        
    }
    
    
}
