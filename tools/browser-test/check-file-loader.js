'use strict';
// Functional check for how the browser (TeaVM) engine reads the file of a
// local `!include`, through the host-provided PLANTUML_FILE_LOADER callback.
//
// usage: node check-file-loader.js target=<dir-or-js>
//   target   a directory containing plantuml.js, or a path to the engine .js itself
//
// The contract checked here:
//
// - NO loader set: behaviour is what it always was. The browser engine has no
//   file system, so a local include fails as a PlantUML error image, and
//   diagrams without one are untouched.
//
// - PLANTUML_FILE_LOADER set: the engine asks the host for the file with
//   (path, from, onOk, onErr) and waits for the answer, whether it comes
//   synchronously or later. `path` is the name written after the directive and
//   `from` is null for an include in the diagram itself, or the identifier the
//   host gave to the file that contains the include, so relative names resolve
//   against that file. The identifier is also what the include strategies
//   compare: a repeated `!include` is skipped, `!include_many` includes again,
//   and `!include_once` reports the second include as an error. A file that
//   holds a whole @startuml...@enduml diagram contributes the inside of that
//   diagram, as in the desktop build. Text arrives as a string, so nothing is
//   decoded along the way.
//
// - The loader fails (onErr), throws, or declines (returns false): the include
//   fails as a PlantUML error image, never as a hang or an unhandled page error,
//   and a failure's message reaches the console.
//
// - Standard-library includes (`!include <lib/...>`) never reach the file
//   loader.
//
// The files live in an in-memory map on the page, keyed by an absolute path
// the page's loader resolves itself, so the check pins the engine's side of the
// contract and needs no file system.
const path = require('path');
const { createCheckReporter, isErrorImage } = require('../lib/browser-check');
const { parseTargetArg } = require('../lib/browser-cli');
const { createMountedServer, startServer } = require('../lib/browser-http');
const { createModulePageHtml, loadPlaywright, makeRenderModuleBody, openReadyPage, renderOn } = require('../lib/browser-page');

const scriptName = path.basename(__filename, '.js');
const { dir, file } = parseTargetArg(process.argv, `node ${scriptName}.js target=<dir-or-js>`);
const pw = loadPlaywright();

// A sequence-diagram participant per file, so no layout engine (viz/smetana)
// is involved and the rendered name proves which file's content flowed in.
const FILES = {
  '/project/docs/greeting.puml': 'participant "Hello from greeting" as GREET',
  '/project/docs/common/nested.puml': '!include ../parts/leaf.puml\nparticipant "Hello from nested" as NESTED',
  '/project/docs/parts/leaf.puml': 'participant "Hello from leaf" as LEAF',
  '/project/docs/counter.puml': '!$n = $n + 1',
  '/project/docs/whole.puml': "' a comment before the diagram\n@startuml\nparticipant \"Hello from inside\" as INSIDE\n@enduml\nparticipant \"Hello from after\" as AFTER",
  '/project/docs/unicode.puml': 'participant "Hello 日本語 ünïcödé" as UNI',
};

// The page's loader: resolves `path` against the directory of `from` (or of
// the diagram, /project/docs), records every call in window.__loaderCalls, and
// answers according to the page's mode.
const loaderScript = mode => `<script>
window.__loaderCalls = [];
var FILES = ${JSON.stringify(FILES)};
function resolve(base, rel) {
  var parts = (rel.charAt(0) === '/' ? rel : base + '/' + rel).split('/');
  var out = [];
  for (var i = 0; i < parts.length; i++) {
    if (parts[i] === '' || parts[i] === '.') continue;
    if (parts[i] === '..') out.pop(); else out.push(parts[i]);
  }
  return '/' + out.join('/');
}
window.PLANTUML_FILE_LOADER = function (path, from, onOk, onErr) {
  window.__loaderCalls.push({ path: path, from: from });
  ${mode === 'decline' ? 'return false;' : ''}
  ${mode === 'throw' ? "throw new Error('loader exploded (simulated)');" : ''}
  var base = from === null ? '/project/docs' : from.replace(/\\/[^/]*$/, '');
  var id = resolve(base, path);
  if (!Object.prototype.hasOwnProperty.call(FILES, id)) { onErr('no such file: ' + id); return; }
  ${mode === 'sync' ? 'onOk(id, FILES[id]);' : 'setTimeout(function () { onOk(id, FILES[id]); }, 5);'}
};
</script>`;

