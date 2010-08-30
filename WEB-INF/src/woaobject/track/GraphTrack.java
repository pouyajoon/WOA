package woaobject.track;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;

import woaobject.WOAObject;
import woaobject.pheromone.NodePheromone;
import world.AntBox;
import world.AntWorld;
import world.AntZone;
import world.Position;
import ant.Ant;

public class GraphTrack {
	public AntWorld w = null;

	public ArrayList<TrackNode> nodes = new ArrayList<TrackNode>();
	public boolean trackCompleted = false;

	public GraphTrack(AntWorld _w) {
		w = _w;
	}

	public TrackNode initGraphTrack(Ant _ant, WOAObject initialTarget) {
		// create first node on the target
		TrackNode tn = new TrackNode(initialTarget, this);
		// update nodes on the target
		initialTarget.tracknodes.put(_ant.getPlayer(), tn);
		nodes.add(tn);
		w.graphtracks.add(this);
		System.out.println("NEW Graph Track created");
		return tn;
	}

	public TrackNode closeTrack(WOAObject target, WOAObject endPoint,
			TrackNode currentNode) {
		TrackNode tn = new TrackNode(endPoint, this);
		tn.toTargets.put(target, currentNode);
		nodes.add(tn);
		currentNode.toHome = tn;
		trackCompleted = true;
		return tn;
	}

	public TrackNode addHomePheromone(WOAObject target, Position pos,
			TrackNode currentNode, AntBox _currentBox) {
		NodePheromone p = new NodePheromone(_currentBox, pos, this, null);
		TrackNode tn = new TrackNode(p, this);
		p.tn = tn;
		currentNode.toHome = tn;
		tn.toTargets.put(target, currentNode);
		nodes.add(tn);
		return tn;
	}

	public void linkTrackToHome(WOAObject home) {

	}

	@Override
	public String toString() {
		String msg = "GraphTrack [size:" + nodes.size() + "]";
		return msg;
	}

	public void updateNodeFromNews(Ant _ant, TrackNode tn, WOAObject target,
			float motivation) {
		if (tn == null)
			return;
		if (motivation == 0.0f) {
			// remove way to this node
			if (tn.toTargets.get(target) != null) {
				TrackNode lastTargetNode = tn.toTargets.get(target);
				lastTargetNode.toHome = null;
				if (lastTargetNode.woao.type.equals("food"))
					lastTargetNode.woao.tracknodes.remove(_ant.getPlayer());
			}
			tn.toTargets.remove(target);
		}
	}

	public void drawTrackLine() {
		for (TrackNode tn : nodes) {
			if (tn.toHome != null) {
				Position v1 = tn.toHome.getBodyPosition();
				Position v2 = tn.getBodyPosition();
				if (v1.get_map() != v2.get_map()) {
					Integer d = AntZone.NextBoxInMap(tn.toHome.woao.currentBox,
							tn.woao.currentBox);
					v1 = AntZone.getTargetPosition(tn.toHome.woao.currentBox,
							tn.woao.currentBox, d, v1.getVec2());
				}
				if (w.parent.currentTest.equals(tn.woao.currentBox)) {
					tn.woao.currentBox.m_debugDraw.drawSegment(v1.getVec2(),
							v2.getVec2(), new Color3f(255.0f, 0f, 0f));
				}
			}
		}
	}

	public void life() throws Throwable {
		drawTrackLine();
	}

	public Boolean stillInUsed() {
		for (TrackNode trackNode : nodes) {
			if (trackNode.woao.type.equals("food")) {
				if (trackNode.woao.adn.getCurrent("storage_food") != 0)
					return true;
			} else {
				if (trackNode.getMotivation() != 0)
					return true;
			}
		}
		return false;
	}

	public void finalize() {
		try {
			Iterator<TrackNode> i = nodes.iterator();
			while (i.hasNext()) {
				final TrackNode tn = i.next();
				if (tn.woao.type.equals("pheromone"))
					tn.woao.finalize();
				i.remove();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
