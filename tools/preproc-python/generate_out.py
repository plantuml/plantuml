#!/usr/bin/env python3
"""Generate the reference .out files of the Python test scripts with CPython 3.

Each script of src/test/resources/python (except the "deviations" directory,
which documents differences from CPython) is run by the running interpreter in
isolated mode, from its own directory, with PYTHONHASHSEED=0. Its standard output
is written next to it as <name>.out. A script that prints nothing gets no .out
file, and a stale .out is removed. A script that does not exit with status 0 is
reported and makes the program fail: such a script cannot be a CPython reference.

    python3 tools/preproc-python/generate_out.py [--check] [path ...]

--check compares instead of writing: it reports the scripts whose .out is
missing, stale or wrong, and writes nothing.
"""
import os
import subprocess
import sys

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.normpath(os.path.join(HERE, "..", "..", "src", "test", "resources", "python"))
SKIP_DIRS = {"deviations"}
TIMEOUT_SECONDS = 120


def scripts(paths):
    for base in paths:
        if os.path.isfile(base):
            if base.endswith(".py"):
                yield os.path.abspath(base)
            continue
        for directory, dirs, files in os.walk(base):
            dirs[:] = sorted(d for d in dirs if d not in SKIP_DIRS)
            for name in sorted(files):
                if name.endswith(".py"):
                    yield os.path.join(directory, name)


def run_script(path):
    env = dict(os.environ, PYTHONHASHSEED="0")
    return subprocess.run([sys.executable, "-I", os.path.basename(path)],
                          cwd=os.path.dirname(path), env=env,
                          capture_output=True, timeout=TIMEOUT_SECONDS)


def main(argv):
    check = "--check" in argv
    paths = [a for a in argv if not a.startswith("--")] or [ROOT]
    problems = 0
    count = 0
    for path in scripts(paths):
        count += 1
        name = os.path.relpath(path, ROOT)
        out_path = path[:-3] + ".out"
        result = run_script(path)
        if result.returncode != 0:
            problems += 1
            print("ERROR (status %d) %s\n%s" % (
                result.returncode, name, result.stderr.decode("utf-8", "replace")[-600:]))
            continue
        expected = result.stdout
        if check:
            if expected:
                if not os.path.exists(out_path):
                    problems += 1
                    print("MISSING .out %s" % name)
                elif open(out_path, "rb").read() != expected:
                    problems += 1
                    print("DIFFERS %s" % name)
            elif os.path.exists(out_path):
                problems += 1
                print("STALE .out %s" % name)
        elif expected:
            with open(out_path, "wb") as f:
                f.write(expected)
        elif os.path.exists(out_path):
            os.remove(out_path)
    action = "checked" if check else "generated"
    print("%s: %d script(s) %s, %d problem(s)" % (ROOT, count, action, problems))
    return 1 if problems else 0


if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
