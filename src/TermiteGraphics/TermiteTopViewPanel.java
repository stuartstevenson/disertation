/*
 * TemiteTopViewPanel.java
 *
 * Created on 01 October 2007, 11:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package TermiteGraphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Container;
import TermiteProjectMain.Block;
import TermiteProjectMain.TermiteWorld;
import TermiteInstances.WorkerTermite;
import java.awt.geom.AffineTransform;
/**
 *
 * @author ug87sjs
 */
public class TermiteTopViewPanel extends JPanel implements ActionListener{
    
    private Block [][][] blocks = null;
    private TermiteWorld world = null;
    private WorkerTermite [] termites = null;
    
    private int x;
    private int y;
    private int z;
    
    private double startX = 25.0;
    private double startZ = 35.0;
    
    private double height = 450;
    private double width = 500;
    
    private double hNew = height*0.8;
    private double wNew = width*0.8;
    
    private double boxWidth;
    private double boxHeight;
    
    private Rectangle2D box = null;
    
    private boolean cons = false;
    private boolean queen = false;
    private boolean cement = false;
    private boolean both = false;
    private boolean wind = false;
    
    /** Creates a new instance of TemiteTopViewPanel */
    public TermiteTopViewPanel(TermiteWorld world) {
        this.world = world;
        this.blocks = world.getWorld();
        this.termites = world.getTermites();
        
        x = world.getXDim();
        y = world.getYDim();
        z = world.getZDim();
        
        boxWidth = wNew/x;
        boxHeight = hNew/z;
        
        cons = true;
        
        JButton construction = new JButton("Cons");
        construction.setActionCommand("cons");
        construction.addActionListener(this);
        this.add(construction);
        
        JButton queenPher = new JButton("Queen");
        queenPher.setActionCommand("queen");
        queenPher.addActionListener(this);
        this.add(queenPher);
        
        JButton cementPher = new JButton("Cement");
        cementPher.setActionCommand("cement");
        cementPher.addActionListener(this);
        this.add(cementPher);
        
        JButton bothPher = new JButton("Both");
        bothPher.setActionCommand("both");
        bothPher.addActionListener(this);
        this.add(bothPher);
        
        JButton wind = new JButton("Wind");
        wind.setActionCommand("wind");
        wind.addActionListener(this);
        this.add(wind);
        
        
        
    }
    
    public void actionPerformed(ActionEvent e){
        
        if(e.getActionCommand().equals("cons")){
            cons = true;
            queen = false;
            cement = false;
            both = false;
            wind = false;
        }else if(e.getActionCommand().equals("queen")){
            cons = false;
            queen = true;
            cement = false;
            both = false;
            wind = false;
        }else if(e.getActionCommand().equals("cement")){
            cons = false;
            queen = false;
            cement = true;
            both = false;
            wind = false;
        }else if(e.getActionCommand().equals("both")){
            cons = false;
            queen = false;
            cement = false;
            both = true;
            wind = false;
        }else if(e.getActionCommand().equals("wind")){
            cons = false;
            queen = false;
            cement = false;
            both = false;
            wind = true;
        }
        repaint();
        
    }
    
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        
        g2.drawString("x axis",this.getWidth()/2,this.getHeight()-10);
        g2.drawString("z",10,this.getHeight()/2);
        x = world.getXDim();
        y = world.getYDim();
        z = world.getZDim();
        
