package antServer;

import visualization.ControlFrame;
import gameboard.Ant;
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

public class Referee extends Thread
{
	TournamentRunner statistician;
	ControlFrame controlFrame;
	GameRunner gameRunner;
	
	boolean playerFlag = false;
	boolean vizFlag = false;
	
	long playerTime;
	long vizTime;
	
	int nrOfRounds = 0;
	int nrOfPlayers = 2;
	int[] points = new int[2];
	
	public Referee(TournamentRunner statistician)
	{
		this.statistician = statistician;
	}

	public void switchDoors(Gameboard gameboard)
	{
		Field[][] fields = gameboard.getFields();
		
		for(int i = 0; i < fields.length; i++)
		{
			for(int j = 0; j < fields[i].length; j++)
			{
				if(fields[i][j].getType() == 2 && Math.random() < GlobalGameboard.doorSwitchFrequency)
				{
					if(fields[i][j].isDoorOpen()) fields[i][j].setDoorOpen(false);
					else                          fields[i][j].setDoorOpen(true);
					
					if(!fields[i][j].isDoorOpen() && fields[i][j].getAnt() != null)
					{
						killAnt(gameboard, fields[i][j].getAnt());
					}
				}
			}
		}
	}

	private void killAnt(Gameboard gameboard, Ant ant)
	{
		ant.setDead(true);
		nrOfPlayers--;
	}
	
	public void run()
	{
		long currentTime = 0;
		
		while(true)
		{
			try
			{
				sleep(20);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			currentTime = System.currentTimeMillis();
			
			if(vizFlag && currentTime - vizTime > GlobalGameboard.vizualizationTime)
			{
				vizFlag = false;
				gameRunner.sendGameboard();
			}
			
			if(playerFlag && currentTime - playerTime > GlobalGameboard.playerMoveTime)
			{
				gameRunner.makeMove("stay", gameRunner.getCurrentPlayerIndex());
			}
			
			if(playerFlag && !gameRunner.isCurrentPlayerAlive())
			{
				gameRunner.makeMove("dead", gameRunner.getCurrentPlayerIndex());
			}
		}
	}

	public void setVizFlag(boolean vizFlag)
	{
		this.vizFlag = vizFlag;
		vizTime = System.currentTimeMillis();
	}
	
	public void setPlayerFlag(boolean playerFlag)
	{
		this.playerFlag = playerFlag;
		playerTime = System.currentTimeMillis();
	}
	
	public void addFrame(ControlFrame controlFrame)
	{
		this.controlFrame = controlFrame;		
	}

	public void addGameRunner(GameRunner gameRunner)
	{
		this.gameRunner = gameRunner;
	}

	public void processMoveResult(int moveFlag, int currentPlayerIndex, Ant[] ants, Gameboard gameboard)
	{
		if(moveFlag == 1)
		{
			points[currentPlayerIndex]++;
			controlFrame.setPoints(points[currentPlayerIndex], currentPlayerIndex);
		}
		
		if(moveFlag == 2)
		{
			int x = ants[currentPlayerIndex].getPosition().getX();
			int y = ants[currentPlayerIndex].getPosition().getY();
			
			for(int i = 0; i < ants.length; i++)
			{
				if(i != currentPlayerIndex && x == ants[i].getPosition().getX() && y == ants[i].getPosition().getY())
				{
					killAnt(gameboard, ants[i]);
				}
			}
		}
	}
	
	public void incrementRounds()
	{
		nrOfRounds++;
		controlFrame.setNrOfRounds(nrOfRounds);
	}

	public GameStatistics checkEndGame()
	{
		if(nrOfRounds > GlobalGameboard.maxNrOfRounds)
		{
			return statistician.endGame(points, nrOfRounds, "maxNrOfRoundsExceeded");	 
		}
		
		if(nrOfPlayers == 0)
		{
			return statistician.endGame(points, nrOfRounds, "allAntsAreDead");
		}
		
		int pointsSum = 0;
		for(int i = 0; i < points.length; i++) pointsSum += points[i];
		
		if(pointsSum == GlobalGameboard.foodNr)
		{
			return statistician.endGame(points, nrOfRounds, "everythingEaten");
		}
		
		return null;
	}

	public void reset()
	{
		nrOfRounds = 0;
		nrOfPlayers = 2;
		points = new int[2];	
	}
}
