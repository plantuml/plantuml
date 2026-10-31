# Vega tests on the C build

`run_vega_c.py` runs `plantuml.exe` (built by `compilec.bat` at the root of the
repository) on the Vega tests of `src/test/resources/vega`, and compares what it
writes with the reference files stored next to the `.puml` files. It needs
Python 3.9+ and nothing else.

```
python tools/vega-c/run_vega_c.py                    # every test
python tools/vega-c/run_vega_c.py mindmap            # the paths containing "mindmap"
python tools/vega-c/run_vega_c.py --diff -j 8        # one .diff per failed test
python tools/vega-c/run_vega_c.py --exe path\to\plantuml.exe
```

The C build is partial (mindmap only for now, plus the PDF tests of `vega/pdf`), so many tests fail: the script
reports, it does not judge. The exit code is 0 unless `--strict` is given.

| Test | Result |
|---|---|
| `output: svg` | `plantuml.exe --svg-vega` (the deterministic SVG of the JVM Vega tests), compared with `<name>.svg` (or `<name>-N.svg`) once both are parsed and pretty-printed, as `SvgCleaner` does for the JVM |
| `output: pdf` | `plantuml.exe --pdf-vega` (the deterministic PDF of the JVM Vega tests: fixed dates and file identifier, no compression), compared byte for byte with `<name>.pdf` (or `<name>-N.pdf`), ignoring carriage returns |
| `output: preproc` | `plantuml.exe --preproc`, compared with `<name>.preproc` |
| other formats, tests that expect an error | `skipped` |

Every test is also run with `plantuml.exe -tpng` (`--no-png` to skip it). There is
no reference file: it only checks that the executable does not crash, does not
hang, and writes a file that starts like a PNG. This is reported apart from
the results above (`png` in `report.json`, "PNG problems" in `summary.txt`), and
it also covers the tests that are `skipped` above.

The other expectations of the YAML header (`expected-description`,
`expected-stderr`...) are not checked. A test that crashes the executable is
`error`, one that does not answer in `--timeout` seconds is `timeout`.

Everything goes to `build/vega-c/` (emptied at each run, git-ignored), never to
`src/test/resources`, so that it cannot be mixed up with the `vega.json` of the
JVM tests:

- `output/`: the files written by `plantuml.exe` (svg, png...), in the same folders as the tests
- `summary.txt` and `report.json`
- `diffs/`: with `--diff`, one `.diff` per failed test
