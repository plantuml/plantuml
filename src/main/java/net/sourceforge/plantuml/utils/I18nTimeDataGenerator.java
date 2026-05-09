/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Generates {@code I18nTimeData.java} by extracting short localized names for
 * {@link DayOfWeek} and {@link Month} from the running JVM.
 * <p>
 * Run this class on a standard JVM (NOT under TeaVM) from the project root. It
 * overwrites {@code src/main/java/net/sourceforge/plantuml/utils/I18nTimeData.java}
 * in place:
 *
 * <pre>
 * java net.sourceforge.plantuml.utils.I18nTimeDataGenerator
 * </pre>
 *
 * The generated class exposes {@code shortName(DayOfWeek, Locale)} and
 * {@code shortName(Month, Locale)} implementations that do not rely on
 * {@code ResourceBundle} or {@code DateTimeTextProvider}, so they are safe to
 * use in a TeaVM-compiled context.
 * <p>
 * back to the English short form.
 */
public class I18nTimeDataGenerator {

	// Languages supported by the generated I18nTimeData.
	// Order only affects readability of the generated code; dispatch is done
	// on Locale.getLanguage() and each branch is independent.
	private static final String[] LANGUAGES = { "en", "de", "es", "fr", "ja", "ko", "ru", "zh" };

	public static void main(String[] args) throws Exception {
		// Write I18nData.java next to this generator's source file.
		// The path is resolved relative to the current working directory, which
		// is expected to be the project root (same convention as other PlantUML
		// code-generation utilities).
		final File output = new File("src/main/java/net/sourceforge/plantuml/utils/I18nTimeData.java");
		final File parent = output.getParentFile();
		if (parent != null && !parent.isDirectory())
			throw new IOException("Target directory does not exist: " + parent);

		try (PrintStream out = new PrintStream(new FileOutputStream(output), false, StandardCharsets.UTF_8.name())) {
			emitHeader(out);
			emitDayOfWeekMethod(out);
			out.println();
			emitMonthMethod(out);
			out.println();
			emitMonthLongNameMethod(out);
			emitFooter(out);
		}

		System.out.println("Generated " + output.getAbsolutePath());
	}

	private static void emitHeader(PrintStream out) {
		out.println("package net.sourceforge.plantuml.utils;");
		out.println();
		out.println("import java.time.DayOfWeek;");
		out.println("import java.time.Month;");
		out.println("import java.util.Locale;");
		out.println();
		out.println("// Generated \u2014 do not edit");
		out.println("// Build by I18nDataTimeGenerator");
		out.println("public class I18nTimeData {");
		out.println();
	}

	private static void emitFooter(PrintStream out) {
		out.println("}");
	}

	private static void emitDayOfWeekMethod(PrintStream out) {
		out.println("\tpublic static String shortName(DayOfWeek dayOfWeek, Locale locale) {");
		out.println("\t\tfinal String lang = locale.getLanguage();");
		out.println("\t\tswitch (lang) {");
		for (String lang : LANGUAGES) {
			if ("en".equals(lang))
				continue; // english is the fallback
			final Locale loc = Locale.forLanguageTag(lang);
			out.println("\t\tcase \"" + lang + "\":");
			out.println("\t\t\tswitch (dayOfWeek) {");
			for (DayOfWeek d : DayOfWeek.values()) {
				final String shortName = dayShortName(d, loc);
				out.println("\t\t\tcase " + d.name() + ": return \"" + escape(shortName) + "\";");
			}
			out.println("\t\t\t}");
			out.println("\t\t\tbreak;");
		}
		out.println("\t\t}");
		// English fallback (also used when locale is unsupported)
		out.println("\t\t// Fallback: English short form (first two letters of enum name)");
		out.println("\t\tswitch (dayOfWeek) {");
		for (DayOfWeek d : DayOfWeek.values()) {
			final String shortName = dayShortName(d, Locale.ENGLISH);
			out.println("\t\tcase " + d.name() + ": return \"" + escape(shortName) + "\";");
		}
		out.println("\t\t}");
		out.println("\t\tthrow new IllegalArgumentException();");
		out.println("\t}");
	}

