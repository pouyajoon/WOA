package org.jbox2d.testbed;

import java.util.ArrayList;
import java.util.Iterator;

import org.jbox2d.common.Vec2;

import processing.core.PApplet;
import world.AntBox;
import world.Position;
import ant.Queen;
import food.Food;

class KeyPressEventHandler {
	protected ArrayList<KeyPressEvent> keyPressEvents = new ArrayList<KeyPressEvent>();

	KeyPressEventHandler() {
	}

	/* 
	 * Add an event
	 */
	void addEvent(char key, String keyString, String description,
			KeyPressFunction function) {
		KeyPressEvent keyPressEvent = new KeyPressEvent(key, keyString,
				description, function);
		keyPressEvents.add(keyPressEvent);
	}

	void addEvent(char key, String description, KeyPressFunction function) {
		KeyPressEvent keyPressEvent = new KeyPressEvent(key, key + "",
				description, function);
		keyPressEvents.add(keyPressEvent);
	}

	void addEvent(int keyCode, String keyString, String description,
			KeyPressFunction function) {
		KeyPressEvent keyPressEvent = new KeyPressEvent(keyCode, keyString,
				description, function);
		keyPressEvents.add(keyPressEvent);
	}

	/*
	 * Test the input key vs all possible keys
	 */
	public void test(char key, int keyCode) {
		Iterator<KeyPressEvent> itr = keyPressEvents.iterator();
		while (itr.hasNext()) {
			KeyPressEvent event = itr.next();
			if (event.test(key, keyCode)) {
				// key had been found and function has been called
				return;
			}
		}
	}

	/*
	 * Generate a String explaining key bindings
	 */
	String getString() {
		String desc = "";
		Iterator<KeyPressEvent> itr = keyPressEvents.iterator();
		while (itr.hasNext()) {
			KeyPressEvent event = itr.next();
			if (event.keyString != "" && event.description != null
					&& event.description != "") {
				desc += event.keyString + "    " + event.description + "\n";
			}
		}
		return desc;
	}

