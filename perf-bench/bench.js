'use strict';
// Render-performance benchmark for the PlantUML browser (TeaVM) engine.
//
// usage: node bench.js target=<dir-or-js> [reference=<dir-or-js>] [options]
//   engine spec     name=path   path is a directory containing plantuml.js (viz-global.js served
//                               too when present) or a path to the engine .js file itself
//   --reps N        renders per diagram per block (default 6; rep 0 of each block is cold, discarded)
//   --blocks N      passes over the corpus (default 2); engines alternate per rep within a diagram
//   --corpus S      only diagrams whose relative path contains S
//   --maxsvg N      maxSvgSize render option passed to every engine (default 98304; engines that
//                   predate the option ignore it and may truncate tall output, which is detected
//                   and marked per row)
//   --out DIR       output directory (default results): results.json, summary.md
//
// Methodology (do not change casually, band history depends on it): all engines render in ONE
// browser instance (one page per engine) so per-run host speed cancels in the target/reference
// ratio; warm medians pooled across blocks; small/ files aggregate into a single row.
const path = require('path'), http = require('http'), fs = require('fs'), os = require('os');
const pw = require(process.env.BENCH_PW || 'playwright');

const HERE = __dirname;
const engines = []; // {name, dir, file}
const opt = { reps: 6, blocks: 2, corpus: '', maxsvg: 98304, out: 'results' };
for (let i = 2; i < process.argv.length; i++) {
  const a = process.argv[i];
  const flag = a.match(/^--(\w+)$/);
  if (flag) { opt[flag[1]] = process.argv[++i]; continue; }
  const m = a.match(/^(\w+)=(.+)$/);
  if (!m) { console.error('bad arg: ' + a); process.exit(2); }
  let dir = m[2], file = 'plantuml.js';
  if (dir.endsWith('.js')) { file = path.basename(dir); dir = path.dirname(dir); }
  engines.push({ name: m[1], dir: path.resolve(dir), file });
}
opt.reps = Number(opt.reps); opt.blocks = Number(opt.blocks); opt.maxsvg = Number(opt.maxsvg);
if (engines.length < 1 || engines.length > 2 || engines[0].name !== 'target') {
  console.error('need target=<path> and optionally reference=<path>'); process.exit(2);
}

const corpusDir = path.join(HERE, 'corpus');
const allFiles = [];
(function walk(d) {
  for (const e of fs.readdirSync(d, { withFileTypes: true })) {
    const p = path.join(d, e.name);
    if (e.isDirectory()) walk(p);
    else if (e.name.endsWith('.puml')) allFiles.push(path.relative(corpusDir, p).replace(/\\/g, '/'));
  }
})(corpusDir);
allFiles.sort();
const files = allFiles.filter(f => f.includes(opt.corpus));
if (files.length === 0) { console.error('corpus filter matched nothing'); process.exit(2); }

function pageHtml(engine) {
  const viz = fs.existsSync(path.join(engine.dir, 'viz-global.js'))
    ? `<script src="/${engine.name}/viz-global.js"></script>` : '';
  return `<!doctype html><html><head><script>window.__t0=performance.now();</script></head><body>
<div id="out"></div>
${viz}
<script type="module">
import {render} from '/${engine.name}/${engine.file}';
window.__render=(lines,id)=>render(lines,id,{maxSvgSize:${opt.maxsvg}});
window.__importMs=Math.round(performance.now()-window.__t0);
window.__ready=1;
</script></body></html>`;
}

const server = http.createServer((req, res) => {
  const u = decodeURIComponent(req.url.split('?')[0]);
  const m = u.match(/^\/(\w+)\/(.*)$/);
  const eng = m && engines.find(e => e.name === m[1]);
  if (u.startsWith('/page/')) {
    const e2 = engines.find(e => e.name === u.slice(6));
    if (e2) { res.setHeader('content-type', 'text/html'); return res.end(pageHtml(e2)); }
  }
  if (eng) {
    const p = path.join(eng.dir, m[2]);
    if (fs.existsSync(p) && fs.statSync(p).isFile()) {
      res.setHeader('content-type', 'application/javascript');
      res.setHeader('cache-control', 'no-store');
      return fs.createReadStream(p).pipe(res);
    }
  }
  res.statusCode = 404; res.end();
});

