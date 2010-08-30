package woaobject;

import java.util.HashMap;

import org.jbox2d.collision.Distance;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import players.Player;
import woaobject.track.TrackNode;
import world.AntBox;
import world.Position;
import ant.Ant;
import ant.properties.ADN;
import ant.properties.Property;

import com.unicate.DrawObject;

public class WOAObject {
	public Integer id = 0;
	public Position pos = null;
	public Body myBody = null;
	public float direction = 0;
	public AntBox currentBox = null;
	public String type = null;
	public ADN adn = null;
	public Integer lifestep = 0;
	public Integer lifestepmax = 60;
	public HashMap<Integer, WOAObject> catchedBy = new HashMap<Integer, WOAObject>();
	public HashMap<Player, TrackNode> tracknodes = new HashMap<Player, TrackNode>();

	/**
	 * @param w
	 *            : the Antworld attached to the object
	 * @param pos
	 *            : object's position
	 * 
	 */
	// public WOAObject(AntWorld w, Position _pos) {
	public WOAObject(AntBox _antBox, Position _pos) {
		super();
		currentBox = _antBox;
		adn = new ADN();
		setPos(_pos);
		this.id = currentBox.world.woal.get_new_id();
		currentBox.world.woal.add_o(this);
		System.out.println("woa object created on map : " + getPos().get_map());
	}

	public void processLifeStep() {
		lifestep++;
		if (lifestep > lifestepmax)
			lifestep = 1;
	}

	public void createBody(AntBox _targetBox) {
	}

	public void destroyBody() {
		currentBox.destroyBody(myBody);
		myBody = null;
	}

	/**
	 * Add the WoaObject to the garbage, will be destroyed later
	 */
	public void addToGarbage() {
		currentBox.world.garbage.put(this.id, this);
	}

	/**
	 * the time to live..., do your custom actions should be override
	 * 
	 * @throws Throwable
	 */
	public void life() throws Throwable {

	}

	public void collisionPersists(WOAObject other) {

	}

	/**
	 * @param other
	 *            : the WoaObject with who the collision was done
	 */
	public void collide(WOAObject other, ContactPoint cp) {
	}

	/**
	 * Catch an object by creating a physical joint
	 * 
	 * @param other
	 *            : the WoaObject to catch
	 */
	// RevoluteJointDef
	// PrismaticJointDef
	// DistanceJointDef
	public RevoluteJointDef jointInit(WOAObject other, Vec2 anchor) {
		RevoluteJointDef jd = new RevoluteJointDef();
		jd.collideConnected = true;
		jd.enableMotor = false;
		jd.initialize(this.myBody, other.myBody, anchor);
		return jd;
	}

	public void jointRelease(Joint j) {
		// if the joint exists destroy it
		if (j != null)
			currentBox.destroyJoint(j);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString() write the object in string with
	 * attributes
	 */
	public String toString() {
		String str = "object id=" + id + ", type=" + type;
		for (Property p : adn.properties.values())
			str += p.toString() + ", ";
		return str;
	}

	/**
	 * @param position
	 *            to move
	 * 
	 */
	public void move(Position to) {
		this.setPos(to);
		this.myBody.setXForm(to.getVec2(), 0);
	}

	/**
	 * update the WoaObject position with physical object position ... should be
	 * reviewd
	 */
	public void UpdateBodyPosition() {
		pos.x = myBody.getPosition().x;
		pos.y = myBody.getPosition().y;
	}

	/**
	 * Move the object using the force applied
	 * 
	 * @param force
	 *            : apply the force to the object
	 */
	public void moveTo(Vec2 new_pos) {
		// update the woaoject position from physics
		UpdateBodyPosition();
	}

	/**
	 * Should be override custom destroy, is called at the end of
	 * {@link #finalize()}
	 */
	public void localfinalize() {

	}

	public void boundaryViolated() {
		currentBox.destroyBody(myBody);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize() Destroy the object then call
	 * localfinalize for customs
	 */
	public void finalize() throws Throwable {
		for (WOAObject woaobject_catchedby : catchedBy.values()) {
			if (woaobject_catchedby.type.equals("ant")) {
				Ant ant_catechedby = (Ant) woaobject_catchedby;
				ant_catechedby.releaseMandible();
			}
		}
		currentBox.world.woal.remove_o(id);
		if (myBody != null)
			currentBox.destroyBody(myBody);
		localfinalize();
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	public float getWeight() {
		return 10.0f;
	}

	public float getSize() {
		return adn.getCurrent("size");
	}

	public float getDistance(WOAObject other) {
		Vec2 x1 = new Vec2();
		Vec2 x2 = new Vec2();

		Distance d = new Distance();
		float distance = d.distance(x1, x2, this.getMyBody().getShapeList(),
				this.getMyBody().getXForm(), other.getMyBody().getShapeList(),
				other.getMyBody().getXForm());
		return distance;
	}

	/**
	 * @return the physical body element
	 */
	public Body getMyBody() {
		return myBody;
	}

	/**
	 * @param myBody
	 *            : set the physical body
	 */
	public void setMyBody(Body myBody) {

		this.myBody = myBody;
		if (myBody != null) {
			myBody.m_userData = this;
			// update woa object position with body position
			UpdateBodyPosition();
		}
	}

	/**
	 * @param pos
	 *            : set the pos
	 */
	public void setPos(Position pos) {
		this.pos = pos;
	}

	/**
	 * @return the drawObject of the WOAObject
	 */
	public DrawObject getDrawObject() {
		// Position p = getPos();
		Position p = new Position(getPos()._map, myBody.getPosition().x,
				myBody.getPosition().y);
		DrawObject dobj = new DrawObject(this.id, p, this.type);
		dobj.size = getSize();
		dobj.direction = this.getMyBody().getAngle(); // direction;
		return dobj;
	}

	public void setBodyAngle(Float angle_r) {
		if (myBody != null) {
			if (!angle_r.isNaN())
				myBody.m_sweep.a = angle_r;
		}
	}

	/**
	 * @return the position
	 */
	public Position getPos() {
		float x = 0.0f;
		float y = 0.0f;
		if (myBody != null && myBody.getPosition() != null) {
			x = myBody.getPosition().x;
			y = myBody.getPosition().y;
		}
		x = pos.x;
		y = pos.y;
		return new Position(pos._map, x, y);
	}
}
