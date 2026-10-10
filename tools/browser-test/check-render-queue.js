'use strict';
// Functional check for how the browser (TeaVM) engine queues render requests.
//
// usage: node check-render-queue.js target=<dir-or-js>
//   target   a directory containing plantuml.js, or a path to the engine .js file itself
//
// The contract checked here:
//
// - renderToString: calls may overlap (several in the same JS turn). Every call
//   is answered, exactly one of its two callbacks is called, once, in the order
//   of the calls. None is dropped.
//
// - render: still "latest wins". Of several render() calls made before the
//   worker is free, only the last one is drawn. A waiting render() and waiting
//   renderToString() calls do not drop each other.
//
// - Callbacks cannot stop the worker: an error without an onError callback, or
//   an onSuccess that throws, leaves the engine answering later requests. A
//   callback that throws is reported with console.error and does not trigger
//   onError for the same request.
//
// Only sequence diagrams are used: they need no Graphviz layout, so the page
// does not load viz-global.js.
const path = require('path');
const { createCheckReporter } = require('../lib/browser-check');
const { parseTargetArg } = require('../lib/browser-cli');
const { createMountedServer, startServer } = require('../lib/browser-http');
const { createModulePageHtml, loadPlaywright, openReadyPage } = require('../lib/browser-page');

const scriptName = path.basename(__filename, '.js');
const { dir, file } = parseTargetArg(process.argv, `node ${scriptName}.js target=<dir-or-js>`);
const pw = loadPlaywright();

