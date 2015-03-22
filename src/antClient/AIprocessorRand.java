package antClient;

import java.util.*;

import gameboard.Ant;
import gameboard.Field;
import gameboard.Gameboard;
import gameboard.GlobalGameboard;

/**
 * Implementation of Rand player - a sample automatic AntWars player. The Rand player makes in each round a random move,
 * uniformly choosing from a list of currently available moves. 
 * 
 * @author Marek Lipczak || lipczak@cs.dal.ca || www.cs.dal.ca/~lipczak/
 * @version 1.0 || 2009-02-10
 *
 */

 /*
  * Whenever you modify the code please describe your contribution here:
  * 
  * Edited By Kyle Asaff
  * 
  * I Have edited AIprocessorRand.java and made the following changes to the AI:
  * 
  * 1. The AI hashes all near by food locations into a hash table using the key as the priority of the food (1=highest)
  *    and will then attempt to reach each food location it has hashed.
  * 2. If the AI comes across an opponent 1 space away, it will kill the opponent.
  * 3. If there is no food near by, the AI visits each corner of the quadrant it is in and determines
  *    which quadrant it is in (NW, NE, SW, SE).
  * 4. After the AI visits all 4 corners of the sector, the AI will visit a quadrant it has not been to yet.
  * 5. It will repeat the 4 above steps and if the game is not ended by the end after visiting all 4 quadrants, the AI
  *    will go in random directions in an attempt to hash any food it comes across and it will try to reach it.
 */

public class AIprocessorRand extends AIprocessor
{
	public static int corners = 0;
	public static analyzeBoard analyzeBoard = new analyzeBoard();

	public void startGameSignal(int position)
	{
		if(GlobalGameboard.doEcho) System.out.println("Game started");
		analyzeBoard.resetSettings();
	}
	
	public void setCurrentPosition(String xmlPosition)
	{
		if(GlobalGameboard.doEcho) System.out.println("Current position: "+xmlPosition);
	}

	/**
	 * This is the main function of any automatic player.
	 * The player receives a new game board setting and it should make decision about the move.
	 * There are five available moves up <code>N</code>, right <code>E</code>, down <code>S</code>, left <code>W</code> and stay <code>stay</code>
	 * The move should be submitted to the server using {@link sendMove(String move)} function.
	 * 
	 * @param gameboard extraction of the game board that shows the fields around the player's ant
	 */
	
	public void setGameboard(Gameboard gameboard)
	{
		
		Field[][] fields =  gameboard.getFields();
		int centerX = fields.length/2;
		int centerY = fields[centerX].length/2;
		
		int targetX = 0;
		int targetY = 0;
		
		int calcX = 0;
		int calcY = 0;
		
		String move = "";

		if(analyzeBoard.checkChangingRoom() == false) {
		HashMap<Integer, foodLocation> targets = new HashMap<Integer, foodLocation>();
		
		
		// this loop hashes food locations with the key being the distance it is located (overwrites food located same distance)
			for(int i=0; i<fields.length; i++) {
				for(int j=0; j<fields[centerX].length; j++) {
					if(fields[i][j].hasFood()) {
						targets.put(Math.abs(i-centerX) + Math.abs(j-centerY), new foodLocation(i,j));
					}
				}
			}
			
		//Search and destroy and if ant is at a distance of 1
			if(fields[2][1].getAntId() != -1)
				sendMove("N");
			else if(fields[3][2].getAntId() != -1)
				sendMove("E");
			else if(fields[2][3].getAntId() != -1)
				sendMove("S");
			else if(fields[1][2].getAntId() != -1)
				sendMove("W");
			
		//Get food closest to current position
			int key = 1;
			
			while(key<fields.length) {
			if(targets.containsKey(key)) {
				targetX = targets.get(key).getXcord();
				targetY= targets.get(key).getYcord();
				
				calcX = targetX-centerX;
				calcY = targetY-centerY;
				
				if(calcX > 0)
					move = "E";
				else if(calcX < 0)
					move = "W";
				else if(calcY > 0)
					move = "S";
				else if(calcY < 0)
					move = "N";
				else
					move = "";
				
				break;
			}
			key++;
		}
		}
			
			if (move.isEmpty()) {
				move = analyzeBoard.findNextmove(fields);
			}
			if (move.equals("Algorithm Complete"))
			{
				move = analyzeBoard.randomMove(fields, gameboard);
			}
		analyzeBoard.checkforCorner(fields);
		sendMove(move);
	}
	
	
	public void gotKilled()
	{
		if(GlobalGameboard.doEcho) System.out.println("I'm dead");
		isDead = true;
		analyzeBoard.resetSettings();
	}
	
