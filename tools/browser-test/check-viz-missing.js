'use strict';
// Functional check for how the browser (TeaVM) engine behaves when the
// Graphviz bridge (viz-global.js) is missing or broken.
//
// usage: node check-viz-missing.js target=<dir-or-js>
//   target   a directory containing plantuml.js (viz-global.js is served from there
//            too for the control page), or a path to the engine .js file itself
//
// The contract checked here has two halves:
//
// - viz-global.js ABSENT: Graphviz-family diagrams fall back to the Smetana
//   layout engine and render normally (check-viz-fallback.js pins the details
//   of that path; here one render per family type is enough). Diagram types
//   with native layout (sequence, activity) keep rendering as they always did.
//
// - viz-global.js PRESENT BUT BROKEN (a Viz whose instance() rejects, the
//   shape of a failed or partial load): the render must produce a visible
//   crash report that names Viz, must not leave the target empty, and must
//   not throw an unhandled page error. This exercises the error path that
//   used to be reachable with viz merely absent: an unhandled exception
//   escaping the render call with nothing drawn at all.
//
// A control page with a working viz-global.js pins that the normal path is
// unchanged.
const fs = require('fs');
const path = require('path');
const { createCheckReporter, isErrorImage } = require('../lib/browser-check');
const { parseTargetArg } = require('../lib/browser-cli');
const { createMountedServer, startServer } = require('../lib/browser-http');
const { createModulePageHtml, loadPlaywright, maybeScriptTag, openReadyPage } = require('../lib/browser-page');

const pw = loadPlaywright();
const { dir, file } = parseTargetArg(process.argv, 'node check-viz-missing.js target=<dir-or-js>');

if (!fs.existsSync(path.join(dir, 'viz-global.js'))) {
  console.error('viz-global.js not found next to the engine in ' + dir + ' (needed for the control page)');
  process.exit(2);
}

// Three pages from one server: /index.html loads only the engine,
// /index-broken.html defines a Viz whose instance() rejects, and
// /index-viz.html loads the real viz-global.js the way the demo pages do.
const brokenStub = `<script>
window.Viz = { instance: function () { return Promise.reject(new Error('Viz failed to initialize (simulated)')); } };
</script>`;
const pageHtml = mode => createModulePageHtml({
  bodyHtml: mode === 'viz' ? maybeScriptTag(dir, 'viz-global.js', '/viz-global.js') : mode === 'broken' ? brokenStub : '',
  modulePath: `/${file}`,
  moduleBody: `window.__render=(lines,id)=>render(lines,id,{maxSvgSize:98304});
window.__ready=1;`,
});

const server = createMountedServer({
  routes: {
    '/index.html': { contentType: 'text/html', body: pageHtml('bare') },
    '/index-broken.html': { contentType: 'text/html', body: pageHtml('broken') },
    '/index-viz.html': { contentType: 'text/html', body: pageHtml('viz') },
  },
  mounts: [{ prefix: '/', dir }],
});

const { check, getFailures } = createCheckReporter();

const CLASS = ['@startuml', 'class Car {', '  +drive(): void', '}', 'class Engine', 'Car *-- Engine', '@enduml'];
const COMPONENT = ['@startuml', '[Web UI] --> [API Gateway]', '[API Gateway] --> [Orders]', '@enduml'];
// A composite state is a distinct failure path: state diagrams run
// CucaDiagramSimplifierState before the dot text is even produced, so the inner
// layout can hit the broken engine earlier than the top-level one.
const STATE = ['@startuml', '[*] --> Working', 'state Working {', '  [*] --> Fetching', '  Fetching --> Parsing : done', '}', 'Working --> [*]', '@enduml'];
const SEQUENCE = ['@startuml', 'Alice -> Bob: hello', 'Bob --> Alice: hi', '@enduml'];
const ACTIVITY = ['@startuml', 'start', ':Receive order;', ':Charge card;', 'stop', '@enduml'];

