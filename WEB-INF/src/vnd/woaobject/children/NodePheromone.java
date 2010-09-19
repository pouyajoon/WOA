package vnd.woaobject.children;

import org.jbox2d.dynamics.Body;
import org.jbox2d.testbed.BodyUtils;

import vnd.track.GraphTrack;
import vnd.track.TrackNode;
import vnd.woaobject.WOAObject;
import vnd.woaobject.properties.Property;
import vnd.world.AntBox;
import vnd.world.Position;

public class NodePheromone extends WOAObject {
	public GraphTrack track = null;
	public TrackNode tn = null;
	private float oldSize = 0;

	public NodePheromone(AntBox w, Position _pos, GraphTrack _t, TrackNode _tn) {
		super(w, _pos);
		type = "pheromone";
		track = _t;
		tn = _tn;
		adn.set("size", new Property("size", 20.0f));
		adn.set("weight", new Property("weight", 0.0f));
		oldSize = 0;
		createBody(currentBox);
		currentBox.world.addNodePheromone(this);
		System.out.println("new node pheromone : is static ?"
				+ myBody.isStatic());
	}

	@Override
	public void createBody(AntBox _targetBox) {
		Body b = currentBox.CreateJCircleBodyStatic(pos.getVec2(),
				adn.getCurrent("size"), 0.0f, 0.0f);
		setMyBody(b);
		setBodyAngle(new Float(0));
		if (myBody != null) {
			myBody.m_shapeList.getFilterData().categoryBits = 0x0002;
			myBody.m_shapeList.getFilterData().maskBits = 0x0004;
		}
		currentBox = _targetBox;
	}

	@Override
	public void life() {
		float size = getSize();
		if (oldSize != size) {
			BodyUtils.resizeCircle(this, getSize(), getWeight());
			oldSize = size;
		}
		if (tn.toHome == null) {
			if (tn.toTargets.size() == 0)
				addToGarbage();
		}
	}

	@Override
	public float getSize() {
		return tn.getMotivation() / 5.0f;
	}

	@Override
	public float getWeight() {
		return 0f;
	}
}