	public void endGameSignal(String msg)
	{
		if(GlobalGameboard.doEcho) System.out.println("Game ended");
		analyzeBoard.resetSettings();
		//I don't care
	}
}

/** foodLocation Class
 * 
 * Stores the x and y coordinate of a food piece.
 *
 */

class foodLocation {
	private int xcord;
	private int ycord;

	  public foodLocation(int xcord, int ycord) {
	    this.xcord = xcord;
	    this.ycord = ycord;
	  }

	public int getXcord() {
		return xcord;
	}

	public int getYcord() {
		return ycord;
	}

	}

/** analyzeBoard Class
 * 
 * Does the majority of the move calculations.
 *
 */

class analyzeBoard {
	
	private boolean nwCorner = false;
	private boolean neCorner = false;
	private boolean swCorner = false;
	private boolean seCorner = false;
	
	private boolean checkednwRoom = false;
	private boolean checkedneRoom = false;
	private boolean checkedswRoom = false;
	private boolean checkedseRoom = false;
	
	private String currRoom = "unknown";

	private boolean changingRoom = false;
	
	//resetSettings: Used to reset settings when dead.
	public void resetSettings(){
		nwCorner = false;
		neCorner = false;
		swCorner = false;
		seCorner = false;
		
		checkednwRoom = false;
		checkedneRoom = false;
		checkedswRoom = false;
		checkedseRoom = false;
		
		currRoom = "unknown";

		changingRoom = false;
	}
	
	//findNextmove: Analyzes the field given to calculate the next move.
	public String findNextmove(Field[][] fields) {
		checkforCorner(fields);
		String move = "";
		
		if(!nwCorner) {
			if(currRoom.equals("unknown")) {
				if(fields[2][1].getType() == 0)
					move = "N";
				else if(fields[1][2].getType() == 0)
					move = "W";
			}
			else if(fields[2][0].getType() == 0 && fields[2][1].getType() == 0)
				move = "N";
			else if(fields[0][2].getType() == 0 && fields[1][2].getType() == 0)
				move = "W";
		}
		
		else if(!neCorner) {
			if(currRoom.equals("unknown")) {
				if(fields[3][2].getType() == 0)
					move = "E";
				else if(fields[2][1].getType() == 0)
					move = "N";
			}
			else if(fields[3][2].getType() == 0 && fields[4][2].getType() == 0)
				move = "E";
			else if(fields[2][0].getType() == 0 && fields[2][1].getType() == 0)
				move = "N";
		}
		
		else if(!seCorner) {
			if(currRoom.equals("unknown")) {
				if(fields[3][2].getType() == 0)
					move = "E";
				else if(fields[2][3].getType() == 0)
					move = "S";
			}
			else if(fields[3][2].getType() == 0 && fields[4][2].getType() == 0)
				move = "E";
			else if(fields[2][3].getType() == 0 && fields[2][4].getType() == 0)
				move = "S";
		}
		
		else if(!swCorner) {
			if(currRoom.equals("unknown")) {
				if(fields[1][2].getType() == 0)
					move = "W";
				else if(fields[2][3].getType() == 0)
					move = "S";
			}
			else if(fields[0][2].getType() == 0 && fields[1][2].getType() == 0)
				move = "W";
			else if(fields[2][3].getType() == 0 && fields[2][4].getType() == 0)
				move = "S";
		}
		
		if(nwCorner && neCorner && seCorner && swCorner) {
			changingRoom = true;
			move = switchRoom(fields);
		}
		
		return move;
	}
	
