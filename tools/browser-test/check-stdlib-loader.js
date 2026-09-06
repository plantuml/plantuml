'use strict';
// Functional check for how the browser (TeaVM) engine loads stdlib bundles
// for `!include <lib/...>`, covering the two host-provided globals that
// customize it: PLANTUML_STDLIB_BASE (a URL prefix for the script tag) and
// PLANTUML_STDLIB_LOADER (a callback that delivers the bundle as data).
//
// usage: node check-stdlib-loader.js target=<dir-or-js>
//   target   a directory containing plantuml.js, or a path to the engine .js itself
//
// The contract checked here:
//
// - NEITHER global set: behaviour is byte-for-byte what it always was. A
//   bundle served next to the page loads through a relative script tag; a
//   missing bundle fails the include loudly instead of hanging; diagrams
//   without stdlib includes are untouched.
//
// - PLANTUML_STDLIB_BASE set: the script tag URL is prefixed, so the bundle
//   is fetched from the configured location and the page's own origin is
//   never asked for it. This is what lets a page that imports the engine
//   from a CDN point bundle loading at the project site (or its own assets)
//   with one line.
//
// - PLANTUML_STDLIB_LOADER set: the loader callback replaces the script tag
//   and delivers the bundle as data (here: fetched JSON), which is the only
//   viable path for hosts that cannot execute remote code (browser
//   extensions under MV3/AMO rules) or have no document (Web Workers). The
//   check asserts no .min.js is requested at all, that concurrent includes
//   of one library coalesce into a single loader call, that a library whose
//   info carries a `link` to another library resolves through two loader
//   calls, and that a loader failure surfaces as a visible include error
//   rather than a hang. Every lazily loaded support script (themes.js,
//   emoji.js, openiconic.js) comes through the same loader, so a hook that
//   only handles stdlib bundles must be able to opt out: returning false
//   (strictly) declines the URL and loading falls back to the script tag,
//   which the check pins with a hook that declines everything.
//
// The stdlib library used is synthetic (one participant definition), so the
// check needs no real stdlib bundle and pins the loading mechanics, not any
// particular library's content.
const path = require('path'), http = require('http'), fs = require('fs');
const pw = require(process.env.BENCH_PW || 'playwright');

let dir = null, file = 'plantuml.js';
for (let i = 2; i < process.argv.length; i++) {
  const m = process.argv[i].match(/^target=(.+)$/);
  if (!m) { console.error('bad arg: ' + process.argv[i]); process.exit(2); }
  dir = m[1];
  if (dir.endsWith('.js')) { file = path.basename(dir); dir = path.dirname(dir); }
}
if (!dir) { console.error('usage: node check-stdlib-loader.js target=<dir-or-js>'); process.exit(2); }
dir = path.resolve(dir);

// The synthetic libraries: a sequence-diagram participant each, so no layout
// engine (viz/smetana) is involved and the rendered name proves which bundle's
// content flowed through the include. Each library exists ONLY at the location
// its scenario is supposed to use (fakelib at the page root, baselib under
// /cdn/, hooklib as JSON), so a wrong loading path cannot render by accident.
const greetingLine = lib => 'participant "Hello from ' + lib + '" as FAKEHELLO';
const bundleScript = lib => `(function(){
window.PLANTUML_STDLIB=window.PLANTUML_STDLIB||{};
window.PLANTUML_STDLIB.${lib}=window.PLANTUML_STDLIB.${lib}||{};
window.PLANTUML_STDLIB.${lib}["greeting"]=[${JSON.stringify(greetingLine(lib))}];
window.PLANTUML_STDLIB_INFO=window.PLANTUML_STDLIB_INFO||{};
window.PLANTUML_STDLIB_INFO.${lib}={name:${JSON.stringify(lib)}};
})();`;
const hooklibJson = JSON.stringify({ info: { name: 'hooklib' }, files: { greeting: [greetingLine('hooklib')] }, json: {} });
const linklibJson = JSON.stringify({ info: { name: 'linklib', link: 'hooklib' }, files: {}, json: {} });

