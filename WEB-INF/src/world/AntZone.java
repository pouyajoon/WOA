package world;

import java.util.HashMap;

import org.jbox2d.common.Vec2;

public class AntZone 
{
	public AntWorld world = null;
    public HashMap<Integer, AntBox> boxes = new HashMap<Integer, AntBox>();
  	public static final int DIRECTION_WEST = 1;
  	public static final int DIRECTION_NORTH_WEST = 2;
  	public static final int DIRECTION_NORTH = 3;
  	public static final int DIRECTION_NORTH_EAST = 4;
  	public static final int DIRECTION_EAST = 5;
  	public static final int DIRECTION_SOUTH_EAST = 6;
  	public static final int DIRECTION_SOUTH = 7;
  	public static final int DIRECTION_SOUTH_WEST = 8;
  	
  	public static final float box_size_x = 2400.f;
  	public static final float box_size_y = 2400.f;
  	
    // number of screen to set in the world
    public static Integer zone_size = 1;  	
  	// number of screen to set in the world
    public Integer max_screens = 0;
    
	
	public AntZone(AntWorld _world, Integer _zone_size) {
		super();
		world = _world;
		zone_size = _zone_size;
		max_screens = zone_size * zone_size;
		SetupScreens();
	}
	
	public void SetupScreens()
	{		
		for (Integer i = 0; i < max_screens; ++i)
		{
			//colonys_screen.put(i, new ArrayList<Colony>());
			AntBox box = new AntBox(world);
			box.id = i;
			boxes.put(i, box);
			world.parent.registerExample(box);
			world.woal.woaobject_screen_share(box);		
		}		
	}
	
	
	/**
	 * Get the new position of the woa object depending on the new landed Box
	 * @param currentBox : last box of the woa object
	 * @param targetBox : new box of the woa object
	 * @param direction : direction of the box swith
	 * @param body_position : current object body position
	 * @return
	 */
	//public Position getTargetPosition(AntBox currentBox, AntBox targetBox, int direction, Vec2 body_position)
	public static Position getTargetPosition(AntBox currentBox, AntBox targetBox, int direction, Vec2 body_position)
	{
		Position targetPos = new Position(targetBox.id, body_position.x, body_position.y);
		switch (direction)
		{
			case DIRECTION_NORTH :
				targetPos.y = body_position.y - AntZone.box_size_y;
			break;
			case DIRECTION_SOUTH :
				// pos_y is negative
				targetPos.y =  AntZone.box_size_y + body_position.y;
			break;	
			case DIRECTION_WEST :
				targetPos.x =  AntZone.box_size_x + body_position.x;
			break;
			case DIRECTION_EAST :
				targetPos.x = body_position.x - AntZone.box_size_x;
			break;				
		}
		return targetPos;
	}
	
	
	/**
	 * Provide the target box direction
	 * @param boxSource : current source box
	 * @param boxTarget : destination box
	 * @return : the direction to use
	 */
	public static Integer NextBoxInMap(AntBox boxSource, AntBox boxTarget)
	{
		Integer source_row = boxSource.id / zone_size;
		Integer source_column = boxSource.id % zone_size;

		Integer target_row = boxTarget.id / zone_size;
		Integer target_column = boxTarget.id % zone_size;
		
		if (Math.abs(target_row - source_row) > zone_size / 2)
		{
			source_row = -source_row;
		}
		if (Math.abs(target_column - source_column) > zone_size / 2)
		{
			source_column = -source_column;
		}

		System.out.println(boxSource.id + ", " + boxTarget.id + " | source:[" + source_row +", " + source_column+ "], target:[" + target_row + "," + target_column + "]");
		
		if (target_row  < source_row && target_column < source_column)
		{
			return DIRECTION_NORTH_WEST;
		}
		if (target_row  < source_row && target_column > source_column)
		{
			return DIRECTION_NORTH_EAST;
		}
		if (target_row  > source_row && target_column < source_column)
		{
			return DIRECTION_SOUTH_WEST;
		}
		if (target_row  > source_row && target_column > source_column)
		{
			return DIRECTION_SOUTH_EAST;
		}		
		if (target_row  > source_row)
		{
			return DIRECTION_SOUTH;
		}
		if (target_row  < source_row)
		{
			return DIRECTION_NORTH;
		}	
		if (target_column > source_column)
		{
			return DIRECTION_EAST;
		}		
		if (target_column < source_column)
		{
			return DIRECTION_WEST;
		}
		return 0;		
	}
	
	/**
	 * get the new box depending on the changing direction of the woa object
	 * @param currentBox
	 * @param direction : woao direction (W, N, E, S)
	 * @returnthe the target box
	 */
	public AntBox getTargetBoxUsingDirection(AntBox currentBox, int direction)	
	{
		Integer new_id = currentBox.id;
		switch (direction)
		{
			case DIRECTION_NORTH :
				// try to see if it fit in the zone
				new_id = currentBox.id - zone_size;
				if (new_id < 0)
				{
					// new_id is already negative (so wee add it)
					new_id = max_screens + new_id;
				}
			break;
			case DIRECTION_SOUTH :
				// try to see if it fit in the zone
				new_id = currentBox.id + zone_size;
				if (new_id > max_screens - 1)
				{
					new_id = new_id - max_screens;
				}
			break;	
			case DIRECTION_WEST :
				// try to see if it fit in the zone
				if (currentBox.id % zone_size == 0)
				{
					new_id = currentBox.id + zone_size - 1;
				}
				else
				{
					new_id = currentBox.id - 1;
				}
			break;
			case DIRECTION_EAST :
				// try to see if it fit in the zone
				if ((currentBox.id + 1) % zone_size == 0)
				{
					new_id = currentBox.id - (zone_size - 1);
				}
				else
				{
					new_id = currentBox.id + 1;
				}
			break;				
		}
		return boxes.get(new_id);
		
	}
	
}
