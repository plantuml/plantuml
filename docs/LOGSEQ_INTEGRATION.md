# Logseq Integration — Proof of Concept

Logseq's own app core is open source, but it doesn't render any diagram
type natively — not Mermaid, not PlantUML, nothing. Everything goes
through Logseq's plugin marketplace. This document describes how the
existing PlantUML plugin for Logseq could add the pure-JavaScript
PlantUML engine used throughout this directory as a second rendering
backend, alongside the server it already talks to — with **zero server,
zero JVM, and (unlike the other platforms in this directory) a plugin
architecture that has already proven this exact pattern works**.

## Where things stand today

[`cofcool/logseq-plantuml-plugin`](https://github.com/cofcool/logseq-plantuml-plugin)
(MIT, 11 stars, still receiving small releases) is the maintained way to
get PlantUML into Logseq: type `/Draw plantuml diagram`, write PlantUML
source in the block that appears, hit "Render." It sends the source to
`https://www.plantuml.com/plantuml/png` by default, with a setting to
point it at a self-hosted server instead. Local rendering — the plugin's
own README lists "Supports `plantuml.jar`" as a todo item — was never
built.

Until recently there was a bigger, more general alternative:
[`npgrosser/logseq-diagrams-as-code`](https://github.com/npgrosser/logseq-diagrams-as-code)
(210 stars) rendered PlantUML, Mermaid, Graphviz, D2, and about fifteen
other diagram languages through the [Kroki](https://kroki.io) API
(self-hostable). It was, by star count, the default choice for diagrams
in Logseq. It was **archived by its owner on February 13, 2026**, with no
successor named. What's left is the smaller, single-purpose plugin above
— still server-dependent, still without a local option.

## Why this fits Logseq specifically

The interesting part isn't that a local option is missing — it's that
Logseq's plugin architecture has already proven a local option works,
just for the other diagram language. `cofcool/logseq-plantuml-plugin` is
a fork of [`benjypng/logseq-mermaid-plugin`](https://github.com/benjypng/logseq-mermaid-plugin),
and that Mermaid plugin renders **entirely client-side**: it bundles
`mermaid.js` itself, draws the diagram in the browser, and converts it to
a PNG via canvas — no server, no network call, using the same
`{{renderer :plugin-id, ...}}` macro-renderer + slash-command mechanism
both plugins share. When `cofcool` forked it to build PlantUML support,
the fork had to switch to a public server, for one reason only: there
was no pure-JavaScript PlantUML engine to bundle the way `benjypng`
bundled `mermaid.js`. That's the entire gap this proposal closes — not an
architectural one, just a "this didn't exist yet" one.

No one has proposed a pure-JS PlantUML backend on this plugin before, so
there's no prior "we don't want more custom JS here" objection to
navigate — unlike the equivalent conversation on Outline's PR, see
[`OUTLINE_INTEGRATION.md`](./OUTLINE_INTEGRATION.md#the-fix-give-plantuml-the-same-treatment-as-mermaid).
If anything, the opposite: Logseq's own marketplace submission guidelines
don't mention any restriction on bundling third-party JS or WASM, and
`benjypng`'s plugin is a working example of exactly that already
published and installable today.

## Existing demand

Diagram rendering generally has been a live, unresolved request for
years. [Mermaid diagram render support](https://discuss.logseq.com/t/mermaid-diagram-render-support/582)
has run since Logseq's early days, spanning two forum pages, with no
core-team commitment either way — a contributor (`@junyu`) floated native
support as something "a testing plugin" could explore given how young the
plugin system was at the time, and `@Carey_Black` asked, unanswered,
whether a diagram renderer could be "tightly packaged" into Logseq itself
rather than depending on a separate server process. That specific
complaint — not wanting a server in the loop — is exactly what a bundled
JS engine removes. Layered on top of that: the most popular all-in-one
diagram plugin just went dark, and the PlantUML-specific plugin that's
left has "no server" as an open todo, not a shipped feature.

## Try it right now

A working build of the engine is already live, no setup required:

**https://plantuml.github.io/plantuml/js-plantuml/index.html**

Type or paste any PlantUML source (`@startuml` … `@enduml`) on the left and
the SVG renders on the right, entirely in the browser — the same
`plantuml.js` + `viz-global.js` pair described below.

## PlantUML JS API

The entire public surface is two exported functions:

| Call | Purpose |
|------|---------|
| `import { render, renderToString } from './plantuml.js'` | Import the API from the ES2015 module. |
| `render(lines, targetId)` | Render a diagram into the DOM element with the given `id`. `lines` is an `Array<string>`. |
| `render(lines, targetId, { dark: true })` | Same, but produces a dark-mode SVG. |
| `renderToString(lines, onSuccess, onError)` | Render and deliver the SVG as a string via the `onSuccess(svg)` callback. Errors go to `onError(message)`. |

`plantuml.js` is an ES2015 module; `viz-global.js` is a classic script
that must be present before any rendering happens. The engine starts its
internal worker lazily on the first `render` / `renderToString` call — no
explicit initialization step.

### Important: asynchronous rendering

`render()` returns immediately, but writes the SVG into the target DOM
element **asynchronously**, and rendering multiple diagrams in the same JS
context must be **serialized** — wait for one SVG to land before starting
the next. A single Logseq page with many PlantUML blocks is the case that
matters here; [`GITHUB_INTEGRATION.md`](./GITHUB_INTEGRATION.md#sequential-vs-worker-rendering)
describes the hidden-iframe-per-diagram pattern that parallelizes this
without blocking the editor.

## How it could plug into the plugin

Logseq plugins are JavaScript/TypeScript, loaded through `@logseq/libs`
and distributed via the [Logseq marketplace](https://github.com/logseq/marketplace).
`cofcool/logseq-plantuml-plugin` already registers a slash command and a
macro renderer (`onMacroRendererSlotted`) that hands a block a place to
render into and a "Render" action to trigger it — the same shape
`benjypng`'s Mermaid plugin uses for its fully local rendering. Adding
`plantuml.js` and `viz-global.js` as bundled assets and wiring them into
that existing render step is additive, not a rewrite — a second backend
next to the current server call, the same way `render server` is
currently just a settings string:

```js
import { renderToString } from "./plantuml.js";

// Inside the plugin's existing macro-renderer callback, as an
// alternative to the current fetch-from-plantuml.com call:
function renderWithJsEngine(source, targetElement, dark) {
    const lines = source.split(/\r\n|\r|\n/);
    renderToString(
        lines,
        svg => { targetElement.innerHTML = svg; },
        error => { targetElement.textContent = `PlantUML error: ${error}`; }
    );
}
```

Logseq's marketplace submission guidelines don't document a restriction
on bundling third-party JS or WASM the way Obsidian's review process
explicitly does — see [`OBSIDIAN_INTEGRATION.md`](./OBSIDIAN_INTEGRATION.md#how-it-could-plug-into-the-plugin)
for that contrast. The only marketplace-review note found is that
enabling the `effect` capability flag triggers "more strict" review,
without the docs specifying what it governs — worth a direct check with
the Logseq team before assuming it's a non-issue, but nothing suggests
bundling a rendering engine is the kind of thing it targets.

## Key Advantages for this plugin

- **Removes the plugin's only external dependency.** No more relying on
  `plantuml.com` (or a self-hosted server) staying reachable — the
  "Supports `plantuml.jar`" todo gets solved without a JVM.
- **Privacy.** Diagram source never leaves the device, matching the
  motivation behind `benjypng`'s local Mermaid rendering.
- **Fills a real, current gap.** The most popular alternative
  (`logseq-diagrams-as-code`, 210 stars) was archived in February 2026
  with no replacement; this plugin is what's left, and it still can't
  render offline.
- **Not a new pattern for Logseq's plugin ecosystem.** `benjypng`'s
  Mermaid plugin already proves fully local, bundled-JS diagram
  rendering works within Logseq's macro-renderer architecture — this is
  PlantUML catching up to a precedent that already exists, just not
  reachable from Logseq's core the way Obsidian's native Mermaid is.
- **Additive, not disruptive.** A second backend option next to the
  existing server call; users who prefer pointing at their own PlantUML
  server keep that choice.
- **Tiny addition.** Two exported functions; the render-step wiring is a
  handful of lines, similar in shape to whatever already calls the
  current `fetch()`-based renderer.

## Files

This document lives in `docs/`, alongside `GITHUB_INTEGRATION.md`,
`NOTION_INTEGRATION.md`, `OUTLINE_INTEGRATION.md`, and
`OBSIDIAN_INTEGRATION.md`. The engine files it describes live in
[`src/main/resources/teavm/`](../src/main/resources/teavm/):

| File | Description |
|------|-------------|
| `plantuml.js` | TeaVM-compiled PlantUML engine (generated by the build). |
| `viz-global.js` | Graphviz (Viz.js) layout engine. |
| `index.html` | Full playground with split editor — the same one live at the demo URL above. |

And in `docs/`:

| File | Description |
|------|-------------|
| `GITHUB_INTEGRATION.md` | The original proposal, for GitHub. |
| `NOTION_INTEGRATION.md` | The same proposal, adapted for Notion. |
| `NOTION_EMBED_HOWTO.md` | A zero-code Notion workaround usable today. |
| `OUTLINE_INTEGRATION.md` | The same proposal, adapted for Outline. |
| `OBSIDIAN_INTEGRATION.md` | The same proposal, adapted for the `obsidian-plantuml` community plugin. |
| `LOGSEQ_INTEGRATION.md` | **This file.** |
