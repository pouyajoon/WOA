package vnd.world.draw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import processing.core.PApplet;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

public class DrawExample extends PApplet {
	private static final long serialVersionUID = -3074157657787604091L;

	static public void main(String args[]) {
		Logger local_log = LoggerFactory.getLogger(DrawExample.class);
		local_log.info("Server.Hello.DrawOnly");
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		StatusPrinter.print(lc);
		new AntFrameDraw(local_log, null);
	}

	public void setup() {
		/*
		 * On newer machines especially, the default JAVA2D renderer is slow as
		 * hell and tends to drop frames. I have no idea why, but for now let's
		 * use P3D and live without the smoothing...
		 */
		size(800, 600, P3D);
		frameRate(60.0f);

	}

	public void draw() {

	}

}
