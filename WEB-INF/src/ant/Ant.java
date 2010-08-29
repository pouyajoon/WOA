package ant;

import java.util.ArrayList;
import java.util.Set;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.testbed.BodyUtils;

import players.Player;

import woaobject.WOAObject;
import woaobject.pheromone.NodePheromone;
import woaobject.pheromone.Pheromone;

import world.AntBox;
import world.AntZone;

import ant.properties.Property;

import com.woa.missions.Mission;
import com.woa.missions.MissionTrack;

public class Ant extends WOAObject
{
	private Colony colony;
	public Joint mandible = null;
	public Mission currentMission = null;
	public MissionTrack currentTrackMission = null;
	public Pheromone lastPheromone = null;
	
	public Ant(Colony c)
	{			
		super(c.currentBox,c.getPos().clone());		
		this.setColony(c);
		// create ant in ant box
		type = "ant";
		float r = 5f;
		adn.set("size", new Property("size", r));		
		adn.set("weight", new Property("weight", 1.0f));
		adn.set("storage_food", new Property("storage_food", 26.0f));		
		adn.set("speed_max", new Property("speed_max", 500.0f));
		//adn.set("acceleration", new Property("acceleration", (1 - (adn.getMax("size") * 10) / 100) * 400 + adn.getMax("speed_max")));
		// how many food the ant can take by step
		adn.set("food_collect_capacity", new Property("food_collect_capacity", 2.0f));
		// how far the ant can view
		adn.set("view_distance", new Property("view_distance", 200.0f));
		//set an empty storage food
		adn.updateCurrent("storage_food", 0);
		//adn.put("size", new Property("size", 2));
		//System.out.println("ant pos : " + pos.toString());
		//setMyBody(c.world.antBox.create_body(adn.getCurrent("size"), adn.getCurrent("weight"), c.myBody.getPosition()));
		//myBody.m_shapeList.m_categoryBits = 0x0002;
		//myBody.m_shapeList.m_maskBits = 0x0004;		
		createBody(currentBox);
		//currentBox.world.log.info("ant borned pos : " + pos.toString());
		//System.out.println("ant borned mass : " + myBody.m_mass);
		//missions.add(new Mission(this));
		//currentMission = new Mission(this);
	}
	
	@Override
	public void createBody(AntBox _targetBox)
	{
		Body b = _targetBox.CreateJCircleBody(getPos().getVec2(), adn.getCurrent("size"), adn.getCurrent("weight"), 0.0f);
		setMyBody(b);
		setBodyAngle(new Float(0));
		myBody.m_shapeList.getFilterData().groupIndex = 2;
		currentBox = _targetBox;
	}
	
	@Override
	public void destroyBody()
	{
		currentBox.destroyBody(myBody);
		myBody = null;
	}

	@Override
	public void boundaryViolated()
	{
		if (myBody == null) return;
		int direction = currentBox.getBoundarySide(myBody.getPosition());
		AntBox boxTarget = currentBox.world.antzone.getTargetBoxUsingDirection(currentBox, direction);
		pos =  currentBox.world.antzone.getTargetPosition(currentBox, boxTarget, direction, myBody.getPosition());
		destroyBody();
		createBody(boxTarget);
	}

	/**
	 * calculate how many the ant can collect and the food capacity and collect it
	 * @param other : the object to eat
	 */
	public void eatFood(WOAObject other)
	{
		WOAObject 	ant = this;
		float 		food_stock_can_be_recolted = 0.0f;

		// calculate how many food the ant can still carry
		float ant_food_place = ant.adn.getMax("storage_food") - ant.adn.getCurrent("storage_food");
		// calculate how many the ant can collect depending on his stock
		float ant_food_recolt_capacity = 0.0f;
		if (ant_food_place - ant.adn.getCurrent("food_collect_capacity") > 0.0f)
		{ 
			// if stock capacity is less the stock max
			ant_food_recolt_capacity = ant.adn.getCurrent("food_collect_capacity");
		}
		else
		{ 
			// only collect the difference between stock max & current
			ant_food_recolt_capacity = ant_food_place;
		}
		if ((other.adn.getCurrent("storage_food") - ant_food_recolt_capacity) > 0)
		{ 
			// if the ant can collect  more stock than food capacity, set the ant collect as max
			food_stock_can_be_recolted = ant_food_recolt_capacity;
		}
		else
		{ 
			// set all the food stock to be collected
			food_stock_can_be_recolted = other.adn.getCurrent("storage_food");
		}
		// remove from food maximum ant can carry
		other.adn.reduce("storage_food", food_stock_can_be_recolted);
		// add to the stomach!
		ant.adn.increase("storage_food", food_stock_can_be_recolted);						
	}

	
	/**
	 * Create an AABB world as viewzone
	 * @return the view zone
	 */
	public AABB viewZone()
	{
		if (myBody == null) 
			return null;
    	Vec2 	p = myBody.getPosition();
    	// Make a small box.
        Vec2 	d = new Vec2(adn.getCurrent("view_distance"), adn.getCurrent("view_distance"));
        AABB 	aabb = new AABB(p.sub(d), p.add(d));
        // create a view border around the ant
		Vec2 	worldLower = aabb.lowerBound;
		Vec2 	worldUpper = aabb.upperBound;
    	Vec2[] 	vsw = new Vec2[4];
    	
		vsw[0]= new Vec2(worldLower.x, worldLower.y);
		vsw[1]= new Vec2(worldUpper.x, worldLower.y);
		vsw[2]= new Vec2(worldUpper.x, worldUpper.y);
		vsw[3]= new Vec2(worldLower.x, worldUpper.y);		
		return aabb;
	}	
	

