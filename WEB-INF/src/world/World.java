package world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.slf4j.Logger;

import ant.Colony;

public class World {

	public static ArrayList<Colony> colonys = new ArrayList<Colony>();
	public static HashMap<Integer, ArrayList<Colony>> colonys_screen = new HashMap<Integer, ArrayList<Colony>>();

	public Logger log;

	public World(Logger _log) {
		this.log = _log;
		log.debug("World is going to be set.");
		try {
			SetupScreens(1);
		} catch (Exception e) {
			log.debug("error creatin world", e);
		}
		Colony c = colonyCreate(new Position(0, 50, 50));
		c.antCreate();
		log.info("World has been created.");
	}

	public void SetupScreens(Integer max_screen) {
		for (Integer i = 0; i < max_screen; ++i)
			colonys_screen.put(i, new ArrayList<Colony>());
	}

	public void lifeColonys(final ArrayList<Colony> colonys) {
		final Iterator<Colony> i = colonys.iterator();
		while (i.hasNext()) {
			final Colony c = i.next();
			c.life();
		}
	}

	public Colony colonyCreate(Position pos) {
		log.debug("Going to create a Colony");
		Colony c = null;
		try {

		} catch (Exception e) {
			log.debug("error creating colony", e);
		}
		return c;
	}

	public void setColonys(final ArrayList<Colony> colonys) {
		World.colonys = colonys;
	}

	public ArrayList<Colony> getColonys() {
		return colonys;
	}

	public void addColonys_screen(final Integer screen, final Colony c) {
		log.debug("Colony added to screen " + screen + ", size : "
				+ colonys_screen.size());
		ArrayList<Colony> colonys = colonys_screen.get(screen);
		colonys.add(c);
	}

	public ArrayList<Colony> getColonys_screen(final Integer screen) {
		return colonys_screen.get(screen);
	}

}
