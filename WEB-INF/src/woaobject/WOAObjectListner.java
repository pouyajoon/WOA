package woaobject;


import static org.red5.server.api.ScopeUtils.getScopeService;

import java.util.ArrayList;
import java.util.HashMap;

import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectService;
import org.red5.server.so.SharedObjectService;

import world.AntBox;
import world.AntWorld;

import com.unicate.DrawObject;
import com.woa.Application;


/*
** keep trace of objects by id.
*/
public class WOAObjectListner {
	public HashMap<Integer, WOAObject> all_objects;
	public HashMap<Integer, ArrayList<WOAObject>> all_objects_screen;
	private Integer generated_id = 0;
	public Application appli;
	public ISharedObject so;
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
	
	public void woaobject_update()
	{

		HashMap<Integer, DrawObject> da = new HashMap<Integer, DrawObject>();
		if (all_objects_screen != null && all_objects_screen.size() > 0 )				
		{
			ArrayList<WOAObject> woa_list = all_objects_screen.get(0);
			for (WOAObject woa : woa_list)
			{
				if (woa != null && !woa.type.equals("pheromone"))
				{
					DrawObject dobj = woa.getDrawObject();
					da.put(woa.id, dobj);	
				}
			}
		}
		if (appli != null)
		{
			if (da != null)
			{				
				appli.log.debug("dynamics size is :" + da.size());
				if (so != null)
				{
					so.beginUpdate();
					so.setAttribute("message", "update");
					so.setAttribute("dynamics", da);
					so.endUpdate();					
				}
			}
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
				so = service.getSharedObject(appli.appScope, "woalshare_" + ab.id, false);	
				appli.log.debug("share world has been setup");	
				try
				{
					//woaobject_update();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				HashMap<Integer, DrawObject> da = new HashMap<Integer, DrawObject>();
				if (so != null)
				{
					so.beginUpdate();
					so.setAttribute("message", "creation");
					so.setAttribute("dynamics", da);
					so.endUpdate();					
				}
			}
			else
				appli.log.debug("app scope is null under woaobject share");	
		}

	}
	
	public void remove_o(Integer id)
	{
		try
		{
			WOAObject r = all_objects.get(id);
			if (r != null)
			{
				ArrayList<WOAObject> objscreen = all_objects_screen.get(r.pos._map);
				objscreen.remove(r);			
				all_objects.remove(id);				
			}
			else
				System.out.println("trying to remove a no present WoaObject inside all_objects");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
		}
	}
}
