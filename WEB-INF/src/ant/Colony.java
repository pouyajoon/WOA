package ant;

import java.util.ArrayList;
import java.util.HashMap;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.red5.io.amf3.IDataOutput;

import woaobject.WOAObject;
import world.AntBox;
import world.Position;
import ant.properties.Property;

import com.unicate.info.ColonyInfo;
import com.woa.missions.Mission;

public class Colony extends WOAObject {
	private ArrayList<Ant> ants = new ArrayList<Ant>();;
	public Queen queen = null;
	public HashMap<Integer, Mission> missions = new HashMap<Integer, Mission>();

	public Colony(Queen q, Position _pos) {
		super(q.currentBox, _pos);
		setQueen(q);
		this.setPos(pos.clone());
		currentBox.world.getColonys().add(this);
		//currentBox.world.addColonys_screen(pos.get_map(), this);
		type = "colony";
		adn.set("size", new Property("size", 10.0f));
		adn.set("storage_food", new Property("storage_food", 3500.0f));
		adn.updateCurrent("storage_food", 30);
		// add the colony shape to the physic world
		// setMyBody(world.antBox.create_colony(pos.getVec2()));
		// world.
		createBody(currentBox);
		currentBox.world.log.debug("Colony has been created.");
		this.antCreate();
	}

	@Override
	public void createBody(AntBox _targetBox) {
		Body b = _targetBox.CreateJBoxBody(new Vec2(getSize(), getSize()),
				pos.getVec2(), 0.0f, 1.0f);
		setMyBody(b);
	}

	public void life() {
		try {
			destroyBody();
			createBody(currentBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public IDataOutput Export(IDataOutput output) {
		output.writeObject(getPos());
		return output;
	}

	public Ant antCreate() {
		// create the ant
		Ant a = new Ant(this);
		// add the ant to the ants list of the colony
		getAnts().add(a);
		return a;
	}

	public ColonyInfo getColonyInfo() {
		return new ColonyInfo(ants.size(), adn.properties.get("storage_food"));
	}

	public float getSize() {
		return ants.size() / 10 + 20;
	}

	/**
	 * @return colony's queen
	 */
	public Queen getQueen() {
		return queen;
	}

	/**
	 * @param set
	 *            the colony's queen
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

}
