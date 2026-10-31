#!/usr/bin/env python3
r"""
Runs the native executable built by compilec.bat (plantuml.exe) on the Vega tests.

Vega is the regression harness of PlantUML (src/test/java/test/vega/VegaTest.java):
every .puml file under src/test/resources/vega/ is rendered, and the output is
compared with the reference file stored next to it (hello.puml -> hello.svg).
This script does the same with the C executable instead of the JVM.

The C build is still partial (mindmap only, SVG / PNG / PDF / --preproc), so many tests
are expected to fail. The script only reports; it does not decide what is a
regression.

What is checked, per test file:
  * output: svg      -> `plantuml.exe --svg-vega`, compared (XML-normalised, as SvgCleaner
                        does in the Java tests) with <name>.svg / <name>-N.svg
  * output: preproc  -> `plantuml.exe --preproc`, compared with <name>.preproc
  * output: pdf      -> `plantuml.exe --pdf-vega` (deterministic PDF: fixed dates and /ID, no
                        compression), compared byte for byte with <name>.pdf / <name>-N.pdf
  * any other output format, or a test that expects an error (expected-exception,
    expected-error-*, expected-status): reported as "skipped".
The other expectations of the YAML header (expected-description, expected-stderr,
expected-image-count...) are not checked.

Usage (from anywhere):
    python tools/vega-c/run_vega_c.py                       # all the tests
    python tools/vega-c/run_vega_c.py mindmap               # paths containing "mindmap"
    python tools/vega-c/run_vega_c.py --exe path\to\plantuml.exe -j 8 --diff

Each test is also run with `-tpng` (unless --no-png): there is no reference file,
it only checks that the executable does not crash, hang, or write something that
is not a PNG file. The PNG results are reported apart from the pass / fail above.

Results go to <repo>/build/vega-c/ (git-ignored): report.json, summary.txt,
with --diff one .diff file per failed test, and output/ with the files
the executable wrote (svg, png...), in the same folders as the tests.
"""

import argparse
import base64
import concurrent.futures
import difflib
import hashlib
import json
import os
import re
import shutil
import struct
import subprocess
import sys
import tempfile
import time
import zlib
import xml.etree.ElementTree as ET
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parents[2]
VEGA_DIR = REPO_ROOT / "src" / "test" / "resources" / "vega"
OUT_DIR = REPO_ROOT / "build" / "vega-c"

YAML_DELIMITER = "---"

# output format of the YAML header -> (command line flags, extension of the produced
# and of the reference file)
SUPPORTED_FORMATS = {
    "svg": (["--svg-vega"], ".svg"),
    "preproc": (["--preproc"], ".preproc"),
    "pdf": (["--pdf-vega"], ".pdf"),
}

# Keys of the YAML header that make a test expect an error: the C build does not
# check them, so these tests are skipped.
ERROR_EXPECTATION_KEYS = (
    "expected-exception",
    "expected-error-line",
    "expected-error-message",
    "expected-status",
)

PASS, FAIL, ERROR, TIMEOUT, SKIPPED = "pass", "fail", "error", "timeout", "skipped"

# Result of the PNG run (which only checks that the executable does not crash)
PNG_OK, PNG_CRASH, PNG_TIMEOUT, PNG_NO_OUTPUT, PNG_INVALID = "ok", "crash", "timeout", "no-output", "invalid"
PNG_SIGNATURE = b"\x89PNG\r\n\x1a\n"

# Prefix of the temporary copy of a test without its YAML header (see write_source_copy)
COPY_PREFIX = "~vega-c-"


# ---------------------------------------------------------------------------
# Test file parsing
# ---------------------------------------------------------------------------

def read_header(path):
    """Returns the top-level `key: value` pairs of the YAML header (empty if none).

    Same delimiting rule as VegaInputFile.parse: the first two lines that are
    `---` enclose the header. PlantUML ignores everything outside @startuml /
    @enduml, so the .puml file can be given as is to the executable.
    """
    lines = path.read_text(encoding="utf-8", errors="replace").splitlines()
    header = []
    inside = False
    for line in lines:
        if line.strip() == YAML_DELIMITER:
            if not inside:
                inside = True
                continue
            break
        if inside:
            header.append(line)
    else:
        if inside:  # no closing delimiter: not a header
            header = []

    result = {}
    for line in header:
        match = re.match(r"^([A-Za-z0-9_-]+)\s*:\s*(.*?)\s*$", line)
        if match:
            value = re.sub(r"\s+#.*$", "", match.group(2))
            result[match.group(1)] = value
    return result


