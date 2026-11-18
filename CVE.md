# Security advisories (CVE) and PlantUML

This page lists recent CVEs that users asked us about, and explains why
PlantUML itself is **not affected** by each of them.

"Not affected" means the PlantUML code does not contain the vulnerable code
and does not reach it. It does not cover the Java runtime or the other
software PlantUML is deployed with: keep them up to date.

| CVE | Component | PlantUML affected? |
|-----|-----------|--------------------|
| [CVE-2026-114938](#cve-2026-114938-java4shell-hotspot-base64-intrinsic) | HotSpot JVM, x86-64 Base64 intrinsic | **No.** Update your JDK anyway. |
| [CVE-2026-111497](#cve-2026-111497-node2shell-brotli-c-decoder) | Google Brotli C decoder | **No** |

---

## CVE-2026-114938 ("Java4Shell", HotSpot Base64 intrinsic)

- Record: <https://www.cve.org/CVERecord?id=CVE-2026-114938>
- Vendor advisory: <https://www.oracle.com/security-alerts/cspunov2026.html>
- Write-up: <https://calif.io/java4shell>

### The vulnerability

The bug is in the **Java Virtual Machine**, not in a Java library. On x86-64
CPUs with AVX-512 VBMI (`avx512f`, `avx512bw`, `avx512vbmi`, `bmi2`), the C2
JIT compiler replaces hot calls to `java.util.Base64.Decoder` with a
hand-written assembly routine (an *intrinsic*). That routine gets the padding
of the last characters (`=`, `==`, ...) wrong and writes past the end of the
output buffer. The result is heap corruption, which can lead to remote code
execution.

It is exploitable only if **all** of these are true:

1. the JVM is an affected build (OpenJDK 17.0.6 and later, from any vendor),
   without the November 2026 fix;
2. the CPU supports AVX-512 VBMI;
3. the application decodes **attacker-controlled** Base64 with
   `java.util.Base64`, often enough for C2 to compile that code.

### Why PlantUML is not affected

PlantUML never passes user input to `java.util.Base64`:

- **All the Base64 that comes from users is decoded by
  `net.sourceforge.plantuml.utils.Base64Coder`**, a decoder written in plain
  Java. The JVM does not replace it with the vulnerable intrinsic. This
  covers:
  - sprites (`CommandSpriteBase64`, `CommandSpriteMd5`);
  - `data:` images in Creole text (`AtomImg`);
  - images embedded in SVG (`SvgSaxParser`).
- **`java.util.Base64.getDecoder()` appears in two places only**:
  `teavm/EmbeddedResources` and `teavm/js/JlatexmathResourcesJs`. Both are
  TeaVM code (compiled to JavaScript or C, never run on HotSpot), and they
  decode resources embedded at build time, never user data.
- **The built-in web server (`-picoweb`)** decodes diagram URLs with
  PlantUML's own encoders (`AsciiEncoder`, `AsciiEncoderHex`), not with
  `java.util.Base64`.

### What you should still do

PlantUML runs on a JVM, and that JVM can still be vulnerable through other
code running in the same process: a servlet container, an authentication
layer or third-party libraries, as in `plantuml-server` or wiki and issue
tracker integrations. The advisory also mentions Java clients that connect to
a malicious TLS server. PlantUML opens such connections for `!include <url>`
and `<img:http...>`, and that code path is in the JDK, not in PlantUML.

- Update your JDK to a build with the November 2026 fix.
- Until then, start the JVM with `-XX:-UseBASE64Intrinsics`, or with
  `-XX:UseAVX=2`. Use `-XX:UseAVX=2` on x86-64 only: an ARM JVM does not
  start with it.

---

## CVE-2026-111497 ("Node2Shell", Brotli C decoder)

- Record: <https://www.cve.org/CVERecord?id=CVE-2026-111497>
- Advisory: <https://github.com/advisories/GHSA-9v8h-qqgm-4m35>
- Fix: google/brotli commit `0ed4268` ("Detect and report invalid stream
  earlier"), released in Brotli 1.0.10, 1.1.1 and 1.2.1

### The vulnerability

This is an out-of-bounds write in Google's Brotli decoder written in C
(`c/dec/decode.c`), versions 1.0.3 to 1.2.0. If a stream has a single block
type, its block length counter can still reach zero. The decoder then tries a
"block switch", which fails, but the return value is ignored (CWE-252).
Decoding goes on with an inconsistent state, and the memory around it gets
corrupted.

### Why PlantUML is not affected

PlantUML ships two Brotli decoders. Neither is vulnerable, and neither ever
decodes data that comes from a user.

**1. The vendored C decoder (`src/teavm-c/native/brotli/`)**

- It was copied from upstream commit `7b93874f` (see its `README.md`), which
  **already contains the fix**:
  - `DecodeBlockTypeAndLength` returns `BROTLI_DECODER_ERROR_FORMAT_BLOCK_SWITCH`;
  - the three block switches are wrapped in `BROTLI_SAFE_WITH_STATUS`;
  - `decode.h` declares error code `-17` (`BLOCK_SWITCH`).
- Its `common/version.h` still says `1.2.0`, because that is the version
  number upstream had at that commit. A scanner that only reads the version
  number can therefore report it as vulnerable: that is a false positive.
- It only decompresses the standard library `.spm` files embedded in the
  executable at build time (`plantuml_resources.c`).

**2. The Java port (`net.sourceforge.plantuml.brotli`)**

- The bug cannot happen there. With a single block type, the block length is
  set to `1 << 28` (`Decode.readMetablockPartition`), while a meta-block holds
  at most 2^24 bytes. The counter never reaches zero, so no block switch is
  attempted.
- Even if it were, this is Java: an out-of-bounds access throws an exception
  and does not corrupt memory.
- It only decodes data that ships with PlantUML: the standard library
  (`SpmChannel`), the bundled Graphviz archive (`WindowsDotArchive`) and the
  donors list (`PSystemDonors`).

Diagram URLs (for instance in `-picoweb`) use Deflate (zlib, zip or gzip),
the legacy Huffman encoding or no compression at all, never Brotli.
