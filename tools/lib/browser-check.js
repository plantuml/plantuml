'use strict';

function isErrorImage(svg) {
  return !!svg && svg.includes('#33FF02') && svg.includes('#FF0000');
}

function createCheckReporter() {
  let failures = 0;

  function check(label, ok, detail) {
    console.log(`${ok ? 'PASS' : 'FAIL'}  ${label}${ok || !detail ? '' : '\n        ' + detail}`);
    if (!ok)
      failures++;
  }

  function finish(options) {
    const opt = options || {};
    const okText = opt.uppercase ? 'ALL CHECKS PASSED' : 'all checks passed';
    const failText = opt.uppercase ? 'CHECK(S) FAILED' : 'check(s) failed';
    console.log((opt.leadingBlankLine ? '\n' : '') + (failures === 0 ? okText : failures + ' ' + failText));
    process.exit(failures === 0 ? 0 : 1);
  }

  return {
    check,
    finish,
    getFailures: () => failures,
  };
}

module.exports = {
  createCheckReporter,
  isErrorImage,
};
