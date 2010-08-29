package ant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.testbed.BodyUtils;
import org.red5.io.amf3.IDataOutput;

import woaobject.WOAObject;
import world.AntBox;
import world.Position;
import ant.properties.Property;

import com.unicate.info.ColonyInfo;
import com.woa.missions.Mission;


/**
 * @author PMOHTACH
 *
 */
public class Colony extends WOAObject{
	private ArrayList<Ant> ants = new ArrayList<Ant>();;
	public Queen queen = null;
	public HashMap<Integer, Mission> missions = new HashMap<Integer, Mission>();
	
	//float food_storage = 1510.0f;


	public Colony(Queen q, Position _pos)
	{	
		super(q.currentBox, _pos);
		//this.currentBox = q.cu;
		setQueen(q);		
		//Position p = new Position (_pos.getPos().get_map(), _pos.getPos().getX() + 200.0f, _pos.getPos().getY());
		this.setPos(pos.clone());
		//this.setPos(p);
		//ArrayList<Colony> c = this.;
		//c.add(this);		
		currentBox.world.getColonys().add(this);
		currentBox.world.addColonys_screen(pos.get_map(), this);
		
		type = "colony";		
		adn.set("size", new Property("size", 10.0f));
		adn.set("storage_food", new Property("storage_food", 3500.0f));
		adn.updateCurrent("storage_food", 30);
		
		// add the colony shape to the physic world
		//setMyBody(world.antBox.create_colony(pos.getVec2()));
		//world.
		
		createBody(currentBox);
		//myBody.m_shapeList.m_categoryBits = 0x0002;
		//myBody.m_shapeList.m_maskBits = 0x0002;	
		
		//UpdateBodyPosition();
		currentBox.world.log.debug("Colony has been created.");		

		
		//queen.getColony().antCreate();
		this.antCreate();
		//this.antCreate();
		//this.antCreate();
		//this.antCreate();
		//this.antCreate();		
	}	
	
	@Override
	public void createBody(AntBox _targetBox)
	{
		Body b = _targetBox.CreateJBoxBody(new Vec2 (getSize() 	, getSize()), pos.getVec2(), 0.0f, 1.0f);
		setMyBody(b);

	}
	
	public ColonyInfo getColonyInfo()
	{		
		return new ColonyInfo(ants.size(), adn.properties.get("storage_food"));
	}
	/*
	// not used
	public void subscribeMission(Mission m)
	{
		if (!missions.containsKey(m.target.id))
		{
			missions.put(m.target.id, m);
		}
	}
	*/
/*
	public Colony(AntWorld w, Position pos)
	{
		super(w, pos);
		// TODO Auto-generated constructor stub
	}
*/

	public float getSize()
	{
		return ants.size() / 10 + 20;
	}
	
	/**
	 * @return colony's queen
	 */
	public Queen getQueen() {
		return queen;
	}

	
	/**
	 * @param set the colony's queen
	 */
	public void setQueen(Queen queen) {
		this.queen = queen;
	}

	public ArrayList<Ant> getAnts() {
		return ants;
	}

	public void setAnts(ArrayList<Ant> ants) {
		this.ants = ants;
	}
	
	public void life()
	{
		try
		{
			// queen life
			//if (getQueen() != null)	getQueen().life();
			
			// iterate on ants & execute life on each one
//			Iterator<Ant> i = getAnts().iterator();
//			while (i.hasNext())
//			{
//				Ant a = i.next();
//				a.life();
//			}
//			//if ((Math.random() * 100) > 69)
//			{
//				//antCreate();	
//			}
			//world.log.debug(id + " size : " + adn.getCurrent("size") );
			//world.log.debug(id + " pos : " + pos.x + "," + pos.y + ", size : "  +  adn.getCurrent("size"));
			destroyBody();
			createBody(currentBox);
		}
		//PolygonShape ps = (PolygonShape)myBody.getShapeList();
		//if (getSize() != ps.get)
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public IDataOutput Export(IDataOutput output)
	{
		output.writeObject(getPos());
		return output;
	}

	
	public Ant antCreate()
	{
		// create the ant
		Ant a = new Ant(this);
		
		// add the ant to the ants list of the colony
		getAnts().add(a);
		
		return a;
	}


}
