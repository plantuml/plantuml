package net.sourceforge.plantuml.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.klimt.geom.Rankdir;

/**
 * The words of "left to right direction" may be separated by any character
 * that %s accepts, but only spaces were turned into underscores before
 * Rankdir.valueOf(), so a tab or a non-breaking space threw
 * "IllegalArgumentException: No enum constant ...Rankdir.LEFT\tTO\tRIGHT".
 */
class CommandRankDirTest {

	@ParameterizedTest
	@CsvSource(value = { //
			"@startuml|left to right|LEFT_TO_RIGHT", //
			"@startuml|left\tto\tright|LEFT_TO_RIGHT", //
			"@startuml|left to right|LEFT_TO_RIGHT", //
			"@startuml|top\tto bottom|TOP_TO_BOTTOM", //
			"@startmindmap|top\tto\tbottom|TOP_TO_BOTTOM", //
	}, delimiter = '|')
	void direction_words_separated_by_any_whitespace(String start, String words, Rankdir expected) {
		final String end = start.replace("@start", "@end");
		final String body = start.equals("@startuml") ? "A -> B" : "* a\n** b";
		final String source = start + "\n" + words.replace("\\t", "\t") + " direction\n" + body + "\n" + end + "\n";
		final Diagram diagram = new SourceStringReader(source).getBlocks().get(0).getDiagram();
		assertInstanceOf(TitledDiagram.class, diagram);
		assertEquals(expected, ((TitledDiagram) diagram).getSkinParam().getRankdir());
	}

}
