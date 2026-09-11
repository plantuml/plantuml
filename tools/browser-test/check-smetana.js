'use strict';
// Functional check that "!pragma layout smetana" works in the browser (TeaVM) engine.
//
// usage: node check-smetana.js target=<dir-or-js>
//   target   a directory containing plantuml.js (viz-global.js is served from there
//            too for the default-path control), or a path to the engine .js file itself
//
// Before the fix this check pins, the browser build parsed the pragma and then
// silently ignored it: CommandPragma skipped all layout handling under TeaVM and
// the maker selection took CucaDiagramFileMakerTeaVM unconditionally, so
// CucaDiagramFileMakerSmetana was dead-code-eliminated from the generated
// JavaScript and every Graphviz-family diagram required viz-global.js and
// WebAssembly.
//
// The contract checked here, on a page that does NOT load viz-global.js: each
// Graphviz-family diagram type (class, component, deployment, state, usecase)
// declaring "!pragma layout smetana" renders a real SVG with pure-Java layout and
// zero WebAssembly involvement. On a control page WITH viz-global.js, the same
// diagrams without the pragma still render through the Graphviz bridge (observed
// via a WebAssembly.instantiate hook), so the default path is unchanged, and with
// the pragma they render without touching WebAssembly at all.
const fs = require('fs');
const path = require('path');
const { createCheckReporter, isErrorImage } = require('../lib/browser-check');
const { parseTargetArg } = require('../lib/browser-cli');
const { createMountedServer, startServer } = require('../lib/browser-http');
const { createModulePageHtml, loadPlaywright, makeRenderModuleBody, maybeScriptTag, openReadyPage, renderOn } = require('../lib/browser-page');

const pw = loadPlaywright();
const { dir, file } = parseTargetArg(process.argv, 'node check-smetana.js target=<dir-or-js>');

if (!fs.existsSync(path.join(dir, 'viz-global.js'))) {
  console.error('viz-global.js not found next to the engine in ' + dir + ' (needed for the control page)');
  process.exit(2);
}

// The WebAssembly hook counts instantiations so a check can assert whether a
// render used the Graphviz bridge (viz-global.js is WebAssembly) or not.
const hook = `<script>
window.__wasm = 0;
['compile','instantiate','instantiateStreaming','compileStreaming'].forEach(function (k) {
  var o = WebAssembly[k];
  if (o) WebAssembly[k] = function () { window.__wasm++; return o.apply(WebAssembly, arguments); };
});
</script>`;

const pageHtml = withViz => createModulePageHtml({
  headHtml: hook,
  bodyHtml: withViz ? maybeScriptTag(dir, 'viz-global.js', '/viz-global.js') : '',
  modulePath: `/${file}`,
  moduleBody: makeRenderModuleBody({ maxSvgSize: 98304 }),
});

const server = createMountedServer({
  routes: {
    '/index.html': { contentType: 'text/html', body: pageHtml(false) },
    '/index-viz.html': { contentType: 'text/html', body: pageHtml(true) },
  },
  mounts: [{ prefix: '/', dir }],
});

const { check, finish } = createCheckReporter();

const FAMILIES = [
  ['class', ['class Car {', '  +drive(): void', '}', 'class Engine', 'class Wheel', 'Car *-- Engine', 'Car *-- "4" Wheel']],
  ['component', ['[Web UI] --> [API Gateway]', '[Mobile App] --> [API Gateway]', '[API Gateway] --> [Orders]']],
  ['deployment', ['node "Load Balancer" as lb', 'node "App Server" as app', 'database "Primary" as db', 'lb --> app', 'app --> db']],
  ['state', ['[*] --> Idle', 'Idle --> Running : start', 'Running --> Idle : stop', 'Running --> [*]']],
  ['usecase', ['actor User', 'User --> (Login)', 'User --> (Browse)', '(Browse) --> (Checkout)']],
  // The paths below have their own machinery on the Smetana side and are pinned separately:
  // composite states go through CucaDiagramSimplifierStateSmetana and a nested sub-layout,
  // packages go through the cluster export, a note on a link goes through the opale path,
  // and left to right direction goes through the rankdir attribute.
  ['composite state', ['[*] --> Working', 'state Working {', '  [*] --> Fetching', '  Fetching --> Parsing : done', '}', 'Working --> [*] : shutdown']],
  ['packaged component', ['package "Frontend" {', '  [Web UI]', '}', 'package "Backend" {', '  [API Gateway]', '}', '[Web UI] --> [API Gateway]']],
  ['link note class', ['class Car', 'class Engine', 'Car *-- Engine', 'note on link: fitted at the factory']],
  ['left to right class', ['left to right direction', 'class Car', 'class Engine', 'class Wheel', 'Car *-- Engine', 'Car *-- Wheel']],
];
const diagram = (body, pragma) => ['@startuml', ...(pragma ? ['!pragma layout smetana'] : []), ...body, '@enduml'];

