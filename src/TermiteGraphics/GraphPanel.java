/*
 * GraphPanel.java
 *
 * Created on 14 March 2008, 15:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package TermiteGraphics;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
/**
 *
 * @author stu
 */
public class GraphPanel extends JPanel {
    
    private double [] yvals = {1.0,2.0,3.0,4.0,5.0};
    private int index = 3;
    private String flag = "rate";
    
    private double max = 5.0;
    
    
    /** Creates a new instance of GraphPanel */
    public GraphPanel() {
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        drawAxis(g2);
        //max = yvals[0];
        int x1 = 30;
        int y1 = this.getHeight()-30;
        int x2 = 0;
        int y2 = 0;
        g2.setColor(Color.RED);
        for (int j = 0; j < index; j++) {
            //f(yvals[j] > max) max = yvals[j];
            double x = (j+1.0)/yvals.length;            
            x2 = 30 + (int)(x*(this.getWidth()-60));
            //System.out.println("x2: "+x2+","+x); 
            double y = (yvals[j]/(max*2))*(this.getHeight()-60);
            y2 = (this.getHeight()-30) - (int)y;
            //System.out.println("y2: "+y2);
            
            g2.drawLine(x1,y1,x2,y2);
            //g2.drawString(""+x2+","+y2,x2,y2);
            x1 = x2;
            y1 = y2;
        }
        g2.setColor(Color.BLACK);
        
    }
    
    public void drawAxis(Graphics2D g2){
        
        //g2.drawString(""+this.getHeight()+"+"+this.getWidth(),100,100);
        g2.drawLine(30,30,30,this.getHeight()-30);
        g2.drawLine(30,this.getHeight()-30,this.getWidth()-30,this.getHeight()-30);
        g2.drawString("Runs",this.getWidth()/2,this.getHeight()-5);
        
        // x axis
        double d = 0.2;
        for (int i = 0; i < 6; i++) {
            g2.drawLine(30+(int)((this.getWidth()-60)*(0.2*i)),this.getHeight()-30,30+(int)((this.getWidth()-60)*(0.2*i)),this.getHeight()-27);
            g2.drawString(""+(int)(yvals.length*(0.2*i)),24+(int)((this.getWidth()-60)*(0.2*i)),this.getHeight()-15);
        }
        /*
        g2.drawLine(30+(int)((this.getWidth()-60)*0.2),this.getHeight()-30,30+(int)((this.getWidth()-60)*0.2),this.getHeight()-27);
        g2.drawString(""+yvals.length*0.2,24+(int)((this.getWidth()-60)*0.2),this.getHeight()-15);
        g2.drawLine(30+(int)((this.getWidth()-60)*0.4),this.getHeight()-30,30+(int)((this.getWidth()-60)*0.4),this.getHeight()-27);
        g2.drawString(""+yvals.length*0.4,24+(int)((this.getWidth()-60)*0.4),this.getHeight()-15);
        g2.drawLine(30+(int)((this.getWidth()-60)*0.6),this.getHeight()-30,30+(int)((this.getWidth()-60)*0.6),this.getHeight()-27);
        g2.drawString(""+yvals.length*0.6,24+(int)((this.getWidth()-60)*0.6),this.getHeight()-15);
        g2.drawLine(30+(int)((this.getWidth()-60)*0.8),this.getHeight()-30,30+(int)((this.getWidth()-60)*0.8),this.getHeight()-27);
        g2.drawString(""+yvals.length*0.8,24+(int)((this.getWidth()-60)*0.8),this.getHeight()-15);
        g2.drawLine(30+(int)((this.getWidth()-60)),this.getHeight()-30,30+(int)((this.getWidth()-60)),this.getHeight()-27);
        g2.drawString(""+yvals.length,24+(int)((this.getWidth()-60)),this.getHeight()-15);
        */
        // y axis
        AffineTransform at = new AffineTransform();
        //at.setToTranslation(5,this.getHeight()/2);
        at.setToRotation(Math.toRadians(-90));
        g2.setTransform(at);
        g2.drawString(flag+"",-this.getHeight()/2,13);
        at.setToRotation(0);
        g2.setTransform(at);
        
        //g2.drawLine()
        for (int i = 5; i >= 0; i--) {
            g2.drawLine(30,30+(int)((this.getHeight()-60)*(0.2*i)),27,30+(int)((this.getHeight()-60)*(0.2*i)));
            String label = ""+(max*2)*(1-0.2*i);
            label = label.substring(0,3);
            g2.drawString(label,10,35+(int)((this.getHeight()-60)*(0.2*i)));
        }
        
    }
    
    public void setYParam(double [] vals, int r, String flag){
        yvals = vals;
        index = r;
        this.flag = flag;
        if(index > 0) findMax();
        repaint();
    }
    
    public void findMax(){
        max = yvals[0];
        for (int i = 0; i < index; i++) {
            if(yvals[i] > max) max = yvals[i];
        }
    }
    
}
