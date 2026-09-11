'use strict';
// Functional check that the diagram background is painted in the PlantUML browser (TeaVM)
// engine, the way the Java build paints it.
//
// usage: node check-background.js target=<dir-or-js>
//   target   a directory containing plantuml.js (viz-global.js and themes.js are served from
//            there too when present), or a path to the engine .js file itself
//
// The Java build honours the merged root.document BackGroundColor (which is where both
// skinparam backgroundColor and a theme's background land) by writing a background style on
// the svg element plus a rectangle covering the whole drawing. These checks assert the same
// contract on the browser engine, including the deliberate skips: null, transparent, pure
// black and pure white paint nothing (see SvgGraphics.paintBackcolor and the
// SvgGraphicsTeaVM constructor). Every check runs; the exit code is non-zero if any failed.
const path = require('path');
const { createCheckReporter, isErrorImage } = require('../lib/browser-check');
const { parseTargetArg } = require('../lib/browser-cli');
const { createMountedServer, startServer } = require('../lib/browser-http');
const { createModulePageHtml, loadPlaywright, makeRenderModuleBody, maybeScriptTag, newRenderer } = require('../lib/browser-page');

const pw = loadPlaywright();
const { dir, file } = parseTargetArg(process.argv, 'node check-background.js target=<dir-or-js>');

const pageHtml = createModulePageHtml({
  modulePath: `/${file}`,
  bodyHtml: maybeScriptTag(dir, 'viz-global.js', '/viz-global.js'),
  moduleBody: makeRenderModuleBody({ maxSvgSize: 98304, allowOverrides: true }),
});

const server = createMountedServer({
  routes: {
    '/index.html': { contentType: 'text/html', body: pageHtml },
  },
  mounts: [{ prefix: '/', dir }],
});

