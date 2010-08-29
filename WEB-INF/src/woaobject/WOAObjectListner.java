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


//keep trace of objects by id.
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
		/*
		Object dynamic = so.getAttribute("dynamics");
		if (dynamic != null)
		{
			ArrayList<DrawObject> da = (ArrayList<DrawObject>)dynamic;
			so.beginUpdate();
			ArrayList<WOAObject> woa_list = all_objects_screen.get(0);
			//for (ArrayList<WOAObject> woa_list : all_objects_screen.get(0))
			{
				for (WOAObject woa : woa_list)
				{
					//da.add(woa.id);
					appli.log.debug("updating " + woa.id + " for dynamics");
					da.set(woa.id, woa.getDrawObject());
					//msg += woa.id + ".";
				}					
			}
			
		}
		*/
		
		
		//
		HashMap<Integer, DrawObject> da = new HashMap<Integer, DrawObject>();
		//String msg = "empty ";
		//ArrayList<Integer> da = new ArrayList<Integer>();
		if (all_objects_screen != null && all_objects_screen.size() > 0 )				
		{
			ArrayList<WOAObject> woa_list = all_objects_screen.get(0);
			//world.log.debug("updating : size for screen 0 : " + woa_list.size());	
			for (WOAObject woa : woa_list)
			{
				//da.add(woa.id);
				if (woa != null && !woa.type.equals("pheromone"))
				{
					DrawObject dobj = woa.getDrawObject();
					//if (dobj.type.equals("food"))
					{
						//System.out.println("dobj : " + dobj.toString());	
					}					
					da.put(woa.id, dobj);	
					//float dir_rad = woa.getDrawObject().direction;
					//float dir_deg = (float) Math.toDegrees(dir_rad);
					//if (woa.type.equals("ant"))
					{
						//System.out.println("woa id : " + woa.id + ", dir/rad=" + dir_rad + ", dir_deg " + dir_deg);	
					}
					//System.out.println("share : " + woa.toString());
				}
				
				//msg += woa.id + ".";
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
			else
			{
				appli.log.debug("da is null");	
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
			    //so.setAttribute("time", worldtime);
			    //appli.log.debug("timer shared object start to " + so.getAttribute("time").toString());
				
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
				
				/*
				
				String msg = "empty ";
				//ArrayList<Integer> da = new ArrayList<Integer>();
				if (all_objects_screen != null && all_objects_screen.size() > 0 )				
				{
					ArrayList<WOAObject> woa_list = all_objects_screen.get(0);
					appli.log.debug("size for screen 0 : " + woa_list.size());	
					for (WOAObject woa : woa_list)
					{
						//da.add(woa.id);
						if (woa != null)
						{
							//appli.log.debug("woa id : " + woa.id);	
							da.put(woa.id, woa.getDrawObject());
							world.log.debug("object shared : " + woa.toString());
						}
						
						//msg += woa.id + ".";
					}
					so.setAttribute("message", "creation");
					world.log.debug("objects share list : " + da.toString());
				}
				if (da != null)
				{				
					appli.log.debug("dynamics size is :" + da.size());
					so.setAttribute("dynamics", da);			
				}
				*/
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
