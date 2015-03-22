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

public class Field
{
	int x;
	int y;
	
	/* ************
	 * Field types:
	 * 0 - floor
	 * 1 - wall
	 * 2 - door
	 * ************/
	int type;
	
	boolean hasFood = false;
	boolean doorOpen = true;
	Ant ant;
	
	public Field(int type, int x, int y)
	{
		this.type = type;
		this.x = x;
		this.y = y;
	}
	
	public Field(Field field, int x, int y, boolean copyFloorState)
	{
		this.x = x;
		this.y = y;
		
		if(field == null)
		{
			type = 1;
		}
		else 
		{
			type = field.type;
			if(copyFloorState)
			{
				hasFood = field.hasFood;
				ant = field.ant;
			}
			doorOpen = field.doorOpen;
		}
	}

	public int getType()
	{
		return type;
	}
	
	public void setAnt(Ant ant)
	{
		this.ant = ant;
	}
	
	public int getAntId()
	{
		if(ant == null) return -1;
		return ant.getId();
	}

	public void setFood(boolean hasFood)
	{
		this.hasFood = hasFood;
	}
	
	public boolean hasFood()
	{
		return hasFood;
	}

	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}

	public String getState()
	{		
		if(type == 0 && hasFood)
		{
			return "<state>"+x+" "+y+" food hasFood</state>";
		}

		if(type == 2 && !doorOpen)
		{
			return "<state>"+x+" "+y+" door doorClosed</state>";
		}
		
		if((type == 0 || type == 2) && ant != null)
		{
			return "<state>"+x+" "+y+" antId "+ant.getId()+"</state>";
		}
				
		return "";
	}

	public void setState(String[] settings, Gameboard gameboard)
	{
		if(settings[2].equals("food") && settings[3].equals("hasFood")) hasFood = true;
		if(settings[2].equals("door") && settings[3].equals("doorClosed")) doorOpen = false;
		if(settings[2].equals("antId"))
		{
			ant = new Ant(new Integer(settings[3]).intValue());
						
			ant.setPosition(this, gameboard);
		}
	}

	public boolean isMoveable()
	{
		if(type == 0) return true;
		if(type == 2 && doorOpen) return true;
		return false;
	}

	public Ant getAnt()
	{
		return ant;
	}

	public boolean isDoorOpen()
	{
		return doorOpen;
	}

	public void setDoorOpen(boolean doorOpen)
	{
		this.doorOpen = doorOpen;		
	}

	public Field getRalativePosition(Field absolutePosition)
	{
		return new Field(-1, x - absolutePosition.x, y - absolutePosition.y);
	}
}