	//randomMove: After the AI has ran through its logical moves it will use random movement if the game has not finished.
	public String randomMove(Field[][] fields, Gameboard gameboard) {
		changingRoom = false;
		
		int xDirection = 0;
		int yDirection = 0;
		int centerX = fields.length/2;
		int centerY = fields[centerX].length/2;
		
		/*
		 * Inside the loop the processor picks random coordinates of move {-1, 0 ,1}.
		 * Coordinates are not accepted when the move is not done in a vertical or horizontal axis,
		 * or when the new field is not movable (e.g., it is a wall field)
		 */
		
		while(xDirection*xDirection  + yDirection*yDirection != 1 || !gameboard.isMoveable(centerX+xDirection, centerY+yDirection))
		{
			xDirection = new Double(Math.random()*3).intValue()-1;
			yDirection = new Double(Math.random()*3).intValue()-1;
		}
				
		String move = "";
		
		if(yDirection == -1) move = "N";
		if(xDirection ==  1) move = "E";
		if(yDirection ==  1) move = "S";
		if(xDirection == -1) move = "W";
				
		return move;
	}
	
	//checkChangingRoom: Used to stop interruption of a room change.
	public boolean checkChangingRoom() {
		return changingRoom;
	}
	
	//switchRoom: All possible ways the AI can switch to a different quadrant/room.
	public String switchRoom(Field[][] fields) {
		// changes room from nw to sw
		if(currRoom.equals("nw") && checkedswRoom == false) {
			if(fields[2][3].getType() == 1)
				return "E";
			else if(fields[2][3].getType() == 2)
				return "S";
			else if(fields[1][2].getType() == 1 && fields[3][2].getType() == 1) {
				checkednwRoom = true;
				currRoom = "sw";
				changeRoom();
				return "S";
			}
			else if(fields[2][3].getType() == 0)
				return "S";
		}
		
		// changes room from nw to ne
		if(currRoom.equals("nw") && checkedneRoom == false) {
			if(fields[3][2].getType() == 1) //move until found door
				return "N";
			else if(fields[3][2].getType() == 2) // attempt to go through door
				return "E";
			else if(fields[2][1].getType() == 1 && fields[2][3].getType() == 1) { //if in doorway
				checkednwRoom = true;
				currRoom = "ne";
				changeRoom();
				return "E";
			}
			else if(fields[3][2].getType() == 0) //go all the way to wall
				return "E";
		}
		// changes room from ne to nw
		if(currRoom.equals("ne") && checkednwRoom == false) {
			if(fields[1][2].getType() == 1) //move until found door
				return "N";
			else if(fields[1][2].getType() == 2) // attempt to go through door
				return "W";
			else if(fields[2][1].getType() == 1 && fields[2][3].getType() == 1) { //if in doorway
				checkedneRoom = true;
				currRoom = "nw";
				changeRoom();
				return "W";
			}
			else if(fields[1][2].getType() == 0) //go all the way to wall
				return "W";
		}
		// changes room from ne to se
		if(currRoom.equals("ne") && checkedseRoom == false) {
			if(fields[2][3].getType() == 1)
				return "E";
			else if(fields[2][3].getType() == 2)
				return "S";
			else if(fields[1][2].getType() == 1 && fields[3][2].getType() == 1) {
				checkedneRoom = true;
				currRoom = "se";
				changeRoom();
				return "S";
			}
			else if(fields[2][3].getType() == 0)
				return "S";
		}
		// changes room from sw to nw
		if(currRoom.equals("sw") && checkednwRoom == false) {
			if(fields[2][1].getType() == 1)
				return "E";
			else if(fields[2][1].getType() == 2)
				return "N";
			else if(fields[1][2].getType() == 1 && fields[3][2].getType() == 1) {
				checkedswRoom = true;
				currRoom = "nw";
				changeRoom();
				return "N";
			}
			else if(fields[2][1].getType() == 0)
				return "N";
		}
		// changes room from sw to se
		if(currRoom.equals("sw") && checkedseRoom == false) {
			if(fields[3][2].getType() == 1)
				return "N";
			else if(fields[3][2].getType() == 2)
				return "E";
			else if(fields[2][1].getType() == 1 && fields[2][3].getType() == 1) {
				checkedswRoom = true;
				currRoom = "se";
				changeRoom();
				return "E";
			}
			else if(fields[3][2].getType() == 0)
				return "E";
		}
		// changes room from se to sw
		if(currRoom.equals("se") && checkedswRoom == false) {
			if(fields[1][2].getType() == 1) //move until found door
				return "N";
			else if(fields[1][2].getType() == 2) // attempt to go through door
				return "W";
			else if(fields[2][1].getType() == 1 && fields[2][3].getType() == 1) { //if in doorway
				checkedseRoom = true;
				currRoom = "sw";
				changeRoom();
				return "W";
			}
			else if(fields[1][2].getType() == 0) //go all the way to wall
				return "W";
		}
		// changes room from se to ne
		if(currRoom.equals("se") && checkedneRoom == false) {
			if(fields[2][1].getType() == 1)
				return "E";
			else if(fields[2][1].getType() == 2)
				return "N";
			else if(fields[1][2].getType() == 1 && fields[3][2].getType() == 1) {
				checkedseRoom = true;
				currRoom = "ne";
				changeRoom();
				return "N";
			}
			else if(fields[2][1].getType() == 0)
				return "N";
		}

		return "Algorithm Complete";
	}
	
