package net.sourceforge.plantuml.preproc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.preproc.spm.SpmChannel;

/**
 * No sprite of the bundled standard library may be stored with variables.
 * <p>
 * A sprite declared in a procedure, such as
 * <code>sprite $b##$name##Icon &lt;svg ... fill="$linecolor"&gt;</code> in
 * edgy2, only exists once the preprocessor has run. When the generator of the
 * .spm files exported it, it was stored once under its unexpanded name
 * (<code>edgy2:b##$name##Icon</code>), every call of the procedure asked for a
 * name that did not exist, and the icon drew nothing (#3039). The generator in
 * plantuml-stdlib now leaves such sprites in the text; this checks the data
 * that is actually bundled, whatever generator produced it.
 * <p>
 * The rule is the generator's: a <code>$</code> or a <code>##</code> in the
 * name (the leading <code>$</code> is never part of the exported name), or a
 * <code>$</code> in the SVG.
 * <p>
 * And every sprite that a library declares from its own data
 * (<code>sprite $x :lib:file:name</code> for SVG, <code>sprite $x #lib#name</code>
 * for bitmaps) must be found in that data: a missing one only draws an
 * "(Unknown sprite ...)" message.
 */
class StdlibSvgSpriteTest {

	private static final Pattern SVG_REFERENCE = Pattern.compile("^\\s*sprite\\s+\\$?\\S+\\s*:([^:\\s]+):(\\S+)\\s*$");
	private static final Pattern BITMAP_REFERENCE = Pattern.compile("^\\s*sprite\\s+\\$\\S+\\s*#([^#\\s]+)#(\\S+)\\s*$");

	@Test
	void everySpriteDeclaredByALibraryIsInItsData() throws IOException {
		final List<String> missing = new ArrayList<>();
		for (String lib : Stdlib.getAllFolderNames()) {
			if (isBundled(lib, "puml.spm") == false)
				continue;

			try (DataInputStream dis = new DataInputStream(SpmChannel.PUML.getInternalInputStream(lib))) {
				final int nb = dis.readInt();
				for (int i = 0; i < nb; i++) {
					final String file = dis.readUTF();
					final byte[] data = new byte[dis.readInt()];
					dis.readFully(data);
					for (String line : new String(data, StandardCharsets.UTF_8).split("\n")) {
						final Matcher svg = SVG_REFERENCE.matcher(line);
						final Matcher bitmap = BITMAP_REFERENCE.matcher(line);
						if (svg.find()) {
							if (Stdlib.retrieve(svg.group(1)).readSvgSprite(svg.group(2)) == null)
								missing.add(lib + "/" + file + " :" + svg.group(1) + ":" + svg.group(2));
						} else if (bitmap.find()) {
							if (Stdlib.retrieve(bitmap.group(1)).readSprite(bitmap.group(2)) == null)
								missing.add(lib + "/" + file + " #" + bitmap.group(1) + "#" + bitmap.group(2));
						}
					}
				}
			}
		}
		assertEquals("[]", missing.toString());
	}

	@Test
	void noSvgSpriteIsStoredWithVariables() throws IOException {
		final List<String> withVariables = new ArrayList<>();
		for (String lib : Stdlib.getAllFolderNames()) {
			if (isBundled(lib, "svg.spm") == false)
				continue;

			try (DataInputStream dis = new DataInputStream(SpmChannel.SVG.getInternalInputStream(lib))) {
				final int nb = dis.readInt();
				for (int i = 0; i < nb; i++) {
					final String name = dis.readUTF();
					final String svg = dis.readUTF();
					if (hasVariables(name) || svg.contains("$"))
						withVariables.add(lib + ":" + name);
				}
			}
		}
		assertEquals("[]", withVariables.toString());
	}

	@Test
	void noBitmapSpriteIsStoredWithVariables() throws IOException {
		final List<String> withVariables = new ArrayList<>();
		for (String lib : Stdlib.getAllFolderNames()) {
			if (isBundled(lib, "sprite.spm") == false)
				continue;

			try (DataInputStream dis = new DataInputStream(SpmChannel.SPRITE.getInternalInputStream(lib))) {
				final int nb = dis.readInt();
				for (int i = 0; i < nb; i++) {
					final String name = dis.readUTF();
					final int width = dis.readInt();
					final int height = dis.readInt();
					// as in Stdlib.readSprite: one byte per column for each pair of rows
					dis.readFully(new byte[width * ((height + 1) / 2)]);
					if (hasVariables(name))
						withVariables.add(lib + ":" + name);
				}
			}
		}
		assertEquals("[]", withVariables.toString());
	}

	private static boolean hasVariables(String name) {
		return name.contains("$") || name.contains("##");
	}

	/**
	 * Not every library has every channel, and an alias (link=) has none: the
	 * resource is looked for first, so that an error while reading a file that
	 * exists is not mistaken for a missing file.
	 */
	private static boolean isBundled(String lib, String file) {
		return StdlibSvgSpriteTest.class.getResource("/stdlib/" + lib + "/" + file) != null;
	}

}
