package visualization;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import gameboard.Field;
import gameboard.Gameboard;
import gameboard.GlobalGameboard;

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

public class SpacePanel extends JPanel
{
	Gameboard gameboard;
	private Color[] playerColor;
	
	private Line2D[] linesV;
	private Line2D[] linesH;

	Shape[][] vizFields;
	Color[][] vizFieldsColor;
	
	public SpacePanel(Gameboard gameboard)
	{
		playerColor = new Color[500];
		
		playerColor[0] = new Color(100, 149, 237);
		playerColor[1] = new Color(0, 255, 255);
		playerColor[2] = new Color(0, 100, 0);
		playerColor[3] = new Color(124, 252, 0);
		playerColor[7] = new Color(240, 230, 140);
		playerColor[5] = new Color(255, 215, 0);
		playerColor[6] = new Color(205, 92, 92);
		playerColor[4] = new Color(178, 34, 34);
		playerColor[8] = new Color(255, 0 ,0);
		playerColor[9] = new Color(255, 20, 147);
		playerColor[10] = new Color(138, 43, 226);
		
		int r, g, b;
				
		for(int i = 11; i < playerColor.length; i++)
		{
			r = new Double(16*Math.random()*16).intValue();
			g = new Double(16*Math.random()*16).intValue();
			b = new Double(16*Math.random()*16).intValue();
			
			playerColor[i] = new Color(r, g, b);
		}
		
		updateGameboard(gameboard);
	}
	
	public void updateGameboard(Gameboard gameboard)
	{
		Field[][] fields = gameboard.getFields();
		
		linesV = new Line2D[fields.length+1];
		linesH = new Line2D[fields[0].length+1];
				
		for(int i = 0; i < linesV.length; i++)
		{
			linesV[i] = new Line2D.Double(new Point2D.Double(GlobalGameboard.vizBeginX+i*GlobalGameboard.fieldSize, GlobalGameboard.vizBeginY),
					                      new Point2D.Double(GlobalGameboard.vizBeginX+i*GlobalGameboard.fieldSize, GlobalGameboard.vizBeginY+(linesH.length-1)*GlobalGameboard.fieldSize));
		}
		
		for(int i = 0; i < linesH.length; i++)
		{
			linesH[i] = new Line2D.Double(new Point2D.Double(GlobalGameboard.vizBeginX, GlobalGameboard.vizBeginY+i*GlobalGameboard.fieldSize),
					                      new Point2D.Double(GlobalGameboard.vizBeginX+(linesV.length-1)*GlobalGameboard.fieldSize, GlobalGameboard.vizBeginY+i*GlobalGameboard.fieldSize));
		}
		
		vizFields = new Shape[fields.length][fields[0].length];
		vizFieldsColor = new Color[fields.length][fields[0].length];
				
		for(int i = 0; i < fields.length; i++)
		{
			for(int j = 0; j < fields[i].length; j++)
			{
				if(fields[i][j].getType() == 1)
				{
					vizFields[i][j] = new Rectangle2D.Double(GlobalGameboard.vizBeginX+2+i*GlobalGameboard.fieldSize,
							                                 GlobalGameboard.vizBeginY+2+j*GlobalGameboard.fieldSize,
							                                 GlobalGameboard.fieldSize-2,
							                                 GlobalGameboard.fieldSize-2);
					
					vizFieldsColor[i][j] = Color.gray;
				}
								
				if(fields[i][j].getType() == 0 && fields[i][j].hasFood())
				{
					vizFields[i][j] = new Rectangle2D.Double(GlobalGameboard.vizBeginX+4+i*GlobalGameboard.fieldSize,
							                                 GlobalGameboard.vizBeginY+4+j*GlobalGameboard.fieldSize,
							                                 GlobalGameboard.fieldSize-7,
							                                 GlobalGameboard.fieldSize-7);
					
					vizFieldsColor[i][j] = Color.yellow;
				}
				
				if(fields[i][j].getType() == 2)
				{
					if(fields[i][j].isDoorOpen())
					{
						vizFields[i][j] = new Rectangle2D.Double(GlobalGameboard.vizBeginX+1+i*GlobalGameboard.fieldSize,
                                GlobalGameboard.vizBeginY+1+j*GlobalGameboard.fieldSize,
                                GlobalGameboard.fieldSize-2,
                                GlobalGameboard.fieldSize-2);
						vizFieldsColor[i][j] = Color.green;
					}
					else
					{
						vizFields[i][j] = new Rectangle2D.Double(GlobalGameboard.vizBeginX+2+i*GlobalGameboard.fieldSize,
                                GlobalGameboard.vizBeginY+2+j*GlobalGameboard.fieldSize,
                                GlobalGameboard.fieldSize-2,
                                GlobalGameboard.fieldSize-2);
						vizFieldsColor[i][j] = Color.red;
					}
				}
				
				if((fields[i][j].getType() == 0 || fields[i][j].getType() == 2) && fields[i][j].getAntId() >= 0)
				{
					vizFields[i][j] = new Ellipse2D.Double(GlobalGameboard.vizBeginX+2+i*GlobalGameboard.fieldSize,
							                                 GlobalGameboard.vizBeginY+2+j*GlobalGameboard.fieldSize,
							                                 GlobalGameboard.fieldSize-3,
							                                 GlobalGameboard.fieldSize-3);
					
					vizFieldsColor[i][j] = playerColor[fields[i][j].getAntId()];
				}
			}
		}	
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		for(int i = 0; i < linesV.length; i++)
		{
			g2.setColor(Color.gray);
			g2.draw(linesV[i]);
		}
		
		for(int i = 0; i < linesH.length; i++)
		{
			g2.setColor(Color.gray);
			g2.draw(linesH[i]);
		}
		
		for(int i = 0; i < vizFields.length; i++)
		{
			for(int j = 0; j < vizFields[i].length; j++)
			{
				if(vizFields[i][j] != null)
				{
					g2.setColor(vizFieldsColor[i][j]);
					g2.fill(vizFields[i][j]);
				}
			}
		}	
	}
}