	public ArrayList<WOAObject> lookFor(String[] search_types)
	{
		ArrayList<WOAObject> 	woaobject_list = new ArrayList<WOAObject>();
        AABB 					aabb = viewZone();
        if (aabb == null) 
        	return null;
        // Query the world for overlapping shapes.
        int 					k_maxCount = 20;
        Shape 					shapes[] = currentBox.m_world.query(aabb, k_maxCount);
        for (int j = 0; j < shapes.length; j++) 
        {
            Body shapeBody = shapes[j].getBody();
            if (shapeBody.equals(this.myBody)) continue;            
            if (shapeBody.isStatic() == false)
            {
            	if (shapes[j].m_body.getUserData() != null)
            	{
            		WOAObject woaobject = (WOAObject)shapes[j].m_body.getUserData();
            		for (String t : search_types)
            		{
            			if (woaobject.type.equals(t))
                			woaobject_list.add(woaobject);
            		}                		
            	}
            	else
            		System.out.println("userData is null on looking");
            }
        }
        return woaobject_list;
	}
	
	public void collide(WOAObject other, ContactPoint cp)
	{
		// be sure the current mission has a target
		if (currentTrackMission != null)
			currentTrackMission.collisionWithTarget(this, other);
	}	

	public void collisionPersists(WOAObject other)
	{
	}		
	
	public void catchWithMandible(WOAObject other, Vec2 cp_point)
	{		
		// if mandible is not used
		if (mandible == null)
		{
			Vec2 anchor = this.myBody.getWorldCenter();//this.myBody.getPosition();
			mandible = currentBox.createJoint(jointInit(other, anchor));	
			mandible.m_collideConnected = false;
			other.catchedBy.put(this.id, this);
		}		
	}
	
	public void releaseMandible()
	{
		if (mandible != null)
		{
			WOAObject woaObjectOther = (WOAObject)mandible.m_body2.getUserData();
			woaObjectOther.catchedBy.remove(this.id);
			mandible.m_body2.m_linearVelocity.x = 0;
			mandible.m_body2.m_linearVelocity.y = 0;
			jointRelease(mandible);
			mandible = null;			
		}
	}
	
	
	public WOAObject searchForFood()
	{
		WOAObject 	target = null;
		String[] 	s = new String[1];
		
		s[0] = "food";
		ArrayList<WOAObject> woaobject_list = this.lookFor(s);
		if (woaobject_list != null)
		{
			if (woaobject_list.size() == 1)
			{
				// set the target on the food
				target = woaobject_list.get(0);
				// go catch it.	
				//state = 1;
				return target;
			}
		}
		return null;
	}
	
	/**
	 * the ant roam around using a random algorithm
	 */
	public void roam()
	{
		if (myBody == null) 
			return;
		long 	dir = Math.round((Math.random() * 16));
		float 	x = 0;
		float 	y = 0;
		float angle = myBody.getAngle();// = 0.785f;
		float angle_rot = 0.785f;

		if (dir == 0) // go left
			angle += angle_rot;
		if (dir == 1) // go right
			angle -= angle_rot;
		x = (float)Math.cos(angle) * adn.getCurrent("speed_max");
		y = (float)Math.sin(angle) * adn.getCurrent("speed_max");
		Vec2 v = new Vec2 (x, y);
		addVelocity(v);
	}

	
	/**
	 * set the new body angle using the velocity to set direction
	 * @param v : Velocity vec2
	 */
	public void setAngleFromVelocity(Vec2 v)
	{
		Float angle_r = BodyUtils.calculateAngle(v);
		if (!angle_r.isNaN())
			setBodyAngle(new Float(angle_r));
	}
	
	public Vec2 reduceVelocityFromMaxSpeed(Vec2 v)
	{
		float speed_max = this.adn.getCurrent("speed_max");		
		if (Math.abs(v.x) - speed_max > 0)
			v.x = speed_max * (v.x / Math.abs(v.x)) ;
		if (Math.abs(v.y) - speed_max > 0)
			v.y = speed_max * (v.y / Math.abs(v.y));
		return v;
	}
	
