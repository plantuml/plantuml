package net.sourceforge.plantuml.ugraphic;

import static net.sourceforge.plantuml.FileFormat.DEBUG;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.atmp.ImageBuilder;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.PlainDiagram;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.creole.legacy.PSystemCreole;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.wbs.WBSDiagram;

class ImageBuilderTest {

	@ParameterizedTest
	@CsvSource(
			value = {
					// inFileFormatOption  Expected
					"  NULL,               none",
					"  foo,                foo",
			},
			nullValues = {"NULL"}
	)
	public void test_preserveAspectRatio_plainDiagram(String inFileFormatOption, String expected) throws Exception {
		final PlainDiagram diagram = new PSystemCreole(UmlSource.create(new ArrayList<StringLocated>(), false));
		FileFormatOption fileFormatOption = new FileFormatOption(DEBUG);

		if (inFileFormatOption != null) fileFormatOption = fileFormatOption.withPreserveAspectRatio(inFileFormatOption);

		final ImageBuilder builder = diagram.createImageBuilder(fileFormatOption);

		assertThat(builder.getPreserveAspectRatio()).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource(
			value = {
					// inSkinParam  inFileFormatOption  Expected
					"  NULL,        NULL,               none",
					"  foo,         NULL,               foo",
					"  NULL,        bar,                bar",
					"  foo,         bar,                bar",
			},
			nullValues = {"NULL"}
	)
	public void test_preserveAspectRatio_styledDiagram(String inSkinParam, String inFileFormatOption, String expected) throws Exception {
		final WBSDiagram diagram = new WBSDiagram(UmlSource.create(new ArrayList<StringLocated>(), false));
		FileFormatOption fileFormatOption = new FileFormatOption(DEBUG);

		if (inSkinParam != null) diagram.setParam("preserveAspectRatio", inSkinParam);
		if (inFileFormatOption != null) fileFormatOption = fileFormatOption.withPreserveAspectRatio(inFileFormatOption);

		final ImageBuilder builder = diagram.createImageBuilder(fileFormatOption);

		assertThat(builder.getPreserveAspectRatio()).isEqualTo(expected);
	}
}
