package net.sourceforge.plantuml.tim.builtin;

import net.sourceforge.plantuml.tim.expression.TValue;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DateFunctionTest {

    private final DateFunction dateFunction = new DateFunction();

    @Test
    public void testDateFunctionNoArgs() {
        TValue result = dateFunction.executeReturnFunction(null, null, null, Arrays.asList(), null);
        String dateString = result.toString();
        assertThat(dateString).isNotNull();
        assertThatCode(() -> new Date(dateString)).doesNotThrowAnyException();
    }

    @Test
    public void testDateFunctionWithFormat() {
        TValue result = dateFunction.executeReturnFunction(null, null, null, Arrays.asList(TValue.fromString("yyyy-MM-dd")), null);
        String formattedDate = result.toString();
        assertThat(formattedDate).isEqualTo(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    @Test
    public void testDateFunctionWithFormatAndTimestamp() {
        long timestamp = 1609459200L; // 2021-01-01 00:00:00 UTC
        TValue result = dateFunction.executeReturnFunction(null, null, null, Arrays.asList(TValue.fromString("yyyy-MM-dd"), TValue.fromInt((int) timestamp)), null);
        String formattedDate = result.toString();
        assertThat(formattedDate).isEqualTo("2021-01-01");
    }

    @Test
    public void testDateFunctionInvalidFormat() {
        assertThatThrownBy(() -> dateFunction.executeReturnFunction(null, null, null, Arrays.asList(TValue.fromString("invalid_format")), null))
            .isInstanceOf(Exception.class);
    }
}
