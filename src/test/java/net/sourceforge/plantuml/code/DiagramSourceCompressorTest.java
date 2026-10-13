package net.sourceforge.plantuml.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import net.sourceforge.plantuml.StringUtils;

/**
 * Tests the cleaning of the source done by {@link DiagramSourceCompressor}
 * (blanks, {@code @startuml} and {@code @enduml} lines), not the compression.
 * <p>
 * This code used to be regular expressions. On a whole diagram source they
 * overflow the JavaScript call stack in the TeaVM build (issue #2934), so they
 * were replaced by plain loops. The old expressions are kept here, in the
 * {@code reference*} methods, as the definition of what the loops must do:
 * every expected value of this class is checked against them as well, and a
 * random comparison runs on top.
 */
class DiagramSourceCompressorTest {

	// What [%s] stands for in a PlantUML regex: \s and the non-breaking space.
	private static final String BLANK = "[\\s\u00A0]";

	private static final Pattern REFERENCE = Pattern.compile("(?s)^" + BLANK + "*(@startuml[^\\n\\r]*)?" + BLANK
			+ "*(.*?)" + BLANK + "*(@enduml)?" + BLANK + "*$");

	private static String referenceStrip(String s, boolean find) {
		final Matcher m = REFERENCE.matcher(s);
		assertTrue(find ? m.find() : m.matches());
		return m.group(2);
	}

	private static String referenceRemove(String s, String token) {
		return s.replaceAll(token + "[^\\n\\r]*", "");
	}

	private static String referenceClean(String s) {
		String result = StringUtils.trin(s);
		result = referenceStrip(result, false);
		result = referenceRemove(result, "@enduml");
		result = referenceRemove(result, "@startuml");
		return StringUtils.trin(result);
	}

	private static String referenceCompressOld(String s) {
		return referenceClean(referenceStrip(s, true));
	}

	// ------------------------------------------------------------------ isBlank

	@ParameterizedTest
	@ValueSource(chars = { ' ', '\t', '\n', '\u000B', '\f', '\r', '\u00A0' })
	void isBlank_accepts_s_and_nbsp(char c) {
		assertTrue(DiagramSourceCompressor.isBlank(c));
	}

	@ParameterizedTest
	@ValueSource(chars = { 'x', '@', '\0', '\u0001', '\u001F', '\u0085', '\u2007', '\u2028', '\u2029', '\u202F',
			'\u3000' })
	void isBlank_rejects_everything_else(char c) {
		assertFalse(DiagramSourceCompressor.isBlank(c));
	}

	@Test
	void isBlank_is_narrower_than_isTrimmable() {
		// Using StringUtils.isTrimmable here would change what the cleaning keeps
		assertTrue(StringUtils.isTrimmable('\0'));
		assertFalse(DiagramSourceCompressor.isBlank('\0'));
	}

	// ------------------------------------------------------------ stripStartEnd

	private static Arguments same(String input, String expected) {
		return Arguments.of(input, expected, expected);
	}

	static Stream<Arguments> strip_cases() {
		return Stream.of(
				// arguments: input, expected with find(), expected with matches()
				same("", ""),
				same("   ", ""),
				same("abc", "abc"),
				same("  abc  ", "abc"),
				same("\n\t abc \r\n", "abc"),
				same("@startuml\nabc\n@enduml", "abc"),
				same("@startuml foo bar\n  abc  \n  @enduml  ", "abc"),
				same("  \n@startuml\nabc", "abc"),
				same("@startuml\n@startuml\nabc", "@startuml\nabc"),
				same("x@startuml\ny", "x@startuml\ny"),
				// not a @startuml diagram (the issue 2934 case): the tags are kept
				same("@startwbs\n+ a\n@endwbs", "@startwbs\n+ a\n@endwbs"),
				same("abc @enduml", "abc"),
				same("a @enduml @enduml", "a @enduml"),
				same("abc@endumlx", "abc@endumlx"),
				same("@enduml", ""),
				same("@startuml@enduml", ""),
				same("@startuml\n@enduml", ""),
				same("@startuml\u2028", ""),
				// only \s and the non-breaking space are blanks
				same("\u00A0abc\u00A0", "abc"),
				same("\0abc\0", "\0abc\0"),
				same("\u2007abc", "\u2007abc"),
				// only with find(), $ also matches before a final U+0085, U+2028 or U+2029
				Arguments.of("abc\u2028", "abc", "abc\u2028"),
				Arguments.of("abc \u2029", "abc", "abc \u2029"),
				Arguments.of("abc\n\u0085", "abc", "abc\n\u0085"),
				Arguments.of("abc\u2028\u2028", "abc\u2028", "abc\u2028\u2028"));
	}

	@ParameterizedTest
	@MethodSource("strip_cases")
	void stripStartEnd_cases(String input, String expectedFind, String expectedMatches) {
		assertEquals(expectedFind, DiagramSourceCompressor.stripStartEnd(input, true));
		assertEquals(expectedMatches, DiagramSourceCompressor.stripStartEnd(input, false));
		// the expected values are what the old regular expression captured
		assertEquals(expectedFind, referenceStrip(input, true));
		assertEquals(expectedMatches, referenceStrip(input, false));
	}

	// ----------------------------------------------------------- removeFromToken

