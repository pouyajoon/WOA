package vnd.track;

import java.util.HashMap;

import org.jbox2d.common.Vec2;

import vnd.woaobject.WOAObject;
import vnd.woaobject.children.Ant;
import vnd.woaobject.properties.Property;
import vnd.world.Position;

public class TrackNode {
	public WOAObject woao = null;
	public HashMap<WOAObject, TrackNode> toTargets = new HashMap<WOAObject, TrackNode>();
	public TrackNode toHome = null;
	public HashMap<WOAObject, Float> motivations = new HashMap<WOAObject, Float>();
	public HashMap<WOAObject, Property> antsRequested = new HashMap<WOAObject, Property>();
	public float motivation = 0;
	public GraphTrack gt = null;
	public Property antRequested = new Property("antRequested", 0);
	public HashMap<WOAObject, HashMap<Ant, Boolean>> antsTracking = new HashMap<WOAObject, HashMap<Ant, Boolean>>();
	public float antRequestedMax = 0;

	public TrackNode(WOAObject _woao, GraphTrack _gt) {
		woao = _woao;
		gt = _gt;
	}

	public Boolean stillRequestAnt(WOAObject target) {
		if (antsTracking.containsKey(target)) {
			float f = antsTracking.get(target).size();
			if (antRequestedMax - f > 0.0f)
				return true;
			else
				return false;
		} else {
			System.out.println("no more target on this node");
			woao.addToGarbage();
			return false;
		}
	}

	public void reduceAntRequested(WOAObject target, Ant _ant) {
		HashMap<Ant, Boolean> ants = antsTracking.get(target);
		if (ants != null)
			ants.remove(_ant);
		else {
			ants = new HashMap<Ant, Boolean>();
			antsTracking.put(target, ants);
		}

	}

	public void increaseAntRequested(WOAObject target, Ant _ant) {
		HashMap<Ant, Boolean> ants = antsTracking.get(target);
		if (ants != null)
			ants.put(_ant, true);
		else {
			ants = new HashMap<Ant, Boolean>();
			ants.put(_ant, true);
		}
		antsTracking.put(target, ants);
	}

	public void updateMotivation(WOAObject target, float motivation) {
		motivations.put(target, motivation);
		antRequestedMax = motivation / 20.0f;
	}

	public Position getBodyPosition() {
		if (woao != null)
			return new Position(woao.getPos()._map,
					woao.myBody.getPosition().x, woao.myBody.getPosition().y);
		else
			return null;
	}

	public float getMotivation() {
		float max = 0;
		for (Float m : motivations.values())
			max += m;
		return max;
	}

}
