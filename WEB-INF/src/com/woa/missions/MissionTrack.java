package com.woa.missions;

import java.util.ArrayList;

import org.jbox2d.testbed.BodyUtils;

import woaobject.WOAObject;
import woaobject.pheromone.NodePheromone;
import woaobject.track.GraphTrack;
import woaobject.track.TrackNode;
import world.AntWorld;
import ant.Ant;

public class MissionTrack {
	// states definition
	public static final int STATE_GO_TARGET = 0;
	public static final int STATE_GO_HOME = 1;
	public static final int STATE_GO_HOME_AND_CREATE_TRACK = 2;
	public static final int STATE_FOLLOW_TRACK_TO_TARGET = 3;
	public static final int STATE_FOLLOW_TRACK_TO_HOME = 4;
	public static final int STATE_EAT = 5;
	public static final int DISTANCE_TO_NEXT_NODE = 30;
	public WOAObject target = null;
	public WOAObject mainTarget = null;
	public Integer state = STATE_GO_TARGET;
	public AntWorld w = null;
	public GraphTrack gt = null;
	public float foodInMind = 0;
	public ArrayList<WOAObject> targets = new ArrayList<WOAObject>();
	public TrackNode currentNode = null;

	public MissionTrack(AntWorld _w, WOAObject _target) {
		w = _w;
		target = _target;
		if (mainTarget == null)
			mainTarget = _target;
		// add a target
		targets.add(_target);
	}

	public void missionIsOver(Ant _ant) {
		gt = null;
		_ant.currentTrackMission = null;
		currentNode = null;
		target = null;
	}

	public TrackNode otherTrackExistForTarget(Ant _ant, WOAObject target) {
		ArrayList<WOAObject> pheromones = BodyUtils.lookFor(_ant, "pheromone",
				_ant.viewZone());
		for (WOAObject wo : pheromones) {
			NodePheromone p = (NodePheromone) wo;
			if (p.tn.toTargets.containsKey(target))
				return p.tn;
		}
		return null;

	}

	public void collisionWithTarget(Ant _ant, WOAObject other) {
		if (other.type.equals("food")) {
			if (gt == null) // has not yet created the track to home
			{
				// if there is other track with this target for the player
				if (other.tracknodes.containsKey(_ant.getPlayer())) { // take
																		// the
																		// track
					TrackNode tn = other.tracknodes.get(_ant.getPlayer());
					gt = tn.gt;
					currentNode = tn;
					state = STATE_FOLLOW_TRACK_TO_TARGET;
				} else { // create a new track
					gt = new GraphTrack(w);
					currentNode = gt.initGraphTrack(_ant, other);
					state = STATE_GO_HOME_AND_CREATE_TRACK;
					_ant.eatFood(other);
					foodInMind = other.adn.getCurrent("storage_food");
				}
			} else // the track exists
			{
			}
		}
		if (other.type.equals("colony") && other == _ant.getColony()) {
			if (gt != null) // if a track exists, follow it
			{
				// give the food to the Colony
				_ant.getColony().adn.increase("storage_food",
						_ant.adn.getCurrent("storage_food"));
				// release ant's storage_food area
				_ant.adn.updateCurrent("storage_food", 0);
				missionIsOver(_ant);
			} else
				missionIsOver(_ant);
		}
	}

