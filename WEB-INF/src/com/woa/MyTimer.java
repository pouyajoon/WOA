package com.woa;

import static org.red5.server.api.ScopeUtils.getScopeService;

import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectService;
import org.red5.server.so.SharedObjectService;

import vnd.world.AntWorld;

public class MyTimer {
	  Toolkit toolkit =  null;
	  Timer timer = null;
	  public Integer worldtime = 0;
	  private AntWorld world = null;
	  public ISharedObject so_timer = null;

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
			        	so_timer = service.getSharedObject(world.appli.appScope, "tempo", false);	
						world.log.debug("timer shared object setup");	
						so_timer.setAttribute("time", worldtime);
					    world.log.info("timer shared object start to " + so_timer.getAttribute("time").toString());			        	
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

		public void update_so_timer()
		{
			if (world.appli != null && so_timer != null)
			{
		    	so_timer.beginUpdate();
		    	Boolean b = so_timer.setAttribute("time", worldtime);
		    	// so_timer UPDATE		    	
		    	so_timer.endUpdate();				
		    	world.log.debug(b.toString() + " new step :" + so_timer.getAttribute("time").toString());
			}
			else
			{
				world.log.error("application is null when sharing timer :" + world.appli + ", so_timer=" + so_timer);
			}
		}
		  
	    public void run() 
	    {
	    	worldtime++;
	    	world.log.debug("water fall new : " + worldtime.toString());
	    	try 
	    	{
	    		//if ()
		    	//world.life();  	
		    	//world.antBox.m_world.step(1.0f / 30.0f, 10);
		    	update_so_timer();
		    	//world.woal.woaobject_update();
		    	//so.release();
		    	
				if (world.appli != null)
				{
					
					world.log.debug("updating woa shared listner");
					world.woal.woaobject_update();
				}		    	
	    	} 
	    	catch (Exception e)
	    	{
	    		world.log.debug("Timer Failure under run : " + e.getLocalizedMessage());
	    		e.printStackTrace();
	    	}
	    }
	  }
	}