async function renderOnce(page, lines) {
  return page.evaluate(async ({ lines, id }) => {
    const out = document.getElementById(id); out.innerHTML = '';
    const t0 = performance.now();
    let err = null;
    const done = new Promise(res => {
      const mo = new MutationObserver(() => { if (out.querySelector('svg') || out.textContent) { mo.disconnect(); res(); } });
      mo.observe(out, { childList: true, subtree: true });
    });
    try { window.__render(lines, id); } catch (e) { err = String(e && e.message || e); }
    if (!err) await Promise.race([done, new Promise(r => setTimeout(r, 180000))]);
    const svg = out.querySelector('svg');
    let sha = null, bytes = 0, truncated = false;
    if (svg) {
      const html = svg.outerHTML;
      bytes = html.length;
      truncated = html.includes('(max ');
      const buf = await crypto.subtle.digest('SHA-256', new TextEncoder().encode(html));
      sha = [...new Uint8Array(buf)].map(b => b.toString(16).padStart(2, '0')).join('');
    }
    return { ms: Math.round(performance.now() - t0), err: err || (svg ? null : String(out.textContent).slice(0, 120)), bytes, sha, truncated };
  }, { lines, id: 'out' });
}

function median(v) { const s = [...v].sort((a, b) => a - b); return s.length ? s[Math.floor(s.length / 2)] : null; }
function iqr(v) { const s = [...v].sort((a, b) => a - b); return s.length ? [s[Math.floor(s.length / 4)], s[Math.floor(3 * s.length / 4)]] : [null, null]; }

