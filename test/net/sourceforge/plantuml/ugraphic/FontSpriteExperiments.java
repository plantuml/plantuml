package net.sourceforge.plantuml.ugraphic;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.nio.file.Files.createDirectories;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class FontSpriteExperiments {

	//
	// Test Cases
	//

	@MyTest
	void arc(String param) {
		g2d.drawArc(10, 10, 80, 80, 0, 180);
	}

//	@MyTest
//	void image() {
//		g2d.drawImage(todo);
//	}
//
	@MyTest
	void line(String param) {
		g2d.drawLine(10, 10, 90, 70);
	}

	@MyTest
	void oval(String param) {
		g2d.drawOval(10, 10, 80, 40);
	}

	@MyTest
	void polygon(String param) {
		g2d.drawPolygon(new int[]{30, 10, 40, 90, 70, 50}, new int[]{80, 10, 40, 20, 80, 20}, 6);
	}

	@MyTest
	void polyline(String param) {
		g2d.drawPolyline(new int[]{30, 10, 40, 90, 70}, new int[]{80, 10, 40, 20, 80}, 5);
	}

	@MyTest
	void rect(String param) {
		g2d.drawRect(10, 10, 80, 80);
	}

	@MyTest
	void roundRect(String param) {
		g2d.drawRoundRect(10, 10, 80, 40, 10, 20);
	}

	//
	// Plumbing
	//

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@ParameterizedTest(name = ParameterizedTest.ARGUMENTS_PLACEHOLDER)
	@ValueSource(strings = {"1px_antialias", "1px", "3px", "3px_antialias"})
	@interface MyTest {
	}

	final BufferedImage image = new BufferedImage(100, 100, TYPE_INT_RGB);

	final Graphics2D g2d = image.createGraphics();

	File file;

	@BeforeEach
	void beforeEach(TestInfo testInfo) throws Exception {
		final String displayName = testInfo.getDisplayName();

		if (displayName.contains("1px")) {
			g2d.setStroke(new BasicStroke(1));
		} else if (displayName.contains("3px")) {
			g2d.setStroke(new BasicStroke(3));
		}

		if (displayName.contains("antialias")) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}

		final String filename = testInfo.getTestMethod().get().getName() + "_" + displayName + ".bmp";
		file = createDirectories(Paths.get("target/images-created")).resolve(filename).toFile();

		g2d.setBackground(WHITE);
		g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
		g2d.setColor(BLACK);
	}

	@AfterEach
	void afterEach() throws Exception {
		ImageIO.write(image, "BMP", file);

		final File reference = Paths.get("testResources/images-reference").resolve(file.getName()).toFile();

		Assertions.assertThat(file)
				.hasSameBinaryContentAs(reference);
	}
}
