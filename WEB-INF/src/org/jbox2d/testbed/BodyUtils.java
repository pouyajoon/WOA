package org.jbox2d.testbed;

import java.util.ArrayList;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import woaobject.WOAObject;

public class BodyUtils {
	
    public static float calculateAngle(Vec2 v)
    {
		float v_div = v.y/v.x;
		
		Float angle_r = new Float(Math.atan(v_div));
		if (v.x < 0 && v.y > 0)
		{ 
			angle_r = new Float(Math.atan(-(v.x/v.y)));
			angle_r = angle_r + (float)Math.PI / 2 ;
			
		}
		if (v.x < 0 && v.y < 0)
		{ 
			//angle_r = new Float(Math.atan(-(v.x/v.y)));
			angle_r = angle_r + (float)Math.PI;
			
		}		 
		return angle_r;
    }	
	
	public static AABB ViewBox(WOAObject woaobject, Vec2 p, Integer zone)
	{
	   	//Vec2 p = world.m_debugDraw.screenToWorld(myBody.getPosition());
    	//Vec2 p = pos.getVec2();

        // Make a small box.
        Vec2 d = new Vec2(zone, zone);
        AABB aabb = new AABB(p.sub(d), p.add(d));
       
        // create a view border around the ant
		Vec2 worldLower = aabb.lowerBound;
		Vec2 worldUpper = aabb.upperBound;
		
    	Vec2[] vsw = new Vec2[4];
		vsw[0]= new Vec2(worldLower.x, worldLower.y);
		vsw[1]= new Vec2(worldUpper.x, worldLower.y);
		vsw[2]= new Vec2(worldUpper.x, worldUpper.y);
		vsw[3]= new Vec2(worldLower.x, worldUpper.y);		
		//return null;	
		
		woaobject.currentBox.m_debugDraw.drawPolygon(vsw, 4, AbstractExample.green);
		return aabb;
	}	
	
	public static ArrayList<WOAObject> lookFor(WOAObject woaobject, String searched_type, AABB aabb)
	{
		if (aabb == null) return null;
		ArrayList<WOAObject> woaobject_list = new ArrayList<WOAObject>();
 		
        //AABB aabb = viewZone();
        
        // Query the world for overlapping shapes.
        int k_maxCount = 20;
        Shape shapes[] = woaobject.currentBox.m_world.query(aabb, k_maxCount);
       // System.out.println("size of shapes :" + shapes.length);
        //Body body = null;
        for (int j = 0; j < shapes.length; j++) {
            Body shapeBody = shapes[j].getBody();
            if (woaobject.myBody != null)
            {
            	if (shapeBody.equals(woaobject.myBody)) continue;
            }                       
            //if (shapeBody.isStatic() == false)
            {
            	if (shapes[j].m_body.getUserData() != null)
            	{
            		WOAObject otherwoaobject = (WOAObject)shapes[j].m_body.getUserData();
        			if (otherwoaobject.type.equals(searched_type))
        			{
            			//System.out.println("smell : " + shapes[j].m_body.getUserData().toString());
            			woaobject_list.add(otherwoaobject);
            			//return woaobject;                			                				
        			}              		
            	}
            	else
            	{
            		//System.out.println("userData is null on looking");
            	}
            }
        }
        return woaobject_list;
	}	
	
//	public static void resizeBox(WOAObject wo, float new_size, float new_weight)
//	{
//		//System.out.println("resize it : " + wo.toString());
//		PolygonShape rectangle = (PolygonShape)wo.myBody.getShapeList();
//		//myBody.getShapeList().m_sweepRadius = adn.getCurrent("storage_food");
//		
//		//float r = circle.getRadius();
//		//float d = circle.m_density;
//		float f = rectangle.m_friction;
//		float rest = rectangle.m_restitution;
//		int cbits = rectangle.m_filter.categoryBits;
//		int mbits = rectangle.m_filter.maskBits;
//			
//		wo.myBody.destroyShape(rectangle);
//		//myBody.destroyShape(circle);
//		// creating a new circle shape
//		
//		PolygonShape newrectangleDef = new RectangleDef();
//		// calculating new radius
//		newCircleDef.radius = new_size;//adn.getCurrent("storage_food");
//		newCircleDef.density = new_weight;
//		newCircleDef.friction = f;
//		newCircleDef.restitution = rest;
//
//		newCircleDef.filter.categoryBits = cbits;
//		newCircleDef.filter.maskBits = mbits;
//		
//		wo.myBody.createShape(newCircleDef);			
//		wo.myBody.setMassFromShapes();			
//
//	}	
	
	public static void resizeCircle(WOAObject wo, float new_size, float new_weight)
	{
		//System.out.println("resize it : " + wo.toString());
		CircleShape circle = (CircleShape)wo.myBody.getShapeList();
		//myBody.getShapeList().m_sweepRadius = adn.getCurrent("storage_food");
		
		//float r = circle.getRadius();
		//float d = circle.m_density;
		float f = circle.m_friction;
		float rest = circle.m_restitution;
		int cbits = circle.m_filter.categoryBits;
		int mbits = circle.m_filter.maskBits;
			
		wo.myBody.destroyShape(circle);
		//myBody.destroyShape(circle);
		// creating a new circle shape
		
		CircleDef newCircleDef = new CircleDef();
		// calculating new radius
		newCircleDef.radius = new_size;//adn.getCurrent("storage_food");
		newCircleDef.density = new_weight;
		newCircleDef.friction = f;
		newCircleDef.restitution = rest;

		newCircleDef.filter.categoryBits = cbits;
		newCircleDef.filter.maskBits = mbits;
		
		wo.myBody.createShape(newCircleDef);			
		wo.myBody.setMassFromShapes();			

	}	
}
