package world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jbox2d.testbed.TestbedMain;
import org.slf4j.Logger;

import players.Player;
import woaobject.WOAObject;
import woaobject.WOAObjectListner;
import woaobject.pheromone.NodePheromone;
import woaobject.track.GraphTrack;
import ant.Colony;

import com.woa.Application;
import com.woa.MyTimer;

import food.Food;


public class AntWorld {
    
	/** The controller that the AbstractExample runs in */
	public TestbedMain parent;
	
	public static ArrayList<Colony> colonys = new ArrayList<Colony>();
	public static HashMap<Integer, ArrayList<Colony>> colonys_screen = new HashMap<Integer, ArrayList<Colony>>();
	//public ArrayList<DrawObject> action_fifo = new ArrayList<DrawObject>();
	public WOAObjectListner woal = null;
	
	//public ArrayList<Pheromone> pheromones = new ArrayList<Pheromone>();
	public ArrayList<NodePheromone> nodepheromones = new ArrayList<NodePheromone>();
	public ArrayList<GraphTrack> graphtracks = new ArrayList<GraphTrack>();	
	
	public HashMap<String, Player> players = new HashMap<String, Player>();
	
	// foods elements
	public ArrayList<Food> foods = new ArrayList<Food>();
	
	// objects inside garbage will be deleted next turn
	public HashMap<Integer, WOAObject> garbage = new HashMap<Integer, WOAObject>();
	public HashMap<Integer, WOAObject> hasViolateBoundary = new HashMap<Integer, WOAObject>();
	
	public Logger log;
	//public AntBox antBox;
	public Application appli;
    public MyTimer mytime;
    //public AntBox ab = null;
    
    
    public AntZone antzone = null;


    public void InitWorld()
    {    	
		woal = new WOAObjectListner(this, this.appli);
		log.debug("World is going to be set.");
		try
		{
			antzone = new AntZone(this, 4);
			
			for (Integer i = 0; i < antzone.max_screens; ++i)
			{
				colonys_screen.put(i, new ArrayList<Colony>());
				woal.all_objects_screen.put(i, new ArrayList<WOAObject>());
			}			
			//SetupScreens();
			mytime = new MyTimer(this);				
			
			//ab = new AntBox(parent, this, log);

			/*
	   		Player pouya = new Player("pouya");
			players.put("pouya", pouya);
			Queen q = new Queen(ab, new Position(0, 150.0f, 150.0f), pouya);
			*/
			
			//q.getColony().antCreate();
			
			/*
			Player jannou = new Player("jannou");
			players.put("jannou", jannou);			
			
			new Queen(this, new Position(0, 400.0f, 400.0f), jannou);
			*/
			//Colony c = colonyCreate(q, );
			//c.antCreate();
			
						
		}
		catch (Exception e) {
			log.debug("error creating world", e);
		}
			
		log.info("World has been created.");
		
		
		//log.debug("borders are set around the world");		
    	
    	//Body b = CreateJBoxBody(new Vec2(200, 200), new Vec2(200, 200), 0.5f, 0.5f);
		//Food food = new Food(ab, new Position(0, 500, 500), 80);		
		//foods.add(food);
		//Food food2 = new Food(this, new Position(0, 600, 300));		
		//foods.add(food2);
   }

	
	public AntWorld(Logger _log, Application a, TestbedMain _phyworld)
	{		
		parent = _phyworld; 
		//antBox = new AntBox(this);
		//antBox.create_world();
		//antBox.create_body();
		//System.out.println("Ant World Welcome !");
		this.appli = a;
		this.log = _log;

		log.debug("Antworld Constructor done... appli: " + this.appli);
		
		InitWorld();
		/*
		woal = new WOAObjectListner(this, this.appli);
		log.debug("World is going to be set.");
		try
		{
			SetupScreens(1);
			mytime = new MyTimer(this);	
			
			new Queen(this, new Position(0, 150.0f, 150.0f));
			//Colony c = colonyCreate(q, );
			//c.antCreate();
			
			woal.woaobject_screen_share();					
		}
		catch (Exception e) {
			log.debug("error creating world", e);
		}
		*/
		/*
		demo = new AntPhysics();
		demo.start();
		
		Body faller = new StaticBody("FallerBox", new Box(40, 40));
		faller.setPosition(150, 150);
		demo.getWorld().add(faller);
		*/				
		//log.info("World has been created.");
		
		//antBox.create_borders_inside();
		
		//
		
		//Food food = new Food(this, new Position(0, 300, 300));
		//Food food2 = new Food(this, new Position(0, 300, 320));
	}
	

	// each step colonys lives
	// for each ant of the colony the ants lives
	public void lifeColonys(final ArrayList<Colony> colonys)
	{
		final Iterator<Colony> i = colonys.iterator();
		while (i.hasNext())
		{
			final Colony c = i.next();
			c.life();
			//if (mytime.worldtime % 10 == 0)
			{
				//c.antCreate();
			}
		}
	}



	
	public void setColonys(final ArrayList<Colony> colonys) {
		AntWorld.colonys = colonys;
	}

	public ArrayList<Colony> getColonys() {
		return colonys;
	}

	// add a colony to a screen
	public void addColonys_screen(final Integer screen, final Colony c) 
	{	
		log.debug("Colony added to screen " + screen + ", size : " + colonys_screen.size());
		ArrayList<Colony> colonys = colonys_screen.get(screen);
		colonys.add(c);
		
		//if (colonys_screen.size() == 0 || colonys_screen.get(screen) == null)
		{
		//	colonys_screen.set(screen, new ArrayList<Colony>());
		//	colonys_screen.get(screen).add(c);
		}
		//else
		{
			
		}	
	}

	public ArrayList<Colony> getColonys_screen(final Integer screen) {
		return colonys_screen.get(screen);
	}

    public void addNodePheromone(NodePheromone p)
    {
  		nodepheromones.add(p);
    }

    public void removeNodePheromone(NodePheromone p)
    {
    	nodepheromones.remove(p);
    }    

}
