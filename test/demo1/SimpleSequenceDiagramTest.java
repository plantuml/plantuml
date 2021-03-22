package demo1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;

/*
 * This test is a simple POC of what could be non-regression test for PlantUML.
 * 
 * In real world, test diagram and expected result would not be stored in source file.
 */
class SimpleSequenceDiagramTest {

	@Test
	void testSimple() throws IOException {

		final String diagramText = getText();
		final SourceStringReader ssr = new SourceStringReader(diagramText, "UTF-8");
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		final DiagramDescription diagramDescription = ssr.outputImage(baos, 0, new FileFormatOption(FileFormat.DEBUG));

		assertEquals("(2 participants)", diagramDescription.getDescription());

		final String desc = new String(baos.toByteArray(), "UTF-8");

		final String expected = getExpectedResult();
		assertEquals(expected, desc);

	}

	private String getText() {
		return packString( //
				"@startuml", //
				"Alice -> Bob : Hello", //
				"@enduml");
	}

	private String getExpectedResult() {
		return packString( //
				"DPI: 96", //
				"", //
				"LINE:", //
				"  pt1: 49;38", //
				"  pt2: 49;85", //
				"  stroke: 5.0-5.0-1.0", //
				"  shadow: 0", //
				"  color: ffa80036", //
				"", //
				"LINE:", //
				"  pt1: 138;38", //
				"  pt2: 138;85", //
				"  stroke: 5.0-5.0-1.0", //
				"  shadow: 0", //
				"  color: ffa80036", //
				"", //
				"RECTANGLE:", //
				"  pt1: 5;5", //
				"  pt2: 89;33", //
				"  xCorner: 0", //
				"  yCorner: 0", //
				"  stroke: 0.0-0.0-1.5", //
				"  shadow: 4", //
				"  color: ffa80036", //
				"  backcolor: fffefece", //
				"", //
				"TEXT:", //
				"  text: Alice", //
				"  position: 12;22", //
				"  orientation: 0", //
				"  font: SansSerif.plain/14 []", //
				"  color: ffa80036", //
				"", //
				"RECTANGLE:", //
				"  pt1: 5;84", //
				"  pt2: 89;112", //
				"  xCorner: 0", //
				"  yCorner: 0", //
				"  stroke: 0.0-0.0-1.5", //
				"  shadow: 4", //
				"  color: ffa80036", //
				"  backcolor: fffefece", //
				"", //
				"TEXT:", //
				"  text: Alice", //
				"  position: 12;101", //
				"  orientation: 0", //
				"  font: SansSerif.plain/14 []", //
				"  color: ffa80036", //
				"", //
				"RECTANGLE:", //
				"  pt1: 108;5", //
				"  pt2: 164;33", //
				"  xCorner: 0", //
				"  yCorner: 0", //
				"  stroke: 0.0-0.0-1.5", //
				"  shadow: 4", //
				"  color: ffa80036", //
				"  backcolor: fffefece", //
				"", //
				"TEXT:", //
				"  text: Bob", //
				"  position: 115;22", //
				"  orientation: 0", //
				"  font: SansSerif.plain/14 []", //
				"  color: ffa80036", //
				"", //
				"RECTANGLE:", //
				"  pt1: 108;84", //
				"  pt2: 164;112", //
				"  xCorner: 0", //
				"  yCorner: 0", //
				"  stroke: 0.0-0.0-1.5", //
				"  shadow: 4", //
				"  color: ffa80036", //
				"  backcolor: fffefece", //
				"", //
				"TEXT:", //
				"  text: Bob", //
				"  position: 115;101", //
				"  orientation: 0", //
				"  font: SansSerif.plain/14 []", //
				"  color: ffa80036", //
				"", //
				"POLYGON:", //
				"  points:", //
				"   - 126;63", //
				"   - 136;67", //
				"   - 126;71", //
				"   - 130;67", //
				"  stroke: 0.0-0.0-1.0", //
				"  shadow: 0", //
				"  color: ffa80036", //
				"  backcolor: ffa80036", //
				"", //
				"LINE:", //
				"  pt1: 49;67", //
				"  pt2: 132;67", //
				"  stroke: 0.0-0.0-1.0", //
				"  shadow: 0", //
				"  color: ffa80036", //
				"", //
				"TEXT:", //
				"  text: Hello", //
				"  position: 56;62", //
				"  orientation: 0", //
				"  font: SansSerif.plain/13 []", //
				"  color: ffa80036" //
		);
	}

	private String packString(String... list) {
		final StringBuilder sb = new StringBuilder();
		for (String s : list) {
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString() + "\n";
	}

}
