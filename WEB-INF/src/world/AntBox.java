package world;



import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.collision.shapes.ShapeDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.testbed.AbstractExample;
import org.red5.server.api.so.ISharedObject;
import org.slf4j.Logger;

import ant.Colony;
import ant.Queen;


//extends AbstractExample
public class AntBox extends AbstractExample {

	public ISharedObject so;
  	public Logger log;
  	public AntWorld world;
  	public Integer id = 69;
  	

	
	public AntBox(AntWorld _world) {
		super(_world.parent);
		world = _world;
		log = _world.log;
		
		createWorld();
		//needsReset = true;
//		initialize();
//		needsReset = false;
	}

	public void createWorld() {
		m_worldAABB = new AABB(new Vec2(0f, 0f), new Vec2(AntZone.box_size_x, AntZone.box_size_y));
		//m_worldAABB.lowerBound = new Vec2(-200.0f, -100.0f);
		//m_worldAABB.upperBound = new Vec2(200.0f, 200.0f);
		//m_worldAABB.lowerBound  = new Vec2(0f, 0f);
		//m_worldAABB.upperBound = new Vec2(AntZone.box_size_x, AntZone.box_size_y);

		Vec2 gravity = new Vec2(0.0f, 0.0f);
		boolean doSleep = true;
		m_world = new World(m_worldAABB, gravity, doSleep);
	}    
    	
	
	public int getBoundarySide(Vec2 pos)
	{
		if (pos.x < 0)
		{
			return AntZone.DIRECTION_WEST;
		}
		if (pos.x > AntZone.box_size_x)
		{
			return AntZone.DIRECTION_EAST;
		}
		if (pos.y < 0)
		{
			return AntZone.DIRECTION_SOUTH;
		}
		if (pos.y > AntZone.box_size_y)
		{
			return AntZone.DIRECTION_NORTH;
		}
		return 0;
	}
	
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		//System.out.println("Ant Box Created!");

		/*
   		Player pouya = new Player("pouya");
		//players.put("pouya", pouya);
		Queen q = new Queen(this, new Position(0, 150.0f, 150.0f), pouya);
		*/
		
