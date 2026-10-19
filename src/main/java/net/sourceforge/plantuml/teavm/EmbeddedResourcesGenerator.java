package net.sourceforge.plantuml.teavm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Generates {@code EmbeddedResources.java} by reading the skins of the main
 * resources directory (see {@link #SKIN_NAMES}) and encoding them as Base64
 * string literals.
 * <p>
 * Run this class on a standard JVM (NOT under TeaVM) from the project root:
 *
 * <pre>
 * java net.sourceforge.plantuml.teavm.EmbeddedResourcesGenerator
 * </pre>
 *
 * The generated class exposes {@code openSkin(String)} (and the shortcuts
 * {@code openPlantumlSkin()} and {@code openStrictSkin()}) which return an
 * {@code InputStream} over the embedded skin bytes, so it works inside a TeaVM-compiled context where
 * {@code ClassLoader.getResourceAsStream()} is not available.
 */
public final class EmbeddedResourcesGenerator {

	/**
	 * The skins of src/main/resources/skin that {@code EmbeddedResources} embeds
	 * (without the {@code .skin} extension).
	 */
	static final String[] SKIN_NAMES = { "plantuml", "strictuml", "rose", "sonyxperiadev", "debug" };

	public static void main(String[] args) throws Exception {
		final File output = new File("src/main/java/net/sourceforge/plantuml/teavm/EmbeddedResources.java");
		if (!output.getParentFile().isDirectory())
			throw new IOException("Target directory does not exist: " + output.getParentFile());

		try (PrintStream out = new PrintStream(new FileOutputStream(output), false, StandardCharsets.UTF_8.name())) {
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
			for (String name : SKIN_NAMES) {
				final String skin = readCleanedSkin("src/main/resources/skin/" + name + ".skin");
				final String b64 = Base64.getEncoder().encodeToString(skin.getBytes(StandardCharsets.UTF_8));
				out.println();
				out.println("\tprivate static final String " + constantName(name) + " =");
				out.println("\t\t\t" + toJavaLiteral(b64) + ";");
			}
			out.println();
			out.println("\t/**");
			out.println("\t * The embedded skin of this file name (for instance \"rose.skin\"), or null.");
			out.println("\t */");
			out.println("\tpublic static InputStream openSkin(String filename) {");
			out.println("\t\tfinal String b64 = getSkinBase64(filename);");
			out.println("\t\tif (b64 == null)");
			out.println("\t\t\treturn null;");
			out.println();
			out.println("\t\tbyte[] data = Base64.getDecoder().decode(b64);");
			out.println("\t\treturn new ByteArrayInputStream(data);");
			out.println("\t}");
			out.println();
			out.println("\tprivate static String getSkinBase64(String filename) {");
			out.println("\t\tswitch (filename) {");
			for (String name : SKIN_NAMES) {
				out.println("\t\tcase \"" + name + ".skin\":");
				out.println("\t\t\treturn " + constantName(name) + ";");
			}
			out.println("\t\tdefault:");
			out.println("\t\t\treturn null;");
			out.println("\t\t}");
			out.println("\t}");
			out.println();
			out.println("\tpublic static InputStream openPlantumlSkin() {");
			out.println("\t\treturn openSkin(\"plantuml.skin\");");
			out.println("\t}");
			out.println();
			out.println("\tpublic static InputStream openStrictSkin() {");
			out.println("\t\treturn openSkin(\"strictuml.skin\");");
			out.println("\t}");
			out.println("}");
		}

		System.out.println("Generated " + output.getAbsolutePath());
	}

	private static String constantName(String skinName) {
		return skinName.toUpperCase() + "_SKIN_B64";
	}

	/**
	 * Reads a skin file, stripping line and block comments and blank lines, and
	 * returns the cleaned content joined by newlines.
	 */
	static String readCleanedSkin(String path) throws IOException {
		final File skinFile = new File(path);
		if (!skinFile.isFile())
			throw new IOException("File not found: " + skinFile.getAbsolutePath());

		final List<String> cleaned = new ArrayList<>();

		boolean inBlockComment = false;

		for (String ligne : Files.readAllLines(skinFile.toPath(), StandardCharsets.UTF_8)) {
			final String trimmed = ligne.trim();

			if (inBlockComment) {
				if (trimmed.contains("*/"))
					inBlockComment = false;
			} else {
				if (trimmed.startsWith("/*")) {
					inBlockComment = true;
					if (trimmed.endsWith("*/"))
						inBlockComment = false;
					continue;
				}
				if (trimmed.startsWith("//") || trimmed.isEmpty())
					continue;
				cleaned.add(trimmed);
			}
		}

		return String.join("\n", cleaned);
	}

	/**
	 * Splits a Base64 string into 100-char chunks rendered as a concatenated Java
	 * string literal, for readability.
	 */
	private static String toJavaLiteral(String b64) {
		final int CHUNK = 100;
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b64.length(); i += CHUNK) {
			if (i > 0)
				sb.append(" +\n\t\t\t");
			sb.append('"').append(b64, i, Math.min(i + CHUNK, b64.length())).append('"');
		}
		return sb.toString();
	}
}