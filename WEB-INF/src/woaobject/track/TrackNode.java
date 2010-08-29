package woaobject.track;

import java.util.HashMap;

import org.jbox2d.common.Vec2;

import woaobject.WOAObject;
import world.Position;
import ant.Ant;
import ant.properties.Property;

public class TrackNode
{
	public WOAObject woao = null;
	public HashMap<WOAObject, TrackNode> toTargets = new HashMap<WOAObject, TrackNode>();
	public TrackNode toHome = null;
	public HashMap<WOAObject, Float> motivations = new HashMap<WOAObject, Float>();
	public HashMap<WOAObject, Property> antsRequested = new HashMap<WOAObject, Property>();
	
	public float motivation = 0;
	//public Position pos = null;
	public GraphTrack gt = null;
	public Property antRequested = new Property("antRequested", 0);	
	public HashMap<WOAObject, HashMap<Ant, Boolean>> antsTracking = new HashMap<WOAObject, HashMap<Ant, Boolean>>();
	public float antRequestedMax = 0;

 
	public TrackNode(WOAObject _woao, GraphTrack _gt)
	{
		woao = _woao;
		//pos = woao.getPos();
		gt = _gt;

	}
	
	public float getMotivation()
	{
		float max = 0;
		for(Float m : motivations.values())
		{
			max += m;
		}
		return max;
	}	
	
	
	public Boolean stillRequestAnt(WOAObject target)
	{
		//Property antrequest = antRequestedForTarget(target);
		//if (antrequest == null) return false;
		//float diff = antrequest.getMax() - antrequest.getCurrent();
		//
		if (antsTracking.containsKey(target))
		{
			//System.out.println("smell ant request" + pos.toString() + ": " + antRequestedMax + ", " + antsTracking.get(target).size());
			float f = antsTracking.get(target).size();
			if (antRequestedMax - f > 0.0f)
			{				
				//System.out.println("still some space");
				return true;
			}
			else
			{
				return false;
			}
			//return false;
		}
		else
		{
			System.out.println("no more target on this node");
			woao.addToGarbage();
			return false;	
		}		
	}
	
	/*
	public Property antRequestedForTarget(WOAObject target)
	{
		if (antsRequested.containsKey(target))
		{
			Property antRequested = antsRequested.get(target);
			return antRequested;
			//return antRequested.getCurrent();			
		}
		else
			return null;
	}
	*/
	
	/*
	public void setAntRequested(WOAObject target, float motivation)
	{
		//Property antRequested = new Property("antRequested", ) 
		//antRequested.max = motivation;
		antRequested.current = motivation / 20;
		antsRequested.put(target, antRequested);			
	}
	*/
	
	
	public void reduceAntRequested(WOAObject target, Ant _ant)
	{
		/*
		if (antsRequested.containsKey(target))
		{ // if target has value
			Property antRequested = antsRequested.get(target);
			antRequested.setCurrent(antRequested.getCurrent() - 1);
			//antRequested.setMax(motivation / 20);
		}
		*/
		//Player p = _ant.getColony().getQueen().player;
		HashMap<Ant, Boolean> ants = antsTracking.get(target); 
		if (ants != null)
		{
			ants.remove(_ant);
		}
		else
		{
			ants = new HashMap<Ant, Boolean>();
			antsTracking.put(target, ants);
		}
		
	}
	
	public void increaseAntRequested(WOAObject target, Ant _ant)
	{
		/*
		if (antsRequested.containsKey(target))
		{ // if target has value
			Property antRequested = antsRequested.get(target);
			antRequested.setCurrent(antRequested.getCurrent() + 1);
			//antRequested.setMax(motivation / 20);
		}
		*/
		//Player p = _ant.getColony().getQueen().player;
		HashMap<Ant, Boolean> ants = antsTracking.get(target); 
		if (ants != null)
		{
			ants.put(_ant, true);
		}
		else
		{
			ants = new HashMap<Ant, Boolean>();
			ants.put(_ant, true);			
		}
		antsTracking.put(target, ants);
	}	
	
	public void updateMotivation(WOAObject target, float motivation)
	{
		//System.out.println("update node motivation :target ID: " + target.id + ", motivation=" + motivation);
		motivations.put(target, motivation);
		
		antRequestedMax = motivation / 20.0f;
		
		//HashMap<Ant, Boolean> ants = antsTracking.get(target); 
		//ants = new HashMap<Ant, Boolean>();		
		/*
		Property antRequested = null;
		// update number of requested ant for target
		if (antsRequested.containsKey(target))
		{ // if target has value
			antRequested = antsRequested.get(target);
			antRequested.setMax((motivation / 20.0f) + 1);
			//antsRequested.put(target, antRequested);
		}
		else
		{
			antRequested = new Property("antRequested", (motivation / 20.0f) + 1);
			System.out.println("new property ant request");
			//antRequested.setMax((motivation / 20.0f) + 1);
			antRequested.setCurrent(0);
		}
		antsRequested.put(target, antRequested);
		*/
	}

	public Position getBodyPosition() {
		// TODO Auto-generated method stub
		if (woao != null)
		{
			return new Position(woao.getPos()._map, woao.myBody.getPosition().x, woao.myBody.getPosition().y);	
		}
		else
			return null;
		
	}

}
