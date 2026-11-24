package net.sourceforge.plantuml.preproc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.preproc.spm.SpmChannel;
import net.sourceforge.plantuml.text.StringLocated;

/**
 * An SVG sprite written with variables in a procedure of the standard library
 * ("sprite $b##$name##Icon &lt;svg ... fill="$linecolor"&gt;" in edgy2) is built
 * at each call of the procedure: it must not be exported to svg.spm, where it
 * would be stored once under its unexpanded name and never found.
 */
class StdlibSvgSpriteTest {

	@Test
	void noSvgSpriteOfTheStandardLibraryHasVariables() throws IOException {
		final List<String> withVariables = new ArrayList<>();
		for (String lib : Stdlib.getAllFolderNames()) {
			final InputStream is;
			try {
				is = SpmChannel.SVG.getInternalInputStream(lib);
			} catch (IOException e) {
				// this library has no SVG sprite
				continue;
			}
			try (DataInputStream dis = new DataInputStream(is)) {
				final int nb = dis.readInt();
				for (int i = 0; i < nb; i++) {
					final String name = dis.readUTF();
					final String svg = dis.readUTF();
					if (name.contains("$") || name.contains("#") || svg.contains("$"))
						withVariables.add(lib + ":" + name);
				}
			}
		}
		assertEquals("[]", withVariables.toString());
	}

	@Test
	void anEdgySpriteDeclaredInAProcedureHasTheColorsOfTheCall() {
		final List<StringLocated> data = new SourceStringReader(
				"@startuml\n!include <edgy/edgy2>\nrectangle \"<$bActivityIcon>\" as r\n@enduml\n").getBlocks().get(0)
				.getData();
		String activity = null;
		for (StringLocated line : data)
			if (line.getString().startsWith("sprite $bActivityIcon "))
				activity = line.getString();
		assertTrue(activity != null && activity.startsWith("sprite $bActivityIcon <svg viewBox=\"0 0 160 26\">"),
				String.valueOf(activity));
		assertTrue(activity.contains(" fill=\"#262626\"/>") && activity.contains(" fill=\"#fff\"/>")
				&& activity.contains(">Activity</text>"), activity);
	}

}
