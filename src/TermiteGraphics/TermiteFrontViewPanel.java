/*
 * TermiteFrontViewPanel.java
 *
 * Created on 01 October 2007, 11:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package TermiteGraphics;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.Container;
import TermiteProjectMain.Block;
import TermiteProjectMain.TermiteWorld;
import java.awt.geom.AffineTransform.*;
/**
 *
 * @author ug87sjs
 */
public class TermiteFrontViewPanel extends JPanel implements ActionListener{
    
    private Block [][][] blocks = null;
    private TermiteWorld world = null;
    
    
    
    private int x;
    private int y;
    private int z;
    
    private double startX = 25.0;
    private double startY = 25.0;
    
    private double height = 450;
    private double width = 500;
    
    private double hNew = height*0.8;
    private double wNew = width*0.8;
    
    private double boxWidth;
    private double boxHeight;
    
    private boolean cons = false;
    private boolean queen = false;
    private boolean cement = false;
    private boolean both = false;
    private boolean wind = false;
    
    
    /** Creates a new instance of TermiteFrontViewPanel */
    public TermiteFrontViewPanel(TermiteWorld world)  {
        this.world = world;
        this.blocks = world.getWorld();
        
        x = world.getXDim();
        y = world.getYDim();
        z = world.getZDim();
        
        boxWidth = wNew/x;
        boxHeight = hNew/y;
        
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
        g2.drawString("x axis",this.getWidth()/2,this.getHeight()-30);
        g2.drawString("y",10,this.getHeight()/2);
        
        x = world.getXDim();
        y = world.getYDim();
        z = world.getZDim();
        boxWidth = (this.getWidth()-2*startX)/x;
        boxHeight = (this.getHeight()-2*startY)/y;
        
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
     *  interface that shows construction in world
     */
    public void drawConstruction(Graphics2D g2){
        boolean last = false;
        x = world.getXDim();
        y = world.getYDim();
        z = world.getZDim();
        
        
        for (int i = 0; i < x; i++) {
            
            for (int j = 0; j < y; j++) {
                last = false;
                for (int l = 0; l < z; l++) {
                    if(blocks[i][j][l].getBlockState() > 0 && !(last)){
                        g2.setColor(blocks[i][j][l].getColor());
                        g2.fill(new Rectangle2D.Double(startX +(boxWidth*i),startY+(boxHeight*y)-(boxHeight*j)-boxHeight,boxWidth,boxHeight));
                        last = true;
                        
                    }else if(blocks[i][j][l].getBlockState() == 0 && l == z-1 && !last){
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.fill(new Rectangle2D.Double(startX +(boxWidth*i),startY+(boxHeight*y)-(boxHeight*j)-boxHeight,boxWidth,boxHeight));
                    }                    
                }                
            }            
        }
    }
    
    /**
     *  shows pheromone spread from the front
     */
    public void drawQueenPheromone(Graphics2D g2){
        boolean last = false;
        double sumQP = 0.0;
        double sumCP = 0.0;
        x = world.getXDim();
        y = world.getYDim();
        z = world.getZDim();
        for (int i = 0; i < x; i++) {
            
            for (int j = 0; j < y; j++) {
                for (int l = 0; l < z; l++) {
                    if(blocks[i][j][l].getBlockState() == 0){
                        sumQP = sumQP + blocks[i][j][l].getQueenPheromone();
                    }
                }
                int qValue = (int)Math.round((Math.sqrt(sumQP)/Math.sqrt(26.0))*255);
                if(qValue > 255) qValue = 255;
                if(qValue < 0) qValue = 0;
                Color concentrate = new Color(0,0,qValue);
                g2.setColor(concentrate);
                g2.fill(new Rectangle2D.Double(startX +(boxWidth*i),startY+(boxHeight*y)-(boxHeight*j)-boxHeight,boxWidth,boxHeight));
                sumCP = 0.0;
                sumQP = 0.0;
            }
            
            
        }
    }

    /**
     *  shows pheromone spread from the front
     */    
    public void drawCementPheromone(Graphics2D g2){
        boolean last = false;
        double sumQP = 0.0;
        double sumCP = 0.0;
        x = world.getXDim();
        y = world.getYDim();
        z = world.getZDim();
        for (int i = 0; i < x; i++) {
            
            for (int j = 0; j < y; j++) {
                for (int l = 0; l < z; l++) {
                    if(blocks[i][j][l].getBlockState() == 0){
                        sumCP = sumCP + blocks[i][j][l].getCementPheromone();
                    }
                    
                }
                int cValue = (int)Math.round((sumCP/Math.sqrt(1.0))*255);
                if(cValue > 255) cValue = 255;
                Color concentrate = new Color(cValue,0,0);
                g2.setColor(concentrate);
                g2.fill(new Rectangle2D.Double(startX +(boxWidth*i),startY+(boxHeight*y)-(boxHeight*j)-boxHeight,boxWidth,boxHeight));
                sumCP = 0.0;
                sumQP = 0.0;
            }
            
        }
    }

    /**
     *  shows pheromones spread from the front
     */    
    public void drawBothPheromones(Graphics2D g2){
        boolean last = false;
        double sumQP = 0.0;
        double sumCP = 0.0;
        x = world.getXDim();
        y = world.getYDim();
        z = world.getZDim();
        for (int i = 0; i < x; i++) {
            
            for (int j = 0; j < y; j++) {
                for (int l = 0; l < z; l++) {
                    if(blocks[i][j][l].getBlockState() == 0){
                        //System.out.println("draw block");
                        sumQP = sumQP + blocks[i][j][l].getQueenPheromone();
                        sumCP = sumCP + blocks[i][j][l].getCementPheromone();
                    }
                }
                //System.out.println("summed pheromone "+sumQP);
                int qValue = (int)Math.round((Math.sqrt(sumQP)/26.0)*255);
                int cValue = (int)Math.round((Math.sqrt(sumCP)/Math.sqrt(1.0))*255);
                //System.out.println("color value = "+value);
                if(cValue > 255) cValue = 255;
                if(qValue > 255) qValue = 255;
                Color concentrate = new Color(cValue,0,qValue);
                g2.setColor(concentrate);
                g2.fill(new Rectangle2D.Double(startX +(boxWidth*i),startY+(boxHeight*y)-(boxHeight*j)-boxHeight,boxWidth,boxHeight));
                sumCP = 0.0;
                sumQP = 0.0;
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
            
            for (int j = 0; j < y; j++) {
                last = false;
                if(blocks[i][j][(int)z/2].getBlockState() > 0 && !(last)){
                    //System.out.println("draw block");
                    if(blocks[i][j][(int)z/2].getBlockState() == 1){
                        g2.setColor(Color.BLACK);
                    }else if(blocks[i][j][(int)z/2].getBlockState() == 2){
                        g2.setColor(Color.RED);
                    }else if(blocks[i][j][(int)z/2].getBlockState() == 3){
                        g2.setColor(Color.BLUE);
                    }
                    g2.fill(new Rectangle2D.Double(startX +(boxWidth*i),startY+(boxHeight*y)-(boxHeight*j)-boxHeight,boxWidth,boxHeight));
                    drawWind( blocks[i][j][(int)z/2], (int)(startX+(boxWidth*i)+5),(int)(startY+(boxHeight*y)-(boxHeight*j)-boxHeight)+5,g2 );
                    last = true;
                    
                }else if(blocks[i][j][(int)z/2].getBlockState() == 0 ){
                    g2.setColor(Color.BLACK);
                    g2.fill(new Rectangle2D.Double(startX +(boxWidth*i),startY+(boxHeight*y)-(boxHeight*j)-boxHeight,boxWidth,boxHeight));
                    drawWind( blocks[i][j][(int)z/2], (int)(startX+(boxWidth*i)+5),(int)(startY+(boxHeight*y)-(boxHeight*j)-boxHeight)+5,g2 );
                }else{
                    g2.setColor(Color.BLACK);
                    g2.fill(new Rectangle2D.Double(startX +(boxWidth*i),startY+(boxHeight*y)-(boxHeight*j)-boxHeight,boxWidth,boxHeight));
                    //drawWind( blocks[(int)x/2][j][i], (int)(startX+(boxWidth*i)+5),(int)(startY+(boxHeight*y)-(boxHeight*j)-boxHeight)+5,g2 );
                }
                //System.out.println("last is "+last);
                
                count = "";
                
            }
            
        }
    }
    
    /**
     *  sets rotation by velocity components and draws an arrow
     */
    public void drawWind(Block b, int x, int z, Graphics2D g2){
        int velX = b.getVelX();
        int velZ = -b.getVelY();
        
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
            at.setToRotation(theta,x+5,z+5);
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
    
    
    public void drawGrid(Graphics2D g){
        g.setColor(Color.BLACK);
        
        for (int i = 0; i <= x; i++) {
            for (int j = 0; j <= z; j++) {
                // vertical lines
                g.draw(new Line2D.Double(startX+(((this.getWidth()-2*startX)/x)*i),startY,startX+(((this.getWidth()-2*startX)/x)*i),startY+(((this.getHeight()-2*startY)/y)*j)));
            }
            //  horizontal lines
            g.draw(new Line2D.Double(startX,startY+(((this.getHeight()-2*startY)/y)*i),startX+(((this.getWidth()-2*startX)/x)*x),startY+(((this.getHeight()-2*startY)/y)*i)));
        }
        
    }
    
    
    public void updateWorld(Block [][][] world, TermiteWorld w){
        this.world = w;
        this.blocks = world;
        repaint();
    }
    
}
