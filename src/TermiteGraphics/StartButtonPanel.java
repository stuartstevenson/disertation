/*
 * StartButtonPanel.java
 *
 * Created on 12 December 2007, 09:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package TermiteGraphics;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Container;
import TermiteProjectMain.TermiteWorld;
/**
 *
 * @author stu
 */
public class StartButtonPanel extends JPanel implements ActionListener {
    
    private TermiteFrame tf = null;
    
    private int worldSize = 0;
    private int NumberOfTermites = 0;
    private int NumberOfMoves = 0;
    private int queenSize = 0;
    private int runs = 0;
    
    private double wind = 0.0;
    private double cementEmisRate = 0.0;
    private double queenEmisRate = 0.0;
    private double cementDiffRate = 0.0;
    private double queenDiffRate = 0.0;
    private double cementEvapRate = 0.0;
    private double queenEvapRate = 0.0;
    private double placingProb = 0.0;
    private double pickingProb = 0.0;
    private double upperQP = 0.0;
    private double lowerQP = 0.0;
    private double maxQP = 0.0;
    
    
    //spinners
    private JSpinner numTermSpinner = null;
    private JSpinner numMovesSpinner = null;
    private JSpinner sizeQSpinner = null;
    private JSpinner axisSpinner = null;
    private JSpinner runSpinner = null;
    
    private JSpinner windStrengthSpinner = null;
    private JSpinner cEmRateSpinner = null;
    private JSpinner cDRateSpinner = null;
    private JSpinner cEvRateSpinner = null;
    private JSpinner qEmRateSpinner = null;
    private JSpinner qDRateSpinner = null;
    private JSpinner qEvRateSpinner = null;
    private JSpinner placePSpinner = null;
    private JSpinner pickPSpinner = null;
    private JSpinner uQPTSpinner = null;
    private JSpinner lQPTSpinner = null;
    private JSpinner mQPSpinner = null;
    
    private JCheckBox rateOfDeposition = null;
    private JCheckBox rateOfDepositionPerTermite = null;
    private JCheckBox meanDistanceFromQueen = null;
    private JCheckBox timeOfDepositionStages = null;
    private JCheckBox meanThicknessOfWall = null;
    
    private JButton start = null;
    
    
    
