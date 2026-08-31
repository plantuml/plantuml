# Outline Integration — Proof of Concept

This document describes how Outline could close a real gap in its existing
PlantUML support, using the same pure-JavaScript PlantUML engine and the
same "render it the way you already render Mermaid" approach proposed to
GitHub and Notion in this directory — with **zero server-side
dependencies**.

## Where things stand today

Outline already merged PlantUML support once: [PR #10379](https://github.com/outline/outline/pull/10379),
shipped in v1.0.0 (October 2025), lets you paste an `editor.plantuml.com`
link and Outline renders it as an embedded SVG. It works, but it's a
second-class citizen next to Mermaid:

- It renders by sending the diagram source to a public image service
  (`img.plantuml.biz/plantuml/svg/`) on every view — not private, and
  dependent on that third party staying up.
- Self-hosted PlantUML servers aren't supported yet; the PR's own
  follow-up discussion notes this as a planned "future integration
  mechanism using Outline's settings."
- It's a *link embed* (paste a URL, get a static image + an "Open"
  button that sends you back to an external editor), not a live code
  block. Mermaid, by contrast, is a first-class citizen: type `/mermaid`
  or set a code block's language to "Mermaid Diagram" and it renders
  in place, live, as you type.

Outline users have been asking for exactly this in [Discussion #3057](https://github.com/outline/outline/discussions/3057)
for years — the merged PR narrowed the gap but didn't close it.

## The fix: give PlantUML the same treatment as Mermaid

Outline already proves, with Mermaid, that it's comfortable rendering
diagram-as-code client-side, in-process, with no sandbox and no server
round trip — Mermaid's renderer is a pure "text in, SVG out" function
bundled into the editor. PlantUML's TeaVM-compiled JavaScript engine is
the same shape: two exported functions, `render()` and `renderToString()`,
also pure "text in, SVG out." Wiring it into Outline's editor the same
way Mermaid is wired in isn't a new capability to build — it's the
existing `/mermaid` mechanism pointed at a second engine, exactly the
pitch made to Notion in this same directory.

Doing it this way also happens to fully answer the "self-hosted PlantUML"
request from the PR thread — not by adding a server option to configure,
but by removing the need for a server at all. Every Outline deployment,
cloud or self-hosted, gets working PlantUML with nothing to install, no
setting to point at a PlantUML instance, and no diagram source ever
leaving the browser.

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

`plantuml.js` is an ES2015 module loaded via `<script type="module">`.
`viz-global.js` is a classic script and must be present in the page before
any rendering happens. The engine starts its internal worker lazily on the
first `render` / `renderToString` call — no explicit initialization step.

### Important: asynchronous rendering

`render()` returns immediately, but writes the SVG into the target DOM
element **asynchronously**, and rendering multiple diagrams in the same JS
context must be **serialized** — wait for one SVG to land before starting
the next, or use `renderToString(lines, onSuccess, onError)` if a string
result (rather than a live DOM write) fits Outline's editor node-view
model better. For a document with several PlantUML blocks rendering at
once, see [`GITHUB_INTEGRATION.md`](./GITHUB_INTEGRATION.md#sequential-vs-worker-rendering)
in this directory for the hidden-iframe-per-diagram pattern that
parallelizes this without blocking the editor.

## Two ways to wire it in

**In-process (matches how Mermaid is already done).** Add `plantuml.js` +
`viz-global.js` as bundled assets, give the editor a `PlantUmlDiagram` node
type the same shape as its existing Mermaid node, and call `render()` (or
`renderToString()`, if the node view wants a string to hand to an `<img>`
or inline `<svg>`) whenever the block's content changes:

```js
import { renderToString } from "./plantuml.js";

// Inside the PlantUML node view's update handler, mirroring
// however the existing Mermaid node view re-renders on edit:
function renderPlantUmlNode(source, targetElement) {
    const lines = source.split(/\r\n|\r|\n/);
    renderToString(
        lines,
        svg => { targetElement.innerHTML = svg; },
        error => { targetElement.textContent = `PlantUML error: ${error}`; }
    );
}
```

This is the simplest option, and it's consistent with the trust model
Outline already applies to Mermaid: no sandbox beyond what the editor's
own content-security setup already provides, because the engine's contract
is the same "diagram source in, SVG out" as Mermaid's.

**Sandboxed (if a stricter isolation boundary is wanted for user-authored
diagram source).** Exactly the iframe + `postMessage` pattern in
[`GITHUB_INTEGRATION.md`](./GITHUB_INTEGRATION.md#proposed-github-architecture)
and [`NOTION_INTEGRATION.md`](./NOTION_INTEGRATION.md#proposed-notion-architecture) —
one hidden, sandboxed iframe per diagram, `PLANTUML_RENDER` /
`PLANTUML_RESULT` / `PLANTUML_ERROR` messages. Worth it only if Outline
wants a different isolation posture for PlantUML than it already accepts
for Mermaid; otherwise it's strictly more moving parts for the same
result.

## Key Advantages for Outline

- **Zero server cost, and it directly answers the open "self-hosted"
  request.** No PlantUML server to install, configure, or point Outline
  at — cloud and self-hosted Outline instances both just work.
- **Privacy improvement over the current embed.** Diagram source never
  leaves the browser; today's merged PR sends it to a public image
  service on every view.
- **Consistency, not a new pattern.** Same "text in, SVG out" shape
  Outline already trusts for Mermaid — no new rendering architecture to
  design or review.
- **Live editing, like Mermaid.** A `/plantuml` slash command or a code
  block language, not a link-paste-and-hope-it-resolves embed.
- **Dark mode built-in.** A single boolean option switches the rendering
  theme, matching Outline's own light/dark toggle the same way it
  presumably already does for Mermaid.
- **Tiny API surface, tiny glue code.** Two exported functions
  (`render`, `renderToString`); the node-view wiring is a handful of
  lines, similar in shape to whatever already calls Mermaid's renderer.

## Existing demand

This isn't speculative. [Discussion #3057](https://github.com/outline/outline/discussions/3057)
has multi-year, recurring demand — including from users migrating off
Wiki.js specifically for this feature — and [PR #10379](https://github.com/outline/outline/pull/10379)
is proof Outline's own maintainers already agreed PlantUML belongs in the
product; they shipped a first version. The natural next step is closing
the gap between that first version and the native, live, server-free
experience Mermaid users already get.

## Files

This document lives in `docs/`, alongside `GITHUB_INTEGRATION.md` and
`NOTION_INTEGRATION.md`. The engine files it describes live in
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
| `OUTLINE_INTEGRATION.md` | **This file.** |
| `OBSIDIAN_INTEGRATION.md` | The same proposal, adapted for the `obsidian-plantuml` community plugin. |
| `LOGSEQ_INTEGRATION.md` | The same proposal, adapted for the `logseq-plantuml-plugin` community plugin. |