// One hook body shared by the hook pages: fetch /json/<lib>.json, populate
// the three globals, count invocations in window.__loaderCalls.
const hookScript = failLib => `<script>
window.__loaderCalls = [];
window.PLANTUML_STDLIB_LOADER = function (url, onOk, onErr) {
  window.__loaderCalls.push(url);
  var lib = url.replace(/\\.min\\.js$/, '');
  ${failLib ? `if (lib === ${JSON.stringify(failLib)}) { onErr('loader refused ' + lib + ' (simulated)'); return; }` : ''}
  fetch('/json/' + lib + '.json')
    .then(function (r) { if (!r.ok) throw new Error('HTTP ' + r.status); return r.json(); })
    .then(function (d) {
      window.PLANTUML_STDLIB = window.PLANTUML_STDLIB || {};
      window.PLANTUML_STDLIB[lib] = d.files;
      window.PLANTUML_STDLIB_JSON = window.PLANTUML_STDLIB_JSON || {};
      window.PLANTUML_STDLIB_JSON[lib] = d.json || {};
      window.PLANTUML_STDLIB_INFO = window.PLANTUML_STDLIB_INFO || {};
      window.PLANTUML_STDLIB_INFO[lib] = d.info;
      onOk();
    })
    .catch(function (e) { onErr(String(e)); });
};
</script>`;

const declineScript = `<script>
window.__loaderCalls = [];
window.PLANTUML_STDLIB_LOADER = function (url) { window.__loaderCalls.push(url); return false; };
</script>`;

const pageHtml = mode => `<!doctype html><html><head></head><body><div id="out"></div>
${mode === 'base' ? `<script>window.PLANTUML_STDLIB_BASE = '/cdn/';</script>` : ''}
${mode === 'hook' ? hookScript(null) : ''}
${mode === 'hookfail' ? hookScript('fakelib') : ''}
${mode === 'decline' ? declineScript : ''}
<script type="module">
import {render} from '/${file}';
window.__render=(lines,id)=>render(lines,id,{maxSvgSize:98304});
window.__ready=1;
</script></body></html>`;

// The server records every bundle-ish request so the checks can assert what
// was and was not fetched. Bundles exist ONLY at the paths each scenario is
// supposed to use: /fakelib.min.js for the relative page, /cdn/fakelib.min.js
// for the base page, /json/*.json for the hook pages.
const requested = [];
const server = http.createServer((req, res) => {
  const u = decodeURIComponent(req.url.split('?')[0]);
  if (/\.min\.js$|\/json\//.test(u)) requested.push(u);
  if (u === '/index.html') { res.setHeader('content-type', 'text/html'); return res.end(pageHtml('bare')); }
  if (u === '/index-base.html') { res.setHeader('content-type', 'text/html'); return res.end(pageHtml('base')); }
  if (u === '/index-hook.html') { res.setHeader('content-type', 'text/html'); return res.end(pageHtml('hook')); }
  if (u === '/index-hookfail.html') { res.setHeader('content-type', 'text/html'); return res.end(pageHtml('hookfail')); }
  if (u === '/index-decline.html') { res.setHeader('content-type', 'text/html'); return res.end(pageHtml('decline')); }
  if (u === '/fakelib.min.js') { res.setHeader('content-type', 'application/javascript'); return res.end(bundleScript('fakelib')); }
  if (u === '/cdn/baselib.min.js') { res.setHeader('content-type', 'application/javascript'); return res.end(bundleScript('baselib')); }
  if (u === '/json/hooklib.json') { res.setHeader('content-type', 'application/json'); return res.end(hooklibJson); }
  if (u === '/json/linklib.json') { res.setHeader('content-type', 'application/json'); return res.end(linklibJson); }
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

const includeOf = lib => ['@startuml', '!include <' + lib + '/greeting>', 'FAKEHELLO -> FAKEHELLO : ping', '@enduml'];
const SEQUENCE = ['@startuml', 'Alice -> Bob: hello', 'Bob --> Alice: hi', '@enduml'];

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
      loaderCalls: window.__loaderCalls ? window.__loaderCalls.slice() : null };
  }, { lines });
}

const rendersGreeting = (r, lib) => !r.thrown && !!r.svg && !isErrorImage(r.svg)
  && r.svg.includes('Hello from ' + lib);
const failsVisibly = r => !r.thrown && (!!r.text.trim() || (!!r.svg && isErrorImage(r.svg)));