	/*
	 * Add all mouse events
	 */
	public void init(final TestbedMain m) {
		addEvent(PApplet.SHIFT, "SHIFT", "", new KeyPressFunction() {
			public void function() {
				m.shiftKey = true;
			}
		});
		addEvent('o', "", new KeyPressFunction() {
			public void function() {
				m.handleOptions = !m.handleOptions;
				if (m.handleOptions)
					m.options.initialize(m.currentTest);
			}
		});
		addEvent(PApplet.RIGHT, "PApplet.RIGHT", "", new KeyPressFunction() {
			public void function() {
				++m.currentTestIndex;
				if (m.currentTestIndex >= m.tests.size())
					m.currentTestIndex = 0;
				System.out.println(m.currentTestIndex);
				m.currentTest = m.tests.get(m.currentTestIndex);
				m.currentTest.needsReset = false;
			}
		});
		addEvent(PApplet.LEFT, "LEFT", "", new KeyPressFunction() {
			public void function() {
				--m.currentTestIndex;
				if (m.currentTestIndex < 0)
					m.currentTestIndex = m.tests.size() - 1;
				m.currentTest = m.tests.get(m.currentTestIndex);
				m.currentTest.needsReset = false;
			}
		});
		addEvent(38, "TOP", "", new KeyPressFunction() {
			public void function() {
				int testIndex = m.currentTestIndex
						- m.antworld.antzone.zone_size;
				if (testIndex < 0)
					m.currentTestIndex = m.tests.size() + testIndex;
				else
					m.currentTestIndex = testIndex;
				System.out.println(testIndex);
				m.currentTest = m.tests.get(m.currentTestIndex);
				m.currentTest.needsReset = false;
			}
		});
		addEvent(40, "BOTTOM", "", new KeyPressFunction() {
			public void function() {
				int testIndexBottom = m.currentTestIndex
						+ m.antworld.antzone.zone_size;
				if (testIndexBottom > m.tests.size() - 1)
					m.currentTestIndex = testIndexBottom - m.tests.size();
				else
					m.currentTestIndex = testIndexBottom;
				m.currentTest = m.tests.get(m.currentTestIndex);
				m.currentTest.needsReset = false;
			}
		});
		addEvent('z', "Zoom in", new KeyPressFunction() {
			public void function() {
				ProcessingDebugDraw d = (ProcessingDebugDraw) (m.currentTest.m_debugDraw);
				Vec2 oldCenter = d.getScreenToWorld(m.width / 2.0f,
						m.height / 2.0f);
				d.scaleFactor = m.min(500f, d.scaleFactor * 1.3f);
				Vec2 newCenter = d.getScreenToWorld(m.width / 2.0f,
						m.height / 2.0f);
				d.transX -= (oldCenter.x - newCenter.x) * d.scaleFactor;
				d.transY -= (oldCenter.y - newCenter.y) * d.scaleFactor;
			}
		});
		addEvent('a', "Zoom out", new KeyPressFunction() {
			public void function() {
				ProcessingDebugDraw d = (ProcessingDebugDraw) (m.currentTest.m_debugDraw);
				Vec2 oldCenter = d.getScreenToWorld(m.width / 2.0f,
						m.height / 2.0f);
				d.scaleFactor = m.min(500f, d.scaleFactor / 1.3f);
				Vec2 newCenter = d.getScreenToWorld(m.width / 2.0f,
						m.height / 2.0f);
				d.transX -= (oldCenter.x - newCenter.x) * d.scaleFactor;
				d.transY -= (oldCenter.y - newCenter.y) * d.scaleFactor;
			}
		});
		addEvent('r', "", new KeyPressFunction() {
			public void function() {
				m.currentTest.needsReset = true;
			}
		});
		addEvent('p', "Pause", new KeyPressFunction() {
			public void function() {
				m.currentTest.settings.pause = !m.currentTest.settings.pause;
			}
		});
		addEvent('+', "Step by step (requires pause)", new KeyPressFunction() {
			public void function() {
				if (m.currentTest.settings.pause) {
					m.currentTest.settings.singleStep = true;
				}
			}
		});
		addEvent('s', "draw/hide stats", new KeyPressFunction() {
			public void function() {
				m.currentTest.settings.drawStats = !m.currentTest.settings.drawStats;
			}
		});
		addEvent('c', "draw/hide contact points", new KeyPressFunction() {
			public void function() {
				m.currentTest.settings.drawContactPoints = !m.currentTest.settings.drawContactPoints;
			}
		});
		addEvent('b', "draw/hide AABBs", new KeyPressFunction() {
			public void function() {
				m.currentTest.settings.drawAABBs = !m.currentTest.settings.drawAABBs;
			}
		});
		addEvent('f', "add food", new KeyPressFunction() {
			public void function() {
				float quantity = (float) (Math.random() * 800f);
				AntBox currentBox = (AntBox) m.currentTest;
				Food food2 = new Food(currentBox,
						new Position(currentBox.id, m.currentTest.mouseWorld.x,
								m.currentTest.mouseWorld.y), quantity);
				m.getAntWorld().foods.add(food2);
			}
		});
		addEvent('q', "add queen", new KeyPressFunction() {
			public void function() {
				AntBox currentBox = (AntBox) m.currentTest;
				new Queen(currentBox,
						new Position(currentBox.id, m.currentTest.mouseWorld.x,
								m.currentTest.mouseWorld.y), m.pouya);
			}
		});
		addEvent('h', "", new KeyPressFunction() {
			public void function() {
				((AntBox) m.currentTest).CreateJCircleBodyStatic(
						new Vec2(m.currentTest.mouseWorld.x,
								m.currentTest.mouseWorld.y), 20, 0, 0.0f);
			}
		});
	}
}