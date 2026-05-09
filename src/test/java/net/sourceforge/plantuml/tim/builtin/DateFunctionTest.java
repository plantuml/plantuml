package net.sourceforge.plantuml.tim.builtin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.expression.TValue;

public class DateFunctionTest {

	private final DateFunction dateFunction = new DateFunction();

	// =========================================================================
	// canCover / signature
	// =========================================================================

	@Test
	public void testCanCoverZeroToThreeArgs() {
		assertThat(dateFunction.canCover(0, Collections.emptySet())).isTrue();
		assertThat(dateFunction.canCover(1, Collections.emptySet())).isTrue();
		assertThat(dateFunction.canCover(2, Collections.emptySet())).isTrue();
		assertThat(dateFunction.canCover(3, Collections.emptySet())).isTrue();
	}

	@Test
	public void testCanCoverRejectsOtherArities() {
		assertThat(dateFunction.canCover(4, Collections.emptySet())).isFalse();
		assertThat(dateFunction.canCover(10, Collections.emptySet())).isFalse();
	}

	@Test
	public void testSignatureName() {
		assertThat(dateFunction.getSignature().getFunctionName()).isEqualTo("%date");
	}

	// =========================================================================
	// 0 arg: %date()  -> Date#toString() in default locale/timezone
	// =========================================================================

	@Test
	public void testNoArgsReturnsNonEmptyString() throws EaterException {
		final TValue result = call();
		assertThat(result.toString()).isNotNull().isNotEmpty();
	}

	@Test
	public void testNoArgsMatchesDateToStringPattern() throws EaterException {
		final String dateString = call().toString();

		// Permissive pattern for date formats such as:
		// "Mon Mar 31 14:23:55 CEST 2025"         (English)
		// "lun. mars 31 14:23:55 UTC+1 2025"      (French)
		// "周一 三月 31 14:23:55 GMT+08:00 2025"   (Chinese)
		// "Mo Mär 31 14:23:55 CET 2025"           (German)
		// "lun mar 31 14:23:55 CET 2025"          (Spanish)
		final String pattern = "^\\S+\\s+\\S+\\s+\\d{1,2}\\s+\\d{2}:\\d{2}:\\d{2}\\s+\\S+\\s+\\d{4}$";
		assertThat(dateString).matches(pattern);
	}

	// =========================================================================
	// 1 arg: %date(format)  -> "now" in default time zone
	// =========================================================================

	@Test
	public void testWithFormatUsesDefaultTimeZone() throws EaterException {
		final TValue result = call("yyyy-MM-dd");
		// The result must equal what SimpleDateFormat would produce locally for "now".
		// Allow a one-day tolerance in case the test runs across midnight.
		final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		final long nowMillis = System.currentTimeMillis();
		final String today = fmt.format(new Date(nowMillis));
		final String yesterday = fmt.format(new Date(nowMillis - 24L * 3600L * 1000L));
		final String tomorrow = fmt.format(new Date(nowMillis + 24L * 3600L * 1000L));
		assertThat(result.toString()).isIn(yesterday, today, tomorrow);
	}

	@Test
	public void testWithFormatHourMinuteHasExpectedShape() throws EaterException {
		final TValue result = call("HH:mm");
		assertThat(result.toString()).matches("^\\d{2}:\\d{2}$");
	}

	// =========================================================================
	// 2 args: %date(format, timestamp)  -> default time zone (legacy behavior)
	// =========================================================================

	@Test
	public void testWithFormatAndTimestampUsesDefaultTimeZone() throws EaterException {
		final long timestamp = 1609459200L; // 2021-01-01 00:00:00 UTC
		final TValue result = call("yyyy-MM-dd HH:mm:ss z", ts(timestamp));

		// Default-time-zone behavior is preserved (this is the very behavior that
		// motivated adding the optional 3rd time-zone argument). We compare against a
		// locally formatted SimpleDateFormat — same time zone, same instant, must match.
		final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		assertThat(result.toString()).isEqualTo(fmt.format(new Date(timestamp * 1000L)));
	}

	@Test
	public void testWithFormatAndTimestampZero() throws EaterException {
		// Unix epoch in UTC is 1970-01-01 00:00:00.
		final TValue result = call("yyyy", ts(0));
		// Year is 1969 or 1970 depending on the JVM time zone. Both are fine.
		assertThat(result.toString()).isIn("1969", "1970");
	}

	// =========================================================================
	// 3 args: %date(format, timestamp, timezone)  -> deterministic, explicit TZ
	// =========================================================================

	@Test
	public void testWithExplicitUtcTimeZone() throws EaterException {
		final long timestamp = 1609459200L; // 2021-01-01 00:00:00 UTC
		final TValue result = call("yyyy-MM-dd", ts(timestamp), "UTC");
		assertThat(result.toString()).isEqualTo("2021-01-01");
	}

	@Test
	public void testWithExplicitUtcFullFormat() throws EaterException {
		final long timestamp = 1609459200L; // 2021-01-01 00:00:00 UTC
		final TValue result = call("yyyy-MM-dd HH:mm:ss", ts(timestamp), "UTC");
		assertThat(result.toString()).isEqualTo("2021-01-01 00:00:00");
	}

	@Test
	public void testWithExplicitGmtAlias() throws EaterException {
		final long timestamp = 1609459200L;
		final TValue result = call("yyyy-MM-dd HH:mm:ss", ts(timestamp), "GMT");
		assertThat(result.toString()).isEqualTo("2021-01-01 00:00:00");
	}

