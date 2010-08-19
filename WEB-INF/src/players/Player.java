package players;

import java.util.ArrayList;

import ant.Queen;

public class Player 
{
	public String name;
	public ArrayList<Queen> queens = new ArrayList<Queen>();

	public Player(String name) {
		super();
		this.name = name;
	}
	
	public void setQueen(Queen q)
	{
		queens.add(q);
	}
}
