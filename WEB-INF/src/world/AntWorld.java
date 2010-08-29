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
	public TestbedMain 									parent;
	public static ArrayList<Colony> 					colonys = new ArrayList<Colony>();
	public static HashMap<Integer, ArrayList<Colony>> 	colonys_screen = new HashMap<Integer, ArrayList<Colony>>();
	public WOAObjectListner 							woal = null;
	public ArrayList<NodePheromone> 					nodepheromones = new ArrayList<NodePheromone>();
	public ArrayList<GraphTrack> 						graphtracks = new ArrayList<GraphTrack>();	
	public HashMap<String, Player> 						players = new HashMap<String, Player>();
	// foods elements
	public ArrayList<Food> 								foods = new ArrayList<Food>();
	// objects inside garbage will be deleted next turn
	public HashMap<Integer, WOAObject> 					garbage = new HashMap<Integer, WOAObject>();
	public HashMap<Integer, WOAObject> 					hasViolateBoundary = new HashMap<Integer, WOAObject>();
	public Logger 										log;
	public Application 									appli;
    public MyTimer 										mytime;
    public AntZone 										antzone = null;

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
			mytime = new MyTimer(this);				
		}
		catch (Exception e)
		{
			log.debug("error creating world", e);
		}
		log.info("World has been created.");
   }

	
	public AntWorld(Logger _log, Application a, TestbedMain _phyworld)
	{		
		parent = _phyworld; 
		this.appli = a;
		this.log = _log;
		log.debug("Antworld Constructor done... appli: " + this.appli);
		InitWorld();
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
		}
	}
	// add a colony to a screen
	public void addColonys_screen(final Integer screen, final Colony c) 
	{	
		log.debug("Colony added to screen " + screen + ", size : " + colonys_screen.size());
		ArrayList<Colony> colonys = colonys_screen.get(screen);
		colonys.add(c);
	}

    public void addNodePheromone(NodePheromone p)
    {
  		nodepheromones.add(p);
    }

    public void removeNodePheromone(NodePheromone p)
    {
    	nodepheromones.remove(p);
    }    

	public void setColonys(final ArrayList<Colony> colonys) {
		AntWorld.colonys = colonys;
	}

	public ArrayList<Colony> getColonys() {
		return colonys;
	}

	public ArrayList<Colony> getColonys_screen(final Integer screen) {
		return colonys_screen.get(screen);
	}

}
