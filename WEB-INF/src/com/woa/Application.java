package com.woa;
import java.util.ArrayList;

import org.jbox2d.testbed.AntFrameTestbed;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import world.AntBox;
import world.AntWorld;
import world.Position;

import com.red5.MySecurityHandler;
import com.unicate.DrawList;
import com.unicate.DrawObject;
import com.unicate.info.PlayerInfo;

import food.Food;


public class Application extends ApplicationAdapter
{
	public Logger log = LoggerFactory.getLogger(Application.class);

    public AntWorld world;
    public IScope appScope;
	
    public AntFrameTestbed worldframe = null;
    
	public Application()
	{
		log.info("Hello Man!");
		//this.log = log;
	}
	
	
	//DrawObject
	/*
	public DrawList get_actions()
	{
		// create a draw list with current actions 
		ArrayList<DrawObject> clone =  new ArrayList<DrawObject>(world.action_fifo);
		DrawList dl = new DrawList(clone);
		// clear the fifo
		world.action_fifo.clear();
		return dl;
	}	
	*/
	/*
	//DrawObject
	public DrawList get_colonies()
	{
		log.debug("colonies requested");
		//return world.woal.
		
		//String msg = "";
		ArrayList<DrawObject> out = new ArrayList<DrawObject>();
		
		ArrayList<Colony> colonys = world.getColonys_screen(0);
		//world.lifeColonys(c);

		if (colonys != null)
		{
			for (Colony c : colonys)
			{
				//msg += c.getPos().toString();
				DrawObject dwo = new DrawObject(c.id, c.getPos(), "colony");
				out.add(dwo);
				
				for (Ant a : c.getAnts())
				{
					out.add(new DrawObject(a.id, a.getPos(), "ant"));	
				}				
				return new DrawList(out);
			}				
		}	
		return null;
	}
	*/
	
	public String createFood(float pos_x, float pos_y, Integer worldBox_id)
	{
		float size = 120;		
		AntBox requestedBox = world.antzone.boxes.get(worldBox_id);
		Food food = new Food(requestedBox, new Position(0, pos_x, pos_y), size);		
		world.foods.add(food);
		String msg = "food created on " + food.getPos().toString();
		System.out.println("food created on " + pos_x + ", " + pos_y);
		return msg;
	}
	
	public PlayerInfo getPlayerInfo(String player_name)
	{
		return new PlayerInfo(world.players.get(player_name));
	}
	
	public String when()
	{
		String msg = "wake up @ : " + world.mytime.worldtime.toString();
		//String msg = "69";
		//log.debug("Asked for time : ", msg);
		return msg; 	
	}
	
	public DrawList get_bodys(Object c)
	{
		//ArrayList<DrawObject> woalist = new ArrayList<DrawObject>();
		//ArrayCollection object_list
//		ArrayList<DrawObject>
//		log.debug("somebody is searching for bodys : " + (()(c.out)).size());
		/*
		for(Object id : s)
		{
			log.debug("id is " + id);
			Integer i = Integer.parseInt(id.toString());
			log.debug("where is this body " + id + " : " + i);
			//woalist.add(world.woal.getDrawObject(i));
		}	
		//WOAObject woa = world.woal.getDrawObject(id);//world.woal.all_objects.get(id);
		//log.debug("i have found " + woalist.size());
		//return new DrawList(woalist);
		return null;
		*/
		return null;
	}
	
	
	public DrawList get_this_body(Integer id)
	{
		log.debug("where is this body " + id);
		//WOAObject woa = world.woal.getDrawObject(id);//world.woal.all_objects.get(id);
		ArrayList<DrawObject> woalist = new ArrayList<DrawObject>();
		woalist.add(world.woal.getDrawObject(id));
		log.debug("someone need this body : " + id + ", i have found " + woalist.size());
		return new DrawList(woalist);
	}
	
	/** {@inheritDoc} */
	@Override
    public boolean appStart(IScope appScope)
    {
		this.appScope = appScope;
        log.info( "Hello.appStart" );
        registerSharedObjectSecurity(new MySecurityHandler(this));

		try
		{
			//world = new AntWorld(log, this, null);
			log.debug("World created");
			worldframe = new AntFrameTestbed(log, this);
			
			//MyFrame f = new MyFrame(world);
			/*
			AntFrame f = new AntFrame(world);
			f.pack();
			f.setSize(800, 600);
			f.setVisible(true);			
			*/
			//MyTimer mytime = new MyTimer(null);
			//PApplet.main(new String[] { "dantjbox.dAntWorld" });
			//MyFrame f = new MyFrame(world);
			
			
	}
		catch (Exception e)
		{
			log.debug("error creating application", e);
		}        
    	return super.appStart(appScope);
    }	

	@Override
	public boolean appConnect(IConnection conn, Object[] params)
	{
		log.info("Red5First.appConnect " + conn.getClient().getId());

		boolean accept = (Boolean) params[0];

		if (!accept)
			rejectClient("you passed false...");

		return true;
	}
	

	public void appDisconnect(IConnection conn, Object[] params)
	{
		log.info("Red5First.appDisconnect " + conn.getClient().getId());
		super.appDisconnect(conn);  
	}	
	
/*

	@Override
    public void appDisconnect(IConnection conn)
    {  
		log.debug(conn.toString());
        log.info( "Bye.appStop" );
   		super.appDisconnect(conn);       
    }	
*/

    public String getmyName(){
        return "I don’t Know";
    } 
    
    public String login(String user, String pass)
    {
    	return "hello: " + user + " with password [" + pass + "]";
    }

  
	
//	private static final Log log = LogFactory.getLog( Application.class );

	/*



    public boolean appConnect( IConnection conn , Object[] params )
    {
    	// log.info
       System.out.println( "Red5First.appConnect " + conn.getClient().getId() );

        boolean accept = (Boolean)params[0];

        if ( !accept ) rejectClient( "you passed false..." );

        return true;
    }

    public void appDisconnect( IConnection conn , Object[] params )
    {
        //log.info( "Red5First.appDisconnect " + conn.getClient().getId() );
    }
    */
}