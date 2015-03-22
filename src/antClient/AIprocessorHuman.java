package antClient;

import visualization.ClientFrame;
import gameboard.Ant;
import gameboard.Field;
import gameboard.Gameboard;
import gameboard.GlobalGameboard;

/**
 * A visual AntWars player, the move decisions are entered by a user via simple GUI.
 * 
 * @author Marek Lipczak || lipczak@cs.dal.ca || www.cs.dal.ca/~lipczak/
 * @version 1.0 || 2009-02-10
 *
 */

 /*
  * Whenever you modify the code please describe your contribution here:
  *
 */

public class AIprocessorHuman extends AIprocessor
{
	Gameboard gameboard;
	ClientFrame vizFrame;
	
	public AIprocessorHuman(Gameboard gameboard)
	{
		this.gameboard = gameboard;
	}
	
	public void setVizFrame(ClientFrame vizFrame)
	{
		this.vizFrame = vizFrame;
	}
	
	public Gameboard getGameboard()
	{
		return gameboard;
	}
	
	public void startGameSignal(int position)
	{
		if(GlobalGameboard.doEcho) System.out.println("Game started");
		//I don't care
	}
	
	public void setGameboard(Gameboard gameboard)
	{
		this.gameboard = gameboard;
		vizFrame.updateGameboard(gameboard);
		vizFrame.unlock();
	}

	public boolean makeMove(String move)
	{
		Field[][] fields =  gameboard.getFields();
		int centerX = fields.length/2;
		int centerY = fields[centerX].length/2;
		
		if((move.equals("N") && !gameboard.isMoveable(centerX, centerY-1)) ||
		   (move.equals("E") && !gameboard.isMoveable(centerX+1, centerY)) ||
		   (move.equals("S") && !gameboard.isMoveable(centerX, centerY+1)) ||
		   (move.equals("W") && !gameboard.isMoveable(centerX-1, centerY)))
		{
			return false;
		}
		
		Ant ant = fields[centerX][centerY].getAnt();
		if(ant == null) return false;
		ant.makeMove(move);
		vizFrame.updateGameboard(gameboard);
		
		sendMove(move);
		return true;
	}

	@Override
	public void setCurrentPosition(String xmlPosition)
	{
		if(GlobalGameboard.doEcho) System.out.println("new position: "+xmlPosition);
		//I don't care
	}
	
	public void gotKilled()
	{
		if(GlobalGameboard.doEcho) System.out.println("I'm dead");
		isDead = true;		
	}
	
	public void endGameSignal(String msg)
	{
		if(GlobalGameboard.doEcho) System.out.println("Game ended");
		//I don't care
	}
}