(async () => {
  const port = await startServer(server);
  const browser = await pw.chromium.launch({ headless: true });

  // Page 1: engine only, no viz-global.js. The pragma must be enough.
  const bareReady = await openReadyPage(browser, `http://127.0.0.1:${port}/index.html`, { polling: 200 });
  const bare = bareReady.page;
  const bareErrors = bareReady.errors;

  for (const [label, body] of FAMILIES) {
    const r = await renderOn(bare, diagram(body, true), {
      includeWasmCount: true,
      includeShapeCounts: true,
      maxTextLength: 120,
    });
    const ok = !r.thrown && !!r.svg && !isErrorImage(r.svg) && r.shapes > 0 && r.texts > 0 && r.wasm === 0;
    check(`smetana ${label} diagram renders without viz-global.js`, ok,
      r.thrown || (!r.svg ? 'no svg: ' + r.text.slice(0, 120)
        : isErrorImage(r.svg) ? 'error image'
        : r.wasm !== 0 ? 'unexpected WebAssembly use (' + r.wasm + ')'
        : 'svg but no drawn content (shapes=' + r.shapes + ' texts=' + r.texts + ')'));
  }
  check('no unhandled page errors on the viz-less page', bareErrors.length === 0, bareErrors.join(' | '));

  // Page 2 (control): viz-global.js loaded. Without the pragma the Graphviz
  // bridge must still be used (default path unchanged); with the pragma the
  // render must not touch WebAssembly.
  const ctrlReady = await openReadyPage(browser, `http://127.0.0.1:${port}/index-viz.html`, { polling: 200 });
  const ctrl = ctrlReady.page;
  const ctrlErrors = ctrlReady.errors;

  const viaViz = await renderOn(ctrl, diagram(FAMILIES[0][1], false), {
    includeWasmCount: true,
    maxTextLength: 120,
  });
  check('control: class diagram without the pragma still uses the Graphviz bridge',
    !viaViz.thrown && !!viaViz.svg && !isErrorImage(viaViz.svg) && viaViz.wasm > 0,
    viaViz.thrown || (!viaViz.svg ? 'no svg: ' + viaViz.text.slice(0, 120)
      : viaViz.wasm === 0 ? 'render used no WebAssembly, default path changed' : 'error image'));

  const viaSmetana = await renderOn(ctrl, diagram(FAMILIES[0][1], true), {
    includeWasmCount: true,
    maxTextLength: 120,
  });
  check('control: class diagram with the pragma ignores viz-global.js even when loaded',
    !viaSmetana.thrown && !!viaSmetana.svg && !isErrorImage(viaSmetana.svg) && viaSmetana.wasm === 0,
    viaSmetana.thrown || (!viaSmetana.svg ? 'no svg: ' + viaSmetana.text.slice(0, 120)
      : viaSmetana.wasm !== 0 ? 'WebAssembly used (' + viaSmetana.wasm + ')' : 'error image'));

  check('no unhandled page errors on the control page', ctrlErrors.length === 0, ctrlErrors.join(' | '));

  await browser.close();
  server.close();
  finish({ uppercase: true });
})().catch(e => { console.error(e); process.exit(2); });