	static Stream<Arguments> remove_cases() {
		return Stream.of(
				// arguments: input, token, expected
				Arguments.of("", "@enduml", ""),
				Arguments.of("@enduml", "@enduml", ""),
				Arguments.of("ab@enduml", "@enduml", "ab"),
				Arguments.of("a@endumlb\nc", "@enduml", "a\nc"),
				// the line ends at \n or \r, and only there
				Arguments.of("x @enduml foo\r\ny", "@enduml", "x \r\ny"),
				Arguments.of("x@enduml foo\ry", "@enduml", "x\ry"),
				Arguments.of("@enduml a\u2028b\nc", "@enduml", "\nc"),
				// several occurrences
				Arguments.of("a@enduml b @enduml c\nd", "@enduml", "a\nd"),
				Arguments.of("@enduml\n@enduml\n", "@enduml", "\n\n"),
				Arguments.of("@enduml@enduml", "@enduml", ""),
				Arguments.of("@startuml x\n@enduml y", "@startuml", "\n@enduml y"),
				Arguments.of("\u00E9@enduml\uD83D\uDE00\nz", "@enduml", "\u00E9\nz"));
	}

	@ParameterizedTest
	@MethodSource("remove_cases")
	void removeFromToken_cases(String input, String token, String expected) {
		assertEquals(expected, DiagramSourceCompressor.removeFromToken(input, token));
		assertEquals(expected, referenceRemove(input, token));
	}

	@Test
	void removeFromToken_returns_the_same_string_when_the_token_is_absent() {
		final String s = "@endum and @startum, but neither tag\nsecond line";
		assertSame(s, DiagramSourceCompressor.removeFromToken(s, "@enduml"));
		assertSame(s, DiagramSourceCompressor.removeFromToken(s, "@startuml"));
	}

	// ------------------------------------------------------ clean, compressOld

	static Stream<Arguments> clean_cases() {
		return Stream.of(
				// arguments: input, expected
				Arguments.of("", ""),
				Arguments.of("  @startuml\n  abc\n@enduml  ", "abc"),
				Arguments.of("@startwbs\n+ a\n@endwbs", "@startwbs\n+ a\n@endwbs"),
				Arguments.of("abc\n@enduml foo\n", "abc"),
				Arguments.of("a\n@startuml x\nb", "a\n\nb"),
				// StringUtils.trin, the last step, is wider than the blanks of the regular expression
				Arguments.of("\0 abc \0", "abc"));
	}

	@ParameterizedTest
	@MethodSource("clean_cases")
	void clean_cases(String input, String expected) {
		assertEquals(expected, new DiagramSourceCompressor().clean(input));
		assertEquals(expected, referenceClean(input));
	}

	@Test
	void compressOld_keeps_the_tags_of_a_diagram_that_is_not_startuml() throws Exception {
		final String wbs = "@startwbs\n+ a\n++ b\n@endwbs";
		assertEquals(wbs, new DiagramSourceCompressor().compressOld(wbs + "\n"));
		assertEquals(wbs, referenceCompressOld(wbs + "\n"));
	}

	@Test
	void compressOld_removes_the_startuml_and_enduml_lines() throws Exception {
		assertEquals("a", new DiagramSourceCompressor().compressOld("@startuml\na\n@enduml\n"));
		assertEquals("a", new DiagramSourceCompressor().compressOld("a\n@enduml"));
	}

	@Test
	void a_big_source_is_cleaned_without_losing_anything() throws Exception {
		final StringBuilder body = new StringBuilder();
		for (int i = 0; i < 50_000; i++)
			body.append("+ node number ").append(i).append('\n');

		final DiagramSourceCompressor compressor = new DiagramSourceCompressor();
		final String wbs = "@startwbs\n" + body + "@endwbs";
		assertEquals(wbs, compressor.compressOld(wbs + "\n"));

		final String uml = "@startuml\n" + body + "@enduml";
		assertEquals(body.substring(0, body.length() - 1), compressor.compressOld(uml + "\n"));
	}

	// ----------------------------------------------- random, against the old regex

	private static final String[] PIECES = { "@startuml", "@startuml foo", "@enduml", "@enduml bar", "@startwbs",
			"@endwbs", "@start", "@end", "@", "uml", " ", "  ", "\t", "\n", "\r", "\r\n", "\u00A0", "\u000B", "\f",
			"\u0085", "\u2028", "\u2029", "\u2007", "\0", "\u0001", "x", "ab", "+ a", "-- b", "\u00E9",
			"\uD83D\uDE00" };

	private static String random(Random r) {
		final int count = r.nextInt(14);
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++)
			sb.append(PIECES[r.nextInt(PIECES.length)]);
		return sb.toString();
	}

	@Test
	void random_sources_give_what_the_old_regular_expressions_gave() throws Exception {
		final Random r = new Random(2934);
		final DiagramSourceCompressor compressor = new DiagramSourceCompressor();
		for (int i = 0; i < 20_000; i++) {
			final String s = random(r);
			final String message = "input is " + s.replace("\n", "\\n").replace("\r", "\\r");

			assertEquals(referenceStrip(s, true), DiagramSourceCompressor.stripStartEnd(s, true), message);
			assertEquals(referenceStrip(s, false), DiagramSourceCompressor.stripStartEnd(s, false), message);
			assertEquals(referenceRemove(s, "@enduml"), DiagramSourceCompressor.removeFromToken(s, "@enduml"),
					message);
			assertEquals(referenceRemove(s, "@startuml"), DiagramSourceCompressor.removeFromToken(s, "@startuml"),
					message);
			assertEquals(referenceClean(s), compressor.clean(s), message);
			assertEquals(referenceCompressOld(s), compressor.compressOld(s), message);
		}
	}

}
