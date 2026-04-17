package net.sourceforge.plantuml.teavm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Generates {@code EmbeddedResources.java} by reading {@code skin/plantuml.skin}
 * from the main resources directory and encoding it as a Base64 string literal.
 * <p>
 * Run this class on a standard JVM (NOT under TeaVM) from the project root:
 *
 * <pre>
 * java net.sourceforge.plantuml.teavm.EmbeddedResourcesGenerator
 * </pre>
 *
 * The generated class exposes {@code openPlantumlSkin()} which returns an
 * {@code InputStream} over the embedded skin bytes, so it works inside a
 * TeaVM-compiled context where {@code ClassLoader.getResourceAsStream()} is
 * not available.
 */
public final class EmbeddedResourcesGenerator {

	public static void main(String[] args) throws Exception {
		final File skinFile = new File("src/main/resources/skin/plantuml.skin");
		if (!skinFile.isFile())
			throw new IOException("File not found: " + skinFile.getAbsolutePath());

		final byte[] skinBytes = Files.readAllBytes(skinFile.toPath());
		final String b64 = Base64.getEncoder().encodeToString(skinBytes);

		// Split into 80-char chunks for readability (same style as the existing file)
		final int CHUNK = 80;
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b64.length(); i += CHUNK) {
			if (i > 0)
				sb.append(" +\n\t\t\t");
			sb.append('"').append(b64, i, Math.min(i + CHUNK, b64.length())).append('"');
		}

		final File output = new File(
				"src/main/java/net/sourceforge/plantuml/teavm/EmbeddedResources.java");
		if (!output.getParentFile().isDirectory())
			throw new IOException("Target directory does not exist: " + output.getParentFile());

		try (PrintStream out = new PrintStream(new FileOutputStream(output), false,
				StandardCharsets.UTF_8.name())) {
			out.println("package net.sourceforge.plantuml.teavm;");
			out.println();
			out.println("import java.io.ByteArrayInputStream;");
			out.println("import java.io.InputStream;");
			out.println("import java.util.Base64;");
			out.println();
			out.println("// Generated \u2014 do not edit");
			out.println("// Built by EmbeddedResourcesGenerator");
			out.println("public final class EmbeddedResources {");
			out.println("\tprivate EmbeddedResources() {");
			out.println("\t}");
			out.println();
			out.println("\tprivate static final String PLANTUML_SKIN_B64 =");
			out.println("\t\t\t" + sb + ";");
			out.println();
			out.println("\tpublic static InputStream openPlantumlSkin() {");
			out.println("\t\tbyte[] data = Base64.getDecoder().decode(PLANTUML_SKIN_B64);");
			out.println("\t\treturn new ByteArrayInputStream(data);");
			out.println("\t}");
			out.println("}");
		}

		System.out.println("Generated " + output.getAbsolutePath());
	}
}