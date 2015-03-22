package antServer;

import gameboard.Field;
import gameboard.Gameboard;

import java.io.BufferedReader;
import java.io.IOException;
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

public class Player
{
	String name;
	GameRunner gameRunner;
	int id;
	
	ServerSocket communicationServer;
	PrintWriter outputChannel;
	
	Field startPosition;
	
	public Player(String name, int socketId)
	{
		this.name = name;
		
		prepareCommunicationChannel(socketId);
	}

	public void setGameRunner(GameRunner gameRunner, int id)
	{
		this.gameRunner = gameRunner;
		this.id = id;
	}
	
	private void prepareCommunicationChannel(int socketId)
	{
		BufferedReader inputChannel = null;
		
		try
		{
			communicationServer = new ServerSocket(socketId);
			Socket socket = communicationServer.accept();
			
			inputChannel = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputChannel = new PrintWriter(socket.getOutputStream(), true);
			outputChannel.println("accepted: "+name);
		}
		catch (IOException e) {e.printStackTrace();}
		
		PlayerListenerThread playerThread = new PlayerListenerThread(inputChannel, this);
		playerThread.start();
		
	}

	public String getName()
	{
		// TODO Auto-generated method stub
		return name;
	}

	public void sentStartMessage(int startPosition)
	{
		outputChannel.println("<startGame><startPosition>"+startPosition+"</startPosition></startGame>");
	}

	public void sentGameboard(Gameboard gameboard, Field currentPosition)
	{
		Field relPosition = currentPosition.getRalativePosition(startPosition);
		String positionString = "<currentPosition><x>"+relPosition.getX()+"</x><y>"+relPosition.getY()+"</y></currentPosition>";
		outputChannel.println("<currentState><health>alive</health>"+positionString+gameboard.encodeXML()+"</currentState>");		
	}

	public void makeMove(String move)
	{
		if(gameRunner != null) gameRunner.makeMove(move, id);		
	}

	public void sentEndGame(GameStatistics stat)
	{
		gameRunner = null;
		outputChannel.println(stat.getEndGameInfoForPlayer());
	}
	
	public void sentKilledFlag()
	{
		outputChannel.println("<currentState><health>killed</health><currentState>");
	}

	public void setStartPosition(Field position)
	{
		startPosition = position;		
	}
}
