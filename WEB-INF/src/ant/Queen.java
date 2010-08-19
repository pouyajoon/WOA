package ant;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import players.Player;
import woaobject.WOAObject;
import world.AntBox;
import world.Position;
import ant.properties.Property;

public class Queen extends WOAObject {

	public static final Integer ANT_PRICE = 15;
	
	private Colony colony;
	public Player player;
	
	/**
	 * @return the colony
	 */
	public Colony getColony() {
		return colony;
	}

	/**
	 * @param colony the colony to set
	 */
	public void setColony(Colony colony) {
		this.colony = colony;
	}

	/**
	 * @param c : the colony of the queen
	 */
	public Queen(AntBox w, Position p, Player _player) 
	{
		//super(c.world, new Position (c.getPos().get_map(), c.getPos().getX(), c.getPos().getY()));
		super(w, p);
		player = _player;
		player.setQueen(this);

		//currentBox.colonyCreate(this, p)
		currentBox.world.log.info("queen has born");		
		//super(c.world, new Position (c.getPos().get_map(), c.getPos().getX(), c.getPos().getY()));
		// TODO Auto-generated constructor stub
		this.type = "queen";

		adn.set("size", new Property("size", 5.0f));		
		adn.set("weight", new Property("weight", 0.0f));
		adn.set("storage_food", new Property("storage_food", 0));		
		adn.set("speed_max", new Property("speed_max", 0));
		adn.set("acceleration", new Property("acceleration", 0));	
		
		//adn.put("size", new Property("size", 2));
		Body b = currentBox.CreateJBoxBody(new Vec2(adn.getCurrent("size"), adn.getCurrent("size")), pos.getVec2(), adn.getCurrent("weight"), 0.5f);
		//Body b = currentBox.CreateJCircleBody(pos.getVec2(), adn.getCurrent("size"), adn.getCurrent("weight"), 0.0f);
		setMyBody(b);
		//setMyBody(c.world.antBox.create_body(adn.getCurrent("size"), adn.getCurrent("weight"), c.myBody.getPosition()));
		this.setColony(new Colony(this, p));
	}

	public void life()
	{
		//MoveTo(new Position(pos._map, 100.0f, 100.0f));
		//world.log.debug(id + " queen is living food : " + this.getColony().food_storage);
		if (this.getColony() != null)
		{
			if (this.getColony().adn.getCurrent("storage_food") > ANT_PRICE)
			{
				if (getColony().getAnts().size() > 800) return;
				this.getColony().antCreate();
				this.getColony().adn.reduce("storage_food", ANT_PRICE);
				//this.getColony().food_storage -= 50;
			}
		}
		else
		{
			System.out.println("no colony for this queen");
		}
		
	}
}
