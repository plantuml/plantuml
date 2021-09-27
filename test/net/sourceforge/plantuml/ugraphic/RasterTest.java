package net.sourceforge.plantuml.ugraphic;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_OFF;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.geom.AffineTransform.getTranslateInstance;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import net.sourceforge.plantuml.test.ApprovalTesting;
import sun.java2d.pipe.RenderingEngine;

public class RasterTest {

	//
	// Test Cases
	//

	@Test
	void test_raster_engine() {
		System.setProperty("sun.java2d.renderer.log", "true");
		try {
			RenderingEngine.getInstance();
		} catch (Throwable e) {
		}
		
		System.out.println("java.runtime.name         = " + System.getProperty("java.runtime.name"));
		System.out.println("java.runtime.version      = " + System.getProperty("java.runtime.version"));
		System.out.println("java.vendor               = " + System.getProperty("java.vendor"));
		System.out.println("java.vendor.version       = " + System.getProperty("java.vendor.version"));
		try {
			System.out.println("Rendering Engine          = " + RenderingEngine.getInstance().getClass().getName());
		} catch (Throwable e) {
		}

		try {
			System.out.println("org.marlin.pisces.Version = " + Class.forName("org.marlin.pisces.Version").getMethod("getVersion").invoke(new Object[]{}));
		} catch (Throwable e) {
		}

		try {
			System.out.println("sun.java2d.marlin.Version = " + Class.forName("sun.java2d.marlin.Version").getMethod("getVersion").invoke(new Object[]{}));
		} catch (Throwable e) {
		}

		final BufferedImage image = new BufferedImage(1550, 850, TYPE_INT_RGB);

		final Graphics2D g = image.createGraphics();
		g.setBackground(WHITE);
		g.setColor(BLACK);
		g.clearRect(0, 0, image.getWidth(), image.getHeight());

		g.translate(10, 10);

		for (Object antialias : ANTIALIAS_OPTIONS) {
			g.setRenderingHint(KEY_ANTIALIASING, antialias);

			for (float width : new float[]{0.5f, 1, 1.5f, 2, 3}) {
				g.setStroke(new BasicStroke(width));

				g.drawOval(0, 0, 90, 90);
				g.translate(100, 0);

				g.drawOval(0, 0, 90, 60);
				g.translate(100, 0);

				g.drawOval(0, 0, 90, 30);
				g.translate(100, 0);
			}

			nextRow(g, 110);
		}

		polyline(g, new int[]{0, 20, 20}, new int[]{0, 0, 20});

		polyline(g, new int[]{0, 10, 0}, new int[]{0, 10, 20});

		polyline(g, new int[]{0, 20, 0}, new int[]{0, 10, 20});

		polyline(g, new int[]{0, 40, 0}, new int[]{0, 10, 20});

		approvalTesting.approve(image);
		approvalTesting.approve("x");
		approvalTesting.approve("y", "1");
	}

	//
	// Test DSL
	//

	@RegisterExtension
	private final ApprovalTesting approvalTesting = new ApprovalTesting();

	private static final List<Object> ANTIALIAS_OPTIONS = unmodifiableList(asList(
			VALUE_ANTIALIAS_OFF, VALUE_ANTIALIAS_ON
	));

	private void nextRow(Graphics2D g, int down) {
		g.setTransform(getTranslateInstance(10, g.getTransform().getTranslateY() + down));
	}

	private void polyline(Graphics2D g, int[] xPoints, int[] yPoints) {
		for (int cap_join : new int[]{0, 1, 2}) {
			for (Object antialias : ANTIALIAS_OPTIONS) {
				g.setRenderingHint(KEY_ANTIALIASING, antialias);
				for (float width : new float[]{0.5f, 1, 1.5f, 2, 3, 5, 8, 12, 15}) {
					g.setStroke(new BasicStroke(width, cap_join, cap_join));
					g.drawPolyline(xPoints, yPoints, 3);
					g.translate(70, 0);
				}
			}
			nextRow(g, 50);
		}
	}
}
