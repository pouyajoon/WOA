/*
 * JBox2D - A Java Port of Erin Catto's Box2D
 * 
 * JBox2D homepage: http://jbox2d.sourceforge.net/ 
 * Box2D homepage: http://www.box2d.org
 * 
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented; you must not
 * claim that you wrote the original software. If you use this software
 * in a product, an acknowledgment in the product documentation would be
 * appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 * misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package org.jbox2d.testbed;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.DebugDraw;
import org.red5.server.api.service.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import players.Player;
import processing.core.PApplet;
import woaobject.WOAObject;
import woaobject.pheromone.NodePheromone;
import woaobject.track.GraphTrack;
import woaobject.track.TrackNode;
import world.AntBox;
import world.AntWorld;
import world.Position;
import ant.Queen;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

import com.woa.Application;

import food.Food;

/**
 * TestbedMain is the holder PApplet for the entire testbed. It has first stab
 * at input events, and delegates them out to the appropriate handlers if
 * necessary. It handles the list of tests to run, and starts them as needed. <BR>
 * <BR>
 * For applet safety, no variables are static, because in the browser a page
 * reload does <em>not</em> reset static variables (even though the applet may
 * get restarted otherwise). From a design perspective this is not ideal, but
 * hey - this is just a demo, anyways, let's not get too bent out of shape over
 * it. <BR>
 * <BR>
 * In the future, other classes should be checked to make sure this odd static
 * variable behavior is not causing memory leaks in the browser.
 * 
 * @author ewjordan
 * 
 */
public class TestbedMain extends PApplet {
	/** I let Eclipse generate this to shut it up about its warnings. */
	private static final long serialVersionUID = 1712524774634907635L;
	/** The list of registered examples */
	protected ArrayList<AbstractExample> tests = new ArrayList<AbstractExample>(
			0);
	/** Currently running example */
	public AbstractExample currentTest = null;
	/**
	 * Index of current example in tests array. Assumes that the array structure
	 * does not change, though it's safe to add things on to the end.
	 */
	protected int currentTestIndex = 0;
	/** Is the options window open? */
	protected boolean handleOptions = false;

	// Little bit of input stuff
	// TODO ewjordan: refactor into an input handler class
	/** Is the shift key held? */
	public boolean shiftKey = false;

	/** Was the mouse down last frame? */
	boolean pmousePressed = false;

	/**
	 * Our options handler - displays GUI and sets TestSettings for currentTest.
	 */
	public TestbedOptions options;

	// Processing handles fps pinning, but we
	// report fps on our own just to be sure.

	/** FPS that we want to achieve */
	final static float targetFPS = 60.0f;
	/** Number of frames to average over when computing real FPS */
	final int fpsAverageCount = 100;
	/** Array of timings */
	long[] nanos;
	/** When we started the nanotimer */
	long nanoStart; //

	/** Number of frames since we started this example. */
	long frameCount = 0;

	/** Drawing handler to use. */
	public DebugDraw g;

	public Logger log = null;
	private Application appli = null;
	public AntWorld antworld = null;
	public KeyPressEventHandler keyPressEventHandler = new KeyPressEventHandler();
	String keyBindingsString;

	// temp user created for tests purposes
	public Player pouya = new Player("pouya");

	/** Constructor - real initialization happens in setup() function */
	public TestbedMain(Logger _log, Application _appli) {
		super();
		log = _log;
		this.appli = _appli;
		// init key bindings
		keyPressEventHandler.init(this);
		keyBindingsString = keyPressEventHandler.getString();
		log.debug("testbedmain done... appli: " + this.appli);
	}

	public AntWorld getAntWorld() {
		if (appli != null) {
			return appli.world;
		} else {
			return antworld;
		}
	}

