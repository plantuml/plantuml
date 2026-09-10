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

  return {
    check,
    getFailures: () => failures,
  };
}

module.exports = {
  createCheckReporter,
  isErrorImage,
};
