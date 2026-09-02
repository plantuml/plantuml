'use strict';
// Functional check that the browser (TeaVM) engine falls back to the Smetana
// layout engine when viz-global.js is not loaded, without any pragma.
//
// usage: node check-viz-fallback.js target=<dir-or-js>
//   target   a directory containing plantuml.js (viz-global.js is served from
//            there too for the control page), or a path to the engine .js file
//
// Before the fallback, a Graphviz-family diagram on a page without
// viz-global.js could not render at all: the Viz global was simply missing.
// The JVM build already handles the equivalent situation (no dot binary) by
// falling back to Smetana, and this check pins the same behavior for the
// browser: with viz-global.js absent and no pragma, every Graphviz-family
// diagram type renders a real SVG through Smetana with zero WebAssembly use,
// and a one-time console note explains what happened. A diagram carrying
// '!pragma layout smetana' renders with no note at all: the pragma
// short-circuits the probe. On a control page WITH viz-global.js, the default
// path still uses the Graphviz bridge, so the fallback changes nothing for
// pages that load viz. A page with a partially loaded Viz (the global exists
// but instance() is not a function) falls back the same way as an absent one.
const path = require('path'), http = require('http'), fs = require('fs');
const pw = require(process.env.BENCH_PW || 'playwright');

let dir = null, file = 'plantuml.js';
for (let i = 2; i < process.argv.length; i++) {
  const m = process.argv[i].match(/^target=(.+)$/);
  if (!m) { console.error('bad arg: ' + process.argv[i]); process.exit(2); }
  dir = m[1];
  if (dir.endsWith('.js')) { file = path.basename(dir); dir = path.dirname(dir); }
}
if (!dir) { console.error('usage: node check-viz-fallback.js target=<dir-or-js>'); process.exit(2); }
dir = path.resolve(dir);

if (!fs.existsSync(path.join(dir, 'viz-global.js'))) {
  console.error('viz-global.js not found next to the engine in ' + dir + ' (needed for the control page)');
  process.exit(2);
}

const hook = `<script>
window.__wasm = 0;
['compile','instantiate','instantiateStreaming','compileStreaming'].forEach(function (k) {
  var o = WebAssembly[k];
  if (o) WebAssembly[k] = function () { window.__wasm++; return o.apply(WebAssembly, arguments); };
});
</script>`;

// A partially loaded viz: the Viz global exists but instance() is not a
// function, the shape a page gets when viz-global.js was interrupted or a
// different script claimed the name. The probe must treat this as missing.
const stub = `<script>window.Viz = {};</script>`;

const pageHtml = mode => `<!doctype html><html><head>${hook}</head><body><div id="out"></div>
${mode === 'viz' ? '<script src="/viz-global.js"></script>' : mode === 'stub' ? stub : ''}
<script type="module">
import {render} from '/${file}';
window.__render=(lines,id)=>render(lines,id,{maxSvgSize:98304});
window.__ready=1;
</script></body></html>`;