        boxWidth = (this.getWidth()-2*startX)/x;
        boxHeight = (this.getHeight()-2*startZ)/z;
        if(cons){
            drawConstruction(g2);
        }else if(queen){
            drawQueenPheromone(g2);
        }else if(cement){
            drawCementPheromone(g2);
        }else if(both){
            drawBothPheromones(g2);
        }else if(wind){
            drawWind(g2);
        }
        drawGrid(g2);
    }
    
    /**
     *  shows world from the top
     */
    public void drawConstruction(Graphics2D g2){
        boolean last = false;
        String count = "";
        x = world.getXDim();
        y = world.getYDim();
        z = world.getZDim();
        
        for (int i = 0; i < x; i++) {
            
            for (int j = 0; j < z; j++) {
                last = false;
                for (int l = y-1; l >= 0; l--) {
                    if(blocks[i][l][j].getBlockState() > 0 && !(last)){
                        //System.out.println("draw block");
                        g2.setColor(blocks[i][l][j].getColor());
                        g2.fill(new Rectangle2D.Double(startX+(boxWidth*i),startZ+(boxHeight*j),boxWidth,boxHeight));
                        int sum = countTermites(i,l+1,j);
                        if(sum >0){
                            count = count+sum;
                            g2.setColor(Color.BLACK);
                            g2.drawString(count,(int)(startX+(boxWidth*i)+5),(int)(startZ+(boxHeight*j)+10 + boxHeight/4));
                        }
                        last = true;
                    }
                }
                count = "";
            }
        }
    }
    
    /**
     *  shows queen pheromone from the top
     */
    public void drawQueenPheromone(Graphics2D g2){
        boolean last = false;
        x = world.getXDim();
        y = world.getYDim();
        z = world.getZDim();
        for (int i = 0; i < x; i++) {
            
            for (int j = 0; j < z; j++) {
                if(blocks[i][1][j].getBlockState()== 0){
                    int qValue = (int)Math.round((Math.sqrt(blocks[i][1][j].getQueenPheromone())/Math.sqrt(26.0))*255);
                    if(qValue > 255) qValue = 255;
                    if(qValue < 0) qValue = 0;
                    Color concentrate = new Color(0,0,qValue);
                    g2.setColor(concentrate);
                }else if(blocks[i][1][j].getBlockState()== 2){
                    g2.setColor(Color.RED);
                    
                }else {
                    g2.setColor(Color.BLUE);
                }
                
                g2.fill(new Rectangle2D.Double(startX+(boxWidth*i),startZ+(boxHeight*j),boxWidth,boxHeight));
                
            }
            
        }
    }
    
    /**
     *  shows cement pheromone from the top
     */
    public void drawCementPheromone(Graphics2D g2){
        boolean last = false;
        x = world.getXDim();
        y = world.getYDim();
        z = world.getZDim();
        for (int i = 0; i < x; i++) {
            
            for (int j = 0; j < z; j++) {
                if(blocks[i][1][j].getBlockState()== 0){
                    int cValue = (int)Math.round((Math.sqrt(blocks[i][1][j].getCementPheromone()))*255);
                    if(cValue > 255) cValue = 255;
                    Color concentrate = new Color(cValue,0,0);
                    g2.setColor(concentrate);
                }else if(blocks[i][1][j].getBlockState()== 2){
                    g2.setColor(Color.RED);
                    
                }else {
                    g2.setColor(Color.BLUE);
                }
                
                g2.fill(new Rectangle2D.Double(startX+(boxWidth*i),startZ+(boxHeight*j),boxWidth,boxHeight));
                
            }
            
        }
    }
    
    /**
     *  shows both pheromone from the top
     */
    public void drawBothPheromones(Graphics2D g2){
        boolean last = false;
        x = world.getXDim();
        y = world.getYDim();
        z = world.getZDim();
        for (int i = 0; i < x; i++) {
            
            for (int j = 0; j < z; j++) {
                if(blocks[i][1][j].getBlockState()== 0){
                    //System.out.println("draw block");
                    int qValue = (int)Math.round((Math.sqrt(blocks[i][1][j].getQueenPheromone())/Math.sqrt(26.0))*255);
                    int cValue = (int)Math.round((Math.sqrt(blocks[i][1][j].getCementPheromone())/Math.sqrt(1.0))*255);
                    //System.out.println("color value = "+value);
                    if(cValue > 255) cValue = 255;
                    if(qValue > 255) qValue = 255;
                    Color concentrate = new Color(cValue,0,qValue);
                    g2.setColor(concentrate);
                }else if(blocks[i][1][j].getBlockState()== 2){
                    g2.setColor(Color.RED);
                    
                }else {
                    g2.setColor(Color.BLUE);
                }
                
                g2.fill(new Rectangle2D.Double(startX+(boxWidth*i),startZ+(boxHeight*j),boxWidth,boxHeight));
                
            }
            
        }
    }
    
    /**
     *  shows wind velocities from the front
     */
    public void drawWind(Graphics2D g2){
        boolean last = false;
        String count = "";
        x = world.getXDim();
        y = world.getYDim();
        z = world.getZDim();
        for (int i = 0; i < x; i++) {
            
            for (int j = 0; j < z; j++) {
                last = false;
                for (int l = y-1; l >= 0; l--) {
                    if(blocks[i][l][j].getBlockState() > 0 && !(last)){
                        //System.out.println("draw block");
                        if(blocks[i][l][j].getBlockState() == 1){
                            g2.setColor(Color.BLACK);
                        }else if(blocks[i][l][j].getBlockState() == 2){
                            g2.setColor(Color.RED);
                        }else if(blocks[i][l][j].getBlockState() == 3){
                            g2.setColor(Color.BLUE);
                        }
                        
                        g2.fill(new Rectangle2D.Double(startX+(boxWidth*i),startZ+(boxHeight*j),boxWidth,boxHeight));
                        drawWind( blocks[i][l+1][j], (int)(startX+(boxWidth*i)+5),(int)(startZ+(boxHeight*j)+5),g2 );
                        last = true;
                        
                    }
                }
                count = "";
                
            }
            
        }
    }
    
    /**
     *  sets rotation by velocity components and draws an arrow
     */
    public void drawWind(Block b, int x, int z, Graphics2D g2){
        int velX = b.getVelX();
        int velZ = b.getVelZ();
        
        AffineTransform at = new AffineTransform();
        double theta = 0;
        
        //System.out.println(velX);
        
        if(velX != 0 || velZ != 0){
            
            // set rotation
            if(velX == 1 && velZ == 0){
                theta = Math.PI/2;
            }else if(velX == 1 && velZ == 1){
                theta = (3*Math.PI)/4;
            }else if(velX == 0 && velZ == 1){
                theta = Math.PI;
            }else if(velX == 1 && velZ == -1){
                theta = Math.PI/4;
            }else if(velX == -1 && velZ == 1){
                theta = -(3*Math.PI)/4;
            }else if(velX == -1 && velZ == 0){
                theta = -Math.PI/2;
            }else if(velX == 0 && velZ == -1){
                theta = 0;
            }else if(velX == -1 && velZ == -1){
                theta = -Math.PI/4;
            }
            //System.out.println(theta);
            at.setToRotation(theta,x+5,z+(boxHeight/4));
            g2.setTransform(at);
            drawArrow(x,z,g2);
            at.setToIdentity();
            g2.setTransform(at);
        }
        
        
    }
    
    public void drawArrow(int x, int z, Graphics2D g2){
        g2.setColor(Color.WHITE);
        g2.drawLine(x+5, z+2, x+5, z+7);
        g2.drawLine(x+5,z+2,x+4,z+3);
        g2.drawLine(x+5,z+2,x+6,z+3);
    }
    
    public int countTermites(int x, int y, int z){
        int sum = 0;
        WorkerTermite t = null;
        for (int i = 0; i < termites.length; i++) {
            t = termites[i];
            if(t.getXPos() == x && t.getYPos() == y && t.getZPos() == z) sum = sum+1;
        }
        return sum;
        
    }
    
    public void drawGrid(Graphics2D g){
        g.setColor(Color.BLACK);
//        for (int i = 0; i <= x; i++) {
//            for (int j = 0; j <= z; j++) {
//                // vertical lines
//                g.draw(new Line2D.Double(startX+(boxWidth*i),startZ+(boxHeight*j),startX+(boxWidth*i),boxHeight*z));
//            }
//            //  horizontal lines
//            g.draw(new Line2D.Double(startX,startZ+(boxHeight*i),startX+(boxWidth*x),startZ+(boxHeight*i)));
//        }
        
        for (int i = 0; i <= x; i++) {
            for (int j = 0; j <= z; j++) {
                // vertical lines
                g.draw(new Line2D.Double(startX+(((this.getWidth()-2*startX)/x)*i),startZ,startX+(((this.getWidth()-2*startX)/x)*i),startZ+(((this.getHeight()-2*startZ)/z)*j)));
            }
            //  horizontal lines
            g.draw(new Line2D.Double(startX,startZ+(((this.getHeight()-2*startZ)/z)*i),startX+(((this.getWidth()-2*startX)/x)*x),startZ+(((this.getHeight()-2*startZ)/z)*i)));
        }
        
    }
    
    
    public void updateWorld(Block [][][] world, TermiteWorld w, WorkerTermite [] t){
        this.blocks = world;
        this.world = w;
        this.termites = t;
        repaint();
    }
}