(async () => {
  await new Promise(r => server.listen(0, '127.0.0.1', r));
  const port = server.address().port;
  const browser = await pw.chromium.launch({ headless: true });

  async function openPage(name) {
    const page = await browser.newPage();
    const errors = [];
    page.on('pageerror', e => errors.push(String(e.message).split('\n')[0]));
    await page.goto(`http://127.0.0.1:${port}/${name}`, { waitUntil: 'load' });
    await page.waitForFunction('window.__ready && window.__render', null, { timeout: 120000, polling: 200 });
    return { page, errors };
  }

  // Page 1: neither global set. Relative loading and the failure mode are
  // exactly what they always were.
  const bare = await openPage('index.html');
  let r = await renderOn(bare.page, SEQUENCE);
  check('plain diagram renders with neither global set', !r.thrown && !!r.svg && !isErrorImage(r.svg),
    r.thrown || 'no svg: ' + r.text.slice(0, 120));
  r = await renderOn(bare.page, includeOf('fakelib'));
  check('relative bundle next to the page still loads', rendersGreeting(r, 'fakelib'),
    r.thrown || (r.svg ? 'include content missing from svg' : 'no svg: ' + r.text.slice(0, 120)));
  check('relative bundle was fetched from the page origin', requested.includes('/fakelib.min.js'),
    'requests seen: ' + requested.join(', '));
  r = await renderOn(bare.page, includeOf('nosuchlib'));
  check('missing bundle fails the include visibly, no hang', failsVisibly(r),
    r.thrown || 'no visible failure output');
  check('no unhandled page errors on the bare page', bare.errors.length === 0, bare.errors.join(' | '));

  // Page 2: PLANTUML_STDLIB_BASE points at /cdn/. The page origin is never
  // asked for the bundle.
  requested.length = 0;
  const base = await openPage('index-base.html');
  r = await renderOn(base.page, includeOf('baselib'));
  check('PLANTUML_STDLIB_BASE loads the bundle from the prefix', rendersGreeting(r, 'baselib'),
    r.thrown || (r.svg ? 'include content missing from svg' : 'no svg: ' + r.text.slice(0, 120)));
  check('base page fetched /cdn/baselib.min.js and nothing from the root',
    requested.includes('/cdn/baselib.min.js') && !requested.includes('/baselib.min.js'),
    'requests seen: ' + requested.join(', '));
  check('no unhandled page errors on the base page', base.errors.length === 0, base.errors.join(' | '));

  // Page 3: PLANTUML_STDLIB_LOADER delivers the bundle as fetched JSON.
  requested.length = 0;
  const hook = await openPage('index-hook.html');
  r = await renderOn(hook.page, includeOf('hooklib'));
  check('PLANTUML_STDLIB_LOADER delivers the bundle as data', rendersGreeting(r, 'hooklib'),
    r.thrown || (r.svg ? 'include content missing from svg' : 'no svg: ' + r.text.slice(0, 120)));
  check('hook page fetched only JSON, no .min.js anywhere',
    requested.some(u => u === '/json/hooklib.json') && !requested.some(u => u.endsWith('.min.js')),
    'requests seen: ' + requested.join(', '));
  r = await renderOn(hook.page, includeOf('hooklib'));
  check('second render coalesces: loader called once for hooklib',
    r.loaderCalls && r.loaderCalls.filter(u => u === 'hooklib.min.js').length === 1,
    'loader calls: ' + (r.loaderCalls || []).join(', '));
  r = await renderOn(hook.page, includeOf('linklib'));
  check('a library with info.link resolves through two loader calls', rendersGreeting(r, 'hooklib')
    && r.loaderCalls.includes('linklib.min.js'),
    r.thrown || (rendersGreeting(r, 'hooklib') ? 'loader calls: ' + (r.loaderCalls || []).join(', ')
      : (r.svg ? 'include content missing from svg' : 'no svg: ' + r.text.slice(0, 120))));
  check('no unhandled page errors on the hook page', hook.errors.length === 0, hook.errors.join(' | '));

  // Page 4: the loader refuses the library. The include must fail visibly.
  const hookfail = await openPage('index-hookfail.html');
  r = await renderOn(hookfail.page, includeOf('fakelib'));
  check('loader failure surfaces as a visible include error, no hang', failsVisibly(r),
    r.thrown || 'no visible failure output');
  check('no unhandled page errors on the hook-failure page', hookfail.errors.length === 0,
    hookfail.errors.join(' | '));

  // Page 5: the loader declines every URL (returns false). Loading must fall
  // back to the script tag, so a hook that only handles stdlib bundles does
  // not break themes.js, emoji.js and friends.
  requested.length = 0;
  const decline = await openPage('index-decline.html');
  r = await renderOn(decline.page, includeOf('fakelib'));
  check('a declining hook falls back to the script tag', rendersGreeting(r, 'fakelib'),
    r.thrown || (r.svg ? 'include content missing from svg' : 'no svg: ' + r.text.slice(0, 120)));
  check('the declining hook was consulted before the fallback',
    r.loaderCalls && r.loaderCalls.includes('fakelib.min.js') && requested.includes('/fakelib.min.js'),
    'loader calls: ' + (r.loaderCalls || []).join(', ') + '; requests: ' + requested.join(', '));
  check('no unhandled page errors on the decline page', decline.errors.length === 0,
    decline.errors.join(' | '));

  await browser.close();
  server.close();
  console.log(failures === 0 ? 'ALL CHECKS PASSED' : failures + ' CHECK(S) FAILED');
  process.exit(failures === 0 ? 0 : 1);
})().catch(e => { console.error(e); process.exit(2); });
