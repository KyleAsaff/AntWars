package antClient;

import gameboard.Gameboard;

/**
 * An abstract move processor (short: player). To implement a new player override presented functions.
 * 
 * @author Marek Lipczak || lipczak@cs.dal.ca || www.cs.dal.ca/~lipczak/
 * @version 1.0 || 2009-02-10
 *
 */

 /*
  * Whenever you modify the code please describe your contribution here:
  *
 */

public abstract class AIprocessor
{
	boolean isDead = false;
	
	ClientCommunicationInterface communicationInterface;
	
	/**
	 * This function sets a server communication interface which allows the processor to send moves to the server.
	 * @param communicationInterface	a server communication interface
	 */
	public void setInterface(ClientCommunicationInterface communicationInterface)
	{
		this.communicationInterface = communicationInterface;
	}
	
	/**
	 * Informs the player that the game has started and gives it a starting position index.
	 * 
	 * @param startIndex	<code>1</code> if the player makes the first move in the game,
	 * 						<code>2</code> if the player moves as the second
	 */
	public abstract void startGameSignal(int startIndex);
	
	/**
	 * Sets the current position of the player relatively to the starting position.
	 * This information is used to synchronize the real position of the player on the board, which was set by the server,
	 * with the position predicted by the player. (In case the player makes an invalid move and misses it)
	 * @param xmlPosition	pseudo-XML code describing players position on the game board
	 */
	public abstract void setCurrentPosition(String xmlPosition);
	
	/**
	 * Sets the extraction of the game board that shows the fields around the player's ant.
	 * The game board is also a signal that the player should make a new move!
	 * 
	 * @param gameboard extraction of the game board that shows the fields around the player's ant
	 */
	public abstract void setGameboard(Gameboard gameboard);
	
	/**
	 * Informs the player that it was killed and no longer takes part in the current game.
	 */
	public abstract void gotKilled();
	
	/**
	 * Informs the player that the game has ended
	 * 
	 * @param msg	a pseudo-XML code containing the information about the game end reason
	 */
	public abstract void endGameSignal(String msg);
	
	/**
	 * Wraps the move in XML tags. This function should be called when the decision about the move is done by the player
	 * 
	 * @param move	there are five available moves up <code>N</code>, right <code>E</code>, down <code>S</code>, left <code>W</code> and stay <code>stay</code>
	 */	
	protected void sendMove(String move)
	{
		communicationInterface.send("<move>"+move+"</move>");		
	}
}
