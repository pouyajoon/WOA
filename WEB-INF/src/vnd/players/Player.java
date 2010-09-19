package vnd.players;

import java.util.ArrayList;

import vnd.woaobject.children.Queen;


public class Player {
	public String name;
	public ArrayList<Queen> queens = new ArrayList<Queen>();

	public Player(String name) {
		super();
		this.name = name;
	}

	public void setQueen(Queen q) {
		queens.add(q);
	}
}
