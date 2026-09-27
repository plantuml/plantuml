# GitHub Integration — Proof of Concept

This document describes how GitHub could integrate the PlantUML JavaScript engine
to render ` ```plantuml ` fenced code blocks directly in Markdown previews,
READMEs, issues, and pull requests — with **zero server-side dependencies**.

## Quick Start

Open `github-integration-poc.html` in a browser.
Because it sits alongside `plantuml.js` and `viz-global.js`, the two example
diagrams in section 4 are rendered live by the real TeaVM-compiled engine.

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

## Standard Library and Support Scripts

`!include <C4/C4_Context>` and friends lazy-load one bundle per library
(`c4.min.js`, `azure.min.js`, ...). Themes, emoji and OpenIconic sprites are
lazy-loaded the same way (`themes.js`, `emoji.js`, `openiconic.js`). The
loader injects a script tag with a **relative** URL, which resolves against
the hosting page, so out of the box these features only work when those
files sit next to the page. Two globals, set before rendering, change that:

```html
<script>
  // Where the bundles live. One line gives a page that imports the engine
  // from a CDN the entire stdlib (and themes/emoji), fetched on demand.
  // The value is a plain URL prefix: note the trailing slash.
  window.PLANTUML_STDLIB_BASE = "https://plantuml.github.io/plantuml/js-plantuml/";
</script>
```

For production, self-host the files your diagrams use and point the base at
your own assets instead of a third-party origin.

Hosts that cannot load scripts at all set a loader callback instead:

```html
<script>
  // Deliver bundles as data (no script execution): fetch <lib>.json,
  // populate the engine's globals, call ok(). Return false to decline a
  // URL (themes.js, emoji.js, ...) and let the script tag handle it.
  window.PLANTUML_STDLIB_LOADER = function (url, ok, err) {
    if (!/\.min\.js$/.test(url)) return false;
    var lib = url.replace(/\.min\.js$/, "");
    fetch("/stdlib-json/" + lib + ".json")
      .then(function (r) { if (!r.ok) throw new Error("HTTP " + r.status); return r.json(); })
      .then(function (d) {
        window.PLANTUML_STDLIB = window.PLANTUML_STDLIB || {};
        window.PLANTUML_STDLIB[lib] = d.files;
        window.PLANTUML_STDLIB_JSON = window.PLANTUML_STDLIB_JSON || {};
        window.PLANTUML_STDLIB_JSON[lib] = d.json || {};
        window.PLANTUML_STDLIB_INFO = window.PLANTUML_STDLIB_INFO || {};
        window.PLANTUML_STDLIB_INFO[lib] = d.info;
        ok();
      })
      .catch(function (e) { err(String(e)); });
  };
</script>
```

This is the path for a browser extension (store rules allow fetching remote
data but not executing remote code) and for a Web Worker (no document to
append a script tag to). With neither global set, behaviour is unchanged.
`tools/browser-test/check-stdlib-loader.js` pins the whole contract.

## Multiple Diagrams per Page

Each ` ```plantuml ` block gets its own iframe and its own `requestId`.
Because each iframe is a separate browsing context with its own engine
instance, multiple diagrams render independently and in parallel — there
is no need to serialize them.

## Potential Enhancements

- **Web Worker.** For complex diagrams, wrapping the render call in a
  Web Worker prevents blocking the main thread. The `postMessage` API
  remains the same.
- **Lazy loading.** Only create iframes for diagrams visible in the viewport
  (IntersectionObserver). This keeps page load fast even for READMEs with
  many diagrams.
- **Caching.** Hash the PlantUML source and cache the SVG output in
  `sessionStorage` or a service worker to skip re-rendering on navigation.
- **Size budget.** `plantuml.js` is several MB. A lazy-load strategy
  (only fetch when a ` ```plantuml ` block is detected) avoids impacting
  pages without diagrams.

## Files in This Directory

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
| `github-integration-poc.html` | GitHub integration PoC — renders diagrams sequentially in the main thread using a serialized queue. Simpler, lower memory usage, but blocks the main thread during rendering. |
| `github-integration-web-worker-poc.html` | GitHub integration PoC (Web Worker variant) — renders each diagram in its own hidden iframe, enabling parallel rendering without blocking the main thread. Higher memory usage (one engine instance per iframe) but better responsiveness. |
| `GITHUB_INTEGRATION.md` | **This file** — documentation. |
