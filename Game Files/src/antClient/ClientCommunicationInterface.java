package antClient;

import gameboard.Gameboard;
import gameboard.GlobalGameboard;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import antServer.Player;

/**
 * TCP/IP interface of a AntWars client. This class is responsible for receiving and sending messages to the server.
 * 
 * @author Marek Lipczak || lipczak@cs.dal.ca || www.cs.dal.ca/~lipczak/
 * @version 1.0 || 2009-02-10
 *
 */

 /*
  * Whenever you modify the code please describe your contribution here:
  *
 */

public class ClientCommunicationInterface extends Thread
{
	public static int baseSocket = 4154;
	
	BufferedReader in;
	PrintWriter out;
	String name;
	AIprocessor processor;

	/**
	 * The constructor, sets the values of parameters and calls the AIprocessor to set this instance as its server communication interface. 
	 * 
	 * @param name		the name of the client, which will be used identify the player by the server
	 * @param processor	AIprocessor responsible for move decisions
	 */
	
	public ClientCommunicationInterface(String name, AIprocessor processor)
	{
		this.name = name;
		this.processor = processor;
		processor.setInterface(this);
	}
	
	/**
	 * Sets the connection with the server on the client's individual socket and then holds in message receiver loop.
	 */
	
	public void run()
	{
		int mySocket;
		
		try
		{
			Socket socket = new Socket("127.0.0.1", baseSocket);
			if(GlobalGameboard.doEcho) System.out.println("Connection with the server established on base socket: "+baseSocket);
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			if(GlobalGameboard.doEcho) System.out.println("Sending name to the server, name: "+name);
			out.println(""+name);
			
			mySocket = new Integer(in.readLine()).intValue();
			
			if(GlobalGameboard.doEcho) System.out.println(" A individual socket socket received: "+mySocket);
			
			socket.close();
			socket = new Socket("127.0.0.1", mySocket);
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			String msg;
			while(true)
			{
				msg = in.readLine();
				if(GlobalGameboard.doEcho) System.out.println(" message received: "+msg);
				processMessage(msg);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Preprocess the message and calls appropriate function of the AIprocessor.
	 * 
	 * @param msg	message from the server in pseudo-XML format
	 */
	
	private void processMessage(String msg)
	{
		String type = msg.replaceAll(">.*", "");
		type = type.replaceAll("<", "");
		
		if(type.equals("startGame"))
		{
			int position = new Integer(getXMLfield("startPosition", msg));
			processor.startGameSignal(position);
		}
		
		if(type.equals("endGame"))
		{
			processor.endGameSignal(msg);
		}
		
		if(type.equals("currentState"))
		{
			if(getXMLfield("health", msg).equals("killed"))
			{
				processor.gotKilled();
				return;
			}
			
			String xmlPosition = getXMLfield("currentPosition", msg);
			String xmlGameboard = getXMLfield("gameboard", msg);
						
			processor.setCurrentPosition(xmlPosition);
			processor.setGameboard(new Gameboard(xmlGameboard));
		}
	}
	
	/**
	 * This function allows the AIprocessor to send messages to the server. It is mainly designed for the move message.
	 * @param msg	a message to a server in pseudo-XML code
	 */
	
	public void send(String msg)
	{
		out.println(msg);
	}
	
	/**
	 * This is a quick and dirty XML parser. The function has no exception handling, and is not recommended to be reused for different tasks.
	 * 
	 * @param name	the name of XML field that should be found
	 * @param xml	a snippet of XML code
	 * @return 		the value of chosen XML field
	 */
	
	private String getXMLfield(String name, String xml)
	{
		String tmp = xml.replaceAll(".*<"+name+">", "");
			   tmp = tmp.replaceAll("</"+name+">.*", "");
		
		return tmp;
	}
}
