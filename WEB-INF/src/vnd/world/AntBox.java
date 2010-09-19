package vnd.world;

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

import vnd.woaobject.children.Colony;
import vnd.woaobject.children.Queen;


public class AntBox extends AbstractExample {
	public Logger log;
	public AntWorld world;
	public Integer id = 69;
	public ISharedObject so;

	public AntBox(AntWorld _world) {
		super(_world.parent);
		world = _world;
		log = _world.log;
		createWorld();
	}

	public void createWorld() {
		m_worldAABB = new AABB(new Vec2(0f, 0f), new Vec2(AntZone.box_size_x,
				AntZone.box_size_y));
		Vec2 gravity = new Vec2(0.0f, 0.0f);
		boolean doSleep = true;
		m_world = new World(m_worldAABB, gravity, doSleep);
	}

	@Override
	public void create() {
	}

	public void drawEarth() {
		int translate_x = id % parent.antworld.antzone.zone_size;
		int translate_y = id / parent.antworld.antzone.zone_size;
		translate_x *= AntZone.box_size_x;
		translate_y *= AntZone.box_size_y;
		for (int i = 0; i < parent.antworld.antzone.max_screens; ++i) {
			if (i == id)
				continue;
			drawRectangle(new Vec2(0 + (i % parent.antworld.antzone.zone_size)
					* (AntZone.box_size_x) - translate_x,
					(i / parent.antworld.antzone.zone_size)
							* -(AntZone.box_size_y) + translate_y), new Vec2(
					AntZone.box_size_x, AntZone.box_size_y));
		}
	}

	/**
	 * create a new colony
	 * 
	 * @param pos
	 * @return the created colony
	 */
	public Colony colonyCreate(Queen q, Position pos) {
		log.debug("Going to create a Colony");
		Colony c = null;
		try {
			c = new Colony(q, pos);
		} catch (Exception e) {
			log.debug("error creating colony", e);
		}
		return c;
	}

	public Body CreateBodyDefStatic(Vec2 position) {
		BodyDef bd = new BodyDef();
		bd.position = position;
		bd.angle = 0;
		Body body = m_world.createBody(bd);
		return body;
	}

	public Body CreateBodyDefDynamic(Vec2 position) {
		BodyDef bd = new BodyDef();
		bd.position = position;
		bd.angle = 0;
		Body body = m_world.createBody(bd);
		return body;
	}

	public ShapeDef CreateBoxShape(Vec2 size, float density, float friction) {
		PolygonDef sd = new PolygonDef();
		sd.setAsBox(size.x, size.y);
		sd.density = density;
		sd.friction = friction;
		return sd;
	}

	public ShapeDef CreateCircleShape(float radius, float restitution,
			float density, float friction) {
		CircleDef sd = new CircleDef();
		sd.radius = radius;
		sd.density = density;
		sd.friction = friction;
		sd.restitution = restitution;
		return sd;
	}

	public Body CreateJBoxBody(Vec2 size, Vec2 position, float density,
			float friction) {
		Body b = CreateBodyDefStatic(position);
		b.createShape(CreateBoxShape(size, density, friction));
		b.setMassFromShapes();
		return b;
	}

	public Body CreateJCircleBody(Vec2 position, float radius, float density,
			float friction) {

		try {
			Body b = CreateBodyDefDynamic(position);
			ShapeDef sd = CreateCircleShape(radius, 0.0f, density, friction);
			b.createShape(sd);
			b.setMassFromShapes();
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void create_borders_inside() {
		float padding = 10.0f;
		float size = 2.0f;

		Vec2 size_width = new Vec2((AntZone.box_size_x / 2) - (padding * 2),
				size);
		Vec2 size_height = new Vec2(size, (AntZone.box_size_y / 2)
				- (padding * 2) - (size * 2));
		System.out.println("border width : " + size_width.toString());
		// bottom
		CreateJBoxBody(size_width, new Vec2(padding * 2 + size_width.x, size
				+ padding * 2), 0, 0);
		// top
		CreateJBoxBody(size_width, new Vec2(padding * 2 + size_width.x,
				AntZone.box_size_y - (size + padding * 2)), 0, 0);
		// left
		CreateJBoxBody(size_height, new Vec2(size + padding * 2, size_height.y
				+ size * 2 + padding * 2), 0, 0);
		// right
		CreateJBoxBody(size_height, new Vec2(AntZone.box_size_x
				- (size + padding * 2), size_height.y + (size * 2) + padding
				* 2), 0, 0);
	}

	public Body CreateJCircleBodyStatic(Vec2 position, float radius,
			float density, float friction) {
		try {
			Body b = CreateBodyDefStatic(position);
			ShapeDef sd = CreateCircleShape(radius, 0.0f, density, friction);
			b.createShape(sd);
			b.setMassFromShapes();
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public void destroyBody(Body b) {
		m_world.destroyBody(b);
	}

	public Joint createJoint(JointDef jd) {
		return m_world.createJoint(jd);
	}

	public void destroyJoint(Joint j) {
		m_world.destroyJoint(j);
	}

	@Override
	public String getName() {
		return "WOA : Box Number " + id;
	}

	public int getBoundarySide(Vec2 pos) {
		if (pos.x < 0)
			return AntZone.DIRECTION_WEST;
		if (pos.x > AntZone.box_size_x)
			return AntZone.DIRECTION_EAST;
		if (pos.y < 0)
			return AntZone.DIRECTION_SOUTH;
		if (pos.y > AntZone.box_size_y)
			return AntZone.DIRECTION_NORTH;
		return 0;
	}
}
