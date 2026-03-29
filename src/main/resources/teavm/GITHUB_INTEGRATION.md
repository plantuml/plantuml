# GitHub Integration — Proof of Concept

This document describes how GitHub could integrate the PlantUML JavaScript engine
to render ` ```plantuml ` fenced code blocks directly in Markdown previews,
READMEs, issues, and pull requests — with **zero server-side dependencies**.

## Quick Start

Open `github-integration-poc.html` in a browser.
Because it sits alongside `plantuml.js` and `viz-global.js`, the live demo
section will use the real TeaVM-compiled engine.
The upper part of the page (architecture diagrams, code samples) works
standalone with placeholder SVGs.

## PlantUML JS API

The entire public surface is three calls:

| Call | Purpose |
|------|---------|
| `plantumlLoad()` | One-time engine initialization (synchronous). |
| `window.plantuml.render(lines, targetId)` | Render a diagram into the DOM element with the given `id`. `lines` is an `Array<string>`. |
| `window.plantuml.render(lines, targetId, { dark: true })` | Same, but produces a dark-mode SVG. |

Both `plantuml.js` and `viz-global.js` must be loaded via `<script>` tags
before calling `plantumlLoad()`.

## Proposed GitHub Architecture

GitHub already renders Mermaid diagrams client-side using a sandboxed iframe
pattern. The same approach works for PlantUML:

```
github.com                    render.githubusercontent.com
┌──────────────────┐          ┌──────────────────────────┐
│                  │          │                          │
│ Markdown parser  │          │  plantuml.js             │
│ finds ```plantuml│ ──────►  │  viz-global.js           │
│ blocks           │ postMsg  │                          │
│                  │          │  plantumlLoad()           │
│ Creates <iframe> │ ◄──────  │  plantuml.render(...)     │
│ per diagram      │ postMsg  │                          │
│                  │  (SVG)   │  Runs in sandbox:        │
│ Inserts SVG      │          │  allow-scripts only      │
└──────────────────┘          └──────────────────────────┘
```

### 1. Sandbox side (iframe renderer)

The iframe loads `plantuml.js` + `viz-global.js`, calls `plantumlLoad()` once,
then listens for incoming render requests via `postMessage`:

```js
const ALLOWED_ORIGIN = 'https://github.com';

plantumlLoad();

const renderTarget = document.createElement('div');
renderTarget.id = 'plantuml-output';
document.body.appendChild(renderTarget);

window.addEventListener('message', (event) => {
    if (event.origin !== ALLOWED_ORIGIN) return;

    const { type, source, requestId, options } = event.data;
    if (type !== 'PLANTUML_RENDER') return;

    const lines = source.split(/\r\n|\r|\n/);
    const dark = options?.dark ?? false;

    try {
        window.plantuml.render(lines, 'plantuml-output', { dark });

        window.parent.postMessage({
            type: 'PLANTUML_RESULT',
            requestId,
            svg: renderTarget.innerHTML,
            height: renderTarget.scrollHeight
        }, event.origin);
    } catch (err) {
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
- **Tiny API surface.** Three function calls. ~40 lines of glue code total.
- **Dark mode built-in.** A single boolean option switches the rendering theme,
  which aligns with GitHub's light/dark mode toggle.
- **Self-contained.** Two JS files (`plantuml.js` + `viz-global.js`) with no
  additional dependencies.

## Multiple Diagrams per Page

Each ` ```plantuml ` block gets its own iframe and its own `requestId`.
The parent listener filters responses by `requestId`, so multiple diagrams
on the same page render independently and in parallel.

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
| `plantuml.js` | TeaVM-compiled PlantUML engine. |
| `viz-global.js` | Graphviz (Viz.js) layout engine. |
| `index-basic.html` | Minimal demo (textarea + live render). |
| `index-basic-dark.html` | Same, with dark mode. |
| `index.html` | Full playground with split editor. |
| `main.js` | Playground logic (renderer, resize, controls). |
| `main.css` | Playground styles. |
| `github-integration-poc.html` | **This PoC** — GitHub integration demo. |
| `GITHUB_INTEGRATION.md` | **This file** — documentation. |
