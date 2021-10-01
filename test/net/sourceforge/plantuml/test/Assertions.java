package net.sourceforge.plantuml.test;

import static java.lang.Math.abs;

import java.awt.image.BufferedImage;
import java.util.Objects;

import org.opentest4j.AssertionFailedError;

public class Assertions {

	public static void assertImagesEqual(BufferedImage expected, BufferedImage actual) {
		assertImagesEqualWithinTolerance(expected, actual, 0);
	}

	@SuppressWarnings("PointlessBitwiseExpression")
	public static void assertImagesEqualWithinTolerance(BufferedImage expected, BufferedImage actual, int tolerance) {
		Objects.requireNonNull(expected);
		Objects.requireNonNull(actual);

		final int expectedHeight = expected.getHeight();
		final int expectedWidth = expected.getWidth();

		final int actualHeight = actual.getHeight();
		final int actualWidth = actual.getWidth();

		if (expectedHeight != actualHeight || expectedWidth != actualWidth) {
			assertionFailed(
					String.format("[width=%d height=%d]", expectedWidth, expectedHeight),
					String.format("[width=%d height=%d]", actualWidth, actualHeight)
			);
		}

		for (int x = 0; x < expectedWidth; x++) {
			for (int y = 0; y < expectedHeight; y++) {
				final int expectedARGB = expected.getRGB(x, y);
				final int expectedA = (expectedARGB >> 24) & 0xFF;
				final int expectedR = (expectedARGB >> 16) & 0xFF;
				final int expectedG = (expectedARGB >> 8) & 0xFF;
				final int expectedB = (expectedARGB >> 0) & 0xFF;

				final int actualARGB = actual.getRGB(x, y);
				final int actualA = (actualARGB >> 24) & 0xFF;
				final int actualR = (actualARGB >> 16) & 0xFF;
				final int actualG = (actualARGB >> 8) & 0xFF;
				final int actualB = (actualARGB >> 0) & 0xFF;

				if (abs(expectedA - actualA) > tolerance
						|| abs(expectedR - actualR) > tolerance
						|| abs(expectedG - actualG) > tolerance
						|| abs(expectedB - actualB) > tolerance
				) {
					assertionFailed(
							String.format("[r=%d g=%d b=%d a=%d]",
									expectedR, expectedG, expectedB, expectedA),
							String.format("[r=%d g=%d b=%d a=%d] at:<[%d, %d]> using tolerance:%d",
									actualR, actualG, actualB, actualA, x, y, tolerance)
					);
				}
			}
		}
	}

	// We use AssertionFailedError because some IDEs can nicely show the "expected" & "actual" details
	private static void assertionFailed(String expected, String actual) {
		final String message = String.format("expected:%s but was:%s", expected, actual);
		throw new AssertionFailedError(message, expected, actual);
	}
}