// The background contract, as one predicate: a rect at 0,0 covering the whole viewBox in
// the expected fill, plus a background-color style on the svg element itself.
function backgroundOf(svg) {
  const vb = svg.match(/viewBox="0 0 ([\d.]+) ([\d.]+)"/);
  if (!vb) return { error: 'no viewBox' };
  const w = parseFloat(vb[1]), h = parseFloat(vb[2]);
  const style = (svg.match(/<svg[^>]*style="([^"]*)"/) || [])[1] || '';
  const styleColor = (style.match(/background-color:\s*([^;"]+)/) || [])[1] || null;
  // the background rect is the one at the origin covering the full viewBox
  let rectColor = null;
  const rectRe = /<rect ([^>]*)>/g;
  for (let m; (m = rectRe.exec(svg));) {
    const a = m[1];
    const num = k => parseFloat((a.match(new RegExp(k + '="([\\d.]+)"')) || [])[1]);
    if (num('x') === 0 && num('y') === 0 && Math.abs(num('width') - w) < 1 && Math.abs(num('height') - h) < 1) {
      rectColor = (a.match(/fill="([^"]+)"/) || [])[1] || null;
      break;
    }
  }
  return { styleColor, rectColor };
}

const { check, finish } = createCheckReporter();
function expectBackground(label, svg, color) {
  const bg = backgroundOf(svg);
  check(label, bg.styleColor === color && bg.rectColor === color,
    `expected background ${color}, got style=${bg.styleColor} rect=${bg.rectColor}`);
}
function expectNoBackground(label, svg) {
  const bg = backgroundOf(svg);
  check(label, !bg.styleColor && !bg.rectColor,
    `expected no background, got style=${bg.styleColor} rect=${bg.rectColor}`);
}

const body = ['Alice -> Bob: hello', 'Bob --> Alice: hi'];
const diagram = (...head) => ['@startuml', ...head, ...body, '@enduml'];

(async () => {
  const port = await startServer(server);
  const browser = await pw.chromium.launch({ headless: true });
  const render = await newRenderer(browser, `http://127.0.0.1:${port}/index.html`, { trackErrors: false }, {
    timeoutMs: 60000,
    maxTextLength: 120,
  });

  const renderSvg = async (lines, opts) => {
    const r = await render(lines, opts || {});
    if (r.err || !r.svg) throw new Error('render produced no svg: ' + r.err);
    return r.svg;
  };

  console.log(`engine : ${path.join(dir, file)}\n`);

  // 1. The default background is white, which both drivers deliberately skip, so the
  //    unthemed diagram must stay exactly as it is today: no background at all.
  const control = await renderSvg(diagram());
  check('control renders', !isErrorImage(control));
  expectNoBackground('control paints no background (white is skipped)', control);

  // 2. skinparam backgroundColor is the plainest way to set the document background.
  expectBackground('skinparam backgroundColor paints the background',
    await renderSvg(diagram('skinparam backgroundColor #0B58A8')), '#0B58A8');

  // 3. The style form of the same setting.
  expectBackground('<style> document BackGroundColor paints the background',
    await renderSvg(diagram('<style>document{BackGroundColor #114411}</style>')), '#114411');

  // 4. Themes set the document background the same way; amiga is white on blue and is
  //    unreadable without it.
  expectBackground('!theme amiga paints its blue background',
    await renderSvg(diagram('!theme amiga')), '#0B58A8');

  // 5. Another dark theme, to show it is not a single hard-coded colour.
  expectBackground('!theme blueprint paints its background',
    await renderSvg(diagram('!theme blueprint')), '#003153');

  // 6. transparent must keep painting nothing: the host page shows through.
  expectNoBackground('skinparam backgroundColor transparent paints nothing',
    await renderSvg(diagram('skinparam backgroundColor transparent')));

  // 7. Dark mode maps the default white background away; it must not start painting one.
  const dark = await renderSvg(diagram(), { dark: true });
  check('dark mode control renders', !isErrorImage(dark));
  expectNoBackground('dark mode control paints no background', dark);

  // 8. Dark mode only suppresses the default: an author's explicit color has no paired
  //    dark value and must paint unchanged.
  expectBackground('dark mode keeps an explicit background',
    await renderSvg(diagram('skinparam backgroundColor #0B58A8'), { dark: true }), '#0B58A8');

  // 9. scale changes the root size but not the viewBox convention; the background must
  //    still cover the whole viewBox (expectBackground asserts exactly that).
  expectBackground('scale 2 keeps the background covering the viewBox',
    await renderSvg(diagram('scale 2', 'skinparam backgroundColor #0B58A8')), '#0B58A8');

  // 10. The fix lives in the shared buildSvg path, not in the sequence renderer;
  //     pin one diagram type with its own layouter and one that goes through graphviz.
  expectBackground('activity diagram paints a theme background',
    await renderSvg(['@startuml', '!theme amiga', 'start', ':do the thing;', 'stop', '@enduml']), '#0B58A8');
  expectBackground('class diagram paints the background',
    await renderSvg(['@startuml', 'skinparam backgroundColor #0B58A8', 'class Foo', 'class Bar', 'Foo -> Bar', '@enduml']),
    '#0B58A8');

  // 11. A skinparam after !theme overrides the theme background, like the Java build
  //     (verified against the jar: background:#114411).
  expectBackground('skinparam after !theme overrides the theme background',
    await renderSvg(diagram('!theme amiga', 'skinparam backgroundColor #114411')), '#114411');

  // 12. A gradient background degrades to its first color under TeaVM (HColorGradient
  //     resolves to color1 there; the Java build renders a real gradient). Pinned so the
  //     degradation stays a degradation and never becomes an error.
  const gradient = await renderSvg(diagram('skinparam backgroundColor #0B58A8-#004488'));
  check('gradient background renders', !isErrorImage(gradient));
  expectBackground('gradient background degrades to its first color', gradient, '#0B58A8');

  await browser.close();
  server.close();

  finish({ leadingBlankLine: true });
})().catch(e => { console.error(e); process.exit(1); });