const pageHtml = mode => createModulePageHtml({
  bodyHtml: mode === 'bare' ? '' : loaderScript(mode),
  modulePath: `/${file}`,
  moduleBody: makeRenderModuleBody({ maxSvgSize: 98304 }),
});

// The pages embed the file texts, non-ASCII included, so they declare their
// charset rather than leave the browser to guess it.
const HTML = 'text/html; charset=utf-8';
const server = createMountedServer({
  routes: {
    '/index.html': { contentType: HTML, body: pageHtml('bare') },
    '/index-hook.html': { contentType: HTML, body: pageHtml('async') },
    '/index-sync.html': { contentType: HTML, body: pageHtml('sync') },
    '/index-decline.html': { contentType: HTML, body: pageHtml('decline') },
    '/index-throw.html': { contentType: HTML, body: pageHtml('throw') },
  },
  mounts: [{ prefix: '/', dir }],
});

const { check, finish } = createCheckReporter();

const diagram = (...body) => ['@startuml', ...body, 'Alice -> Alice : ping', '@enduml'];
const SEQUENCE = ['@startuml', 'Alice -> Bob: hello', 'Bob --> Alice: hi', '@enduml'];
const counted = directive => ['@startuml', '!$n = 0', directive + ' counter.puml', directive + ' counter.puml',
  'participant "count $n" as COUNT', 'Alice -> Alice : ping', '@enduml'];

const renders = (r, ...texts) => !r.thrown && !!r.svg && !isErrorImage(r.svg) && texts.every(t => r.svg.includes(t));
const failsAsIncludeError = r => !r.thrown && !!r.svg && isErrorImage(r.svg);
const renderedAs = r => r.thrown || (r.svg ? (isErrorImage(r.svg) ? 'error image: ' + r.text.slice(0, 120)
  : 'svg without the expected content') : 'no svg: ' + r.text.slice(0, 120));
const calls = r => JSON.stringify(r.loaderCalls || []);

