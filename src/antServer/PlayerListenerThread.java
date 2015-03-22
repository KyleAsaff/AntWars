package antServer;

import gameboard.GlobalGameboard;

import java.io.BufferedReader;
import java.io.IOException;

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

public class PlayerListenerThread extends Thread
{
	BufferedReader inputChannel;
	Player player;
	
	
	public PlayerListenerThread(BufferedReader inputChannel, Player player)
	{
		this.inputChannel = inputChannel;
		this.player = player;
	}

	public void run()
	{
		String line;
		
		while(true)
		{
			try
			{
				if(GlobalGameboard.doEcho) System.out.println(">>> PLAYER NAME: "+player.getName());
				line = inputChannel.readLine();
				if(GlobalGameboard.doEcho) System.out.println(">>> PLAYER SAYS: "+line);
				if(line.matches("<move>.*")) player.makeMove(getXMLfield("move", line));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private String getXMLfield(String name, String xml)
	{
		String tmp = xml.replaceAll(".*<"+name+">", "");
			   tmp = tmp.replaceAll("</"+name+">.*", "");
		
		return tmp;
	}
}