async function renderOn(page, lines) {
  return page.evaluate(async ({ lines }) => {
    const out = document.getElementById('out');
    out.innerHTML = '';
    const done = new Promise(res => {
      const mo = new MutationObserver(() => {
        if (out.querySelector('svg') || out.textContent) { mo.disconnect(); res(); }
      });
      mo.observe(out, { childList: true, subtree: true });
    });
    let thrown = null;
    try { window.__render(lines, 'out'); } catch (e) { thrown = String(e && e.message || e); }
    if (!thrown) await Promise.race([done, new Promise(r => setTimeout(r, 30000))]);
    const svg = out.querySelector('svg');
    return { thrown, svg: svg ? svg.outerHTML : null, text: out.textContent || '',
      shapes: svg ? svg.querySelectorAll('path,polygon,line,rect,ellipse').length : 0 };
  }, { lines });
}

(async () => {
  const port = await startServer(server);
  const browser = await pw.chromium.launch({ headless: true });

  // Page 1: engine only, viz-global.js not loaded at all.
  const bareReady = await openReadyPage(browser, `http://127.0.0.1:${port}/index.html`, { polling: 200 });
  const bare = bareReady.page;
  const bareErrors = bareReady.errors;

  for (const [label, lines] of [['sequence', SEQUENCE], ['activity', ACTIVITY]]) {
    const r = await renderOn(bare, lines);
    check(`${label} diagram renders without viz-global.js`, !r.thrown && !!r.svg,
      r.thrown || 'no svg produced: ' + r.text.slice(0, 120));
  }

  for (const [label, lines] of [['class', CLASS], ['component', COMPONENT], ['composite state', STATE]]) {
    const r = await renderOn(bare, lines);
    const ok = !r.thrown && !!r.svg && !isErrorImage(r.svg) && r.shapes > 0;
    check(`${label} diagram without viz-global.js falls back to smetana`, ok,
      r.thrown || (!r.svg ? 'no svg: ' + r.text.slice(0, 120)
        : isErrorImage(r.svg) ? 'crash report instead of a fallback render'
        : 'svg but no drawn content (shapes=' + r.shapes + ')'));
  }
  check('no unhandled page errors on the viz-less page', bareErrors.length === 0, bareErrors.join(' | '));

  // Page 2: a Viz that is present but broken (instance() rejects).
  const brokenReady = await openReadyPage(browser, `http://127.0.0.1:${port}/index-broken.html`, { polling: 200 });
  const broken = brokenReady.page;
  const brokenErrors = brokenReady.errors;

  for (const [label, lines] of [['class', CLASS], ['component', COMPONENT], ['composite state', STATE]]) {
    const r = await renderOn(broken, lines);
    const output = r.svg || r.text;
    check(`${label} diagram with a broken Viz produces output`, !r.thrown && !!output && output.trim().length > 0,
      r.thrown || 'target element left empty');
    check(`${label} diagram output names Viz`, !!output && /viz/i.test(output),
      'output does not mention the failed engine: ' + String(output).slice(0, 160));
  }
  check('no unhandled page errors on the broken-viz page', brokenErrors.length === 0, brokenErrors.join(' | '));

  // Page 3 (control): viz-global.js loaded, the normal path must be unchanged.
  const ctrlReady = await openReadyPage(browser, `http://127.0.0.1:${port}/index-viz.html`, { polling: 200 });
  const ctrl = ctrlReady.page;
  const ctrlErrors = ctrlReady.errors;

  for (const [label, lines] of [['class', CLASS], ['component', COMPONENT]]) {
    const r = await renderOn(ctrl, lines);
    const ok = !r.thrown && !!r.svg && !/viz is not loaded/i.test(r.svg) && !isErrorImage(r.svg);
    check(`control: ${label} diagram still renders with viz-global.js`, ok,
      r.thrown || (r.svg ? 'crash text in output' : 'no svg produced: ' + r.text.slice(0, 120)));
  }
  check('no unhandled page errors on the control page', ctrlErrors.length === 0, ctrlErrors.join(' | '));

  await browser.close();
  server.close();
  const failures = getFailures();
  console.log(failures === 0 ? 'ALL CHECKS PASSED' : failures + ' CHECK(S) FAILED');
  process.exit(failures === 0 ? 0 : 1);
})().catch(e => { console.error(e); process.exit(2); });