def write_source_copy(path):
    """Writes next to the test a copy without the YAML header, and returns its path.

    The Java tests give PlantUML only the lines after the header (VegaInputFile.parse),
    so the line numbers of the output (data-source-line) start at 1. The copy is in
    the same folder as the test, so that the relative !include keep working.
    """
    lines = path.read_text(encoding="utf-8", errors="replace").splitlines()
    kept = []
    inside = False
    done = False
    for line in lines:
        if not done and line.strip() == YAML_DELIMITER:
            if inside:
                done = True
            inside = not done
            continue
        if not inside:
            kept.append(line)
    copy = path.with_name(COPY_PREFIX + path.name)
    copy.write_text("\n".join(kept) + "\n", encoding="utf-8", newline="\n")
    return copy


def output_formats(header):
    value = header.get("output")
    if value is None:
        return ["svg"]
    return [token.strip().lower() for token in value.split(",") if token.strip()]


# ---------------------------------------------------------------------------
# Normalisation of the SVG (port of test.vega.SvgCleaner)
# ---------------------------------------------------------------------------

ET.register_namespace("", "http://www.w3.org/2000/svg")
ET.register_namespace("xlink", "http://www.w3.org/1999/xlink")


def _tidy(element):
    """Drops the whitespace-only text nodes and sorts the attributes."""
    if element.text is not None and not element.text.strip():
        element.text = None
    if element.tail is not None and not element.tail.strip():
        element.tail = None
    if isinstance(element.tag, str):
        element.attrib = dict(sorted(element.attrib.items()))
    for child in element:
        _tidy(child)


PNG_HREF = re.compile(r"data:image/png;base64,([A-Za-z0-9+/=\s]+)")


def png_fingerprint(base64_text):
    """Width, height and a hash of the RGBA pixels of a PNG (8 bits per channel,
    not interlaced): two encoders write different bytes for the same image, only
    the pixels are compared. Anything else is kept as it is."""
    try:
        data = base64.b64decode(base64_text)
        if data[:8] != b"\x89PNG\r\n\x1a\n":
            return None
        position = 8
        palette = None
        transparency = None
        idat = b""
        header = None
        while position + 8 <= len(data):
            length, kind = struct.unpack(">I4s", data[position:position + 8])
            body = data[position + 8:position + 8 + length]
            position += 12 + length
            if kind == b"IHDR":
                header = struct.unpack(">IIBBBBB", body)
            elif kind == b"PLTE":
                palette = body
            elif kind == b"tRNS":
                transparency = body
            elif kind == b"IDAT":
                idat += body
        width, height, depth, color_type, _, _, interlace = header
        channels = {0: 1, 2: 3, 3: 1, 4: 2, 6: 4}[color_type]
        if depth != 8 or interlace != 0:
            return None
        raw = zlib.decompress(idat)
        stride = width * channels
        rows = []
        previous = bytearray(stride)
        offset = 0
        for _ in range(height):
            filter_type = raw[offset]
            line = bytearray(raw[offset + 1:offset + 1 + stride])
            offset += 1 + stride
            for i in range(stride):
                left = line[i - channels] if i >= channels else 0
                up = previous[i]
                upper_left = previous[i - channels] if i >= channels else 0
                if filter_type == 1:
                    line[i] = (line[i] + left) & 255
                elif filter_type == 2:
                    line[i] = (line[i] + up) & 255
                elif filter_type == 3:
                    line[i] = (line[i] + ((left + up) >> 1)) & 255
                elif filter_type == 4:
                    p = left + up - upper_left
                    pa, pb, pc = abs(p - left), abs(p - up), abs(p - upper_left)
                    predictor = left if pa <= pb and pa <= pc else up if pb <= pc else upper_left
                    line[i] = (line[i] + predictor) & 255
            rows.append(line)
            previous = line
        rgba = bytearray()
        for line in rows:
            for x in range(width):
                if color_type == 6:
                    rgba += line[4 * x:4 * x + 4]
                elif color_type == 2:
                    rgba += line[3 * x:3 * x + 3] + b"\xff"
                elif color_type == 0:
                    rgba += bytes([line[x]] * 3) + b"\xff"
                elif color_type == 4:
                    rgba += bytes([line[2 * x]] * 3) + line[2 * x + 1:2 * x + 2]
                else:
                    index = line[x]
                    alpha = transparency[index] if transparency and index < len(transparency) else 255
                    rgba += palette[3 * index:3 * index + 3] + bytes([alpha])
        # The hash tells whether two images are the same; the most frequent
        # colors help to see why they are not
        counts = {}
        for i in range(0, len(rgba), 4):
            key = bytes(rgba[i:i + 4]).hex()
            counts[key] = counts.get(key, 0) + 1
        top = sorted(counts.items(), key=lambda item: (-item[1], item[0]))[:4]
        return "png:%dx%d:%s:%d colors:%s" % (width, height, hashlib.sha1(bytes(rgba)).hexdigest(),
                                              len(counts), ",".join("%s*%d" % item for item in top))
    except Exception:
        return None


