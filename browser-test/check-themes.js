'use strict';
// Functional check that !theme works in the PlantUML browser (TeaVM) engine.
//
// usage: node check-themes.js target=<dir-or-js>
//   target   a directory containing plantuml.js (viz-global.js and themes.js are served from
//            there too when present), or a path to the engine .js file itself
//
// Every case renders in headless Chromium against the real built engine, with themes.js served
// over http exactly as it is in the npm package. Every check runs; the exit code is non-zero if
// any failed, so it can gate a build. There are no golden files: the expected output for a theme is derived
// from that theme's own text, read out of the served themes.js.
const path = require('path'), http = require('http'), fs = require('fs'), crypto = require('crypto');
const pw = require(process.env.BENCH_PW || 'playwright');

let dir = null, file = 'plantuml.js';
for (let i = 2; i < process.argv.length; i++) {
  const m = process.argv[i].match(/^target=(.+)$/);
  if (!m) { console.error('bad arg: ' + process.argv[i]); process.exit(2); }
  dir = m[1];
  if (dir.endsWith('.js')) { file = path.basename(dir); dir = path.dirname(dir); }
}
if (!dir) { console.error('usage: node check-themes.js target=<dir-or-js>'); process.exit(2); }
dir = path.resolve(dir);

const themesJsPath = path.join(dir, 'themes.js');
if (!fs.existsSync(themesJsPath)) {
  console.error('themes.js not found next to the engine in ' + dir);
  console.error('It is produced by ThemesJsGenerator and copied by the npmPackage task.');
  process.exit(2);
}
// The themes the engine will actually serve, read from the same file the browser loads.
const THEMES = (() => {
  const g = {};
  // themes.js resolves its global as globalThis, then self; bind both to a local
  // object so the parse cannot touch this process's real globals.
  new Function('globalThis', 'self', fs.readFileSync(themesJsPath, 'utf8'))(g, g);
  return g.PLANTUML_THEMES;
})();
const NAMES = Object.keys(THEMES).sort();

