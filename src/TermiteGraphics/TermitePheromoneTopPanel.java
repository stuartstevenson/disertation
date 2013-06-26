/*
 * TermiteControlPanel.java
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

/**
 *
 * @author ug87sjs
 */
public class TermitePheromoneTopPanel extends JPanel {
    
    private Block [][][] blocks = null;
    private TermiteWorld world = null;
    
    
    
    private int x;
    private int y;
    private int z;
    
    private double startX = 25.0;
    private double startZ = 25.0;
    
    private double height = 450;
    private double width = 500;
    
    private double hNew = height*0.8;
    private double wNew = width*0.8;
    
    private double boxWidth;
    private double boxHeight;
    
    private Rectangle2D box = null;
    
    /** Creates a new instance of TemiteTopViewPanel */
    public TermitePheromoneTopPanel(TermiteWorld world) {
        this.world = world;
        this.blocks = world.getWorld();
        
        x = world.getXDim();
        y = world.getYDim();
        z = world.getZDim();
        
        boxWidth = wNew/x;
        boxHeight = hNew/z;
        System.out.println("height is "+height);
        //this.setBackground(Color.BLACK);
    }
    
    public void paintComponent(Graphics g){
        
        
        Graphics2D g2 = (Graphics2D)g;
        
        g2.drawString("x axis",200,10);
        g2.drawString("z",10,200);
        
        boolean last = false;

        // do graphics
        //System.out.println("x"+x);
        //System.out.println("y"+y);
        //System.out.println("z"+z);
        for (int i = 0; i < x; i++) {
            
            for (int j = 0; j < z; j++) {
                
                

               
                    
                    //System.out.println("block state of block "+i+","+l+","+j+" is "+blocks[i][l][j].getBlockState());
                    if(blocks[i][1][j].getBlockState()== 0){
                        //System.out.println("draw block");
                        int qValue = (int)Math.round((Math.sqrt(blocks[i][1][j].getQueenPheromone())/Math.sqrt(26.0))*255);
                        //int cValue = (int)Math.round((Math.sqrt(blocks[i][1][j].getCementPheromone())/Math.sqrt(13.0))*255);
                        //System.out.println("color value = "+value);
                        Color concentrate = new Color(0,0,qValue);
                        g2.setColor(concentrate);
                        //System.out.println("color is "+blocks[i][l][j].getColor());
                        
                        //System.out.println("boxWidth is "+boxWidth);
                        
                        //g2.fill(new Rectangle2D.Double(100,100,boxWidth,boxHeight));                    

                    }else if(blocks[i][1][j].getBlockState()== 2){
                        g2.setColor(Color.RED);
                    
                    }else {
                        g2.setColor(Color.BLUE);                        
                    }

                g2.fill(new Rectangle2D.Double(startX+(boxWidth*i),startZ+(boxHeight*j),boxWidth,boxHeight));                    

            }
            
        }
        drawGrid(g2);
    }
    
    public void drawGrid(Graphics2D g){
        g.setColor(Color.BLACK); 
        for (int i = 0; i <= x; i++) {
            for (int j = 0; j <= z; j++) {
                g.draw(new Line2D.Double(startX+(boxWidth*i),startZ+(boxHeight*j),startX+(boxWidth*i),boxHeight*z));
            }
            g.draw(new Line2D.Double(startX,startZ+(boxHeight*i),startX+(boxWidth*x),startZ+(boxHeight*i)));
        }
        
    }
    
    
    public void updateWorld(Block [][][] world){
        this.blocks = world;
        repaint();
    }
    
}
