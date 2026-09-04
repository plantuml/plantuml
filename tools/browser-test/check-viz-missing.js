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
const path = require('path'), http = require('http'), fs = require('fs');
const pw = require(process.env.BENCH_PW || 'playwright');

let dir = null, file = 'plantuml.js';
for (let i = 2; i < process.argv.length; i++) {
  const m = process.argv[i].match(/^target=(.+)$/);
  if (!m) { console.error('bad arg: ' + process.argv[i]); process.exit(2); }
  dir = m[1];
  if (dir.endsWith('.js')) { file = path.basename(dir); dir = path.dirname(dir); }
}
if (!dir) { console.error('usage: node check-viz-missing.js target=<dir-or-js>'); process.exit(2); }
dir = path.resolve(dir);

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
const pageHtml = mode => `<!doctype html><html><head></head><body><div id="out"></div>
${mode === 'viz' ? '<script src="/viz-global.js"></script>' : mode === 'broken' ? brokenStub : ''}
<script type="module">
import {render} from '/${file}';
window.__render=(lines,id)=>render(lines,id,{maxSvgSize:98304});
window.__ready=1;
</script></body></html>`;

const server = http.createServer((req, res) => {
  const u = decodeURIComponent(req.url.split('?')[0]);
  if (u === '/index.html') { res.setHeader('content-type', 'text/html'); return res.end(pageHtml('bare')); }
  if (u === '/index-broken.html') { res.setHeader('content-type', 'text/html'); return res.end(pageHtml('broken')); }
  if (u === '/index-viz.html') { res.setHeader('content-type', 'text/html'); return res.end(pageHtml('viz')); }
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
  await new Promise(r => server.listen(0, '127.0.0.1', r));
  const port = server.address().port;
  const browser = await pw.chromium.launch({ headless: true });

  // Page 1: engine only, viz-global.js not loaded at all.
  const bare = await browser.newPage();
  const bareErrors = [];
  bare.on('pageerror', e => bareErrors.push(String(e.message).split('\n')[0]));
  await bare.goto(`http://127.0.0.1:${port}/index.html`, { waitUntil: 'load' });
  await bare.waitForFunction('window.__ready && window.__render', null, { timeout: 120000, polling: 200 });

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
  const broken = await browser.newPage();
  const brokenErrors = [];
  broken.on('pageerror', e => brokenErrors.push(String(e.message).split('\n')[0]));
  await broken.goto(`http://127.0.0.1:${port}/index-broken.html`, { waitUntil: 'load' });
  await broken.waitForFunction('window.__ready && window.__render', null, { timeout: 120000, polling: 200 });

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
  const ctrl = await browser.newPage();
  const ctrlErrors = [];
  ctrl.on('pageerror', e => ctrlErrors.push(String(e.message).split('\n')[0]));
  await ctrl.goto(`http://127.0.0.1:${port}/index-viz.html`, { waitUntil: 'load' });
  await ctrl.waitForFunction('window.__ready && window.__render', null, { timeout: 120000, polling: 200 });

  for (const [label, lines] of [['class', CLASS], ['component', COMPONENT]]) {
    const r = await renderOn(ctrl, lines);
    const ok = !r.thrown && !!r.svg && !/viz is not loaded/i.test(r.svg) && !isErrorImage(r.svg);
    check(`control: ${label} diagram still renders with viz-global.js`, ok,
      r.thrown || (r.svg ? 'crash text in output' : 'no svg produced: ' + r.text.slice(0, 120)));
  }
  check('no unhandled page errors on the control page', ctrlErrors.length === 0, ctrlErrors.join(' | '));

  await browser.close();
  server.close();
  console.log(failures === 0 ? 'ALL CHECKS PASSED' : failures + ' CHECK(S) FAILED');
  process.exit(failures === 0 ? 0 : 1);
})().catch(e => { console.error(e); process.exit(2); });