		//log.debug("borders are set around the world");
		//create_borders_inside();		
		//InitWorld();		
				
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "WOA : Box Number " + id;
	}

	public void drawEarth()
	{ 
		int translate_x = id % parent.antworld.antzone.zone_size; 
		int translate_y = id / parent.antworld.antzone.zone_size;
		translate_x *= AntZone.box_size_x;
		translate_y *= AntZone.box_size_y;
		
		for (int i = 0; i < parent.antworld.antzone.max_screens; ++i)
		{
			if (i == id) continue;
			drawRectangle(new Vec2(0 + (i % parent.antworld.antzone.zone_size)  * (AntZone.box_size_x) - translate_x, (i / parent.antworld.antzone.zone_size) * - (AntZone.box_size_y) + translate_y), new Vec2(AntZone.box_size_x, AntZone.box_size_y));
		}
	}
	
	/**
	 * create a new colony
	 * @param pos
	 * @return the created colony
	 */
	public Colony colonyCreate(Queen q, Position pos)
	{
		log.debug("Going to create a Colony");
		Colony c = null;
		try 
		{
			c = new Colony(q, pos);
		} 
		catch (Exception e)
		{
			log.debug("error creating colony", e);
		}
		return c;	
	}	
	
	
	   public Body CreateBodyDefStatic(Vec2 position)
	    {
	    	BodyDef bd = new BodyDef();
			bd.position = position;
			bd.angle = 0;
			Body body = m_world.createBody(bd);
			//m_world.createStaticBody(bd);
			return body;    	
	    }    
	    
	    public Body CreateBodyDefDynamic(Vec2 position)
	    {
	    	BodyDef bd = new BodyDef();
			bd.position = position;
			bd.angle = 0;
			//Body body = m_world.createDynamicBody(bd);
			Body body = m_world.createBody(bd);
			return body;    	
	    }          
	    
	    public ShapeDef CreateBoxShape(Vec2 size, float density, float friction)
	    {
	    	PolygonDef sd = new PolygonDef();
	        sd.setAsBox(size.x, size.y);
	        sd.density = density;            
	        sd.friction = friction;
	        return sd;
	    }
	    
	    public ShapeDef CreateCircleShape(float radius, float restitution, float density, float friction)
	    {
	    	CircleDef sd = new CircleDef();
	        sd.radius = radius;        
	        sd.density = density;         
	        sd.friction = friction;
	        sd.restitution = restitution;
	        return sd;
	    }
	    
	    public Body CreateJBoxBody(Vec2 size, Vec2 position, float density, float friction)
		{
	    	Body b = CreateBodyDefStatic(position);
	        b.createShape(CreateBoxShape(size, density, friction));
	        b.setMassFromShapes();   		
			return b;
		}    
	    
	    public Body CreateJCircleBody(Vec2 position, float radius, float density, float friction)
		{
	    	
	    	try 
	    	{
	    		Body b = CreateBodyDefDynamic(position);
	    		/*
		    	BodyDef bd = new BodyDef();
				bd.position = position;
				bd.angle = 0;
				Body b = m_world.createDynamicBody(bd);    	
		    	*/
		    	ShapeDef sd = CreateCircleShape(radius, 0.0f, density, friction);
		    	/*
		        CircleDef sd = new CircleDef();
		        sd.radius = radius;
		        sd.density = density;
		        sd.restitution = 0.0f;
		        */
		    	
		    	b.createShape(sd);
		    	b.setMassFromShapes();   
		        //log.debug("an body created");
		        
		        //System.out.println("an body created");
		        return b;
	    	}
	        catch (Exception e)
	        {
	    		e.printStackTrace();
	    		return null;
	        }
			
		}    
	    
	    public void create_borders_inside()
	    {
	    	float padding =  10.0f;
	    	float size =  2.0f;
	    	
	    	Vec2 size_width = new Vec2((AntZone.box_size_x / 2) - (padding * 2), size);
	    	Vec2 size_height = new Vec2(size, (AntZone.box_size_y / 2) - (padding * 2) - (size * 2));
	    	//Vec2 size_height = new Vec2(10, box_size_y);
	    	System.out.println("border width : " + size_width.toString());
	    	//bottom
	    	//create_box(size_width, new Vec2(padding * 2 + size_width.x, size + padding * 2));
	    	CreateJBoxBody(size_width, new Vec2(padding * 2 + size_width.x, size + padding * 2), 0, 0);
	    	//top
	    	//create_box(size_width, new Vec2(padding * 2 + size_width.x, box_size_y - (size + padding * 2)));
	    	CreateJBoxBody(size_width, new Vec2(padding * 2 + size_width.x, AntZone.box_size_y - (size + padding * 2)), 0, 0);
	    	//left
	    	//create_box(size_height, new Vec2(size + padding * 2, size_height.y + size * 2 + padding * 2 + 1));
	    	CreateJBoxBody(size_height, new Vec2(size + padding * 2, size_height.y + size * 2 + padding * 2), 0, 0);
	    	//right
	    	//create_box(size_height, new Vec2(box_size_x - (size + padding * 2), size_height.y + (size * 2) + padding * 2 + 1 ));
	    	CreateJBoxBody(size_height, new Vec2(AntZone.box_size_x - (size + padding * 2), size_height.y + (size * 2) + padding * 2), 0, 0);
	    	//create_box(size_width, new Vec2(padding + ((box_size_x  - (padding * 2)) / 2), padding));
	        //antWorld.log.debug("body has been set");
	    }	  	    

	    public Body CreateJCircleBodyStatic(Vec2 position, float radius, float density, float friction)
		{    	
	    	try 
	    	{
	    		Body b = CreateBodyDefStatic(position);
	    		ShapeDef sd = CreateCircleShape(radius, 0.0f, density, friction);
		    	b.createShape(sd);
		    	b.setMassFromShapes();
		        return b;
	    	}
	        catch (Exception e)
	        {
	    		e.printStackTrace();
	    		return null;
	        }
			
		}   
	    
		public void destroyBody(Body b)
		{
			m_world.destroyBody(b);
//			//myBody = null;
		}	    
	    
		public Joint createJoint(JointDef jd)
		{
			return m_world.createJoint(jd);
		}

		public void destroyJoint(Joint j)
		{
			m_world.destroyJoint(j);
		}	    
	    
}
