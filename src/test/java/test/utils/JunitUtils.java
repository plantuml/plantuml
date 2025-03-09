package test.utils;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonValue;

/**
 * Class to help test with `Junit`.
 */
public class JunitUtils {
	/**
	 * `StringJsonConverter` class to use with `@ConvertWith`.
	 */
	public static class StringJsonConverter extends SimpleArgumentConverter {
		@Override
		protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
			if (source instanceof String) 
				return (JsonValue) Json.parse((String) source);
			else
				throw new IllegalArgumentException("Conversion from " + source.getClass() + " to "
									    			+ targetType + " not supported.");
		}
	}
}