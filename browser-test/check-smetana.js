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
const path = require('path'), http = require('http'), fs = require('fs');
const pw = require(process.env.BENCH_PW || 'playwright');

let dir = null, file = 'plantuml.js';
for (let i = 2; i < process.argv.length; i++) {
  const m = process.argv[i].match(/^target=(.+)$/);
  if (!m) { console.error('bad arg: ' + process.argv[i]); process.exit(2); }
  dir = m[1];
  if (dir.endsWith('.js')) { file = path.basename(dir); dir = path.dirname(dir); }
}
if (!dir) { console.error('usage: node check-smetana.js target=<dir-or-js>'); process.exit(2); }
dir = path.resolve(dir);

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

const pageHtml = withViz => `<!doctype html><html><head>${hook}</head><body><div id="out"></div>
${withViz ? '<script src="/viz-global.js"></script>' : ''}
<script type="module">
import {render} from '/${file}';
window.__render=(lines,id)=>render(lines,id,{maxSvgSize:98304});
window.__ready=1;
</script></body></html>`;

const server = http.createServer((req, res) => {
  const u = decodeURIComponent(req.url.split('?')[0]);
  if (u === '/index.html') { res.setHeader('content-type', 'text/html'); return res.end(pageHtml(false)); }
  if (u === '/index-viz.html') { res.setHeader('content-type', 'text/html'); return res.end(pageHtml(true)); }
  const p = path.join(dir, u);
  if (p.startsWith(dir) && fs.existsSync(p) && fs.statSync(p).isFile()) {
    res.setHeader('content-type', 'application/javascript');
    res.setHeader('cache-control', 'no-store');
    return fs.createReadStream(p).pipe(res);
  }
  res.statusCode = 404; res.end();
});

// The PlantUML error image is green on black; a laid-out diagram never is.
const isErrorImage = svg => svg.includes('#33FF02') && svg.includes('#FF0000');

let failures = 0;
function check(label, ok, detail) {
  console.log(`${ok ? 'PASS' : 'FAIL'}  ${label}${ok || !detail ? '' : '\n        ' + detail}`);
  if (!ok) failures++;
}

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

async function renderOn(page, lines) {
  return page.evaluate(async ({ lines }) => {
    const out = document.getElementById('out');
    out.innerHTML = '';
    const w0 = window.__wasm;
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
    return {
      thrown, svg: svg ? svg.outerHTML : null, text: out.textContent || '',
      wasm: window.__wasm - w0,
      shapes: svg ? svg.querySelectorAll('path,polygon,line,rect,ellipse').length : 0,
      texts: svg ? svg.querySelectorAll('text').length : 0,
    };
  }, { lines });
}

(async () => {
  await new Promise(r => server.listen(0, '127.0.0.1', r));
  const port = server.address().port;
  const browser = await pw.chromium.launch({ headless: true });

  // Page 1: engine only, no viz-global.js. The pragma must be enough.
  const bare = await browser.newPage();
  const bareErrors = [];
  bare.on('pageerror', e => bareErrors.push(String(e.message).split('\n')[0]));
  await bare.goto(`http://127.0.0.1:${port}/index.html`, { waitUntil: 'load' });
  await bare.waitForFunction('window.__ready && window.__render', null, { timeout: 120000, polling: 200 });

  for (const [label, body] of FAMILIES) {
    const r = await renderOn(bare, diagram(body, true));
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
  const ctrl = await browser.newPage();
  const ctrlErrors = [];
  ctrl.on('pageerror', e => ctrlErrors.push(String(e.message).split('\n')[0]));
  await ctrl.goto(`http://127.0.0.1:${port}/index-viz.html`, { waitUntil: 'load' });
  await ctrl.waitForFunction('window.__ready && window.__render', null, { timeout: 120000, polling: 200 });

  const viaViz = await renderOn(ctrl, diagram(FAMILIES[0][1], false));
  check('control: class diagram without the pragma still uses the Graphviz bridge',
    !viaViz.thrown && !!viaViz.svg && !isErrorImage(viaViz.svg) && viaViz.wasm > 0,
    viaViz.thrown || (!viaViz.svg ? 'no svg: ' + viaViz.text.slice(0, 120)
      : viaViz.wasm === 0 ? 'render used no WebAssembly, default path changed' : 'error image'));

  const viaSmetana = await renderOn(ctrl, diagram(FAMILIES[0][1], true));
  check('control: class diagram with the pragma ignores viz-global.js even when loaded',
    !viaSmetana.thrown && !!viaSmetana.svg && !isErrorImage(viaSmetana.svg) && viaSmetana.wasm === 0,
    viaSmetana.thrown || (!viaSmetana.svg ? 'no svg: ' + viaSmetana.text.slice(0, 120)
      : viaSmetana.wasm !== 0 ? 'WebAssembly used (' + viaSmetana.wasm + ')' : 'error image'));

  check('no unhandled page errors on the control page', ctrlErrors.length === 0, ctrlErrors.join(' | '));

  await browser.close();
  server.close();
  console.log(failures === 0 ? 'ALL CHECKS PASSED' : failures + ' CHECK(S) FAILED');
  process.exit(failures === 0 ? 0 : 1);
})().catch(e => { console.error(e); process.exit(2); });
