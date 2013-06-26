/*
 * TermiteControlPanel.java
 *
 * Created on 01 October 2007, 11:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package TermiteGraphics;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Container;

/**
 *
 * @author ug87sjs
 */
public class TermiteControlPanel extends JPanel {
    
    /** Creates a new instance of TermiteControlPanel */
    public TermiteControlPanel() {
        
        
    }
    
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        
        g2.drawString("I AM YOUR GOD!",200,225);
    }
    
}
