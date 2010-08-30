package world.draw;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.slf4j.Logger;

import com.woa.Application;

public class AntFrameDraw extends Frame {
	private static final long serialVersionUID = 7444731971102972798L;
	public DrawExample embed;
	public Application appli;

	public AntFrameDraw(Logger log, Application _appli) {
		super("Ant World Server View Based on Processing over JBox2D");
		this.appli = _appli;
		setLayout(new BorderLayout());
		embed = new DrawExample();
		add(embed, BorderLayout.CENTER);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// important to call this whenever embedding a PApplet.
		// It ensures that the animation thread is started and
		// that other internal variables are properly set.
		embed.init();
		pack();
		setSize(800, 600);
		setVisible(true);

	}
}
