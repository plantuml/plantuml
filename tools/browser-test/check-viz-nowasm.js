'use strict';
// Functional check for @plantuml/core-nowasm, the npm package with no
// WebAssembly at all: its viz-global-nowasm.js is the Viz.js of
// @plantuml/core whose Graphviz module was translated from WebAssembly to
// JavaScript (tools/viz-nowasm). Pages are served with a strict Content
// Security Policy.
//
// usage: node check-viz-nowasm.js target=<dir-or-js> [nowasm=<dir>]
//   target   the @plantuml/core package (a directory containing plantuml.js
//            and viz-global.js, or the path of the engine .js itself): the
//            reference
//   nowasm   the @plantuml/core-nowasm package; by default the directory
//            next to target's, with "-nowasm" appended to its name
//            (build/npm-plantuml -> build/npm-plantuml-nowasm)
//
// The CSP is `script-src 'self'`: no 'unsafe-eval', no 'wasm-unsafe-eval',
// no inline script (so every script of the test pages is a file).
//
// - The packages: the nowasm one has no .wasm file and no WebAssembly call,
//   and no viz-global.js; @plantuml/core has no viz-global-nowasm.js; both
//   ship the same plantuml.js; viz-global-nowasm.js was generated from the
//   viz-global.js of @plantuml/core (not a stale one).
// - Control: with @plantuml/core, that CSP must block WebAssembly (a
//   script-src violation on 'wasm-eval') and Graphviz diagrams must report
//   Viz. This proves the policy is really enforced, so that the next point
//   is not vacuous.
// - With @plantuml/core-nowasm under the same CSP, every Graphviz diagram
//   type renders, with no CSP violation, no console error and no page error,
//   both into the DOM and to a string, and the SVG is the very same as with
//   @plantuml/core on a page without CSP.
// - With @plantuml/core-nowasm on a page where WebAssembly does not exist at
//   all (as in a jitless engine), the SVG is the same too.
const crypto = require('crypto');
const fs = require('fs');
const path = require('path');
const { createCheckReporter, isErrorImage } = require('../lib/browser-check');
const { resolveEngineSpec } = require('../lib/browser-cli');
const { createMountedServer, startServer } = require('../lib/browser-http');
const { loadPlaywright } = require('../lib/browser-page');

const scriptName = path.basename(__filename, '.js');
const usage = `usage: node ${scriptName}.js target=<dir-or-js> [nowasm=<dir>]`;
const args = {};
for (const arg of process.argv.slice(2)) {
  const m = /^(target|nowasm)=(.+)$/.exec(arg);
  if (!m) {
    console.error('bad arg: ' + arg + '\n' + usage);
    process.exit(2);
  }
  args[m[1]] = m[2];
}
if (!args.target) {
  console.error(usage);
  process.exit(2);
}
const { dir, file } = resolveEngineSpec(args.target);
const nowasmDir = path.resolve(args.nowasm || dir + '-nowasm');
const pw = loadPlaywright();

for (const [d, name] of [[dir, file], [dir, 'viz-global.js'], [nowasmDir, 'plantuml.js'], [nowasmDir, 'viz-global-nowasm.js']])
  if (!fs.existsSync(path.join(d, name))) {
    console.error(name + ' not found in ' + d + '\n(build both packages: gradlew :plantuml-mit:npmPackage :plantuml-mit:npmPackageNowasm -Pci)');
    process.exit(2);
  }

const CSP = "script-src 'self'; object-src 'none'; base-uri 'none'";

// Loaded first: records CSP violations, and, on the no-WebAssembly page,
// removes WebAssembly before anything else runs.
const probeJs = `window.__violations = [];
document.addEventListener('securitypolicyviolation', function (e) {
  window.__violations.push(e.effectiveDirective + ' ' + e.blockedURI);
});`;
const noWasmJs = 'delete window.WebAssembly;';
const mainJs = engine => `import { render, renderToString } from '${engine}';
window.__render = function (lines) { render(lines, 'out', { maxSvgSize: 0 }); };
window.__toString = function (lines) {
  return new Promise(function (resolve) {
    renderToString(lines, function (svg) { resolve({ svg: svg }); },
      function (err) { resolve({ err: String(err) }); }, { maxSvgSize: 0 });
  });
};
window.__ready = 1;`;

// prefix: /core (the @plantuml/core package) or /nowasm.
const pageHtml = (prefix, viz, noWasm) => `<!doctype html><html><head><meta charset="utf-8">
<script src="/probe.js"></script>${noWasm ? '<script src="/nowasm-env.js"></script>' : ''}
</head><body><div id="out"></div>
<script src="${prefix}/${viz}"></script>
<script type="module" src="${prefix}/check-main.js"></script></body></html>`;

