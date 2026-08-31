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
const path = require('path'), http = require('http'), fs = require('fs');
const pw = require(process.env.BENCH_PW || 'playwright');

let dir = null, file = 'plantuml.js';
for (let i = 2; i < process.argv.length; i++) {
  const m = process.argv[i].match(/^target=(.+)$/);
  if (!m) { console.error('bad arg: ' + process.argv[i]); process.exit(2); }
  dir = m[1];
  if (dir.endsWith('.js')) { file = path.basename(dir); dir = path.dirname(dir); }
}
if (!dir) { console.error('usage: node check-background.js target=<dir-or-js>'); process.exit(2); }
dir = path.resolve(dir);

const pageHtml = `<!doctype html><html><head></head><body><div id="out"></div>
${fs.existsSync(path.join(dir, 'viz-global.js')) ? '<script src="/viz-global.js"></script>' : ''}
<script type="module">
import {render} from '/${file}';
window.__render=(lines,id,opts)=>render(lines,id,Object.assign({maxSvgSize:98304},opts||{}));
window.__ready=1;
</script></body></html>`;

const server = http.createServer((req, res) => {
  const u = decodeURIComponent(req.url.split('?')[0]);
  if (u === '/index.html') { res.setHeader('content-type', 'text/html'); return res.end(pageHtml); }
  const p = path.join(dir, u);
  if (p.startsWith(dir) && fs.existsSync(p) && fs.statSync(p).isFile()) {
    res.setHeader('content-type', 'application/javascript');
    res.setHeader('cache-control', 'no-store');
    return fs.createReadStream(p).pipe(res);
  }
  res.statusCode = 404; res.end();
});

const isErrorImage = svg => svg.includes('#33FF02') && svg.includes('#FF0000');

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

let failures = 0;
function check(label, ok, detail) {
  console.log(`${ok ? 'PASS' : 'FAIL'}  ${label}${ok || !detail ? '' : '\n        ' + detail}`);
  if (!ok) failures++;
}
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
  await new Promise(r => server.listen(0, '127.0.0.1', r));
  const port = server.address().port;
  const browser = await pw.chromium.launch({ headless: true });
  const page = await browser.newPage();
  await page.goto(`http://127.0.0.1:${port}/index.html`, { waitUntil: 'load' });
  await page.waitForFunction('window.__ready && window.__render', null, { timeout: 120000 });

  const render = async (lines, opts) => {
    const r = await page.evaluate(async ({ lines, opts }) => {
      const out = document.getElementById('out'); out.innerHTML = '';
      const done = new Promise(res => {
        const mo = new MutationObserver(() => {
          if (out.querySelector('svg') || out.textContent) { mo.disconnect(); res(); }
        });
        mo.observe(out, { childList: true, subtree: true });
      });
      let err = null;
      try { window.__render(lines, 'out', opts); } catch (e) { err = String((e && e.message) || e); }
      if (!err) await Promise.race([done, new Promise(r => setTimeout(r, 60000))]);
      const svg = out.querySelector('svg');
      return { err, svg: svg ? svg.outerHTML : null };
    }, { lines, opts: opts || {} });
    if (r.err || !r.svg) throw new Error('render produced no svg: ' + r.err);
    return r.svg;
  };

  console.log(`engine : ${path.join(dir, file)}\n`);

  // 1. The default background is white, which both drivers deliberately skip, so the
  //    unthemed diagram must stay exactly as it is today: no background at all.
  const control = await render(diagram());
  check('control renders', !isErrorImage(control));
  expectNoBackground('control paints no background (white is skipped)', control);

  // 2. skinparam backgroundColor is the plainest way to set the document background.
  expectBackground('skinparam backgroundColor paints the background',
    await render(diagram('skinparam backgroundColor #0B58A8')), '#0B58A8');

  // 3. The style form of the same setting.
  expectBackground('<style> document BackGroundColor paints the background',
    await render(diagram('<style>document{BackGroundColor #114411}</style>')), '#114411');

  // 4. Themes set the document background the same way; amiga is white on blue and is
  //    unreadable without it.
  expectBackground('!theme amiga paints its blue background',
    await render(diagram('!theme amiga')), '#0B58A8');

  // 5. Another dark theme, to show it is not a single hard-coded colour.
  expectBackground('!theme blueprint paints its background',
    await render(diagram('!theme blueprint')), '#003153');

  // 6. transparent must keep painting nothing: the host page shows through.
  expectNoBackground('skinparam backgroundColor transparent paints nothing',
    await render(diagram('skinparam backgroundColor transparent')));

  // 7. Dark mode maps the default white background away; it must not start painting one.
  const dark = await render(diagram(), { dark: true });
  check('dark mode control renders', !isErrorImage(dark));
  expectNoBackground('dark mode control paints no background', dark);

  await browser.close();
  server.close();

  console.log(`\n${failures === 0 ? 'all checks passed' : failures + ' check(s) failed'}`);
  process.exit(failures === 0 ? 0 : 1);
})().catch(e => { console.error(e); process.exit(1); });
