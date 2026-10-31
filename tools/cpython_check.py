#!/usr/bin/env python3
"""Run the test scripts of src/test/resources/python with CPython.

A script must exit with status 0. If it prints something, the output must
match the <name>.out file next to it; --update (re)writes those files.
Scripts under a "deviations" directory are not run (they rely on documented
differences from CPython).

    python3 tools/cpython_check.py [--update] [path ...]
"""
import os
import subprocess
import sys

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..",
                    "src", "test", "resources", "python")


def scripts(paths):
    for base in paths:
        if os.path.isfile(base):
            yield base
            continue
        for d, dirs, files in os.walk(base):
            dirs[:] = sorted(x for x in dirs if x != "deviations")
            for f in sorted(files):
                if f.endswith(".py"):
                    yield os.path.join(d, f)


def main(argv):
    update = "--update" in argv
    paths = [a for a in argv if not a.startswith("--")] or [ROOT]
    failed = 0
    count = 0
    for path in scripts(paths):
        count += 1
        out = path[:-3] + ".out"
        r = subprocess.run([sys.executable, "-I", os.path.basename(path)],
                           cwd=os.path.dirname(path), capture_output=True,
                           timeout=120)
        name = os.path.relpath(path, ROOT)
        if r.returncode != 0:
            failed += 1
            print("FAIL (status %d) %s\n%s" % (r.returncode, name,
                                                r.stderr.decode(errors="replace")[-600:]))
            continue
        if update:
            if r.stdout:
                with open(out, "wb") as f:
                    f.write(r.stdout)
            elif os.path.exists(out):
                os.remove(out)
        elif r.stdout:
            if not os.path.exists(out):
                failed += 1
                print("FAIL (no .out file) %s" % name)
            elif open(out, "rb").read() != r.stdout:
                failed += 1
                print("FAIL (output differs) %s" % name)
        elif os.path.exists(out):
            failed += 1
            print("FAIL (.out exists but nothing printed) %s" % name)
    print("%d script(s), %d failure(s)" % (count, failed))
    return 1 if failed else 0


if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