    /** Creates a new instance of StartButtonPanel */
    public StartButtonPanel(TermiteFrame tf) {
        
        this.tf = tf;
        setParameters();
        JTabbedPane startUp = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT);
        startUp.addTab("Start",null,startUpTab(),"set initial parameters");
        startUp.addTab("Parameters",null,pheromoneAlterationsTab(),"change properties");
        //startUp.addTab("Results",null,resultsTab(),"process results");
        startUp.addTab("Results",null,graphTab(),"process data");
        
        
        this.add(startUp);
        
        
    }
    
    public void setParameters(){
        
        worldSize = tf.getWorld().getXDim();
        NumberOfTermites = tf.getWorld().getNumberOfTermites();
        NumberOfMoves = tf.getWorld().getNumberOfMoves();
        queenSize = tf.getWorld().getSizeOfQueen();
        runs = tf.getRuns();
        
        wind = tf.getWorld().getWindStrength();
        cementEmisRate = tf.getWorld().getCpLossRate();
        queenEmisRate = tf.getWorld().getQpLevel();
        cementDiffRate = tf.getWorld().getCpDiffRate();
        queenDiffRate = tf.getWorld().getQpDiffRate();
        cementEvapRate = tf.getWorld().getCpEvapRate();
        queenEvapRate = tf.getWorld().getQpEvapRate();
        placingProb = tf.getWorld().getProbOfPlacingMaterial();
        pickingProb = tf.getWorld().getProbOfPickingMaterial();
        upperQP = tf.getWorld().getLowerQueenPheremoneThresholdMax();
        lowerQP = tf.getWorld().getLowerQueenPheremoneThresholdMin();
        maxQP = tf.getWorld().getMaxQueenPheremoneThreshold();
    }
    
    public TermiteWorld getParameters(){
        TermiteWorld world = tf.getWorld();
        System.out.println(world);
        // set the parametrs and call update Parameters method
        boolean running = tf.getRunning();
        updateParameters();
        
        if(!running){
            //replace world
            world = new TermiteWorld(
                    // x dimension
                    worldSize,
                    // y dimension
                    worldSize,
                    // z dim
                    worldSize,
                    // amount of queen pheromone in a queen block, controls rate of emission
                    this.queenEmisRate,
                    // amount of cement pheromone in a cement block, needs to last for equivalent of two minutes
                    20,
                    // rate at which cement pheromone is lost in the cement block
                    this.cementEmisRate,
                    // rate at which qp diffuses
                    this.queenDiffRate,
                    // rate at which qp evaporates
                    this.queenEvapRate,
                    // rate at which cp diffuses
                    this.cementDiffRate,
                    // rate at which cp evaporates
                    this.cementEvapRate,
                    // number of termites
                    this.NumberOfTermites,
                    // number of moves
                    this.NumberOfMoves,
                    // size of queen
                    this.queenSize,
                    // cement pheromone threshold
                    0.5,
                    // upper bound of qp threshold for building
                    this.upperQP,
                    // lower bound of qp threshold for building
                    this.lowerQP,
                    // queen pheromone threshold
                    this.maxQP,
                    // probability of picking up material
                    this.pickingProb,
                    // probability of placing material
                    this.placingProb,
                    // wind strength
                    this.wind
                    );
            
            tf.setRuns(this.runs);
            
            
            
            
        }else{
            world.updateParameters(wind,queenDiffRate,queenEvapRate,queenEmisRate,
                    cementDiffRate,cementEvapRate,cementEmisRate,
                    placingProb,pickingProb,upperQP,lowerQP,maxQP
                    );
        }
        
        // set all other params
        
        return world;
    }
    
    public void updateParameters(){
        worldSize = Integer.parseInt(this.axisSpinner.getModel().getValue().toString());
        NumberOfTermites = Integer.parseInt(this.numTermSpinner.getModel().getValue().toString());
        NumberOfMoves = Integer.parseInt(this.numMovesSpinner.getModel().getValue().toString());
        queenSize = Integer.parseInt(this.sizeQSpinner.getModel().getValue().toString());
        runs = Integer.parseInt(this.runSpinner.getModel().getValue().toString());
        
        wind = Double.parseDouble(this.windStrengthSpinner.getModel().getValue().toString());
        cementEmisRate = Double.parseDouble(this.cEmRateSpinner.getModel().getValue().toString());
        queenEmisRate = Double.parseDouble(this.qEmRateSpinner.getModel().getValue().toString());
        cementDiffRate = Double.parseDouble(this.cDRateSpinner.getModel().getValue().toString());
        queenDiffRate = Double.parseDouble(this.qDRateSpinner.getModel().getValue().toString());
        cementEvapRate = Double.parseDouble(this.cEvRateSpinner.getModel().getValue().toString());
        queenEvapRate = Double.parseDouble(this.qEvRateSpinner.getModel().getValue().toString());
        placingProb = Double.parseDouble(this.placePSpinner.getModel().getValue().toString());
        pickingProb = Double.parseDouble(this.pickPSpinner.getModel().getValue().toString());
        upperQP = Double.parseDouble(this.uQPTSpinner.getModel().getValue().toString());
        lowerQP = Double.parseDouble(this.lQPTSpinner.getModel().getValue().toString());
        maxQP = Double.parseDouble(this.mQPSpinner.getModel().getValue().toString());
        //System.out.println(placingProb);
    }
    
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("start")){
            if(start.getText().equals("start")){
                start.setText("pause");
            }else{
                start.setText("start");
            }
        }
    }
    
    public JComponent startUpTab(){
        JPanel j = new JPanel();
        
        j.setLayout(new GridLayout(12,2));
        
        start = new JButton("start");
        start.setActionCommand("start");
        start.addActionListener(tf);
        start.addActionListener(this);
        j.add(start,0);
        
        JButton reset = new JButton("reset (must be paused)");
        reset.setActionCommand("reset");
        reset.addActionListener(tf);
        j.add(reset,1);
        
        JLabel filler = new JLabel("Number of Termites");
        j.add(filler,2);
        System.out.println(NumberOfTermites);
        numTermSpinner = new JSpinner(new SpinnerNumberModel(NumberOfTermites,10,1000,10));
        j.add(numTermSpinner,3);
        
        JLabel movesPerTurn = new JLabel("Moves Per Turn");
        j.add(movesPerTurn,4);
        numMovesSpinner = new JSpinner(new SpinnerNumberModel(NumberOfMoves,1,10,1));
        j.add(numMovesSpinner,5);
        
        JLabel sizeOfQueen= new JLabel("Size Of Queen");
        j.add(sizeOfQueen,6);
        sizeQSpinner = new JSpinner(new SpinnerNumberModel(queenSize,1,4,1));
        j.add(sizeQSpinner,7);
        
        JLabel axisLength= new JLabel("Length Of Axis");
        j.add(axisLength,8);
        axisSpinner = new JSpinner(new SpinnerNumberModel(worldSize,10,25,5));
        j.add(axisSpinner,9);
        
        JLabel runLabel = new JLabel("Number of Generations");
        j.add(runLabel,10);
        runSpinner = new JSpinner(new SpinnerNumberModel(runs,20,10000,10));
        j.add(runSpinner,11);
        
        return j;
    }
    
    public JComponent pheromoneAlterationsTab(){
        JPanel j = new JPanel();
        j.setLayout(new GridLayout(13,2));
        
        JLabel windS = new JLabel("Wind Strength");
        j.add(windS,0);
        windStrengthSpinner = new JSpinner(new SpinnerNumberModel(wind,0.0,1.0,0.1));
        j.add(windStrengthSpinner,1);
        
        JLabel filler = new JLabel("Cement Phermone Loss Rate");
        j.add(filler,2);
        cEmRateSpinner = new JSpinner(new SpinnerNumberModel(cementEmisRate,0.0,1.0,0.01));
        j.add(cEmRateSpinner,3);
        
        JLabel cementDiffRates = new JLabel("Cement Diffusion Rate");
        j.add(cementDiffRates,4);
        cDRateSpinner = new JSpinner(new SpinnerNumberModel(cementDiffRate,0.0,1.0,0.01));
        j.add(cDRateSpinner,5);
        
        JLabel cementEvapRates = new JLabel("Cement Evaporation Rate");
        j.add(cementEvapRates,6);
        cEvRateSpinner = new JSpinner(new SpinnerNumberModel(cementEvapRate,0.0,1.0,0.01));
        j.add(cEvRateSpinner,7);
        
        JLabel queenEmisRates = new JLabel("Queen Pheromone Level");
        j.add(queenEmisRates,8);
        qEmRateSpinner = new JSpinner(new SpinnerNumberModel(queenEmisRate,0.0,50.0,0.01));
        j.add(qEmRateSpinner,9);
        
        JLabel queenDiffRates = new JLabel("Queen Diffusion Rate");
        j.add(queenDiffRates,10);
        qDRateSpinner = new JSpinner(new SpinnerNumberModel(queenDiffRate,0.0,1.0,0.01));
        j.add(qDRateSpinner,11);
        
        JLabel queenEvapRates = new JLabel("Queen Evaporation Rate");
        j.add(queenEvapRates,12);
        qEvRateSpinner = new JSpinner(new SpinnerNumberModel(queenEvapRate,0.0,1.0,0.01));
        j.add(qEvRateSpinner,13);
        
        JLabel placingProbs = new JLabel("Placing Prob");
        j.add(placingProbs,14);
        placePSpinner = new JSpinner(new SpinnerNumberModel(placingProb,0.0,1.0,0.1));
        j.add(placePSpinner,15);
        
        JLabel pickingProbs = new JLabel("Picking Prob");
        j.add(pickingProbs,16);
        pickPSpinner = new JSpinner(new SpinnerNumberModel(pickingProb,0.0,1.0,0.1));
        j.add(pickPSpinner,17);
        
        JLabel upperQPT = new JLabel("Upper QP Threshold");
        j.add(upperQPT,18);
        uQPTSpinner = new JSpinner(new SpinnerNumberModel(upperQP,0.0,maxQP,0.01));
        j.add(uQPTSpinner,19);
        
        JLabel lowerQPT = new JLabel("Lower QP Threshold");
        j.add(lowerQPT,20);
        lQPTSpinner = new JSpinner(new SpinnerNumberModel(lowerQP,0.0,maxQP,0.01));
        j.add(lQPTSpinner,21);
        
        JLabel maxQPT = new JLabel("Maximum QP level");
        j.add(maxQPT,22);
        mQPSpinner = new JSpinner(new SpinnerNumberModel(maxQP,0.0,26.0,0.01));
        j.add(mQPSpinner,23);
        
        JButton start = new JButton("set values");
        start.setActionCommand("set");
        start.addActionListener(tf);
        j.add(start,24);
        
        return j;
    }
    
    
    public JComponent resultsTab(){
        JPanel j = new JPanel();
        j.setLayout(new GridLayout(13,1));
        
        rateOfDeposition = new JCheckBox("Rate Of Deposition");
        j.add(rateOfDeposition,0);
        
        rateOfDepositionPerTermite = new JCheckBox("Rate Of Deposition Per Termite");
        j.add(rateOfDepositionPerTermite,1);
        
        meanDistanceFromQueen = new JCheckBox("Mean Distance From Queen");
        j.add(meanDistanceFromQueen,2);
        
        timeOfDepositionStages = new JCheckBox("Time To Reach Important Stages");
        j.add(timeOfDepositionStages,3);
        
        meanThicknessOfWall = new JCheckBox("Mean Thickness Of Wall");
        j.add(meanThicknessOfWall,4);
        
        JButton save = new JButton("Save to file");
        save.setActionCommand("save");
        save.addActionListener(tf);
        j.add(save,5);
        
        return j;
    }
    
    public JComponent graphTab(){
        JPanel j = new JPanel();
        j.setLayout(new GridLayout(13,1));
        
        JLabel graphLabel = new JLabel("Click to select y-axis of graph:");
        j.add(graphLabel,0);
        
        JButton rate = new JButton("Rate of Deposition");
        rate.setActionCommand("rate");
        rate.addActionListener(tf);
        j.add(rate,1);
        
        JButton depPerTerm = new JButton("Deposits per Termite");
        depPerTerm.setActionCommand("depPerTerm");
        depPerTerm.addActionListener(tf);
        j.add(depPerTerm,2);
        
        JButton total = new JButton("Total Deposits");
        total.setActionCommand("total");
        total.addActionListener(tf);
        j.add(total,3);
        
        JButton meanX = new JButton("Mean X distance");
        meanX.setActionCommand("meanX");
        meanX.addActionListener(tf);
        j.add(meanX,4);
        
        JButton meanY = new JButton("Mean Y distance");
        meanY.setActionCommand("meanY");
        meanY.addActionListener(tf);
        j.add(meanY,5);
        
        JButton meanZ = new JButton("Mean Z distance");
        meanZ.setActionCommand("meanZ");
        meanZ.addActionListener(tf);
        j.add(meanZ,6);
        
        JButton meanH = new JButton("Mean Hyp distance");
        meanH.setActionCommand("meanH");
        meanH.addActionListener(tf);
        j.add(meanH,7);
        
        JButton thick = new JButton("Mean Thickness");
        thick.setActionCommand("thickness");
        thick.addActionListener(tf);
        j.add(thick,8);
        
        JButton cpInt = new JButton("Cement Pheromone Intensity");
        cpInt.setActionCommand("cp");
        cpInt.addActionListener(tf);
        j.add(cpInt,9);
        
        JButton qpInt = new JButton("Queen Pheromone Intensity");
        qpInt.setActionCommand("qp");
        qpInt.addActionListener(tf);
        j.add(qpInt,10);
        
        JLabel saveToFile = new JLabel("Click to save results so far:");
        j.add(saveToFile,11);
        
        JButton save = new JButton("Save data to file");
        save.setActionCommand("save");
        save.addActionListener(tf);
        j.add(save,12);
        
        return j;
    }
    
    public boolean getRateOfDeposition(){
        return this.rateOfDeposition.isSelected();
    }
    
    public boolean getRateOfDepositionPerTermite(){
        return this.rateOfDepositionPerTermite.isSelected();
    }
    
    public boolean getMeanDistanceFromQueen(){
        return this.meanDistanceFromQueen.isSelected();
    }
    
    public boolean getTimeOfDepositionStages(){
        return this.timeOfDepositionStages.isSelected();
    }
    
    public boolean getMeanThicknessOfWall(){
        return this.meanThicknessOfWall.isSelected();
    }
    
    public void setNumberOfTermites(int num){
        this.NumberOfTermites = num;
        this.numTermSpinner.getModel().setValue(NumberOfTermites);
    }
    
    public void setQueenSpinner(int num){
        this.queenSize = num;
        this.sizeQSpinner.getModel().setValue(queenSize);
    }
    
    public void setLowerQueenBound(double num){
        this.lowerQP = num;
        this.lQPTSpinner.getModel().setValue(lowerQP);
    }
    
    public void setUpperQueenBound(double num){
        this.upperQP = num;
        this.uQPTSpinner.getModel().setValue(upperQP);
    }
    
    public void setQueenDiffusion(double num){
        this.queenDiffRate = num;
        this.qDRateSpinner.getModel().setValue(queenDiffRate);
    }
    
    public void setQueenEvaporation(double num){
        this.queenEvapRate = num;
        this.qEvRateSpinner.getModel().setValue(queenEvapRate);
    }
    
    public void setQueenEmission(double num){
        this.queenEmisRate = num;
        this.qEmRateSpinner.getModel().setValue(queenEmisRate);
    }
    
    public void setCementDiffusion(double num){
        this.cementDiffRate = num;
        this.cDRateSpinner.getModel().setValue(cementDiffRate);
    }
    
    public void setCementEvaporation(double num){
        this.cementEvapRate = num;
        this.cEvRateSpinner.getModel().setValue(cementEvapRate);
    }
    
    public void setCementEmission(double num){
        this.cementEmisRate = num;
        this.cEmRateSpinner.getModel().setValue(cementEmisRate);
    }
    
    public void setPlacingProb(double num){
        this.placingProb = num;
        this.placePSpinner.getModel().setValue(placingProb);
    }
    
    public void setPickingProb(double num){
        this.pickingProb = num;
        this.pickPSpinner.getModel().setValue(pickingProb);
    }
}
