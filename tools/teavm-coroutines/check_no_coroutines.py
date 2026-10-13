#!/usr/bin/env python3
"""Fails when TeaVM generated code still contains coroutines.

PlantUML must not rely on TeaVM coroutines (threads, Object.wait(), Thread.sleep(),
@Async methods, java.net.URL in JavaScript...): a single suspending method turns
every method that can reach it into a state machine ("async" code), in the
JavaScript and in the C output alike. See README.md next to this script.

usage:
  check_no_coroutines.py <plantuml.js | C output directory> [...]

Works on the obfuscated and on the readable JavaScript output, and on the
directory TeaVM writes the C sources to (build/generated/teavm/c).
Exit status: 0 when every target is clean, 1 otherwise, 2 on bad usage.
"""

import os
import re
import sys

# TeaVM always emits its own java.lang.Object.monitorEnter() as a coroutine,
# together with the monitorEnterWait() it suspends in, even when nothing calls
# it (without Thread.start(), synchronized blocks use monitorEnterSync()).
# Anything beyond that one state machine and that one suspension is PlantUML
# code turned into a coroutine.
#
# That runtime coroutine is also the proof that the patterns below still match
# what TeaVM writes: it must be found, exactly once. If a TeaVM upgrade renamed
# what they look for, they would find nothing and a check on "at most" would
# pass silently; this one fails and says so.
EXPECTED_JS_STATE_MACHINES = 1
EXPECTED_JS_SUSPENSIONS = 1
EXPECTED_C_FUNCTION = "meth_jl_Object_monitorEnter"

# Readable output: "switch ($ptr)"; obfuscated output: "switch($p)".
JS_STATE_MACHINE = re.compile(r"switch\s*\(\s*\$p(?:tr)?\s*\)")
JS_SUSPENSION = re.compile(r"\bthread\.suspend\(")
# Readable output only: the name of the function holding a state machine.
JS_FUNCTION = re.compile(r"^(?:let |const |function )?\s*([A-Za-z0-9_$]+)\s*=\s*(?:\(|[A-Za-z0-9_$]+\s*=>)|^function ([A-Za-z0-9_$]+)\(")

C_FUNCTION = re.compile(r"^[A-Za-z_][A-Za-z0-9_\s\*]*?\b(meth_[A-Za-z0-9_]+)\([^;]*\)\s*\{\s*$")
C_COROUTINE = re.compile(r"\bmeth_otr_Fiber_isResuming\(")


def check_js(path):
    with open(path, encoding="utf-8", errors="replace") as f:
        text = f.read()
    machines = len(JS_STATE_MACHINE.findall(text))
    suspensions = len(JS_SUSPENSION.findall(text))
    names = []
    current = None
    for line in text.splitlines():
        m = JS_FUNCTION.match(line)
        if m:
            current = m.group(1) or m.group(2)
        if JS_STATE_MACHINE.search(line) and current and current not in names and len(names) < 40:
            names.append(current)
    ok = machines == EXPECTED_JS_STATE_MACHINES and suspensions == EXPECTED_JS_SUSPENSIONS
    print("%s %s: %d coroutine(s), %d suspension point(s) (expected %d and %d: TeaVM's Object.monitorEnter)"
          % ("OK  " if ok else "FAIL", path, machines, suspensions,
             EXPECTED_JS_STATE_MACHINES, EXPECTED_JS_SUSPENSIONS))
    if machines < EXPECTED_JS_STATE_MACHINES or suspensions < EXPECTED_JS_SUSPENSIONS:
        print("     TeaVM's own coroutine was not found: the patterns of this script no longer match"
              " the output (new TeaVM version?), so it cannot tell whether coroutines are back."
              " Update them (see README.md).")
    elif not ok and names:
        print("     coroutines (first ones, readable output only): " + ", ".join(names))
    return ok


def check_c(directory):
    classes = os.path.join(directory, "classes")
    if not os.path.isdir(classes):
        print("FAIL %s: no classes/ directory (not a TeaVM C output?)" % directory)
        return False
    found = []
    expected_seen = False
    for root, _, files in os.walk(classes):
        for name in files:
            if not name.endswith(".c"):
                continue
            path = os.path.join(root, name)
            current = None
            with open(path, encoding="utf-8", errors="replace") as f:
                for line in f:
                    m = C_FUNCTION.match(line)
                    if m:
                        current = m.group(1)
                    elif C_COROUTINE.search(line) and current is not None:
                        rel = os.path.relpath(path, classes)
                        if current == EXPECTED_C_FUNCTION:
                            expected_seen = True
                        elif not rel.startswith("org" + os.sep + "teavm" + os.sep + "runtime"):
                            found.append("%s (%s)" % (current, rel))
                        current = None
    ok = expected_seen and not found
    print("%s %s: %d coroutine(s) outside the TeaVM runtime" % ("OK  " if ok else "FAIL", directory, len(found)))
    if not expected_seen:
        print("     TeaVM's own coroutine (%s) was not found: the patterns of this script no longer"
              " match the output (new TeaVM version?), so it cannot tell whether coroutines are back."
              " Update them (see README.md)." % EXPECTED_C_FUNCTION)
    for entry in sorted(found)[:40]:
        print("     " + entry)
    return ok


def main(args):
    if not args:
        print(__doc__.strip())
        return 2
    ok = True
    for target in args:
        if os.path.isdir(target):
            ok = check_c(target) and ok
        elif os.path.isfile(target):
            ok = check_js(target) and ok
        else:
            print("FAIL %s: no such file or directory" % target)
            ok = False
    return 0 if ok else 1


if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
