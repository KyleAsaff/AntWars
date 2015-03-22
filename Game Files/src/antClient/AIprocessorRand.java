package antClient;

import gameboard.Ant;
import gameboard.Field;
import gameboard.Gameboard;
import gameboard.GlobalGameboard;

/**
 * Implementation of Rand player - a sample automatic AntWars player. The Rand player makes in each round a random move,
 * uniformly choosing from a list of currently available moves. 
 * 
 * @author Marek Lipczak || lipczak@cs.dal.ca || www.cs.dal.ca/~lipczak/
 * @version 1.0 || 2009-02-10
 *
 */

 /*
  * Whenever you modify the code please describe your contribution here:
  *
 */

public class AIprocessorRand extends AIprocessor
{
	public void startGameSignal(int position)
	{
		if(GlobalGameboard.doEcho) System.out.println("Game started");
		//I don't care...
	}
	
	public void setCurrentPosition(String xmlPosition)
	{
		if(GlobalGameboard.doEcho) System.out.println("Current position: "+xmlPosition);
		//I don't care	
	}

	/**
	 * This is the main function of any automatic player.
	 * The player receives a new game board setting and it should make decision about the move.
	 * There are five available moves up <code>N</code>, right <code>E</code>, down <code>S</code>, left <code>W</code> and stay <code>stay</code>
	 * The move should be submitted to the server using {@link sendMove(String move)} function.
	 * 
	 * @param gameboard extraction of the game board that shows the fields around the player's ant
	 */
	
	public void setGameboard(Gameboard gameboard)
	{
		int moveX = 0;
		int moveY = 0;
		
		Field[][] fields =  gameboard.getFields();
		int centerX = fields.length/2;
		int centerY = fields[centerX].length/2;
		
		/*
		 * Inside the loop the processor picks random coordinates of move {-1, 0 ,1}.
		 * Coordinates are not accepted when the move is not done in a vertical or horizontal axis,
		 * or when the new field is not movable (e.g., it is a wall field)
		 */
		
		while(moveX*moveX  + moveY*moveY != 1 || !gameboard.isMoveable(centerX+moveX, centerY+moveY))
		{
			moveX = new Double(Math.random()*3).intValue()-1;
			moveY = new Double(Math.random()*3).intValue()-1;
		}
				
		String move = "";
		
		if(moveY == -1) move = "N";
		if(moveX ==  1) move = "E";
		if(moveY ==  1) move = "S";
		if(moveX == -1) move = "W";
				
		Ant ant = fields[centerX][centerY].getAnt();
		sendMove(move);
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
