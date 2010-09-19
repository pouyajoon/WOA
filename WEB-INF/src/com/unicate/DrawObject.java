package com.unicate;

import java.io.Serializable;

import vnd.world.Position;

public class DrawObject implements Serializable 
{

	private static final long serialVersionUID = 1885648236030829600L;

	public DrawObject(Integer d, Position pos, String type) {
		super();
		this.id = d;
		this.pos = pos;
		this.type = type;
		//this.size = _size;	
	}
	public Integer id;
	public Position pos;
	public String type;
	public float size;
	public float direction;
	//public Position food_storage;

	@Override 
	public String toString()
	{
		String msg = "[" + id.toString()+ ", "+ pos.toString() + ", size : " + size + "]";
		return msg;
	}
	
}