	/**
	 * Same instant (2021-01-01 00:00:00 UTC) viewed from various time zones.
	 * Each row exercises the 3-arg signature and must produce a deterministic result
	 * regardless of the JVM's default time zone. Offsets chosen are well-known and
	 * not affected by DST on Jan 1st 2021 (which falls outside DST in the US/EU).
	 */
	@ParameterizedTest(name = "[{index}] timestamp 1609459200 in {0} -> {1}")
	@CsvSource({
			" UTC                , '2021-01-01 00:00:00' ",
			" GMT                , '2021-01-01 00:00:00' ",
			" Europe/Paris       , '2021-01-01 01:00:00' ",
			" Europe/London      , '2021-01-01 00:00:00' ",
			" America/New_York   , '2020-12-31 19:00:00' ",
			" America/Los_Angeles, '2020-12-31 16:00:00' ",
			" Asia/Tokyo         , '2021-01-01 09:00:00' ",
			" Pacific/Auckland   , '2021-01-01 13:00:00' ",
			" Asia/Kolkata       , '2021-01-01 05:30:00' ",
	})
	public void testTimestampAcrossTimeZones(String tz, String expected) throws EaterException {
		final long timestamp = 1609459200L; // 2021-01-01 00:00:00 UTC
		final TValue result = call("yyyy-MM-dd HH:mm:ss", ts(timestamp), tz);
		assertThat(result.toString()).isEqualTo(expected);
	}

	@Test
	public void testExplicitTimeZoneIsIndependentOfJvmDefault() throws EaterException {
		// Force a non-trivial JVM default time zone for the duration of this test, then
		// verify that an explicit "UTC" argument is honored regardless.
		final TimeZone original = TimeZone.getDefault();
		try {
			TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
			final long timestamp = 1609459200L; // 2021-01-01 00:00:00 UTC

			// First, prove that the JVM default really did change: with 2 args (no
			// explicit time zone), the result must reflect Los_Angeles (UTC-8 on Jan 1).
			// If TimeZone.setDefault() were silently ignored, this assertion would fail
			// and the next one wouldn't actually be testing anything meaningful.
			final TValue defaultTzResult = call("yyyy-MM-dd", ts(timestamp));
			assertThat(defaultTzResult.toString()).isEqualTo("2020-12-31");

			// Now, with an explicit "UTC" 3rd argument, the result must be UTC-based
			// regardless of the (verified) Los_Angeles default.
			final TValue explicitUtcResult = call("yyyy-MM-dd", ts(timestamp), "UTC");
			assertThat(explicitUtcResult.toString()).isEqualTo("2021-01-01");
		} finally {
			TimeZone.setDefault(original);
		}
	}

	@Test
	public void testWithDateOnlyFormatTokyoCrossesMidnight() throws EaterException {
		// 2020-12-31 16:00:00 UTC is already 2021-01-01 in Tokyo (UTC+9).
		final long timestamp = 1609430400L;
		final TValue result = call("yyyy-MM-dd", ts(timestamp), "Asia/Tokyo");
		assertThat(result.toString()).isEqualTo("2021-01-01");
	}

	// =========================================================================
	// Errors
	// =========================================================================

	@Test
	public void testInvalidFormatThrows() {
		// An unterminated quote in the pattern makes SimpleDateFormat's constructor
		// throw IllegalArgumentException, which DateFunction must wrap in EaterException.
		assertThatThrownBy(() -> call("yyyy '"))
				.isInstanceOf(EaterException.class)
				.hasMessageContaining("Bad date pattern");
	}

	@Test
	public void testInvalidFormatWithTimestampThrows() {
		assertThatThrownBy(() -> call("yyyy '", ts(1609459200L)))
				.isInstanceOf(EaterException.class)
				.hasMessageContaining("Bad date pattern");
	}

	@Test
	public void testInvalidFormatWithTimestampAndTimeZoneThrows() {
		assertThatThrownBy(() -> call("yyyy '", ts(1609459200L), "UTC"))
				.isInstanceOf(EaterException.class)
				.hasMessageContaining("Bad date pattern");
	}

	@Test
	public void testUnknownTimeZoneThrows() {
		// SimpleDateFormat silently falls back to GMT for unknown ids; DateFunction
		// must surface this as a clear error instead so users can spot typos.
		assertThatThrownBy(() -> call("yyyy-MM-dd", ts(1609459200L), "Not/A/Zone"))
				.isInstanceOf(EaterException.class)
				.hasMessageContaining("Unknown time zone");
	}

	@Test
	public void testEmptyTimeZoneStringThrows() {
		assertThatThrownBy(() -> call("yyyy-MM-dd", ts(1609459200L), ""))
				.isInstanceOf(EaterException.class)
				.hasMessageContaining("Unknown time zone");
	}

	@Test
	public void testTimeZoneIsCaseSensitive() {
		// "utc" lowercase is NOT a valid TimeZone id — only "UTC" is.
		assertThatThrownBy(() -> call("yyyy-MM-dd", ts(1609459200L), "utc"))
				.isInstanceOf(EaterException.class)
				.hasMessageContaining("Unknown time zone");
	}

	// =========================================================================
	// Helpers
	// =========================================================================

	private TValue call(Object... args) throws EaterException {
		final List<TValue> values = new ArrayList<>(args.length);
		for (Object arg : args) {
			if (arg instanceof TValue)
				values.add((TValue) arg);
			else if (arg instanceof Integer)
				values.add(TValue.fromInt((Integer) arg));
			else
				values.add(TValue.fromString(arg.toString()));
		}
		// EaterException requires a non-null StringLocated, so we always pass a dummy one
		// — the LineLocation can stay null, only the StringLocated wrapper must exist.
		final StringLocated location = new StringLocated("test", null);
		return dateFunction.executeReturnFunction(null, null, location, values, null);
	}

	private static TValue ts(long timestamp) {
		return TValue.fromInt((int) timestamp);
	}
}
