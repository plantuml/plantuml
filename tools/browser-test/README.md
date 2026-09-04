# browser-test

Functional checks for the PlantUML browser (TeaVM) engine, run in headless Chromium against a
real build. Unlike `perf-bench`, which measures speed and never fails a build, these assert
behaviour and exit non-zero when it is wrong.

## check-themes.js

Checks that `!theme <name>` works in the browser.

Themes live in `src/main/resources/themes` as `.puml` files, which the browser build cannot read
because it has no classpath. They are shipped to it instead through the generated
`teavm/themes.js`, loaded on demand the same way `emoji.js` is. This check covers that path end
to end:

- a theme changes the drawing, and changes it to that theme's own colours;
- loading a theme by name produces exactly what pasting the theme's body into the diagram does,
  which is the real correctness property (both sides are rendered by the same engine, so only
  the loading path differs);
- an unknown theme name is reported rather than ignored;
- `%get_current_theme()` returns the metadata from the theme's YAML header;
- `%get_all_theme()` does not advertise more themes than the engine ships;
- every bundled theme loads and changes the drawing, except ones with an empty body
  (`_none_` is deliberately empty), which must leave it unchanged;
- a host that populates `globalThis.PLANTUML_THEMES` itself works without `themes.js` being fetchable,
  which is what lets themes work inside a Web Worker, where the script loader has no document
  to append a script tag to;
- with neither, the directive fails loudly instead of rendering unthemed.

There are no golden files. The expected rendering for a theme is derived from that theme's own
text, read out of the `themes.js` being tested, so adding or editing a theme needs no fixture
update.

## check-background.js

Checks that the document background is painted the way the Java build paints it: a
background style on the svg element plus a rectangle covering the whole viewBox, for
`skinparam backgroundColor`, the `<style>` document form, and themes. The deliberate
skips are pinned too: transparent and the default white paint nothing, in dark mode as
well. Run the same way with `node check-background.js target=...`.

## check-smetana.js

Checks that `!pragma layout smetana` works in the browser. On a page without `viz-global.js`,
each Graphviz-family diagram type (class, component, deployment, state, usecase) declaring the
pragma must render a real SVG with zero WebAssembly involvement, observed through a hook on
`WebAssembly.instantiate`. On a control page with `viz-global.js`, the same diagram without the
pragma must still go through the Graphviz bridge (the default path is unchanged), and with the
pragma must not touch WebAssembly even though the bridge is available. Run the same way with
`node check-smetana.js target=...`.

## check-viz-missing.js

Checks how the engine behaves when `viz-global.js` is missing or broken. With viz absent,
Graphviz-family diagram types fall back to the Smetana layout engine and render normally
(check-viz-fallback.js pins the details), and native-layout types (sequence, activity) keep
rendering as always. With a Viz that is present but broken (its `instance()` rejects, the shape
of a failed load), the render must produce a visible crash report naming Viz instead of throwing
an unhandled exception and leaving the target element empty. A control page with a working
`viz-global.js` pins that the normal path is unchanged. Run the same
way with `node check-viz-missing.js target=...`.

## Running it

```
gradlew :plantuml-mit:npmPackage -Pci
cd tools/browser-test && npm ci && npx playwright install --with-deps --only-shell chromium
node check-themes.js target=../../plantuml-mit/build/npm-plantuml
```

`target` also accepts a path to the engine `.js` itself, and any directory holding a published
package works, so a release can be checked with `npm pack @plantuml/core && node check-themes.js
target=package`.

Running it against an engine built before themes were wired up fails 8 of the 12 checks, with
every bundled theme reported as leaving the drawing unchanged.

## check-viz-fallback.js

Checks that a page which loads only the engine, with no viz-global.js and no
pragma, still renders every Graphviz-family diagram type: the engine falls
back to the Smetana layout engine, uses no WebAssembly, and logs a one-time
console note. A diagram carrying the pragma renders with no note at all (the
pragma short-circuits the probe), and a partially loaded Viz (the global
exists but instance() is not a function) falls back the same way as an absent
one. A control page with viz-global.js pins that the default path is
unchanged (the Graphviz bridge is still used, and no note is logged).
`node check-viz-fallback.js target=...`.
