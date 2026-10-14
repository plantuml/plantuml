package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

/**
 * {@link CliFlagDocTable} holds, as plain code, the values of the annotations of
 * {@link CliFlag}: the TeaVM C build cannot read these annotations, but prints
 * the same help as the JVM and needs the default values to parse the command
 * line. It must not drift from the annotations.
 */
class CliFlagDocTableTest {

	@Test
	void tableIsTheSameAsTheAnnotations() throws NoSuchFieldException {
		for (CliFlag flag : CliFlag.values()) {
			final Field field = CliFlag.class.getField(flag.name());
			final CliFlagDoc doc = field.getAnnotation(CliFlagDoc.class);
			final CliDefaultValue defaultValue = field.getAnnotation(CliDefaultValue.class);

			assertEquals(doc != null && !doc.value().isEmpty() ? doc.value() : null, CliFlagDocTable.doc(flag),
					message("doc", flag));
			assertEquals(doc != null && !doc.value().isEmpty() ? doc.newGroup() : "", CliFlagDocTable.newGroup(flag),
					message("newGroup", flag));
			assertEquals(doc != null && !doc.usage().isEmpty() ? doc.usage() : null, CliFlagDocTable.usage(flag),
					message("usage", flag));
			assertEquals(doc != null ? doc.level() : -1, CliFlagDocTable.level(flag), message("level", flag));
			assertEquals(defaultValue != null && !defaultValue.value().isEmpty() ? defaultValue.value() : null,
					CliFlagDocTable.defaultValue(flag), message("defaultValue", flag));
		}
	}

	private static String message(String accessor, CliFlag flag) {
		return "CliFlagDocTable." + accessor + "(" + flag.name() + ") differs from the annotations of CliFlag: "
				+ "update CliFlagDocTable";
	}

}