(async () => {
  await new Promise(r => server.listen(0, '127.0.0.1', r));
  const port = server.address().port;
  const browser = await pw.chromium.launch({ headless: true });
  const reps = []; // {engine, diagram, block, rep, ms, err, bytes, sha, truncated}
  const engineInfo = {};

  const pages = {};
  for (const e of engines) {
    const page = await browser.newPage();
    page.on('pageerror', err => console.error('PAGEERROR', e.name, err.message));
    await page.goto(`http://127.0.0.1:${port}/page/${e.name}`, { waitUntil: 'load' });
    await page.waitForFunction('window.__ready && window.__render', null, { timeout: 120000 });
    pages[e.name] = page;
    engineInfo[e.name] = {
      path: path.join(e.dir, e.file),
      sizeBytes: fs.statSync(path.join(e.dir, e.file)).size,
      importMs: await page.evaluate('window.__importMs'),
    };
  }

  const sources = {};
  for (const f of files) sources[f] = fs.readFileSync(path.join(corpusDir, f), 'utf8').replace(/\r\n/g, '\n').split('\n');

  // Engines alternate per rep (T,R then R,T) so paired samples are adjacent in time: linear AND
  // convex drift over the session (JIT tiering, thermal) hits both engines equally and cancels in
  // the ratio. Engine-level blocks measured a consistent +5..10% bias on an A/A run; this removed it.
  for (let block = 0; block < opt.blocks; block++) {
    for (const f of files) {
      for (let rep = 0; rep < opt.reps; rep++) {
        const order = (rep + block) % 2 === 0 ? engines : [...engines].reverse();
        for (const e of order) {
          const r = await renderOnce(pages[e.name], sources[f]);
          reps.push({ engine: e.name, diagram: f, block, rep, ...r });
        }
      }
    }
  }

  const browserVersion = browser.version();
  await browser.close(); server.close();

  // Aggregate: per (engine, logical row). small/ files fold into one row.
  const rowOf = f => f.startsWith('small/') ? 'small-30' : f.replace(/\.puml$/, '');
  const rows = [...new Set(files.map(rowOf))];
  const agg = {}; // row -> engine -> {ms, sha, truncated, bytes}
  for (const row of rows) {
    agg[row] = {};
    for (const e of engines) {
      const mine = reps.filter(r => r.engine === e.name && rowOf(r.diagram) === row && r.rep > 0 && !r.err);
      const errs = reps.filter(r => r.engine === e.name && rowOf(r.diagram) === row && r.err);
      let med, lo, hi;
      if (row === 'small-30') {
        // sum of per-file pooled medians = total warm cost of the set
        const perFile = [...new Set(mine.map(r => r.diagram))].map(d => median(mine.filter(r => r.diagram === d).map(r => r.ms)));
        med = perFile.reduce((a, b) => a + b, 0); lo = hi = null;
      } else {
        const v = mine.map(r => r.ms);
        med = median(v); [lo, hi] = iqr(v);
      }
      agg[row][e.name] = {
        medianMs: med, iqr: [lo, hi],
        sha8: mine.length && row !== 'small-30' ? mine[mine.length - 1].sha.slice(0, 16) : null,
        truncated: mine.some(r => r.truncated),
        err: errs.length ? errs[0].err : null,
        svgBytes: row !== 'small-30' && mine.length ? mine[mine.length - 1].bytes : null,
      };
    }
  }

  let bands = {};
  try { bands = JSON.parse(fs.readFileSync(path.join(HERE, 'expected-bands.json'), 'utf8')); } catch (e) { /* optional */ }

  const hasRef = engines.length === 2;
  const lines = [];
  lines.push('| diagram | target ms (IQR) | ' + (hasRef ? 'reference ms (IQR) | ratio | band | ' : '') + 'output |');
  lines.push('|---|---|' + (hasRef ? '---|---|---|' : '') + '---|');
  for (const row of rows) {
    const t = agg[row].target, r = hasRef ? agg[row].reference : null;
    const fmt = x => x.err ? (x.err.includes('too large') ? 'size-limited (no maxSvgSize)' : 'ERROR')
      : (x.medianMs === null ? '-' : `${x.medianMs}${x.iqr[0] !== null ? ` (${x.iqr[0]}-${x.iqr[1]})` : ''}${x.truncated ? ' (truncated)' : ''}`);
    let ratio = '', band = '', output = '';
    if (hasRef && !t.err && !r.err && t.medianMs && r.medianMs) {
      const q = t.medianMs / r.medianMs;
      ratio = q.toFixed(2);
      const b = bands[row];
      if (t.truncated || r.truncated) band = 'n/a (truncated)';
      else if (b) band = Math.abs(q - b.ratio) <= b.tol ? 'OK'
        : (q > b.ratio ? `SLOWER than band by ${((q - b.ratio - b.tol) * 100).toFixed(0)}pp` : `faster than band by ${((b.ratio - b.tol - q) * 100).toFixed(0)}pp`);
      else band = 'no band';
    }
    if (t.sha8) {
      output = '`' + t.sha8 + '`';
      if (hasRef && r.sha8) output += t.sha8 === r.sha8 ? ' = ref' : ' != ref';
    }
    lines.push(`| ${row} | ${fmt(t)} | ` + (hasRef ? `${fmt(r)} | ${ratio} | ${band} | ` : '') + `${output} |`);
  }
  lines.push('');
  for (const e of engines)
    lines.push(`- ${e.name}: ${(engineInfo[e.name].sizeBytes / 1048576).toFixed(2)} MB, module import ${engineInfo[e.name].importMs} ms (${engineInfo[e.name].path})`);
  lines.push(`- reps ${opt.reps} x blocks ${opt.blocks} (rep 0 per block discarded), maxSvgSize ${opt.maxsvg}`);
  lines.push(`- ${os.cpus()[0].model} (${os.cpus().length} cores), node ${process.versions.node}, chromium ${browserVersion}`);

  const outDir = path.resolve(opt.out);
  fs.mkdirSync(outDir, { recursive: true });
  fs.writeFileSync(path.join(outDir, 'results.json'), JSON.stringify({ opt, engines: engineInfo, reps, env: { cpu: os.cpus()[0].model, cores: os.cpus().length, node: process.versions.node, chromium: browserVersion, platform: os.platform() } }, null, 1));
  fs.writeFileSync(path.join(outDir, 'summary.md'), lines.join('\n') + '\n');
  console.log(lines.join('\n'));
  process.exit(0); // non-blocking by design: results are informational
})().catch(e => { console.error('FATAL', e && e.stack || e); process.exit(1); });
