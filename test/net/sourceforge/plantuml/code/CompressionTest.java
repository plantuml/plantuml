package net.sourceforge.plantuml.code;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.code.deflate.OutputStreamProtected;

class CompressionTest {

	@Test
	public void no_100_000_limit() throws Exception {
		final Compression compression = new CompressionZlib();
		compression.decompress(compression.compress(repeat("x", 100_000).getBytes(UTF_8)));
		compression.decompress(compression.compress(repeat("x", 100_001).getBytes(UTF_8)));

		final Transcoder transcoder = TranscoderUtil.getDefaultTranscoder();
		transcoder.decode(transcoder.encode(repeat("x", 100_000)));
		transcoder.decode(transcoder.encode(repeat("x", 100_001)));
	}

	@Test
	public void zip_bombing_ok() throws Exception {
		final Transcoder transcoder = TranscoderUtil.getDefaultTranscoder();
		final String encoded = transcoder.encode(repeat("x", OutputStreamProtected.MAX_OUTPUT_SIZE));
		assertTrue(encoded.length() < 10_000);
		transcoder.decode(encoded);
	}

	@Test
	public void avoid_zip_bombing_too_big() throws Exception {
		final Transcoder transcoder = TranscoderUtil.getDefaultTranscoder();
		final String encoded = transcoder.encode(repeat("x", OutputStreamProtected.MAX_OUTPUT_SIZE + 1));
		assertTrue(encoded.length() < 10_000);
		try {
			transcoder.decode(encoded);
			fail("Expected exception.");
		} catch (final NoPlantumlCompressionException e) {
			assertEquals(
					"net.sourceforge.plantuml.code.NoPlantumlCompressionException: java.util.zip.ZipException: incorrect header check",
					e.getMessage());
		}
	}

	static private String repeat(final String s, final int times) {
		final StringBuilder sb = new StringBuilder();
		for (int idx = 0; idx < times; idx += 1) {
			sb.append(s);
		}
		return sb.toString();
	}

}
