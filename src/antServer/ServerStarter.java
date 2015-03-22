package antServer;

import antClient.AIprocessor;
import antClient.ClientCommunicationInterface;
import gameboard.Ant;
import gameboard.Gameboard;
import gameboard.GlobalGameboard;
import visualization.ControlFrame;
import visualization.JGameboardFrame;

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

public class ServerStarter
{
	static ControlFrame controlFrame;
	
	public static void main(String[] args)
	{
		for(int i = 0; i < args.length; i++)
		{
			checkParameters(args[i]);
		}
		
		TournamentRunner tournamentRunner = new TournamentRunner("tournamentGameStats");
		tournamentRunner.start();
		Referee referee = new Referee(tournamentRunner);
		referee.start();
		GameRunner gameRunner = new GameRunner(referee);
		tournamentRunner.setGameRunner(gameRunner);
		
		ServerCommunicationInterface communicationInterface;
		communicationInterface = new ServerCommunicationInterface(gameRunner, tournamentRunner);
		communicationInterface.start();
		
		Gameboard gameboard = new Gameboard(false);		

		controlFrame = new ControlFrame(new JGameboardFrame(gameboard));
		controlFrame.setVisible(true);
		
		referee.addFrame(controlFrame);
		referee.addGameRunner(gameRunner);
				
		gameRunner.setControlFrame(controlFrame);
		tournamentRunner.setControlFrame(controlFrame);
		        
	}
	
	private static void checkParameters(String param)
	{
		if(param.equals("echo"))
		{
			GlobalGameboard.doEcho = true;
			return;
		}
		
		if(param.matches("^socket\\d+"))
		{
			param = param.replace("socket", "");
			ServerCommunicationInterface.baseSocket = new Integer(param).intValue();
			return;
		}
	}
}
