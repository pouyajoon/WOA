package com.woa;

import java.util.ArrayList;

import org.jbox2d.testbed.AntFrameTestbed;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vnd.woaobject.children.Food;
import vnd.world.AntBox;
import vnd.world.AntWorld;
import vnd.world.Position;

import com.red5.MySecurityHandler;
import com.unicate.DrawList;
import com.unicate.DrawObject;
import com.unicate.info.PlayerInfo;
import com.unicate.info.WOAServerConfigurationInfo;


public class Application extends ApplicationAdapter {
	public Logger log = LoggerFactory.getLogger(Application.class);
	public AntWorld world;
	public IScope appScope;
	public AntFrameTestbed worldframe = null;

	public Application() {
		log.info("Hello Man!");
	}

	// commentaires
	public String createFood(float pos_x, float pos_y, Integer worldBox_id) {
		float size = 120;
		AntBox requestedBox = world.antzone.boxes.get(worldBox_id);
		Food food = new Food(requestedBox, new Position(0, pos_x, pos_y), size);
		world.foods.add(food);
		String msg = "food created on " + food.getPos().toString();
		System.out.println("food created on " + pos_x + ", " + pos_y);
		return msg;
	}

	public PlayerInfo getPlayerInfo(String player_name) {
		return new PlayerInfo(world.players.get(player_name));
	}

	public String when() {
		String msg = "wake up @ : " + world.mytime.worldtime.toString();
		return msg;
	}

	public DrawList get_bodys(Object c) {
		return null;
	}

	public WOAServerConfigurationInfo getServerWorldConfiguration(String user_name)
	{
		WOAServerConfigurationInfo woaci = new WOAServerConfigurationInfo(world.antzone.zone_size, world.antzone.max_screens);
		return woaci;		
	}
	public DrawList get_this_body(Integer id) {
		log.debug("where is this body " + id);
		ArrayList<DrawObject> woalist = new ArrayList<DrawObject>();
		woalist.add(world.woal.getDrawObject(id));
		log.debug("someone need this body : " + id + ", i have found "
				+ woalist.size());
		return new DrawList(woalist);
	}

	/** {@inheritDoc} */
	@Override
	public boolean appStart(IScope appScope) {
		this.appScope = appScope;
		log.info("Hello.appStart");
		registerSharedObjectSecurity(new MySecurityHandler(this));

		try {
			log.debug("World created");
			worldframe = new AntFrameTestbed(log, this);
		} catch (Exception e) {
			log.debug("error creating application", e);
		}
		return super.appStart(appScope);
	}

	@Override
	public boolean appConnect(IConnection conn, Object[] params) {
		log.info("Red5First.appConnect " + conn.getClient().getId());

		boolean accept = (Boolean) params[0];

		if (!accept)
			rejectClient("you passed false...");

		return true;
	}

	public void appDisconnect(IConnection conn, Object[] params) {
		log.info("Red5First.appDisconnect " + conn.getClient().getId());
		super.appDisconnect(conn);
	}

	/*
	 * 
	 * @Override public void appDisconnect(IConnection conn) {
	 * log.debug(conn.toString()); log.info( "Bye.appStop" );
	 * super.appDisconnect(conn); }
	 */

	public String getmyName() {
		return "I don’t Know";
	}

	public String login(String user, String pass) {
		return "hello: " + user + " with password [" + pass + "]";
	}
}
