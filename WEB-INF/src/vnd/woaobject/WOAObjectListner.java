package vnd.woaobject;


import static org.red5.server.api.ScopeUtils.getScopeService;

import java.util.ArrayList;
import java.util.HashMap;

import org.jbox2d.dynamics.Body;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectService;
import org.red5.server.so.SharedObjectService;

import vnd.world.AntBox;
import vnd.world.AntWorld;

import com.unicate.DrawObject;
import com.woa.Application;


//keep trace of objects by id.
/**
 * @author pouya
 *
 */
public class WOAObjectListner {

	public HashMap<Integer, WOAObject> all_objects;
	public HashMap<Integer, ArrayList<WOAObject>> all_objects_screen;
	private Integer generated_id = 0;
	public Application appli;
	//public ISharedObject so;
	public AntWorld world;
	
	public WOAObjectListner(AntWorld w, Application a)
	{
		this.appli = a;
		this.world = w;
		all_objects = new HashMap<Integer, WOAObject>();
		all_objects_screen = new HashMap<Integer, ArrayList<WOAObject>>();
	}
	
	public Integer get_new_id()
	{
		generated_id++;
		return generated_id;
	}
	
	public void add_o(WOAObject o)
	{
		all_objects.put(o.id, o);
		all_objects_screen.get(o.pos._map).add(o);
	}
	public WOAObject get_o(Integer id)
	{
		return all_objects.get(id);
	}

	public DrawObject getDrawObject(Integer id)
	{
		WOAObject dobject = all_objects.get(id);
		return dobject.getDrawObject();
	}
	
	
	/**
	 * browse bodies for this antBox id and put them in da
	 * @param antBox_id
	 * @param da
	 */
	public void antBox_update(Integer antBox_id, HashMap<Integer, DrawObject> da)
	{
		
		Body b = world.antzone.boxes.get(antBox_id).m_world.getBodyList();
		
		while (b != null)
		{
			WOAObject woa = (WOAObject)b.getUserData();
			if (woa != null && !woa.type.equals("pheromone"))
			{
				DrawObject dobj = woa.getDrawObject();
				da.put(woa.id, dobj);	
			}
			b = b.getNext();	
		}		
		world.log.debug("antBox udated : " + antBox_id + ", number : " + da.size());
	}
	
	public void updateSharedObjectForAntBox(ISharedObject so, HashMap<Integer, DrawObject> da)
	{
		if (appli != null)
		{
			if (da != null)
			{				
				appli.log.debug("when updates [" + so.getName() + "] dynamics size is :" + da.size());
				if (so != null)
				{
					so.beginUpdate();
					so.setAttribute("message", "update");
					so.setAttribute("dynamics", da);
					so.endUpdate();	
					
				}
			}
			else
			{
				appli.log.debug("da is null");	
			}			
		}		
	}
	
	
	public void woaobject_update()
	{
		HashMap<Integer, DrawObject> da = null;
		for (int i = 0; i < world.antzone.max_screens; ++i)
		{
			da = new HashMap<Integer, DrawObject>();
			//world.log.debug("da size on creation: " + da.size());
			antBox_update(i, da);
			updateSharedObjectForAntBox(world.antzone.boxes.get(i).so, da);			
		}
		
	}
	
	public void woaobject_screen_share(AntBox ab)
	{
		if (appli != null)
		{
			if (appli.appScope != null)
			{				
		      ISharedObjectService service = (ISharedObjectService) getScopeService(
		    		  appli.appScope,
		              ISharedObjectService.class,
		              SharedObjectService.class,
		              false
		        );				
					
		        service.createSharedObject(appli.appScope, "woalshare_" + ab.id, false);
				ab.so = service.getSharedObject(appli.appScope, "woalshare_" + ab.id, false);	
				appli.log.debug("share world has been setup");	
			    //so.setAttribute("time", worldtime);
			    //appli.log.debug("timer shared object start to " + so.getAttribute("time").toString());

				
				HashMap<Integer, DrawObject> da = new HashMap<Integer, DrawObject>();
				if (ab.so != null)
				{
					ab.so.beginUpdate();
					ab.so.setAttribute("message", "creation");
					ab.so.setAttribute("dynamics", da);
					ab.so.endUpdate();					
				}

			}
			else
			{
				appli.log.debug("app scope is null under woaobject share");	
			}					
		}

	}
	
	public void remove_o(Integer id)
	{
		try
		{
			WOAObject r = all_objects.get(id);
			if (r != null)
			{
				//System.out.println("kill this : " + r.toString());
				ArrayList<WOAObject> objscreen = all_objects_screen.get(r.pos._map);
				objscreen.remove(r);			
				all_objects.remove(id);				
	    		//ServiceUtils serverutils = new ServiceUtils();
				//serverutils.invokeOnAllConnections(appli.appScope, "diedOnServer", new Object[]{id});
			}
			else
			{
				System.out.println("trying to remove a no present WoaObject inside all_objects");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			//System.out.println("object removed from listner");
			//RTMPConnection.invoke("Red5ClientTest/add_log");
			
		}
	}
}
