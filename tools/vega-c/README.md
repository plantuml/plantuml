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

The C build is partial (mindmap only for now), so many tests fail: the script
reports, it does not judge. The exit code is 0 unless `--strict` is given.

| Test | Result |
|---|---|
| `output: svg` | `plantuml.exe -tsvg`, compared with `<name>.svg` (or `<name>-N.svg`) once both are parsed and pretty-printed, as `SvgCleaner` does for the JVM |
| `output: preproc` | `plantuml.exe --preproc`, compared with `<name>.preproc` |
| other formats, tests that expect an error | `skipped` |

The other expectations of the YAML header (`expected-description`,
`expected-stderr`...) are not checked. A test that crashes the executable is
`error`, one that does not answer in `--timeout` seconds is `timeout`.

The reports are written to `build/vega-c/` (`summary.txt`, `report.json`,
`diffs/` and `actual/` with `--diff`), never to `src/test/resources`, so that
they cannot be mixed up with the `vega.json` of the JVM tests.
