package net.sourceforge.plantuml.nio;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import net.sourceforge.plantuml.security.SURL;

/**
 * Integration test for {@link InputFileUrl}.
 *
 * <p>This test opens a remote HTTPS connection to https://plantuml.com and
 * verifies that the returned HTML contains a &lt;title&gt; tag.
 * The test is skipped automatically if the network is unavailable.
 */
class InputFileUrlTest {

    @Test
    @Timeout(10) // prevent the test from hanging if the network is slow
    void testNewInputStream_containsTitle() throws IOException {
        final InputFileUrl cut = new InputFileUrl(SURL.create("https://plantuml.com"));

        // Attempt to open the URL; skip the test if offline
        InputStream raw;
        try {
            raw = cut.newInputStream();
        } catch (IOException e) {
            assumeTrue(false, "Network unavailable, skipping test: " + e.getMessage());
            return; // safeguard, though assumeTrue already aborts
        }

        try (InputStream is = new BufferedInputStream(raw)) {
            final String html = readUtf8Limited(is, 256 * 1024); // limit read to 256 KB
            assertNotNull(html, "HTML content should not be null");
            assertTrue(html.toLowerCase(Locale.ROOT).contains("<title"),
                    "Expected the HTML to contain a <title> tag");
        }
    }

    /** Reads up to {@code maxBytes} bytes from the stream as a UTF-8 string. */
    private static String readUtf8Limited(InputStream is, int maxBytes) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream(Math.min(maxBytes, 8192));
        final byte[] buffer = new byte[8192];
        int total = 0;
        int n;
        while ((n = is.read(buffer)) != -1) {
            if (total + n > maxBytes) {
                out.write(buffer, 0, maxBytes - total);
                break;
            }
            out.write(buffer, 0, n);
            total += n;
        }
        return out.toString(StandardCharsets.UTF_8);
    }
}