def normalise_png_images(text):
    """Replaces each embedded PNG by the fingerprint of its pixels."""
    def replace(match):
        fingerprint = png_fingerprint("".join(match.group(1).split()))
        return fingerprint if fingerprint else match.group(0)
    return PNG_HREF.sub(replace, text)


def normalise_svg(data):
    """Parses and pretty-prints, so that only a real change makes a difference.

    Processing instructions are dropped (the parser ignores them), comments are
    kept, as the Java normaliser does.
    """
    text = data.decode("utf-8", errors="replace") if isinstance(data, bytes) else data
    text = text.replace("\r\n", "\n").replace("\r", "\n")
    text = normalise_png_images(text)
    parser = ET.XMLParser(target=ET.TreeBuilder(insert_comments=True))
    root = ET.fromstring(text.encode("utf-8"), parser=parser)
    _tidy(root)
    tree = ET.ElementTree(root)
    ET.indent(tree, space="  ")
    return ET.tostring(root, encoding="unicode")


def normalise_pdf(data):
    """The bytes of the PDF as ISO-8859-1 text (one character per byte, nothing lost),
    without the carriage returns that a Git checkout may add to a file it takes for
    text. The deterministic PDF is not compressed, so the diff is readable."""
    return data.replace(b"\r", b"").decode("latin-1")


def normalise_text(data):
    text = data.decode("utf-8", errors="replace") if isinstance(data, bytes) else data
    return text.replace("\r\n", "\n").replace("\r", "\n")


# ---------------------------------------------------------------------------
# Running one test
# ---------------------------------------------------------------------------

def expected_files(puml, extension, count):
    """hello.puml -> hello.svg ; for several images hello-1.svg, hello-2.svg..."""
    base = puml.with_suffix("")
    if count == 1:
        return [Path(str(base) + extension)]
    return [Path("%s-%d%s" % (base, i + 1, extension)) for i in range(count)]


def produced_files(outdir, puml, extension):
    """The files written by the executable: name.svg, then name_001.svg, ..."""
    stem = puml.stem
    first = outdir / (stem + extension)
    others = sorted(p for p in outdir.glob(stem + "_*" + extension))
    return ([first] if first.exists() else []) + others


def unified_diff(expected, actual, name):
    return "".join(difflib.unified_diff(
        expected.splitlines(keepends=True), actual.splitlines(keepends=True),
        fromfile="expected/" + name, tofile="actual/" + name))


def keep_files(outdir, relative):
    """Copies what the executable wrote to <out>/output/<folder of the test>/.

    The executable writes in a private folder per test, so that two tests of the
    same folder (foo.puml and foo_bar.puml) cannot be mixed up; the files are
    copied from there once the run is over.
    """
    target = OUT_DIR / "output" / Path(relative).parent
    for produced in outdir.iterdir():
        if produced.is_file():
            target.mkdir(parents=True, exist_ok=True)
            name = produced.name[len(COPY_PREFIX):] if produced.name.startswith(COPY_PREFIX) else produced.name
            shutil.copyfile(produced, target / name)