	/**
	 * Called once upon program initialization (by Processing). Here we set up
	 * graphics, set the framerate, register all the testbed examples, set up a
	 * mousewheel listener, and set up the frame rate timer.
	 */
	public void setup() {
		/*
		 * On newer machines especially, the default JAVA2D renderer is slow as
		 * hell and tends to drop frames. I have no idea why, but for now let's
		 * use P3D and live without the smoothing...
		 */
		size(800, 600, P3D);
		frameRate(targetFPS);
		g = new ProcessingDebugDraw(this);

		// smooth();
		for (int i = 0; i < 100; ++i) {
			this.requestFocus();
		}
		/* Register the examples */
		// Simple functionality examples

		// registerExample(new Domino(this));
		ProcessingDebugDraw d = (ProcessingDebugDraw) g;
		d.transX = 30;
		d.transY = 90;
		d.scaleFactor = 0.3f;

		if (appli != null) {
			appli.world = new AntWorld(log, appli, this);
			antworld = appli.world;
			// registerExample(appli.world.ab);
		} else {
			antworld = new AntWorld(log, null, this);
			// AntWorld antworld2 = new AntWorld(log, null, this);
			// antworld2.initialize();
			// registerExample(antworld.ab);
			// registerExample(antworld2);
			// for (int i = 0; i < antworld.antzone.max_screens; i++)
			{
				// registerExample(antworld.antzone.boxes.get(i));
			}
		}
		// Set up the mouse wheel listener to control zooming
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (currentTest != null) {
					ProcessingDebugDraw d = (ProcessingDebugDraw) (currentTest.m_debugDraw);
					int notches = e.getWheelRotation();
					Vec2 oldCenter = d.getScreenToWorld(width / 2.0f,
							height / 2.0f);
					// Change the zoom and clamp it to reasonable values
					// System.out.println("scalefactor : " + d.scaleFactor);
					if (notches < 0) {
						d.scaleFactor = min(500f, d.scaleFactor * 1.3f);
					} else if (notches > 0) {
						d.scaleFactor = max(.01f, d.scaleFactor / 1.3f);
					}
					Vec2 newCenter = d.getScreenToWorld(width / 2.0f,
							height / 2.0f);
					d.transX -= (oldCenter.x - newCenter.x) * d.scaleFactor;
					d.transY -= (oldCenter.y - newCenter.y) * d.scaleFactor;

					// NEEDCHECK
					// currentTest.cachedCamScale = d.scaleFactor;
					// System.out.println("old center : " + oldCenter.toString()
					// + ", new center =" + newCenter.toString());
				}
			}
		});

		/* Set up the timers for FPS reporting */
		nanos = new long[fpsAverageCount];
		long nanosPerFrameGuess = (long) (1000000000.0 / targetFPS);
		nanos[fpsAverageCount - 1] = System.nanoTime();
		for (int i = fpsAverageCount - 2; i >= 0; --i) {
			nanos[i] = nanos[i + 1] - nanosPerFrameGuess;
		}
		nanoStart = System.nanoTime();

		options = new TestbedOptions(this);
	}

	public void postStep() {
		try {
			// remove the objects outside of the border
			Iterator<WOAObject> vb = antworld.hasViolateBoundary.values()
					.iterator();
			while (vb.hasNext()) {
				final WOAObject w = vb.next();

				w.boundaryViolated();
				// remove from the list
				vb.remove();
			}

			Iterator<WOAObject> i = antworld.garbage.values().iterator();
			while (i.hasNext()) {
				final WOAObject w = i.next();

				if (appli != null && appli.appScope != null) {
					// ServiceUtils serverutils = new ServiceUtils();
					ServiceUtils.invokeOnAllConnections(appli.appScope,
							"diedOnServer", new Object[] { w.id });
				}

				// destroy the woaobject
				w.finalize();

				// remove from garbage
				i.remove();
			}

			Iterator<GraphTrack> gti = antworld.graphtracks.iterator();
			while (gti.hasNext()) {
				GraphTrack gt = gti.next();
				if (!gt.stillInUsed()) {
					gt.finalize();
					gti.remove();
				} else {
					Iterator<TrackNode> tni = gt.nodes.iterator();
					while (tni.hasNext()) {
						TrackNode tn = tni.next();
						if (tn.toHome == null) {
							if (tn.toTargets.size() == 0) {
								if (tn.woao.type.equals("pheromone")) {
									tn.woao.addToGarbage();
									tni.remove();
								}
							}
						}
					}
				}

			}

			/*
			 * for (WOAObject woaobject : garbage.values()) {
			 * woaobject.finalize(); //garbage.remove(woaobject); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void preStep() {
		try {

			Iterator<WOAObject> woaoi = antworld.woal.all_objects.values()
					.iterator();
			while (woaoi.hasNext()) {
				WOAObject woao = woaoi.next();
				woao.life();
			}
			// {
			// }

			// life of colonies (include ants)
			// antworld.lifeColonys(antworld.getColonys());
			// life of foods
			// for (Food f : antworld.foods)
			// {
			// f.life();
			// }
			// // life of pheromones
			// System.out.println("node pheromones size : " +
			// antworld.nodepheromones.size());
			// for (NodePheromone p : antworld.nodepheromones)
			{
				// System.out.println(p.toString());
			}

			// life of graph tracks
			Iterator<GraphTrack> i = antworld.graphtracks.iterator();
			while (i.hasNext()) {
				GraphTrack gt = i.next();
				gt.life();

			}

		} catch (ConcurrentModificationException c) {
			antworld.log
					.debug("concurrent modification exception catch on creating a new object while iterating the big list");
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// woal.so.endUpdate();

		// log.debug("body : " + antBox.myBody.getPosition().toString());
	}

	/**
	 * This is the main looping function, and is called targetFPS times per
	 * second. In the testbed, Processing takes care of the timing of these
	 * calls for us, but in your own game you will likely need to handle that
	 * yourself. This function also keeps detailed track of the current FPS and
	 * Vec2 creations for optimization purposes.
	 */
	public void draw() {

		if (handleOptions) {
			options.handleOptions();
		} else {
			background(0);
			Vec2.creationCount = 0;

			/* Make sure we've got a valid test to run and reset it if needed */

			if (currentTest == null) {
				currentTestIndex = 0;
				currentTest = tests.get(currentTestIndex);
				nanoStart = System.nanoTime();
				frameCount = 0;
			}

			if (currentTest.needsReset) {
				// System.out.println("Resetting "+currentTest.getName());
				TestSettings s = currentTest.settings; // copy settings
				currentTest.initialize();
				if (s != null)
					currentTest.settings = s;
				nanoStart = System.nanoTime();
				frameCount = 0;
			}

			currentTest.m_textLine = AbstractExample.textLineHeight;
			g.drawString(5, currentTest.m_textLine, currentTest.getName(),
					AbstractExample.white);
			currentTest.m_textLine += 2 * AbstractExample.textLineHeight;

			/* Take our time step (drawing is done here, too) */
			// ProcessingDebugDraw d = (ProcessingDebugDraw)(g);
			// d.setCamera(0, 0, 0.1f);

			((AntBox) currentTest).drawEarth();
			preStep();

			for (AbstractExample test : tests) {
				test.m_world.setAutoDebugDraw(false);
				if (test.needsReset == false) {
					test.step();

					if (test.equals(currentTest)) {
						currentTest.m_world.drawDebugData();
						// currentTest.step();
						// currentTest.m_world.drawDebugData();
						// currentTest.m_world.setAutoDebugDraw(false);
					} else {
						// m_world.setAutoDebugDraw(false);
						// float timeStep = test.settings.hz > 0.0f ? 1.0f /
						// test.settings.hz : 0.0f;
						// test.m_world.step(timeStep,
						// test.settings.iterationCount);
					}

				} else {
					test.initialize();
					test.needsReset = false;
				}
			}

			// currentTest.step();
			postStep();

			/* If the user wants to move the canvas, do it */
			handleCanvasDrag();

			/* ==== Vec2 creation and FPS reporting ==== */
			if (currentTest.settings.drawStats) {
				g.drawString(5, currentTest.m_textLine,
						"Vec2 creations/frame: " + Vec2.creationCount,
						AbstractExample.white);
				currentTest.m_textLine += AbstractExample.textLineHeight;
			}

			for (int i = 0; i < fpsAverageCount - 1; ++i) {
				nanos[i] = nanos[i + 1];
			}
			nanos[fpsAverageCount - 1] = System.nanoTime();
			float averagedFPS = (float) ((fpsAverageCount - 1) * 1000000000.0 / (nanos[fpsAverageCount - 1] - nanos[0]));
			++frameCount;
			float totalFPS = (float) (frameCount * 1000000000 / (1.0 * (System
					.nanoTime() - nanoStart)));
			if (currentTest.settings.drawStats) {
				g.drawString(5, currentTest.m_textLine, "Average FPS ("
						+ fpsAverageCount + " frames): " + averagedFPS,
						AbstractExample.white);
				currentTest.m_textLine += AbstractExample.textLineHeight;
				g.drawString(5, currentTest.m_textLine,
						"Average FPS (entire test): " + totalFPS,
						AbstractExample.white);
				currentTest.m_textLine += AbstractExample.textLineHeight;
			}
		}

		/* Store whether the mouse was pressed this step */
		pmousePressed = mousePressed;
	}

	/**
	 * Allows the world to be dragged with a right-click.
	 */
	public void handleCanvasDrag() {
		// Handle mouse dragging stuff
		// Left mouse attaches mouse joint to object.
		// Right mouse drags canvas.
		ProcessingDebugDraw d = (ProcessingDebugDraw) (currentTest.m_debugDraw);

		// Vec2 mouseWorld = d.screenToWorld(mouseX, mouseY);
		if (mouseButton == RIGHT) {
			if (mousePressed) {
				d.transX += mouseX - pmouseX;
				d.transY -= mouseY - pmouseY;
				Vec2 v = d.getScreenToWorld(width * .5f, height * .5f);
				// currentTest.cachedCamX = v.x;
				// currentTest.cachedCamY = v.y;
			}
		}

	}

	/** Dispatch mousePressed events to the current test. */
	public void mousePressed() {
		if (currentTest == null || handleOptions)
			return;
		currentTest.mouseDown(new Vec2(mouseX, mouseY));
	}

	/** Dispatch mouseReleased events to the current test. */
	public void mouseReleased() {
		if (currentTest == null || handleOptions)
			return;
		currentTest.mouseUp();
	}

	/** Dispatch mouseMoved events to the current test. */
	public void mouseMoved() {
		if (currentTest == null || handleOptions)
			return;
		currentTest.mouseMove(new Vec2(mouseX, mouseY));
	}

	/** Dispatch mouseDragged events to the current test. */
	public void mouseDragged() {
		mouseMoved();
	}

	/**
	 * Apply keyboard shortcuts, do keypress handling, and then send the key
	 * event to the current test if appropriate.
	 */
	public void keyPressed() {
		keyPressEventHandler.test(key, keyCode);
		if (handleOptions)
			return;
		if (currentTest == null)
			return;
		currentTest.keyPressed(key);
	}

	/** Handle keyReleased events and pass them on to currentTest. */
	public void keyReleased() {
		if (keyCode == PApplet.SHIFT) {
			shiftKey = false;
		}
		if (currentTest == null)
			return;
		currentTest.keyReleased(key);
	}

	/** Register an AbstractExample to the current list of examples. */
	public void registerExample(AbstractExample test) {
		tests.add(test);
	}

	/** Start PApplet as a Java program (can also be run as an applet). */
	static public void main(String args[]) {

		Logger local_log = LoggerFactory.getLogger(TestbedMain.class);
		local_log.info("Server.Hello");
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		StatusPrinter.print(lc);

		// Application a = new Application();
		new AntFrameTestbed(local_log, null);

		// PApplet.main(new String[] { "org.jbox2d.testbed.TestbedMain" });
	}

}
