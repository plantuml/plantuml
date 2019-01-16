/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import net.sourceforge.plantuml.version.PSystemVersion;

public class Splash extends Window implements MouseListener, MouseMotionListener {

	private static final Color LINK_NORMAL = Color.BLUE;
	private static final Color LINK_HOVER = new Color(127, 0, 127);

	private static Splash singleton;

	private final int width = 280;
	private final int height = 80;
	private final BufferedImage logo;
	private final AtomicInteger total = new AtomicInteger();
	private final AtomicInteger done = new AtomicInteger();
	private final AtomicInteger errors = new AtomicInteger();
	private final Font font = new Font("SansSerif", Font.BOLD, 12);

	public static void main(String[] args) throws Exception {
		Splash.createSplash();
		Splash.incTotal(30);
		for (int i = 0; i < 20; i++) {
			Splash.incDone(i > 3);
			Thread.sleep(1000);
		}
	}

	private Splash() {
		super(null);
		this.logo = PSystemVersion.getPlantumlImage();
		final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = (int) ((dimension.getWidth() - width)) / 2;
		final int y = (int) ((dimension.getHeight() - height)) / 2;

		this.setBounds(x, y, width, height);
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		this.setFocusable(false);
		this.setFocusableWindowState(false);

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	private int xClicked;
	private int yClicked;
	private int limY;
	private int limX;

	public void mouseDragged(MouseEvent event) {
		int x = event.getXOnScreen();
		int y = event.getYOnScreen();
		this.setBounds(x - xClicked, y - yClicked, width, height);

	}

	private Color link = LINK_NORMAL;

	public void mouseMoved(MouseEvent event) {
		if (event.getY() > limY && event.getX() < limX) {
			updateLinkColor(LINK_HOVER);
		} else {
			updateLinkColor(LINK_NORMAL);
		}
	}

	private void updateLinkColor(final Color newLink) {
		if (link != newLink) {
			link = newLink;
			this.setCursor(link == LINK_NORMAL ? Cursor.getDefaultCursor() : Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			repaint();
		}
	}

	public void mouseReleased(MouseEvent event) {
		// System.err.println("mouseReleased " + event);
	}

	public void mousePressed(MouseEvent event) {
		this.xClicked = event.getX();
		this.yClicked = event.getY();
	}

	public void mouseClicked(MouseEvent event) {
		if (link != LINK_NORMAL) {
			try {
				Desktop.getDesktop().browse(new URL("http://plantuml.com").toURI());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		this.xClicked = event.getX();
		this.yClicked = event.getY();
	}

	public void mouseExited(MouseEvent event) {
		updateLinkColor(LINK_NORMAL);
	}

	public void mouseEntered(MouseEvent event) {
		// System.err.println("mouseEntered " + event);
	}

	private void incTotalInternal(int nb) {
		this.total.addAndGet(nb);
		// invalidate();
		repaint();
		// System.err.println("INC TOTAL=" + done + "/" + total);
	}

	private void incDoneInternal(boolean error) {
		this.done.incrementAndGet();
		if (error) {
			this.errors.incrementAndGet();
		}
		// invalidate();
		repaint();
		// System.err.println("INC TOTAL=" + done + "/" + total);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);
		g.drawRect(1, 1, width - 3, height - 3);
		// g.setColor(Color.RED);
		// final String status = done + "/" + total;
		// g.drawString(status, width / 2, height / 2);
		g.drawImage(logo, width - logo.getWidth() - 4, height - logo.getHeight() - 4, null);
		drawProgessBar(g, done.intValue(), total.intValue());
		final int nbErrors = errors.get();
		if (nbErrors > 0) {
			g.setColor(Color.RED);
			final String message = "" + nbErrors + (nbErrors > 1 ? " diagrams" : " diagram") + " contains errors";
			g.drawString(message, 10, 20);
		}
		g.setColor(link);
		final String urllink = "http://plantuml.com";
		final Rectangle2D rect = getUsed(g, urllink);
		g.drawString(urllink, 10, (int) (height - rect.getMaxY()));
		limY = (int) (height - rect.getMaxY() + rect.getMinY());
		limX = (int) (10 + rect.getMaxX());

	}

	private void drawProgessBar(Graphics g, int intValue, int totalValue) {
		if (totalValue == 0) {
			return;
		}
		g.setFont(font);
		final String label = "" + intValue + "/" + totalValue;
		final Rectangle2D rect = getUsed(g, label);

		final int x = 10;
		final int y = 33;
		final int barWidth = 170;
		final int barHeight = (int) (rect.getHeight() + 2);
		final int grey = 230;
		g.setColor(new Color(grey, grey, grey));
		final int value = barWidth * intValue / totalValue;
		g.fillRect(x, y, value, barHeight);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, barWidth, barHeight);

		final double xLabel = x + (barWidth - rect.getWidth()) / 2;
		final double yLabel = y - rect.getY() + (barHeight - rect.getHeight()) / 2;
		g.drawString(label, (int) xLabel, (int) yLabel);
	}

	private Rectangle2D getUsed(Graphics g, final String label) {
		return g.getFontMetrics().getStringBounds(label, g);
	}

	// Static

	public synchronized static void createSplash() {
		if (singleton == null) {
			singleton = new Splash();
		}
	}

	public static void incTotal(int nb) {
		if (singleton != null) {
			singleton.incTotalInternal(nb);
		}
	}

	public static void incDone(boolean error) {
		if (singleton != null) {
			singleton.incDoneInternal(error);
		}
	}

	public synchronized static void disposeSplash() {
		if (singleton != null) {
			singleton.setVisible(false);
			singleton.dispose();
			singleton = null;
		}
	}

}
