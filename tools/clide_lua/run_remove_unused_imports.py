#!/usr/bin/env python3
"""Starts the clide daemon on PlantUML, runs remove_unused_imports.lua, then
terminates the daemon.

Usage:
    python tools/clide_lua/run_remove_unused_imports.py [--clide DIR] [--project DIR]

--clide    directory of the clide checkout, built with `ant` (clide.jar next to
           start_clide.py and clide.py). Default: $CLIDE_HOME, else a `clide`
           directory next to the project directory.
--project  the PlantUML directory. Default: the root of this repository.

The Lua script commits its transaction, so the changes are on disk when this
script ends: review them with `git diff` (`git checkout .` undoes them).
If the Lua script fails, the daemon is left running (its transaction is still
open and must be rolled back from a clide client; `terminate` refuses to run
while a transaction is open).
"""
import argparse
import os
import subprocess
import sys
from pathlib import Path

HERE = Path(__file__).resolve().parent
DEFAULT_PROJECT = HERE.parent.parent


def run(cmd, cwd, stdin=None):
    print("> " + " ".join(str(c) for c in cmd), flush=True)
    return subprocess.run([str(c) for c in cmd], cwd=str(cwd), input=stdin, text=True).returncode


def main():
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("--clide")
    ap.add_argument("--project", default=str(DEFAULT_PROJECT))
    args = ap.parse_args()

    project = Path(args.project).resolve()
    clide = Path(args.clide or os.environ.get("CLIDE_HOME") or project.parent / "clide")
    for name in ("start_clide.py", "clide.py", "clide.jar"):
        if not (clide / name).is_file():
            sys.exit("%s not found in %s (build clide with `ant`, then use --clide or CLIDE_HOME)" % (name, clide))

    script = HERE / "remove_unused_imports.lua"

    if run([sys.executable, "start_clide.py", project], clide) != 0:
        sys.exit("Could not start the clide daemon")

    if run([sys.executable, "clide.py", "--lua", script, project], clide) != 0:
        sys.exit("The Lua script failed: the daemon is still running and a transaction may be open")

    # `terminate` is not available from Lua: send it through the client.
    if run([sys.executable, "clide.py", project], clide, stdin="terminate\n") != 0:
        sys.exit("Could not terminate the clide daemon")
    print("Daemon terminated.")


if __name__ == "__main__":
    main()
