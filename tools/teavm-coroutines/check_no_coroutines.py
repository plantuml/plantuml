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
ALLOWED_JS_STATE_MACHINES = 1
ALLOWED_JS_SUSPENSIONS = 1
ALLOWED_C_FUNCTIONS = {"meth_jl_Object_monitorEnter"}

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
    ok = machines <= ALLOWED_JS_STATE_MACHINES and suspensions <= ALLOWED_JS_SUSPENSIONS
    print("%s %s: %d coroutine(s) (allowed %d), %d suspension point(s) (allowed %d)"
          % ("OK  " if ok else "FAIL", path, machines, ALLOWED_JS_STATE_MACHINES,
             suspensions, ALLOWED_JS_SUSPENSIONS))
    if not ok and names:
        print("     coroutines (first ones, readable output only): " + ", ".join(names))
    return ok


def check_c(directory):
    classes = os.path.join(directory, "classes")
    if not os.path.isdir(classes):
        print("FAIL %s: no classes/ directory (not a TeaVM C output?)" % directory)
        return False
    found = []
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
                        if current not in ALLOWED_C_FUNCTIONS and not rel.startswith("org" + os.sep + "teavm" + os.sep + "runtime"):
                            found.append("%s (%s)" % (current, rel))
                        current = None
    ok = not found
    print("%s %s: %d coroutine(s) outside the TeaVM runtime" % ("OK  " if ok else "FAIL", directory, len(found)))
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