def run_png(puml, exe, timeout, workdir, relative):
    """Runs `plantuml.exe -tpng`: there is no reference file, the only question is
    whether it crashes, hangs, or writes something that is not a PNG file."""
    outdir = Path(tempfile.mkdtemp(prefix="vega-c-", dir=workdir))
    try:
        command = [str(exe), "-tpng", "-o", str(outdir), str(puml)]
        try:
            proc = subprocess.run(command, stdin=subprocess.DEVNULL, stdout=subprocess.PIPE,
                                  stderr=subprocess.PIPE, timeout=timeout, cwd=str(puml.parent))
        except subprocess.TimeoutExpired:
            return {"status": PNG_TIMEOUT, "message": "no answer after %ss" % timeout}
        stderr = proc.stderr.decode("utf-8", errors="replace").strip()
        if proc.returncode < 0 or proc.returncode > 255:
            return {"status": PNG_CRASH, "message": "exit code %d %s" % (proc.returncode, stderr[:300])}
        produced = produced_files(outdir, puml, ".png")
        if not produced:
            return {"status": PNG_NO_OUTPUT, "message": "exit code %d %s" % (proc.returncode, stderr[:300])}
        for png in produced:
            if not png.read_bytes().startswith(PNG_SIGNATURE):
                return {"status": PNG_INVALID, "message": "%s is not a PNG file" % png.name}
        return {"status": PNG_OK, "message": ""}
    finally:
        keep_files(outdir, relative)
        shutil.rmtree(outdir, ignore_errors=True)


def run_one(puml, exe, timeout, workdir, with_png):
    result = run_checks(puml, exe, timeout, workdir)
    if with_png:
        result["png"] = run_png(puml, exe, timeout, workdir, result["file"])
    return result


def run_checks(puml, exe, timeout, workdir):
    relative = puml.relative_to(VEGA_DIR).as_posix()
    result = {"file": relative, "status": SKIPPED, "message": "", "duration_ms": 0}
    header = read_header(puml)
    result["allow_failure"] = header.get("allow-failure") == "true"

    formats = output_formats(header)
    supported = [f for f in formats if f in SUPPORTED_FORMATS]
    if not supported:
        result["message"] = "output format not available in the C build: " + ", ".join(formats)
        return result
    for key in ERROR_EXPECTATION_KEYS:
        if key in header:
            result["message"] = "test expects an error (%s): not checked" % key
            return result
    if "decimal" in header:
        result["message"] = "decimal option: not supported by this script"
        return result

    source = write_source_copy(puml)
    try:
        return check_formats(puml, source, supported, exe, timeout, workdir, relative, result)
    finally:
        source.unlink()


