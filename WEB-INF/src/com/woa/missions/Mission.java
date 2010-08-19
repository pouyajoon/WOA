package com.woa.missions;


public class Mission {
/*	public Ant ant;
	public int state;
	public WOAObject target = null;
	public Integer target_id = 0;
	public int carry_tentative = 0;
	
	public boolean piste_completed = false;
	public Pheromone firstPheromone = null;
	public Pheromone homePheromone = null;
	
	public Pheromone currentPheromone = null;
	public float rememberdTargetFood = 0;
	public Track targetTrack;
	
	public Mission(Ant _ant, WOAObject _target)
	{
		ant = _ant;
		state = 1;
		target = _target;
		carry_tentative = 0;
		//piste_completed = false;
		if (target != null)
		{
			target_id = target.id;
		}
		//System.out.println("missions added for the ant");
	}
	
	*//**
	 * When the ant collide with the colony
	 *//*
	public void collisionWithColony()
	{

		// give the food to the Colony
		ant.getColony().adn.increase("storage_food", ant.adn.getCurrent("storage_food"));
		// release ant's storage_food area
		ant.adn.updateCurrent("storage_food", 0);
		
		if (targetTrack != null)
		{
			targetTrack.completed = true;
			state = 3;			
		}
		//currentPheromone = 
		
		
		if (target != null)
		{		
			//System.out.println("going to target, colony reached : " + homePheromone);
			
			
			currentPheromone = homePheromone;
			// if target still there, run back to it
			state = 4;
		}
		else
		{
			// go back roaming
			state = 0;
		}
		
		if (homePheromone != null)
		{
			currentPheromone = homePheromone;
			state = 4;
		}
		else
		{
			ant.currentMission = null;
			//state = 0;
		}
		
		System.out.println("collide with home + " + toString());
		System.out.println("home pheromone : " + homePheromone);
		if (piste_completed == false)
		{
			//System.out.println("piste completed : " + toString());
			piste_completed = true;
			//homePheromone = currentPheromone;
		}
		
		
	}
	
	public void endMission()
	{
		targetTrack = null;
		ant.currentMission = null;
		
		//System.out.println("ending mission");
		// remove target if there is no more food
		
		target = null;
		// release the mandible...
		
		rememberdTargetFood = 0;
		ant.releaseMandible();
		// go back to colony
		
		if (firstPheromone != null)
		{
			currentPheromone = firstPheromone;
			state = 3;	
		}
		else
		{
			System.out.println("no more pheromone when finish mission");
			ant.currentMission = null;
			// go back on roaming			
			state = 0;			
		}

		
		if (ant.adn.getCurrent("storage_food") / ant.adn.getMax("storage_food") > 0.5f)
		{ // if the ant has more than of half of his capacity go back home
			// go back to colony
//			currentPheromone = firstPheromone;
//			state = 3;
		}
		else
		{
			// no more current mission
//			ant.currentMission = null;
			// go back on roaming			
//			state = 0;
		}
		
	}
	
	public WOAObject collisionWithTarget(WOAObject other, ContactPoint cp)
	{
		// check if the ant collide with his mission target 
		if (other == null) return null;
		if (target == null) return null;
		if (target.equals(other))
		{
			//System.out.println("collision with : " + other.toString());
			if (other.type.equals("food"))
			{
				// catch the food with the ant mandible
				ant.catchWithMandible(other, cp.position);
				// if there is no track create the new one
				if (targetTrack == null)
				{
		    		//Pheromone p = new Pheromone(ant.world, new Position(0, 450, 250));
		    		//ant.world.pheromones.add(p);
					
					//Pheromone p = new Pheromone(other.world, ant.getPos());
					//ant.world.addPheromone(p);

					ArrayList<WOAObject> pheromones =  BodyUtils.lookFor(ant, "pheromone", ant.viewZone());
					System.out.println("SMELL ["+ ant.id +"]before create a new track :" + pheromones.size());
					for (WOAObject wo : pheromones)
					{
						Pheromone p = (Pheromone)wo;
						if (p.current_track.target == target)
						{
							System.out.println("YES same pheromone");
							ant.currentMission = null;
							//ant.smellPheromone(p);
							return other;
						}			
						else
						{
							System.out.println("i have smell a pheromone but it's not the same than the target");
						}
					}
					targetTrack = new Track(ant.currentBox.world, other);
					
					//targetTrack.setTargetPheromone(ant.getPos());				
				}
				return other;
			}		
		}

		return null;			
	}
	
	public WOAObject searchFoodButNotTheTarget()
	{
		ArrayList<WOAObject> woa_objects = BodyUtils.lookFor(ant, "food", ant.viewZone());
		for (WOAObject woao : woa_objects)
		{
			if (woao != target)
			{
				return woao;
			}
		}	
		return null;
	}
	
	
	public void whatToDo()
	{
		try 
		{
			//System.out.println("current mission state : " + state);
			switch (state)
			{
			//roam
			case 0 :			
			break;
			// go to target
			case 1 :
				if (ant.MoveToWOAObject(target) == null)
				{
					// no more target
					System.out.println("trying to reach unexisting target");
					//ant.releaseMandible();
				}
				else
				{
					// go to target
					
					// if ant has catch something
					if (ant.mandible != null)
					{
						// what the ant has catch ?
						WOAObject food = (WOAObject)ant.mandible.m_body2.getUserData();
						// eat some
						ant.eatFood(food);
						//System.out.println("ant ate: " + ant.adn.toString("storage_food"));						
					}
					if (target.adn.isEmpty("storage_food"))
					{
						//currentPheromone = targetTrack.targetPheromone;
						state = 4;
					}						
					// if ant has no more room
					if (ant.adn.hasMax("storage_food"))
					{						
						//ant.releaseMandible();
						//currentPheromone = targetTrack.targetPheromone;
						state = 4;
					}
					if (target == null)
					{
						rememberdTargetFood = 0;
					}
					else
					{
						rememberdTargetFood = target.adn.getCurrent("storage_food");
					}
				}
			break;
			// go to home
			case 2 :
				ant.MoveToWOAObject(ant.getColony());
			break;
			// follow track to target
			case 3 : 
				ant.releaseMandible();
				//System.out.println("current pheromone : " + currentPheromone);
				//System.out.println("current pheromone  to T: " + currentPheromone.toTrackString());
				if (target == null)
				{
					endMission();
					break;
				}
				if (targetTrack != null && currentPheromone == targetTrack.targetPheromone)
				{
					state = 1;
				}
				else
				{						
					if (targetTrack.pheromones.size() == 0 || currentPheromone == null)
					{
						endMission();
						break;
					}
					if (ant.getDistance(currentPheromone) < 10)
					{
						//System.out.println("follow path : " + currentPheromone.toTrackString());
						currentPheromone = currentPheromone.targetPheromone;	
					}					
					ant.MoveToWOAObject(currentPheromone);
				}
			break;
			// follow track to home
			case 4 :	
				ant.releaseMandible();
				if (targetTrack != null && targetTrack.targetPheromone == null)
				{
					//System.out.println("target pheromone is null");
					targetTrack.setTargetPheromone(target.getPos(), target.id, rememberdTargetFood);
					//currentPheromone.setMotivation(target_id, rememberdTargetFood);
					currentPheromone = targetTrack.targetPheromone;
				}
				
				WOAObject seeOtherFood = searchFoodButNotTheTarget();
				if (seeOtherFood != null)
				{
					target = seeOtherFood;
					target_id = seeOtherFood.id;
					// go to it
					state = 5;
					break;
				}				
				
				if (targetTrack != null && targetTrack.completed == false && currentPheromone.homePheromone == null)
				{
					if (ant.getDistance(currentPheromone) > 100)
					{										
						//Pheromone rem = currentPheromone;
						//System.out.println("old pheromone:" + rem.toTrackString());
						currentPheromone = targetTrack.setNewPheromone(ant.pos, target_id, rememberdTargetFood);						
						//currentPheromone.targetPheromone = rem;
						//System.out.println("set new pheromone :" + currentPheromone.toTrackString());						
					}
					ant.MoveToWOAObject(ant.getColony());
				}
				else
				{ // path exists
					// if the pheromone is the last one before home
					if ((targetTrack != null && targetTrack.pheromones.size() == 0) || currentPheromone == null)
					{
						endMission();
						break;
					}
					if (currentPheromone == targetTrack.homePheromone)
					{					
						// if have reached it
						if (ant.getDistance(currentPheromone) < 10)
						{ // update motivation & go directly home
							currentPheromone.setMotivation(target_id, rememberdTargetFood);
							state = 2;
						}
						else
						{// go to the pheromone
							ant.MoveToWOAObject(currentPheromone);
						}						
					}
					else
					{ // go to the pheromone
						if (ant.getDistance(currentPheromone) < 10)
						{						
							//System.out.println("follow path : " + currentPheromone.toTrackString());
							currentPheromone.setMotivation(target_id, rememberdTargetFood);
							currentPheromone = currentPheromone.homePheromone;
						}
						ant.MoveToWOAObject(currentPheromone);
					}
				}
			break;
			// only go to target
			case 5 : 
				ant.MoveToWOAObject(target);
			break;
		
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public void setMotivation(WOAObject target, float motivation)
	{
		//if (target == null)
		currentPheromone.motivation.put(target_id, motivation);
		
		System.out.println("reached pheromone with motivation : " + currentPheromone.getMotivationMax());
		if (currentPheromone.getMotivationMax() == 0)
		{
			currentPheromone.addToGarbage();
			System.out.println("delete this : " + currentPheromone);
			// set the mission's home pheromone to null, if it's deleted
			//if (homePheromone == currentPheromone)
			{
			//	homePheromone = null;
			}
			//currentPheromone = null;
		}
		
	}
	
	public Mission clone(Ant a)
	{
		
		//Mission m = new Mission(a); 
		//m.target = target;
		// go to target
		//m.state = 1;
		return null;
	}
	
	public String toString()
	{
		String s = "";
		if (target != null)
		{
			 s += "Mission : STATE[" + state + "], TARGET : " + target.toString();
		}
		else
		{
			s += "Mission : STATE[" + state + "], TARGET : null";
		}
		return s;
	}
	*/
}
