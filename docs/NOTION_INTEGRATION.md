# Notion Integration — Proof of Concept

This document describes how Notion could integrate the PlantUML JavaScript
engine to render `plantuml` code blocks directly inside pages — in the web
app, the desktop app, and the mobile apps — with **zero server-side
dependencies**.

It follows the same recipe as [`GITHUB_INTEGRATION.md`](./GITHUB_INTEGRATION.md)
in this directory. The two documents share the engine, the API, and most of
the sandboxing pattern; only the host integration point changes.

## Try it right now

Working builds of the engine are already live, no setup required:

- **https://plantuml.github.io/plantuml/js-plantuml/index.html** — the
  playground. Type or paste any PlantUML source (`@startuml` … `@enduml`)
  on the left and the SVG renders on the right, entirely in the browser.
  This is the exact `plantuml.js` + `viz-global.js` pair described below —
  nothing server-side is involved in that page.
- **https://plantuml.github.io/plantuml/js-plantuml/github-integration-web-worker-poc.html**
  — a working integration demo showing several diagrams rendering in
  parallel, each in its own hidden iframe, without blocking the page. This
  is the pattern most relevant to a busy Notion page with multiple
  diagrams — see [Rendering many diagrams on one page](#rendering-many-diagrams-on-one-page)
  below.

Either page is the fastest way for anyone on Notion's side to see the
actual output quality and rendering behavior before reading any of the
integration details.

## Why this is a small ask for Notion specifically

Notion already renders diagrams client-side today: a code block with its
language set to **Mermaid** renders live, in place, with no server round
trip. That means the plumbing this proposal needs — detect a code block's
language, hand its text to a JS rendering engine, insert the resulting SVG
back into the block — already exists and already ships to every Notion
user, on every platform.

This proposal is not "build a diagramming feature." It's "point the
mechanism you already built at a second engine." Concretely: add
`PlantUML` next to `Mermaid` in the code block's language list, and route
blocks with that language through `plantuml.js` instead of (or alongside)
the Mermaid renderer.

## PlantUML JS API

The entire public surface is two exported functions:

| Call | Purpose |
|------|---------|
| `import { render, renderToString } from './plantuml.js'` | Import the API from the ES2015 module. |
| `render(lines, targetId)` | Render a diagram into the DOM element with the given `id`. `lines` is an `Array<string>`. |
| `render(lines, targetId, { dark: true })` | Same, but produces a dark-mode SVG. |
| `renderToString(lines, onSuccess, onError)` | Render and deliver the SVG as a string via the `onSuccess(svg)` callback. Errors go to `onError(message)`. |

`plantuml.js` is an ES2015 module loaded via `<script type="module">`.
`viz-global.js` is a classic script loaded via a plain `<script>` tag and
must be present in the page before any rendering happens.
The engine starts its internal worker lazily on the first `render` /
`renderToString` call — no explicit initialization is required.

### Important: asynchronous rendering

`render()` returns immediately, but writes the SVG into the target
DOM element **asynchronously**. This has two consequences:

1. You cannot read `targetElement.innerHTML` right after calling `render()`
   — the SVG is not there yet.
2. When rendering multiple diagrams in the same page (same JS context),
   you must **serialize** renders: wait for the first SVG to appear in the
   DOM before starting the next render. The engine uses shared internal
   state and will silently overwrite the previous result otherwise.

If you need the SVG as a string instead of a DOM element, use
`renderToString(lines, onSuccess, onError)` — the callback receives the
fully-formed SVG once rendering completes.

In the iframe-per-diagram architecture described below, the serialization
issue does not apply because each iframe has its own isolated engine
instance.

## Proposed Notion Architecture

Whatever Notion currently does to isolate its Mermaid renderer — an iframe
sandbox, a Web Worker, or an inline call inside a trusted first-party
bundle — the same isolation boundary works for PlantUML, because the
engine's entire contract is "give it lines of text, get back an SVG or an
error." Below is the iframe-sandboxed variant, mirroring the one already
proposed to GitHub for its Mermaid-style sandbox, since it is the safest
default if Notion's current Mermaid isolation model isn't a known
constraint here:

```
notion.so / desktop app / mobile webview     notion-controlled sandbox origin
┌──────────────────────────────┐             ┌──────────────────────────┐
│                               │             │                          │
│ Block renderer detects a     │             │  plantuml.js (module)    │
│ code block with               │  ──────►   │  viz-global.js           │
│ language: "plantuml"          │  postMsg    │                          │
│                               │             │  import { render }       │
│ Creates a sandboxed <iframe> │  ◄──────    │  render(lines, id, ...)  │
│ (or reuses the Mermaid one)  │  postMsg     │                          │
│                               │  (SVG)      │  Runs in sandbox:        │
│ Inserts SVG into the block   │             │  allow-scripts only      │
└──────────────────────────────┘             └──────────────────────────┘
```

This works identically across Notion's surfaces because the engine is pure
JavaScript with no native dependency: the web app, the Electron-based
desktop app, and the Chromium/WebKit webviews used on mobile all run it the
same way. There is no per-platform rendering code to write or maintain —
one engine, one integration, every surface, including fully offline once
the two JS files are cached.

### 1. Sandbox side (iframe renderer)

Each iframe loads `viz-global.js` as a classic script and imports
`plantuml.js` as an ES2015 module, then listens for an incoming render
request via `postMessage`. The engine starts its worker lazily on the
first `render()` call.

Because each iframe is its own isolated context, the asynchronous nature
of `render()` is handled naturally: we use a `MutationObserver` to detect
when the SVG has been inserted, then send the result back.

```js
import { render } from './plantuml.js';

const ALLOWED_ORIGIN = 'https://www.notion.so'; // adjust for desktop/mobile hosts as needed

const renderTarget = document.createElement('div');
renderTarget.id = 'plantuml-output';
document.body.appendChild(renderTarget);

window.addEventListener('message', (event) => {
    if (event.origin !== ALLOWED_ORIGIN) return;

    const { type, source, requestId, options } = event.data;
    if (type !== 'PLANTUML_RENDER') return;

    const lines = source.split(/\r\n|\r|\n/);
    const dark = options?.dark ?? false;

    // Watch for the SVG to appear in the DOM
    const observer = new MutationObserver(() => {
        if (renderTarget.querySelector('svg')) {
            observer.disconnect();
            window.parent.postMessage({
                type: 'PLANTUML_RESULT',
                requestId,
                svg: renderTarget.innerHTML,
                height: renderTarget.scrollHeight
            }, event.origin);
        }
    });
    observer.observe(renderTarget, { childList: true, subtree: true });

    try {
        render(lines, 'plantuml-output', { dark });
    } catch (err) {
        observer.disconnect();
        window.parent.postMessage({
            type: 'PLANTUML_ERROR',
            requestId,
            error: err.message
        }, event.origin);
    }
});
```

### 2. Parent side (block scanner)

Inside the Notion client, a script finds all code blocks whose language is
`plantuml`, creates a sandboxed iframe for each one, and sends the source
text — this is the same scan Notion already performs to find `mermaid`
blocks, just matched against a second language string:

```js
const RENDERER_URL = 'https://<notion-controlled-sandbox-origin>/plantuml/frame.html';

function initPlantUMLBlocks() {
    const blocks = document.querySelectorAll('[data-block-language="plantuml"]');

    blocks.forEach((block, i) => {
        const source = block.textContent;
        const requestId = `puml-${i}-${Date.now()}`;
        const dark = document.documentElement.dataset.colorMode === 'dark';

        const iframe = document.createElement('iframe');
        iframe.src = RENDERER_URL;
        iframe.sandbox = 'allow-scripts';
        iframe.style.cssText = 'border:none; width:100%; overflow:hidden;';

        iframe.addEventListener('load', () => {
            iframe.contentWindow.postMessage({
                type: 'PLANTUML_RENDER',
                source,
                requestId,
                options: { dark }
            }, new URL(RENDERER_URL).origin);
        });

        window.addEventListener('message', (e) => {
            if (e.data.requestId !== requestId) return;
            if (e.data.type === 'PLANTUML_RESULT') {
                iframe.style.height = e.data.height + 'px';
            }
        });

        block.parentElement.replaceWith(iframe);
    });
}

document.addEventListener('DOMContentLoaded', initPlantUMLBlocks);
```

The `data-block-language="plantuml"` selector above is illustrative —
Notion's actual block model will have its own way of tagging a code
block's language; the only requirement is routing blocks tagged
`plantuml` through this same flow instead of (or in addition to) however
`mermaid` blocks are currently routed.

## Message Protocol

### Request: `PLANTUML_RENDER`

Sent from the Notion client to the iframe.

| Field | Type | Description |
|-------|------|-------------|
| `type` | `string` | Always `"PLANTUML_RENDER"`. |
| `source` | `string` | The full PlantUML source (including `@startuml` / `@enduml`). |
| `requestId` | `string` | Unique ID to correlate request and response. |
| `options.dark` | `boolean` | If `true`, render in dark mode. |

### Response: `PLANTUML_RESULT`

Sent from the iframe back to the Notion client.

| Field | Type | Description |
|-------|------|-------------|
| `type` | `string` | `"PLANTUML_RESULT"` on success, `"PLANTUML_ERROR"` on failure. |
| `requestId` | `string` | Echoed from the request. |
| `svg` | `string` | The rendered SVG markup (success only). |
| `height` | `number` | Pixel height of the rendered output (success only). |
| `error` | `string` | Error message (error only). |

## Key Advantages for Notion

- **Zero server cost.** No Java process, no Graphviz binary, no container.
  Everything runs in the browser, the desktop app, or the mobile webview.
- **One engine, every surface.** Because it's pure JS with no native code,
  the same integration covers web, desktop (Electron), and mobile
  (Chromium/WebKit webviews) without platform-specific rendering paths.
- **Same sandbox pattern Notion already ships for Mermaid.** No new
  isolation model to design or get security sign-off on.
- **Tiny API surface.** Two exported functions (`render`, `renderToString`).
  ~40 lines of glue code total.
- **Dark mode built-in.** A single boolean option switches the rendering
  theme, matching Notion's light/dark toggle.
- **Works offline.** Once the two JS files are cached, no network round
  trip is needed to render or re-render a diagram — useful for the
  desktop app and for users on flaky connections.
- **Self-contained.** Two JS files (`plantuml.js` + `viz-global.js`) with
  no additional dependencies.
- **No source leaves the device.** Unlike the server-based workarounds
  Notion users currently rely on (see below), diagram source text never
  has to be sent to a third-party rendering service.

## Multiple Diagrams per Page

Each `plantuml` code block gets its own iframe and its own `requestId`.
Because each iframe is a separate browsing context with its own engine
instance, multiple diagrams render independently and in parallel — there
is no need to serialize them.

## Rendering many diagrams on one page

Notion pages routinely accumulate more blocks than a GitHub README ever
would — a spec doc with a dozen sequence diagrams, a wiki page embedding a
gallery of linked sub-pages that each render one. The render engine has a
constraint worth planning around from day one: `render()` writes its SVG
into the DOM **asynchronously** and uses shared internal state, so two
renders in the *same* JS context must be serialized (see
[Important: asynchronous rendering](#important-asynchronous-rendering)) —
render one, wait for its SVG to land, then start the next.

There's a live, working demonstration of the fix:
**https://plantuml.github.io/plantuml/js-plantuml/github-integration-web-worker-poc.html**
gives each diagram its **own hidden iframe** — created on demand, sent the
source via `postMessage`, and destroyed once its SVG comes back — so a
page with several diagrams renders all of them in parallel instead of
queueing.

One clarification worth having ready, because it will come up: the demo's
own badge calls this a "Web Worker" pattern, but it isn't implemented with
the `Worker` API. The TeaVM-compiled engine calls
`document.getElementById()` and writes SVG straight into the DOM, and real
Web Workers have no DOM — so they can't run it. A hidden iframe gives each
render its own DOM *and* its own JS execution context, which is what
actually isolates renders from each other and keeps the page responsive;
that's the mechanism, even though the demo names it after the API it
stands in for.

The demo's iframes use `srcdoc` and stay same-origin with the parent page
purely to keep the concurrency demo in one static file — that's a demo
shortcut, not a security recommendation. In Notion's [proposed architecture](#proposed-notion-architecture)
above, this same "one hidden iframe per diagram, rendered in parallel"
pattern should be combined with the sandboxed, isolated-origin iframe
already described there, the same way the block already isolates Mermaid.
The `PLANTUML_RENDER` / `PLANTUML_RESULT` / `PLANTUML_ERROR` protocol
doesn't change — only how many iframes exist at once does.

Trade-off worth flagging: parallel rendering costs more memory (one engine
instance per in-flight diagram) and each diagram has a slightly longer
startup (iframe creation + engine load). For the common case — a handful
of diagrams per page — this is a non-issue; it's worth measuring for a
page with dozens of diagrams, or for a database gallery view rendering
many pages' diagrams at once.

## Existing demand from Notion's own users

This isn't a speculative feature. Notion users already build and share
workarounds for PlantUML today — a public integration that pulls
`plantuml` code blocks through the Notion API and renders them via a
third-party service ([`rnovicky/notion-plantuml`](https://github.com/rnovicky/notion-plantuml)
using Kroki), blog write-ups walking through the same trick for sequence
diagrams, and open discussion threads asking how to get PlantUML into
Notion at all. Every one of these workarounds needs an external server and
a round trip; a native, client-side integration removes both.

## Potential Enhancements

- **Parallel rendering for multi-diagram pages.** Already demonstrated
  live, not just proposed — see
  [Rendering many diagrams on one page](#rendering-many-diagrams-on-one-page)
  above.
- **Lazy loading.** Only load the engine, and only create a renderer, for
  pages that actually contain a `plantuml` block. This keeps page load
  fast for the overwhelming majority of pages that don't use diagrams.
- **Caching.** Hash the PlantUML source and cache the SVG output (e.g. in
  `sessionStorage` or alongside however Notion already caches rendered
  Mermaid output) to skip re-rendering on navigation.
- **Size budget.** `plantuml.js` is several MB. A lazy-load strategy
  (only fetch when a `plantuml` block is detected) avoids impacting pages
  without diagrams — the same principle Notion presumably already applies
  to keep its own bundle lean.

## Files Referenced in This Proposal

This document lives in `docs/`, alongside `GITHUB_INTEGRATION.md`. The
engine and the PoC pages it describes live in
[`src/main/resources/teavm/`](../src/main/resources/teavm/):

| File | Description |
|------|-------------|
| `plantuml.js` | TeaVM-compiled PlantUML engine (generated by the build). |
| `viz-global.js` | Graphviz (Viz.js) layout engine. |
| `index.html` | Full playground with split editor — the same one live at the demo URL above. |
| `github-integration-poc.html` | The GitHub-oriented integration PoC (basic, sequential) this proposal is modeled on. Live: [github-integration-poc.html](https://plantuml.github.io/plantuml/js-plantuml/github-integration-poc.html) |
| `github-integration-web-worker-poc.html` | The parallel-rendering (hidden-iframe) variant referenced in [Rendering many diagrams on one page](#rendering-many-diagrams-on-one-page). Live: [github-integration-web-worker-poc.html](https://plantuml.github.io/plantuml/js-plantuml/github-integration-web-worker-poc.html) |

And in `docs/`:

| File | Description |
|------|-------------|
| `GITHUB_INTEGRATION.md` | The GitHub proposal this document mirrors. |
| `NOTION_INTEGRATION.md` | **This file.** |

A `notion-integration-poc.html`, built the same way as
`github-integration-poc.html`, would be a natural next step once there is
someone on Notion's side to hand a working prototype to.
