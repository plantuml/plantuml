'use strict';
// Functional check for how the browser (TeaVM) engine gets what a diagram needs
// from JavaScript without waiting for it: the Viz.js instance, support scripts
// (themes.js, emoji.js...), and files of PLANTUML_FILE_LOADER.
//
// usage: node check-resource-rounds.js target=<dir-or-js>
//   target   a directory containing plantuml.js, or a path to the engine .js file itself
//
// The engine uses no thread and no TeaVM coroutine. A rendering that misses a
// resource is discarded, the resource is loaded with JavaScript callbacks, and
// the diagram is rendered again (PendingResources / RenderQueue). The contract
// checked here:
//
// - a callback is never called before renderToString returns;
// - each round discovers one more level: an include chain of any depth up to
//   the limit renders, and the host is asked once per file during a rendering
//   (and again at the next rendering, so an edited file is seen);
// - rendering again is invisible: the same source gives the same SVG, a
//   failure that is reported on the console is reported once, not once per
//   round, and render() only writes the target with the final result;
// - render() keeps "latest wins" while a request waits for its resources;
// - a request made from a callback is answered, after the current one;
// - past 20 rounds, the request fails with a message naming what is missing;
// - a host that never answers makes the include fail after 60 s instead of
//   leaving the engine waiting forever, and later requests are answered.
const path = require('path');
const { createCheckReporter } = require('../lib/browser-check');
const { parseTargetArg } = require('../lib/browser-cli');
const { createMountedServer, startServer } = require('../lib/browser-http');
const { createModulePageHtml, loadPlaywright, openReadyPage, maybeScriptTag } = require('../lib/browser-page');

const scriptName = path.basename(__filename, '.js');
const { dir, file } = parseTargetArg(process.argv, `node ${scriptName}.js target=<dir-or-js>`);
const pw = loadPlaywright();

const server = createMountedServer({
  routes: {
    '/index.html': {
      contentType: 'text/html',
      body: createModulePageHtml({
        modulePath: `/${file}`,
        headHtml: maybeScriptTag(dir, 'viz-global.js'),
        moduleBody: `import {renderToString} from '/${file}';
window.__api = {render, renderToString};
// Files of the host, by absolute path. A file named "chainN.puml" includes
// "chain(N-1).puml", down to chain0.puml.
window.__files = {};
for (let i = 0; i < 30; i++)
  window.__files['/p/chain' + i + '.puml'] = (i > 0 ? '!include chain' + (i - 1) + '.puml\\n' : '') + 'A' + i + ' -> B' + i + ' : level' + i;
window.__files['/p/broken.puml'] = '!include does-not-exist.puml\\nX -> Y : broken';
window.__loads = [];
window.__delay = 5;
window.__hang = false;
globalThis.PLANTUML_FILE_LOADER = function(p, from, ok, err) {
  const full = '/p/' + p;
  window.__loads.push(full);
  if (window.__hang && p === 'hang.puml') return;
  setTimeout(() => window.__files[full] !== undefined ? ok(full, window.__files[full]) : err('no such file ' + full), window.__delay);
};
window.__h = {
  wait: ms => new Promise(r => setTimeout(r, ms)),
  withTimeout: (p, ms) => Promise.race([p, new Promise(r => setTimeout(r, ms)).then(() => 'timeout')]),
  rts: (lines, opts) => new Promise(res => {
    let returned = false;
    window.__api.renderToString(lines,
      svg => res({ ok: svg, early: !returned }),
      e => res({ err: String(e), early: !returned }), opts);
    returned = true;
  }),
};
window.__ready = 1;`,
      }),
    },
  },
  mounts: [{ prefix: '/', dir }],
});

const { check, finish } = createCheckReporter();

