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

public class Ant
{
	int id;
	Field position;
	Gameboard gameboard;
	boolean isDead = false;

	public Ant(int id)
	{
		this.id = id;
	}
	
	public void setPosition(Field position, Gameboard gameboard)
	{
		this.position = position;
		this.gameboard = gameboard;
	}
	
	public int getId()
	{
		return id;
	}
	
	/*public Gameboard createSubBoard()
	{
		return new Gameboard(position, gameboard);
	}*/

	public Field getPosition()
	{
		return position;
	}

	public int makeMove(String move)
	{
		int newX = -1;
		int newY = -1;
		
		if((move.equals("N") && gameboard.isMoveable(position.getX(), position.getY()-1)))
		{
			newX = position.getX();
			newY = position.getY()-1;
		}
		else if (move.equals("E") && gameboard.isMoveable(position.getX()+1, position.getY()))
		{
			newX = position.getX()+1;
			newY = position.getY();
		}
		else if (move.equals("S") && gameboard.isMoveable(position.getX(), position.getY()+1))
		{
			newX = position.getX();
			newY = position.getY()+1;
		}
		else if (move.equals("W") && gameboard.isMoveable(position.getX()-1, position.getY()))
		{
			newX = position.getX()-1;
			newY = position.getY();
		}
		else   
		{
			return -1;
		}
		
		position.setAnt(null);
		position = gameboard.getField(newX, newY);
		
		if(position.getAntId() >= 0)
		{
			position.setAnt(this);
			return 2;
		}
		
		position.setAnt(this);
		
		if(position.hasFood)
		{
			position.hasFood = false;
			return 1;
		}
		
		return 0;
	}

	public void setDead(boolean isDead)
	{
		this.isDead = isDead;
		if(isDead && position.getAnt().equals(this)) position.setAnt(null);
	}
	
	public boolean isDead()
	{
		return isDead;
	}
}
