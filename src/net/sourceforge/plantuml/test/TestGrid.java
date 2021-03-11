package net.sourceforge.plantuml.test;

import java.io.File;
import java.io.IOException;

import static net.sourceforge.plantuml.test.TestGridMarkdown.writeGridAsMarkdown;
import static net.sourceforge.plantuml.test.TestGridPng.writeGridAsPng;
import static net.sourceforge.plantuml.test.TestUtils.TEST_OUTPUT_DIR;

public class TestGrid {
	public final String[] rowNames;
	public final String[] colNames;
	public final File[][] files;

	public TestGrid(String[] rowNames, String[] colNames, File[][] paths) {
		this.rowNames = rowNames;
		this.colNames = colNames;
		this.files = paths;
	}

	public TestGrid markdown(String outputFile) throws IOException {
		writeGridAsMarkdown(this, TEST_OUTPUT_DIR.file(outputFile + ".md"));
		return this;
	}

	public TestGrid png(String outputFile) throws IOException {
		writeGridAsPng(this, TEST_OUTPUT_DIR.file(outputFile + ".png"));
		return this;
	}
}
