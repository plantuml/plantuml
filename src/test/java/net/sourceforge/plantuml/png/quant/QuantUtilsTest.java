package net.sourceforge.plantuml.png.quant;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

class QuantUtilsTest {

    // Slow reference: apply (x & 0xFE) | ((x >>> 7) & 1) to each byte A,R,G,B
    private static int referenceCompressPackedARGB(int argb) {
        return (perByte(argb >>> 24) << 24)
             | (perByte(argb >>> 16) << 16)
             | (perByte(argb >>>  8) <<  8)
             |  perByte(argb);
    }

    private static int perByte(int x) {
        x &= 0xFF;
        return (x & 0xFE) | ((x >>> 7) & 0x01);
    }

    @Test
    void testBlackAndWhiteInvariant() {
        assertEquals(0x00000000, QuantUtils.compressPackedARGB(0x00000000),
                "Pure black must remain unchanged");
        assertEquals(0xFFFFFFFF, QuantUtils.compressPackedARGB(0xFFFFFFFF),
                "Pure white must remain unchanged");
    }

    @Test
    void testKnownExamplesPerByte() {
        // Byte 0x7F (0111_1111) -> MSB=0, new LSB=0 : 0x7E
        int v = 0x7F;
        assertEquals(0x7E, perByte(v));

        // Byte 0x80 (1000_0000) -> MSB=1, new LSB=1 : 0x81
        v = 0x80;
        assertEquals(0x81, perByte(v));

        // Pixel ARGB: A=0x7F, R=0x80, G=0x55, B=0xAA
        int argb = (0x7F << 24) | (0x80 << 16) | (0x55 << 8) | 0xAA;
        int expected = (perByte(0x7F) << 24) | (perByte(0x80) << 16) | (perByte(0x55) << 8) | perByte(0xAA);
        assertEquals(expected, QuantUtils.compressPackedARGB(argb));
    }

    @Test
    void testAllByteValuesEachChannel() {
        // For every byte value 0..255, verify A-only, R-only, G-only, and B-only cases
        for (int b = 0; b < 256; b++) {
            // A-only
            int a = b << 24;
            assertEquals(referenceCompressPackedARGB(a), QuantUtils.compressPackedARGB(a));
            // R-only
            int r = b << 16;
            assertEquals(referenceCompressPackedARGB(r), QuantUtils.compressPackedARGB(r));
            // G-only
            int g = b << 8;
            assertEquals(referenceCompressPackedARGB(g), QuantUtils.compressPackedARGB(g));
            // B-only
            int bl = b;
            assertEquals(referenceCompressPackedARGB(bl), QuantUtils.compressPackedARGB(bl));
        }
    }

    @Test
    void testOnlyLSBChanges() {
        // (x ^ y) must have bits set only at the LSB position of each byte (bits 0,8,16,24)
        final int NON_LSB_MASK = 0xFEFEFEFE;
        Random rnd = new Random(123456789L);
        for (int i = 0; i < 10_000; i++) {
            int x = rnd.nextInt();
            int y = QuantUtils.compressPackedARGB(x);
            assertEquals(0, (x ^ y) & NON_LSB_MASK,
                    "Only the LSB of each byte may change");
        }
    }

    @Test
    void testLSBBecomesMSB() {
        // The new LSB of each byte must equal the original MSB of that byte
        Random rnd = new Random(987654321L);
        for (int i = 0; i < 10_000; i++) {
            int x = rnd.nextInt();
            int y = QuantUtils.compressPackedARGB(x);
            int newLSBs   = y & 0x01010101;
            int oldMSBs01 = (x >>> 7) & 0x01010101;
            assertEquals(oldMSBs01, newLSBs, "LSB of each byte must equal the original MSB");
        }
    }

    @Test
    void testIdempotence() {
        // Applying the operation twice should yield the same result as once
        Random rnd = new Random(424242L);
        for (int i = 0; i < 10_000; i++) {
            int x = rnd.nextInt();
            int y1 = QuantUtils.compressPackedARGB(x);
            int y2 = QuantUtils.compressPackedARGB(y1);
            assertEquals(y1, y2, "Operation must be idempotent");
        }
    }

    @Test
    void testMatchesReferenceImplementation() {
        // Fast packed implementation must match the slow per-byte reference
        Random rnd = new Random(20250827L);
        for (int i = 0; i < 10_000; i++) {
            int x = rnd.nextInt();
            assertEquals(referenceCompressPackedARGB(x), QuantUtils.compressPackedARGB(x),
                    "Packed implementation must match the per-byte reference");
        }
    }

}
