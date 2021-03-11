package net.sourceforge.plantuml.test;

import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.security.SFile;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;

import static net.sourceforge.plantuml.test.TestData.loadTestData;

public class TestUtils {

	public static final SFile TEST_OUTPUT_DIR = new SFile("testoutput");

	public static SFile calculateOutputFile(String outputDir, FileFormat fileFormat, String testName) {
		return TEST_OUTPUT_DIR
				.file(outputDir)
				.file(fileFormat.changeName(testName, 0));
	}

	public static void log(String format, Object ... arg) {
		System.out.printf(format, arg);
		System.out.println();
	}

	public static void logException(Exception e, String format, Object ... arg) {
		System.out.printf(format, arg);
		System.out.println();
		e.printStackTrace(System.out);
		System.out.println();
	}

	public static void renderTestCase(FileFormat fileFormat, String outputDir, String testName, String... config) {
		final SFile outputFile = calculateOutputFile(outputDir, fileFormat, testName);

		log("--- Rendering %s ---", outputFile.getPath());

		try {
			outputFile.getParentFile().mkdirs();

			final String source = loadTestData(testName + ".puml");

			final SourceStringReader ssr = new SourceStringReader(Defines.createEmpty(), source, Arrays.asList(config));

			final Diagram diagram = ssr.getBlocks().get(0).getDiagram();

			try (OutputStream os = outputFile.createBufferedOutputStream()) {
				diagram.exportDiagram(os, 0, new FileFormatOption(fileFormat));
			}

			if (diagram instanceof PSystemError) {
				final ErrorUml error = getFirstError((PSystemError) diagram);
				if (error != null) {
					final String ignore = String.format("' Expect error \"%s\" on line %d",
							error.getError(), error.getLineLocation().getPosition());
					if (source.contains(ignore)) return;
					log("(The following error can be ignored by putting [%s] in the source)", ignore);
				}
				for (String line : ((PSystemError) diagram).getPureAsciiFormatted()) {
					log(line);
				}
			}
		} catch (Exception e) {
			logException(e, "Error rendering '%s'", outputFile.getPath());
			outputFile.delete();
		}
	}

	public static ErrorUml getFirstError(PSystemError system) {
		final Collection<ErrorUml> errors = system.getErrorsUml();
		return errors.isEmpty() ? null : errors.iterator().next();
	}
}