def check_formats(puml, source, supported, exe, timeout, workdir, relative, result):
    start = time.time()
    problems = []
    diffs = []
    checked = 0
    for fmt in supported:
        flags, extension = SUPPORTED_FORMATS[fmt]
        outdir = Path(tempfile.mkdtemp(prefix="vega-c-", dir=workdir))
        try:
            command = [str(exe)] + flags + ["-o", str(outdir), str(source)]
            try:
                proc = subprocess.run(command, stdin=subprocess.DEVNULL, stdout=subprocess.PIPE,
                                      stderr=subprocess.PIPE, timeout=timeout, cwd=str(puml.parent))
            except subprocess.TimeoutExpired:
                result["status"] = TIMEOUT
                result["message"] = "[%s] no answer after %ss" % (fmt, timeout)
                result["duration_ms"] = int((time.time() - start) * 1000)
                return result

            produced = produced_files(outdir, source, extension)
            stderr = proc.stderr.decode("utf-8", errors="replace").strip()
            if proc.returncode < 0 or proc.returncode > 255:
                # a crash: a signal, or an NTSTATUS code such as 0xC0000005 on Windows
                result["status"] = ERROR
                result["message"] = "[%s] crashed, exit code %d %s" % (fmt, proc.returncode, stderr[:300])
                result["duration_ms"] = int((time.time() - start) * 1000)
                return result
            if not produced:
                problems.append("[%s] no output file written (exit code %d) %s" % (fmt, proc.returncode, stderr[:300]))
                continue

            references = expected_files(puml, extension, len(produced))
            for actual_file, reference in zip(produced, references):
                if not reference.exists():
                    problems.append("[%s] no reference file %s" % (fmt, reference.name))
                    continue
                actual_bytes = actual_file.read_bytes()
                expected_bytes = reference.read_bytes()
                try:
                    if fmt == "svg":
                        actual = normalise_svg(actual_bytes)
                        expected = normalise_svg(expected_bytes)
                    elif fmt == "pdf":
                        actual = normalise_pdf(actual_bytes)
                        expected = normalise_pdf(expected_bytes)
                    else:
                        actual = normalise_text(actual_bytes)
                        expected = normalise_text(expected_bytes)
                except ET.ParseError as e:
                    problems.append("[%s] XML not well-formed in %s: %s" % (fmt, actual_file.name, e))
                    continue
                checked += 1
                if actual != expected:
                    problems.append("[%s] output differs from %s" % (fmt, reference.name))
                    diffs.append(unified_diff(expected, actual, reference.name))
        finally:
            keep_files(outdir, relative)
            shutil.rmtree(outdir, ignore_errors=True)

    result["duration_ms"] = int((time.time() - start) * 1000)
    if problems:
        result["status"] = FAIL
        result["message"] = "; ".join(problems)
        result["diff"] = "\n".join(diffs)
    elif checked:
        result["status"] = PASS
    else:
        result["message"] = "nothing compared"
    return result


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

def find_exe(requested):
    if requested:
        exe = Path(requested)
        return exe if exe.is_file() else None
    candidates = [REPO_ROOT / "plantuml.exe", REPO_ROOT / "plantuml"]
    candidates += sorted((REPO_ROOT / "build" / "generated" / "teavm" / "c" / "bin").rglob("plantuml.exe"))
    for candidate in candidates:
        if candidate.is_file():
            return candidate
    return None