(async () => {
  const port = await startServer(server);
  const browser = await pw.chromium.launch({ headless: true });
  const opts = { includeLoaderCalls: true, maxTextLength: 200 };

  async function openPage(name) {
    const consoleLines = [];
    const ready = await openReadyPage(browser, `http://127.0.0.1:${port}/${name}`, {
      polling: 200,
      onConsole: msg => consoleLines.push(msg.text()),
    });
    ready.consoleLines = consoleLines;
    return ready;
  }

  // Page 1: no loader. A local include fails exactly as it always did.
  const bare = await openPage('index.html');
  let r = await renderOn(bare.page, SEQUENCE, opts);
  check('plain diagram renders with no loader set', renders(r), renderedAs(r));
  r = await renderOn(bare.page, diagram('!include greeting.puml'), opts);
  check('without a loader a local include fails as a PlantUML error image', failsAsIncludeError(r), renderedAs(r));
  check('no unhandled page errors on the bare page', bare.errors.length === 0, bare.errors.join(' | '));

  // Page 2: a loader that answers asynchronously.
  const hook = await openPage('index-hook.html');
  r = await renderOn(hook.page, SEQUENCE, opts);
  check('plain diagram renders with a loader set and the loader is not called', renders(r)
    && r.loaderCalls.length === 0, renderedAs(r) + '; calls: ' + calls(r));
  r = await renderOn(hook.page, diagram('!include greeting.puml'), opts);
  check('a file delivered asynchronously is included', renders(r, 'Hello from greeting'), renderedAs(r));
  check('the loader received the name as written and from = null', r.loaderCalls.length === 1
    && r.loaderCalls[0].path === 'greeting.puml' && r.loaderCalls[0].from === null, calls(r));
  await hook.page.evaluate(() => { window.__loaderCalls.length = 0; });
  r = await renderOn(hook.page, diagram('!include common/nested.puml'), opts);
  check('a nested include resolves against the file that contains it',
    renders(r, 'Hello from nested', 'Hello from leaf'), renderedAs(r));
  check('the nested include received the including file\'s identifier as from',
    r.loaderCalls.some(c => c.path === '../parts/leaf.puml' && c.from === '/project/docs/common/nested.puml'),
    calls(r));
  r = await renderOn(hook.page, counted('!include'), opts);
  check('a repeated !include of one file is skipped', renders(r, 'count 1'), renderedAs(r));
  r = await renderOn(hook.page, counted('!include_many'), opts);
  check('!include_many includes the file again', renders(r, 'count 2'), renderedAs(r));
  r = await renderOn(hook.page, counted('!include_once'), opts);
  check('!include_once reports the second include as an error', failsAsIncludeError(r)
    && r.svg.includes('already been included'), renderedAs(r));
  r = await renderOn(hook.page, diagram('!include whole.puml'), opts);
  check('a file holding a whole diagram contributes the inside of it',
    renders(r, 'Hello from inside') && !r.svg.includes('Hello from after'), renderedAs(r));
  r = await renderOn(hook.page, diagram('!include unicode.puml'), opts);
  check('non-ASCII text arrives unchanged', renders(r, '日本語 ünïcödé'),
    renderedAs(r));
  r = await renderOn(hook.page, diagram('!include missing.puml'), opts);
  check('a loader failure surfaces as a PlantUML error image, no hang', failsAsIncludeError(r), renderedAs(r));
  check('the failure message reaches the console',
    hook.consoleLines.some(l => l.includes('no such file: /project/docs/missing.puml')),
    hook.consoleLines.slice(-3).join(' | '));
  await hook.page.evaluate(() => { window.__loaderCalls.length = 0; });
  r = await renderOn(hook.page, diagram('!include <nosuchlib/greeting>'), opts);
  check('a standard-library include never reaches the file loader', r.loaderCalls.length === 0, calls(r));
  check('no unhandled page errors on the hook page', hook.errors.length === 0, hook.errors.join(' | '));

  // Page 3: a loader that answers synchronously, from inside the call.
  const sync = await openPage('index-sync.html');
  r = await renderOn(sync.page, diagram('!include common/nested.puml'), opts);
  check('a file delivered synchronously is included', renders(r, 'Hello from nested', 'Hello from leaf'),
    renderedAs(r));
  check('no unhandled page errors on the sync page', sync.errors.length === 0, sync.errors.join(' | '));

  // Page 4: a loader that declines everything.
  const decline = await openPage('index-decline.html');
  r = await renderOn(decline.page, diagram('!include greeting.puml'), opts);
  check('a declined file fails as if no loader were set', failsAsIncludeError(r) && r.loaderCalls.length === 1,
    renderedAs(r) + '; calls: ' + calls(r));
  check('no unhandled page errors on the decline page', decline.errors.length === 0, decline.errors.join(' | '));

  // Page 5: a loader that throws.
  const thrown = await openPage('index-throw.html');
  r = await renderOn(thrown.page, diagram('!include greeting.puml'), opts);
  check('a throwing loader fails the include as a PlantUML error image', failsAsIncludeError(r), renderedAs(r));
  check('no unhandled page errors on the throw page', thrown.errors.length === 0, thrown.errors.join(' | '));

  await browser.close();
  server.close();
  finish(scriptName, { uppercase: true });
})().catch(e => { console.error(e); process.exit(2); });
