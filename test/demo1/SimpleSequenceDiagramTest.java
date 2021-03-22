package demo1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

		final String actual = new String(baos.toByteArray(), "UTF-8");

		final String expected = getExpectedResult();
		assertSameDebugGraphic(expected, actual);
	}

	private void assertSameDebugGraphic(String expectedString, String actualString) {
		final String[] expected = expectedString.split("\n");
		final String[] actual = actualString.split("\n");

		if (expected.length != actual.length) {
			// We know it will fail here, but we want to print the full info
			assertEquals(expectedString, actualString);
			return;
		}

		for (int i = 0; i < actual.length; i++) {
			if (sameString(expected[i], actual[i]) == false) {
				System.err.println("expected: " + expected[i]);
				System.err.println("actual  : " + actual[i]);
				// We know it will fail here, but we want to print the full info
				assertEquals(expectedString, actualString);
			}
		}
	}

	private final Pattern pointLine = Pattern.compile("^(.*?)\\[ ([-.0-9]+) ; ([-.0-9]+) \\]$");

	private boolean sameString(String s1, String s2) {
		final Matcher line1 = pointLine.matcher(s1);
		final Matcher line2 = pointLine.matcher(s2);
		if (line1.matches() && line2.matches()) {
			final String start1 = line1.group(1);
			final String start2 = line2.group(1);
			final String x1 = line1.group(2);
			final String x2 = line2.group(2);
			final String y1 = line1.group(3);
			final String y2 = line2.group(3);
			return start1.equals(start2) && sameDouble(x1, x2) && sameDouble(y1, y2);

		}
		return s1.equals(s2);
	}

	private boolean sameDouble(String double1, String double2) {
		final double value1 = Double.parseDouble(double1);
		final double value2 = Double.parseDouble(double2);
		final double diff = Math.abs(value1 - value2);
		final boolean result = diff < 0.001;
		if (result == false) {
			System.err.println("sameDouble:Non null diff=" + diff);
		}
		return result;
	}

	private String getText() {
		return packString( //
				"@startuml", //
				"Alice -> Bob : Hello", //
				"@enduml");
	}

	private String getExpectedResult() {
		return packString("DPI: 96", //
				"", //
				"LINE:", //
				"  pt1: [ 50.0000 ; 38.0000 ]", //
				"  pt2: [ 50.0000 ; 85.0000 ]", //
				"  stroke: 5.0-5.0-1.0", //
				"  shadow: 0", //
				"  color: ffa80036", //
				"", //
				"LINE:", //
				"  pt1: [ 156.8135 ; 38.0000 ]", //
				"  pt2: [ 156.8135 ; 85.0000 ]", //
				"  stroke: 5.0-5.0-1.0", //
				"  shadow: 0", //
				"  color: ffa80036", //
				"", //
				"RECTANGLE:", //
				"  pt1: [ 5.0000 ; 5.0000 ]", //
				"  pt2: [ 92.9573 ; 33.0000 ]", //
				"  xCorner: 0", //
				"  yCorner: 0", //
				"  stroke: 0.0-0.0-1.5", //
				"  shadow: 4", //
				"  color: ffa80036", //
				"  backcolor: fffefece", //
				"", //
				"TEXT:", //
				"  text: Alice", //
				"  position: [ 12.0000 ; 22.8889 ]", //
				"  orientation: 0", //
				"  font: SansSerif.plain/14 []", //
				"  color: ffa80036", //
				"", //
				"RECTANGLE:", //
				"  pt1: [ 5.0000 ; 84.0000 ]", //
				"  pt2: [ 92.9573 ; 112.0000 ]", //
				"  xCorner: 0", //
				"  yCorner: 0", //
				"  stroke: 0.0-0.0-1.5", //
				"  shadow: 4", //
				"  color: ffa80036", //
				"  backcolor: fffefece", //
				"", //
				"TEXT:", //
				"  text: Alice", //
				"  position: [ 12.0000 ; 101.8889 ]", //
				"  orientation: 0", //
				"  font: SansSerif.plain/14 []", //
				"  color: ffa80036", //
				"", //
				"RECTANGLE:", //
				"  pt1: [ 130.8135 ; 5.0000 ]", //
				"  pt2: [ 180.5185 ; 33.0000 ]", //
				"  xCorner: 0", //
				"  yCorner: 0", //
				"  stroke: 0.0-0.0-1.5", //
				"  shadow: 4", //
				"  color: ffa80036", //
				"  backcolor: fffefece", //
				"", //
				"TEXT:", //
				"  text: Bob", //
				"  position: [ 137.8135 ; 22.8889 ]", //
				"  orientation: 0", //
				"  font: SansSerif.plain/14 []", //
				"  color: ffa80036", //
				"", //
				"RECTANGLE:", //
				"  pt1: [ 130.8135 ; 84.0000 ]", //
				"  pt2: [ 180.5185 ; 112.0000 ]", //
				"  xCorner: 0", //
				"  yCorner: 0", //
				"  stroke: 0.0-0.0-1.5", //
				"  shadow: 4", //
				"  color: ffa80036", //
				"  backcolor: fffefece", //
				"", //
				"TEXT:", //
				"  text: Bob", //
				"  position: [ 137.8135 ; 101.8889 ]", //
				"  orientation: 0", //
				"  font: SansSerif.plain/14 []", //
				"  color: ffa80036", //
				"", //
				"POLYGON:", //
				"  points:", //
				"   - [ 145.6660 ; 63.0000 ]", //
				"   - [ 155.6660 ; 67.0000 ]", //
				"   - [ 145.6660 ; 71.0000 ]", //
				"   - [ 149.6660 ; 67.0000 ]", //
				"  stroke: 0.0-0.0-1.0", //
				"  shadow: 0", //
				"  color: ffa80036", //
				"  backcolor: ffa80036", //
				"", //
				"LINE:", //
				"  pt1: [ 50.9786 ; 67.0000 ]", //
				"  pt2: [ 151.6660 ; 67.0000 ]", //
				"  stroke: 0.0-0.0-1.0", //
				"  shadow: 0", //
				"  color: ffa80036", //
				"", //
				"TEXT:", //
				"  text: Hello", //
				"  position: [ 57.9786 ; 62.1111 ]", //
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
