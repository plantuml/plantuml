package test.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static test.utils.ImageTestUtils.assertImageSizeEqual;
import static test.utils.ImageTestUtils.assertImagesEqual;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

class ImageTestUtilsTest {

	@Test
	void test_assertImagesEqual_same() {
		final BufferedImage expected = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		expected.setRGB(0, 0, 0xFFFF0000);
		expected.setRGB(1, 1, 0xFF00FF00);
		expected.setRGB(2, 2, 0xFF0000FF);
		expected.setRGB(3, 3, 0xAA000000);

		final BufferedImage actual = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		actual.setRGB(0, 0, 0xFFFF0000);
		actual.setRGB(1, 1, 0xFF00FF00);
		actual.setRGB(2, 2, 0xFF0000FF);
		actual.setRGB(3, 3, 0xAA000000);

		try {
			assertImagesEqual(expected, actual);
		} catch (Throwable t) {
			fail("assertImagesEqual() should not throw an exception here", t);
		}
	}

	@Test
	void test_assertImagesEqual_different() {
		final BufferedImage expected = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

		final BufferedImage actual = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		actual.setRGB(1, 2, 0xAAFF0000);

		try {
			assertImagesEqual(expected, actual);
		} catch (AssertionFailedError e) {
			assertThat(e)
					.hasMessage("expected:ColorHSB[a=00 r=00 g=00 b=00 / h=0.000000 s=0.000000 b=0.000000]" +
							" but was:ColorHSB[a=AA r=FF g=00 b=00 / h=0.000000 s=1.000000 b=1.000000] at:<[1, 2]>");
			assertThat(e.isExpectedDefined())
					.isTrue();
			assertThat(e.isActualDefined())
					.isTrue();
			assertThat(e.getExpected().getStringRepresentation())
					.isEqualTo("ColorHSB[a=00 r=00 g=00 b=00 / h=0.000000 s=0.000000 b=0.000000]");
			assertThat(e.getActual().getStringRepresentation())
					.isEqualTo("ColorHSB[a=AA r=FF g=00 b=00 / h=0.000000 s=1.000000 b=1.000000] at:<[1, 2]>");
			return;
		}
		fail("AssertionFailedError expected");
	}

	@Test
	void test_assertImageSizeEqual_same() {
		try {
			assertImageSizeEqual(
					new BufferedImage(10, 20, BufferedImage.TYPE_BYTE_GRAY),
					new BufferedImage(10, 20, BufferedImage.TYPE_INT_ARGB)
			);
		} catch (Throwable t) {
			fail("assertImagesSameSize() should not throw an exception here", t);
		}
	}

	@Test
	void test_assertImageSizeEqual_different() {
		try {
			assertImageSizeEqual(
					new BufferedImage(10, 20, BufferedImage.TYPE_BYTE_GRAY),
					new BufferedImage(11, 22, BufferedImage.TYPE_INT_ARGB)
			);
		} catch (AssertionFailedError e) {
			assertThat(e)
					.hasMessage("expected:[width=10 height=20] but was:[width=11 height=22]");
			assertThat(e.isExpectedDefined())
					.isTrue();
			assertThat(e.isActualDefined())
					.isTrue();
			assertThat(e.getExpected().getStringRepresentation())
					.isEqualTo("[width=10 height=20]");
			assertThat(e.getActual().getStringRepresentation())
					.isEqualTo("[width=11 height=22]");
			return;
		}
		fail("AssertionFailedError expected");
	}
}