function htmlRoute(body, csp) {
  return (req, res) => {
    res.setHeader('content-type', 'text/html');
    if (csp)
      res.setHeader('content-security-policy', csp);
    res.end(body);
  };
}

const server = createMountedServer({
  routes: {
    '/probe.js': { contentType: 'application/javascript', body: probeJs },
    '/nowasm-env.js': { contentType: 'application/javascript', body: noWasmJs },
    '/core/check-main.js': { contentType: 'application/javascript', body: mainJs('/core/' + file) },
    '/nowasm/check-main.js': { contentType: 'application/javascript', body: mainJs('/nowasm/plantuml.js') },
    '/reference.html': htmlRoute(pageHtml('/core', 'viz-global.js', false), null),
    '/csp-wasm.html': htmlRoute(pageHtml('/core', 'viz-global.js', false), CSP),
    '/csp-nowasm.html': htmlRoute(pageHtml('/nowasm', 'viz-global-nowasm.js', false), CSP),
    '/no-webassembly.html': htmlRoute(pageHtml('/nowasm', 'viz-global-nowasm.js', true), null),
  },
  mounts: [{ prefix: '/core', dir }, { prefix: '/nowasm', dir: nowasmDir }],
});

const { check, finish } = createCheckReporter();

const DIAGRAMS = [
  ['class', ['@startuml', 'class Car {', '  +drive(): void', '}', 'interface Vehicle', 'Vehicle <|.. Car', 'Car *-- "4" Wheel', '@enduml']],
  ['component', ['@startuml', 'package Front {', '  [Web UI]', '}', '[Web UI] --> [API Gateway]', '[API Gateway] --> [Orders]', 'database DB', '[Orders] --> DB', '@enduml']],
  ['state', ['@startuml', '[*] --> Working', 'state Working {', '  [*] --> Fetching', '  Fetching --> Parsing : done', '}', 'Working --> [*]', '@enduml']],
  ['usecase', ['@startuml', 'actor User', 'User --> (Log in)', '(Log in) .> (Check password) : include', '@enduml']],
  ['object', ['@startuml', 'object order', 'order : id = 42', 'object customer', 'order --> customer', '@enduml']],
  ['deployment', ['@startuml', 'node Server {', '  artifact app.war', '}', 'cloud Internet', 'Internet --> Server', '@enduml']],
];

// A real drawing: neither the error image nor the crash report that names Viz.
function isDrawing(svg) {
  return !!svg && !isErrorImage(svg) && svg.indexOf('An error has occurred') < 0;
}

function filesIn(d) {
  let out = [];
  for (const e of fs.readdirSync(d, { withFileTypes: true })) {
    const p = path.join(d, e.name);
    out = e.isDirectory() ? out.concat(filesIn(p)) : out.concat([p]);
  }
  return out;
}

function sha256(text) {
  return crypto.createHash('sha256').update(text).digest('hex');
}

async function open(browser, url) {
  const page = await browser.newPage();
  const problems = [];
  page.on('pageerror', err => problems.push('page error: ' + String(err && err.message || err).split('\n')[0]));
  page.on('console', msg => {
    if (msg.type() === 'error')
      problems.push('console error: ' + msg.text().slice(0, 200));
  });
  await page.goto(url);
  await page.waitForFunction('window.__ready', null, { timeout: 120000 });
  return { page, problems };
}

async function toString(page, lines) {
  return Promise.race([
    page.evaluate(l => window.__toString(l), lines),
    new Promise(resolve => setTimeout(() => resolve({ err: 'timed out' }), 60000)),
  ]);
}

// render() into the DOM: waits for an svg that is not the transient one drawn
// while Viz is loading.
async function intoDom(page, lines) {
  await page.evaluate(l => window.__render(l), lines);
  await page.waitForFunction(() => {
    const svg = document.querySelector('#out svg');
    return svg && !/viz is loading/i.test(svg.textContent);
  }, null, { timeout: 60000 }).catch(() => {});
  return page.evaluate(() => {
    const svg = document.querySelector('#out svg');
    return svg ? svg.outerHTML : document.getElementById('out').textContent;
  });
}

