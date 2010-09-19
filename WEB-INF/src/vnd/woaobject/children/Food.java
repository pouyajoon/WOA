package vnd.woaobject.children;

//import org.jbox2d.collision.CircleShape;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.testbed.BodyUtils;

import vnd.woaobject.WOAObject;
import vnd.woaobject.properties.Property;
import vnd.world.AntBox;
import vnd.world.Position;

public class Food extends WOAObject {
	public Food(AntBox w, Position pos, float size) {
		super(w, pos);
		type = "food";
		adn.set("storage_food", new Property("storage_food", size));
		// adn.set("size", new Property("size", 10.0f));
		// adn.getCurrent("storage_food") / 10.0f)
		// adn.set("storage_food", new Property("storage_food", 100.0f));
		// adn.set("size", new Property("size", 50.f));

		// adn.getCurrent("size");
		Body b = currentBox.CreateJCircleBody(pos.getVec2(), getSize(),
				getWeight(), 0.0f);
		// b.setUserData(this);
		setMyBody(b);
		// TODO Auto-generated constructor stub
	}

	public float getWeight() {
		return adn.getCurrent("storage_food") * 1000.0f;
	}

	@Override
	public float getSize() {
		float size = adn.getCurrent("storage_food") / 10.0f;
		// adn.updateCurrent("size", size);
		return size;
	}

	public void life() {
		// System.out.println("it's my life");

		if (myBody != null) {
			myBody.m_torque = 0;
			CircleShape c = (CircleShape) myBody.getShapeList();
			if (c.getRadius() != getSize()) {
				// System.out.println("need resize to : " +
				// adn.getCurrent("storage_food") + ", current radius : " +
				// c.getRadius());
				resize();
			}
			if (adn.getCurrent("storage_food") == 0.0f) {
				addToGarbage();
			}
		} else {
			// System.out.println("body is null on food resizing");
		}
	}

	public void localfinalize() {
		currentBox.world.foods.remove(this);
		if (tracknodes.size() > 0) {
			// for (TrackNode tn : tracknodes.values())
			{
				// if (tn != null && tn.toHome != null && tn.toHome.toTargets !=
				// null)
				{
					// TrackNode tnHome =
					// (TrackNode)tn.toHome.toTargets.get(this);
				}

				// tnHome.motivation = 0;
				// tn.toHome.toTargets.remove(this);
				// tn.toTargets.get(this)
			}
		}
	}

	public void resize() {
		BodyUtils.resizeCircle(this, getSize(), getWeight());
		// adn.updateCurrent("size", 10);
		/*
		 * CircleShape circle = (CircleShape)myBody.getShapeList();
		 * //myBody.getShapeList().m_sweepRadius =
		 * adn.getCurrent("storage_food");
		 * 
		 * float r = circle.getRadius(); float d = circle.m_density; float f =
		 * circle.m_friction; float rest = circle.m_restitution;
		 * 
		 * myBody.destroyShape(circle); //myBody.destroyShape(circle); //
		 * creating a new circle shape
		 * 
		 * CircleDef newCircleDef = new CircleDef(); // calculating new radius
		 * newCircleDef.radius = getSize();//adn.getCurrent("storage_food");
		 * newCircleDef.density = getWeight(); newCircleDef.friction = f;
		 * newCircleDef.restitution = rest;
		 * 
		 * myBody.createShape(newCircleDef); myBody.setMassFromShapes();
		 */
		if (adn.getCurrent("storage_food") <= 0) {
			// System.out.println("add this to garbage : " + this.toString());
			// world.destroyBody(this.myBody);
			addToGarbage();
		}
	}

	public void collide(WOAObject other) {
		if (other.type.equals("ant")) {
			// System.out.println("stop eating me : " +
			// adn.getCurrent("storage_food") + "@body :" +
			// myBody.getPosition().toString());
		}
	}

}
