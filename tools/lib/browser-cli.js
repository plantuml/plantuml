'use strict';

const path = require('path');

function resolveEngineSpec(spec) {
  let dir = spec;
  let file = 'plantuml.js';
  if (dir.endsWith('.js')) {
    file = path.basename(dir);
    dir = path.dirname(dir);
  }
  return { dir: path.resolve(dir), file };
}

function parseTargetArg(argv, usage) {
  if (argv.length <= 2) {
    console.error('usage: ' + usage);
    process.exit(2);
  }
  let target = null;
  for (let i = 2; i < argv.length; i++) {
    const m = argv[i].match(/^target=(.+)$/);
    if (!m) {
      console.error('bad arg: ' + argv[i]);
      process.exit(2);
    }
    target = m[1];
  }
  if (!target) {
    console.error('usage: ' + usage);
    process.exit(2);
  }
  return resolveEngineSpec(target);
}

function parseNamedEnginesAndOptions(argv, defaults) {
  const engines = [];
  const options = Object.assign({}, defaults);
  for (let i = 2; i < argv.length; i++) {
    const arg = argv[i];
    const flag = arg.match(/^--(\w+)$/);
    if (flag) {
      if (i + 1 >= argv.length) {
        console.error('missing value for ' + arg);
        process.exit(2);
      }
      options[flag[1]] = argv[++i];
      continue;
    }
    const m = arg.match(/^(\w+)=(.+)$/);
    if (!m) {
      console.error('bad arg: ' + arg);
      process.exit(2);
    }
    const resolved = resolveEngineSpec(m[2]);
    engines.push({ name: m[1], dir: resolved.dir, file: resolved.file });
  }
  return { engines, options };
}

module.exports = {
  parseNamedEnginesAndOptions,
  parseTargetArg,
  resolveEngineSpec,
};
