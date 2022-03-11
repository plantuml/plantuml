package net.sourceforge.plantuml.project.lang;

import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.Load;
import net.sourceforge.plantuml.test.FailableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ComplementSeveralDaysTest {
	private static final ComplementSeveralDays tested = new ComplementSeveralDays();
	private static final String KEY = "COMPLEMENT";

	@Test
	void onInvalidLine() {
		// given
		IRegex regex = tested.toRegex("");

		// when
		RegexResult result = regex.matcher("invalidLine");

		// then
		assertThat(result).isNull();
	}

	@ParameterizedTest
	@MethodSource("validArguments")
	void onValidLineRegexpReturnsMatch(String input, int argumentCount, List<String> arguments) {
		// given
		IRegex regex = tested.toRegex("");

		// when
		RegexResult result = regex.matcher(input);

		// then
		assertThat(getKeyMatch(result))
			.isNotNull()
			.hasSize(argumentCount)
			.containsExactlyElementsOf(arguments);
	}

	private static Stream<Arguments> validArguments() {
		return Stream.<Arguments>builder()
			.add(of("lasts 1 week and 4 days", 4, List.of("1", "week", "4", "day")))
			.add(of("lasts 1 week and 4 days", 4, List.of("1", "week", "4", "day")))
			.add(of("lasts 1 day and 4 week", 4, List.of("1", "day", "4", "week")))
			.add(of("lasts 1 month and 4 days", 4, List.of("1", "month", "4", "day")))
			.add(of("lasts 1 months and 4 days", 4, List.of("1", "month", "4", "day")))
			.add(of("lasts 1 month and 4 quarter", 4, List.of("1", "month", "4", "quarter")))
			.add(of("lasts 1 months and 4 quarters", 4, List.of("1", "month", "4", "quarter")))
			.add(of("lasts 1 year and 2 months", 4, List.of("1", "year", "2", "month")))
			.add(of("lasts 2 years and 3 days", 4, List.of("2", "year", "3", "day")))
			.add(of("[Test prototype] lasts 10 days", 4, stream(new String[]{"10", "day", null, null}).toList()))
			.build();
	}

	@ParameterizedTest
	@MethodSource("validDays")
	void onValidLineRegexpReturnsAmountInDays(String input, int workweekLength, int days) {
		// given
		IRegex regex = tested.toRegex("");
		RegexResult matchResult = regex.matcher(input);
		GanttDiagram diagram = mock(GanttDiagram.class);
		when(diagram.daysInWeek()).thenReturn(workweekLength);

		// when
		Failable<Load> result = tested.getMe(diagram, matchResult, "");

		// then
		FailableAssert.assertThat(result)
			.isOk()
			.data()
			.isEqualTo(Load.inWinks(days));
	}

	private static Stream<Arguments> validDays() {
		return Stream.<Arguments>builder()
			.add(of("[T4 (1 week and 4 days)] lasts 1 week and 4 days", 7, 11))
			.add(of("[T4 (1 days and 4 weeks)] lasts 1 days and 4 week", 7, 29))
			.add(of("[T4 (1 days and 4 weeks)] lasts 1 days and 4 week", 3, 13))
			.add(of("[Test prototype] lasts 10 days", 7, 10))
			.add(of("[Test prototype] lasts 10 weeks", 7, 70))
			.add(of("[Test prototype] lasts 10 months", 7, 304))
			.add(of("[Test prototype] lasts 10 quarters", 6, 783))
			.add(of("[Test prototype] lasts 1 month and 3 days", 4, 20))
			.add(of("[Test prototype] lasts 1 quarter and 3 days", 4, 55))
			.add(of("[Test prototype] lasts 1 year and 2 months", 4, 243))
			.add(of("[Test prototype] lasts 2 years and 3 days", 4, 420))
			.build();
	}

	private static RegexPartialMatch getKeyMatch(RegexResult matcher) {
		return matcher.get(KEY);
	}
}