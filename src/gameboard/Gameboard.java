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

public class Gameboard
{
	Field[][] fields;
	
	public Gameboard(boolean doClient)
	{
		if(doClient)
		{
			fields = new Field[2*GlobalGameboard.sightRadius+1][2*GlobalGameboard.sightRadius+1];
			
			for(int i = 0; i < fields.length; i++)
			{
				for(int j = 0; j <fields[i].length; j++)
				{
					fields[i][j] = new Field(0, i, j);
				}
			}
		}
		else
		{
			makeBoard();
		}
	}
	
	public Gameboard(Ant[] ants)
	{
		makeBoard();
		
		int randX;
		int randY;
		
		for(int i = 0; i < GlobalGameboard.playersNr; i++)
		{
			randX = new Double(Math.random()*GlobalGameboard.roomsNrX).intValue();
			randY = new Double(Math.random()*GlobalGameboard.roomsNrY).intValue();
			
			//scaling
			randX = (GlobalGameboard.roomSize/2+1)+randX*(GlobalGameboard.roomSize+1);
			randY = (GlobalGameboard.roomSize/2+1)+randY*(GlobalGameboard.roomSize+1);
			
			if(fields[randX][randY].getAntId() == -1)
			{
				fields[randX][randY].setAnt(ants[i]);
				ants[i].setPosition(fields[randX][randY], this);
			}
			else
			{
				i--;
			}
		}
		
		for(int i = 0; i < GlobalGameboard.foodNr; i++)
		{
			randX = new Double(Math.random()*fields.length).intValue();
			randY = new Double(Math.random()*fields[randX].length).intValue();
			
			if(fields[randX][randY].getType() == 0 && fields[randX][randY].getAntId() == -1 && !fields[randX][randY].hasFood())
			{
				fields[randX][randY].setFood(true);
			}
			else
			{
				i--;
			}
		}
	}

	private void makeBoard()
	{
		fields = new Field[GlobalGameboard.roomsNrX*(GlobalGameboard.roomSize+1)+1][GlobalGameboard.roomsNrY*(GlobalGameboard.roomSize+1)+1];
		
		int xPos = 0;
		int yPos = 0;
		for(int i = 0; i <= GlobalGameboard.roomsNrX; i++)
		{
			yPos = 0;
			for(int j = 0; j < fields[xPos].length; j++)
			{
				yPos = j;
				if(i != 0 && i != GlobalGameboard.roomsNrX &&
				   (yPos - GlobalGameboard.roomSize/2-1)%(GlobalGameboard.roomSize+1) == 0)
				{
					fields[xPos][yPos] = new Field(2, xPos, yPos);
					if(Math.random() < 0.5) fields[xPos][yPos].setDoorOpen(false);
				}
				else
				{
					fields[xPos][yPos] = new Field(1, xPos, yPos);
				}
				
			}
			
			
			xPos += GlobalGameboard.roomSize+1;
		}
		
		
		for(int i = 0; i < fields.length; i++)
		{
			xPos = i;
			yPos = 0;
			for(int j = 0; j <= GlobalGameboard.roomsNrY; j++)
			{
				
				if(j != 0 && j != GlobalGameboard.roomsNrY &&
				  (xPos - GlobalGameboard.roomSize/2-1)%(GlobalGameboard.roomSize+1) == 0)
				{
					fields[xPos][yPos] = new Field(2, xPos, yPos);
				}
				else
				{
					fields[xPos][yPos] = new Field(1, xPos, yPos);
				}
				
				yPos += GlobalGameboard.roomSize+1;
			}
		}
		
		for(int i = 0; i < fields.length; i++)
		{
			for(int j = 0; j < fields[i].length; j++)
			{
				if(fields[i][j] == null) fields[i][j] = new Field(0, i, j);
			}
		}
		
	}

