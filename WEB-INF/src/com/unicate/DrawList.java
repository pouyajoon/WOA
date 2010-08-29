package com.unicate;

import java.io.Serializable;
import java.util.ArrayList;

public class DrawList implements Serializable
{

	private static final long serialVersionUID = -3994573661173212989L;
	public ArrayList<DrawObject> out;

	public DrawList(ArrayList<DrawObject> out) {
		super();
		this.out = out;
	}
	

}
