/*
 * TermiteFrame.java
 *
 * Created on 01 October 2007, 11:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package TermiteGraphics;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Container;
import TermiteProjectMain.Block;
import TermiteProjectMain.TermiteWorld;
import java.io.*;

/**
 *
 * @author ug87sjs
 */
public class TermiteFrame extends JFrame implements ActionListener{
    
    
    private TermiteWorld world = null;
    private int r = 0;
    private int runs;
    
    // graphics panels
    private TermitePheromoneTopPanel tcp = null;
    private TermiteSideViewPanel svp = null;
    private TermiteTopViewPanel tvp = null;
    private TermiteFrontViewPanel fvp = null;
    private TermitePheromoneSidePanel psp = null;
    private TermitePheromoneFrontPanel pfp = null;
    
    private StartButtonPanel sbp = null;
    
    // flags
    private boolean run = false;
    private boolean running = false;
    
    private int fileCounter = 0;
    
    // string elements to save time forming longer strings
    private String table = "<table>";
    private String ntable = "</table>";
    private String row = "<tr>";
    private String nrow = "</tr>";
    private String cell = "<td>";
    private String ncell = "</td>";
    
    private String headers = "";
    private String output = "";
    
    private GraphPanel gp = null;
    
    // arrays for data stoarage to be past to graph frame
    private double [] rates = null;
    private double [] depsPerTs = null;
    private double [] total = null;
    private double [] meanX = null;
    private double [] meanY = null;
    private double [] meanZ = null;
    private double [] meanH = null;
    private double [] thickness = null;
    private double [] cpIntensity = null;
    private double [] qpIntensity = null;
    