	public Gameboard(Field position, Gameboard gameboard, boolean obstructiveWalls)
	{
		fields = new Field[GlobalGameboard.sightRadius*2+1][GlobalGameboard.sightRadius*2+1];
		
		boolean copyFloorState;
		
		int newI, newJ;
		
		for(int i = position.getX() - GlobalGameboard.sightRadius; i <= position.getX() + GlobalGameboard.sightRadius; i++)
		{
			for(int j = position.getY() - GlobalGameboard.sightRadius; j <= position.getY() + GlobalGameboard.sightRadius; j++)
			{
				newI = i - position.getX() + GlobalGameboard.sightRadius;
				newJ = j - position.getY() + GlobalGameboard.sightRadius;
				
				if(i < 0 || j < 0 || i >= gameboard.fields.length || j >= gameboard.fields[i].length)
				{
					fields[newI][newJ] = new Field(null, newI, newJ, false);
				}
				else
				{
					copyFloorState = true;
					if(obstructiveWalls && gameboard.getRoomIndex(gameboard.fields[i][j]) != gameboard.getRoomIndex(position)) copyFloorState = false;
					fields[newI][newJ] = new Field(gameboard.fields[i][j], newI, newJ, copyFloorState);
				}
			}
		}
	}
	
	private int getRoomIndex(Field position)
	{
		int index = -1;
		
		for(int begin = 1; begin < fields.length; begin += GlobalGameboard.roomSize+1)
		{
			if(position.getX() >= begin && position.getX() < begin+GlobalGameboard.roomSize)
			{
				index = begin;
			}
		}
		
		for(int begin = 1; begin < fields[position.getX()].length; begin += GlobalGameboard.roomSize+1)
		{
			if(position.getY() >= begin && position.getY() < begin+GlobalGameboard.roomSize)
			{
				index += begin*10000;
			}
		}
		
		return index;
	}

	public Gameboard(String xml)
	{
		String tmp = getXMLfield("fields", xml);
		String[] cols = getXMLfieldsArray("col", tmp);
		String[] tmpArr;
		
		fields = new Field[cols.length][cols[0].split("\\s+").length];
		
		for(int i = 0; i < cols.length; i++)
		{
			tmpArr = cols[i].split("\\s+");
			
			for(int j = 0; j < tmpArr.length; j++)
			{
				fields[i][j] = new Field(new Integer(tmpArr[j]).intValue(), i, j);
			}
		}
		
		tmp = getXMLfield("states", xml);
		String[] states;
		if(tmp.equals("")) states = new String[0];
		else               states = getXMLfieldsArray("state", tmp);
		
		for(int i = 0; i < states.length; i++)
		{
			tmpArr = states[i].split("\\s+");
			
			fields[new Integer(tmpArr[0]).intValue()][new Integer(tmpArr[1]).intValue()].setState(tmpArr, this);
		}
	}

	private String getXMLfield(String name, String xml)
	{
		String tmp = xml.replaceAll(".*<"+name+">", "");
			   tmp = tmp.replaceAll("</"+name+">.*", "");
		
		return tmp;
	}
	
	private String[] getXMLfieldsArray(String name, String xml)
	{
		xml = xml.replaceAll("^<"+name+">", "");
		String[] tmpArr = xml.split("<"+name+">");
		
		for(int i = 0; i < tmpArr.length; i++)
		{
			tmpArr[i] = tmpArr[i].split("</"+name+">")[0];
		}
	    
		return tmpArr;
	}

	public Field[][] getFields()
	{
		return fields;
	}
	
	public String encodeXML()
	{
		String result = "<gameboard>";
		String states = "<states>";
		
		result += "<fields>";
		
		for(int i = 0; i < fields.length; i++)
		{
			result += "<col>";
			
			for(int j = 0; j < fields[i].length; j++)
			{
				result += fields[i][j].getType()+" ";
				states += fields[i][j].getState();
			}
			
			result += "</col>";
		}
		
		result += "</fields>";
		states += "</states>";
		
		result += states;
		
		return result+"</gameboard>";
	}

	public boolean isMoveable(int x, int y)
	{
		return fields[x][y].isMoveable();
	}

	public Field getField(int x, int y)
	{
		return fields[x][y];
	}
}