	//changeRoom: Resets all the values for fresh start in new room.
	public void changeRoom() {
		nwCorner = false;
		neCorner = false;
		swCorner = false;
		seCorner = false;
		changingRoom = false;
	}
	
	//checkforCorner: Identifies which quadrant the AI is in and keeps track of visited quadrant corners.
	public void checkforCorner(Field[][] fields) {
		//check to see if nw corner found
		if(nwCorner == false && currRoom.equals("unknown")) {
			if(fields[1][1].getType() == 1 && fields[2][1].getType() == 1 && fields[3][1].getType() == 1 && fields[4][1].getType() == 1
					 && fields[1][2].getType() == 1 && fields[1][3].getType() == 1 && fields[1][4].getType() == 1 && fields[0][0].getType() == 0) {
				nwCorner = true;
				currRoom = "se";
			}
			else if(fields[1][1].getType() == 1 && fields[2][1].getType() == 1 && fields[3][1].getType() == 1 && fields[4][1].getType() == 1
					 && fields[1][2].getType() == 1 && fields[1][3].getType() == 1 && fields[1][4].getType() == 1) {
				nwCorner = true;
			}
		}
		else if (nwCorner == false) {
			
		if(fields[0][0].getType() == 1 && fields[1][0].getType() == 1 && fields[2][0].getType() == 1 && fields[3][0].getType() == 1 && fields[4][0].getType() == 1
				&& fields[0][1].getType() == 1 && fields[0][2].getType() == 1 && fields[0][3].getType() == 1 && fields[0][4].getType() == 1) {
			nwCorner = true;
			System.out.println("Found nwCorner via c1\n\n\n");
		}
		if(fields[1][0].getType() == 1 && fields[2][0].getType() == 1 && fields[3][0].getType() == 1 && fields[4][0].getType() == 1 
				&& fields[1][1].getType() == 1 && fields[1][2].getType() == 1 && fields[1][3].getType() == 1 && fields[1][4].getType() == 1) {
			nwCorner = true;
			System.out.println("Found nwCorner via c2\n\n\n");
		}
		if(fields[1][1].getType() == 1 && fields[2][1].getType() == 1 && fields[3][1].getType() == 1 && fields[4][1].getType() == 1
				 && fields[1][2].getType() == 1 && fields[1][3].getType() == 1 && fields[1][4].getType() == 1) {
			nwCorner = true;
			System.out.println("Found nwCorner via c3\n\n\n");
		}
		if(fields[0][1].getType() == 1 && fields[1][1].getType() == 1 && fields[2][1].getType() == 1 && fields[3][1].getType() == 1 && fields[4][1].getType() == 1 
				&& fields[0][2].getType() == 1 && fields[0][3].getType() == 1 && fields[0][4].getType() == 1) {
			nwCorner = true;
			System.out.println("Found nwCorner via c4\n\n\n");
		}
		}
		
		//check if ne corner found
		if(neCorner == false && currRoom.equals("unknown")) {
			if(fields[0][1].getType() == 1 && fields[1][1].getType() == 1 && fields[2][1].getType() == 1 && fields[3][1].getType() == 1
					 && fields[3][2].getType() == 1 && fields[3][3].getType() == 1 && fields[3][4].getType() == 1 && fields[4][0].getType() == 0) {
				neCorner = true;
				currRoom = "sw";
			}
			else if (fields[0][1].getType() == 1 && fields[1][1].getType() == 1 && fields[2][1].getType() == 1 && fields[3][1].getType() == 1
					 && fields[3][2].getType() == 1 && fields[3][3].getType() == 1 && fields[3][4].getType() == 1) {
				neCorner = true;
		}
		}
		else if (neCorner == false) {
			
		if(fields[0][0].getType() == 1 && fields[1][0].getType() == 1 && fields[2][0].getType() == 1 && fields[3][0].getType() == 1 && fields[4][0].getType() == 1
				&& fields[4][1].getType() == 1 && fields[4][2].getType() == 1 && fields[4][3].getType() == 1 && fields[4][4].getType() == 1) {
			neCorner = true;
			System.out.println("Found neCorner via c1\n\n\n");
		}
		if(fields[0][1].getType() == 1 && fields[1][1].getType() == 1 && fields[2][1].getType() == 1 && fields[3][1].getType() == 1 
				&& fields[4][1].getType() == 1 && fields[4][2].getType() == 1 && fields[4][3].getType() == 1 && fields[4][4].getType() == 1) {
			neCorner = true;
			System.out.println("Found neCorner via c2\n\n\n");
		}
		if(fields[0][1].getType() == 1 && fields[1][1].getType() == 1 && fields[2][1].getType() == 1 && fields[3][1].getType() == 1
				 && fields[3][2].getType() == 1 && fields[3][3].getType() == 1 && fields[3][4].getType() == 1) {
			neCorner = true;
			System.out.println("Found neCorner via c3\n\n\n");
		}
		if(fields[0][0].getType() == 1 && fields[1][0].getType() == 1 && fields[2][0].getType() == 1 && fields[3][0].getType() == 1 
				&& fields[3][1].getType() == 1 && fields[3][2].getType() == 1 && fields[3][3].getType() == 1 && fields[3][4].getType() == 1) {
			neCorner = true;
			System.out.println("Found neCorner via c4\n\n\n");
		}
		}
		
		//check if sw corner found
		if(swCorner == false && currRoom.equals("unknown")) {
			if(fields[1][0].getType() == 1 && fields[1][1].getType() == 1 && fields[1][2].getType() == 1 && fields[1][3].getType() == 1
					 && fields[2][3].getType() == 1 && fields[3][3].getType() == 1 && fields[4][3].getType() == 1 && fields[0][4].getType() == 0) {
				swCorner = true;
				currRoom = "ne";
			}
			else if(fields[1][0].getType() == 1 && fields[1][1].getType() == 1 && fields[1][2].getType() == 1 && fields[1][3].getType() == 1
					 && fields[2][3].getType() == 1 && fields[3][3].getType() == 1 && fields[4][3].getType() == 1) {
				swCorner = true;
			}
		}
		else if(swCorner == false) {
			
			if(fields[0][0].getType() == 1 && fields[0][1].getType() == 1 && fields[0][2].getType() == 1 && fields[0][3].getType() == 1 && fields[0][4].getType() == 1
					&& fields[1][4].getType() == 1 && fields[2][4].getType() == 1 && fields[3][4].getType() == 1 && fields[4][4].getType() == 1) {
				swCorner = true;
				System.out.println("Found swCorner via c1\n\n\n");
			}
			if(fields[1][0].getType() == 1 && fields[1][1].getType() == 1 && fields[1][2].getType() == 1 && fields[1][3].getType() == 1 
					&& fields[1][4].getType() == 1 && fields[2][4].getType() == 1 && fields[3][4].getType() == 1 && fields[4][4].getType() == 1) {
				swCorner = true;
				System.out.println("Found swCorner via c2\n\n\n");
			}
			if(fields[1][0].getType() == 1 && fields[1][1].getType() == 1 && fields[1][2].getType() == 1 && fields[1][3].getType() == 1
					 && fields[2][3].getType() == 1 && fields[3][3].getType() == 1 && fields[4][3].getType() == 1) {
				swCorner = true;
				System.out.println("Found swCorner via c3\n\n\n");
			}
			if(fields[0][0].getType() == 1 && fields[0][1].getType() == 1 && fields[0][2].getType() == 1 && fields[0][3].getType() == 1 
					&& fields[1][3].getType() == 1 && fields[2][3].getType() == 1 && fields[3][3].getType() == 1 && fields[4][3].getType() == 1) {
				swCorner = true;
				System.out.println("Found swCorner via c4\n\n\n");
			}
		}
		
		//check if se corner found
		
		if(seCorner == false && currRoom.equals("unknown")) {
			if(fields[0][3].getType() == 1 && fields[1][3].getType() == 1 && fields[2][3].getType() == 1 && fields[3][3].getType() == 1
					 && fields[3][2].getType() == 1 && fields[3][1].getType() == 1 && fields[3][0].getType() == 1 && fields[4][4].getType() == 0) {
				seCorner = true;
				currRoom = "nw";
			}
			else if(fields[0][3].getType() == 1 && fields[1][3].getType() == 1 && fields[2][3].getType() == 1 && fields[3][3].getType() == 1
					 && fields[3][2].getType() == 1 && fields[3][1].getType() == 1 && fields[3][0].getType() == 1) {
				seCorner = true;
				System.out.println("Found seCorner via c3\n\n\n");
			}
		}
		
		else if(seCorner == false) {
			if(fields[0][4].getType() == 1 && fields[1][4].getType() == 1 && fields[2][4].getType() == 1 && fields[3][4].getType() == 1 && fields[4][4].getType() == 1
					&& fields[4][3].getType() == 1 && fields[4][2].getType() == 1 && fields[4][1].getType() == 1 && fields[4][0].getType() == 1) {
				seCorner = true;
				System.out.println("Found seCorner via c1\n\n\n");
			}
			if(fields[0][4].getType() == 1 && fields[1][4].getType() == 1 && fields[2][4].getType() == 1 && fields[3][4].getType() == 1 
					&& fields[3][3].getType() == 1 && fields[3][2].getType() == 1 && fields[3][1].getType() == 1 && fields[3][0].getType() == 1) {
				seCorner = true;
				System.out.println("Found seCorner via c2\n\n\n");
			}
			if(fields[0][3].getType() == 1 && fields[1][3].getType() == 1 && fields[2][3].getType() == 1 && fields[3][3].getType() == 1
					 && fields[3][2].getType() == 1 && fields[3][1].getType() == 1 && fields[3][0].getType() == 1) {
				seCorner = true;
				System.out.println("Found seCorner via c3\n\n\n");
			}
			if(fields[0][3].getType() == 1 && fields[1][3].getType() == 1 && fields[2][3].getType() == 1 && fields[3][3].getType() == 1 
					&& fields[4][3].getType() == 1 && fields[4][2].getType() == 1 && fields[4][1].getType() == 1 && fields[4][0].getType() == 1) {
				seCorner = true;
				System.out.println("Found seCorner via c4\n\n\n");
			}
			
		}
		
	}
		
	}

