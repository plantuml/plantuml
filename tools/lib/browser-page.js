'use strict';

const fs = require('fs');
const path = require('path');

function loadPlaywright() {
  if (process.env.BENCH_PW)
    return require(process.env.BENCH_PW);

  try {
    return require('playwright');
  } catch (e) {
    const resolved = require.resolve('playwright', {
      paths: [
        path.join(__dirname, '..', 'browser-test'),
        path.join(__dirname, '..', 'perf-bench'),
      ],
    });
    return require(resolved);
  }
}

function maybeScriptTag(dir, fileName, requestPath) {
  return fs.existsSync(path.join(dir, fileName)) ? `<script src="${requestPath || '/' + fileName}"></script>` : '';
}

function createModulePageHtml(options) {
  return `<!doctype html><html><head>${options.headHtml || ''}</head><body><div id="out"></div>
${options.bodyHtml || ''}
<script type="module">
import {render} from '${options.modulePath}';
${options.moduleBody}
</script></body></html>`;
}

function makeRenderModuleBody(options) {
  const opt = options || {};
  const baseOptions = {};
  if (typeof opt.maxSvgSize !== 'undefined')
    baseOptions.maxSvgSize = opt.maxSvgSize;
  const baseOptionsJson = JSON.stringify(baseOptions);
  if (opt.allowOverrides) {
    return `window.__render=(lines,id,opts)=>render(lines,id,Object.assign(${baseOptionsJson},opts||{}));
window.__ready=1;`;
  }
  return `window.__render=(lines,id)=>render(lines,id,${baseOptionsJson});
window.__ready=1;`;
}

function shortPageError(err) {
  return String(err && err.message || err).split('\n')[0];
}

async function openReadyPage(browser, url, options) {
  const opt = options || {};
  const page = await browser.newPage();
  const errors = [];
  try {
    if (opt.trackErrors !== false)
      page.on('pageerror', err => errors.push(shortPageError(err)));
    if (typeof opt.onConsole === 'function')
      page.on('console', opt.onConsole);
    if (typeof opt.beforeGoto === 'function')
      await opt.beforeGoto(page);
    await page.goto(url, { waitUntil: opt.waitUntil || 'load' });
    await page.waitForFunction(opt.readyExpression || 'window.__ready && window.__render', null, {
      timeout: opt.timeout || 120000,
      polling: opt.polling,
    });
  } catch (e) {
    await page.close().catch(() => {});
    throw e;
  }
  return { page, errors };
}

async function renderOn(page, lines, options) {
  const opt = options || {};
  const timeoutMs = opt.timeoutMs || 30000;
  const targetId = opt.targetId || 'out';
  return page.evaluate(async ({ lines, timeoutMs, targetId, renderOptions, includeTiming, includeHash, includeTruncation, includeLoaderCalls, includeShapeCounts, includeWasmCount, maxTextLength }) => {
    const out = document.getElementById(targetId);
    out.innerHTML = '';
    const t0 = includeTiming ? performance.now() : 0;
    const w0 = includeWasmCount && typeof window.__wasm === 'number' ? window.__wasm : null;
    const done = new Promise(res => {
      const mo = new MutationObserver(() => {
        if (out.querySelector('svg') || out.textContent) {
          mo.disconnect();
          res();
        }
      });
      mo.observe(out, { childList: true, subtree: true });
    });
    let thrown = null;
    try {
      window.__render(lines, targetId, renderOptions);
    } catch (e) {
      thrown = String((e && e.message) || e);
    }
    if (!thrown)
      await Promise.race([done, new Promise(r => setTimeout(r, timeoutMs))]);
    const svg = out.querySelector('svg');
    const svgHtml = svg ? svg.outerHTML : null;
    let sha = null;
    if (includeHash && svgHtml) {
      const buf = await crypto.subtle.digest('SHA-256', new TextEncoder().encode(svgHtml));
      sha = [...new Uint8Array(buf)].map(b => b.toString(16).padStart(2, '0')).join('');
    }
    let text = out.textContent || '';
    if (maxTextLength && text.length > maxTextLength)
      text = text.slice(0, maxTextLength);
    const wasmNow = includeWasmCount && typeof window.__wasm === 'number' ? window.__wasm : null;
    return {
      err: thrown,
      thrown,
      svg: svgHtml,
      text,
      wasm: w0 === null || wasmNow === null ? null : wasmNow - w0,
      shapes: includeShapeCounts && svg ? svg.querySelectorAll('path,polygon,line,rect,ellipse').length : 0,
      texts: includeShapeCounts && svg ? svg.querySelectorAll('text').length : 0,
      loaderCalls: includeLoaderCalls && window.__loaderCalls ? window.__loaderCalls.slice() : null,
      ms: includeTiming ? Math.round(performance.now() - t0) : null,
      bytes: svgHtml ? svgHtml.length : 0,
      sha,
      truncated: includeTruncation && svgHtml ? svgHtml.includes('(max ') : false,
    };
  }, {
    lines,
    timeoutMs,
    targetId,
    renderOptions: opt.renderOptions,
    includeTiming: opt.includeTiming === true,
    includeHash: opt.includeHash === true,
    includeTruncation: opt.includeTruncation === true,
    includeLoaderCalls: opt.includeLoaderCalls === true,
    includeShapeCounts: opt.includeShapeCounts === true,
    includeWasmCount: opt.includeWasmCount === true,
    maxTextLength: opt.maxTextLength || 0,
  });
}

function makeRenderer(page, options) {
  const base = Object.assign({}, options || {});
  return async function renderer(lines, renderOptions) {
    const runOptions = Object.assign({}, base);
    if (arguments.length >= 2)
      runOptions.renderOptions = renderOptions;
    return renderOn(page, lines, runOptions);
  };
}

async function openRenderer(browser, url, openOptions, renderOptions) {
  const ready = await openReadyPage(browser, url, openOptions);
  const renderer = makeRenderer(ready.page, renderOptions);
  renderer.page = ready.page;
  renderer.errors = ready.errors;
  return renderer;
}

function delay(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

module.exports = {
  createModulePageHtml,
  delay,
  loadPlaywright,
  makeRenderer,
  makeRenderModuleBody,
  maybeScriptTag,
  openRenderer,
  openReadyPage,
  renderOn,
  shortPageError,
};
