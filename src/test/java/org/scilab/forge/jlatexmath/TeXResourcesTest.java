package org.scilab.forge.jlatexmath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * The resources of the library are read through TeXResources: the provider that is
 * installed (the class path on the JVM, the embedded files in the C build) and
 * nothing else. The tests put the provider back as they found it: the others use the library.
 */
class TeXResourcesTest {

	@Test
	void testAbsoluteName() {
		assertEquals("org/scilab/forge/jlatexmath/fonts/glyphs.bin",
				TeXResources.absoluteName(GlyphOutlines.class, "fonts/glyphs.bin"));
		assertEquals("org/scilab/forge/jlatexmath/DefaultTeXFont.xml",
				TeXResources.absoluteName(DefaultTeXFontParser.class, "DefaultTeXFont.xml"));
		// the package of the alphabets
		assertEquals("org/scilab/forge/jlatexmath/greek/fonts/glyphs.bin",
				TeXResources.absoluteName(org.scilab.forge.jlatexmath.greek.GreekRegistration.class, "fonts/glyphs.bin"));
		// a slash is the root, as with Class.getResourceAsStream
		assertEquals("a/b.xml", TeXResources.absoluteName(GlyphOutlines.class, "/a/b.xml"));
	}

	@Test
	void testWithoutProviderThereIsNoResource() {
		final TeXResourceProvider before = TeXResources.getProvider();
		TeXResources.setProvider(null);
		try {
			assertNull(TeXResources.open(GlyphOutlines.class, "fonts/glyphs.bin"));
		} finally {
			TeXResources.setProvider(before);
		}
	}

	@Test
	void testTheClassPathProvider() throws IOException {
		final TeXClassLoaderResources provider = new TeXClassLoaderResources();
		try (InputStream in = provider.open("org/scilab/forge/jlatexmath/fonts/glyphs.bin")) {
			assertNotNull(in);
			assertEquals('J', in.read());
		}
		assertNull(provider.open("org/scilab/forge/jlatexmath/fonts/nothing.bin"));
	}

	@Test
	void testTheProviderGetsTheWholeName() throws IOException {
		final TeXResourceProvider before = TeXResources.getProvider();
		final List<String> asked = new ArrayList<String>();
		TeXResources.setProvider(name -> {
			asked.add(name);
			return name.endsWith("known.txt") ? new ByteArrayInputStream(new byte[] { 42 }) : null;
		});
		try {
			try (InputStream in = TeXResources.open(GlyphOutlines.class, "known.txt")) {
				assertEquals(42, in.read());
			}
			assertNull(TeXResources.open(GlyphOutlines.class, "fonts/glyphs.bin"));
			assertEquals(Arrays.asList("org/scilab/forge/jlatexmath/known.txt",
					"org/scilab/forge/jlatexmath/fonts/glyphs.bin"), asked);
		} finally {
			TeXResources.setProvider(before);
		}
		assertSame(before, TeXResources.getProvider());
	}

	@Test
	void testAnErrorOfTheProviderIsNoResource() {
		final TeXResourceProvider before = TeXResources.getProvider();
		TeXResources.setProvider(name -> {
			throw new IOException("broken");
		});
		try {
			assertNull(TeXResources.open(GlyphOutlines.class, "x"));
		} finally {
			TeXResources.setProvider(before);
		}
	}

}