def main(argv=None):
    global VEGA_DIR, OUT_DIR
    parser = argparse.ArgumentParser(description="Run plantuml.exe (C build) on the Vega tests.")
    parser.add_argument("filters", nargs="*",
                        help="only the tests whose path (relative to the vega folder) contains one of these strings")
    parser.add_argument("--exe", help="path of plantuml.exe (default: the repository root, as compilec.bat puts it)")
    parser.add_argument("--vega-dir", default=str(VEGA_DIR), help="folder of the tests (default: %(default)s)")
    parser.add_argument("-j", "--jobs", type=int, default=os.cpu_count() or 1, help="parallel runs (default: %(default)s)")
    parser.add_argument("--timeout", type=float, default=60, help="seconds per test and per format (default: %(default)s)")
    parser.add_argument("--diff", action="store_true", help="write a .diff file for each failed test")
    parser.add_argument("--out", default=str(OUT_DIR), help="folder of the reports (default: %(default)s)")
    parser.add_argument("--no-png", action="store_true", help="do not run the PNG generation")
    parser.add_argument("--strict", action="store_true", help="exit code 1 when a test fails (default: always 0)")
    parser.add_argument("-v", "--verbose", action="store_true", help="print every test, not only the failures")
    args = parser.parse_args(argv)

    VEGA_DIR = Path(args.vega_dir).resolve()
    OUT_DIR = Path(args.out).resolve()

    exe = find_exe(args.exe)
    if exe is None:
        print("plantuml.exe not found: run compilec.bat first, or pass --exe <path>", file=sys.stderr)
        return 2
    exe = exe.resolve()

    files = sorted(VEGA_DIR.rglob("*.puml"))
    if args.filters:
        files = [f for f in files if any(s in f.relative_to(VEGA_DIR).as_posix() for s in args.filters)]
    if not files:
        print("No .puml file found in %s" % VEGA_DIR, file=sys.stderr)
        return 2

    if OUT_DIR.exists():
        shutil.rmtree(OUT_DIR, ignore_errors=True)
    OUT_DIR.mkdir(parents=True)
    workdir = Path(tempfile.mkdtemp(prefix="vega-c-work-"))

    print("Executable: %s" % exe)
    print("Tests:      %d (in %s), %d parallel" % (len(files), VEGA_DIR, args.jobs))

    results = []
    started = time.time()
    try:
        with concurrent.futures.ThreadPoolExecutor(max_workers=max(1, args.jobs)) as pool:
            futures = {pool.submit(run_one, f, exe, args.timeout, workdir, not args.no_png): f for f in files}
            for done, future in enumerate(concurrent.futures.as_completed(futures), 1):
                try:
                    result = future.result()
                except Exception as e:  # a bug of this script must not stop the whole run
                    puml = futures[future]
                    result = {"file": puml.relative_to(VEGA_DIR).as_posix(), "status": ERROR,
                              "message": "script error: %r" % e, "duration_ms": 0}
                results.append(result)
                if args.verbose or result["status"] in (ERROR, TIMEOUT):
                    print("%-8s %s  %s" % (result["status"].upper(), result["file"], result["message"][:160]))
                if done % 100 == 0:
                    print("... %d / %d" % (done, len(files)))
    finally:
        shutil.rmtree(workdir, ignore_errors=True)

    results.sort(key=lambda r: r["file"])
    counts = {s: sum(1 for r in results if r["status"] == s) for s in (PASS, FAIL, ERROR, TIMEOUT, SKIPPED)}

    png_labels = (PNG_OK, PNG_CRASH, PNG_TIMEOUT, PNG_NO_OUTPUT, PNG_INVALID)
    png_counts = {s: sum(1 for r in results if r.get("png", {}).get("status") == s) for s in png_labels}
    png_bad = [r for r in results if r.get("png", {}).get("status", PNG_OK) != PNG_OK]

    if args.diff:
        for r in results:
            if r.get("diff"):
                target = OUT_DIR / "diffs" / (r["file"] + ".diff")
                target.parent.mkdir(parents=True, exist_ok=True)
                target.write_text(r["diff"], encoding="utf-8")
    for r in results:
        r.pop("diff", None)

    report = {"executable": str(exe), "summary": dict(counts, total=len(results)),
              "png": png_counts, "tests": results}
    (OUT_DIR / "report.json").write_text(json.dumps(report, indent=2, ensure_ascii=False), encoding="utf-8")

    lines = [
        "VEGA (C build) SUMMARY",
        "  Total:    %d" % len(results),
        "  Passed:   %d" % counts[PASS],
        "  Failed:   %d" % counts[FAIL],
        "  Crashed:  %d" % counts[ERROR],
        "  Timeout:  %d" % counts[TIMEOUT],
        "  Skipped:  %d" % counts[SKIPPED],
    ]
    if not args.no_png:
        lines += [
            "PNG (only checks that the executable does not crash):",
            "  Written:   %d" % png_counts[PNG_OK],
            "  Crashed:   %d" % png_counts[PNG_CRASH],
            "  Timeout:   %d" % png_counts[PNG_TIMEOUT],
            "  No output: %d" % png_counts[PNG_NO_OUTPUT],
            "  Not a PNG: %d" % png_counts[PNG_INVALID],
        ]
    lines += ["", "Time: %.1fs" % (time.time() - started)]
    head = len(lines)
    lines.append("")
    for label, status in (("Passed", PASS), ("Failed", FAIL), ("Crashed", ERROR), ("Timeout", TIMEOUT)):
        selected = [r for r in results if r["status"] == status]
        if selected:
            lines.append("%s:" % label)
            lines += ["  - %s%s" % (r["file"], "" if status == PASS else "  " + r["message"][:120]) for r in selected]
            lines.append("")
    if png_bad:
        lines.append("PNG problems:")
        lines += ["  - %s  [%s] %s" % (r["file"], r["png"]["status"], r["png"]["message"][:120]) for r in png_bad]
        lines.append("")
    (OUT_DIR / "summary.txt").write_text("\n".join(lines), encoding="utf-8")

    print()
    print("\n".join(lines[:head]))
    print("Reports: %s" % OUT_DIR)
    return 1 if args.strict and (counts[FAIL] or counts[ERROR] or counts[TIMEOUT] or png_bad) else 0


if __name__ == "__main__":
    sys.exit(main())
