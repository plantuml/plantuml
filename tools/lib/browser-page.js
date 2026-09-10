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

function delay(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

module.exports = {
  createModulePageHtml,
  delay,
  loadPlaywright,
  maybeScriptTag,
  openReadyPage,
  shortPageError,
};
