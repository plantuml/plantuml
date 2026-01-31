package test.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.security.SImageIO;

public class PlantUmlTestUtils {

	public static ExportDiagram exportDiagram(String... source) {
		final SourceStringReader ssr = new SourceStringReader(String.join("\n", source));

		final List<BlockUml> blocks = ssr.getBlocks();
		if (blocks.isEmpty()) throw new AssertionError("There is no diagram");

		final Diagram diagram = blocks.get(0).getDiagram();
		return new ExportDiagram(diagram);
	}

	@SuppressWarnings("unused")
	public static class ExportDiagram {
		private final Diagram diagram;
		private boolean metadata;

		public ExportDiagram(Diagram diagram) {
			this.diagram = diagram;
		}

		public Diagram getDiagram(){
			return diagram;
		}

		public ExportDiagram assertDiagramType(Class<? extends Diagram> klass) {
			assertNoError();
			assertThat(diagram).isInstanceOf(klass);
			return this;
		}

		public ExportDiagram assertNoError() {
			if (diagram instanceof PSystemError) {
				final PSystemError error = (PSystemError) this.diagram;
				throw new AssertionError("Diagram has an error: " + String.join("\n", error.getPureAsciiFormatted()));
			}
			return this;
		}

		/**
		 * Check if the exported content contains error indicators.
		 * This detects runtime errors that occur during export (not parsing).
		 * 
		 * @param content The exported content (SVG or PNG as string/bytes)
		 * @return true if content contains error indicators, false otherwise
		 */
		public boolean hasRuntimeError(byte[] content) {
			if (content == null || content.length == 0) {
				return true;
			}
			
			// Convert to string to check for error signatures
			String contentStr = new String(content, UTF_8);
			
			// Check for PlantUML error signatures
			return contentStr.contains("An error has occured") || 
			       contentStr.contains("PlantUML") && contentStr.contains("has crashed") ||
			       contentStr.contains("java.lang.") && contentStr.contains("Exception");
		}

		/**
		 * Assert that the exported diagram doesn't contain runtime errors.
		 * Checks both parsing errors and export-time errors.
		 * 
		 * @param fileFormat The format to check
		 * @return this ExportDiagram for chaining
		 * @throws IOException if export fails
		 */
		public ExportDiagram assertNoRuntimeError(FileFormat fileFormat) throws IOException {
			assertNoError(); // Check parsing errors first
			
			byte[] content = asByteArray(fileFormat);
			if (hasRuntimeError(content)) {
				String preview = new String(content, UTF_8);
				// Limit preview to first 500 chars
				if (preview.length() > 500) {
					preview = preview.substring(0, 500) + "...";
				}
				throw new AssertionError("Exported diagram contains runtime error. Preview: " + preview);
			}
			return this;
		}


		public byte[] asByteArray(FileFormat fileFormat) throws IOException {
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			stream(os, fileFormat);
			return os.toByteArray();
		}

		public BufferedImage asImage() throws IOException {
			return asImage(FileFormat.PNG);
		}

		public BufferedImage asImage(FileFormat fileFormat) throws IOException {
			return SImageIO.read(asByteArray(fileFormat));
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

		public ExportDiagram toFile(Path path, FileFormat fileFormat) throws IOException {
			try (OutputStream os = Files.newOutputStream(path)) {
				return stream(os, fileFormat);
			}
		}

		public ExportDiagram withMetadata(boolean metadata) {
			this.metadata = metadata;
			return this;
		}
	}
}
