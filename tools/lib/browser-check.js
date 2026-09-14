'use strict';

const fs = require('fs');

function isErrorImage(svg) {
  return !!svg && svg.includes('#33FF02') && svg.includes('#FF0000');
}

function createCheckReporter() {
  let failures = 0;
  let checks = [];

  function check(label, ok, detail) {
    console.log(`${ok ? 'PASS' : 'FAIL'}  ${label}${ok || !detail ? '' : '\n        ' + detail}`);
    checks.push({ label, ok, detail });
    if (!ok)
      failures++;
  }

  function generateMarkdownSummary(name) {
    const statusText = failures === 0 ? '✅ All checks passed for:' : `❌ ${failures} check(s) failed for:`;
    
    let summary = `<details>\n`;
    summary += `<summary>${statusText} <strong>${name}</strong></summary>\n\n`;
    
    checks.forEach(({ label, ok, detail }) => {
      summary += `1. ${ok ? '✅' : '❌'} ${label}`;
      if (!ok && detail) {
        console.log(!ok && detail);
        summary += `\n   \`\`\`\n   ${detail}\n   \`\`\``;
      }
      summary += '\n';
    });
    
    summary += `</details>\n`;
    
    return summary;
  }

  function finish(name, options) {
    const testName = name || '';
    const opt = options || {};
    const okText = opt.uppercase ? 'ALL CHECKS PASSED' : 'all checks passed';
    const failText = opt.uppercase ? 'CHECK(S) FAILED' : 'check(s) failed';
    console.log((opt.leadingBlankLine ? '\n' : '') + (failures === 0 ? '✅ ' + okText : '❌ ' + failures + ' ' + failText) + '\n');
    
    // Export to GITHUB_STEP_SUMMARY if available
    const summaryFile = process.env.GITHUB_STEP_SUMMARY;
    if (summaryFile) {
      const markdown = generateMarkdownSummary(testName);
      fs.appendFileSync(summaryFile, markdown + '\n');
    }
    
    process.exit(failures === 0 ? 0 : 1);
  }

  return {
    check,
    finish,
    getFailures: () => failures,
    generateMarkdownSummary,
  };
}

module.exports = {
  createCheckReporter,
  isErrorImage,
};