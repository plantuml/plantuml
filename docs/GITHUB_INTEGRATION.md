# GitHub Integration — Proof of Concept

This document describes how GitHub could integrate the PlantUML JavaScript engine
to render ` ```plantuml ` fenced code blocks directly in Markdown previews,
READMEs, issues, and pull requests — with **zero server-side dependencies**.

## Quick Start

Two live demos, no setup required:

- **https://plantuml.github.io/plantuml/js-plantuml/github-integration-poc.html**
  — basic variant: renders diagrams sequentially in the main thread.
- **https://plantuml.github.io/plantuml/js-plantuml/github-integration-web-worker-poc.html**
  — worker variant: renders each diagram in its own hidden iframe, in parallel,
  without blocking the main thread. See [Sequential vs. worker rendering](#sequential-vs-worker-rendering)
  below.

To run them locally instead, open `github-integration-poc.html` or
`github-integration-web-worker-poc.html` from
[`src/main/resources/teavm/`](../src/main/resources/teavm/) in a browser.
Because they sit alongside `plantuml.js` and `viz-global.js` in that
directory, the example diagrams are rendered live by the real
TeaVM-compiled engine.

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
DOM element **asynchronously**.  This has two consequences:

1. You cannot read `targetElement.innerHTML` right after calling `render()`
   — the SVG is not there yet.
2. When rendering multiple diagrams in the same page (same JS context),
   you must **serialize** renders: wait for the first SVG to appear in the
   DOM before starting the next render.  The engine uses shared internal
   state and will silently overwrite the previous result otherwise.

If you need the SVG as a string instead of a DOM element, use
`renderToString(lines, onSuccess, onError)` — the callback receives the
fully-formed SVG once rendering completes.

In the iframe-per-diagram architecture described below, the serialization
issue does not apply because each iframe has its own isolated engine instance.

## Proposed GitHub Architecture

GitHub already renders Mermaid diagrams client-side using a sandboxed iframe
pattern. The same approach works for PlantUML:

```
github.com                    render.githubusercontent.com
┌──────────────────┐          ┌──────────────────────────┐
│                  │          │                          │
│ Markdown parser  │          │  plantuml.js (module)    │
│ finds ```plantuml│ ──────►  │  viz-global.js           │
│ blocks           │ postMsg  │                          │
│                  │          │  import { render }       │
│ Creates <iframe> │ ◄──────  │  render(lines, id, ...)  │
│ per diagram      │ postMsg  │                          │
│                  │  (SVG)   │  Runs in sandbox:        │
│ Inserts SVG      │          │  allow-scripts only      │
└──────────────────┘          └──────────────────────────┘
```

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

const ALLOWED_ORIGIN = 'https://github.com';

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

### 2. Parent side (markdown scanner)

On `github.com`, a script finds all PlantUML code blocks, creates a sandboxed
iframe for each one, and sends the source text:

```js
const RENDERER_URL = 'https://render.githubusercontent.com/plantuml/frame.html';

function initPlantUMLBlocks() {
    const blocks = document.querySelectorAll('pre[lang="plantuml"]');

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

## Message Protocol

### Request: `PLANTUML_RENDER`

Sent from `github.com` to the iframe.

| Field | Type | Description |
|-------|------|-------------|
| `type` | `string` | Always `"PLANTUML_RENDER"`. |
| `source` | `string` | The full PlantUML source (including `@startuml` / `@enduml`). |
| `requestId` | `string` | Unique ID to correlate request and response. |
| `options.dark` | `boolean` | If `true`, render in dark mode. |

### Response: `PLANTUML_RESULT`

Sent from the iframe back to `github.com`.

| Field | Type | Description |
|-------|------|-------------|
| `type` | `string` | `"PLANTUML_RESULT"` on success, `"PLANTUML_ERROR"` on failure. |
| `requestId` | `string` | Echoed from the request. |
| `svg` | `string` | The rendered SVG markup (success only). |
| `height` | `number` | Pixel height of the rendered output (success only). |
| `error` | `string` | Error message (error only). |

## Key Advantages for GitHub

- **Zero server cost.** No Java process, no Graphviz binary, no container.
  Everything runs in the browser.
- **Same sandbox pattern as Mermaid.** The iframe isolation model is already
  deployed and battle-tested by GitHub.
- **Tiny API surface.** Two exported functions (`render`, `renderToString`). ~40 lines of glue code total.
- **Dark mode built-in.** A single boolean option switches the rendering theme,
  which aligns with GitHub's light/dark mode toggle.
- **Self-contained.** Two JS files (`plantuml.js` + `viz-global.js`) with no
  additional dependencies.

## Multiple Diagrams per Page

Each ` ```plantuml ` block gets its own iframe and its own `requestId`.
Because each iframe is a separate browsing context with its own engine
instance, multiple diagrams render independently and in parallel — there
is no need to serialize them.

