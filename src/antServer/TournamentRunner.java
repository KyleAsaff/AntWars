package antServer;

import gameboard.GlobalGameboard;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

public class TournamentRunner extends Thread
{
	String name;
	ArrayList<Player> playersList;
	ControlFrame controlFrame;
	
	ArrayList<GameStatistics> gameStats = new ArrayList<GameStatistics>();
	GameRunner gameRunner;
	
	int tournamentIndex1;
	int tournamentIndex2;
	boolean runTournament = false;
	
	public TournamentRunner(String name)
	{
		this.name = name;
		playersList = new ArrayList<Player>();
	}
	
	public void setGameRunner(GameRunner gameRunner)
	{
		this.gameRunner = gameRunner;		
	}
	
	public void addPlayer(Player newPlayer)
	{
		playersList.add(newPlayer);
		controlFrame.addPlayer(newPlayer.getName());
	}
	
	public void setControlFrame(ControlFrame controlFrame)
	{
		this.controlFrame = controlFrame;
		controlFrame.addTournamentRunner(this);
	}
	
	public void setCurrentPlayer(int position, String name)
	{
		gameRunner.setCurrentPlayer(position, findPlayer(name));
	}

	private Player findPlayer(String name)
	{
		for(int i = 0; i < playersList.size(); i++)
		{
			if(playersList.get(i).getName().equals(name)) return playersList.get(i);
		}
		
		return null;
	}

	public void startTournament()
	{
		tournamentIndex1 = 0;
		tournamentIndex2 = 0;
		runTournament = true;
	}
	
	public void run()
	{
		while(true)
		{
			try
			{
				sleep(1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			if(runTournament && !gameRunner.isGameRunning())
			{
				if(tournamentIndex1 != tournamentIndex2)
				{
					gameRunner.setCurrentPlayer(0, playersList.get(tournamentIndex1));
					gameRunner.setCurrentPlayer(1, playersList.get(tournamentIndex2));
					controlFrame.setPlayerName(0, playersList.get(tournamentIndex1).getName());
					controlFrame.setPlayerName(1, playersList.get(tournamentIndex2).getName());
					gameRunner.startSingleGame();
				}
								
				tournamentIndex1 = (tournamentIndex1+1)%playersList.size();
				if(tournamentIndex1 == 0) tournamentIndex2 = (tournamentIndex2+1)%playersList.size();
				if(tournamentIndex1 == 0 && tournamentIndex2 == 0) runTournament = false;
			}
		}
	}
	
	public GameStatistics endGame(int[] points, int nrOfRounds, String reason)
	{
		String[] playerNames = gameRunner.getPlayerNames();
		GameStatistics singleStats = new GameStatistics(playerNames, points, GlobalGameboard.foodNr, nrOfRounds, reason);
		
		gameStats.add(singleStats);
		
		PrintWriter outFile = null;
		try
		{
			outFile = new PrintWriter(new BufferedWriter(new FileWriter(name+".dat", false)));
		}
		catch (FileNotFoundException e)  {System.out.println("file not found"); e.printStackTrace();}
		catch (IOException e) {System.out.println("io exception"); e.printStackTrace();}
		
		GameStatistics.writeToFile(outFile, gameStats);
		outFile.close();
		
		return singleStats;
	}
}
