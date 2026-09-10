'use strict';

const fs = require('fs');
const path = require('path');

function loadPlaywright() {
  return require(process.env.BENCH_PW || 'playwright');
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
