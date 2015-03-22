package antClient;

import gameboard.Gameboard;
import gameboard.GlobalGameboard;
import visualization.ClientFrame;

/**
 * Starter class for Rand AntWars client. Rand client makes in each round a random move,
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

public class ClientRandStarter
{
	/**
	 * Before submission please change the name of your ant to antXXXXXX, where XXXXXX is the last six digits of your Dal ID
	 * and turn off the random number generator below
	 */
	
	static String name = "ant";
	
	public static void main(String[] args)
	{
		for(int i = 0; i < args.length; i++)
		{
			checkParameters(args[i]);
		}
		
		//TODO: turn it off before subnission
		name += ""+(new Double(Math.random()*1000).intValue());
		
		AIprocessorRand processor = new AIprocessorRand();
				
		ClientCommunicationInterface communicator = new ClientCommunicationInterface(name, processor);
		communicator.start();
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
			ClientCommunicationInterface.baseSocket = new Integer(param).intValue();
			return;
		}
	}
}
