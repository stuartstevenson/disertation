/*
 * TermitePheromoneFronPanel.java
 *
 * Created on 20 November 2007, 12:26
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
public class TermitePheromoneFrontPanel extends JPanel{
    
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
    
    
    /** Creates a new instance of TermiteFrontViewPanel */
    public TermitePheromoneFrontPanel(TermiteWorld world) {
        this.world = world;
        this.blocks = world.getWorld();
        
        x = world.getXDim();
        y = world.getYDim();
        z = world.getZDim();
        
        boxWidth = wNew/x;
        boxHeight = hNew/y;
        
        this.setBackground(Color.MAGENTA);
    }
    
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.drawString("x axis",200,10);
        g2.drawString("y",10,200);
        
        boolean last = false;
        double sumQP = 0.0;
        double sumCP = 0.0;
        for (int i = 0; i < x; i++) {
            
            for (int j = 0; j < y; j++) {
                
                
                //System.out.println("last is "+last);
                for (int l = 0; l < z; l++) {
                    
                    //System.out.println("block state of block "+i+","+l+","+j+" is "+blocks[i][l][j].getBlockState());
                    if(blocks[i][j][l].getBlockState() == 0){
                        //System.out.println("draw block");
                        sumQP = sumQP + blocks[i][j][l].getQueenPheromone();
                        sumCP = sumCP + blocks[i][j][l].getCementPheromone();
                        //System.out.println("color is "+blocks[i][l][j].getColor());
                        //System.out.println("boxWidth is "+boxWidth);
                        //g2.fill(new Rectangle2D.Double(100,100,boxWidth,boxHeight));
                        
                        
                        
                    }
                    //System.out.println("last is "+last);
                    
                }
                //System.out.println("summed pheromone "+sumQP);
                int qValue = (int)Math.round((Math.sqrt(sumQP)/26.0)*255);
                int cValue = (int)Math.round((Math.sqrt(sumCP)/Math.sqrt(13.0))*255);
                //System.out.println("color value = "+value);
                Color concentrate = new Color(cValue,0,qValue);
                g2.setColor(concentrate);
                g2.fill(new Rectangle2D.Double(startX +(boxWidth*i),startY+(boxHeight*y)-(boxHeight*j)-boxHeight,boxWidth,boxHeight));
                sumCP = 0.0;
                sumQP = 0.0;
            }
            
            
        }
        drawGrid(g2);
    }
    
    
    public void drawGrid(Graphics2D g){
        g.setColor(Color.BLACK);
        for (int i = 0; i <= x; i++) {
            for (int j = 0; j <= z; j++) {
                g.draw(new Line2D.Double(startX+(boxWidth*i),startY+(boxHeight*j),startX+(boxWidth*i),boxHeight*z));
            }
            g.draw(new Line2D.Double(startX,startY+(boxHeight*i),startX+(boxWidth*x),startY+(boxHeight*i)));
        }
        
    }
    
    
    public void updateWorld(Block [][][] world){
        this.blocks = world;
        repaint();
    }
}
