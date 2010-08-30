package com.woa;

import static org.red5.server.api.ScopeUtils.getScopeService;

import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectService;
import org.red5.server.so.SharedObjectService;

import world.AntWorld;

public class MyTimer {
	  Toolkit toolkit =  null;
	  Timer timer = null;
	  public Integer worldtime = 0;
	  private AntWorld world = null;
	  public ISharedObject so = null;

	  public void create_tempo()
	  {
		  if (world.appli != null)
		  {
			  	world.log.debug("trying to create tempo, appli: " + world.appli.toString());
				if (world.appli.appScope != null)
				{														
			      ISharedObjectService service = (ISharedObjectService) getScopeService(
			    		  world.appli.appScope,
			              ISharedObjectService.class,
			              SharedObjectService.class,
			              false
			        );				
			        if (service.createSharedObject(world.appli.appScope, "tempo", false) == true)
			        {
						so = service.getSharedObject(world.appli.appScope, "tempo", false);	
						world.log.debug("timer shared object setup");	
					    so.setAttribute("time", worldtime);
					    world.log.info("timer shared object start to " + so.getAttribute("time").toString());			        	
			        }
				}
				else
				{
					world.log.error("app scope is null");	
				}			  
		  }
		  else
		  {
			  world.log.error("application is null : shared timer has not been set.");
		  }
	  }
	  
	  public MyTimer(AntWorld _world) 
	  {
		world = _world;
		world.log.debug("timer is starting");
	    toolkit = Toolkit.getDefaultToolkit();
	    timer = new Timer();
	    this.create_tempo();
	    timer.schedule(new RemindTask(), 0, //initial delay
	        1 * 500); //subsequent rate
	   
	  }	

	  class RemindTask extends TimerTask {

		public void update_so()
		{
			if (world.appli != null && so != null)
			{
		    	so.beginUpdate();
		    	Boolean b = so.setAttribute("time", worldtime);
		    	// SO UPDATE		    	
		    	so.endUpdate();				
		    	world.log.debug(b.toString() + " new step :" + so.getAttribute("time").toString());
			}
			else
			{
				world.log.error("application is null when sharing timer :" + world.appli + ", so=" + so);
			}
		}
		  
	    public void run() 
	    {
	    	worldtime++;
	    	world.log.debug("water fall : " + worldtime.toString());
	    	try 
	    	{
		    	update_so();
				if (world.appli != null)
					world.woal.woaobject_update();
	    	}
	    	catch (Exception e)
	    	{
	    		world.log.debug("Timer Failure under run : " + e.getLocalizedMessage());
	    		e.printStackTrace();
	    	}
	    }
	}
}
