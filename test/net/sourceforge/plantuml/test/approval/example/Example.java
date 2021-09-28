package net.sourceforge.plantuml.test.approval.example;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.test.TestUtils.exportOneDiagramToByteArray;

import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.test.approval.ApprovalTesting;
import net.sourceforge.plantuml.test.approval.ApprovalTestingJUnitExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.security.ImageIO;

@ExtendWith(ApprovalTestingJUnitExtension.class)
public class Example {

	private ApprovalTesting approvalTesting;  // injected by ApprovalTestingJUnitExtension

	private static final String SOURCE = "" +
			"@startuml\n" +
			"a -> b\n" +
			"@enduml\n";

	@Test
	void test_export_png() throws Exception {
		final byte[] bytes = exportOneDiagramToByteArray(SOURCE, FileFormat.PNG, "-nometadata");
		final BufferedImage image = ImageIO.read(bytes);
		approvalTesting.approve(image);
	}

	@Test
	void test_export_ascii() throws Exception {
		final byte[] bytes = exportOneDiagramToByteArray(SOURCE, FileFormat.ATXT);
		final String string = new String(bytes, UTF_8);
		approvalTesting.approve(string);
	}

	@ParameterizedTest(name = "{arguments}")
	@EnumSource(
			value = FileFormat.class,
			names = {"ATXT", "DEBUG", "EPS", "HTML5", "LATEX", "SVG", "UTXT", "VDX"}
	)
	void test_export_many(FileFormat fileFormat) throws Exception {
		final byte[] bytes = exportOneDiagramToByteArray(SOURCE, fileFormat, "-nometadata");
		final String string = new String(bytes, UTF_8);
		approvalTesting
				.withExtension(fileFormat.getFileSuffix())
				.approve(string);
	}
}