(async () => {
  // The packages themselves.
  const wasmCall = /WebAssembly\s*\.\s*(instantiate|instantiateStreaming|compile|compileStreaming|Module|Instance)\b/;
  const nowasmFiles = filesIn(nowasmDir).map(f => path.relative(nowasmDir, f));
  const wasmFiles = nowasmFiles.filter(f => f.endsWith('.wasm'));
  check('nowasm package: no .wasm file', wasmFiles.length === 0, wasmFiles.join(', '));
  const wasmUsers = nowasmFiles.filter(f => f.endsWith('.js') && wasmCall.test(fs.readFileSync(path.join(nowasmDir, f), 'utf8')));
  check('nowasm package: no script calls WebAssembly', wasmUsers.length === 0, wasmUsers.join(', '));
  check('nowasm package: no `viz-global.js`', nowasmFiles.every(f => path.basename(f) !== 'viz-global.js'));
  const pages = nowasmFiles.filter(f => f.endsWith('.html') && /viz-global\.js/.test(fs.readFileSync(path.join(nowasmDir, f), 'utf8')));
  check('nowasm package: no page loads `viz-global.js`', pages.length === 0, pages.join(', '));
  check('core package: no `viz-global-nowasm.js`', !fs.existsSync(path.join(dir, 'viz-global-nowasm.js')));
  check('both packages ship the same engine',
    sha256(fs.readFileSync(path.join(dir, file))) === sha256(fs.readFileSync(path.join(nowasmDir, 'plantuml.js'))));
  // Stale file: viz-global.js was replaced without running tools/viz-nowasm again.
  const vizSha = sha256(fs.readFileSync(path.join(dir, 'viz-global.js'), 'utf8').replace(/\r\n/g, '\n'));
  const recorded = (/from viz-global\.js sha256 ([0-9a-f]{64})/.exec(fs.readFileSync(path.join(nowasmDir, 'viz-global-nowasm.js'), 'utf8').slice(0, 4096)) || [])[1];
  check('`viz-global-nowasm.js` was generated from the `viz-global.js` of the core package', recorded === vizSha,
    'recorded ' + recorded + ', actual ' + vizSha + ': run tools/viz-nowasm/build.js again');

  const port = await startServer(server);
  const base = `http://127.0.0.1:${port}`;
  const browser = await pw.chromium.launch({ headless: true });

  // Reference: @plantuml/core, no CSP.
  const ref = await open(browser, base + '/reference.html');
  const expected = {};
  for (const [label, lines] of DIAGRAMS) {
    const r = await toString(ref.page, lines);
    expected[label] = r.svg;
    check(`reference: ${label} diagram renders with \`viz-global.js\``, isDrawing(r.svg), r.err || 'no drawing');
  }

  // Control: @plantuml/core under the strict CSP.
  const ctrl = await open(browser, base + '/csp-wasm.html');
  const ctrlOut = await toString(ctrl.page, DIAGRAMS[0][1]);
  const ctrlText = ctrlOut.svg || ctrlOut.err || '';
  const ctrlViolations = await ctrl.page.evaluate(() => window.__violations);
  check('control: the CSP blocks the WebAssembly of `viz-global.js`',
    ctrlViolations.some(v => /^script-src.* wasm-eval$/.test(v)), 'violations: ' + JSON.stringify(ctrlViolations));
  check('control: the class diagram then reports Viz', /viz/i.test(ctrlText) && ctrlOut.svg !== expected.class,
    ctrlText.slice(0, 160));

  // @plantuml/core-nowasm under the strict CSP.
  const csp = await open(browser, base + '/csp-nowasm.html');
  for (const [label, lines] of DIAGRAMS) {
    const r = await toString(csp.page, lines);
    check(`CSP: ${label} diagram renders with \`viz-global-nowasm.js\``, isDrawing(r.svg), r.err || 'no drawing');
    check(`CSP: ${label} SVG is identical to \`viz-global.js\` without CSP`, !!r.svg && r.svg === expected[label],
      r.svg ? 'svg differs (' + r.svg.length + ' vs ' + (expected[label] || '').length + ' chars)' : 'no svg');
  }
  const dom = await intoDom(csp.page, DIAGRAMS[0][1]);
  check('CSP: `render()` draws the class diagram into the page', /^<svg/.test(dom) && isDrawing(dom), String(dom).slice(0, 160));
  const violations = await csp.page.evaluate(() => window.__violations);
  check('CSP: no violation with `viz-global-nowasm.js`', violations.length === 0, JSON.stringify(violations));
  check('CSP: no console or page error with `viz-global-nowasm.js`', csp.problems.length === 0, csp.problems.join(' | '));

  // @plantuml/core-nowasm where WebAssembly does not exist.
  const bare = await open(browser, base + '/no-webassembly.html');
  check('no WebAssembly: the global is really gone', await bare.page.evaluate(() => typeof WebAssembly === 'undefined'));
  for (const [label, lines] of DIAGRAMS.slice(0, 3)) {
    const r = await toString(bare.page, lines);
    check(`no WebAssembly: ${label} SVG is identical to \`viz-global.js\``, !!r.svg && r.svg === expected[label], r.err || 'svg differs');
  }
  check('no WebAssembly: no console or page error', bare.problems.length === 0, bare.problems.join(' | '));

  await browser.close();
  server.close();
  finish(scriptName);
})().catch(err => {
  console.error(err);
  process.exit(1);
});