	private static void emitMonthMethod(PrintStream out) {
		out.println("\tpublic static String shortName(Month month, Locale locale) {");
		out.println("\t\tfinal String lang = locale.getLanguage();");
		out.println("\t\tswitch (lang) {");
		for (String lang : LANGUAGES) {
			if ("en".equals(lang))
				continue;
			final Locale loc = Locale.forLanguageTag(lang);
			out.println("\t\tcase \"" + lang + "\":");
			out.println("\t\t\tswitch (month) {");
			for (Month m : Month.values()) {
				final String shortName = monthShortName(m, loc);
				out.println("\t\t\tcase " + m.name() + ": return \"" + escape(shortName) + "\";");
			}
			out.println("\t\t\t}");
			out.println("\t\t\tbreak;");
		}
		out.println("\t\t}");
		out.println("\t\t// Fallback: English short form (first three letters of enum name)");
		out.println("\t\tswitch (month) {");
		for (Month m : Month.values()) {
			final String shortName = monthShortName(m, Locale.ENGLISH);
			out.println("\t\tcase " + m.name() + ": return \"" + escape(shortName) + "\";");
		}
		out.println("\t\t}");
		out.println("\t\tthrow new IllegalArgumentException();");
		out.println("\t}");
	}

	private static void emitMonthLongNameMethod(PrintStream out) {
		out.println("\tpublic static String longName(Month month, Locale locale) {");
		out.println("\t\tfinal String lang = locale.getLanguage();");
		out.println("\t\tswitch (lang) {");
		for (String lang : LANGUAGES) {
			if ("en".equals(lang))
				continue;
			final Locale loc = Locale.forLanguageTag(lang);
			out.println("\t\tcase \"" + lang + "\":");
			out.println("\t\t\tswitch (month) {");
			for (Month m : Month.values()) {
				final String longName = monthLongName(m, loc);
				out.println("\t\t\tcase " + m.name() + ": return \"" + escape(longName) + "\";");
			}
			out.println("\t\t\t}");
			out.println("\t\t\tbreak;");
		}
		out.println("\t\t}");
		out.println("\t\t// Fallback: English long form");
		out.println("\t\tswitch (month) {");
		for (Month m : Month.values()) {
			final String longName = monthLongName(m, Locale.ENGLISH);
			out.println("\t\tcase " + m.name() + ": return \"" + escape(longName) + "\";");
		}
		out.println("\t\t}");
		out.println("\t\tthrow new IllegalArgumentException();");
		out.println("\t}");
	}

	// ------------------------------------------------------------------
	// Reproduce DayOfWeekUtils.shortName / MonthUtils.shortName semantics
	// ------------------------------------------------------------------

	private static String capitalize(String s) {
		if (s == null || s.isEmpty())
			return s;
		return Character.toUpperCase(s.charAt(0)) + s.toLowerCase().substring(1);
	}

	private static String dayShortName(DayOfWeek d, Locale locale) {
		if (locale == Locale.ENGLISH || "en".equals(locale.getLanguage()))
			return capitalize(d.name().substring(0, 2));
		final String s = d.getDisplayName(TextStyle.SHORT_STANDALONE, locale);
		if (s.length() > 2)
			return s.substring(0, 2);
		return s;
	}

	private static String monthShortName(Month m, Locale locale) {
		if (locale == Locale.ENGLISH || "en".equals(locale.getLanguage()))
			return capitalize(m.name()).substring(0, 3);
		return m.getDisplayName(TextStyle.SHORT_STANDALONE, locale);
	}

	private static String monthLongName(Month m, Locale locale) {
		if (locale == Locale.ENGLISH || "en".equals(locale.getLanguage()))
			return capitalize(m.name());
		return m.getDisplayName(TextStyle.FULL_STANDALONE, locale);
	}

	// Escape only characters that have a special meaning inside a Java string
	// literal. Non-ASCII characters are emitted as-is: the generated file is
	// written in UTF-8, which keeps the source human-readable.
	private static String escape(String s) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			switch (c) {
			case '"':
			case '\\':
				sb.append('\\').append(c);
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

}
