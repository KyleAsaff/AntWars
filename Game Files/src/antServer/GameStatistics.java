package antServer;

import java.io.PrintWriter;
import java.util.ArrayList;

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

public class GameStatistics
{
	public static String title = "#player1Name\tplayer2Name\tpoints1\tpoints2\tsmallPoints1\tsmallPoints2\tnrOfRounds\ttotalFoodNr\tendReason";
	
	String[] playerNames;
	double[] playerPoints;
	double[] playerSmallPoints;
	int totalFoodNr;
	int nrOfRounds;
	
	String gameEndReason;
	
	public GameStatistics(String[] playerNames, int[] playerSmallPoints, int totalFoodNr, int nrOfRounds, String gameEndReason)
	{
		this.playerNames = playerNames;
		this.totalFoodNr = totalFoodNr;
		this.nrOfRounds = nrOfRounds;
		
		this.gameEndReason = gameEndReason;
		
		calculateSmallPoints(playerSmallPoints);
		calculatePoints(playerSmallPoints);
	}
	
	private void calculateSmallPoints(int[] smallPoints)
	{
		playerSmallPoints = new double[smallPoints.length];
		
		for(int i = 0; i < smallPoints.length; i++)
		{
			playerSmallPoints[i] = smallPoints[i]/(1.0*totalFoodNr);
		}
	}

	private void calculatePoints(int[] smallPoints)
	{
		playerPoints = new double[smallPoints.length];
		
		int bestScore = -1;
		int nrOfBestScores = 1;
		
		for(int i = 0; i < smallPoints.length; i++)
		{
			if(bestScore == smallPoints[i])
			{
				nrOfBestScores++;
			}
			else if(bestScore < smallPoints[i])
			{
				bestScore = smallPoints[i];
				nrOfBestScores = 1;
			}
		}
		
		for(int i = 0; i < smallPoints.length; i++)
		{
			if(bestScore == smallPoints[i]) playerPoints[i] = 1.0/(1.0*nrOfBestScores);
			else                            playerPoints[i] = 0;
		}		
	}

	public String getEndGameInfoForPlayer()
	{
		String playerInfo = "<players>";
		
		for(int i = 0; i < playerNames.length; i++)
		{
			playerInfo += "<player id=\""+i+"\">";
			
			playerInfo += "<playerName>"+playerNames[i]+"</playerName>";
			playerInfo += "<playerPoints>"+playerPoints[i]+"</playerPoints>";
			playerInfo += "<playerSmallPoints>"+playerSmallPoints[i]+"</playerSmallPoints>";
			
			playerInfo += "</player>";
		}
		
		playerInfo += "</players>";
		
		return "<endGame>"+playerInfo+"<nrOfRounds>"+nrOfRounds+"</nrOfRounds><totalFoodNr>"+totalFoodNr+"</totalFoodNr><reason>"+gameEndReason+"</reason></endGame>";
	}
	
	public static void writeToFile(PrintWriter outFile, ArrayList<GameStatistics> stats)
	{
		outFile.println(title);
		GameStatistics singleStats;
		
		for(int i = 0; i < stats.size(); i++)
		{
			singleStats = stats.get(i);
			
			for(int j = 0; j < singleStats.playerNames.length; j++) outFile.print(singleStats.playerNames[j]+"\t");
			for(int j = 0; j < singleStats.playerPoints.length; j++) outFile.print(singleStats.playerPoints[j]+"\t");
			for(int j = 0; j < singleStats.playerSmallPoints.length; j++) outFile.print(singleStats.playerSmallPoints[j]+"\t");
			
			outFile.print(singleStats.totalFoodNr+"\t");
			outFile.print(singleStats.nrOfRounds+"\t");
			outFile.print(singleStats.gameEndReason+"\t");
			
			outFile.println("");
		}
	}
}
