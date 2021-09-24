package net.sourceforge.plantuml.ugraphic;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.geom.AffineTransform.getTranslateInstance;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import sun.java2d.pipe.RenderingEngine;

public class RasterTest {

	//
	// Test Cases
	//

	@Test
	void test_rendering_engine_class() {
		assertThat(RenderingEngine.getInstance().getClass().getName())
				.isEqualTo("sun.java2d.marlin.DMarlinRenderingEngine");
	}

	@Test
	void test_raster_primitives() throws Exception {
		final BufferedImage image = new BufferedImage(1600, 1200, TYPE_INT_RGB);

		final Graphics2D g2d = image.createGraphics();
		g2d.setBackground(WHITE);
		g2d.setColor(BLACK);
		g2d.clearRect(0, 0, image.getWidth(), image.getHeight());

		g2d.translate(10, 10);

		for (Object hint : new Object[]{RenderingHints.VALUE_ANTIALIAS_OFF, RenderingHints.VALUE_ANTIALIAS_ON}) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, hint);

			for (float width : new float[]{0.5f, 1, 1.5f, 2, 3}) {
				g2d.setStroke(new BasicStroke(width));

				g2d.drawOval(0, 0, 90, 90);
				g2d.translate(100, 0);

				g2d.drawOval(0, 0, 90, 60);
				g2d.translate(100, 0);

				g2d.drawOval(0, 0, 90, 30);
				g2d.translate(100, 0);
			}

			nextRow(g2d, 110);
		}

		polyline(g2d, new int[]{0, 20, 20}, new int[]{0, 0, 20});

		polyline(g2d, new int[]{0, 10, 0}, new int[]{0, 10, 20});

		polyline(g2d, new int[]{0, 20, 0}, new int[]{0, 10, 20});

		polyline(g2d, new int[]{0, 40, 0}, new int[]{0, 10, 20});

		// TODO
		// image

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "BMP", baos);
		byte[] bytes = baos.toByteArray();

		Files.write(Paths.get("target/raster-test-output.bmp"), bytes);

		assertThat(getClass().getResourceAsStream("/raster-test-reference.bmp"))
				.hasBinaryContent(bytes);

	}

	//
	// Test DSL
	//

	private void nextRow(Graphics2D g2d, int down) {
		g2d.setTransform(getTranslateInstance(10, g2d.getTransform().getTranslateY() + down));
	}

	private void polyline(Graphics2D g2d, int[] xPoints, int[] yPoints) {
		for (int cap_join : new int[]{0, 1, 2}) {
			for (Object hint : new Object[]{RenderingHints.VALUE_ANTIALIAS_OFF, RenderingHints.VALUE_ANTIALIAS_ON}) {
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, hint);
				for (float width : new float[]{0.5f, 1, 1.5f, 2, 3, 5, 8, 12, 15}) {
					g2d.setStroke(new BasicStroke(width, cap_join, cap_join));
					g2d.drawPolyline(xPoints, yPoints, 3);
					g2d.translate(70, 0);
				}
			}
			nextRow(g2d, 50);
		}
	}
}
