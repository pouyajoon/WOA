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
	private 			Colony colony;
	public 				Player player;
	
	/**
	 * @param c : the colony of the queen
	 */
	public Queen(AntBox w, Position p, Player _player) 
	{
		super(w, p);
		player = _player;
		player.setQueen(this);
		currentBox.world.log.info("queen has born");		
		this.type = "queen";
		adn.set("size", new Property("size", 5.0f));		
		adn.set("weight", new Property("weight", 0.0f));
		adn.set("storage_food", new Property("storage_food", 0));		
		adn.set("speed_max", new Property("speed_max", 0));
		adn.set("acceleration", new Property("acceleration", 0));	
		Body b = currentBox.CreateJBoxBody(new Vec2(adn.getCurrent("size"), adn.getCurrent("size")), pos.getVec2(), adn.getCurrent("weight"), 0.5f);
		setMyBody(b);
		this.setColony(new Colony(this, p));
	}

	public void life()
	{
		if (this.getColony() != null)
		{
			if (this.getColony().adn.getCurrent("storage_food") > ANT_PRICE)
			{
				if (getColony().getAnts().size() > 800) return;
				this.getColony().antCreate();
				this.getColony().adn.reduce("storage_food", ANT_PRICE);
			}
		}
		else
			System.out.println("no colony for this queen");
	}
	
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
}