const server = createMountedServer({
  routes: {
    '/index.html': {
      contentType: 'text/html',
      body: createModulePageHtml({
        modulePath: `/${file}`,
        moduleBody: `import {renderToString} from '/${file}';
window.__api = {render, renderToString};
// A sequence diagram that carries its label, so that an SVG can be matched with
// the request that produced it.
window.__h = {
  seq: label => ['@startuml', 'Alice -> Bob : ' + label, '@enduml'],
  wait: ms => new Promise(r => setTimeout(r, ms)),
  withTimeout: (p, ms) => Promise.race([p, new Promise(r => setTimeout(r, ms)).then(() => 'timeout')]),
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
  const consoleErrors = [];
  const { page, errors: pageErrors } = await openReadyPage(browser, `http://127.0.0.1:${port}/index.html`, {
    polling: 200,
    onConsole: msg => { if (msg.type() === 'error') consoleErrors.push(msg.text()); },
  });

  // 1. Overlapping renderToString calls, all in the same JS turn.
  const labels = ['queueAlpha', 'queueBravo', 'queueCharlie', 'queueDelta', 'queueEcho'];
  const overlap = await page.evaluate(async ({ labels }) => {
    const log = [];
    const { seq, wait, withTimeout } = window.__h;
    const all = new Promise(res => {
      labels.forEach(label => window.__api.renderToString(seq(label),
        svg => { log.push({ label, kind: 'success', hasOwnLabel: svg.includes(label) }); if (log.length === labels.length) res('done'); },
        err => { log.push({ label, kind: 'error', err }); if (log.length === labels.length) res('done'); }));
    });
    const state = await withTimeout(all, 60000);
    await wait(300); // a late duplicate would show up here
    return { state, log };
  }, { labels });
  check(`${labels.length} overlapping renderToString calls are all answered`,
    overlap.state === 'done' && overlap.log.length === labels.length,
    `state=${overlap.state}, answered ${overlap.log.length}/${labels.length}`);
  check('they are answered in the order of the calls',
    JSON.stringify(overlap.log.map(l => l.label)) === JSON.stringify(labels),
    overlap.log.map(l => l.label).join(','));
  check('each one gets its own SVG, through onSuccess',
    overlap.log.every(l => l.kind === 'success' && l.hasOwnLabel),
    JSON.stringify(overlap.log));

  // 2. render() is still "latest wins" ...
  const latest = await page.evaluate(async () => {
    const { seq, wait, withTimeout } = window.__h;
    const out = document.getElementById('out');
    out.innerHTML = '';
    window.__api.render(seq('renderFirst'), 'out');
    window.__api.render(seq('renderSecond'), 'out');
    window.__api.render(seq('renderLast'), 'out');
    for (let i = 0; i < 600 && out.querySelector('svg') === null; i++)
      await wait(100);
    await wait(500); // an older request drawn late would replace the result
    return out.textContent;
  });
  check('of several render() calls in the same turn, only the last one is drawn',
    latest.includes('renderLast') && !latest.includes('renderFirst') && !latest.includes('renderSecond'),
    'text in the target: ' + latest.slice(0, 120));

  // 3. ... and neither kind of request drops the other.
  const mixed = await page.evaluate(async () => {
    const { seq, wait, withTimeout } = window.__h;
    const out = document.getElementById('out');
    out.innerHTML = '';
    const answers = [];
    const all = new Promise(res => {
      const done = () => { if (answers.length === 2) res('done'); };
      window.__api.renderToString(seq('mixedBefore'), svg => { answers.push(svg.includes('mixedBefore')); done(); }, () => { answers.push(false); done(); });
      window.__api.render(seq('mixedRender'), 'out');
      window.__api.renderToString(seq('mixedAfter'), svg => { answers.push(svg.includes('mixedAfter')); done(); }, () => { answers.push(false); done(); });
    });
    const state = await withTimeout(all, 60000);
    for (let i = 0; i < 100 && out.querySelector('svg') === null; i++)
      await wait(100);
    return { state, answers, text: out.textContent };
  });
  check('a render() in between does not drop the renderToString calls around it',
    mixed.state === 'done' && mixed.answers.length === 2 && mixed.answers.every(Boolean),
    JSON.stringify(mixed));
  check('a renderToString around it does not drop the render()',
    mixed.text.includes('mixedRender'), 'text in the target: ' + mixed.text.slice(0, 120));

  // 4. The error path: exactly one callback, onError.
  const failing = await page.evaluate(async () => {
    const { seq, wait, withTimeout } = window.__h;
    const calls = { success: 0, error: 0, message: '' };
    const all = new Promise(res => window.__api.renderToString(seq('tooBig'),
      () => { calls.success++; res('done'); },
      err => { calls.error++; calls.message = String(err); res('done'); },
      { maxSvgSize: 1 }));
    const state = await withTimeout(all, 60000);
    await wait(300);
    return { state, calls };
  });
  check('a failing render calls onError once, and not onSuccess',
    failing.calls.error === 1 && failing.calls.success === 0,
    JSON.stringify(failing));

  // 5. No onError: the failure must not stop the worker.
  for (const [label, missing] of [['undefined', 'undefined'], ['null', 'null']]) {
    const afterMissing = await page.evaluate(async ({ missing }) => {
      const { seq, wait, withTimeout } = window.__h;
      const noError = missing === 'null' ? null : undefined;
      let threw = null;
      try {
        window.__api.renderToString(seq('noErrorCallback'), () => {}, noError, { maxSvgSize: 1 });
      } catch (e) {
        threw = String(e && e.message || e);
      }
      const next = new Promise(res => window.__api.renderToString(seq('afterMissing'),
        svg => res(svg.includes('afterMissing') ? 'ok' : 'wrong svg'), err => res('error: ' + err)));
      return { threw, next: await withTimeout(next, 60000) };
    }, { missing });
    check(`a failing render with an ${label} onError does not stop the engine`,
      afterMissing.threw === null && afterMissing.next === 'ok',
      JSON.stringify(afterMissing));
  }

  // 6. A callback that throws: contained, reported, no onError for the same request.
  const before = consoleErrors.length;
  const throwing = await page.evaluate(async () => {
    const { seq, wait, withTimeout } = window.__h;
    const calls = { success: 0, error: 0 };
    window.__api.renderToString(seq('hostBug'),
      () => { calls.success++; throw new Error('host callback bug (simulated)'); },
      () => { calls.error++; });
    const next = new Promise(res => window.__api.renderToString(seq('afterThrow'),
      svg => res(svg.includes('afterThrow') ? 'ok' : 'wrong svg'), err => res('error: ' + err)));
    const answer = await withTimeout(next, 60000);
    await wait(300);
    return { calls, answer };
  });
  check('a throwing onSuccess is called once, and onError is not called for it',
    throwing.calls.success === 1 && throwing.calls.error === 0, JSON.stringify(throwing.calls));
  check('a throwing onSuccess does not stop the engine', throwing.answer === 'ok', String(throwing.answer));
  check('a throwing onSuccess is reported with console.error',
    consoleErrors.slice(before).some(m => /callback threw/.test(m)),
    'console errors: ' + consoleErrors.slice(before).join(' | '));

  check('no unhandled page errors', pageErrors.length === 0, pageErrors.join(' | '));

  await browser.close();
  server.close();
  finish(scriptName, { uppercase: true });
})().catch(e => { console.error(e); process.exit(2); });
