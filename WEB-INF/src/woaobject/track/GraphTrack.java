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

public class GraphTrack 
{
	public AntWorld w = null;
	
	//public TrackNode currentNode = null;
	public ArrayList<TrackNode> nodes = new ArrayList<TrackNode>();
	public boolean trackCompleted = false;
	
	public GraphTrack(AntWorld _w)
	{
		w = _w;
	}
	
	public TrackNode initGraphTrack(Ant _ant, WOAObject initialTarget)
	{
		// create first node on the target
		TrackNode tn = new TrackNode(initialTarget, this);
		// update nodes on the target
		initialTarget.tracknodes.put(_ant.getPlayer(), tn);
		nodes.add(tn);
		//currentNode = tn;
		w.graphtracks.add(this);
		System.out.println("NEW Graph Track created");
		return tn;
	}
	
	public TrackNode closeTrack(WOAObject target, WOAObject endPoint, TrackNode currentNode)
	{
		TrackNode tn = new TrackNode(endPoint, this);
		tn.toTargets.put(target, currentNode);
		nodes.add(tn);
		currentNode.toHome = tn;
		//currentNode = tn;
		trackCompleted = true;
		return tn;
	}
	
	public TrackNode addHomePheromone(WOAObject target, Position pos, TrackNode currentNode, AntBox _currentBox)
	{
		NodePheromone p = new NodePheromone(_currentBox, pos, this, null);
		TrackNode tn = new TrackNode(p, this); 
		p.tn = tn;
		currentNode.toHome = tn;
		tn.toTargets.put(target, currentNode);
		//currentNode = tn;
		nodes.add(tn);
		return tn;
	}
	
	public void linkTrackToHome(WOAObject home)
	{
		
	}
	
	@Override
	public String toString()
	{
		String msg = "GraphTrack [size:" + nodes.size() + "]";
		return msg;	
	}
	
	
	public void updateNodeFromNews(Ant _ant, TrackNode tn, WOAObject target, float motivation)
	{
		if (tn == null) return;
		if (motivation == 0.0f)
		{
			// remove way to this node				
			if (tn.toTargets.get(target) != null)
			{
				TrackNode lastTargetNode = tn.toTargets.get(target);
				lastTargetNode.toHome = null;	
				if (lastTargetNode.woao.type.equals("food"))
				{
					lastTargetNode.woao.tracknodes.remove(_ant.getPlayer());
				}
			}				
			tn.toTargets.remove(target);
//			System.out.println("targets size : " + tn.toTargets.size());
		}		
	}
	

	
	public void drawTrackLine()
	{		
		for(TrackNode tn : nodes)
		{
			if (tn.toHome != null)
			{
				Position v1 = tn.toHome.getBodyPosition();
				Position v2 = tn.getBodyPosition();
				if (v1.get_map() != v2.get_map())
				{
					Integer d = AntZone.NextBoxInMap(tn.toHome.woao.currentBox, tn.woao.currentBox);
					v1 = AntZone.getTargetPosition(tn.toHome.woao.currentBox, tn.woao.currentBox, d, v1.getVec2());
				}
				
				if (w.parent.currentTest.equals(tn.woao.currentBox))
				{
					tn.woao.currentBox.m_debugDraw.drawSegment(v1.getVec2(), v2.getVec2(), new Color3f(255.0f, 0f, 0f));	
				}				
			}
		}
	}

