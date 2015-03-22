package antClient;

import gameboard.Gameboard;
import gameboard.GlobalGameboard;
import visualization.ClientFrame;
import visualization.JGameboardFrame;

/**
 * Starter class for visual AntWars client. This client allows a human player to direct the ant using simple GUI.
 *  
 * @author Marek Lipczak || lipczak@cs.dal.ca || www.cs.dal.ca/~lipczak/
 * @version 1.0 || 2009-02-10
 *
 */

 /*
  * Whenever you modify the code please describe your contribution here:
  * 
  */

public class ClientVizStarter
{
	/**
	 * Before submission please change the name of your ant to antXXXXXX, where XXXXXX is the last six digits of your Dal ID
	 * and turn off the random number generator below
	 */
	
	public static String name = "ant";
	
	/**
	 * Starts a visual client.
	 * @param args 	two handled parameters are <code>echo</code> and <code>socketXXXXX</code>.
	 * 				<code>echo</code> tells the client to print comments to standard output,
	 * 				<code>socketXXXXX</code> changes the value of the base socket (which by default is set to <code>4154</code>). 
	 *                                        
	 */
	
	public static void main(String[] args)
	{
		for(int i = 0; i < args.length; i++)
		{
			checkParameters(args[i]);
		}
		
		//TODO: turn it off before subnission
		name += ""+(new Double(Math.random()*1000000).intValue());
		
		AIprocessorHuman processor = new AIprocessorHuman(new Gameboard(true));
				
		ClientCommunicationInterface communicator = new ClientCommunicationInterface(name, processor);
		communicator.start();
		
		ClientFrame frame = new ClientFrame(processor);
    	frame.setVisible(true);
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
