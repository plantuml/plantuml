package net.sourceforge.plantuml.test.approval;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ApprovalTestingTest {

	@RegisterExtension
	private final ApprovalTestingJUnitExtension approvalTesting = new ApprovalTestingJUnitExtension();

	@Test
	void test_simple() {
		approvalTesting.approve("simple");
	}

	@ParameterizedTest(name="{arguments}")
	@CsvSource({
			"a, 1",
			"b, 2",
	})
	void test_parameterized(String s, int i) {
		approvalTesting.approve(s + i);
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
