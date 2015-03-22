package visualization;

import gameboard.Gameboard;
import gameboard.GlobalGameboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import antClient.AIprocessorHuman;

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

public class ClientFrame extends JFrame
{
	SpacePanel display;
	
	
	boolean lock = true;
    /**
     * The constructor.
     */  
     public ClientFrame(AIprocessorHuman _processor)
     {
    	final AIprocessorHuman processor = _processor;
    	
    	processor.setVizFrame(this);
    	   	        
        setTitle("AntWars VizClient");
        setSize(new Dimension(GlobalGameboard.windowWidthClnt, GlobalGameboard.windowHeightClnt));
        setResizable(false);
        
        // Add window listener.
        this.addWindowListener
        (
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    ClientFrame.this.windowClosed();
                }
            }
        ); 
        
        this.addKeyListener
        (
        	new KeyListener()
        	{
				public void keyPressed(java.awt.event.KeyEvent e)
				{
					/*
					 * Key codes:
					 * 37 - left
					 * 39 - right
					 * 38 - up
					 * 40 - down
					 */
					
					if(lock) return;
					
					String move = "";
					
					if(e.getKeyCode() == 37) move = "W";
					if(e.getKeyCode() == 38) move = "N";
					if(e.getKeyCode() == 39) move = "E";
					if(e.getKeyCode() == 40) move = "S";
					
					if(processor.makeMove(move)) lock = true;			
				}

				@Override
				public void keyReleased(java.awt.event.KeyEvent e)
				{
										
				}

				@Override
				public void keyTyped(java.awt.event.KeyEvent e)
				{
									
				}
        	}
        );
        
        GlobalGameboard.setClient();
        
        display = new SpacePanel(processor.getGameboard());
        
		display.setBackground(Color.white);
		
		getContentPane().add(display, BorderLayout.CENTER);
    }


	/**
     * Shutdown procedure when run as an application.
     */
    protected void windowClosed()
    {
        System.exit(0);
    }


	public void updateGameboard(Gameboard gameboard)
	{
		display.updateGameboard(gameboard);
		display.repaint();
	}
	
	public void unlock()
	{
		lock = false;
	}
}

