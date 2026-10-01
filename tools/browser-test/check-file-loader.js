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
//   (path, from, onOk, onErr) and waits for the outcome, whether it comes
//   synchronously, later, or from an async function. `path` is the local name
//   the directive asks for, after variables are expanded and a `file!tag`
//   selector is split off (the engine resolves nothing); `from` is null for an
//   include in the diagram itself, or the identifier the host gave to the
//   delivered file that contains the include, so relative names resolve
//   against that file. The identifier, compared as it is, is also what the
//   include strategies use: a repeated `!include` is skipped, `!include_many`
//   includes again, and `!include_once` reports the second include as an
//   error. A file that holds a whole @startuml...@enduml diagram contributes
//   the inside of that diagram, as in the desktop build. Text arrives as a
//   string, so nothing is decoded along the way.
//
// - The first outcome wins: a second ok, an err after ok, or an ok after a
//   false decline changes nothing. Only a non-empty string id with a string
//   text is a delivery. The rejection of a promise the loader returns fails
//   the include; a fulfilled value delivers nothing.
//
// - The loader fails (onErr), throws, rejects or declines (returns false): the
//   include fails as a PlantUML error image, never as a hang or an unhandled
//   page error, and a failure's message reaches the console. The engine has
//   no timeout, so a loader that never settles leaves the rendering waiting;
//   that case is documented, not exercised here.
//
// - The loader is only asked for local names: standard-library includes, URL
//   includes and `!includesub` keep their existing routes, and a relative
//   include written in a standard-library file or in a bundled theme never
//   reaches it (those files are the engine's own), whether the diagram or a
//   delivered file brought the library or theme in; the delivered file is
//   `from` again afterwards.
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
  // What the host would deliver if a relative include written in a library or
  // a theme wrongly reached it: a name that must never appear in a drawing.
  '/project/docs/leaf.puml': 'participant "WRONG_HOST" as WRONG',
  '/project/docs/via-lib.puml': '!include <guardlib/relative>',
  // Delivered files that bring a benign library or theme in, then include a
  // local file: that local include must still come from the delivered file.
  '/project/docs/via-lib-ok.puml': '!include <guardlib/greeting>\n!include greeting.puml',
  '/project/docs/via-theme-ok.puml': '!theme guardtheme-ok\n!include greeting.puml',
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
  ${mode === 'reject' ? "return (async function () { await new Promise(function (r) { setTimeout(r, 5); }); throw new Error('rejected after await (simulated)'); })();" : ''}
  var base = from === null ? '/project/docs' : from.replace(/\\/[^/]*$/, '');
  var id = resolve(base, path);
  if (!Object.prototype.hasOwnProperty.call(FILES, id)) { onErr('no such file: ' + id); return; }
  var twist = window.__twist; window.__twist = null;
  if (twist === 'ok-twice') { onOk(id, FILES[id]); onOk(id, 'participant "SECOND_CALL" as SC'); return; }
  if (twist === 'ok-then-err') { onOk(id, FILES[id]); onErr('error after ok (simulated)'); return; }
  if (twist === 'empty-id') { onOk('', FILES[id]); return; }
  if (twist === 'number-text') { onOk(id, 42); return; }
  if (twist === 'false-late') { setTimeout(function () { onOk(id, 'participant "TOO_LATE" as LATE'); }, 5); return false; }
  ${mode === 'sync' ? 'onOk(id, FILES[id]);' : 'setTimeout(function () { onOk(id, FILES[id]); }, 5);'}
};
// A synthetic standard library and a synthetic bundled theme, registered the
// way a host may, each with a relative include that must never reach the loader.
window.PLANTUML_STDLIB = window.PLANTUML_STDLIB || {};
window.PLANTUML_STDLIB.guardlib = { greeting: ['participant "Hello from guardlib" as LIB'], relative: ['!include leaf.puml'] };
window.PLANTUML_STDLIB_INFO = window.PLANTUML_STDLIB_INFO || {};
window.PLANTUML_STDLIB_INFO.guardlib = { name: 'guardlib' };
window.__pl_script_state = window.__pl_script_state || Object.create(null);
window.__pl_script_state['guardlib.min.js'] = { state: 'loaded' };
globalThis.PLANTUML_THEMES = { guardtheme: '!include leaf.puml', 'guardtheme-ok': 'skinparam backgroundColor #ABCDEF' };
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
    '/index-reject.html': { contentType: HTML, body: pageHtml('reject') },
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
  check('the loader received the name the directive asks for and from = null', r.loaderCalls.length === 1
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
  // The first outcome wins, and only a non-empty string id with a string text
  // is a delivery.
  for (const [twist, label, expected] of [
    ['ok-twice', 'a second ok is ignored', r => renders(r, 'Hello from greeting') && !r.svg.includes('SECOND_CALL')],
    ['ok-then-err', 'an err after ok is ignored', r => renders(r, 'Hello from greeting')],
    ['empty-id', 'an empty id is not a delivery and fails the include', failsAsIncludeError],
    ['number-text', 'a text that is not a string is not a delivery and fails the include', failsAsIncludeError],
    ['false-late', 'an ok after a false decline is ignored', r => failsAsIncludeError(r) && !r.svg.includes('TOO_LATE')],
  ]) {
    await hook.page.evaluate(t => { window.__twist = t; }, twist);
    r = await renderOn(hook.page, diagram('!include greeting.puml'), opts);
    check(label, expected(r), renderedAs(r));
  }
  // Lets the late callback above fire before the page's errors are inspected.
  await hook.page.waitForTimeout(50);
  await hook.page.evaluate(() => { window.__loaderCalls.length = 0; });
  r = await renderOn(hook.page, diagram('!include greeting.puml!1'), opts);
  check('a diagram selector is split off the name and not applied, as for a local file in the Java build',
    renders(r, 'Hello from greeting') && r.loaderCalls.length === 1 && r.loaderCalls[0].path === 'greeting.puml',
    renderedAs(r) + '; calls: ' + calls(r));
  await hook.page.evaluate(() => { window.__loaderCalls.length = 0; });
  r = await renderOn(hook.page, diagram('!include <nosuchlib/greeting>'), opts);
  check('a standard-library include never reaches the file loader', r.loaderCalls.length === 0, calls(r));
  // A URL include and !includesub keep their existing browser behaviour (the
  // former fails with "cannot include", the latter includes nothing and
  // reports no error) and never reach the loader.
  for (const [what, label, expected] of [
    ['!include https://example.invalid/x.puml', 'a URL include',
      r => failsAsIncludeError(r) && r.svg.includes('cannot include https://example.invalid/x.puml')],
    ['!includesub greeting.puml!PART', '!includesub', r => renders(r) && !r.svg.includes('Hello from greeting')],
  ]) {
    await hook.page.evaluate(() => { window.__loaderCalls.length = 0; });
    r = await renderOn(hook.page, diagram(what), opts);
    check(label + ' keeps its existing browser behaviour and never reaches the file loader',
      expected(r) && r.loaderCalls.length === 0, renderedAs(r) + '; calls: ' + calls(r));
  }
  // The engine's own files never ask the host: a relative include written in
  // a library file or a theme fails as it always did, whether the diagram or
  // a delivered file brought the library in, and the host is reachable again
  // afterwards.
  for (const [what, label] of [
    ['!include <guardlib/relative>', 'a library file'],
    ['!theme guardtheme', 'a theme'],
    ['!include via-lib.puml', 'a library file brought in by a delivered file'],
  ]) {
    await hook.page.evaluate(() => { window.__loaderCalls.length = 0; });
    r = await renderOn(hook.page, diagram(what), opts);
    check('a relative include written in ' + label + ' does not reach the file loader',
      failsAsIncludeError(r) && !r.svg.includes('WRONG_HOST') && !r.loaderCalls.some(c => c.path === 'leaf.puml'),
      renderedAs(r) + '; calls: ' + calls(r));
  }
  await hook.page.evaluate(() => { window.__loaderCalls.length = 0; });
  r = await renderOn(hook.page, diagram('!include <guardlib/greeting>', '!include greeting.puml'), opts);
  check('the file loader is reachable again after a library file',
    renders(r, 'Hello from guardlib', 'Hello from greeting') && r.loaderCalls.length === 1,
    renderedAs(r) + '; calls: ' + calls(r));
  // A delivered file that brings a benign library or theme in is `from` again
  // for the local include written after it.
  for (const [name, label] of [['via-lib-ok.puml', 'a library file'], ['via-theme-ok.puml', 'a theme']]) {
    await hook.page.evaluate(() => { window.__loaderCalls.length = 0; });
    r = await renderOn(hook.page, diagram('!include ' + name), opts);
    check('after ' + label + ' brought in by a delivered file, that file is from again',
      renders(r, 'Hello from greeting')
      && r.loaderCalls.some(c => c.path === 'greeting.puml' && c.from === '/project/docs/' + name),
      renderedAs(r) + '; calls: ' + calls(r));
  }
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

  // Page 6: an async loader that throws after awaiting, so the promise it
  // returned rejects once the call is over.
  const reject = await openPage('index-reject.html');
  r = await renderOn(reject.page, diagram('!include greeting.puml'), Object.assign({ timeoutMs: 8000 }, opts));
  check('a loader whose promise rejects fails the include as a PlantUML error image, no hang',
    failsAsIncludeError(r), renderedAs(r));
  check('no unhandled page errors on the reject page', reject.errors.length === 0, reject.errors.join(' | '));

  await browser.close();
  server.close();
  finish(scriptName, { uppercase: true });
})().catch(e => { console.error(e); process.exit(2); });
