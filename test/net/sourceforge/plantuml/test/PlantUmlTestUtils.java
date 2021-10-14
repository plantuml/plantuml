package net.sourceforge.plantuml.test;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.security.ImageIO;

public class PlantUmlTestUtils {

	public static ExportDiagram exportDiagram(String... source) {
		final SourceStringReader ssr = new SourceStringReader(StringTestUtils.join("\n", source));

		final List<BlockUml> blocks = ssr.getBlocks();
		if (blocks.isEmpty()) throw new AssertionError("There is no diagram");

		final Diagram diagram = blocks.get(0).getDiagram();
		return new ExportDiagram(diagram);
	}

	public static class ExportDiagram {
		private final Diagram diagram;
		private boolean metadata;

		public ExportDiagram(Diagram diagram) {
			this.diagram = diagram;
		}

		public ExportDiagram assertNoError() {
			if (diagram instanceof PSystemError) {
				final PSystemError error = (PSystemError) this.diagram;
				throw new AssertionError("Diagram has an error: " + StringTestUtils.join("\n", error.getPureAsciiFormatted()));
			}
			return this;
		}

		public byte[] asByteArray(FileFormat fileFormat) throws IOException {
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			stream(os, fileFormat);
			return os.toByteArray();
		}

		public BufferedImage asImage() throws IOException {
			return ImageIO.read(asByteArray(FileFormat.PNG));
		}

		public String asString() throws IOException {
			return asString(FileFormat.UTXT);
		}

		public String asString(FileFormat fileFormat) throws IOException {
			return new String(asByteArray(fileFormat), UTF_8);
		}

		public ExportDiagram stream(OutputStream os, FileFormat fileFormat) throws IOException {
			final FileFormatOption fileFormatOption = new FileFormatOption(fileFormat, metadata);
			diagram.exportDiagram(os, 0, fileFormatOption);
			return this;
		}

		public ExportDiagram withMetadata(boolean metadata) {
			this.metadata = metadata;
			return this;
		}
	}
}