    private String graph = "rate";
    
    
    /** Creates a new instance of TermiteFrame */
    public TermiteFrame(TermiteWorld world, int runs) {
        
        this.world = world;
        this.runs = runs;
        
        
        setTitle("Termite Simulation, runs = "+runs);
        setSize(900, 800);
        setLayout(new GridLayout(2,2));
                       
        tvp = new TermiteTopViewPanel(world);        
        svp = new TermiteSideViewPanel(world);        
        fvp = new TermiteFrontViewPanel(world);        
        sbp = new StartButtonPanel(this);
        
        getContentPane().add(fvp,0);
        getContentPane().add(svp,1);
        getContentPane().add(tvp,2);
        getContentPane().add(sbp,3);
        
        // sets up text and graphs parameters
        createHeaders();
        setUpLists();
        setUpGraphFrame();
        setVisible(true);
        
        
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent event){
                System.exit(0);
            }  });
    }
    
    /**
     *  loops until user presses the start button, extends data output string every 1/20 steps
     */
    public void run(){

        while(r < runs){
            while(run == false){}
            //System.out.println("run: "+r);
            world.update(r);
            updateLists();
            setUpGP();
            
            if(r <= runs-1){
                //world.printWorld();
                // world.printTermites();
                
                tvp.updateWorld(world.getWorld(),world,world.getTermites());
                fvp.updateWorld(world.getWorld(), world);
                svp.updateWorld(world.getWorld(), world);
            }
            if(r%(runs/20) == 0 && r != 0){
                extendOutPut();
            }
            r++;
        }        
        extendOutPut();
        run = false;
    }
    
    /**
     *  recieves events from StartButtonPanel
     */
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("start")){
            running = true;
            if(run){
                run = false;
            }else{
                run = true;
            }
            //System.out.println(run);
        }else if(e.getActionCommand().equals("reset")){
            if(run == false){
                running = false;
                run = false;
                reset();
            }
            // call reset method in termite world, reset runs value, wait for start command, what if parameters are changed?
        }else if(e.getActionCommand().equals("set")){
            //System.out.println(running);
            getParameters();
        }else if(e.getActionCommand().equals("save")){
            extendOutPut();
            saveToFile();
        }else{
            graph = e.getActionCommand();
            setUpGP();
        }
    }
    
    /**
     *  svaes data to files and updates file name
     */
    public void saveToFile(){
        try {
            String fileName = "results"+fileCounter+".txt";
            fileCounter++;
            PrintWriter out = new PrintWriter(new FileWriter(fileName));
            
            out.println(table+headers+output+ntable);
            
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        output = "";
    }
    
    /**
     *  adds another line of results to output string
     *  currently set up to save ALL data
     */
    public void extendOutPut(){
        
        output = output+""+paramsToString();
        // use out.println(); to create a table of data
        //if(sbp.getRateOfDeposition() == true){
        double rate = world.rateOfDeposition();
        //System.out.println("rate: "+rate);
        //System.out.println("runs r: "+r);
        if(r>0) rate = rate/r;
        //System.out.println("rate after div: "+rate);
        output = output+cell+rate+ncell;
        //headers = headers+cell+"Rate of Dep"+ncell;
        //}
        //if(sbp.getRateOfDepositionPerTermite() == true){
        rate = world.rateOfDeposition();
        if(r>0) rate = rate/r;
        rate = rate/world.getNumberOfTermites();
        //System.out.println("rate after div: "+rate);
        output = output+cell+rate+ncell;
//                headers = headers+cell+"Rate of Dep Per Term"+ncell;
        //}
        //if(sbp.getMeanDistanceFromQueen() == true){
        double [] values = world.meanDistanceFromQueen();
        
        output= output+cell+values[0]+ncell;
        //headers = headers+cell+"Mean x"+ncell;
        output= output+cell+values[1]+ncell;
        //headers = headers+cell+"Mean y"+ncell;
        output= output+cell+values[2]+ncell;
        //headers = headers+cell+"Mean z"+ncell;
        output= output+cell+values[3]+ncell;
//            headers = headers+cell+"Mean hyp"+ncell;
                
        //}
        //if(sbp.getTimeOfDepositionStages() == true){
        if(world.firstDepositMade()){
            output = output+cell+world.timeToFirstDeposit()+ncell;
            //headers = headers+cell+"Time to Firsr Dep"+ncell;
        }
        if(world.getFlagForMeanHeight()){
            output = output+cell+world.getTimeToMeanHeight()+ncell;
            //headers = headers+cell+"Time to Mean Height"+ncell;
        }
        if(world.getFlagForComplete()){
            output = output+cell+world.getTimeToComplete()+ncell;
            //headers = headers+cell+"Time to Complete"+ncell;
        }
        //}
        //if(sbp.getMeanThicknessOfWall() == true){
        double mean = world.meanWallThickness();
        output = output+cell+mean+ncell;
        //headers = headers+cell+"Mean Wall Thickness"+ncell;
        //}
        
        output = output+nrow;
    }
    
    /**
     *  concatenates parameters in strings
     */
    public String paramsToString(){
        String s = "";
        
        s = s + row +
                cell + r +ncell +
                cell + world.getXDim() + ncell +
                cell + world.getNumberOfTermites() + ncell +
                cell + world.getNumberOfMoves() + ncell +
                cell + world.getSizeOfQueen() + ncell +
                cell + world.getWindStrength() + ncell +
                cell + world.getCpDiffRate() + ncell +
                cell + world.getCpEvapRate() + ncell +
                cell + world.getCpLossRate() + ncell +
                cell + world.getQpLevel() + ncell +
                cell + world.getQpDiffRate() + ncell +
                cell + world.getQpEvapRate() + ncell +
                cell + world.getProbOfPlacingMaterial() + ncell +
                cell + world.getProbOfPickingMaterial() + ncell +
                cell + world.getLowerQueenPheremoneThresholdMax() + ncell +
                cell + world.getLowerQueenPheremoneThresholdMin() + ncell +
                cell + world.getMaxQueenPheremoneThreshold() + ncell;
        
        return s;
    }
    
    /**
     *  creates a list of headers for the results
     */
    public void createHeaders(){
        headers = ""+row +
                cell+"run"+ncell+
                cell+"Axis"+ncell+
                cell+"Number Of Termites"+ncell+
                cell+"Moves Per Turn"+ncell+
                cell+"Size Of Queen"+ncell+
                cell+"Wind Strength"+ncell+
                cell+"CP Diffusion"+ncell+
                cell+"CP Evaporation"+ncell+
                cell+"CP Loss Rate"+ncell+
                cell+"QP Level"+ncell+
                cell+"QP Diffusion"+ncell+
                cell+"QP Evaporation"+ncell+
                cell+"Placing Prob"+ncell+
                cell+"Picking Prob"+ncell+
                cell+"Upper QP Threshold"+ncell+
                cell+"Lower QP Threshold"+ncell+
                cell+"Max QP Threashold"+ncell;
        headers = headers+cell+"Rate of Dep"+ncell;
        headers = headers+cell+"Rate of Dep Per Term"+ncell;
        headers = headers+cell+"Mean x"+ncell;
        headers = headers+cell+"Mean y"+ncell;
        headers = headers+cell+"Mean z"+ncell;
        headers = headers+cell+"Mean hyp"+ncell;
        headers = headers+cell+"Time to Firsr Dep"+ncell;
        if(world.getFlagForMeanHeight()) headers = headers+cell+"Time to Mean Height"+ncell;
        if(world.getFlagForComplete()) headers = headers+cell+"Time to Complete"+ncell;
        headers = headers+cell+"Mean Wall Thickness"+ncell;
        headers = headers+nrow;
    }
    
    public void reset(){
        r = 0;
        //createHeaders();
        //output = ""+paramsToString();
        //world = null;
        getParameters();
        setUpLists();
        setUpGP();
        //world = new TermiteWorld();
    }
    
    public void getParameters(){
        world = sbp.getParameters();
        tvp.updateWorld(world.getWorld(),world,world.getTermites());
        fvp.updateWorld(world.getWorld(), world);
        svp.updateWorld(world.getWorld(), world);
    }
    
    public TermiteWorld getWorld(){
        return world;
    }
    
    public void setWorld(TermiteWorld world){
        this.world = world;
    }
    
    public boolean getRunning(){
        return running;
    }
    
    public void setRunning(boolean newV){
        this.running = newV;
    }
    
    public StartButtonPanel getSBP(){
        return sbp;
    }
    
    public void setUpGraphFrame(){
        JFrame gf = new JFrame();
        gf.setSize(400,500);
        gf.setLocation(1000,0);
        gf.setTitle("Graph Frame");
        
        gp = new GraphPanel();
        setUpGP();
        gf.add(gp);
        gf.setVisible(true);
    }
    
    public void setUpLists(){
        rates = new double[runs];
        depsPerTs = new double[runs];
        total = new double[runs];
        meanX = new double[runs];
        meanY = new double[runs];
        meanZ = new double[runs];
        meanH = new double[runs];
        thickness = new double[runs];
        cpIntensity = new double[runs];
        qpIntensity = new double[runs];
    }
    
    public void updateLists(){
        if(r >10){
            double rate = world.rateOfDeposition()+0.0;
            //System.out.println("rate: "+rate);
            rates[r] = rate/r;
            depsPerTs[r] = rate/world.getNumberOfTermites();
            total[r] = rate;
            double [] values = world.meanDistanceFromQueen();
            meanX[r] = values[0];
            meanY[r] = values[1];
            meanZ[r] = values[2];
            meanH[r] = values[3];
            thickness[r] = world.meanWallThickness();
            cpIntensity[r] = world.calcAvgCP();
            qpIntensity[r] = world.calcAvgQP();
        }
    }
    
    public void setUpGP(){
        if(graph.equals("rate")){
            gp.setYParam(rates,r,graph);
        }else if(graph.equals("depPerTerm")){
            gp.setYParam(depsPerTs,r,graph);
        }else if(graph.equals("total")){
            gp.setYParam(total,r,graph);
        }else if(graph.equals("meanX")){
            gp.setYParam(meanX,r,graph);
        }else if(graph.equals("meanY")){
            gp.setYParam(meanY,r,graph);
        }else if(graph.equals("meanZ")){
            gp.setYParam(meanZ,r,graph);
        }else if(graph.equals("meanH")){
            gp.setYParam(meanH,r,graph);
        }else if(graph.equals("thickness")){
            gp.setYParam(thickness,r,graph);
        }else if(graph.equals("cp")){
            gp.setYParam(cpIntensity,r,graph);
        }else if(graph.equals("qp")){
            gp.setYParam(qpIntensity,r,graph);
        }
    }
    
    public void setRuns(int runs){
        this.runs = runs;
    }
    
    public int getRuns(){
        return runs;
    }
    
}
