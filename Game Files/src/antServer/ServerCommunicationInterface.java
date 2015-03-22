package antServer;

import gameboard.GlobalGameboard;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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

public class ServerCommunicationInterface extends Thread
{
	GameRunner gameRunner;
	TournamentRunner tournamentRunner;
	
	public static int baseSocket = 4154;
	
	public ServerCommunicationInterface(GameRunner gameRunner, TournamentRunner tournamentRunner)
	{
		this.gameRunner = gameRunner;
		this.tournamentRunner = tournamentRunner;
	}
	
	public void run()
	{
		int playerIndex = 0;
		
		try
		{
			ServerSocket server;
			Socket incoming;
			
			while(true)
			{
				server = new ServerSocket(baseSocket);

				incoming = server.accept();
				
				if(GlobalGameboard.doEcho) System.out.println("incoming connection");
				
				BufferedReader in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
				PrintWriter out = new PrintWriter(incoming.getOutputStream(), true);
				
				String playerName = in.readLine();
				if(GlobalGameboard.doEcho) System.out.println("new player: "+playerName);
				out.println(baseSocket+1+playerIndex);
								
				Player newPlayer = new Player(playerName, baseSocket+1+playerIndex);
				tournamentRunner.addPlayer(newPlayer);
				
				out.println(baseSocket+1+playerIndex);
				
				incoming.close();
				server.close();
				
				playerIndex++;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
