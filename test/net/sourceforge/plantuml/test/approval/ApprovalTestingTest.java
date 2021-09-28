package net.sourceforge.plantuml.test.approval;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@ExtendWith(ApprovalTestingJUnitExtension.class)
class ApprovalTestingTest {

	private ApprovalTesting approvalTesting;

	@Test
	void test_approveImage() {
		final BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		image.createGraphics().drawRect(2, 3, 5, 3);
		approvalTesting.approveImage(image);
	}

	@Test
	void test_approveImage_bmp() {
		final BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		image.createGraphics().drawRect(2, 3, 5, 3);
		approvalTesting.withExtension(".bmp").approveImage(image);
	}

	@Test
	void test_approveString() {
		approvalTesting.approveString("foo");
	}

	@ParameterizedTest(name = "{arguments}")
	@CsvSource({
			"foo, 1",
			"bar, 2",
	})
	void test_parameterized(String s, int i) {
		approvalTesting.approveString(s + i);
	}

	@Test
	void test_withExtension() {
		approvalTesting.withExtension(".foo").approveString("foo");
	}

	@Test
	void test_withSuffix() {
		approvalTesting.withSuffix("-bar").approveString("foo");
	}

	@ParameterizedTest
	@CsvSource(delimiter = 'D', value = {
			"x     D  x",
			"_x    D  x",
			"__x   D  x",
			"x_    D  x",
			"x__   D  x",
			"x y   D  x_y",
			"x  y  D  x_y",
			"x,y   D  x_y",
			"x()   D  x",
			"☺x☺︎   D  x",
			"!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~} x !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~} y !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~}  D  x_y",
	})
	void test_simplifyTestName(String input, String output) {
		assertThat(ApprovalTestingImpl.simplifyTestName(input))
				.isEqualTo(output);
	}

}
