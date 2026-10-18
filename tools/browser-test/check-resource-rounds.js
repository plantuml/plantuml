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
// - a rendering reads on past an include whose file is not there yet, so the
//   includes of one level are asked for in the same round: many includes side
//   by side take one round, not one per file (issue #2943);
// - a file chosen by an !if on a value from a file not loaded yet gives the
//   right branch, and the other branch's file, asked for once, is not reported;
// - rendering again is invisible: the same source gives the same SVG, a
//   failure that is reported on the console is reported once, not once per
//   round, and render() only writes the target with the final result;
// - render() keeps "latest wins" while a request waits for its resources;
// - a request made from a callback is answered, after the current one;
// - past 20 rounds, the request fails with a message naming what is missing;
// - a host that never answers makes the include fail after 60 s instead of
//   leaving the engine waiting forever, and later requests are answered;
// - a failure to create the Viz.js instance (the WebAssembly module did not
//   load) fails that request only: the next one tries again.
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
// "wideN.puml" stands alone; "fanN.puml" includes "leafN.puml".
for (let i = 1; i <= 60; i++) {
  window.__files['/p/wide' + i + '.puml'] = 'W' + i + ' -> V' + i + ' : wide' + i;
  window.__files['/p/fan' + i + '.puml'] = '!include leaf' + i + '.puml';
  window.__files['/p/leaf' + i + '.puml'] = 'L' + i + ' -> M' + i + ' : leaf' + i;
}
// flag.puml sets the variable picked.puml depends on; ghost.puml does not exist.
window.__files['/p/flag.puml'] = '!$picked = "yes"';
window.__files['/p/picked.puml'] = 'P -> Q : pickedBranch';
window.__loads = [];
// Loading rounds: the loader is called for every file of a round in the same
// JavaScript task, so calls in one task count as one round.
window.__rounds = 0;
window.__inRound = false;
window.__delay = 5;
window.__hang = false;
globalThis.PLANTUML_FILE_LOADER = function(p, from, ok, err) {
  const full = '/p/' + p;
  window.__loads.push(full);
  if (!window.__inRound) {
    window.__inRound = true;
    window.__rounds++;
    setTimeout(() => { window.__inRound = false; }, 0);
  }
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
  const rounds = () => page.evaluate(() => { const r = window.__rounds; window.__rounds = 0; return r; });
  const includes = (prefix, n) => ['@startuml', ...Array.from({ length: n }, (_, i) => `!include ${prefix}${i + 1}.puml`), '@enduml'];
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

  // 2b. Many includes on one level (issue #2943): one round, not one per file.
  for (const n of [21, 60]) {
    await loads(); await rounds();
    const wide = await run(includes('wide', n));
    const wideLoads = await loads();
    const wideRounds = await rounds();
    check(`${n} includes side by side render`,
      !!wide.ok && wide.ok.includes('wide1') && wide.ok.includes('wide' + n), String(wide.err || '').slice(0, 200));
    check(`${n} includes side by side are asked for in one round`,
      wideRounds === 1 && wideLoads.length === n && new Set(wideLoads).size === n,
      `${wideRounds} round(s), ${wideLoads.length} load(s)`);
  }
  await loads(); await rounds();
  const fan = await run(includes('fan', 30));
  const fanLoads = await loads();
  const fanRounds = await rounds();
  check('30 includes that each include a file render',
    !!fan.ok && fan.ok.includes('leaf1') && fan.ok.includes('leaf30'), String(fan.err || '').slice(0, 200));
  check('30 includes that each include a file take two rounds, one per level',
    fanRounds === 2 && fanLoads.length === 60 && new Set(fanLoads).size === 60,
    `${fanRounds} round(s), ${fanLoads.length} load(s)`);

  // 2c. An !if on a value from a file that is not there yet.
  const warnsBeforeIf = consoleMessages.length;
  await loads();
  const branch = await run(['@startuml', '!include flag.puml', '!if %variable_exists("$picked")', '!include picked.puml',
    '!else', '!include ghost.puml', '!endif', '@enduml']);
  const branchLoads = await loads();
  check('an !if on a value from a file loaded later takes the right branch',
    !!branch.ok && branch.ok.includes('pickedBranch') && !/cannot include/.test(branch.ok), String(branch.err || branch.ok).slice(0, 200));
  check('the file of the other branch is asked for once',
    branchLoads.filter(l => l === '/p/ghost.puml').length === 1, branchLoads.join(','));
  check('the file of the other branch is not reported',
    consoleMessages.slice(warnsBeforeIf).every(m => !/ghost\.puml/.test(m.text)),
    consoleMessages.slice(warnsBeforeIf).map(m => m.text).join(' | '));

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

  // 8. A transient failure of Viz.instance(), on a page of its own (the Viz.js
  // instance is created once per page).
  const flaky = await openReadyPage(browser, `http://127.0.0.1:${port}/index.html`, {
    readyExpression: 'window.__ready',
    beforeGoto: p => p.addInitScript(() => {
      let calls = 0;
      Object.defineProperty(globalThis, 'PLANTUML_VIZ', {
        configurable: true,
        get: () => ({ instance: () => (++calls === 1 ? Promise.reject(new Error('flaky wasm download')) : globalThis.Viz.instance()) }),
      });
    }),
  });
  const classDiagram = ['@startuml', 'class Flaky', 'Flaky --> Other', '@enduml'];
  const runFlaky = () => flaky.page.evaluate(l => window.__h.withTimeout(window.__h.rts(l), 60000), classDiagram);
  const firstFlaky = await runFlaky();
  const secondFlaky = await runFlaky();
  check('a failed Viz.instance() is reported, naming the failure',
    !!firstFlaky.ok && /flaky wasm download/.test(firstFlaky.ok), JSON.stringify(firstFlaky).slice(0, 200));
  check('the next request tries Viz.instance() again, and renders',
    !!secondFlaky.ok && secondFlaky.ok.includes('Flaky') && !/flaky wasm download/.test(secondFlaky.ok),
    JSON.stringify(secondFlaky).slice(0, 200));
  await flaky.page.close();

  check('no unhandled page errors', pageErrors.concat(flaky.errors).length === 0, pageErrors.concat(flaky.errors).join(' | '));
  check('no TeaVM suspension error on the console',
    consoleMessages.every(m => !/Suspension point|non-threading context/.test(m.text)));

  await browser.close();
  server.close();
  finish(scriptName, { uppercase: true });
})().catch(e => { console.error(e); process.exit(2); });
