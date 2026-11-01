package net.sourceforge.plantuml.teavm.c;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * The names of the files of JLaTeXMath that the C build embeds: copyNativeSources
 * (build.gradle.kts) puts them under native/resources/jlatexmath, and the embedded
 * name is the path under that folder.
 */
class JlatexmathResourcesTest {

	@Test
	void testEmbeddedName() {
		assertEquals("jlatexmath/DefaultTeXFont.xml",
				JlatexmathResources.embeddedName("org/scilab/forge/jlatexmath/DefaultTeXFont.xml"));
		assertEquals("jlatexmath/fonts/base/jlm_cmr10.xml",
				JlatexmathResources.embeddedName("org/scilab/forge/jlatexmath/fonts/base/jlm_cmr10.xml"));
		assertEquals("jlatexmath/greek/fonts/glyphs.bin",
				JlatexmathResources.embeddedName("org/scilab/forge/jlatexmath/greek/fonts/glyphs.bin"));
	}

	@Test
	void testOtherNamesAreNotOurs() {
		assertNull(JlatexmathResources.embeddedName("org/openpdf/text/pdf/fonts/Helvetica.afm"));
		assertNull(JlatexmathResources.embeddedName("themes/puml-theme-amiga.puml"));
		assertNull(JlatexmathResources.embeddedName("org/scilab/forge/jlatexmathx/a.xml"));
	}

}