	public void addVelocity(Vec2 v)
	{
		// limit the movement to maximum move capacity
		v = reduceVelocityFromMaxSpeed(v);
		if (mandible == null)
		{
			// change angle from the velocity
			setAngleFromVelocity(v);
		}		
		myBody.setLinearVelocity(v);
	}
	
	
	/**
	 * provide the translate vec2 for the provided direction
	 * @param direction : AntZone directions
	 * @return the force to apply
	 */
	public Vec2 getForceToGoToDirection(Integer direction)
	{
		Vec2 	force = new Vec2(0, 0);
		float 	limit_speed = adn.getCurrent("speed_max");
		
		switch (direction) 
		{
			case AntZone.DIRECTION_WEST:
				force.x = -limit_speed;				
			break;
			case AntZone.DIRECTION_NORTH_WEST:
				force.x = -limit_speed;
				force.y = limit_speed;				
			break;
			case AntZone.DIRECTION_NORTH:
				force.y = limit_speed;				
			break;
			case AntZone.DIRECTION_NORTH_EAST:
				force.x = limit_speed;
				force.y = limit_speed;
			break;
			case AntZone.DIRECTION_EAST:
				force.x = limit_speed;				
			break;
			case AntZone.DIRECTION_SOUTH_EAST:
				force.x = limit_speed;
				force.y = limit_speed;				
			break;
			case AntZone.DIRECTION_SOUTH:
				force.y = -limit_speed;				
			break;
			case AntZone.DIRECTION_SOUTH_WEST:
				force.x = -limit_speed;
				force.y = -limit_speed;				
				
			break;
			default:
			break;
		}
		return force;
	}

	
	public Vec2 MoveToWOAObject(WOAObject aim)
	{
		if (aim != null)
		{
			if (aim.getPos()._map.equals(getPos()._map))
			{
				Vec2 dest = aim.getMyBody().getPosition();
				Vec2 me = this.getMyBody().getPosition();
				Vec2 apply_force = dest.sub(me);
				addVelocity(apply_force);
				UpdateBodyPosition();
				return apply_force;				
			}
			else //change the map
			{
				Integer direction = AntZone.NextBoxInMap(currentBox, aim.currentBox);
				System.out.println("need to change map, direction " +direction);
				Vec2 apply_force = getForceToGoToDirection(direction);
				System.out.println("applying: " +apply_force.toString());
				addVelocity(apply_force);
				UpdateBodyPosition();
				return apply_force;
			}
		}
		else
			return null;
	}	

	public boolean seeColony(WOAObject search_colony)
	{
		try 
		{
			float distance = getDistance(search_colony);
			return distance < adn.getCurrent("view_distance");			
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		return false;
	}
	
	public void smellPheromone(NodePheromone p)
	{
		if (currentTrackMission == null)
		{		
			if (p.tn.toTargets.size() > 0)
			{				
				WOAObject target = getFirstTarget(p.tn.toTargets.keySet());
				Boolean stillRequestAnt = p.tn.stillRequestAnt(target);
				if (stillRequestAnt)
				{
					currentTrackMission = new MissionTrack(currentBox.world, target);
					currentTrackMission.gt = p.tn.gt;	
					currentTrackMission.currentNode = p.tn;
					currentTrackMission.state = MissionTrack.STATE_FOLLOW_TRACK_TO_TARGET;					
				}	
				else
				{
					if (p.tn.getMotivation() > 0)
						p.tn.antRequested.max += 1;
					else
						p.addToGarbage();
				}
			}
			else
				p.addToGarbage();
		}
	}
	
	public WOAObject getFirstTarget(Set<WOAObject> st)
	{
		for (WOAObject w : st)
			return w;
		return null;		
	}
	
	public void life()
	{		
		if ((lifestep % 15) == 0)
		{
			ArrayList<WOAObject> pheromones =  BodyUtils.lookFor(this, "pheromone", viewZone());
			if (pheromones != null)
			{
				for (WOAObject wo : pheromones)
				{
					smellPheromone((NodePheromone)wo);
					break;
				}						
			}
		}
		if (currentTrackMission == null)
		{				
			roam();	
			// search for food
			if ((lifestep % 10) == 0)
			{
				WOAObject target = searchForFood();
				if (target != null)
					currentTrackMission = new MissionTrack(currentBox.world, target);
			}
		}
		else
			currentTrackMission.whatToDo(this);
		
		processLifeStep();
	}

	public void setColony(Colony colony) {
		this.colony = colony;
	}

	public Colony getColony() {
		return colony;
	}
	
	public String toString()	{
		String s = "ant ID : " + id.toString() + ", ";
		if (currentMission != null)
			s += currentMission.toString();
		if (currentTrackMission != null)
			s += currentTrackMission.toString();
		return s;
	}

	public Player getPlayer()	{
		return getColony().getQueen().player;
	}
	
	@Override
	public float getWeight()	{
		return adn.getCurrent("weight"); 
	}
	
	@Override
	public float getSize()	{
		return adn.getCurrent("size");
	}		
}
