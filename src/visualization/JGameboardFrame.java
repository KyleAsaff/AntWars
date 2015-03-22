package visualization;


import gameboard.Gameboard;
import gameboard.GlobalGameboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * @author Marek Lipczak || lipczak@cs.dal.ca || www.cs.dal.ca/~lipczak/
 * @version 1.0 || 2009-02-11
 *
 */

 /*
  * Whenever you modify the code please describe your contribution here:
  *
 */

public class JGameboardFrame extends JFrame
{
	SpacePanel display;
	
	int mouseX;
	int mouseY;
    /**
     * The constructor.
     */  
     public JGameboardFrame(Gameboard gameboard)
     {
        setTitle("Ant Wars Server Visualization");
        setLocation(GlobalGameboard.windowWidthControl+10, 0);
        setSize(new Dimension(GlobalGameboard.windowWidth, GlobalGameboard.windowHeight));
        setResizable(false);
       		        	        
        display = new SpacePanel(gameboard);

		display.setBackground(Color.white);
		
		getContentPane().add(display, BorderLayout.CENTER);
    }

    public void updateGameboard(Gameboard gameboard)
	{
		display.updateGameboard(gameboard);
		display.repaint();
	}    
}

