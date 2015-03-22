package antServer;

import gameboard.Ant;
import gameboard.Gameboard;
import gameboard.GlobalGameboard;

import java.util.ArrayList;

import visualization.ControlFrame;

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

public class GameRunner
{
	Referee referee;

	ControlFrame controlFrame;
	int currentPlayerIndex = 0;
	boolean[] antSementary;
	
	Gameboard gameboard;
	
	Player[] currentPlayers;
	Ant[] ants;
	
	boolean isGameRunning;
	
	public GameRunner(Referee referee)
	{
		this.referee = referee;
		
		Ant[] ants = new Ant[GlobalGameboard.playersNr];
		for(int i = 0; i < ants.length; i++)
		{
			ants[i] = new Ant(i);
		}
		
		antSementary = new boolean[ants.length];
		
		currentPlayers = new Player[2];
	}

	public void setControlFrame(ControlFrame controlFrame)
	{
		this.controlFrame = controlFrame;
		controlFrame.addGameRunner(this);
	}
	
	public void setCurrentPlayer(int position, Player player)
	{
		currentPlayers[position] = player;
	}
	
	public void startSingleGame()
	{	
		isGameRunning = true;
		
		ants = new Ant[currentPlayers.length];
		for(int i = 0; i < ants.length; i++)
		{
			if(currentPlayers[i] == null) return;
			currentPlayers[i].sentStartMessage(i+1);
			currentPlayers[i].setGameRunner(this, i);
			ants[i] = new Ant(i);
			antSementary[i] = false;
		}
		
		antSementary = new boolean[ants.length];
		
		gameboard = new Gameboard(ants);
		controlFrame.updateGameboard(gameboard);
		
		referee.reset();
		
		for(int i = 0; i < currentPlayers.length; i++)
		{
			currentPlayers[i].setStartPosition(ants[i].getPosition());
		}
		
		currentPlayers[0].sentGameboard(new Gameboard(ants[0].getPosition(), gameboard, GlobalGameboard.obstructiveWalls), ants[0].getPosition());
		referee.setPlayerFlag(true);
		currentPlayerIndex = 0;
	}

	public void makeMove(String move, int id)
	{
		if(currentPlayerIndex != id) return;
		
		referee.setPlayerFlag(false);
		
		if(!ants[currentPlayerIndex].isDead())
		{
			int moveFlag = ants[currentPlayerIndex].makeMove(move);
			referee.processMoveResult(moveFlag, currentPlayerIndex, ants, gameboard);
		}
		
		currentPlayerIndex = (currentPlayerIndex+1)%currentPlayers.length;
		if(currentPlayerIndex == 0)
		{
			GameStatistics endGameStats = referee.checkEndGame();
			
			if(endGameStats != null)
			{
				for(int i = 0; i < currentPlayers.length; i++)
				{
					currentPlayers[i].sentEndGame(endGameStats);
				}
				
				isGameRunning = false;
				
				return;
			}
			referee.incrementRounds();
			referee.switchDoors(gameboard);
		}
		controlFrame.updateGameboard(gameboard);
		
		if(controlFrame.isVizualization())
		{
			referee.setVizFlag(true);
		}
		else
		{
			sendGameboard();
		}	
	}

	public void sendGameboard()
	{
		if(ants[currentPlayerIndex].isDead())
		{
			if(!antSementary[currentPlayerIndex]) currentPlayers[currentPlayerIndex].sentKilledFlag();
			antSementary[currentPlayerIndex] = true;
			referee.setPlayerFlag(true);
			return;
		}
		
		currentPlayers[currentPlayerIndex].sentGameboard(new Gameboard(ants[currentPlayerIndex].getPosition(), gameboard, GlobalGameboard.obstructiveWalls), ants[currentPlayerIndex].getPosition());
		referee.setPlayerFlag(true);
	}
	
	public int getCurrentPlayerIndex()
	{
		return currentPlayerIndex;
	}

	public boolean isCurrentPlayerAlive()
	{
		return !antSementary[currentPlayerIndex];
	}

	public String[] getPlayerNames()
	{
		String[] names = new String[currentPlayers.length];
		
		for(int i = 0; i < names.length; i++)
		{
			names[i] = currentPlayers[i].getName();
		}
		
		return names;
	}

	public boolean isGameRunning()
	{
		return isGameRunning;
	}
}