	public void life() throws Throwable
	{
		
		//System.out.println("graph life : " + nodes.size());
		//if (!stillInUsed()){
		
		//ArrayList<TrackNode> should_be_deleted = new ArrayList<TrackNode>();
		
		

		
//		if (motivation == false)
//		{
//			//should_be_deleted = nodes;
//		}
//		
/*
		if (nodes.size() != 0)
		{
			System.out.println("tracknodes : " + nodes.size());
		}
		if (nodes.size() <= 2)
		{
			for (TrackNode n : nodes)
			{
				System.out.println("tracknodes woao : " + n.woao);
				if (n.woao.type.equals("pheromone"))
				{
					System.out.println("pheromone: " + n.getMotivation());
				}
			}
		}
*/
		
//		if (nodes.size() == 2)
//		{
//			for (Integer i = 0; i < 2; ++i)
//			{
//				if (nodes.get(i).woao.type.equals("food"))
//				{
//					if (nodes.get(i).woao.adn.getCurrent("food_storage") == 0)
//					{
//						Integer j = 0;
//						if (i == 0) j = 1;
////						Integer j = !i;
//						if (nodes.get(j).woao.type.equals("colony"))
//						{
//							System.out.println("special failure cleaning");
//							should_be_deleted.add(nodes.get(i));	
//						}							
//					}					
//				}
////				System.out.println("tracknodes woao : " + n.woao);
//			}
//
//		}
//
//		if (nodes.size() == 1)
//		{
//			if (nodes.get(0).woao.type.equals("colony") && trackCompleted == true)
//			{
//				System.out.println("only colony delete it");
//				should_be_deleted.add(nodes.get(0));
//			}
//			if (nodes.get(0).woao.type.equals("food") && trackCompleted == true)
//			{
//				if (nodes.get(0).woao.adn.getCurrent("food_storage") == 0)
//				{
//					System.out.println("only empty food : clean track");
//					should_be_deleted.add(nodes.get(0));
//				}
//			}
//			
//			
//		}
		// TODO Auto-generated method stub
		
//		for(TrackNode tn : nodes)
//		{
//			if (tn.woao == null)
//			{
//				System.out.println("my bag is empty");
//			}
//			if (tn.toTargets.size() == 0 && tn.woao.type.equals("food"))
//			{
//				if (tn.toHome == null && trackCompleted)
//				{
//					should_be_deleted.add(tn);
//				}
//			}		
//			if (tn.toTargets.size() == 0 && !tn.woao.type.equals("food"))
//			{
//				if (tn.toHome == null)
//				{
//					should_be_deleted.add(tn);
//				}
//			}
//			/*
//			if (tn.toTargets.size() == 0 && !tn.woao.type.equals("colony"))
//			{
//				if (tn.toHome == null)
//				{
//					should_be_deleted.add(tn);
//				}
//			}	
//			*/		
//		}		
		//System.out.println("size of should be deleted : " + should_be_deleted.size());
//		Iterator<TrackNode> i = should_be_deleted.iterator();
//		while (i.hasNext())
//		{
//			TrackNode tn = i.next();
//			if (tn.woao.type.equals("pheromone"))
//			{
//				tn.woao.finalize();
//			}
//			i.remove();
//		}
//		for(TrackNode tn : should_be_deleted)
//		{
//			if (tn.woao.type.equals("pheromone"))
//			{
//				tn.woao.finalize();
//			}
//			nodes.remove(tn);
//		}
		//System.out.println("size of nodes : " + nodes.size());
		drawTrackLine();
	}
	
	public Boolean stillInUsed()
	{
		//System.out.println("graph request usage : " + nodes.size());
		for (TrackNode trackNode : nodes)
		{
			//System.out.println("tracknode woao : " + trackNode.getMotivation() + ", " + trackNode.woao.toString());			
			if (trackNode.woao.type.equals("food"))
			{
				if (trackNode.woao.adn.getCurrent("storage_food") != 0)
				{
					return true;
				}
			}
			else
			{
				if (trackNode.getMotivation() != 0)
				{
					return true;
				}				
			}
		}
		return false;		
	}
	
	public void finalize()
	{
		//System.out.println("finalize graph track");
		try 
		{
			Iterator<TrackNode> i = nodes.iterator();
			while (i.hasNext())
			{
				final TrackNode tn = i.next();
				if (tn.woao.type.equals("pheromone"))
				{
					tn.woao.finalize();
					//tn = null;					
				}
				i.remove();
			}			
		}						
		catch (Throwable e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
