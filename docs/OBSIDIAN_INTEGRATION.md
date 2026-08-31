# Obsidian Integration — Proof of Concept

Obsidian's own app core is closed-source, so there's no repository to send
a proposal like [`GITHUB_INTEGRATION.md`](./GITHUB_INTEGRATION.md) or
[`OUTLINE_INTEGRATION.md`](./OUTLINE_INTEGRATION.md) to. But Obsidian has a
mature, open plugin ecosystem, and PlantUML already has an established
community plugin there. This document describes how that plugin — not
Obsidian itself — could add a fourth rendering backend: the pure-JavaScript
PlantUML engine used throughout this directory, with **zero server, zero
Java, and no per-platform code**.

## Where things stand today

[`joethei/obsidian-plantuml`](https://github.com/joethei/obsidian-plantuml)
(529 stars, MIT-licensed, actively maintained) is the established way to
get PlantUML into Obsidian. It offers three rendering backends, and all
three depend on something external to Obsidian itself:

- **PlantUML Online Server** (the default) — sends diagram source to a
  cloud service on every render.
- **A self-hosted server** (Docker / JEE / PicoWeb) — removes the
  third-party dependency, but now there's a server to run and keep
  reachable.
- **A local `.jar` file** — no network needed, but it needs a JVM
  installed and correctly configured, and the README itself notes it's
  "not as performant as using a server."

None of the three work without something outside Obsidian: a network
connection, a running server process, or a Java runtime.

## Why this fits Obsidian specifically

Obsidian's own core already draws the line these three options are missing.
Mermaid diagrams are built directly into Obsidian's markdown renderer —
not a plugin, not a server, not a JVM: write a ` ```mermaid ` code block
and it renders, on desktop and mobile alike, offline. A PlantUML plugin
that rendered the same way — bundled JS, zero external dependency — 
wouldn't be introducing anything exotic to the app; it would be catching
up to the bar Obsidian's own core already sets for Mermaid.

There's also a gap none of the existing three backends can close: **mobile**.
Obsidian runs on iOS and Android, and there is no JVM there — the local
`.jar` option is desktop-only by construction. On mobile, a privacy- or
offline-conscious user is left with the cloud server or a self-hosted
server reachable from their phone; there's currently no local-rendering
option at all. PlantUML's JS engine runs anywhere Obsidian's own JS runtime
runs, including the mobile app's webview — which makes it not just a
better desktop option, but the first PlantUML rendering path that would
work unmodified on mobile too.

## Existing demand

This isn't speculative:

- [Issue #6](https://github.com/joethei/obsidian-plantuml/issues/6),
  "Support local rendering," was opened specifically over **not wanting to
  send diagrams to a third-party server** — exactly the privacy argument
  for a fully client-side engine. It was resolved by adding the `.jar`
  option, which only partly answers it (it still requires trusting and
  maintaining a local JVM).
- [Issue #37](https://github.com/joethei/obsidian-plantuml/issues/37) is a
  concrete symptom of that partial fix: rendering hangs indefinitely on
  "Generating PlantUML diagram" specifically when using the local `.jar`
  backend — a JVM/configuration failure mode that a bundled JS engine with
  no external process simply can't hit.

No one has proposed a pure-JS rendering backend on this repo before, so
there's no prior "we don't want more custom JS here" call to navigate
around, unlike the equivalent conversation on Outline's PR — see
[`OUTLINE_INTEGRATION.md`](./OUTLINE_INTEGRATION.md#the-fix-give-plantuml-the-same-treatment-as-mermaid)
for that contrast.

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

`plantuml.js` is an ES2015 module; `viz-global.js` is a classic script that
must be present before any rendering happens. The engine starts its
internal worker lazily on the first `render` / `renderToString` call — no
explicit initialization step.

### Important: asynchronous rendering

`render()` returns immediately, but writes the SVG into the target DOM
element **asynchronously**, and rendering multiple diagrams in the same JS
context must be **serialized** — wait for one SVG to land before starting
the next. This matters less inside a single note (Obsidian's markdown
processor rarely renders dozens of PlantUML blocks in one note the way a
public README might), but for notes with several diagrams,
[`GITHUB_INTEGRATION.md`](./GITHUB_INTEGRATION.md#sequential-vs-worker-rendering)
describes the hidden-iframe-per-diagram pattern that parallelizes this
without blocking.

## How it could plug into the plugin

Obsidian plugins are TypeScript, bundled with esbuild into a single
`main.js` — `obsidian-plantuml` itself is built from the standard
`obsidian-sample-plugin` template. `plantuml.js` and `viz-global.js` would
ship as bundled assets alongside it, the same way the plugin already
bundles whatever talks to its local-jar and server backends today.

The plugin registers a markdown post-processor that hands PlantUML code
blocks a target DOM element to render into. `renderToString()` fits that
shape more directly than `render()`: get the SVG as a string, then set it
on the element the post-processor already gave you, no fabricated element
`id` required:

```js
import { renderToString } from "./plantuml.js";

// Inside the plugin's existing PlantUML code-block processor,
// as a fourth option alongside "server" / "self-hosted" / "local jar":
function renderWithJsEngine(source, targetElement, dark) {
    const lines = source.split(/\r\n|\r|\n/);
    renderToString(
        lines,
        svg => { targetElement.innerHTML = svg; },
        error => { targetElement.setText(`PlantUML error: ${error}`); }
    );
}
```

One Obsidian-specific constraint worth flagging up front: the community
plugin review process doesn't allow fetching and `eval`-ing remote code at
runtime. That's not a problem here — `plantuml.js` and `viz-global.js`
would ship bundled inside the plugin's own package like any other asset,
never fetched from a CDN at runtime, so this is a rendering-backend
addition, not a new remote-code pattern to get past review.

## Key Advantages for this plugin

- **Zero server, zero JVM.** Answers the original motivation behind
  [issue #6](https://github.com/joethei/obsidian-plantuml/issues/6) more
  completely than the local-`.jar` option that was shipped for it — no
  process to configure, nothing that can be "not installed" or
  misconfigured the way [issue #37](https://github.com/joethei/obsidian-plantuml/issues/37)
  shows the jar path can be.
- **Works on mobile.** The one platform none of the three existing
  backends fully cover.
- **Fully offline**, including the very first render — no "reach a
  server once to warm a cache" step.
- **Matches the bar Obsidian's own core already set for Mermaid** — not a
  new pattern, just PlantUML catching up to it.
- **Tiny addition.** Two exported functions, a handful of lines wiring
  them into the existing post-processor — additive to the current three
  backends, not a replacement for users who prefer them.
- **Dark mode built-in.** A single boolean option switches the rendering
  theme, matching Obsidian's own light/dark setting.

## Files

This document lives in `docs/`, alongside `GITHUB_INTEGRATION.md`,
`NOTION_INTEGRATION.md`, and `OUTLINE_INTEGRATION.md`. The engine files it
describes live in
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
| `OBSIDIAN_INTEGRATION.md` | **This file.** |
| `LOGSEQ_INTEGRATION.md` | The same proposal, adapted for the `logseq-plantuml-plugin` community plugin. |