	public void whatToDo(Ant _ant) {
		switch (state) {
		case STATE_GO_TARGET: // go to target
			_ant.MoveToWOAObject(target);
			if (_ant.myBody.isTouching(target.myBody))// _ant.mandible != null)
			{
				_ant.eatFood(target);
				if (_ant.adn.hasMax("storage_food")
						|| target.adn.isEmpty("storage_food")) {
					state = STATE_FOLLOW_TRACK_TO_HOME;
					foodInMind = target.adn.getCurrent("storage_food");
				}
			}
			if (target.adn.isEmpty("storage_food")) {
				foodInMind = 0;
				state = STATE_FOLLOW_TRACK_TO_HOME;
			}
			break;
		case STATE_GO_HOME: // go to home
			_ant.releaseMandible();
			_ant.MoveToWOAObject(_ant.getColony());
			break;
		case STATE_GO_HOME_AND_CREATE_TRACK: // go to home & create track
			float distance_to_home = _ant.getDistance(_ant.getColony());
			if (!_ant.getPos().get_map()
					.equals(_ant.getColony().getPos().get_map()))
				distance_to_home = DISTANCE_TO_NEXT_NODE * 2;
			System.out.println("home distance : " + distance_to_home);
			// if near the home, go to it directly
			if (distance_to_home < DISTANCE_TO_NEXT_NODE) {
				if (foodInMind > 0) {
					// close the track to the home
					if (!gt.trackCompleted) {
						currentNode.updateMotivation(target, foodInMind);
						currentNode = gt.closeTrack(target, _ant.getColony(),
								currentNode);
					}
				} else { // no need to create track, can be deleted, no food
							// available
					gt = null;
					state = STATE_GO_HOME;
				}
			} else { // still far from home
				float distance_with_last_node = _ant
						.getDistance(currentNode.woao);
				// create a node every 100 distances
				if (distance_with_last_node > 100) {
					// create a new pheromone
					if (foodInMind > 0) {
						currentNode = gt.addHomePheromone(target,
								_ant.getPos(), currentNode, _ant.currentBox);
						currentNode.updateMotivation(target, foodInMind);
						currentNode.reduceAntRequested(target, _ant);
					}
				} else {
				}
			}
			_ant.MoveToWOAObject(_ant.getColony());
			break;
		case STATE_FOLLOW_TRACK_TO_TARGET:
			_ant.releaseMandible();
			if (currentNode == null) {
				missionIsOver(_ant);
				break;
			}
			// if the current known node is no more part of the track
			if (!gt.nodes.contains(currentNode)) {
				missionIsOver(_ant);
				break;
			}
			// no more nodes to browse // last security
			if (gt.nodes.size() == 0) {
				missionIsOver(_ant);
				break;
			}
			if (currentNode.woao == null) {
				missionIsOver(_ant);
				break;
			}
			if (currentNode.woao.type.equals("food")) {
				state = STATE_GO_TARGET;
				break;
			}
			float distance_to_next_node = _ant.getDistance(currentNode.woao);
			if (distance_to_next_node < DISTANCE_TO_NEXT_NODE) // next node
																// reached
			{
				if (currentNode.toTargets.size() > 0) {
					Boolean stillRequestAnt = currentNode
							.stillRequestAnt(target);
					if (stillRequestAnt) {
						currentNode.increaseAntRequested(target, _ant);
						currentNode = currentNode.toTargets.get(target);
					} else {
						missionIsOver(_ant);
						break;
					}
				}
			}
			_ant.MoveToWOAObject(currentNode.woao);
			break;
		case STATE_FOLLOW_TRACK_TO_HOME:
			_ant.releaseMandible();
			if (_ant.adn.getRatio("storage_food") < 0.5 && foodInMind != 0) {
				missionIsOver(_ant);
				break;
			}
			if (currentNode == null) {
				missionIsOver(_ant);
				break;
			}
			float distance_to_next_home_node = _ant
					.getDistance(currentNode.woao);
			if (distance_to_next_home_node < DISTANCE_TO_NEXT_NODE) // next node
																	// reached
			{
				// update pheromone motivation (size)
				TrackNode keepMe = currentNode.toHome;
				if (currentNode.woao != _ant.getColony()) {
					currentNode.updateMotivation(target, foodInMind);
					currentNode.reduceAntRequested(target, _ant);
					gt.updateNodeFromNews(_ant, currentNode, target, foodInMind);
					currentNode = keepMe;
				} else {
					gt.updateNodeFromNews(_ant, currentNode, target, foodInMind);
					state = STATE_GO_HOME;
				}
			} else {
				_ant.MoveToWOAObject(currentNode.woao);
			}
			break;
		case STATE_EAT:
			state = STATE_FOLLOW_TRACK_TO_HOME;
			System.out.println("gt current node : " + currentNode.toString());
			break;

		default:
			break;
		}
	}

	public String toString() {
		String s = "";
		if (target != null)
			s += "Mission : STATE[" + state + "], TARGET : "
					+ target.toString();
		else
			s += "Mission : STATE[" + state + "], TARGET : null";
		return s;
	}
}
