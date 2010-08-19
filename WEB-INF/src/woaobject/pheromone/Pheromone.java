package woaobject.pheromone;

import woaobject.WOAObject;
import world.AntBox;
import world.Position;


public class Pheromone extends WOAObject {

	public Pheromone(AntBox antBox, Position pos) {
		super(antBox, pos);
		// TODO Auto-generated constructor stub
	}
/*
	public Integer lifetime = 600;
	public Mission mission = null;
	public Pheromone targetPheromone = null;
	public Pheromone homePheromone = null;
	public HashMap<Integer, Float> motivation = new HashMap<Integer, Float>();
	public HashMap<Integer, Track> tracks = new HashMap<Integer, Track>();
	public Track current_track = null;
	public float lastMotivationMax = 0;
	
	public Pheromone(AntBox w, Position _pos, Integer _target_id, Track _t)//, Pheromone _targetPheromone) 
	{
		super(w, _pos);
		// TODO Auto-generated constructor stub
		
		type = "pheromone";
		adn.set("size", new Property("size", 50.0f));		
		adn.set("weight", new Property("weight", 0.0f));	
		adn.set("lifetime", new Property("lifetime", 600));
		
		//targetPheromone = _targetPheromone;
		targetPheromone = null;
		
		tracks.put(_target_id, _t);
		current_track = _t;
		
		//setMotivation(0, 0);
		//adn.getCurrent("size")
		
		//System.out.println("at position : " + pos.getVec2());
		
		Body b = currentBox.CreateJCircleBodyStatic(pos.getVec2(), adn.getCurrent("size"), adn.getCurrent("weight"), 0.0f);
		
		//Body b = world.CreateJBoxBody(new Vec2(adn.getCurrent("size"), adn.getCurrent("size")), pos.getVec2(), adn.getCurrent("weight"), 0.0f);
		//System.out.println("body created POS : " + b.getPosition().toString());
		setMyBody(b);
		setBodyAngle(new Float(0));
		// set fake motivation
		
		if (myBody != null)
		{
			myBody.m_shapeList.m_categoryBits = 0x0002;
			myBody.m_shapeList.m_maskBits = 0x0004;				
		}
		//myBody.m_shapeList.m_groupIndex = -2;
		
		Shape s = myBody.getShapeList();
		while (s != null)
		{
			System.out.println("looping on shapes");
			s.m_categoryBits = 0x0004;
			s.m_maskBits = 0x0002;
			s.m_groupIndex = -8;
			s = s.getNext();
		}
		
		//myBody.getShapeList().m_categoryBits = 0x0004;
		//myBody.getShapeList().m_maskBits = 0x0002;
		//myBody.getShapeList().m_groupIndex = -8;
		//currentBox.world.addPheromone(this);
	}
	
	public float getSize()
	{
		return (getMotivationMax() / 10);
		//return (adn.getCurrent("lifetime") * 2) / (adn.getCurrent("size"));
	}
	
	public float getWeight()
	{
		return 0;
	}
	
	public void localfinalize()
	{
		current_track.pheromones.remove(this.id);
		//currentBox.world.pheromones.remove(this.id);
	}	

	public void setMotivation(Integer target_id, float _motivation)
	{
		//System.out.println("set for pheromone : [" + id +"], target " + target_id + " & motivation : " + _motivation);
		motivation.put(target_id, _motivation);
	}
	
	public float getMotivationMax()
	{
		
		float max = 0;
		for (Float f : motivation.values())
		{
			max += f.floatValue();
		}
		return max;
	}
	
	
	public void life() throws Throwable
	{
		//adn.updateCurrent("lifetime", adn.getCurrent("lifetime") - 1);
		
		float lifetime = adn.getCurrent("lifetime");
		//lifetime--;
		if (lifetime == 0.0f)
		{
			//finalize();
			addToGarbage();
			adn.updateCurrent("lifetime", 0);
		}
		else
		{
		}
		
		float new_size = getSize();
		//System.out.println("pheromone size : " + new_size);		
		if (lastMotivationMax != new_size)
		{
			if (new_size == 0)
			{
				addToGarbage();
				targetPheromone = null;
				homePheromone = null;
				lastMotivationMax = new_size;
				//lifetime = new_size;
			}
			else
			{
				//System.out.println("resize pheromone : " + toString());
				BodyUtils.resizeBody(this, new_size, 0);
				lastMotivationMax = new_size;				
			}
		}
		//AABB smellarea = BodyUtils.ViewBox(this, pos.getVec2(), 100);
		//ArrayList<WOAObject> ants = BodyUtils.lookFor(this, "ant", smellarea);
		//for (WOAObject a : ants)
		{
			//System.out.println("smell this : " + a.toString());
		}
		
	}

	public String toTrackString()
	{
		return "[home : " + this.homePheromone + ", me : "+ this.id + ", target :" + this.targetPheromone + "]";
	}
	*/
}
