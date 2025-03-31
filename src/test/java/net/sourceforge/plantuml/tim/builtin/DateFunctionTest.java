package net.sourceforge.plantuml.tim.builtin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.expression.TValue;

public class DateFunctionTest {

    private final DateFunction dateFunction = new DateFunction();

    @Test
    public void testDateFunctionNoArgs() throws EaterException {
        TValue result = dateFunction.executeReturnFunction(null, null, null, Arrays.asList(), null);
        String dateString = result.toString();
        assertThat(dateString).isNotNull();

		// Permissive pattern for date formats such as:
		// "Mon Mar 31 14:23:55 CEST 2025"         (English)
		// "lun. mars 31 14:23:55 UTC+1 2025"      (French)
		// "周一 三月 31 14:23:55 GMT+08:00 2025"   (Chinese)
		// "Mo Mär 31 14:23:55 CET 2025"           (German)
		// "lun mar 31 14:23:55 CET 2025"          (Spanish)
		final String pattern = "^\\S+\\s+\\S+\\s+\\d{1,2}\\s+\\d{2}:\\d{2}:\\d{2}\\s+\\S+\\s+\\d{4}$";

		assertThat(dateString).matches(pattern);

    }

    @Test
    public void testDateFunctionWithFormat() throws EaterException {
        TValue result = dateFunction.executeReturnFunction(null, null, null, Arrays.asList(TValue.fromString("yyyy-MM-dd")), null);
        String formattedDate = result.toString();
        assertThat(formattedDate).isEqualTo(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    @Test
    public void testDateFunctionWithFormatAndTimestamp() throws EaterException {
        long timestamp = 1609459200L; // 2021-01-01 00:00:00 UTC
        TValue result = dateFunction.executeReturnFunction(null, null, null, Arrays.asList(TValue.fromString("yyyy-MM-dd"), TValue.fromInt((int) timestamp)), null);
        String formattedDate = result.toString();
        assertThat(formattedDate).isEqualTo("2021-01-01");
    }

    @Test
    public void testDateFunctionInvalidFormat() throws EaterException {
        assertThatThrownBy(() -> dateFunction.executeReturnFunction(null, null, null, Arrays.asList(TValue.fromString("invalid_format")), null))
            .isInstanceOf(Exception.class);
    }
}