(async () => {
  const port = await startServer(server);
  const browser = await pw.chromium.launch({ headless: true });
  const consoleMessages = [];
  const { page, errors: pageErrors } = await openReadyPage(browser, `http://127.0.0.1:${port}/index.html`, {
    readyExpression: 'window.__ready',
    onConsole: msg => consoleMessages.push({ type: msg.type(), text: msg.text() }),
  });
  const run = (lines, opts) => page.evaluate(([l, o]) => window.__h.withTimeout(window.__h.rts(l, o), 120000), [lines, opts || null]);
  const loads = () => page.evaluate(() => { const l = window.__loads; window.__loads = []; return l; });
  const chain = n => ['@startuml', `!include chain${n}.puml`, '@enduml'];

  // 1. Never answered from the call itself, even with everything at hand.
  const quick = await run(['@startuml', 'Alice -> Bob : quick', '@enduml']);
  check('a callback is never called before renderToString returns', quick.ok && quick.early === false, JSON.stringify(quick).slice(0, 200));

  // 2. An include chain: one level per round.
  await loads();
  const deep = await run(chain(12));
  const deepLoads = await loads();
  check('an include chain 13 files deep renders', !!deep.ok && [0, 6, 12].every(i => deep.ok.includes('level' + i)),
    String(deep.err || '').slice(0, 200));
  check('the host is asked once per file during a rendering',
    deepLoads.length === 13 && new Set(deepLoads).size === 13, deepLoads.join(','));
  await run(chain(12));
  const againLoads = await loads();
  check('the next rendering asks the host again (an edited file is seen)', againLoads.length === 13, againLoads.join(','));

  // 3. Rendering again is invisible.
  const full = ['@startuml', '!theme cerulean', '!include chain3.puml', 'class Foo', 'Foo --> Bar : <:smile:> <&heart>', '@enduml'];
  const first = await run(full);
  const second = await run(full);
  check('a diagram needing Viz, a theme, emoji, OpenIconic and includes renders',
    !!first.ok && first.ok.includes('Foo') && first.ok.includes('level3'), String(first.err || '').slice(0, 200));
  check('the same source gives the same SVG', !!first.ok && first.ok === second.ok);

  const warnsBefore = consoleMessages.length;
  const broken = await run(['@startuml', '!include chain2.puml', '!include broken.puml', '@enduml']);
  const warned = consoleMessages.slice(warnsBefore).filter(m => /cannot include does-not-exist\.puml/.test(m.text));
  check('a failing nested include is an include error', !!broken.ok && /cannot include/.test(broken.ok), String(broken.err || '').slice(0, 200));
  check('its console warning is written once, not once per round', warned.length === 1,
    warned.length + ' warning(s): ' + warned.map(m => m.text).join(' | '));

  // 4. "Latest wins" while waiting for resources; the target only gets the final result.
  const latest = await page.evaluate(async () => {
    const { wait } = window.__h;
    const out = document.getElementById('out');
    out.innerHTML = '';
    const seen = [];
    const mo = new MutationObserver(() => seen.push(out.textContent));
    mo.observe(out, { childList: true, subtree: true, characterData: true });
    window.__delay = 400;
    window.__api.render(['@startuml', '!include chain4.puml', 'Z -> Z : firstRender', '@enduml'], 'out');
    await wait(200); // the first request now waits for its files
    window.__api.render(['@startuml', '!include chain1.puml', 'Z -> Z : lastRender', '@enduml'], 'out');
    for (let i = 0; i < 300 && !out.textContent.includes('lastRender'); i++)
      await wait(100);
    await wait(1500);
    window.__delay = 5;
    mo.disconnect();
    return { text: out.textContent, seen };
  });
  check('render() replaced while waiting for its files: only the latest is drawn',
    latest.text.includes('lastRender') && latest.seen.every(t => !t.includes('firstRender')),
    latest.text.slice(0, 120));
  check('render() writes the target only with a complete result',
    latest.seen.every(t => t === '' || t.includes('level0')), JSON.stringify(latest.seen.map(t => t.slice(0, 40))));

  // 5. A request made from a callback.
  const nested = await page.evaluate(() => window.__h.withTimeout(new Promise(res => {
    const order = [];
    window.__api.renderToString(['@startuml', '!include chain2.puml', '@enduml'], () => {
      order.push('outer');
      window.__api.renderToString(['@startuml', '!include chain1.puml', 'Q -> Q : inner', '@enduml'],
        svg => { order.push(svg.includes('inner') ? 'inner' : 'wrong'); res(order); }, e => { order.push('error ' + e); res(order); });
      order.push('outer returns');
    }, e => res(['error ' + e]));
  }), 60000));
  check('a request made from a callback is answered, after the callback returns',
    JSON.stringify(nested) === JSON.stringify(['outer', 'outer returns', 'inner']), JSON.stringify(nested));

  // 6. The limit on rounds.
  const tooDeep = await run(chain(25));
  check('past 20 rounds, onError names what is still missing',
    !!tooDeep.err && /20 rounds/.test(tooDeep.err) && /chain/.test(tooDeep.err), JSON.stringify(tooDeep).slice(0, 300));
  const after = await run(['@startuml', 'Alice -> Bob : afterLimit', '@enduml']);
  check('the engine answers the next request', !!after.ok && after.ok.includes('afterLimit'));

  // 7. A host that never answers.
  await page.evaluate(() => { window.__hang = true; });
  const t0 = Date.now();
  const hung = await run(['@startuml', '!include hang.puml', 'Alice -> Bob : afterHang', '@enduml']);
  const elapsed = Date.now() - t0;
  check('a loader that never answers fails the include after 60 s',
    !!hung.ok && /cannot include/.test(hung.ok) && elapsed >= 59000 && elapsed < 90000,
    `after ${elapsed} ms: ` + JSON.stringify(hung).slice(0, 200));
  check('the console says the host did not answer',
    consoleMessages.some(m => /no answer within 60 s/.test(m.text)));
  await page.evaluate(() => { window.__hang = false; });
  const afterHang = await run(['@startuml', '!include chain1.puml', '@enduml']);
  check('the engine answers the next request after a hung loader', !!afterHang.ok && afterHang.ok.includes('level1'));

  check('no unhandled page errors', pageErrors.length === 0, pageErrors.join(' | '));
  check('no TeaVM suspension error on the console',
    consoleMessages.every(m => !/Suspension point|non-threading context/.test(m.text)));

  await browser.close();
  server.close();
  finish(scriptName, { uppercase: true });
})().catch(e => { console.error(e); process.exit(2); });