const server = http.createServer((req, res) => {
  const u = decodeURIComponent(req.url.split('?')[0]);
  if (u === '/index.html') { res.setHeader('content-type', 'text/html'); return res.end(pageHtml('bare')); }
  if (u === '/index-viz.html') { res.setHeader('content-type', 'text/html'); return res.end(pageHtml('viz')); }
  if (u === '/index-stub.html') { res.setHeader('content-type', 'text/html'); return res.end(pageHtml('stub')); }
  const p = path.join(dir, u);
  if (p.startsWith(dir) && fs.existsSync(p) && fs.statSync(p).isFile()) {
    res.setHeader('content-type', 'application/javascript');
    res.setHeader('cache-control', 'no-store');
    return fs.createReadStream(p).pipe(res);
  }
  res.statusCode = 404; res.end();
});

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
  ['usecase', ['actor User', 'User --> (Login)', 'User --> (Browse)', '(Browse) --> (Checkout)']],
  ['composite state', ['[*] --> Working', 'state Working {', '  [*] --> Fetching', '  Fetching --> Parsing : done', '}', 'Working --> [*] : shutdown']],
];
const SEQUENCE = ['Alice -> Bob: hello', 'Bob --> Alice: hi'];
const diagram = body => ['@startuml', ...body, '@enduml'];

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

  // Page 1: engine only, viz-global.js not loaded, no pragma anywhere.
  const bare = await browser.newPage();
  const bareErrors = [];
  const fallbackNotes = [];
  bare.on('pageerror', e => bareErrors.push(String(e.message).split('\n')[0]));
  bare.on('console', m => { if (m.type() === 'info' && /Smetana layout engine/.test(m.text())) fallbackNotes.push(m.text()); });
  await bare.goto(`http://127.0.0.1:${port}/index.html`, { waitUntil: 'load' });
  await bare.waitForFunction('window.__ready && window.__render', null, { timeout: 120000, polling: 200 });

  // First render on the page carries the pragma: the pragma must short-circuit
  // the probe, so it renders through Smetana with no fallback note at all.
  const prag = await renderOn(bare, ['@startuml', '!pragma layout smetana', ...FAMILIES[0][1], '@enduml']);
  await new Promise(r => setTimeout(r, 250)); // let any console event arrive before asserting absence
  check('pragma diagram on the viz-less page renders with no fallback note (pragma short-circuits the probe)',
    !prag.thrown && !!prag.svg && !isErrorImage(prag.svg) && prag.shapes > 0 && prag.wasm === 0 && fallbackNotes.length === 0,
    prag.thrown || (!prag.svg ? 'no svg: ' + prag.text.slice(0, 120)
      : fallbackNotes.length !== 0 ? 'fallback note logged for an explicit pragma render'
      : 'render failed (shapes=' + prag.shapes + ' wasm=' + prag.wasm + ')'));

  const seq = await renderOn(bare, diagram(SEQUENCE));
  check('sequence diagram renders without viz-global.js', !seq.thrown && !!seq.svg,
    seq.thrown || 'no svg produced: ' + seq.text.slice(0, 120));

  for (const [label, body] of FAMILIES) {
    const r = await renderOn(bare, diagram(body));
    const ok = !r.thrown && !!r.svg && !isErrorImage(r.svg) && r.shapes > 0 && r.texts > 0 && r.wasm === 0;
    check(`${label} diagram without viz-global.js and without pragma falls back to smetana`, ok,
      r.thrown || (!r.svg ? 'no svg: ' + r.text.slice(0, 120)
        : isErrorImage(r.svg) ? 'error image'
        : r.wasm !== 0 ? 'unexpected WebAssembly use (' + r.wasm + ')'
        : 'svg but no drawn content (shapes=' + r.shapes + ' texts=' + r.texts + ')'));
  }
  check('one console note explains the fallback', fallbackNotes.length === 1,
    fallbackNotes.length === 0 ? 'no console.info note seen' : fallbackNotes.length + ' notes seen (expected exactly one)');
  check('no unhandled page errors on the viz-less page', bareErrors.length === 0, bareErrors.join(' | '));

  // Page 2 (control): viz-global.js loaded. The default path must be unchanged.
  const ctrl = await browser.newPage();
  const ctrlErrors = [];
  const ctrlNotes = [];
  ctrl.on('pageerror', e => ctrlErrors.push(String(e.message).split('\n')[0]));
  ctrl.on('console', m => { if (m.type() === 'info' && /Smetana layout engine/.test(m.text())) ctrlNotes.push(m.text()); });
  await ctrl.goto(`http://127.0.0.1:${port}/index-viz.html`, { waitUntil: 'load' });
  await ctrl.waitForFunction('window.__ready && window.__render', null, { timeout: 120000, polling: 200 });

  const viaViz = await renderOn(ctrl, diagram(FAMILIES[0][1]));
  check('control: class diagram without the pragma still uses the Graphviz bridge',
    !viaViz.thrown && !!viaViz.svg && !isErrorImage(viaViz.svg) && viaViz.wasm > 0,
    viaViz.thrown || (!viaViz.svg ? 'no svg: ' + viaViz.text.slice(0, 120)
      : viaViz.wasm === 0 ? 'render used no WebAssembly, default path changed' : 'error image'));
  check('control: no fallback note when viz-global.js is loaded', ctrlNotes.length === 0,
    ctrlNotes.join(' | '));
  check('no unhandled page errors on the control page', ctrlErrors.length === 0, ctrlErrors.join(' | '));

  // Page 3: a partially loaded Viz (the global exists, instance() is not a
  // function). The probe must treat it as missing and fall back, with the note.
  const part = await browser.newPage();
  const partErrors = [];
  const partNotes = [];
  part.on('pageerror', e => partErrors.push(String(e.message).split('\n')[0]));
  part.on('console', m => { if (m.type() === 'info' && /Smetana layout engine/.test(m.text())) partNotes.push(m.text()); });
  await part.goto(`http://127.0.0.1:${port}/index-stub.html`, { waitUntil: 'load' });
  await part.waitForFunction('window.__ready && window.__render', null, { timeout: 120000, polling: 200 });

  const viaStub = await renderOn(part, diagram(FAMILIES[0][1]));
  await new Promise(r => setTimeout(r, 250));
  check('partially loaded Viz (no instance function) falls back to smetana with the note',
    !viaStub.thrown && !!viaStub.svg && !isErrorImage(viaStub.svg) && viaStub.shapes > 0 && viaStub.wasm === 0 && partNotes.length === 1,
    viaStub.thrown || (!viaStub.svg ? 'no svg: ' + viaStub.text.slice(0, 120)
      : viaStub.wasm !== 0 ? 'unexpected WebAssembly use (' + viaStub.wasm + ')'
      : partNotes.length !== 1 ? partNotes.length + ' fallback notes seen (expected exactly one)'
      : 'svg but no drawn content (shapes=' + viaStub.shapes + ')'));
  check('no unhandled page errors on the partial-viz page', partErrors.length === 0, partErrors.join(' | '));

  await browser.close();
  server.close();
  console.log(failures === 0 ? 'ALL CHECKS PASSED' : failures + ' CHECK(S) FAILED');
  process.exit(failures === 0 ? 0 : 1);
})().catch(e => { console.error(e); process.exit(2); });
