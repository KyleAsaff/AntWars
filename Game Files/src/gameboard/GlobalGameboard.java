package gameboard;

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

public class GlobalGameboard
{
	/*
	 * GAMEBOARD
	 */	
	public static int roomSize = 11;
	public static int roomsNrX = 2;
	public static int roomsNrY = 2;
	
	public static int playersNr = 2;
	
	public static int foodNr = 40;
	public static int maxNrOfRounds = 1000;
	
	public static int sightRadius = 2;
	public static boolean obstructiveWalls = true;
	
	/*
	 * VISUALIZATION
	 */	
	
	public static int windowWidthControl = 200;
	public static int windowHeightControl = 600;
	
	public static int windowWidth = 800;
	public static int windowHeight = 600;
	
	public static int vizBeginX = 10;
	public static int vizBeginY = 10;
	public static int fieldSize = 20;
	
	public static int windowWidthClnt = 200;
	public static int windowHeightClnt = 240;
	
	public static int vizBeginXClnt = 40;
	public static int vizBeginYClnt = 40;
	public static int fieldSizeClnt = 20;
	
	/*
	 * REFEREE
	 */
	
	public static double doorSwitchFrequency = 0.1;
	public static long vizualizationTime = 200;
	public static long playerMoveTime = 5000;
	
	/*
	 * ECHO
	 */
	
	public static boolean doEcho = false;
		
	public static void setClient()
	{
		vizBeginX = vizBeginXClnt;
		vizBeginY = vizBeginYClnt;
		fieldSize = fieldSizeClnt;
	}
}
