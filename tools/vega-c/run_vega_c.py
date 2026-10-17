#!/usr/bin/env python3
r"""
Runs the native executable built by compilec.bat (plantuml.exe) on the Vega tests.

Vega is the regression harness of PlantUML (src/test/java/test/vega/VegaTest.java):
every .puml file under src/test/resources/vega/ is rendered, and the output is
compared with the reference file stored next to it (hello.puml -> hello.svg).
This script does the same with the C executable instead of the JVM.

The C build is still partial (mindmap only, SVG / PNG / --preproc), so many tests
are expected to fail. The script only reports; it does not decide what is a
regression.

What is checked, per test file:
  * output: svg      -> `plantuml.exe -tsvg`, compared (XML-normalised, as SvgCleaner
                        does in the Java tests) with <name>.svg / <name>-N.svg
  * output: preproc  -> `plantuml.exe --preproc`, compared with <name>.preproc
  * any other output format, or a test that expects an error (expected-exception,
    expected-error-*, expected-status): reported as "skipped".
The other expectations of the YAML header (expected-description, expected-stderr,
expected-image-count...) are not checked.

Usage (from anywhere):
    python tools/vega-c/run_vega_c.py                       # all the tests
    python tools/vega-c/run_vega_c.py mindmap               # paths containing "mindmap"
    python tools/vega-c/run_vega_c.py --exe path\to\plantuml.exe -j 8 --diff

Results go to <repo>/build/vega-c/ (git-ignored): report.json, summary.txt,
and with --diff one .diff file per failed test.
"""

import argparse
import concurrent.futures
import difflib
import json
import os
import re
import shutil
import subprocess
import sys
import tempfile
import time
import xml.etree.ElementTree as ET
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parents[2]
VEGA_DIR = REPO_ROOT / "src" / "test" / "resources" / "vega"
OUT_DIR = REPO_ROOT / "build" / "vega-c"

YAML_DELIMITER = "---"

# output format of the YAML header -> (command line flags, extension of the produced
# and of the reference file)
SUPPORTED_FORMATS = {
    "svg": (["-tsvg"], ".svg"),
    "preproc": (["--preproc"], ".preproc"),
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


def normalise_svg(data):
    """Parses and pretty-prints, so that only a real change makes a difference.

    Processing instructions are dropped (the parser ignores them), comments are
    kept, as the Java normaliser does.
    """
    text = data.decode("utf-8", errors="replace") if isinstance(data, bytes) else data
    text = text.replace("\r\n", "\n").replace("\r", "\n")
    parser = ET.XMLParser(target=ET.TreeBuilder(insert_comments=True))
    root = ET.fromstring(text.encode("utf-8"), parser=parser)
    _tidy(root)
    tree = ET.ElementTree(root)
    ET.indent(tree, space="  ")
    return ET.tostring(root, encoding="unicode")


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


def run_one(puml, exe, timeout, workdir, keep_outputs):
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

    start = time.time()
    problems = []
    diffs = []
    checked = 0
    for fmt in supported:
        flags, extension = SUPPORTED_FORMATS[fmt]
        outdir = Path(tempfile.mkdtemp(prefix="vega-c-", dir=workdir))
        try:
            command = [str(exe)] + flags + ["-o", str(outdir), str(puml)]
            try:
                proc = subprocess.run(command, stdin=subprocess.DEVNULL, stdout=subprocess.PIPE,
                                      stderr=subprocess.PIPE, timeout=timeout, cwd=str(puml.parent))
            except subprocess.TimeoutExpired:
                result["status"] = TIMEOUT
                result["message"] = "[%s] no answer after %ss" % (fmt, timeout)
                result["duration_ms"] = int((time.time() - start) * 1000)
                return result

            produced = produced_files(outdir, puml, extension)
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
                    if keep_outputs:
                        target = OUT_DIR / "actual" / Path(relative).parent
                        target.mkdir(parents=True, exist_ok=True)
                        shutil.copyfile(actual_file, target / actual_file.name)
        finally:
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
    parser.add_argument("--diff", action="store_true", help="write a .diff file for each failed test, and keep the actual outputs")
    parser.add_argument("--out", default=str(OUT_DIR), help="folder of the reports (default: %(default)s)")
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
            futures = {pool.submit(run_one, f, exe, args.timeout, workdir, args.diff): f for f in files}
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

    if args.diff:
        for r in results:
            if r.get("diff"):
                target = OUT_DIR / "diffs" / (r["file"] + ".diff")
                target.parent.mkdir(parents=True, exist_ok=True)
                target.write_text(r["diff"], encoding="utf-8")
    for r in results:
        r.pop("diff", None)

    report = {"executable": str(exe), "summary": dict(counts, total=len(results)), "tests": results}
    (OUT_DIR / "report.json").write_text(json.dumps(report, indent=2, ensure_ascii=False), encoding="utf-8")

    lines = [
        "VEGA (C build) SUMMARY",
        "  Total:    %d" % len(results),
        "  Passed:   %d" % counts[PASS],
        "  Failed:   %d" % counts[FAIL],
        "  Crashed:  %d" % counts[ERROR],
        "  Timeout:  %d" % counts[TIMEOUT],
        "  Skipped:  %d" % counts[SKIPPED],
        "  Time:     %.1fs" % (time.time() - started),
        "",
    ]
    for label, status in (("Passed", PASS), ("Failed", FAIL), ("Crashed", ERROR), ("Timeout", TIMEOUT)):
        selected = [r for r in results if r["status"] == status]
        if selected:
            lines.append("%s:" % label)
            lines += ["  - %s%s" % (r["file"], "" if status == PASS else "  " + r["message"][:120]) for r in selected]
            lines.append("")
    (OUT_DIR / "summary.txt").write_text("\n".join(lines), encoding="utf-8")

    print()
    print("\n".join(lines[:8]))
    print("Reports: %s" % OUT_DIR)
    return 1 if args.strict and (counts[FAIL] or counts[ERROR] or counts[TIMEOUT]) else 0


if __name__ == "__main__":
    sys.exit(main())
