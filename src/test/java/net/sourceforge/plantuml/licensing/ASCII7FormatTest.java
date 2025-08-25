package net.sourceforge.plantuml.licensing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ASCII7FormatTest {

	@Test
	@DisplayName("Round-trip: single ASCII char encodes to 1 byte and decodes back")
	void roundTrip_singleChar() throws IOException {

		String s = "A"; // 65 -> 0b1000001, LSB-first => one byte: 65
		byte[] encoded = ASCII7Format.encodeString(s);
		assertArrayEquals(new byte[] { 65 }, encoded, "Expected single byte 65 for 'A'");
		assertEquals(s, ASCII7Format.decodeString(encoded), "Decoding must recover the original string");
	}

	@Test
	@DisplayName("Round-trip: two ASCII chars produces 2 bytes and decodes back")
	void roundTrip_twoChars() throws IOException {

		String s = "AB"; // See bit packing; expected bytes: [65, 33]
		byte[] encoded = ASCII7Format.encodeString(s);
		assertArrayEquals(new byte[] { 65, 33 }, encoded, "Expected [65,33] for 'AB' with 7-bit LSB-first packing");
		assertEquals(s, ASCII7Format.decodeString(encoded), "Decoding must recover the original string");
	}

	@Test
	@DisplayName("Length property: 8 ASCII chars compress to 7 bytes (no padding needed)")
	void length_eightChars_noPadding() throws IOException {

		String s = "ABCDEFGH"; // N = 8 -> 7N/8 is integer => 7 bytes
		byte[] encoded = ASCII7Format.encodeString(s);
		assertEquals(7, encoded.length, "8 chars should encode to exactly 7 bytes");
		assertEquals(s, ASCII7Format.decodeString(encoded), "Round-trip must match");
	}

	@Test
	@DisplayName("Length property: various sizes adhere to ceil(7*N/8) and round-trip")
	void length_variousSizes_andRoundTrip() throws IOException {

		for (int n = 1; n <= 25; n++) {
			String s = "A".repeat(n); // ASCII 65, valid in 1..127
			byte[] encoded = ASCII7Format.encodeString(s);
			int expectedLen = (7 * n + 7) / 8; // ceil(7*n/8)
			assertEquals(expectedLen, encoded.length, "Encoded length must be ceil(7*N/8) for N=" + n);
			assertEquals(s, ASCII7Format.decodeString(encoded), "Round-trip must match for N=" + n);
		}
	}

	@Test
	@DisplayName("Round-trip: random ASCII strings (1..127) survive encode/decode")
	void roundTrip_randomAscii() throws IOException {

		Random rnd = new Random(12345);
		// Generate several random strings using ASCII in 1..127
		for (int t = 0; t < 100; t++) {
			int len = 1 + rnd.nextInt(64);
			StringBuilder sb = new StringBuilder(len);
			for (int i = 0; i < len; i++) {
				int c = 1 + rnd.nextInt(127); // inclusive [1..127]
				sb.append((char) c);
			}
			String s = sb.toString();
			byte[] encoded = ASCII7Format.encodeString(s);
			String decoded = ASCII7Format.decodeString(encoded);
			assertEquals(s, decoded, "Random string must be preserved after round-trip");
		}
	}

	@Test
	@DisplayName("encodeString: null or empty must throw IllegalArgumentException")
	void encode_throwsOnNullOrEmpty() {

		assertThrows(IllegalArgumentException.class, () -> ASCII7Format.encodeString(null));
		assertThrows(IllegalArgumentException.class, () -> ASCII7Format.encodeString(""));
	}

	@Test
	@DisplayName("encodeString: character 0 (NUL) and >127 must throw IllegalArgumentException")
	void encode_throwsOnOutOfRangeChars() {

		// Contains NUL (0) — forbidden
		assertThrows(IllegalArgumentException.class, () -> ASCII7Format.encodeString("A\u0000B"));
		// Contains non-ASCII (e.g., 'é' 233) — forbidden
		assertThrows(IllegalArgumentException.class, () -> ASCII7Format.encodeString("Cafe\u00E9"));
	}

	@Test
	@DisplayName("decodeString: null or empty must throw IllegalArgumentException")
	void decode_throwsOnNullOrEmpty() {

		assertThrows(IllegalArgumentException.class, () -> ASCII7Format.decodeString(null));
		assertThrows(IllegalArgumentException.class, () -> ASCII7Format.decodeString(new byte[0]));
	}

	@Test
	@DisplayName("Padding behavior: non-multiple-of-8 lengths still decode correctly")
	void padding_behavior_decodesCorrectly() throws IOException {

		// Choose lengths with N % 8 != 0 to force padding in the final byte
		String[] samples = { "A", "AB", "HELLO", "PLANTUML", "SEVENBITS" };
		for (String s : samples) {
			byte[] encoded = ASCII7Format.encodeString(s);
			String decoded = ASCII7Format.decodeString(encoded);
			assertEquals(s, decoded, "String with padding must decode exactly");
		}
	}

	@Test
	@DisplayName("Stress: long string round-trip (e.g., 10k chars)")
	void stress_longString() throws IOException {

		StringBuilder sb = new StringBuilder(10_000);
		for (int i = 0; i < 10_000; i++) {
			// Cycle through 1..127 to stay within the allowed range
			char c = (char) ((i % 127) + 1);
			sb.append(c);
		}
		String s = sb.toString();
		byte[] encoded = ASCII7Format.encodeString(s);
		String decoded = ASCII7Format.decodeString(encoded);
		assertEquals(s, decoded, "Long string must round-trip correctly");
	}
}
