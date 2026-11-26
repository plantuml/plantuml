# viz-nowasm

Builds `src/main/teavm/nowasm/viz-global-nowasm.js`: the same Viz.js as
`src/main/teavm/vendor/viz-global.js`, with its embedded WebAssembly module
translated to plain JavaScript by
[Binaryen](https://github.com/WebAssembly/binaryen)'s `wasm2js`.

It is shipped only in the npm package `@plantuml/core-nowasm`
(`gradlew :plantuml-mit:npmPackageNowasm -Pci`), which is `@plantuml/core`
without any WebAssembly, for hosts where WebAssembly cannot run: a Content
Security Policy without `'wasm-unsafe-eval'` (or `'unsafe-eval'`), WebAssembly
disabled by policy, or a JavaScript engine in jitless mode. It defines the same
global `Viz` with the same API, and `globalThis.PLANTUML_VIZ` works the same
way. Nothing else copies `src/main/teavm/nowasm/`: `@plantuml/core` and the
site build are unchanged.

The engine itself (`plantuml.js`) uses neither WebAssembly, nor `eval`, nor
`new Function`: with this file, the page works under `script-src 'self'`.
`tools/browser-test/check-viz-nowasm.js` checks that, with a real CSP header.

## Regenerating

Run it again whenever `src/main/teavm/vendor/viz-global.js` is replaced (the
browser check fails until then):

```bash
cd tools/viz-nowasm
npm ci
node build.js
```

It takes about 20 seconds. The tools are pinned in `package.json` and
`package-lock.json` (`binaryen` from npm, which ships a `wasm2js` compiled to
JavaScript: no native binary to install; `terser` for mangling), so the same
input gives the same output, byte for byte. The header of the generated file
records the tool versions and the sha256 of the `viz-global.js` it was built
from: a mismatch with the current `viz-global.js` means it is stale.

`node build.js in=<viz-global.js> out=<file> minify=false` builds from another
file, elsewhere, or without mangling (5.6 MB instead of 2.0 MB; gzip 0.83 MB
instead of 0.60 MB).

## How it works

`viz-global.js` is an Emscripten build: the Graphviz module is a base64 `data:`
URL, decoded and passed to `WebAssembly.instantiate(bytes, imports)`, and only
`instance.exports` is used afterwards. `build.js`:

1. decodes that module;
2. runs `wasm2js -O` on it, which gives a function of the imports returning the
   same exports, linear memory included (an `ArrayBuffer` that grows by copy);
3. puts that function inside the UMD factory of `viz-global.js`, replaces the
   `WebAssembly.instantiate` call with it, drops the base64 payload, and makes
   the abort path work where `WebAssembly` is not defined at all.

Each rewrite must match exactly once, or the script stops with an error: a new
Viz.js release whose glue changed shape needs this script to be looked at,
instead of producing a broken file silently. The script also refuses an output
that still compiles WebAssembly, or that uses `eval` or the `Function`
constructor.

## Cost, measured

Viz.js 3.24.0 (Graphviz 14.1.1), headless Chromium, November 2026:

- size: 2.02 MB (gzip 0.60 MB), against 1.38 MB (gzip 0.57 MB);
- output: the SVG is identical for all 1507 diagrams of `src/test/resources/vega`
  (129 of them go through Graphviz) and for synthetic class and component
  diagrams of up to 400 nodes;
- speed: Graphviz itself is 1.5 to 5 times slower, but it is a small share of
  the time PlantUML takes to render a diagram. The whole render took 41% longer
  in total over the 129 vega diagrams that use Graphviz (small ones: about 8 ms
  each), and 7 to 40% longer on most of the large synthetic ones (one outlier
  at 2x, on a single measurement). The first diagram of a fresh page, Viz
  instance included, took about 40 ms longer (median of 9 cold starts).