const pageHtml = `<!doctype html><html><head></head><body><div id="out"></div>
${fs.existsSync(path.join(dir, 'viz-global.js')) ? '<script src="/viz-global.js"></script>' : ''}
<script type="module">
import {render} from '/${file}';
window.__render=(lines,id)=>render(lines,id,{maxSvgSize:98304});
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

// The PlantUML error image is green on black; a themed diagram never is.
const isErrorImage = svg => svg.includes('#33FF02') && svg.includes('#FF0000');
// The embedded source differs whenever the source text does, so it is excluded from comparisons.
const shape = svg => svg.replace(/<\?plantuml-src[^?]*\?>/g, '');
const hash = svg => crypto.createHash('sha256').update(shape(svg)).digest('hex');
// A theme file starts with a YAML header; the body alone is what !theme executes.
const themeBody = name => THEMES[name].replace(/^---\n[\s\S]*?\n---\n/, '');

let failures = 0;
function check(label, ok, detail) {
  console.log(`${ok ? 'PASS' : 'FAIL'}  ${label}${ok || !detail ? '' : '\n        ' + detail}`);
  if (!ok) failures++;
}

const body = ['Alice -> Bob: hello', 'Bob --> Alice: hi', 'note right: a note'];
const diagram = (...head) => ['@startuml', ...head, ...body, '@enduml'];

(async () => {
  await new Promise(r => server.listen(0, '127.0.0.1', r));
  const port = server.address().port;
  const browser = await pw.chromium.launch({ headless: true });

  async function newRenderer({ blockThemesJs = false, preregister = false } = {}) {
    const page = await browser.newPage();
    if (blockThemesJs) await page.route('**/themes.js', r => r.abort());
    if (preregister)
      await page.addInitScript(`globalThis.PLANTUML_THEMES = ${JSON.stringify(THEMES)};`);
    await page.goto(`http://127.0.0.1:${port}/index.html`, { waitUntil: 'load' });
    await page.waitForFunction('window.__ready && window.__render', null, { timeout: 120000 });
    return async lines => {
      const r = await page.evaluate(async ({ lines }) => {
        const out = document.getElementById('out'); out.innerHTML = '';
        const done = new Promise(res => {
          const mo = new MutationObserver(() => {
            if (out.querySelector('svg') || out.textContent) { mo.disconnect(); res(); }
          });
          mo.observe(out, { childList: true, subtree: true });
        });
        let err = null;
        try { window.__render(lines, 'out'); } catch (e) { err = String((e && e.message) || e); }
        if (!err) await Promise.race([done, new Promise(r => setTimeout(r, 60000))]);
        const svg = out.querySelector('svg');
        return { err, svg: svg ? svg.outerHTML : null };
      }, { lines });
      if (r.err || !r.svg) throw new Error('render produced no svg: ' + r.err);
      return r.svg;
    };
  }

  const render = await newRenderer();

  console.log(`engine : ${path.join(dir, file)}`);
  console.log(`themes : ${NAMES.length} in themes.js\n`);

  // 1. The control still renders, so later differences are attributable to the theme.
  const control = await render(diagram());
  check('control diagram renders', !isErrorImage(control));

  // 2. A theme must actually change the output. This is the regression guard for the original
  //    bug, where !theme was accepted and silently ignored.
  const amiga = await render(diagram('!theme amiga'));
  check('!theme amiga changes the output', hash(amiga) !== hash(control),
    'identical to the unthemed diagram: the directive was ignored');

  // 3. ...and change it to that theme's own colours.
  check('!theme amiga applies the amiga palette', amiga.includes('#0B58A8'),
    'expected the theme background #0B58A8 in the svg');

  // 4. Strongest check: loading a theme by name must equal pasting that theme's body inline.
  //    Both sides come from the same engine, so only the loading path differs.
  const inlined = await render(diagram(...themeBody('amiga').split('\n')));
  check('!theme amiga == the same theme inlined by hand', hash(amiga) === hash(inlined),
    'the theme loaded, but produced different output than executing its body directly');

  // 5. An unknown name must be reported, not ignored.
  const bogus = await render(diagram('!theme zzz-does-not-exist'));
  check('unknown theme name reports an error', isErrorImage(bogus),
    'rendered a normal diagram, so a typo in a theme name would pass unnoticed');

  // 6. The YAML header of the loaded theme must reach %get_current_theme().
  const meta = await render(['@startuml', '!theme amiga', '!$m = %get_current_theme()',
    'Alice -> Bob: $m.display_name', '@enduml']);
  check('%get_current_theme() returns the loaded theme metadata',
    meta.includes('Amiga Workbench 1.x'), 'expected display_name from the theme YAML header');

  // 7. %get_all_theme() must not advertise more themes than the engine can load.
  const all = await render(['@startuml', '!$a = %get_all_theme()',
    'Alice -> Bob: count=%size($a)', '@enduml']);
  check(`%get_all_theme() agrees with themes.js (${NAMES.length})`,
    all.includes(`count=${NAMES.length}`),
    'the engine lists a different number of themes than it ships');

  // 8. Every bundled theme loads and takes effect. "Takes effect" means the drawing differs
  //    from the unthemed one, which is exactly what a silently ignored directive cannot do.
  //    A theme whose body is empty (_none_ is deliberately so) is expected to match instead.
  //    Two themes drawing alike is not checked: spacelab and spacelab-white differ only in
  //    $BGCOLOR, which a sequence diagram does not show.
  const controlHash = hash(control);
  const broken = [], inert = [], unexpected = [];
  for (const name of NAMES) {
    const svg = await render(diagram(`!theme ${name}`));
    if (isErrorImage(svg)) { broken.push(name); continue; }
    const empty = themeBody(name).trim() === '';
    const same = hash(svg) === controlHash;
    if (!empty && same) inert.push(name);
    if (empty && !same) unexpected.push(name);
  }
  check(`all ${NAMES.length} bundled themes render`, broken.length === 0,
    'failed to render: ' + broken.join(', '));
  check(`all ${NAMES.length} bundled themes change the drawing`, inert.length === 0,
    'rendered identically to the unthemed diagram, so these were ignored: ' + inert.join(', '));
  check('empty themes leave the drawing unchanged', unexpected.length === 0,
    'has an empty body but changed the output: ' + unexpected.join(', '));

  // 9. A host that registers PLANTUML_THEMES itself must not need themes.js to be fetchable.
  //    This is what lets themes work inside a Web Worker, where the script loader has no
  //    document to append a script tag to.
  const preregistered = await newRenderer({ blockThemesJs: true, preregister: true });
  const amigaPre = await preregistered(diagram('!theme amiga'));
  check('pre-registered PLANTUML_THEMES works without fetching themes.js',
    amigaPre.includes('#0B58A8') && hash(amigaPre) === hash(amiga),
    'did not match the themes.js-loaded rendering of the same theme');

  // 10. Without themes.js and without pre-registration the directive must fail loudly.
  const noThemes = await newRenderer({ blockThemesJs: true });
  const amigaMissing = await noThemes(diagram('!theme amiga'));
  check('missing themes.js reports an error rather than rendering unthemed',
    isErrorImage(amigaMissing), 'silently rendered the unthemed diagram');

  await browser.close();
  server.close();

  console.log(`\n${failures === 0 ? 'all checks passed' : failures + ' check(s) failed'}`);
  process.exit(failures === 0 ? 0 : 1);
})().catch(e => { console.error(e); process.exit(1); });
