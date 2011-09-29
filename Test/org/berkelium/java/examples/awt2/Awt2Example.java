package org.berkelium.java.examples.awt2;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.berkelium.java.Berkelium;
import org.berkelium.java.BufferedImageAdapter;
import org.berkelium.java.Window;

public class Awt2Example extends JFrame {
	private static final long serialVersionUID = 8835790859223385092L;
	private final Berkelium runtime = Berkelium.getInstance();
	private final JPanel panel;
	private final Window win = runtime.createWindow();
	private final Window win2 = runtime.createWindow();
	private final BufferedImageAdapter bia = new BufferedImageAdapter();
	private final BufferedImageAdapter bia2 = new BufferedImageAdapter();
	private final int initialWidth = 640;
	private final int initialHeight = 480;
	private final Queue<Runnable> queue = new ConcurrentLinkedQueue<Runnable>();

	public Awt2Example() {
		setTitle("Awt2Example");
		setSize(new Dimension(initialWidth, initialHeight));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		panel = new JPanel() {
			private static final long serialVersionUID = 2923057154675646250L;

			@Override
			public void paint(Graphics g) {
				Awt2Example.this.paint(g, bia);
				Awt2Example.this.paint(g, bia2);
			}
		};
		panel.setDoubleBuffered(true);
		add(panel);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				handleMouseButtonEvent(e, false);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				handleMouseButtonEvent(e, true);
			}
		});
	}

	private void handleMouseButtonEvent(MouseEvent e, final boolean down) {
		final BufferedImage bi = bia.getImage();
		if (bia == null)
			return;
		final int x = e.getX() * bi.getWidth() / getWidth();
		final int y = e.getY() * bi.getHeight() / getHeight();
		final int b = e.getButton();

		// the event must be handled in the berkelium thread
		queue.add(new Runnable() {
			@Override
			public void run() {
				win.mouseMoved(x, y);
				win.mouseButton(b, down);
			}
		});
	}

	public void paint(Graphics g, BufferedImageAdapter bia) {
		BufferedImage img = bia.getImage();
		if (img != null) {
			// do not allow updates to the image while we draw it
			synchronized (bia) {
				g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
			}
		}
	}

	public void run() throws Exception {
		synchronized (runtime) {
			win.setDelegate(bia);
			bia.resize(initialWidth, initialHeight);
			win.resize(initialWidth, initialHeight);
			win.navigateTo("http://upload.wikimedia.org/wikipedia/commons/b/b5/I-15bis.ogg");

			win2.setTransparent(true);
			win2.setDelegate(bia2);
			bia2.resize(initialWidth, initialHeight);
			win2.resize(initialWidth, initialHeight);
			win2.navigateTo("http://jensbeimsurfen.de/ping-pong/");
		}

		while (isVisible()) {
			while (!queue.isEmpty()) {
				queue.remove().run();
			}
			synchronized (runtime) {
				runtime.update();
			}
			if (bia.wasUpdated() || bia2.wasUpdated()) {
				repaint();
			}
			Thread.sleep(10);
		}
	}

	public static void main(String[] args) throws Exception {
		try {
			System.out.println("initializing berkelium-java...");
			Berkelium.createInstance();
			System.out.println("running main loop...");
			new Awt2Example().run();
			System.out.println("main loop terminated.");
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			System.out.println("destroying berkelium-java...");
			Berkelium.getInstance().destroy();
			System.out.println("berkelium-java destroyed.");
			System.exit(0);
		}
	}
}