## Sequential vs. worker rendering

The two PoCs above demonstrate the two ends of a trade-off, and both are
already built and running live — this isn't a "potential enhancement",
it's a working comparison:

- **`github-integration-poc.html`** (basic) — a single engine instance
  runs in one sandboxed iframe and diagrams are rendered one at a time
  (the serialization constraint from [above](#important-asynchronous-rendering)).
  Simpler, lower memory usage, but rendering N diagrams blocks on N
  sequential renders.
- **`github-integration-web-worker-poc.html`** (worker) — each diagram
  gets its **own hidden iframe**, created on demand and destroyed once
  its SVG is delivered. All iframes render in parallel, so the page never
  waits on a queue.

**Why hidden iframes and not literal `Worker` objects:** the TeaVM-compiled
engine calls `document.getElementById()` and writes SVG straight into the
DOM — real Web Workers have no DOM at all, so they cannot run it. A hidden
iframe gives each render its own DOM *and* its own JS execution context,
which is what actually decouples renders from one another and from the
main thread. It's the same trick, functionally, that a Web Worker would
be used for if the engine didn't need a DOM — hence the demo's "Web
Worker" label — but the DOM requirement is why it's implemented as an
iframe.

The worker PoC's hidden iframes use `srcdoc` and stay same-origin with the
parent page purely to demonstrate the concurrency model in a single static
file — that is a demo simplification, not a security recommendation. A
production integration should combine this pattern with the cross-origin,
`sandbox="allow-scripts"` isolation already described in
[Proposed GitHub Architecture](#proposed-github-architecture): one hidden,
cross-origin, sandboxed iframe per diagram, all rendering in parallel. The
message protocol above (`PLANTUML_RENDER` / `PLANTUML_RESULT` /
`PLANTUML_ERROR`) is unchanged either way — only how many iframes exist at
once, and where they're allowed to load from, differs.

Trade-off to flag for whoever implements this: the worker/parallel variant
uses more memory (one full engine instance per in-flight diagram) and has
a slightly longer per-diagram startup (iframe creation + engine load each
time) than the sequential variant. For a typical README or issue with a
handful of diagrams this is a non-issue; it's worth measuring for pages
with dozens.

## Potential Enhancements

- **Lazy loading.** Only create iframes for diagrams visible in the viewport
  (IntersectionObserver). This keeps page load fast even for READMEs with
  many diagrams.
- **Caching.** Hash the PlantUML source and cache the SVG output in
  `sessionStorage` or a service worker to skip re-rendering on navigation.
- **Size budget.** `plantuml.js` is several MB. A lazy-load strategy
  (only fetch when a ` ```plantuml ` block is detected) avoids impacting
  pages without diagrams.

## Files

This document lives in `docs/`. The engine and the PoC pages it describes
live in [`src/main/resources/teavm/`](../src/main/resources/teavm/):

| File | Description |
|------|-------------|
| `plantuml.js` | TeaVM-compiled PlantUML engine (generated by the build). |
| `viz-global.js` | Graphviz (Viz.js) layout engine. |
| `index-basic.html` | Minimal demo (textarea + live render). |
| `index-basic-dark.html` | Same, with dark mode. |
| `index.html` | Full playground with split editor. |
| `index-collection.html` | Multi-diagram collection page. |
| `main.js` | Playground logic (renderer, resize, controls). |
| `main.css` | Playground styles. |
| `github-integration-poc.html` | GitHub integration PoC (basic) — renders diagrams sequentially in the main thread using a serialized queue. Simpler, lower memory usage, but blocks on a render queue. Live: [github-integration-poc.html](https://plantuml.github.io/plantuml/js-plantuml/github-integration-poc.html) |
| `github-integration-web-worker-poc.html` | GitHub integration PoC (worker variant) — renders each diagram in its own hidden iframe, enabling parallel rendering without blocking the main thread. Higher memory usage (one engine instance per iframe) but better responsiveness. Live: [github-integration-web-worker-poc.html](https://plantuml.github.io/plantuml/js-plantuml/github-integration-web-worker-poc.html) |

And in `docs/`:

| File | Description |
|------|-------------|
| `GITHUB_INTEGRATION.md` | **This file** — documentation. |
| `NOTION_INTEGRATION.md` | The same proposal, adapted for Notion. |
